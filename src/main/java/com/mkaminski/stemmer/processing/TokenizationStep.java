package com.mkaminski.stemmer.processing;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class TokenizationStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) {
        InputStream source = processingContext.getSource();
        if (source != null) {
            Scanner scanner = new Scanner(source).useDelimiter("[\\s]+");
            processingContext.setFileScanner(scanner);
        }
    }

    @Override
    public List<String> inCommands() {
        return Collections.singletonList("stem");
    }
}
