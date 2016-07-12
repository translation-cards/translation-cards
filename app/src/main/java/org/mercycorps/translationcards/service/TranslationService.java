package org.mercycorps.translationcards.service;

import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.repository.TranslationRepository;
import org.mercycorps.translationcards.model.Translation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationService {

    private TranslationRepository translationRepository;
    private DictionaryService dictionaryService;
    private List<Boolean> expanded;

    public TranslationService(TranslationRepository translationRepository, DictionaryService dictionaryService) {
        this.translationRepository = translationRepository;
        this.dictionaryService = dictionaryService;
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
        return translationRepository.getTranslationsForDictionary(dictionaryService.currentDictionary());
    }

    public void deleteTranslation(String sourcePhrase) {
        for(Dictionary dictionary : dictionaryService.getDictionariesForCurrentDeck()) {
            Translation translation = dictionary.getTranslationBySourcePhrase(sourcePhrase);
            translationRepository.deleteTranslation(translation.getDbId());
        }
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

    public void saveTranslationContext(NewTranslation context) {
        Translation translation = context.getTranslation();
        if (context.isEdit()) {
            translationRepository.updateTranslation(translation.getDbId(), translation.getLabel(), translation.getIsAsset(), translation.getFilename(), translation.getTranslatedText());
        } else {
            translationRepository.addTranslationAtTop(context.getDictionary().getDbId(), translation.getLabel(), translation.getIsAsset(), translation.getFilename(), translation.getTranslatedText());
        }
    }
}
