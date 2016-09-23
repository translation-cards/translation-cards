package org.mercycorps.translationcards.view;

import android.app.Activity;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Robolectric.buildActivity;


@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class ChipTest {

    private Chip chip;

    @Before
    public void setUp() throws Exception {
        chip = new Chip(buildActivity(Activity.class).create().get());
    }

    @Test
    public void shouldInflateALayoutFile() {
        assertNotNull(chip.findViewById(R.id.chip_layout));
    }

    @Test
    public void shouldHaveATextField() {
        assertNotNull(chip.findViewById(R.id.chip_text));
    }

    @Test
    public void shouldHaveAnImageField() {
        assertNotNull(chip.findViewById(R.id.delete_chip));
    }

    @Test
    public void shouldSetChipText() {
        String chipText = "Hot Chips!";

        chip.setText(chipText);

        TextView chipTextView = (TextView) chip.findViewById(R.id.chip_text);
        assertEquals(chipText, chipTextView.getText().toString());
    }

    @Test
    public void shouldSetOnDeleteListener() {
        Chip.OnDeleteListener onDeleteListener = mock(Chip.OnDeleteListener.class);
        chip.setOnDeleteListener(onDeleteListener);

        chip.deleteChip();

        verify(onDeleteListener).delete();
    }
}
