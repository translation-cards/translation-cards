package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.setText;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckSourceLanguageActivityTest {

    AddDeckActivityHelper<EnterDeckSourceLanguageActivity> helper = new AddDeckActivityHelper<>(EnterDeckSourceLanguageActivity.class);

    @After
    public void teardown() {
        helper.teardown();
    }

    @Test
    public void shouldReturnToEnterDeckTitleActivityWhenBackButtonIsClicked(){
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.deck_source_language_back_arrow);
        assertEquals(EnterDeckTitleActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldStartDeckDestinationLanguagesActivityWhenNextButtonIsClicked(){
        Activity activity = helper.createActivityToTest();
        click(activity, R.id.deck_source_language_next_label);
        assertEquals(EnterDeckDestinationLanguagesActivity.class.getName(), shadowOf(activity).getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldInflateEnterSourceLanguageImageWhenActivityIsCreated() {
        Activity activity = helper.createActivityToTest();
        ImageView imageView = findImageView(activity, R.id.deck_source_language_image);
        assertEquals(R.drawable.enter_phrase_image, shadowOf(imageView.getDrawable()).getCreatedFromResId());
    }

    @Test
    public void shouldSaveSourceLanguageToContextWhenNextButtonIsClicked() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.deck_source_language_text, helper.DEFAULT_SOURCE_LANGUAGE);
        click(activity, R.id.deck_source_language_next_label);
        verify(newDeckContext).setSourceLanguageIso(helper.DEFAULT_SOURCE_LANGUAGE_ISO);
    }

    @Test
    public void shouldSetSourceLanguagePickerAsClickable() {
        Activity activity = helper.createActivityToTest();
        View sourceLanguagePicker = activity.findViewById(R.id.deck_source_language_picker);
        assertTrue(sourceLanguagePicker.isClickable());
    }
}