package org.mercycorps.translationcards.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.DbManager;
import org.mercycorps.translationcards.data.Deck;
import org.mercycorps.translationcards.data.Dictionary;
import org.mercycorps.translationcards.data.Translation;

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
    private final MyDecksActivity activity;
    private LayoutInflater layoutInflater;
    @Bind(R.id.deck_name)TextView deckNameTextView;
    @Bind(R.id.deck_information)TextView deckInformationTextView;
    @Bind(R.id.translation_languages)TextView translationLanguagesTextView;
    @Bind(R.id.translation_card)LinearLayout deckItemLayout;
    @Bind(R.id.deck_card_copy)LinearLayout copyDeck;
    @Bind(R.id.deck_menu) FrameLayout deckMenu;

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
        disableDeckCopyingIfUnlocked(deck);
        return view;
    }

    private void disableDeckCopyingIfUnlocked(Deck deck) {
        if(!deck.isLocked()){
            copyDeck.setVisibility(View.GONE);
        }
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

    public void setClickListeners(final Deck deck) {
        deckItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent decksIntent = new Intent(activity, TranslationsActivity.class);
                decksIntent.putExtra(TranslationsActivity.INTENT_KEY_DECK, deck);
                activity.startActivity(decksIntent);
            }
        });
        copyDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText deckNameField = new EditText(activity);
                new AlertDialog.Builder(activity)
                        .setTitle(R.string.deck_copy_dialog_title)
                        .setView(deckNameField)
                        .setPositiveButton(R.string.misc_ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        copyDeck(deck, deckNameField.getText().toString());
                                        activity.refreshMyDecksList();
                                    }
                                })
                        .setNegativeButton(R.string.misc_cancel,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                })
                        .show();
            }
        });
        deckMenu.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                PopupMenu deckMenu = new PopupMenu(getContext(), view);
                deckMenu.getMenuInflater().inflate(R.menu.popup_menu, deckMenu.getMenu());

                deckMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        optionallyDelete(deck);
                        return true;
                    }
                });
                deckMenu.show();
            }
        });
    }

    private void copyDeck(Deck deck, String deckName) {
        File targetDir = new File(new File(getContext().getFilesDir(), "recordings"),
                String.format("copy-%d", (new Random()).nextInt()));
        targetDir.mkdirs();
        long deckId = getDbManager().addDeck(deckName, getContext().getString(R.string.data_self_publisher), (new Date()).getTime(), null, null, false, deck.getSrcLanguageIso());
        Dictionary[] dictionaries = getDbManager().getAllDictionariesForDeck(deck.getDbId());
        int dictionaryIndex = dictionaries.length - 1;
        try {
            for (Dictionary dictionary : dictionaries) {
                long dictionaryId = getDbManager().addDictionary(dictionary.getDestLanguageIso(), dictionary.getLabel(), dictionaryIndex, deckId);
                dictionaryIndex--;
                int translationDbIndex = dictionary.getTranslationCount() - 1;
                for (int i = 0; i < dictionary.getTranslationCount(); i++) {
                    Translation translation = dictionary.getTranslation(i);
                    String filename = translation.getFilename();
                    if (!translation.getIsAsset()) {
                        File srcFile = new File(filename);
                        File targetFile = new File(targetDir,
                                String.format("%s-%d",
                                        srcFile.getName(), (new Random()).nextInt()));
                        copyFile(srcFile, targetFile);
                        filename = targetFile.getAbsolutePath();
                    }
                    getDbManager().addTranslation(dictionaryId, translation.getLabel(), translation.getIsAsset(),
                            filename, translationDbIndex, translation.getTranslatedText());
                    translationDbIndex--;
                }
            }
        } catch (IOException e) {
            getDbManager().deleteDeck(deckId);
        }
    }

    private void copyFile(File src, File target) throws IOException {
        InputStream inStream = new FileInputStream(src);
        OutputStream outStream = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int read;
        while ((read = inStream.read(buf)) > 0) {
            outStream.write(buf, 0, read);
        }
        inStream.close();
        outStream.close();
    }
}