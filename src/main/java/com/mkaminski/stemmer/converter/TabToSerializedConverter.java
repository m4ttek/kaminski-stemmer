package com.mkaminski.stemmer.converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.Buffer;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

/**
 * @author Mateusz Kamiński
 */
public class TabToSerializedConverter implements DictionaryConverter {

    @Override
    public Serializable convert(InputStreamReader inputStreamReader) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader
                    .lines()
                    .reduce(new PatriciaTrie<>(),
                            this::tabLineParse,
                            (patriciaTrie1, patriciaTrie2) -> patriciaTrie1);
        }
    }

    private PatriciaTrie<String> tabLineParse(PatriciaTrie<String> patriciaTrie, String line) {
        String[] split = line.split("\t");
        if (split.length >= 2) {
            patriciaTrie.put(split[0], split[1]);
        }
        return patriciaTrie;
    }

}
