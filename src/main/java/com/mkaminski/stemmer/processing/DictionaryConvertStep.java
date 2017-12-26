package com.mkaminski.stemmer.processing;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import com.mkaminski.stemmer.converter.TabToSerializedConverter;

public class DictionaryConvertStep implements ProcessStep {

    @Override
    public void makeProcess(ProcessingContext processingContext) {
        InputStream source = processingContext.getSource();
        try (InputStreamReader inputStreamReader = new InputStreamReader(source)) {
            Serializable convertedDict = new TabToSerializedConverter().convert(inputStreamReader);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(processingContext.getDest());
            objectOutputStream.writeObject(convertedDict);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> inCommands() {
        return List.of("convert");
    }
}
