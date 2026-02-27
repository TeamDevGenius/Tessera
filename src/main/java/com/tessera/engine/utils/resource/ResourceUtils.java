/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tessera.engine.utils.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author zipCoder933
 */
public class ResourceUtils {

    //files
    public static File RESOURCE_DIR;
    public static File APP_DATA_DIR;
    public static File WORLDS_DIR;
    public static File LOCAL_DIR;
    public static File LOGS_DIR;

    //Individual paths
    public static File BLOCK_ICON_DIR,PLAYER_GLOBAL_INFO;


    static {
        System.out.println("RESOURCES:");
        LOCAL_DIR = new File(System.getProperty("user.dir", "."));
        LOGS_DIR = new File(LOCAL_DIR, "logs");
        RESOURCE_DIR = new File(LOCAL_DIR, "res");
        // Note: do NOT call mkdirs() here — on Android, LOCAL_DIR may not be writable.
        // mkdirs() is called in initialize() once we have a writable path.

        System.out.println("\tLocal path: " + LOCAL_DIR);
        System.out.println("\tResource path: " + RESOURCE_DIR);

        BLOCK_ICON_DIR = file("items\\blocks\\icons");
    }
    public static void initialize(boolean gameDevResources, String appDataDir) {
        String appBase;
        if (System.getenv("LOCALAPPDATA") != null) {
            appBase = System.getenv("LOCALAPPDATA");
        } else if (Gdx.files != null) {
            appBase = Gdx.files.getLocalStoragePath();
        } else {
            appBase = System.getProperty("user.home", ".");
        }
        APP_DATA_DIR = new File(appBase, appDataDir == null ? "tessera" : appDataDir);
        APP_DATA_DIR.mkdirs();
        System.out.println("\tApp Data path: " + APP_DATA_DIR);

        // On Android (or any platform where Gdx.files is available), move RESOURCE_DIR
        // to writable private storage so we can extract assets into it.
        if (Gdx.files != null) {
            String localPath = Gdx.files.getLocalStoragePath();
            if (localPath != null && !localPath.isEmpty()) {
                RESOURCE_DIR = new File(localPath, "res");
            }
        }
        RESOURCE_DIR.mkdirs();
        System.out.println("\tResource path (resolved): " + RESOURCE_DIR);

        // Re-initialise BLOCK_ICON_DIR now that RESOURCE_DIR is correct
        BLOCK_ICON_DIR = file("items/blocks/icons");

        //Individual files
        PLAYER_GLOBAL_INFO = new File(APP_DATA_DIR, "player_global_info.dat");
        //Worlds
        WORLDS_DIR = new File(APP_DATA_DIR, (gameDevResources ? "game_dev" : "game"));
        WORLDS_DIR.mkdirs();
        System.out.println("\tWorlds path: " + WORLDS_DIR);
    }

    public static File localFile(String path) {
        return new File(LOCAL_DIR, path);
    }

    /**
     * Returns a File for the given resource path.
     * On Android (or whenever Gdx is available), the file is lazily extracted
     * from the APK's assets into RESOURCE_DIR so that callers using the
     * File API can read it from the local filesystem.
     */
    public static File file(String path) {
        // Normalise to forward slashes
        path = path.replace("\\", "/");
        // If already absolute and inside RESOURCE_DIR, return it directly
        if (path.startsWith(RESOURCE_DIR.getAbsolutePath())) {
            return new File(path);
        }
        // Strip any leading slash
        if (path.startsWith("/")) path = path.substring(1);
        File f = new File(RESOURCE_DIR, path);
        // Lazily extract the asset from the APK when Gdx is available and file is missing
        if (!f.exists() && Gdx.files != null) {
            try {
                FileHandle fh = Gdx.files.internal(path);
                if (fh.exists() && !fh.isDirectory()) {
                    f.getParentFile().mkdirs();
                    byte[] bytes = fh.readBytes();
                    try (FileOutputStream fos = new FileOutputStream(f)) {
                        fos.write(bytes);
                    }
                    if (Gdx.app != null)
                        Gdx.app.log("ResourceUtils", "Extracted asset: " + path + " → " + f);
                }
            } catch (Exception e) {
                if (Gdx.app != null)
                    Gdx.app.log("ResourceUtils", "Could not extract asset: " + path + " (" + e.getMessage() + ")");
            }
        }
        return f;
    }

    public static File appDataFile(String path) {
        return new File(APP_DATA_DIR, path);
    }

    public static File worldFile(String path) {
        return new File(WORLDS_DIR, path);
    }




    public static byte[] downloadFile(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(10000);  // 10 seconds
        connection.setReadTimeout(10000);     // 10 seconds

        try (InputStream inputStream = connection.getInputStream();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();
        }
    }
}
