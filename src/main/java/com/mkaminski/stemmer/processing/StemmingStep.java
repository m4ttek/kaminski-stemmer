package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * @author Mateusz Kamiński
 */
public class StemmingStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) {
        PatriciaTrie<String> dictionary = (PatriciaTrie<String>) processingContext.getDictionary();
        Scanner scanner = processingContext.getFileScanner();
        if (scanner == null || dictionary == null) {
            return;
        }
        try (OutputStreamWriter outputStreamWriter = new OutputStreamWriter(processingContext.getDest())) {
            while (scanner.hasNext()) {
                String token = scanner.next();
                outputStreamWriter
                        .append(stemStringToken(dictionary, token))
                        .append(" ");
            }
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
    }

    private String stemStringToken(PatriciaTrie<String> dictionary, String token) {
        if (token == null) {
            return "";
        }
        String trimmedToken = token.trim().replaceAll("\\p{Punct}", "");
        if (dictionary.containsKey(trimmedToken)) {
            return dictionary.get(trimmedToken);
        }
        if (trimmedToken.length() < 2) {
            return trimmedToken;
        }

        if (dictionary.containsKey(trimmedToken.toLowerCase())) {
            return dictionary.get(trimmedToken.toLowerCase());
        }
        return trimmedToken;
    }

    @Override
    public List<String> inCommands() {
        return Collections.singletonList("stem");
    }
}
