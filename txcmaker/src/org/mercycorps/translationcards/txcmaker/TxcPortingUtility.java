package org.mercycorps.translationcards.txcmaker;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TxcPortingUtility {

  public static String buildTxcJson(ExportSpec exportSpec) {
    Gson gson = new Gson();
    return gson.toJson(exportSpec);
  }

  public static class ExportSpec {

    private String deck_label;
    private String publisher;
    private String id;
    private long timestamp;
    private String license_url;
    private boolean locked;
    private List<LanguageSpec> languages;
    private transient Map<String, LanguageSpec> languageLookup;

    public ExportSpec() {
      languages = new ArrayList<LanguageSpec>();
      languageLookup = new HashMap<String, LanguageSpec>();
    }

    public ExportSpec setDeckLabel(String deckLabel) {
      this.deck_label = deckLabel;
      return this;
    }

    public ExportSpec setPublisher(String publisher) {
      this.publisher = publisher;
      return this;
    }

    public ExportSpec setDeckId(String deckId) {
      this.id = deckId;
      return this;
    }

    public ExportSpec setTimestamp(long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public ExportSpec setLicenseUrl(String licenseUrl) {
      this.license_url = licenseUrl;
      return this;
    }

    public ExportSpec setLocked(boolean locked) {
      this.locked = locked;
      return this;
    }

    public ExportSpec addCard(String language, CardSpec card) {
      if (!languageLookup.containsKey(language)) {
        LanguageSpec langSpec = new LanguageSpec(language);
        languages.add(langSpec);
        languageLookup.put(language, langSpec);
      }
      languageLookup.get(language).addCard(card);
      return this;
    }
  }

  public static class LanguageSpec {

    private String iso_code;
    private List<CardSpec> cards;

    public LanguageSpec(String isoCode) {
      this.iso_code = isoCode;
      cards = new ArrayList<CardSpec>();
    }

    public LanguageSpec addCard(CardSpec card) {
      cards.add(card);
      return this;
    }
  }

  public static class CardSpec {

    private String card_label;
    private String dest_audio;
    private String dest_txt;

    public CardSpec setLabel(String label) {
      this.card_label = label;
      return this;
    }

    public CardSpec setFilename(String filename) {
      this.dest_audio = filename;
      return this;
    }

    public CardSpec setTranslationText(String translationText) {
      this.dest_txt = translationText;
      return this;
    }
  }
}
