package com.mkaminski.stemmer;

import static com.mkaminski.stemmer.TestUtil.calculateAndPrintStatistics;
import static com.mkaminski.stemmer.TestUtil.getOutputFileForSaveStemmingResult;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * @author Mateusz Kamiński
 */
public class KaminskiStemmerTest {

    private static Path kaminskiStemmerTestPath;

    @BeforeAll
    public static void setupDictionary() throws IOException, URISyntaxException {
        kaminskiStemmerTestPath = Files.createTempDirectory("morfologik_stemmer_test");
        testStemming();
    }

    @Test
    @Disabled
    void testDictionaryConversion() throws URISyntaxException, IOException {
        // given
        Path kaminskiStemmerTestPath = Files.createTempDirectory("kaminski_stemmer_test");
        Path dictionaryResultPath = Paths.get(kaminskiStemmerTestPath.toString(), "result.ser");

        // when
        KaminskiStemmerRunner kaminskiStemmerRunner = new KaminskiStemmerRunner(
                new RunOptionsBuilder()
                        .setMainCommand("convert")
                        .setSourcePath(Paths.get(getClass().getClassLoader().getResource("data").toURI()))
                        .setResultPath(dictionaryResultPath)
                        .createRunOptions());

        kaminskiStemmerRunner.run();

        // then
        assertTrue(Files.exists(dictionaryResultPath));
    }

    static void testStemming() throws URISyntaxException, IOException {
        // given
        Path stemmedText = getOutputFileForSaveStemmingResult(kaminskiStemmerTestPath, "stemmed");

        // when
        KaminskiStemmerRunner kaminskiStemmerRunner = new KaminskiStemmerRunner(
                new RunOptionsBuilder()
                        .setMainCommand("stem")
                        .setDictPath(Paths.get(KaminskiStemmerTest.class.getClassLoader().getResource("pl_dict.ser").toURI()))
                        .setSourcePath(Paths.get(KaminskiStemmerTest.class.getClassLoader().getResource("to_stem.txt").toURI()))
                        .setResultPath(stemmedText)
                        .createRunOptions());

        kaminskiStemmerRunner.run();

        // then
        assertTrue(Files.exists(stemmedText));
        assertEquals("Tomek pisać dokumentacja Alo mój pies ", Files.readAllLines(stemmedText).get(0));
    }


    @Test
    public void stemOldPolishTest() throws IOException, URISyntaxException {
        testStemmingStatistics("pan_tadeusz");
    }

    @Test
    public void stemOldBigPolishTest() throws IOException, URISyntaxException {
        testStemmingStatistics("ogniem-i-mieczem");
    }

    @Test
    public void stemScienceBigPolishTest() throws IOException, URISyntaxException {
        testStemmingStatistics("zbikowski-doktorat");
    }

    @Test
    public void stemContemporaryPolishTest() throws IOException, URISyntaxException {
        testStemmingStatistics("sztuka-zdobywania-pieniedzy");
    }

    @Test
    public void stemLiteraturePolishTest() throws IOException, URISyntaxException {
        testStemmingStatistics("tadeusz-micinski-nauczycielka");
    }

    void testStemmingStatistics(String resourceName) throws URISyntaxException, IOException {
        // given
        Path unstemmedText = Paths.get(getClass().getClassLoader().getResource(resourceName + ".txt").toURI());
        Path stemmedText = getOutputFileForSaveStemmingResult(kaminskiStemmerTestPath, resourceName);

        // when
        KaminskiStemmerRunner kaminskiStemmerRunner = new KaminskiStemmerRunner(
                new RunOptionsBuilder()
                        .setMainCommand("stem")
                        .setSourcePath(unstemmedText)
                        .setResultPath(stemmedText)
                        .createRunOptions());

        long start = System.currentTimeMillis();
        kaminskiStemmerRunner.run();
        long end = System.currentTimeMillis();

        // then
        assertTrue(Files.exists(stemmedText));

        calculateAndPrintStatistics(end - start, kaminskiStemmerTestPath, resourceName);
    }
}
