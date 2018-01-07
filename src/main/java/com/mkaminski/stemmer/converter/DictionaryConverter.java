package com.mkaminski.stemmer.converter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;

/**
 * Interfejs dla konwertera słownika zewnętrznego na wykorzystywany w ramach Stemmera.
 *
 * @author Mateusz Kamiński
 */
public interface DictionaryConverter {

    Serializable convert(InputStreamReader inputStreamReader) throws IOException;
}
