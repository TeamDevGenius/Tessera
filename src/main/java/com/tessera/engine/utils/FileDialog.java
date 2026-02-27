package com.tessera.engine.utils;

import java.io.File;
import java.util.function.Consumer;

/** File dialog - desktop only. No-op on Android. */
public class FileDialog {
    public static void fileDialog(Consumer<Object> setupConsumer, Consumer<File> chosenFile) {
        chosenFile.accept(null);
    }

    public static File fileDialog(Consumer<Object> setupConsumer) {
        return null;
    }
}
