package org.mercycorps.translationcards.data;

import java.util.ArrayList;
import java.util.List;

public class Repository {
    private DbManager dbManager;

    public Repository(DbManager dbManager) {
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
