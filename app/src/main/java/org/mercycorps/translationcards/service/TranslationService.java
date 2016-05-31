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
    private boolean displayCardsWithNoAudio;
    private List<Boolean> expanded;

    public TranslationService(Repository repository, DictionaryService dictionaryService) {
        this.repository = repository;
        this.dictionaryService = dictionaryService;
        displayCardsWithNoAudio = true;
        initializeCardStates();
    }

    public void initializeCardStates() {
        expanded = new ArrayList<>(
                Arrays.asList(
                        new Boolean[getCurrentTranslations().size()]
                )
        );
        Collections.fill(expanded, Boolean.FALSE);
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
        expanded.set(position, true);
    }

    public void minimizeCard(int position) {
        expanded.set(position, false);
    }

    public boolean cardIsExpanded(int position) {
        return expanded.size() > 0 && expanded.get(position);
    }
}
