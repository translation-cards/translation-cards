package org.mercycorps.translationcards.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.addDeck.AddDeckActivity;
import org.mercycorps.translationcards.activity.addDeck.EnterDeckTitleActivity;
import org.mercycorps.translationcards.activity.addDeck.NewDeckContext;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.porting.ExportTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

import static org.mercycorps.translationcards.ui.LanguageDisplayUtil.getDestLanguageListDisplay;

/**
 * Created by njimenez on 3/31/16.
 */
public class MyDeckAdapter extends ArrayAdapter<Deck> {
    public static final String DELETE_DECK = "Delete";
    public static final String SHARE_DECK = "Share";
    public static final String EDIT_DECK = "Edit";
    private final MyDecksActivity activity;
    private LayoutInflater layoutInflater;
    @Bind(R.id.deck_name)TextView deckNameTextView;
    @Bind(R.id.deck_information)TextView deckInformationTextView;
    @Bind(R.id.translation_languages)TextView translationLanguagesTextView;
    @Bind(R.id.translation_card)LinearLayout deckItemLayout;
    @Bind(R.id.deck_menu) FrameLayout deckMenu;
    @Bind(R.id.lock_icon) FrameLayout lockIcon;

    public MyDeckAdapter(MyDecksActivity context, int deckItemResource, int deckNameResource, List<Deck> decks) {
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
        }
        initFields(deck);
        setClickListeners(deck);
        disableDeckCopyingAndLockIconIfUnlocked(deck);
        return view;
    }

    private void disableDeckCopyingAndLockIconIfUnlocked(Deck deck) {
        if(!deck.isLocked()){
            lockIcon.setVisibility(View.GONE);
            deckInformationTextView.setPadding(getPaddingInPx(16), 0, getPaddingInPx(16), getPaddingInPx(20));
        }
        else{
            lockIcon.setVisibility(View.VISIBLE);
            deckInformationTextView.setPadding(getPaddingInPx(5), 0, getPaddingInPx(16), getPaddingInPx(20));
        }
    }

    private int getPaddingInPx(int padding) {
        final float scale = translationLanguagesTextView.getResources().getDisplayMetrics().density;
        return (int) (padding* scale + 0.5f);
    }

    private void initFields(Deck deck){
        if(deck == null) return;
        deckNameTextView.setText(deck.getLabel());
        deckInformationTextView.setText(String.format("%s, %s", deck.getPublisher(), deck.getCreationDateString()));
        translationLanguagesTextView.setText(getDestLanguageListDisplay(Arrays.asList(deck.getDictionaries()), "  "));
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
        nameField.setText(deck.getLabel());
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

    public void setClickListeners(final Deck deck) {
        deckItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent decksIntent = new Intent(activity, TranslationsActivity.class);
                decksIntent.putExtra(TranslationsActivity.INTENT_KEY_DECK, deck);
                activity.startActivity(decksIntent);
            }
        });
        deckMenu.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu deckMenu = new PopupMenu(getContext(), view);
                deckMenu.getMenuInflater().inflate(R.menu.popup_menu, deckMenu.getMenu());

                deckMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getTitle().toString()) {
                            case DELETE_DECK:
                                optionallyDelete(deck);
                                break;
                            case SHARE_DECK:
                                shareDeck(deck);
                                break;
                            case EDIT_DECK:
                                Intent intent = new Intent(activity, EnterDeckTitleActivity.class);
                                String languagesForDeck = getLanguagesStringForDeck(deck);
                                intent.putExtra(AddDeckActivity.INTENT_KEY_DECK, new NewDeckContext(deck, languagesForDeck, true));
                                activity.startActivity(intent);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                deckMenu.show();
            }
        });
    }

    private String getLanguagesStringForDeck(Deck deck) {
        return TextUtils.join(", ", deck.getDictionaries());
    }
}