package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.data.Repository;
import org.mercycorps.translationcards.data.Translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationService {

    private Repository repository;
    private DictionaryService dictionaryService;
    private List<Translation> currentTranslations;
    private boolean displayCardsWithNoAudio;
    private List<Boolean> translationCardStates;

    public TranslationService(Repository repository, DictionaryService dictionaryService) {
        this.repository = repository;
        this.dictionaryService = dictionaryService;
        currentTranslations = repository.getTranslationsForDictionary(dictionaryService.currentDictionary());
        translationCardStates = new ArrayList<>(Arrays.asList(new Boolean[currentTranslations.size()]));
        Collections.fill(translationCardStates, Boolean.FALSE);
        displayCardsWithNoAudio = true;
    }

    public List<Translation> getCurrentTranslations() {
        List<Translation> translations = repository.getTranslationsForDictionary(dictionaryService.currentDictionary());

        return filterTranslationsWithNoAudio(translations);
    }

    private List<Translation> filterTranslationsWithNoAudio(List<Translation> translations) {
        List<Translation> filteredTranslations = new ArrayList<>(translations.size());
        for(int index = 0; index < translations.size(); index++) {
            if(displayCardsWithNoAudio || translations.get(index).isAudioFilePresent()) {
                filteredTranslations.add(translations.get(index));
            }
        }
        return filteredTranslations;
    }

    public void deleteTranslation(String sourcePhrase) {
        repository.deleteTranslationBySourcePhrase(sourcePhrase, dictionaryService.getDictionariesForCurrentDeck());
    }

    public void setDisplayCardsWithNoAudio(boolean displayCardsWithNoAudio) {
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
