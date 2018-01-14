package com.mkaminski.stemmer.processing;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mkaminski.stemmer.dictionary.loader.SerializedTrieDictionaryLoader;

/**
 * @author Mateusz Kamiński
 */
public class DictionaryLoadStep implements ProcessStep {

    private static Map<String, String> cachedDictionaryMap;

    @Override
    public synchronized void makeProcess(ProcessingContext processingContext) {
        if (cachedDictionaryMap == null) {
            InputStream dictionaryResource = processingContext.getDictSource();
            if (dictionaryResource == null) {
                dictionaryResource = this.getClass().getClassLoader().getResourceAsStream("pl_dict.ser");
            }
            cachedDictionaryMap = new SerializedTrieDictionaryLoader().loadDictionary(dictionaryResource);
        }
        processingContext.setDictionary(cachedDictionaryMap);
    }

    @Override
    public List<String> inCommands() {
        return Collections.singletonList("stem");
    }
}
