package org.mercycorps.translationcards.activity;

import android.widget.Button;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Test for RecordingActivity
 *
 * @author patdale216@gmail.com (Pat Dale)
 */
@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class RecordingActivityTest {

    private RecordingActivity recordingActivity;

    @Before
    public void setUp() throws Exception {
        recordingActivity = Robolectric.setupActivity(RecordingActivity.class);
    }

    @Test
    public void onCreate_shouldHideActionBar() {
        assertThat(recordingActivity.getSupportActionBar().isShowing(), is(false));
    }

    @Test
    public void moveToInstructionsStep_shouldShowHeaderWhenMakingATranslationCard() {
        TextView instructionsHeader = (TextView) recordingActivity
                .findViewById(R.id.get_started_header);

        assertThat(instructionsHeader.getText().toString(), is("Make your own flashcards"));
    }

    @Test
    public void moveToInstructionsStep_shouldDisplayInstructionsToMakeTranslationCard() {
        TextView instructionsDetail = (TextView) recordingActivity
                .findViewById(R.id.get_started_detail);

        assertThat(instructionsDetail.getText().toString()
                , is("Write your script, record your phrase,\\nand use your flashcard in the field."));
    }

    @Test
    public void moveToInstructionsStep_shouldHaveCardCreationButton() {
        Button recordingButton = (Button) recordingActivity
                .findViewById(R.id.get_started_button);

        assertThat(recordingButton.getText().toString(), is("GET STARTED"));
    }
}