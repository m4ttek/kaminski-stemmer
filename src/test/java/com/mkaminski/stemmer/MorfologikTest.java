package com.mkaminski.stemmer;

import static com.mkaminski.stemmer.TestUtil.getOutputFileForSaveStemmingResult;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private static Path morfologikStemmerTestPath;

    @BeforeAll
    public static void setupDictionary() throws IOException {
        dict = new DictionaryLookup(new PolishStemmer().getDictionary());
        morfologikStemmerTestPath = Files.createTempDirectory("morfologik_stemmer_test");
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
        final long time = stemResourceAndCountTime("pan_tadeusz.txt",
                getOutputFileForSaveStemmingResult(morfologikStemmerTestPath, "pan_tadeusz"));
        TestUtil.calculateAndPrintStatistics(time, morfologikStemmerTestPath, "pan_tadeusz");
    }

    @Test
    public void stemOldBigPolishTest() throws IOException {
        final long time = stemResourceAndCountTime("ogniem-i-mieczem.txt",
                getOutputFileForSaveStemmingResult(morfologikStemmerTestPath, "ogniem-i-mieczem"));
        TestUtil.calculateAndPrintStatistics(time, morfologikStemmerTestPath,"ogniem-i-mieczem");
    }

    @Test
    public void stemScienceBigPolishTest() throws IOException {
        final long time = stemResourceAndCountTime("zbikowski-doktorat.txt",
                getOutputFileForSaveStemmingResult(morfologikStemmerTestPath, "zbikowski-doktorat"));
        TestUtil.calculateAndPrintStatistics(time, morfologikStemmerTestPath,"zbikowski-doktorat");
    }

    @Test
    public void stemContemporaryPolishTest() throws IOException {
        final long time = stemResourceAndCountTime("sztuka-zdobywania-pieniedzy.txt",
                getOutputFileForSaveStemmingResult(morfologikStemmerTestPath, "sztuka-zdobywania-pieniedzy"));
        TestUtil.calculateAndPrintStatistics(time, morfologikStemmerTestPath,"sztuka-zdobywania-pieniedzy");
    }

    @Test
    public void stemLiteraturePolishTest() throws IOException {
        final long time = stemResourceAndCountTime("tadeusz-micinski-nauczycielka.txt",
                getOutputFileForSaveStemmingResult(morfologikStemmerTestPath, "tadeusz-micinski-nauczycielka"));
        TestUtil.calculateAndPrintStatistics(time, morfologikStemmerTestPath,"tadeusz-micinski-nauczycielka");
    }

    private long stemResourceAndCountTime(String resourceName, Path stemmedTextPath) throws IOException {
        long start = System.currentTimeMillis();

        Scanner scanner = getResourceScannerWithDelimiter(resourceName);
        List<String> stemmedText = stemText(scanner);

        assertTrue(stemmedText.size() > 0);
        writeTextDelimitedWithSpace(stemmedTextPath, stemmedText);

        long end = System.currentTimeMillis();
        return end - start;
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

    private Scanner getResourceScannerWithDelimiter(String resourceName) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName);
        return new Scanner(resource).useDelimiter("([\\s]+|[\\p{Punct}]+)");
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
