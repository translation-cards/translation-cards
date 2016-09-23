package org.mercycorps.translationcards.addDeck.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.AbstractTranslationCardsActivity;
import org.mercycorps.translationcards.service.LanguageService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;

public class LanguageSelectorActivity extends AbstractTranslationCardsActivity {
    public static final String SELECTED_LANGUAGE_KEY = "selectedLanguage";
    public static final int CANCEL_BUTTON_ID = 0;
    @Bind(R.id.languages_list)
    RecyclerView languagesList;
    @Bind(R.id.language_filter_field)
    EditText languageFilterField;
    @Inject LanguageService languageService;
    private String selectedLanguage;
    private LanguageSelectorAdapter languageSelectorAdapter;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_language_selector);
    }

    @Override
    protected void initStates() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);

        initializeLanguageList();
        initializeLanguageFilterField();
    }

    private void initializeLanguageList() {
        languagesList.setHasFixedSize(true);
        languagesList.setLayoutManager(new LinearLayoutManager(this));
        languageSelectorAdapter = new LanguageSelectorAdapter(languageService.getLanguageNames());
        languagesList.setAdapter(languageSelectorAdapter);
    }

    private void initializeLanguageFilterField() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                languageSelectorAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        languageFilterField.addTextChangedListener(textWatcher);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CANCEL_BUTTON_ID:
                selectedLanguage = "";
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
        int result = selectedLanguage.isEmpty() ? RESULT_CANCELED : RESULT_OK;
        setResult(result, data);
        super.finish();
    }

    @Override
    public void setBitmapsForActivity() {
    }

    class LanguageSelectorAdapter extends RecyclerView.Adapter<LanguageSelectorAdapter.ViewHolder> {
        private List<String> items;
        private List<String> languageNames;

        public class ViewHolder extends RecyclerView.ViewHolder {

            public TextView languageItem;

            public ViewHolder(TextView itemView) {
                super(itemView);
                languageItem = itemView;
            }
        }

        public LanguageSelectorAdapter(List<String> languageNames) {
            this.languageNames = languageNames;
            this.items = languageNames;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView languageItem = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ViewHolder(languageItem);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.languageItem.setText(items.get(position));
            holder.languageItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedLanguage = items.get(holder.getAdapterPosition());
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public void filter(String string) {
            List<String> items = new ArrayList<>();
            for (String languageName : languageNames) {
                if (languageName.toLowerCase().contains(string.toLowerCase())) {
                    items.add(languageName);
                }
            }
            this.items = items;
            notifyDataSetChanged();
        }

        public String getItem(int position) {
            return items.get(position);
        }
    }
}
