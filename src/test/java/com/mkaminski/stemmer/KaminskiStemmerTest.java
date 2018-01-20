package com.mkaminski.stemmer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

/**
 * @author Mateusz Kamiński
 */
public class KaminskiStemmerTest {

    @Test
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

    @Test
    void testStemming() throws URISyntaxException, IOException {
        // given
        Path kaminskiStemmerTestPath = Files.createTempDirectory("kaminski_stemmer_test");
        Path stemmedText = Paths.get(kaminskiStemmerTestPath.toString(), "stemmed.txt");

        // when
        KaminskiStemmerRunner kaminskiStemmerRunner = new KaminskiStemmerRunner(
                new RunOptionsBuilder()
                        .setMainCommand("stem")
                        .setDictPath(Paths.get(getClass().getClassLoader().getResource("pl_dict.ser").toURI()))
                        .setSourcePath(Paths.get(getClass().getClassLoader().getResource("to_stem.txt").toURI()))
                        .setResultPath(stemmedText)
                        .createRunOptions());

        kaminskiStemmerRunner.run();

        // then
        assertTrue(Files.exists(stemmedText));
        assertEquals("Tomek pisać dokumentacja Alo mój pies ", Files.readAllLines(stemmedText).get(0));
    }

    @Test
    void testStemmingStatistics() throws URISyntaxException, IOException {
        // given
        Path kaminskiStemmerTestPath = Files.createTempDirectory("kaminski_stemmer_test");
        Path stemmedText = Paths.get(kaminskiStemmerTestPath.toString(), "stemmed.txt");

        // when
        KaminskiStemmerRunner kaminskiStemmerRunner = new KaminskiStemmerRunner(
                new RunOptionsBuilder()
                        .setMainCommand("stem")
                        .setDictPath(Paths.get(getClass().getClassLoader().getResource("pl_dict.ser").toURI()))
                        .setSourcePath(Paths.get(getClass().getClassLoader().getResource("pan_tadeusz.txt").toURI()))
                        .setResultPath(stemmedText)
                        .createRunOptions());

        System.currentTimeMillis();
        kaminskiStemmerRunner.run();
        System.currentTimeMillis();

        // then
        assertTrue(Files.exists(stemmedText));
    }
}
