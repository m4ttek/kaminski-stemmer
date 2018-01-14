package com.mkaminski.stemmer.processing;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.StreamTokenizer;
import java.util.Map;
import java.util.Scanner;

import com.mkaminski.stemmer.RunOptions;

/**
 * @author Mateusz Kamiński
 */
public class ProcessingContext {

    private RunOptions runOptions;
    private InputStream source;
    private OutputStream dest;
    private InputStream dictSource;
    private Map<String, String> dictionary;
    private Scanner fileScanner;

    public void setRunOptions(RunOptions runOptions) {
        this.runOptions = runOptions;
    }

    public void setSource(InputStream source) {
        this.source = source;
    }

    public void setDest(OutputStream dest) {
        this.dest = dest;
    }

    public void setDictSource(InputStream dictSource) {
        this.dictSource = dictSource;
    }

    public RunOptions getRunOptions() {
        return runOptions;
    }

    public InputStream getSource() {
        return source;
    }

    public OutputStream getDest() {
        return dest;
    }

    public InputStream getDictSource() {
        return dictSource;
    }

    public Map<String, String> getDictionary() {
        return dictionary;
    }

    public void setDictionary(Map<String, String> dictionary) {
        this.dictionary = dictionary;
    }

    public Scanner getFileScanner() {
        return fileScanner;
    }

    public void setFileScanner(Scanner fileScanner) {
        this.fileScanner = fileScanner;
    }
}
