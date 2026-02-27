package com.tessera.engine.utils;

import com.tessera.engine.client.ClientWindow;
import com.tessera.engine.utils.resource.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * @author zipCoder933
 */
public class ErrorHandler {

    /** Shows a popup message; on Android (no Swing) falls back to ClientWindow.popupMessage. */
    public static void createPopupWindow(String title, String str) {
        // Attempt Swing popup reflectively for desktop; silently fall through on Android
        try {
            Class<?> jFrameClass = Class.forName("javax.swing.JFrame");
            Class<?> jLabelClass = Class.forName("javax.swing.JLabel");
            Object frame = jFrameClass.getDeclaredConstructor().newInstance();
            Object label = jLabelClass.getDeclaredConstructor().newInstance();
            jLabelClass.getMethod("setText", String.class).invoke(label,
                    "<html><body style='padding:5px;'>" + str.replace("\n", "<br>") + "</body></html>");
            jFrameClass.getMethod("add", java.awt.Component.class).invoke(frame, label);
            jFrameClass.getMethod("setTitle", String.class).invoke(frame, title);
            jFrameClass.getMethod("setSize", int.class, int.class).invoke(frame, 380, 240);
            jFrameClass.getMethod("setAlwaysOnTop", boolean.class).invoke(frame, true);
            jFrameClass.getMethod("setLocationRelativeTo", java.awt.Component.class).invoke(frame, (Object) null);
            jFrameClass.getMethod("setVisible", boolean.class).invoke(frame, true);
            return;
        } catch (Throwable ignored) {}
        // Android / headless fallback
        System.err.println("[ErrorHandler] " + title + ": " + str);
        try {
            if (ClientWindow.popupMessage != null) {
                ClientWindow.popupMessage.message(title, str);
            }
        } catch (Throwable ignored) {}
    }

    private static final String localDir = new File("").getAbsolutePath();
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss");

    public static void report(Throwable ex) {
        report("error", ex);
    }

    public static void report(String title, String details) {
        if (title == null || title.isBlank()) title = "Error";
        try {
            ClientWindow.popupMessage.message(title, details);
        } catch (Throwable ignored) {
            System.err.println("[ErrorHandler] " + title + ": " + details);
        }
    }

    public static void report(String userMsg, Throwable ex) {
        String errMessage = (ex.getMessage() != null ? " \n(" + ex.getMessage() + ")" : "");
        if (userMsg == null || userMsg.isBlank()) userMsg = "Runtime Error!";
        try {
            ClientWindow.popupMessage.message(userMsg, errMessage + "\n(Content saved to clipboard)");
        } catch (Throwable ignored) {
            System.err.println("[ErrorHandler] " + userMsg + errMessage);
        }
        log(ex, userMsg);
    }

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
                saveLogToFile(devMessage, errorStr);
                MiscUtils.setClipboard(errorStr);
            } catch (IOException ex1) {
                // ignore log-write failure
            }
        }
    }

    private static File saveLogToFile(String devMessage, String errorStr) throws IOException {
        String date = dateFormat.format(new Date()).replace(":", "_");
        File logFile = new File(ResourceUtils.LOGS_DIR, date + ".txt");
        if (!logFile.getParentFile().exists()) logFile.getParentFile().mkdirs();
        if (devMessage != null) errorStr = "Message: \t" + devMessage + "\n" + errorStr;
        Files.writeString(logFile.toPath(), errorStr);
        return logFile;
    }

    public static void log(Throwable throwable) {
        log(throwable, "unnamed exception");
    }
}
