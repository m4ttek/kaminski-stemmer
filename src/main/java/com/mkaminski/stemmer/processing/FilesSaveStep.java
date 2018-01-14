package com.mkaminski.stemmer.processing;

import java.io.IOException;

/**
 * @author Mateusz Kamiński
 */
public class FilesSaveStep implements ProcessStep {
    @Override
    public void makeProcess(ProcessingContext processingContext) {
        try {
            if (processingContext.getDictSource() != null) {
                processingContext.getDictSource().close();
            }
            if (processingContext.getSource() != null) {
                processingContext.getSource().close();
            }
            if (processingContext.getDest() != null) {
                processingContext.getDest().close();
            }
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

}
