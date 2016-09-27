package org.mercycorps.translationcards.view;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static android.support.v4.content.ContextCompat.getColor;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class NextButtonTest {

    private NextButton nextButton;

    @Before
    public void setUp() throws Exception {
        nextButton = new NextButton(buildActivity(Activity.class).create().get());
    }

    @Test
    public void shouldEnableNextButton() {
        nextButton.enable();

        assertTrue(nextButton.findViewById(R.id.next_button_layout).isClickable());

        TextView nextButtonLabelText = (TextView) nextButton.findViewById(R.id.next_button_text);
        assertEquals(getColor(nextButton.getContext(), R.color.primaryTextColor), nextButtonLabelText.getCurrentTextColor());

        ImageView nextButtonImage = (ImageView) nextButton.findViewById(R.id.next_button_image);
        assertEquals(R.drawable.forward_arrow, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }

    @Test
    public void shouldDisableNextButton() {
        nextButton.disable();

        assertFalse(nextButton.findViewById(R.id.next_button_layout).isClickable());

        TextView nextButtonLabelText = (TextView) nextButton.findViewById(R.id.next_button_text);
        assertEquals(getColor(nextButton.getContext(), R.color.textDisabled), nextButtonLabelText.getCurrentTextColor());

        ImageView nextButtonImage = (ImageView) nextButton.findViewById(R.id.next_button_image);
        assertEquals(R.drawable.forward_arrow_disabled, shadowOf(nextButtonImage.getBackground()).getCreatedFromResId());
    }
}