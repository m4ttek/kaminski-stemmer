package com.mkaminski.stemmer;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Mateusz Kamiński
 */
public class TestUtil {

    static void calculateAndPrintStatistics(long time, Path path, String stemmedResource) throws IOException {
        Scanner fileScannerWithDelimiter = getFileScannerWithDelimiter(path, stemmedResource);
        final Stream<String> stream = StreamSupport.stream(
                ((Iterable<String>) () -> fileScannerWithDelimiter).spliterator(), false);

        long distinctCount = stream.distinct().count();
        System.out.println(String.join("|",
                stemmedResource,
                Long.valueOf(time).toString() + " ms",
                Long.valueOf(distinctCount).toString()));
    }

    static Scanner getFileScannerWithDelimiter(Path path, String resourceName) throws IOException {
        InputStream resource = Files.newInputStream(getOutputFileForSaveStemmingResult(path, resourceName));
        return new Scanner(resource).useDelimiter("([\\s]+|[\\p{Punct}]+)");
    }

    static Path getOutputFileForSaveStemmingResult(Path path, String resourceName) {
        return path.resolve(resourceName + ".txt");
    }
}
