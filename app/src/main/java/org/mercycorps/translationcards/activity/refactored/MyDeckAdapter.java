package org.mercycorps.translationcards.activity.refactored;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDeckAdapter extends ArrayAdapter<Deck> {
    private final Activity context;
    private LayoutInflater layoutInflater;
    private ViewHolder viewHolder;


    static class ViewHolder {
        @Bind(R.id.origin_language)TextView originLanguageTextView;
        @Bind(R.id.deck_name)TextView deckNameTextView;
        @Bind(R.id.deck_information)TextView deckInformationTextView;
        @Bind(R.id.translation_languages)TextView translationLanguagesTextView;
        @Bind(R.id.deck_card_expansion_delete)LinearLayout deckCardExpansionDeleteLinearLayout;

        public ViewHolder(View view){
            ButterKnife.bind(this, view);
        }

        @OnClick(R.id.deck_card_expansion_delete)
        public void deleteDeck(){
        }

    }

    public MyDeckAdapter(Activity context, int deckItemResource, int deckNameResource, List<Deck> decks) {
        super(context, deckItemResource, deckNameResource, decks);
        this.layoutInflater = context.getLayoutInflater();
        this.context = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Deck deck = getItem(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.deck_item, parent, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        initFields(deck);
        return view;
    }

    private void initFields(Deck deck){
        if(deck == null) return;
        viewHolder.deckNameTextView.setText(deck.getLabel());
        viewHolder.deckInformationTextView.setText(String.format("%s, %s", deck.getPublisher(), deck.getCreationDateString()));
        viewHolder.translationLanguagesTextView.setText(getDbManager().getTranslationLanguagesForDeck(deck.getDbId()));
    }


    private DbManager getDbManager(){
        return ((MainApplication) context.getApplication()).getDbManager();
    }
}
