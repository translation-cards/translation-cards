package org.mercycorps.translationcards.activity.addTranslation;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.fragment.TranslationTabsFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class EnterTranslatedPhraseActivity extends AddTranslationActivity {
    @Bind(R.id.translated_phrase_field)TextView translatedPhraseTextView;
    @Bind(R.id.translated_phrase_input_language_label)TextView translatedPhraseInputLanguageLabel;
    @Bind(R.id.recording_label_next_text)TextView skipLabel;
    @Bind(R.id.origin_translation_text)TextView sourcePhrase;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_enter_translated_phrase);
    }

    @Override
    public void initStates(){
        inflateFragmentInView();
        includeSourcePhraseInHeader();
        updateInputLanguageLabel();
        updateTranslatedPhraseTextField();
    }

    private void inflateFragmentInView() {
        Fragment translationTabsFragment = new TranslationTabsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle arguments = new Bundle();
        ArrayList<Dictionary> dictionaries = new ArrayList<>();
        dictionaries.add(getContextFromIntent().getDictionary());
        arguments.putSerializable("Dictionaries", dictionaries);
        translationTabsFragment.setArguments(arguments);
        transaction.replace(R.id.language_tabs_fragment, translationTabsFragment);
        transaction.commit();
    }

    @OnClick(R.id.enter_translated_phrase_next_label)

    protected void enterTranslatedTextNextLabelClicked(){
        updateContextWithTranslatedText();
        startNextActivity(EnterTranslatedPhraseActivity.this, RecordAudioActivity.class);
    }

    @OnClick(R.id.enter_translated_phrase_back_label)
    protected void enterTranslatedPhraseBackLabelClicked() {
        updateContextWithTranslatedText();
        startNextActivity(EnterTranslatedPhraseActivity.this, EnterSourcePhraseActivity.class);
    }

    @OnTextChanged(R.id.translated_phrase_field)
    protected void changeSkipLabelToNextLabelWhenTextIsEnter(){
        String inputText = translatedPhraseTextView.getText().toString();
        Integer labelTextResourceId = inputText.isEmpty() ? R.string.navigation_button_skip : R.string.navigation_button_next;
        skipLabel.setText(labelTextResourceId);
    }

    private void includeSourcePhraseInHeader() {
        sourcePhrase.setText(getContextFromIntent().getTranslation().getLabel());
    }

    private void updateInputLanguageLabel() {
        String inputLanguageLabel = getContextFromIntent().getDictionary().getLabel().toUpperCase();
        translatedPhraseInputLanguageLabel.setText(String.format(getString(R.string.translated_phrase_input_language_label), inputLanguageLabel));
    }

    private void updateTranslatedPhraseTextField() {
        translatedPhraseTextView.setText(getContextFromIntent().getTranslation().getTranslatedText());
    }

    private void updateContextWithTranslatedText() {
        String translatedText  = translatedPhraseTextView.getText().toString();
        getContextFromIntent().setTranslatedText(translatedText);
    }


}
