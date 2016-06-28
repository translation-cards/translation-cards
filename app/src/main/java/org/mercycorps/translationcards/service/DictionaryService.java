package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Dictionary;

import java.util.Arrays;
import java.util.List;

public class DictionaryService {

    private DbManager dbManager;
    Dictionary currentDictionary;
    DeckService deckService;
    int currentDictionaryIndex;

    public DictionaryService(DbManager dbManager, DeckService deckService) {
        this.dbManager = dbManager;
        this.deckService = deckService;
        currentDictionaryIndex = 0;
        currentDictionary = getDictionariesForCurrentDeck().get(currentDictionaryIndex);
    }

    public List<Dictionary> getDictionariesForCurrentDeck() {
        return Arrays.asList(
                dbManager.getAllDictionariesForDeck(
                        deckService.currentDeck().getDbId()));
    }

    public Dictionary currentDictionary() {
        return getDictionariesForCurrentDeck().get(currentDictionaryIndex);
    }

    public int getCurrentDictionaryIndex() {
        return currentDictionaryIndex;
    }

    public void setCurrentDictionary(int index) {
        currentDictionaryIndex = index;
        currentDictionary = getDictionariesForCurrentDeck().get(index);
    }
}
