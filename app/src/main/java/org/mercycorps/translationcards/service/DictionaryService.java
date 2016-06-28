package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.repository.DictionaryRepository;

import java.util.Arrays;
import java.util.List;

public class DictionaryService {

    private DictionaryRepository dictionaryRepository;
    Dictionary currentDictionary;
    DeckService deckService;
    int currentDictionaryIndex;

    public DictionaryService(DictionaryRepository dictionaryRepository, DeckService deckService) {
        this.dictionaryRepository = dictionaryRepository;
        this.deckService = deckService;
        currentDictionaryIndex = 0;
        currentDictionary = getDictionariesForCurrentDeck().get(currentDictionaryIndex);
    }

    public List<Dictionary> getDictionariesForCurrentDeck() {
        return Arrays.asList(
                dictionaryRepository.getAllDictionariesForDeck(
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
