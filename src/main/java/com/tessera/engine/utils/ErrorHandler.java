/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.tessera.engine.utils;

import com.badlogic.gdx.Gdx;
import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.utils.resource.ResourceUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author zipCoder933
 */
public class ErrorHandler {

    public static void createPopupWindow(String title, String str) {
        final JFrame parent = new JFrame();
        JLabel label = new JLabel("");
        label.setText("<html><body style='padding:5px;'>" + str.replace("\n", "<br>") + "</body></html>");
//        label.setFont(new Font("Arial", 0, 12));
        label.setVerticalAlignment(JLabel.TOP);
        parent.add(label);
        parent.pack();
        parent.getContentPane().setBackground(Color.white);
        parent.setVisible(true);
        parent.pack();

        parent.setIconImage(popupWindowIcon.getImage());
        parent.setTitle(title);
        parent.setLocationRelativeTo(null);
        parent.setAlwaysOnTop(true);
        parent.setVisible(true);
        parent.setSize(380, 240);
    }

    private static ImageIcon popupWindowIcon;
    static {
        try {
            popupWindowIcon = new ImageIcon(ResourceUtils.file("logo.png").getAbsolutePath());
        } catch (Throwable ignored) {
            System.out.println("ErrorHandler: could not load popup icon (Swing/resource unavailable)");
        }
    }
    private static final String localDir = new File("").getAbsolutePath();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");

    public static void report(Throwable ex) {
        report("error", ex);
    }

    public static void report(String title, String details) {
        if (title == null || title.isBlank()) title = "Error";
        if (ClientWindow.popupMessage != null) {
            ClientWindow.popupMessage.message(title, details);
        } else {
            String msg = title + ": " + details;
            System.err.println(msg);
            try { Gdx.app.error("Tessera", msg); } catch (Throwable ignored) {}
        }
    }

    public static void report(String userMsg, Throwable ex) {
        String errMessage = (ex.getMessage() != null ? " \n(" + ex.getMessage() + ")" : "");
        if (userMsg == null || userMsg.isBlank()) userMsg = "Runtime Error!";

        if (ClientWindow.popupMessage != null) {
            ClientWindow.popupMessage.message(userMsg, errMessage + "\n(Content saved to clipboard)");
        } else {
            System.err.println(userMsg + errMessage);
            try { Gdx.app.error("Tessera", userMsg + errMessage, ex); } catch (Throwable ignored) {}
        }
        log(ex, userMsg);
    }


    /**
     * Prints the stack trace and saves the error to log file.
     *
     * @param ex         the throwable
     * @param devMessage
     */
    public static void log(Throwable ex, String devMessage) {
        String errorStr;
        if (ex != null) {
            if (ex.getMessage() == null) {
                errorStr = "Message: \t" + devMessage + "\n"
                        + "Class: \t" + ex.getClass() + "\n\n"
                        + "Stack trace:\n" + Arrays.toString(ex.getStackTrace()).replace(",", "\n");
            } else {
                errorStr = "Message: \t" + devMessage + "\n"
                        + "Error: \t" + ex.getMessage() + "\n"
                        + "Class: \t" + ex.getClass() + "\n\n"
                        + "Stack trace:\n" + Arrays.toString(ex.getStackTrace()).replace(",", "\n");
            }
            System.out.println(errorStr);
            try {
                //Create log file directory if it doesn't exist
                saveLogToFile(devMessage, errorStr);
                //Copy to clipboard
                MiscUtils.setClipboard(errorStr);
            } catch (IOException ex1) {
            }
        }
    }

    private static File saveLogToFile(String devMessage, String errorStr) throws IOException {
        String date = dateFormat.format(new Date()).replace(":", "_");
        File logFile = new File(ResourceUtils.LOGS_DIR, date + ".txt");
        if (!logFile.getParentFile().exists()) logFile.getParentFile().mkdirs();

        if (devMessage != null) errorStr = "Message: \t" + devMessage + "\n" + errorStr;
        try (FileOutputStream fos = new FileOutputStream(logFile)) {
            fos.write(errorStr.getBytes(StandardCharsets.UTF_8));
        }
        return logFile;
    }

    public static void log(Throwable throwable) {
        log(throwable, "unnamed exception");
    }
}
