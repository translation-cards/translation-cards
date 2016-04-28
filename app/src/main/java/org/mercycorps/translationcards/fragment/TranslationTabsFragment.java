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
import org.mercycorps.translationcards.activity.addTranslation.AddTranslationActivity;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.List;

public class TranslationTabsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translation_tabs, container, false);
        LinearLayout languagesScrollList = (LinearLayout) fragmentView.findViewById(R.id.languages_scroll_list);
        List<Dictionary> dictionaries = (List<Dictionary>) getArguments().getSerializable(AddTranslationActivity.DICTIONARY_FRAGMENT_KEY);
        for (Dictionary dictionary : dictionaries) {
            View languageTab = inflater.inflate(R.layout.language_tab, languagesScrollList, false);
            TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
            languageTabText.setText(dictionary.getLabel().toUpperCase());
            languageTabText.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColor));
            languageTab.findViewById(R.id.tab_border).setBackgroundResource(R.color.textColor);
            languagesScrollList.addView(languageTab);
        }
        return fragmentView;
    }

}
