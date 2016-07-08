package org.mercycorps.translationcards.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.porting.ExportTask;
import org.mercycorps.translationcards.service.DeckService;
import org.mercycorps.translationcards.service.DictionaryService;
import org.mercycorps.translationcards.view.DeckItem;

import java.util.List;

public class MyDeckAdapter extends ArrayAdapter<Deck> implements DeckItem.DeckMenuListener {
    private final MyDecksActivity activity;
    DeckService deckService;
    private DictionaryService dictionaryService;

    public MyDeckAdapter(MyDecksActivity context, int deckItemResource, int deckNameResource, List<Deck> decks, DeckService deckService, DictionaryService dictionaryService) {
        super(context, deckItemResource, deckNameResource, decks);
        this.deckService = deckService;
        this.dictionaryService = dictionaryService;
        this.activity = context;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        DeckItem deckItem;
        Deck deck = getItem(position);
        if (view == null) {
            deckItem = new DeckItem(this.activity);
        } else {
            deckItem = (DeckItem) view;
        }
        deckItem.setDeck(deck);
        deckItem.setMenuListener(this);
        deckItem.setOnClickListener(getDeckItemClickListener(deck));

        return deckItem;
    }

    private View.OnClickListener getDeckItemClickListener(final Deck deck) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent decksIntent = new Intent(activity, TranslationsActivity.class);
                deckService.setCurrentDeck(deck);
                dictionaryService.setCurrentDictionary(0);
                activity.startActivity(decksIntent);
            }
        };
    }

    private void optionallyDelete(final Deck deck) {
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setTitle(R.string.deck_delete_dialog_title)
                .setPositiveButton(R.string.misc_ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deck.delete();
                                activity.refreshMyDecksList();
                            }
                        })
                .setNegativeButton(R.string.misc_cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                .create();
        alertDialog.show();
    }

    private void shareDeck(final Deck deck) {
        final EditText nameField = new EditText(getContext());
        nameField.setText(deck.getTitle());
        new AlertDialog.Builder(activity)
                .setTitle(R.string.deck_export_dialog_title)
                .setView(nameField)
                .setPositiveButton(R.string.misc_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        (new ExportTask(nameField.getText().toString(), deck, activity)).execute();
                    }
                })
                .setNegativeButton(R.string.misc_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing.
                    }
                })
                .show();
    }

    @Override
    public void onShareClicked(Deck deck) {
        shareDeck(deck);
    }

    @Override
    public void onDeleteClicked(Deck deck) {
        optionallyDelete(deck);
    }
}