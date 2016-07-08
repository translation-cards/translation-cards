package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.model.Deck;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.repository.LanguageRepository;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.mockito.ArgumentCaptor;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findLinearLayout;
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
    @Ignore
    public void shouldLaunchLanguageSelectorWhenLanguageNameIsTapped() {
        Activity activity = helper.createActivityToTest();
        ShadowActivity shadowActivity = shadowOf(activity);

        click(activity, R.id.deck_source_language_view);

        assertEquals(DestinationLanguageSelectorActivity.class.getName(), shadowActivity.getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    @Ignore
    //TODO assert that language is saved on return from selector activity
    public void shouldSaveSourceLanguageToContextWhenLanguageSelectorReturns() {
        NewDeckContext newDeckContext = mock(NewDeckContext.class);
        Activity activity = helper.createActivityToTestWithContext(newDeckContext);
        setText(activity, R.id.deck_source_language_input, helper.SPANISH_SOURCE_LANGUAGE);
        click(activity, R.id.deck_source_language_next_label);
        Language spanish = new LanguageRepository().withName(helper.SPANISH_SOURCE_LANGUAGE);

        ArgumentCaptor<Language> argumentCaptor = ArgumentCaptor.forClass(Language.class);
        verify(newDeckContext).setSourceLanguage(argumentCaptor.capture());

        assertEquals(argumentCaptor.getValue().getName(), spanish.getName());
    }

    @Test
    public void shouldNotChangeSourceLanguageIfLanguageSelectorIsCancelled() {

    }

    @Test
    public void shouldDefaultLanguageToEnglishIfNotSetInNewDeckContext() {

    }
}