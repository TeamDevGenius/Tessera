package com.tessera.engine.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * File dialog utility.
 * On desktop, delegates to java.awt.FileDialog via reflection.
 * On Android (no AWT), the callback receives null immediately.
 */
public class FileDialog {

    /** Platform-neutral wrapper around java.awt.FileDialog. */
    public static final class Wrapper {
        public static final int LOAD = 0;
        public static final int SAVE = 1;

        private final Object awtFd; // java.awt.FileDialog instance, or null on Android
        private String directory;
        private String file = "*";
        private int mode = LOAD;
        private FilenameFilter filter;

        Wrapper(Object awtFd) { this.awtFd = awtFd; }

        public void setDirectory(String dir) {
            this.directory = dir;
            invoke("setDirectory", String.class, dir);
        }

        public void setFile(String f) {
            this.file = f;
            invoke("setFile", String.class, f);
        }

        public void setMode(int m) {
            this.mode = m;
            invoke("setMode", int.class, m);
        }

        public void setFilenameFilter(FilenameFilter f) {
            this.filter = f;
            try {
                if (awtFd != null) {
                    Method m = awtFd.getClass().getMethod("setFilenameFilter", FilenameFilter.class);
                    m.invoke(awtFd, f);
                }
            } catch (Throwable ignored) {}
        }

        private void invoke(String name, Class<?> paramType, Object value) {
            try {
                if (awtFd != null) {
                    Method m = awtFd.getClass().getMethod(name, paramType);
                    m.invoke(awtFd, value);
                }
            } catch (Throwable ignored) {}
        }
    }

    public static void fileDialog(Consumer<Wrapper> setupConsumer, Consumer<File> chosenFile) {
        (new Thread(() -> {
            File result = null;
            try {
                Class<?> jFrameClass = Class.forName("javax.swing.JFrame");
                Class<?> fdClass = Class.forName("java.awt.FileDialog");
                Object frame = jFrameClass.getDeclaredConstructor().newInstance();
                Object fd = fdClass.getDeclaredConstructor(
                        Class.forName("java.awt.Frame"), String.class, int.class)
                        .newInstance(frame, "Choose a file", fdClass.getField("LOAD").getInt(null));

                Wrapper wrapper = new Wrapper(fd);
                if (setupConsumer != null) setupConsumer.accept(wrapper);

                // Apply mode (LOAD/SAVE mapped to AWT constants 0/1)
                fdClass.getMethod("setMode", int.class).invoke(fd, wrapper.mode);

                fdClass.getMethod("setVisible", boolean.class).invoke(fd, true);
                jFrameClass.getMethod("toFront").invoke(frame);
                jFrameClass.getMethod("setAlwaysOnTop", boolean.class).invoke(frame, true);
                String dir = (String) fdClass.getMethod("getDirectory").invoke(fd);
                String file = (String) fdClass.getMethod("getFile").invoke(fd);
                if (dir != null && file != null) result = new File(dir, file);
                jFrameClass.getMethod("dispose").invoke(frame);
            } catch (Throwable ignored) {
                // AWT not available (Android) — result stays null
            }
            chosenFile.accept(result);
        })).start();
    }

    private static class FileWrapper { public File file; }

    public static File fileDialog(Consumer<Wrapper> setupConsumer) {
        AtomicBoolean done = new AtomicBoolean(false);
        FileWrapper file = new FileWrapper();
        fileDialog(setupConsumer, f -> { done.set(true); file.file = f; });
        while (!done.get()) {
            try { Thread.sleep(10); } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); break;
            }
        }
        return file.file;
    }
}
