package org.mercycorps.translationcards.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addTranslation.AddNewTranslationContext;
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.activity.addTranslation.NewTranslation;

import java.util.List;

public class TranslationTabsFragment extends Fragment  {

    private NewTranslation currentTranslation;
    private View currentLanguageTab;
    private OnLanguageTabSelectedListener onLanguageTabSelectedListener = new OnLanguageTabSelectedListener() {
        @Override
        public void onLanguageTabSelected(NewTranslation currentTranslation) {

        }
    };

    public interface OnLanguageTabSelectedListener {
        void onLanguageTabSelected(NewTranslation currentTranslation);
    }

    public void setOnLanguageTabSelectedListener(OnLanguageTabSelectedListener onLanguageTabSelectedListener) {
        this.onLanguageTabSelectedListener = onLanguageTabSelectedListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translation_tabs, container, false);
        LinearLayout languagesScrollList = (LinearLayout) fragmentView.findViewById(R.id.languages_scroll_list);
        AddNewTranslationContext addNewTranslationContext = getArguments().getParcelable(AddTranslationActivity.CONTEXT_INTENT_KEY);
        List<NewTranslation> newTranslations = addNewTranslationContext.getNewTranslations();

        currentTranslation = newTranslations.get(0);
        for (NewTranslation newTranslation : newTranslations) {
            View languageTab = inflateLanguageTab(inflater, languagesScrollList, newTranslation);
            addContentToTab(newTranslation, languageTab);
            languagesScrollList.addView(languageTab);
            if (currentTranslation == newTranslation) {
                currentLanguageTab = languageTab;
            }
        }

        return fragmentView;
    }

    private void addContentToTab(NewTranslation newTranslation, View languageTab) {
        TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
        languageTabText.setText(newTranslation.getDictionary().getLanguage().toUpperCase());

        updateTranslationTabColor(newTranslation, languageTab);
    }

    private void updateTranslationTabColor(NewTranslation newTranslation, View languageTab) {
        TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
        int tabBorderColor = currentTranslation == newTranslation ? R.color.textColor : R.color.colorPrimary;
        int tabTextColor = currentTranslation == newTranslation ? R.color.textColor : R.color.unselectedLanguageTabText;
        languageTabText.setTextColor(ContextCompat.getColor(getActivity(), tabTextColor));
        languageTab.findViewById(R.id.tab_border).setBackgroundResource(tabBorderColor);
    }

    private View inflateLanguageTab(LayoutInflater inflater, LinearLayout languagesScrollList, final NewTranslation newTranslation) {
        final View languageTab = inflater.inflate(R.layout.language_tab, languagesScrollList, false);
        languageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewTranslation previousNewTranslation = currentTranslation;
                updateTranslationTabColor(newTranslation, currentLanguageTab);
                currentLanguageTab = languageTab;
                currentTranslation = newTranslation;
                onLanguageTabSelectedListener.onLanguageTabSelected(previousNewTranslation);
                updateTranslationTabColor(newTranslation, languageTab);
            }
        });
        return languageTab;
    }

    public void setCurrentTranslation(NewTranslation currentTranslation) {
        this.currentTranslation = currentTranslation;
    }

    public NewTranslation getCurrentTranslation() {
        return currentTranslation;
    }
}
