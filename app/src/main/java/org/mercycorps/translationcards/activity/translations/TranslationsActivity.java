/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mercycorps.translationcards.activity.translations;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.GetStartedActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.model.Dictionary;
import org.mercycorps.translationcards.model.Translation;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.service.TranslationService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity for the main screen, with lists of phrases to play.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class TranslationsActivity extends AbstractTranslationCardsActivity {

    public static final String INTENT_KEY_CURRENT_DICTIONARY_INDEX = "CurrentDictionaryIndex";
    protected static final boolean IS_EDIT = true;

    private TextView[] languageTabTextViews;
    private View[] languageTabBorders;
    protected CardListAdapter listAdapter;
    private TranslationService translationService;
    private DictionaryService dictionaryService;
    private DeckService deckService;

    @Inject DecoratedMediaManager decoratedMediaManager;

    @Bind(R.id.add_translation_button) RelativeLayout addTranslationButton;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        translationService = application.getTranslationService();
        translationService.initializeCardStates();
        dictionaryService = application.getDictionaryService();
        deckService = application.getDeckService();

        application.getBaseComponent().inject(this);

        setContentView(R.layout.activity_translations);
        initTabs();
        initList();
        updateView(dictionaryService.getCurrentDictionaryIndex());
        initActionBar();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(deckService.currentDeck().getTitle());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    protected void initStates() {
        updateAddTranslationButtonVisibility();
    }

    private void updateHeader() {
        Dictionary currentDictionary = dictionaryService.currentDictionary();
        int headerVisibility = (currentDictionary.getTranslationCount() == 0) ? View.GONE : View.VISIBLE;
        findViewById(R.id.translation_list_header).setVisibility(headerVisibility);
    }

    private void updateAddTranslationButtonVisibility() {
        if(deckService.currentDeck().isLocked()){
            addTranslationButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBitmapsForActivity() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getIntent().putExtra(INTENT_KEY_CURRENT_DICTIONARY_INDEX, dictionaryService.currentDictionary());
    }

    @OnClick(R.id.add_translation_button)
    protected void addTranslationButtonClicked() {
        launchGetStartedActivity();
    }

    private void initTabs() {
        LayoutInflater inflater = LayoutInflater.from(this);
        List<Dictionary> dictionaries = dictionaryService.getDictionariesForCurrentDeck();
        languageTabTextViews = new TextView[dictionaries.size()];
        languageTabBorders = new View[dictionaries.size()];
        LinearLayout tabContainer = (LinearLayout) findViewById(R.id.tabs);
        for (int tabIndex = 0; tabIndex < dictionaries.size(); tabIndex++) {
            initTab(inflater, dictionaries, tabContainer, tabIndex);
        }
    }

    private void initTab(LayoutInflater inflater, List<Dictionary> dictionaries, LinearLayout tabContainer, int tabIndex) {
        Dictionary dictionary = dictionaries.get(tabIndex);
        View textFrame = inflater.inflate(R.layout.language_tab, tabContainer, false);
        TextView textView = (TextView) textFrame.findViewById(R.id.tab_label_text);
        textView.setText(dictionary.getLanguage().toUpperCase());
        final int index = tabIndex;
        textFrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateView(index);
            }
        });
        tabContainer.addView(textFrame);
        languageTabTextViews[tabIndex] = textView;
        languageTabBorders[tabIndex] = textFrame.findViewById(R.id.tab_border);
    }

    private void initList() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        LayoutInflater layoutInflater = getLayoutInflater();
        list.addHeaderView(layoutInflater.inflate(R.layout.translation_list_header, list, false));
        findViewById(R.id.translation_list_header).setOnClickListener(null);
        inflateListFooter();

        listAdapter = new CardListAdapter(this,
                R.layout.translation_item, R.id.origin_translation_text,
                new ArrayList<Translation>(), translationService, dictionaryService, deckService);
        list.setAdapter(listAdapter);
    }

    private void inflateListFooter() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        LayoutInflater layoutInflater = getLayoutInflater();
        list.addFooterView(layoutInflater.inflate(R.layout.translation_list_footer, list, false));
        findViewById(R.id.translations_list_footer).setOnClickListener(null);
        updateWelcomeInstructionsState();
    }

    private void updateWelcomeInstructionsState() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        boolean isTranslationsListEmpty = dictionaryService.currentDictionary().getTranslationCount() == 0;
        int welcomeInstructionsVisibility = isTranslationsListEmpty ? View.VISIBLE : View.GONE;
        findViewById(R.id.empty_deck_title).setVisibility(welcomeInstructionsVisibility);
        findViewById(R.id.empty_deck_message).setVisibility(welcomeInstructionsVisibility);
        updateListViewCentered(list, isTranslationsListEmpty);
    }

    private void launchGetStartedActivity(){
        Intent nextIntent = new Intent(TranslationsActivity.this, GetStartedActivity.class);
        nextIntent.putExtra(AddTranslationActivity.CONTEXT_INTENT_KEY, createTranslationContext());
        nextIntent.putExtra(DeckService.INTENT_KEY_DECK, deckService.currentDeck());
        startActivity(nextIntent);
    }

    //// TODO: FACTORY
    private AddNewTranslationContext createTranslationContext() {
        ArrayList<NewTranslation> newTranslations = new ArrayList<>();
        for (Dictionary dictionary : dictionaryService.getDictionariesForCurrentDeck()) {
            newTranslations.add(new NewTranslation(dictionary));
        }
        return new AddNewTranslationContext(newTranslations);
    }

    protected void updateView() {
        updateView(dictionaryService.getCurrentDictionaryIndex());
    }

    protected void updateView(int nextDictionaryIndex) {
        decoratedMediaManager.stop();
        int currentDictionaryIndex = dictionaryService.getCurrentDictionaryIndex();
        updateTabs(currentDictionaryIndex, nextDictionaryIndex);
        dictionaryService.setCurrentDictionary(nextDictionaryIndex);
        listAdapter.update();
        updateHeader();
        updateWelcomeInstructionsState();
    }

    private void updateTabs(int currentDictionaryIndex, int nextDictionaryIndex) {
        languageTabTextViews[currentDictionaryIndex].setTextColor(
                ContextCompat.getColor(this, R.color.unselectedLanguageTabText));
        languageTabBorders[currentDictionaryIndex].setBackgroundColor(0);

        languageTabTextViews[nextDictionaryIndex].setTextColor(
                ContextCompat.getColor(this, R.color.textColor));
        languageTabBorders[nextDictionaryIndex].setBackgroundColor(
                ContextCompat.getColor(this, R.color.textColor));
    }

    @Override
    protected void onPause() {
        super.onPause();
        decoratedMediaManager.stop();
    }

}
