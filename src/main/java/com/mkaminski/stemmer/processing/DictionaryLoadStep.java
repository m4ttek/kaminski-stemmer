package com.mkaminski.stemmer.processing;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.mkaminski.stemmer.dictionary.loader.SerializedTrieDictionaryLoader;

/**
 * @author Mateusz Kamiński
 */
public class DictionaryLoadStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) {
        InputStream dictionaryResource = processingContext.getDictSource();
        if (dictionaryResource == null) {
            dictionaryResource = this.getClass().getResourceAsStream("pl_dict.ser");
        }
        Map<String, String> dictionaryMap = new SerializedTrieDictionaryLoader().loadDictionary(dictionaryResource);
        processingContext.setDictionary(dictionaryMap);
    }

    @Override
    public List<String> inCommands() {
        return List.of("stem");
    }
}
