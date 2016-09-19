package org.mercycorps.translationcards.addDeck.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.service.LanguageService;

import javax.inject.Inject;

import butterknife.Bind;

public class LanguageSelectorActivity extends AbstractTranslationCardsActivity {
    public static final String SELECTED_LANGUAGE_KEY = "selectedLanguage";
    public static final int CANCEL_BUTTON_ID = 0;
    @Bind(R.id.languages_list)
    ListView languagesList;
    @Bind(R.id.language_filter_field)
    EditText languageFilterField;
    @Inject LanguageService languageService;
    private ArrayAdapter<String> languagesAdapter;
    private String selectedLanguage;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_language_selector);
    }

    @Override
    protected void initStates() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);

        initLanguagesAdapter();
        initLanguagesFilter();
        initListView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CANCEL_BUTTON_ID:
                selectedLanguage = null;
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, CANCEL_BUTTON_ID, Menu.NONE, R.string.misc_cancel);
        MenuItem item = menu.findItem(CANCEL_BUTTON_ID);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(SELECTED_LANGUAGE_KEY, selectedLanguage);
        int result = selectedLanguage == null ? RESULT_CANCELED : RESULT_OK;
        setResult(result, data);
        super.finish();
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
        languagesAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                languageService.getLanguageNames());
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
