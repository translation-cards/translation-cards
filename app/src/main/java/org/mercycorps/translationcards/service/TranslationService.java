package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationService {

    private DbManager dbManager;
    private DictionaryService dictionaryService;
    private List<Translation> currentTranslations;
    private boolean displayCardsWithNoAudio;
    private List<Boolean> translationCardStates;

    public TranslationService(DbManager dbManager, DictionaryService dictionaryService) {
        this.dbManager = dbManager;
        this.dictionaryService = dictionaryService;
        currentTranslations = findCurrentTranslations();
        translationCardStates = new ArrayList<>(Arrays.asList(new Boolean[currentTranslations.size()]));
        Collections.fill(translationCardStates, Boolean.FALSE);
        displayCardsWithNoAudio = true;
    }

    private List<Translation> findCurrentTranslations() {
        List<Translation> translations = new ArrayList<>();
        Dictionary currentDictionary = dictionaryService.currentDictionary();
        for(int i = 0; i < currentDictionary.getTranslationCount(); i++) {
            Translation translation = currentDictionary.getTranslation(i);
            if(displayCardsWithNoAudio || translation.isAudioFilePresent()) {
                translations.add(translation);
            }
        }

        return translations;
    }

    public List<Translation> getCurrentTranslations() {
        return findCurrentTranslations();
    }

    public void deleteTranslationBySourcePhrase(String sourcePhrase) {
        for(Dictionary dictionary : dictionaryService.getDictionariesForCurrentDeck()) {
            Translation translation = dictionary.getTranslationBySourcePhrase(sourcePhrase);
            dbManager.deleteTranslation(translation.getDbId());
        }
    }

    public void toggleDisplayCardsWithNoAudio(boolean displayCardsWithNoAudio) {
        this.displayCardsWithNoAudio = displayCardsWithNoAudio;
    }

    public void expandCard(int position) {
        translationCardStates.set(position, true);
    }

    public void minimizeCard(int position) {
        translationCardStates.set(position, false);
    }

    public boolean cardIsExpanded(int position) {
        return translationCardStates.size() > 0 && translationCardStates.get(position);
    }
}
