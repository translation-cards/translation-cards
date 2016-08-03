package org.mercycorps.translationcards.activity.addDeck;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.MainApplication;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.dagger.TestBaseComponent;
import org.mercycorps.translationcards.model.Language;
import org.mercycorps.translationcards.service.LanguageService;
import org.mercycorps.translationcards.util.AddDeckActivityHelper;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import javax.inject.Inject;

import static junit.framework.Assert.assertEquals;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.click;
import static org.mercycorps.translationcards.util.TestAddTranslationCardActivityHelper.findImageView;
import static org.mockito.Mockito.when;
import static org.robolectric.Shadows.shadowOf;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class EnterDeckSourceLanguageActivityTest {

    @Inject LanguageService languageService;

    AddDeckActivityHelper<EnterDeckSourceLanguageActivity> helper = new AddDeckActivityHelper<>(EnterDeckSourceLanguageActivity.class);

    @Before
    public void setUp() throws Exception {
        MainApplication application = (MainApplication) RuntimeEnvironment.application;
        ((TestBaseComponent) application.getBaseComponent()).inject(this);
    }

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
    public void shouldLaunchLanguageSelectorWhenLanguageNameIsTapped() {
        Activity activity = helper.createActivityToTest();
        ShadowActivity shadowActivity = shadowOf(activity);

        click(activity, R.id.deck_source_language_view);

        assertEquals(LanguageSelectorActivity.class.getName(), shadowActivity.getNextStartedActivity().getComponent().getClassName());
    }

    @Test
    public void shouldSaveSourceLanguageToContextWhenLanguageSelectorReturns() {
        NewDeckContext newDeckContext = new NewDeckContext();
        EnterDeckSourceLanguageActivity activity = (EnterDeckSourceLanguageActivity) helper.createActivityToTestWithContext(newDeckContext);
        when(languageService.getLanguageWithName("Spanish")).thenReturn(new Language("", "Spanish"));
        Intent data = new Intent();
        data.putExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "Spanish");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_OK, data);
        assertEquals(newDeckContext.getSourceLanguage(),"Spanish");
        assertEquals("Spanish", ((TextView) activity.findViewById(R.id.deck_source_language_view)).getText());
    }

    @Test
    public void shouldNotChangeSourceLanguageIfLanguageSelectorIsCancelled() {
        NewDeckContext newDeckContext = new NewDeckContext();
        EnterDeckSourceLanguageActivity activity = (EnterDeckSourceLanguageActivity) helper.createActivityToTestWithContext(newDeckContext);
        CharSequence expectedValue = ((TextView) activity.findViewById(R.id.deck_source_language_view)).getText();
        Intent data = new Intent();
        data.putExtra(LanguageSelectorActivity.SELECTED_LANGUAGE_KEY, "Spanish");
        activity.onActivityResult(EnterDeckDestinationLanguagesActivity.REQUEST_CODE, Activity.RESULT_CANCELED, data);
        assertEquals(newDeckContext.getSourceLanguage(),expectedValue);
        assertEquals(expectedValue, ((TextView) activity.findViewById(R.id.deck_source_language_view)).getText());
    }

    @Test
    public void shouldDefaultLanguageToEnglishIfNotSetInNewDeckContext() {
        NewDeckContext newDeckContext = new NewDeckContext();
        EnterDeckSourceLanguageActivity activity = (EnterDeckSourceLanguageActivity) helper.createActivityToTestWithContext(newDeckContext);
        assertEquals(newDeckContext.getSourceLanguage(),"English");
        assertEquals("English", ((TextView) activity.findViewById(R.id.deck_source_language_view)).getText());
    }
}