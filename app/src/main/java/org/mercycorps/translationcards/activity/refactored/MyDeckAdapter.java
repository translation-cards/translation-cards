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

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDeckAdapter extends ArrayAdapter<Deck> {
    private final Activity activity;
    private LayoutInflater layoutInflater;
    @Bind(R.id.origin_language)TextView originLanguageTextView;
    @Bind(R.id.deck_name)TextView deckNameTextView;
    @Bind(R.id.deck_information)TextView deckInformationTextView;
    @Bind(R.id.translation_languages)TextView translationLanguagesTextView;
    @Bind(R.id.deck_card_expansion_delete)LinearLayout deckCardExpansionDeleteLinearLayout;

    public MyDeckAdapter(Activity context, int deckItemResource, int deckNameResource, List<Deck> decks) {
        super(context, deckItemResource, deckNameResource, decks);
        this.layoutInflater = context.getLayoutInflater();
        this.activity = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        Deck deck = getItem(position);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.deck_item, parent, false);
            ButterKnife.bind(this, view);
            initFields(deck);
            setClickListeners(deck);
        }
        return view;
    }

    private void initFields(Deck deck){
        if(deck == null) return;
        deckNameTextView.setText(deck.getLabel());
        deckInformationTextView.setText(String.format("%s, %s", deck.getPublisher(), deck.getCreationDateString()));
        translationLanguagesTextView.setText(getDbManager().getTranslationLanguagesForDeck(deck.getDbId()));
    }


    private DbManager getDbManager(){
        return ((MainApplication) activity.getApplication()).getDbManager();
    }

    private void optionallyDelete(final Deck deck) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.deck_delete_dialog_title)
                .setPositiveButton(R.string.misc_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getDbManager().deleteDeck(deck.getDbId());
//                                clear();
//                                addAll(getDbManager().getAllDecks());
//                                notifyDataSetChanged();
                            }
                        })
                .setNegativeButton(R.string.misc_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {}
                        })
                .create();
        alertDialog.show();
    }

    public void setClickListeners(final Deck deck) {
        deckCardExpansionDeleteLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionallyDelete(deck);
            }
        });
    }
}
