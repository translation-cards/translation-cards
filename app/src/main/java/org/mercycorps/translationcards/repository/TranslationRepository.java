package org.mercycorps.translationcards.repository;

import org.mercycorps.translationcards.model.DbManager;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;

import java.util.ArrayList;
import java.util.List;

public class TranslationRepository {
    private DbManager dbManager;

    public TranslationRepository(DbManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Translation> getTranslationsForDictionary(Dictionary dictionary) {
        List<Translation> translations = new ArrayList<>();
        for(int i = 0; i < dictionary.getTranslationCount(); i++) {
            translations.add(dictionary.getTranslation(i));
        }

        return translations;
    }

    public void deleteTranslationBySourcePhrase(String sourcePhrase, List<Dictionary> deleteFrom) {
        for(Dictionary dictionary : deleteFrom) {
            Translation translation = dictionary.getTranslationBySourcePhrase(sourcePhrase);
            dbManager.deleteTranslation(translation.getDbId());
        }
    }
}
