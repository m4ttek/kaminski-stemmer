package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.util.List;

/**
 * @author Mateusz Kamiński
 */
public interface ProcessStep {

    void makeProcess(ProcessingContext processingContext) throws IOException;

    default List<String> inCommands() {
        return List.of("stem", "convert");
    }
}
