package org.mercycorps.translationcards.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Dictionary;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TranslationTabsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_translation_tabs, container, false);
        LinearLayout languagesScrollList = (LinearLayout) fragmentView.findViewById(R.id.languages_scroll_list);
        List<Dictionary> dictionaries = (List<Dictionary>) getArguments().getSerializable("Dictionaries");
        for (Dictionary dictionary : dictionaries) {
            View languageTab = inflater.inflate(R.layout.language_tab, languagesScrollList, false);
            TextView languageTabText = (TextView) languageTab.findViewById(R.id.tab_label_text);
            languageTabText.setText(dictionary.getLabel().toUpperCase());
            languagesScrollList.addView(languageTab);
        }
        return fragmentView;
    }


}
