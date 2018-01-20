package com.mkaminski.stemmer;

import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;
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

    @Test
    public void stemTest() {
        // when
        Scanner scanner = getResourceScannerWithDelimiter("to_stem.txt");
        List<String> stemmedText = stemText(scanner);

        // then
        assertTrue(stemmedText.size() > 0);
        assertEquals("Tomek pisać dokumentacja  Ala mieć pies", stemmedText.stream().collect(joining(" ")));
    }

    @Test
    public void stemOldPolishTest() throws IOException {
        // given
        Path kaminskiStemmerTestPath = Files.createTempDirectory("morfologik_stemmer_test");
        Path stemmedTextPath = Paths.get(kaminskiStemmerTestPath.toString(), "pan_tadeusz_stemmed.txt");

        // when
        Scanner scanner = getResourceScannerWithDelimiter("pan_tadeusz.txt");
        List<String> stemmedText = stemText(scanner);

        // then
        assertTrue(stemmedText.size() > 0);

        writeTextDelimitedWithSpace(stemmedTextPath, stemmedText);
    }



    private Scanner getResourceScannerWithDelimiter(String resourceName) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName);
        return new Scanner(resource).useDelimiter("([\\s]+|[\\p{Punct}]+)");
    }

    private List<String> stemText(Scanner delimitedResource) {
        List<String> stemmedText = new ArrayList<>();
        while (delimitedResource.hasNext()) {
            String nextWord = delimitedResource.next();
            List<WordData> foundWords = dict.lookup(nextWord);
            if (foundWords.size() > 0) {
                stemmedText.add(foundWords.get(0).getStem().toString());
            } else {
                stemmedText.add(nextWord);
            }
        }
        return stemmedText;
    }

    private void writeTextDelimitedWithSpace(Path path, List<String> delimitedText) throws IOException {
        Files.write(path, singletonList(delimitedText.stream().collect(Collectors.joining(" "))));
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
