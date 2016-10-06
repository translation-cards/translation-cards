package org.mercycorps.translationcards.addDeck.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.myDecks.MyDecksActivity;
import org.mercycorps.translationcards.addDeck.activity.EnterDeckTitleActivity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class GetStartedDeckPresenterTest {

    private AddDeckView activity;
    private GetStartedDeckPresenter getStartedDeckPresenter;

    @Before
    public void setUp() throws Exception {
        activity = mock(AddDeckView.class);
        getStartedDeckPresenter = new GetStartedDeckPresenter(activity);
    }

    @Test
    public void shouldInflateBitmaps() {
        getStartedDeckPresenter.inflateBitmaps();

        verify(activity).setActivityBitmap(R.id.deck_get_started_image, R.drawable.get_started_image);
    }

    @Test
    public void shouldStartEnterDeckTitleActivityWhenWhenGetStartButtonClicked() {
        getStartedDeckPresenter.getStartedButtonClicked();

        verify(activity).startActivityWithClass(EnterDeckTitleActivity.class);
    }

    @Test
    public void shouldStartMyDecksActivityWhenBackButtonIsClicked() {
        getStartedDeckPresenter.backButtonClicked();

        verify(activity).startActivityWithClass(MyDecksActivity.class);
    }
}