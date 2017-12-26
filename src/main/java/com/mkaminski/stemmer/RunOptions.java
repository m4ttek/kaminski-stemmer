package com.mkaminski.stemmer;

import java.nio.file.Path;

/**
 * @author Mateusz Kamiński
 */
public class RunOptions {

    private final String mainCommand;
    private final Path sourcePath;
    private final Path resultPath;
    private final Path dictPath;

    public RunOptions(String mainCommand, Path sourcePath, Path resultPath, Path dictPath) {
        this.mainCommand = mainCommand;
        this.sourcePath = sourcePath;
        this.resultPath = resultPath;
        this.dictPath = dictPath;
    }

    public String getMainCommand() {
        return mainCommand;
    }

    public Path getSourcePath() {
        return sourcePath;
    }

    public Path getResultPath() {
        return resultPath;
    }

    public Path getDictPath() {
        return dictPath;
    }
}
