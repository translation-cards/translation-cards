package org.mercycorps.translationcards.activity.addDeck;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class DestinationLanguagesAdapterTest {

    public static final String A_LANGUAGE = "Some Language";

    @Test
    public void shouldAddLanguageToAdapter() {
        DestinationLanguagesAdapter destinationLanguagesAdapter = new DestinationLanguagesAdapter(mock(Context.class), new ArrayList<String>());
        destinationLanguagesAdapter.add(A_LANGUAGE);

        assertEquals(1, destinationLanguagesAdapter.getCount());
        assertEquals(A_LANGUAGE, destinationLanguagesAdapter.getItem(0));
    }

    @Test
    public void shouldNotAddDuplicateLanguageToAdapter() {
        DestinationLanguagesAdapter destinationLanguagesAdapter = new DestinationLanguagesAdapter(mock(Context.class), new ArrayList<String>());
        destinationLanguagesAdapter.add(A_LANGUAGE);
        destinationLanguagesAdapter.add(A_LANGUAGE);

        assertEquals(1, destinationLanguagesAdapter.getCount());
        assertEquals(A_LANGUAGE, destinationLanguagesAdapter.getItem(0));
    }
}