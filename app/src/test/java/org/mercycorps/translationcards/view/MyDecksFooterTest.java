package org.mercycorps.translationcards.view;

import android.app.Activity;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Robolectric.buildActivity;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class MyDecksFooterTest {

    private MyDecksFooter myDecksFooter;

    @Before
    public void setUp() throws Exception {
        myDecksFooter = new MyDecksFooter(buildActivity(Activity.class).create().get());
    }

    @Test
    public void shouldUpdateWhenDecksAreNotInView() {
        myDecksFooter.emptyDecksView();

        assertEquals(View.VISIBLE, myDecksFooter.findViewById(R.id.empty_my_decks_message).getVisibility());
        assertEquals(View.VISIBLE, myDecksFooter.findViewById(R.id.empty_my_decks_title).getVisibility());
        assertEquals(View.GONE, myDecksFooter.findViewById(R.id.feedback_button).getVisibility());
    }

    @Test
    public void shouldUpdateWhenDecksAreInView() {
        myDecksFooter.nonEmptyDecksView();

        assertEquals(View.GONE, myDecksFooter.findViewById(R.id.empty_my_decks_message).getVisibility());
        assertEquals(View.GONE, myDecksFooter.findViewById(R.id.empty_my_decks_title).getVisibility());
        assertEquals(View.VISIBLE, myDecksFooter.findViewById(R.id.feedback_button).getVisibility());
    }
}