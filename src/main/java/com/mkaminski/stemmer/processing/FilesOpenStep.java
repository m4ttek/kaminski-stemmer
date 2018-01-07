package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.nio.file.Files;

import com.mkaminski.stemmer.RunOptions;

/**
 * @author Mateusz Kamiński
 */
public class FilesOpenStep implements ProcessStep {
    @Override
    public void makeProcess(ProcessingContext processingContext) throws IOException {
        RunOptions runOptions = processingContext.getRunOptions();
        if (Files.notExists(runOptions.getSourcePath())) {
            throw new IOException("source file does not exist!");
        }
        processingContext.setSource(Files.newInputStream(runOptions.getSourcePath()));
        if (Files.exists(runOptions.getResultPath())) {
            throw new IOException("destination file already exists!");
        }
        processingContext.setDest(Files.newOutputStream(runOptions.getResultPath()));

        processingContext.setDictSource(Files.newInputStream(runOptions.getSourcePath()));
    }

}
