package com.mkaminski.stemmer;

import java.nio.file.Path;

public class RunOptionsBuilder {
    private String mainCommand;
    private Path sourcePath;
    private Path resultPath;
    private Path dictPath;

    public RunOptionsBuilder setMainCommand(String mainCommand) {
        this.mainCommand = mainCommand;
        return this;
    }

    public RunOptionsBuilder setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

    public RunOptionsBuilder setResultPath(Path resultPath) {
        this.resultPath = resultPath;
        return this;
    }

    public RunOptionsBuilder setDictPath(Path dictPath) {
        this.dictPath = dictPath;
        return this;
    }

    public RunOptions createRunOptions() {
        return new RunOptions(mainCommand, sourcePath, resultPath, dictPath);
    }
}