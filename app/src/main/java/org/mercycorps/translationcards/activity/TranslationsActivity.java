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

package org.mercycorps.translationcards.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.EnterSourcePhraseActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslationContext;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.media.CardAudioClickListener;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.media.DecoratedMediaManager;
import org.mercycorps.translationcards.porting.ExportException;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.porting.ExportTask;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.activity.addTranslation.GetStartedActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Activity for the main screen, with lists of phrases to play.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class TranslationsActivity extends AbstractTranslationCardsActivity {

    public static final String INTENT_KEY_DECK = "Deck";
    private static final String TAG = "TranslationsActivity";

    private static final int REQUEST_KEY_ADD_CARD = 1;
    private static final int REQUEST_KEY_EDIT_CARD = 2;
    public static final String INTENT_KEY_CURRENT_DICTIONARY_INDEX = "CurrentDictionaryIndex";
    private static final String CONTEXT_INTENT_KEY = "NewTranslationContext";
    private static final boolean IS_EDIT = true;


    @Bind(R.id.add_translation_button) RelativeLayout addTranslationButton;

    DbManager dbManager;
    private Dictionary[] dictionaries;
    private int currentDictionaryIndex;
    private TextView[] languageTabTextViews;
    private View[] languageTabBorders;
    private CardListAdapter listAdapter;
    private Deck deck;
    private boolean[] translationCardStates;
    private DecoratedMediaManager decoratedMediaManager;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        decoratedMediaManager = application.getDecoratedMediaManager();
        dbManager = application.getDbManager();
        deck = (Deck) getIntent().getSerializableExtra(INTENT_KEY_DECK);
        dictionaries = dbManager.getAllDictionariesForDeck(deck.getDbId());
        currentDictionaryIndex = getIntent().getIntExtra(INTENT_KEY_CURRENT_DICTIONARY_INDEX, 0);
        setContentView(R.layout.activity_translations);
        initTabs();
        initList();
        setDictionary(currentDictionaryIndex);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(deck.getLabel());
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setElevation(0);
    }

    @Override
    protected void initStates() {
        updateAddTranslationButtonVisibility();
    }

    private void updateHeaderAndShare() {
        int headerVisibility = dictionaries[currentDictionaryIndex].getTranslationCount() == 0 ? View.GONE : View.VISIBLE;
        findViewById(R.id.translation_list_header).setVisibility(headerVisibility);
        findViewById(R.id.share_deck_button).setVisibility(headerVisibility);
    }

    private void updateAddTranslationButtonVisibility() {
        if(deck.isLocked()){
            addTranslationButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void setBitmapsForActivity() {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getIntent().putExtra(INTENT_KEY_CURRENT_DICTIONARY_INDEX, currentDictionaryIndex);
    }

    @OnClick(R.id.add_translation_button)
    protected void addTranslationButtonClicked() {
        launchGetStartedActivity();
    }

    private void initTabs() {
        LayoutInflater inflater = LayoutInflater.from(this);
        languageTabTextViews = new TextView[dictionaries.length];
        languageTabBorders = new View[dictionaries.length];
        LinearLayout tabContainer = (LinearLayout) findViewById(R.id.tabs);
        for (int i = 0; i < dictionaries.length; i++) {
            Dictionary dictionary = dictionaries[i];
            View textFrame = inflater.inflate(R.layout.language_tab, tabContainer, false);
            TextView textView = (TextView) textFrame.findViewById(R.id.tab_label_text);
            textView.setText(dictionary.getLabel().toUpperCase());
            final int index = i;
            textFrame.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setDictionary(index);
                }
            });
            tabContainer.addView(textFrame);
            languageTabTextViews[i] = textView;
            languageTabBorders[i] = textFrame.findViewById(R.id.tab_border);
        }
    }

    private void initList() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        LayoutInflater layoutInflater = getLayoutInflater();
        list.addHeaderView(layoutInflater.inflate(R.layout.translation_list_header, list, false));
        findViewById(R.id.translation_list_header).setOnClickListener(null);

        inflateListFooter();

        listAdapter = new CardListAdapter(
                this, R.layout.translation_item, R.id.origin_translation_text,
                new ArrayList<Translation>());
        list.setAdapter(listAdapter);
    }

    private void inflateListFooter() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        LayoutInflater layoutInflater = getLayoutInflater();
        list.addFooterView(layoutInflater.inflate(R.layout.translation_list_footer, list, false));
        updateWelcomeInstructionsState();
    }

    private void updateWelcomeInstructionsState() {
        ListView list = (ListView) findViewById(R.id.translations_list);
        boolean isTranslationsListEmpty = dictionaries[currentDictionaryIndex].getTranslationCount() == 0;
        int welcomeInstructionsVisibility = isTranslationsListEmpty ? View.VISIBLE : View.GONE;
        findViewById(R.id.empty_deck_title).setVisibility(welcomeInstructionsVisibility);
        findViewById(R.id.empty_deck_message).setVisibility(welcomeInstructionsVisibility);
        updateListViewCentered(list, isTranslationsListEmpty);
    }

    private void launchGetStartedActivity(){
        Intent nextIntent = new Intent(TranslationsActivity.this, GetStartedActivity.class);
        nextIntent.putExtra(AddTranslationActivity.CONTEXT_INTENT_KEY, createTranslationContext());
        nextIntent.putExtra(INTENT_KEY_DECK, deck);
        startActivity(nextIntent);
    }

    private NewTranslationContext createTranslationContext() {
        return new NewTranslationContext(dictionaries[currentDictionaryIndex]);
    }

    private void setDictionary(int dictionaryIndex) {
        decoratedMediaManager.stop();
        translationCardStates = new boolean[dictionaries[dictionaryIndex].getTranslationCount()];
        Arrays.fill(translationCardStates, false);

        if (currentDictionaryIndex != -1) {
            languageTabTextViews[currentDictionaryIndex].setTextColor(
                    ContextCompat.getColor(this, R.color.unselectedLanguageTabText));
            languageTabBorders[currentDictionaryIndex].setBackgroundColor(0);
        }
        languageTabTextViews[dictionaryIndex].setTextColor(
                ContextCompat.getColor(this, R.color.textColor));
        languageTabBorders[dictionaryIndex].setBackgroundColor(
                ContextCompat.getColor(this, R.color.textColor));
        currentDictionaryIndex = dictionaryIndex;
        Dictionary dictionary = dictionaries[dictionaryIndex];
        listAdapter.clear();
        for (int translationIndex = 0;
             translationIndex < dictionary.getTranslationCount();
             translationIndex++) {
            listAdapter.add(dictionary.getTranslation(translationIndex));
        }
        updateHeaderAndShare();
        updateWelcomeInstructionsState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_KEY_ADD_CARD:
            case REQUEST_KEY_EDIT_CARD:
                if (resultCode == RESULT_OK) {
                    dictionaries = dbManager.getAllDictionariesForDeck(deck.getDbId());
                    setDictionary(currentDictionaryIndex);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        decoratedMediaManager.stop();
    }

    @OnClick (R.id.share_deck_button)
    public void onExportButtonPress(View v) {
        final EditText nameField = new EditText(this);
        nameField.setText(deck.getLabel());
        (new AlertDialog.Builder(this))
                .setTitle(R.string.deck_export_dialog_title)
                .setView(nameField)
                .setPositiveButton(R.string.misc_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new ExportTask(nameField.getText().toString(), deck, TranslationsActivity.this)).execute();
                    }
                })
                .setNegativeButton(R.string.misc_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                })
                .show();
    }

    private class CardListAdapter extends ArrayAdapter<Translation> {

        public CardListAdapter(
                Context context, int resource, int textViewResourceId,
                List<Translation> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.translation_item, parent, false);
                convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.expand_arrow);
            }

            if (translationCardStates[position]) {
                convertView.findViewById(R.id.translation_child).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.collapse_arrow);
            } else {
                convertView.findViewById(R.id.translation_child).setVisibility(View.GONE);
                convertView.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.expand_arrow);
            }

            convertView.setOnClickListener(null);

            convertView.findViewById(R.id.translation_indicator_layout)
                    .setOnClickListener(new CardIndicatorClickListener(convertView, position));

            View editView = convertView.findViewById(R.id.translation_card_edit);
            View deleteView = convertView.findViewById(R.id.translation_card_delete);
            if (deck.isLocked()) {
                editView.setVisibility(View.GONE);
                deleteView.setVisibility(View.GONE);
            } else {
                editView.setOnClickListener(new CardEditClickListener(getItem(position)));
                deleteView.setOnClickListener(new CardDeleteClickListener(getItem(position).getDbId()));
            }

            TextView cardTextView = (TextView) convertView.findViewById(
                    R.id.origin_translation_text);
            cardTextView.setText(getItem(position).getLabel());

            ProgressBar progressBar = (ProgressBar) convertView.findViewById(
                    R.id.list_item_progress_bar);
            cardTextView.setOnClickListener(new CardAudioClickListener(getItem(position), progressBar,
                    decoratedMediaManager));

            TextView translatedText = (TextView) convertView.findViewById(R.id.translated_text);
            if(getItem(position).getTranslatedText().isEmpty()){
                translatedText.setText(String.format(getString(R.string.translated_text_hint),
                        dictionaries[currentDictionaryIndex].getLabel()));
                translatedText.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.textDisabled));
                translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            } else {
                translatedText.setText(getItem(position).getTranslatedText());
                translatedText.setTextColor(ContextCompat.getColor(getContext(),
                        R.color.primaryTextColor));
                translatedText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            }

            convertView.findViewById(R.id.translated_text_layout)
                    .setOnClickListener(new CardAudioClickListener(getItem(position), progressBar,
                            decoratedMediaManager));

            return convertView;
        }
    }

    private class CardIndicatorClickListener implements View.OnClickListener {

        private View translationItem;
        private int position;

        public CardIndicatorClickListener(View translationItem, int position) {

            this.translationItem = translationItem;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            View translationChild = translationItem.findViewById(R.id.translation_child);
            if (translationChild.getVisibility() == View.GONE) {
                translationChild.setVisibility(View.VISIBLE);
                translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.collapse_arrow);
                translationCardStates[position] = true;
            } else {
                translationChild.setVisibility(View.GONE);
                translationItem.findViewById(R.id.indicator_icon).setBackgroundResource(
                        R.drawable.expand_arrow);
                translationCardStates[position] = false;
            }
        }
    }

    private class CardEditClickListener implements View.OnClickListener {
        private Translation translationCard;

        public CardEditClickListener(Translation translationCard) {
            this.translationCard = translationCard;
        }

        @Override
        public void onClick(View view) {
            Intent nextIntent = new Intent(TranslationsActivity.this, EnterSourcePhraseActivity.class);
            Dictionary dictionary = dictionaries[currentDictionaryIndex];
            nextIntent.putExtra(CONTEXT_INTENT_KEY, new NewTranslationContext(dictionary, translationCard, IS_EDIT));
            nextIntent.putExtra(INTENT_KEY_DECK, deck);
            startActivity(nextIntent);
        }
    }

    private class CardDeleteClickListener implements View.OnClickListener {

        long translationId;

        public CardDeleteClickListener(long translationId) {
            this.translationId = translationId;
        }

        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(TranslationsActivity.this)
                    .setTitle("Delete Flashcard")
                    .setMessage("Are you sure you want to delete this translation card?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dbManager.deleteTranslation(translationId);
                            dictionaries = dbManager.getAllDictionariesForDeck(deck.getDbId());
                            setDictionary(currentDictionaryIndex);
                            listAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
        }
    }

}
