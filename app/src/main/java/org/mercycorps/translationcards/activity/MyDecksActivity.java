package org.mercycorps.translationcards.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.repository.DeckRepository;
import org.mercycorps.translationcards.view.MyDecksFooter;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;

public class MyDecksActivity extends AbstractTranslationCardsActivity implements MyDecksPresenter.MyDecksView {


    @Bind(R.id.my_decks_list)ListView myDeckListView;

    @Inject DeckRepository deckRepository;
    @Inject Router router;

    private MyDecksFooter myDecksFooter;
    private MyDecksPresenter myDecksPresenter;
    private MyDeckAdapter myDecksAdapter;

    @Override
    public void inflateView() {
        MainApplication application = (MainApplication) getApplication();
        application.getBaseComponent().inject(this);
        setContentView(R.layout.activity_my_decks);
        inflateListHeaderAndFooter();
        myDecksPresenter = new MyDecksPresenter(this, deckRepository);
    }

    @Override
    public void initStates() {
        setActionBarTitle();
        myDecksAdapter = new MyDeckAdapter(this, myDecksPresenter);
        myDeckListView.setAdapter(myDecksAdapter);
    }

    @OnClick(R.id.import_deck_button)
    public void importDeckButtonClicked() {
        router.launchFilePicker(this);
    }

    @OnClick(R.id.create_deck_button)
    public void createDeckButtonClicked() {
        router.launchCreateDeckFlow(this, new NewDeckContext());
    }

    @OnClick(R.id.feedback_button)
    public void feedbackButtonClicked() {
        router.launchFeedbackActivity(this);
    }

    @Override
    protected void onResume() {
        myDecksPresenter.refreshMyDecksList();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Router.REQUEST_CODE_IMPORT_FILE_PICKER:
                if (resultCode == RESULT_OK) {
                    router.launchImportActivityForResult(this, data.getData());
                }
                break;
            case Router.REQUEST_CODE_IMPORT_FILE:
                if (resultCode == RESULT_OK) {
                    myDecksPresenter.refreshMyDecksList();
                }
                break;
            case Router.REQUEST_CODE_CREATE_DECK:
                if (resultCode == RESULT_OK) {
                    myDecksPresenter.refreshMyDecksList();
                }
                break;
        }
    }


    private void inflateListHeaderAndFooter() {
        inflateListHeader();
        inflateListFooter();
    }

    private void inflateListHeader() {
        View headerView = getLayoutInflater().inflate(R.layout.my_decks_header, myDeckListView, false);
        ((ListView) findViewById(R.id.my_decks_list)).addHeaderView(headerView);
    }

    private void inflateListFooter() {
        myDecksFooter = new MyDecksFooter(this);
        ((ListView) findViewById(R.id.my_decks_list)).addFooterView(myDecksFooter);
    }

    private void setActionBarTitle() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.my_decks);
        }
    }

    // MyDecksView Implementation
    @Override
    public void emptyViewState() {
        myDecksFooter.emptyDecksView();
    }

    @Override
    public void nonEmptyViewState() {
        myDecksFooter.nonEmptyDecksView();
    }

    @Override
    public void updateMyDeckListCentered(int isCentered) {
        final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) myDeckListView.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, isCentered);
        myDeckListView.setLayoutParams(params);
    }

    @Override
    public void updateMyDecksList(List<Deck> decks) {
        myDecksAdapter.setDecks(decks);
    }
}
