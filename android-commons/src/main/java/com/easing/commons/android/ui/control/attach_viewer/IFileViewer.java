package com.easing.commons.android.ui.control.attach_viewer;

import com.easing.commons.android.io.Files;

public interface IFileViewer {

    IFileViewer defaultFileViewer = new IFileViewer() {};

    default void openTextFile(String path) {
        Files.openFile(path);
    }

    default void openImageFile(String path) {
        Files.openFile(path);
    }

    default void openAudioFile(String path) {
        Files.openFile(path);
    }

    default void openVideoFile(String path) {
        Files.openFile(path);
    }

    default void openPdfFile(String path) {
        Files.openFile(path);
    }
}
