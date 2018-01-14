package com.mkaminski.stemmer.dictionary.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.collections4.trie.PatriciaTrie;
import org.nustaq.serialization.FSTObjectInput;

/**
 * @author Mateusz Kamiński
 */
public class SerializedTrieDictionaryLoader implements DictionaryLoader {

    @Override
    public Map<String, String> loadDictionary(InputStream inputStream) {
        Map<String, String> dictionary = new PatriciaTrie<>();
        try (FSTObjectInput objectInputStream = new FSTObjectInput(inputStream)) {
            dictionary = (Map<String, String>) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getLocalizedMessage());
        }
        return dictionary;
    }
}
