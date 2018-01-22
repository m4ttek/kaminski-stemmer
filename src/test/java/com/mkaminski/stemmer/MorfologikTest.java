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
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;
import morfologik.stemming.polish.PolishStemmer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class MorfologikTest {

    private static DictionaryLookup dict;

    private static Path kaminskiStemmerTestPath;

    @BeforeAll
    public static void setupDictionary() throws IOException {
        dict = new DictionaryLookup(new PolishStemmer().getDictionary());
        kaminskiStemmerTestPath = Files.createTempDirectory("morfologik_stemmer_test");
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
                getOutputFileForSaveStemmingResult("pan_tadeusz"));
        calculateAndPrintStatistics(time, "pan_tadeusz");
    }

    @Test
    public void stemOldBigPolishTest() throws IOException {
        final long time = stemResourceAndCountTime("ogniem-i-mieczem.txt",
                getOutputFileForSaveStemmingResult("ogniem-i-mieczem"));
        calculateAndPrintStatistics(time, "ogniem-i-mieczem");
    }

    private Path getOutputFileForSaveStemmingResult(String resourceName) throws IOException {
        return Paths.get(kaminskiStemmerTestPath.toString(), resourceName + ".txt");
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

    private void calculateAndPrintStatistics(long time, String stemmedResource) throws IOException {
        Scanner fileScannerWithDelimiter = getFileScannerWithDelimiter(stemmedResource);
        final Stream<String> stream = StreamSupport.stream(new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return fileScannerWithDelimiter;
            }
        }.spliterator(), false);

        long wholeCount = stream.count();
        long distinctCount = stream.distinct().count();
        System.out.println(String.join("|",
                stemmedResource,
                Long.valueOf(time).toString() + " ms",
                Long.valueOf(wholeCount).toString(),
                Long.valueOf(distinctCount).toString()));
    }

    private Scanner getResourceScannerWithDelimiter(String resourceName) {
        InputStream resource = getClass().getClassLoader().getResourceAsStream(resourceName);
        return new Scanner(resource).useDelimiter("([\\s]+|[\\p{Punct}]+)");
    }

    private Scanner getFileScannerWithDelimiter(String resourceName) throws IOException {
        InputStream resource = Files.newInputStream(getOutputFileForSaveStemmingResult(resourceName));
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
