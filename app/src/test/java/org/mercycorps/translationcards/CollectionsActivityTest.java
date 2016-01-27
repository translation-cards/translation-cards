package org.mercycorps.translationcards;

import android.content.Intent;
import android.widget.Button;
import android.widget.ListView;
import android.net.Uri;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class CollectionsActivityTest {

    private CollectionsActivity collectionsActivity;

    @Before
    public void setUp() throws Exception {
        collectionsActivity = Robolectric.setupActivity(CollectionsActivity.class);
    }

    @Test
    public void onCreate_shouldCreateCollectionsActivity() {
        assertNotNull(collectionsActivity);

        ListView cardCollections = (ListView) collectionsActivity.findViewById(R.id.collections_list);
        assertThat(cardCollections, is(notNullValue()));
    }

    @Test
    public void initFeedbackButton_shouldShowFeedbackButton() {
        Button feedbackButton = (Button) collectionsActivity.findViewById(R.id.main_feedback_button);

        assertThat(feedbackButton.getText().toString(), is("GIVE FEEDBACK"));
    }

    @Test
    public void initFeedbackButton_shouldLaunchFeedbackForm() {
        collectionsActivity.findViewById(R.id.main_feedback_button).performClick();

        Intent expectedIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://docs.google.com/forms/d/1p8nJlpFSv03MXWf67pjh_fHyOfjbK9LJgF8hORNcvNM/viewform?entry.1158658650=0.2.1"));
        assertThat(shadowOf(collectionsActivity).getNextStartedActivity(), is(expectedIntent));
    }
}
