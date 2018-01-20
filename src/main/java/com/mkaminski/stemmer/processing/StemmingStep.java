package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import com.sun.deploy.util.StringUtils;
import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * @author Mateusz Kamiński
 */
public class StemmingStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) {
        PatriciaTrie<String> dictionary = (PatriciaTrie<String>) processingContext.getDictionary();
        Scanner scanner = processingContext.getFileScanner();
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
        String trimmedToken = StringUtils.trimWhitespace(token);
        if (dictionary.containsKey(trimmedToken)) {
            return dictionary.get(trimmedToken);
        }
        if (trimmedToken.length() < 2) {
            return trimmedToken;
        }
        if (dictionary.containsKey(trimmedToken.toLowerCase())) {
            return dictionary.get(trimmedToken.toLowerCase());
        }
        return stemStringToken(dictionary, trimmedToken.substring(0, trimmedToken.length() - 1));
    }

    @Override
    public List<String> inCommands() {
        return Collections.singletonList("stem");
    }
}
