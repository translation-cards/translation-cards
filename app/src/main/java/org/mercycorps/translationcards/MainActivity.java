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

package org.mercycorps.translationcards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for the main screen, with lists of phrases to play.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String FEEDBACK_URL =
            "https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/" +
                    "viewform?entry.1158658650=0.2.1";

    private static final int REQUEST_KEY_ADD_CARD = 1;
    private static final int REQUEST_KEY_EDIT_CARD = 2;

    private DbManager dbm;
    private Dictionary[] dictionaries;
    private int currentDictionaryIndex;
    private TextView[] languageTabTextViews;
    private View[] languageTabBorders;
    private ArrayAdapter<String> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbm = new DbManager(this);
        dictionaries = dbm.getAllDictionaries();
        currentDictionaryIndex = -1;
        setContentView(R.layout.activity_main);
        initTabs();
        initList();
        initFeedbackButton();
        setDictionary(0);
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
        ListView list = (ListView) findViewById(R.id.list);
        LayoutInflater layoutInflater = getLayoutInflater();
        list.addHeaderView(layoutInflater.inflate(R.layout.main_list_header, list, false));
        list.addFooterView(layoutInflater.inflate(R.layout.main_list_footer, list, false));
        listAdapter = new CardListAdapter(
                this, R.layout.list_item, R.id.card_text, new ArrayList<String>(), list);
        list.setAdapter(listAdapter);
        ImageButton addTranslationButton = (ImageButton) findViewById(R.id.add_button);
        addTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddTranslationDialog();
            }
        });
    }

    private void initFeedbackButton() {
        findViewById(R.id.main_feedback_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(FEEDBACK_URL)));
            }
        });
    }

    private void setDictionary(int dictionaryIndex) {
        if (currentDictionaryIndex != -1) {
            languageTabTextViews[currentDictionaryIndex].setTextColor(
                    getResources().getColor(R.color.unselectedLanguageTabText));
            languageTabBorders[currentDictionaryIndex].setBackgroundColor(0);
        }
        languageTabTextViews[dictionaryIndex].setTextColor(
                getResources().getColor(R.color.textColor));
        languageTabBorders[dictionaryIndex].setBackgroundColor(
                getResources().getColor(R.color.textColor));
        currentDictionaryIndex = dictionaryIndex;
        Dictionary dictionary = dictionaries[dictionaryIndex];
        listAdapter.clear();
        for (int translationIndex = 0;
             translationIndex < dictionary.getTranslationCount();
             translationIndex++) {
            listAdapter.add(dictionary.getTranslation(translationIndex).getLabel());
        }

    }

    private void displayAndPlayTranslation(Dictionary.Translation translationCard) {
        Bundle bundle = new Bundle();
        bundle.putString("translationText", translationCard.getTranslatedText());
        bundle.putSerializable("TranslationCard", translationCard);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        PlayCardFragment playCardFragment = new PlayCardFragment();
        playCardFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.mainActivity, playCardFragment);
        fragmentTransaction.addToBackStack("playCardFragment");
        fragmentTransaction.commit();
    }

    private void showAddTranslationDialog() {
        Intent intent = new Intent(this, RecordingActivity.class);
        intent.putExtra(
                RecordingActivity.INTENT_KEY_DICTIONARY_ID,
                dictionaries[currentDictionaryIndex].getDbId());
        intent.putExtra(
                RecordingActivity.INTENT_KEY_DICTIONARY_LABEL,
                dictionaries[currentDictionaryIndex].getLabel());
        startActivityForResult(intent, REQUEST_KEY_ADD_CARD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_KEY_ADD_CARD:
            case REQUEST_KEY_EDIT_CARD:
                if (resultCode == RESULT_OK) {
                    dictionaries = dbm.getAllDictionaries();
                    setDictionary(currentDictionaryIndex);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void onExportButtonPress(View v) {
        (new ExportTask()).execute();
    }

    private class CardListAdapter extends ArrayAdapter<String> {

        private final View.OnClickListener clickListener;
        private final View.OnClickListener editClickListener;

        public CardListAdapter(
                Context context, int resource, int textViewResourceId, List<String> objects,
                ListView list) {
            super(context, resource, textViewResourceId, objects);
            clickListener = new CardClickListener(list);
            editClickListener = new CardEditClickListener(list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater layoutInflater = getLayoutInflater();
                view = layoutInflater.inflate(R.layout.list_item, parent, false);
                view.findViewById(R.id.card_text).setOnClickListener(clickListener);
                view.findViewById(R.id.card_edit).setOnClickListener(editClickListener);
            } else {
                view = convertView;
            }
            TextView cardTextView = (TextView) view.findViewById(R.id.card_text);
            cardTextView.setText(getItem(position));
            return view;
        }
    }

    private class CardClickListener implements View.OnClickListener {

        private final ListView list;

        public CardClickListener(ListView list) {
            this.list = list;
        }

        @Override
        public void onClick(View view) {
            View listItem = (View) view.getParent().getParent().getParent();
            int position = list.getPositionForView(listItem);
            if (position == 0 ||
                    position > dictionaries[currentDictionaryIndex].getTranslationCount()) {
                // It's a click on the header or footer bumper, ignore it.
                return;
            }
            int itemIndex = position - 1;

            Dictionary.Translation translationCard =
                    dictionaries[currentDictionaryIndex].getTranslation(itemIndex);
            displayAndPlayTranslation(translationCard);
        }
    }

    private class CardEditClickListener implements View.OnClickListener {

        private final ListView list;

        public CardEditClickListener(ListView list) {
            this.list = list;
        }

        @Override
        public void onClick(View view) {
            View listItem = (View) view.getParent().getParent().getParent();
            int position = list.getPositionForView(listItem);
            if (position == 0 ||
                    position > dictionaries[currentDictionaryIndex].getTranslationCount()) {
                // It's a click on the header or footer bumper, ignore it.
                return;
            }
            int itemIndex = position - 1;
            Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
            Dictionary dictionary = dictionaries[currentDictionaryIndex];
            Dictionary.Translation translation = dictionary.getTranslation(itemIndex);
            intent.putExtra(RecordingActivity.INTENT_KEY_DICTIONARY_ID, dictionary.getDbId());
            intent.putExtra(RecordingActivity.INTENT_KEY_DICTIONARY_LABEL, dictionary.getLabel());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_ID, translation.getDbId());
            intent.putExtra(RecordingActivity.INTENT_KEY_TRANSLATION_LABEL, translation.getLabel());
            intent.putExtra(
                    RecordingActivity.INTENT_KEY_TRANSLATION_IS_ASSET, translation.getIsAsset());
            intent.putExtra(
                    RecordingActivity.INTENT_KEY_TRANSLATION_FILENAME, translation.getFilename());
            intent.putExtra(
                    RecordingActivity.INTENT_KEY_TRANSLATION_TEXT, translation.getTranslatedText());
            startActivityForResult(intent, REQUEST_KEY_EDIT_CARD);
        }
    }





    private class ExportTask extends AsyncTask<Void, Void, Void> {

        private File targetFile;
        private ProgressDialog loadingDialog;

        protected void onPreExecute() {
            loadingDialog = ProgressDialog.show(
                    MainActivity.this,
                    getString(R.string.export_progress_dialog_title),
                    getString(R.string.export_progress_dialog_message),
                    true);
        }

        protected Void doInBackground(Void... params) {
            targetFile = new File(getExternalCacheDir(), "export.txc");
            if (targetFile.exists()) {
                targetFile.delete();
            }
            TxcPortingUtility portingUtility = new TxcPortingUtility();
            DbManager dbm = new DbManager(MainActivity.this);
            try {
                portingUtility.exportData(dbm.getAllDictionaries(), targetFile);
            } catch (ExportException e) {
                alertUserOfExportFailure(e);
                return null;
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            loadingDialog.cancel();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(targetFile));
            startActivity(intent);
        }
    }

    private void alertUserOfExportFailure(ExportException error) {
        String errorMessage = getString(R.string.import_failure_default_error_message);
        if (error.getProblem() == ExportException.ExportProblem.TARGET_FILE_NOT_FOUND) {
            errorMessage = getString(R.string.export_failure_target_file_not_found_error_message);
        } else if (error.getProblem() == ExportException.ExportProblem.WRITE_ERROR) {
            errorMessage = getString(R.string.export_failure_write_error_error_message);
        } else if (error.getProblem() ==
                ExportException.ExportProblem.TOO_MANY_DUPLICATE_FILENAMES) {
            errorMessage = getString(
                    R.string.export_failure_too_many_duplicate_filenames_error_message);
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.import_failure_alert_title)
                .setMessage(errorMessage)
                .show();
    }
}
