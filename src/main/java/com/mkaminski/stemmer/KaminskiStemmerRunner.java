package com.mkaminski.stemmer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.mkaminski.stemmer.processing.DictionaryConvertStep;
import com.mkaminski.stemmer.processing.DictionaryLoadStep;
import com.mkaminski.stemmer.processing.FilesOpenStep;
import com.mkaminski.stemmer.processing.FilesSaveStep;
import com.mkaminski.stemmer.processing.ProcessStep;
import com.mkaminski.stemmer.processing.ProcessingContext;
import com.mkaminski.stemmer.processing.StemmingStep;
import com.mkaminski.stemmer.processing.TokenizationStep;

/**
 * @author Mateusz Kamiński
 */
public class KaminskiStemmerRunner {

    private RunOptions runOptions;
    private ProcessingContext processingContext;
    private List<ProcessStep> processStepList;

    public KaminskiStemmerRunner(RunOptions runOptions) {
        this.runOptions = runOptions;
        this.processingContext = new ProcessingContext();
        this.processingContext.setRunOptions(runOptions);
        this.processStepList = Arrays.asList(new FilesOpenStep(), new DictionaryConvertStep(), new TokenizationStep(),
                new DictionaryLoadStep(), new StemmingStep(), new FilesSaveStep());
    }

    public KaminskiStemmerRunner(ProcessingContext processingContext, List<ProcessStep> processSteps) {
        this.processingContext = processingContext;
        this.processStepList = processSteps;
    }

    public void run() {
        processSteps();
    }

    void processSteps() {
        processStepList
                .stream()
                .filter(processStep -> processStep.inCommands().contains(runOptions.getMainCommand()))
                .forEach(processStep -> {
                    try {
                        processStep.makeProcess(processingContext);
                    } catch (IOException e) {
                        throw new FailureException(e);
                    }
                });

    }

    public static void main(String[] args) {
        try {
            new KaminskiStemmerRunner(new OptionsGetter().parseCommands(args)).run();
        } catch (FailureException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    static class FailureException extends RuntimeException {

        public FailureException(Exception ex) {
            super(ex);
        }
    }
}
