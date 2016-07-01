package org.mercycorps.translationcards.activity.addDeck;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.service.LanguageService;

import butterknife.Bind;

public class DestinationLanguageSelectorActivity extends AbstractTranslationCardsActivity {
    public static final String SELECTED_LANGUAGE_KEY = "selectedLanguage";
    @Bind(R.id.languages_list)
    ListView languagesList;
    @Bind(R.id.language_filter_field)
    EditText languageFilterField;
    private ArrayAdapter<String> languagesAdapter;
    private String selectedLanguage;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_destination_language_selector);
    }

    @Override
    protected void initStates() {
        super.initStates();
        initLanguagesAdapter();
        initLanguagesFilter();
        initListView();
    }

    private void initListView() {
        languagesList.setAdapter(languagesAdapter);
        languagesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedLanguage = languagesAdapter.getItem(position);
                finish();
            }
        });
    }

    private void initLanguagesAdapter() {
        LanguageService languageService = ((MainApplication) getApplication()).getLanguageService();
        languagesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                languageService.getLanguageNames());
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(SELECTED_LANGUAGE_KEY, selectedLanguage);
        int result = selectedLanguage == null? RESULT_CANCELED : RESULT_OK;
        setResult(result, data);
        super.finish();
    }

    private void initLanguagesFilter() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                languagesAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        languageFilterField.addTextChangedListener(textWatcher);
    }

    @Override
    public void setBitmapsForActivity() {
    }


}
