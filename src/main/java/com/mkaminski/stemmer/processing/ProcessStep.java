package com.mkaminski.stemmer.processing;

import java.util.List;

/**
 * @author Mateusz Kamiński
 */
public interface ProcessStep {

    void makeProcess(ProcessingContext processingContext);

    default List<String> inCommands() {
        return List.of("stem", "convert");
    }
}
