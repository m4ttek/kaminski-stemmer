package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import com.mkaminski.stemmer.converter.TabToSerializedConverter;
import org.nustaq.serialization.FSTObjectOutput;

public class DictionaryConvertStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) throws IOException {
        InputStream source = processingContext.getSource();
        try (InputStreamReader inputStreamReader = new InputStreamReader(source)) {
            Serializable convertedDict = new TabToSerializedConverter().convert(inputStreamReader);

            try (FSTObjectOutput objectOutputStream = new FSTObjectOutput(processingContext.getDest())) {
                objectOutputStream.writeObject(convertedDict);
            }
        }
    }

    @Override
    public List<String> inCommands() {
        return Collections.singletonList("convert");
    }
}
