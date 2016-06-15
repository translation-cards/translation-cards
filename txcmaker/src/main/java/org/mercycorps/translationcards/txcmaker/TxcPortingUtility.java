package org.mercycorps.translationcards.txcmaker;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

class TxcPortingUtility {

  private static final Pattern FILE_URL_MATCHER = Pattern.compile(
      "https?://docs.google.com/spreadsheets/d/(.*?)(/.*)?$");
  private static final Pattern DIR_URL_MATCHER = Pattern.compile(
      "https?://drive.google.com/corp/drive/folders/(.*)$");

  public static String buildTxcJson(ExportSpec exportSpec) {
    Gson gson = new Gson();
    return gson.toJson(exportSpec);
  }

  public static String getSpreadsheetId(HttpServletRequest req) {
    String spreadsheetFileId = req.getParameter("docId");
    Matcher spreadsheetFileIdMatcher = FILE_URL_MATCHER.matcher(spreadsheetFileId);
    if (spreadsheetFileIdMatcher.matches()) {
      spreadsheetFileId = spreadsheetFileIdMatcher.group(1);
    }
    return spreadsheetFileId;
  }

  public static String getAudioDirId(HttpServletRequest req) {
    String audioDirId = req.getParameter("audioDirId");
    Matcher audioDirIdMatcher = DIR_URL_MATCHER.matcher(audioDirId);
    if (audioDirIdMatcher.matches()) {
      audioDirId = audioDirIdMatcher.group(1);
    }
    return audioDirId;
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
