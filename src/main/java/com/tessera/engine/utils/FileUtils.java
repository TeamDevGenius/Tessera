package com.tessera.engine.utils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;



public class FileUtils {
    public final static boolean canRecycleFiles;
    static {
        boolean canRecycle = false;
        try {
            Class<?> desktop = Class.forName("java.awt.Desktop");
            Object instance = desktop.getMethod("getDesktop").invoke(null);
            Object action = Class.forName("java.awt.Desktop$Action").getField("MOVE_TO_TRASH").get(null);
            canRecycle = (Boolean) desktop.getMethod("isSupported", Class.forName("java.awt.Desktop$Action")).invoke(instance, action);
        } catch (Throwable t) {
            canRecycle = false;
        }
        canRecycleFiles = canRecycle;
    }
    private static final Pattern FILE_EXTENSION_PATTERN = Pattern.compile(".*\\.[\\w]+$");

    public static boolean hasFileExtension(String resourcePath) {
        return FILE_EXTENSION_PATTERN.matcher(resourcePath).matches();
    }

    /**
     * Removes the base path from a full path, returning a relative path.
     * This method is platform independent and works with any file type.
     *
     * @param basePath The base path to remove.
     * @param fullPath The full file path.
     * @return The relative path with the base removed.
     * @throws IllegalArgumentException if the base path is not a prefix of the full path.
     */
    public static String removeBasePath(String basePath, String fullPath) {
        // Convert both strings to Path objects
        Path base = Paths.get(basePath).normalize();
        Path full = Paths.get(fullPath).normalize();

        if (!full.startsWith(base)) {
            throw new IllegalArgumentException("The full path does not start with the base path.");
        }

        // Use relativize to compute the relative path
        Path relative = base.relativize(full);
        return relative.toString();
    }

    public static ByteBuffer fileToByteBuffer(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(filePath);
             FileChannel fileChannel = fis.getChannel()) {

            ByteBuffer buffer = ByteBuffer.allocate((int) fileChannel.size());
            fileChannel.read(buffer);
            buffer.flip(); // Prepare for reading
            return buffer;
        }
    }

    public static void moveDirectoryToTrash(File directory) throws IOException {
        if (directory.isDirectory() && directory.exists()) {
            try {
                Class<?> desktopClass = Class.forName("java.awt.Desktop");
                if ((Boolean) desktopClass.getMethod("isDesktopSupported").invoke(null)) {
                    Object desktop = desktopClass.getMethod("getDesktop").invoke(null);
                    desktopClass.getMethod("moveToTrash", File.class).invoke(desktop, directory);
                    System.out.println("Directory moved to trash: " + directory.getAbsolutePath());
                } else {
                    System.out.println("Desktop operations are not supported on this system.");
                }
            } catch (Exception e) {
                System.out.println("Cannot move to trash: " + e.getMessage());
            }
        } else {
            System.out.println("The specified directory does not exist or is not a directory.");
        }
    }

    public static boolean fileIsInUse(File file) {
        boolean used;
        Channel channel = null;
        try {
            channel = new RandomAccessFile(file, "rw").getChannel();
            used = false;
        } catch (FileNotFoundException ex) {
            used = true;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException ex) {
                    // exception handling
                }
            }
        }
        return used;
    }

}
