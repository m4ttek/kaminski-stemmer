package com.mkaminski.stemmer;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.polish.PolishStemmer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MorfologikTest {

    private static DictionaryLookup dict;

    @BeforeAll
    public static void setupDictionary() {
        dict = new DictionaryLookup(new PolishStemmer().getDictionary());
    }

    @Test
    public void sanityCheck() {
        assertTrue(stemsOf("planetariów").contains("planetarium"));
        assertTrue(stemsOf("krowami").contains("krowa"));
        assertTrue(tagsOf("krowami").contains("subst:pl:inst:f"));
    }

    private static List<String> stemsOf(String word) {
        return dict.lookup(word).stream()
                .map((wd) -> wd.getStem().toString())
                .collect(Collectors.toList());
    }

    private static List<String> tagsOf(String word) {
        return dict.lookup(word).stream()
                .map((wd) -> wd.getTag().toString())
                .collect(Collectors.toList());
    }
}
