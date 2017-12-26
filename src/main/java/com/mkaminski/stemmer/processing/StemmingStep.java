package com.mkaminski.stemmer.processing;

import java.util.List;

/**
 * @author Mateusz Kamiński
 */
public class StemmingStep implements ProcessStep {
    @Override
    public void makeProcess(ProcessingContext processingContext) {

    }

    @Override
    public List<String> inCommands() {
        return List.of("stem");
    }
}
