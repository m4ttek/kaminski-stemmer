package com.mkaminski.stemmer.dictionary.loader;

import java.io.InputStream;
import java.util.Map;

/**
 * @author Mateusz Kamiński
 */
public interface DictionaryLoader {

    Map<String, String> loadDictionary(InputStream inputStream);
}
