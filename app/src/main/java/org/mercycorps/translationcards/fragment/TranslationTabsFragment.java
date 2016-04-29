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

public class TranslationTabsFragment extends Fragment {

    private NewTranslation currentTranslation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translation_tabs, container, false);
        LinearLayout languagesScrollList = (LinearLayout) fragmentView.findViewById(R.id.languages_scroll_list);
        AddNewTranslationContext addNewTranslationContext = (AddNewTranslationContext) getArguments().getSerializable(AddTranslationActivity.CONTEXT_INTENT_KEY);
        List<NewTranslation> newTranslations = addNewTranslationContext.getNewTranslations();
        currentTranslation = newTranslations.get(0);
        for (NewTranslation newTranslation : newTranslations) {
            View languageTab = inflateLanguageTab(inflater, languagesScrollList, newTranslation);

            TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
            languageTabText.setText(newTranslation.getDictionary().getLabel().toUpperCase());
            languageTabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));

            languageTab.findViewById(R.id.tab_border).setBackgroundResource(R.color.textColor);

            languagesScrollList.addView(languageTab);
        }
        return fragmentView;
    }

    private View inflateLanguageTab(LayoutInflater inflater, LinearLayout languagesScrollList, final NewTranslation newTranslation) {
        View languageTab = inflater.inflate(R.layout.language_tab, languagesScrollList, false);
        languageTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentTranslation = newTranslation;
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
