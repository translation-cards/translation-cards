package org.mercycorps.translationcards.refactor.activity;

import org.mercycorps.translationcards.data.Dictionary;

public class DictionaryFactory {
    public static Dictionary createDictionary(String name, int translationCount){
        Dictionary dict = new Dictionary(name);
        return dict;
    }
}
