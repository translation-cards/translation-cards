package org.mercycorps.translationcards.activity.translations;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.data.Translation;
import org.mercycorps.translationcards.service.TranslationService;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class CardDeleteClickListenerTest {

    // Dependencies
    @Mock Translation translation;
    @Mock TranslationsActivity translationsActivity;
    @Mock TranslationService translationService;
    @Mock AlertDialog.Builder alertDialogBuilder;
    @Mock View view;

    final Context context = RuntimeEnvironment.application;
    CardDeleteClickListener cardDeleteClickListener;

    @Before
    public void setup() {
        initMocks(this);

        when(translation.getLabel()).thenReturn("translation label");

        cardDeleteClickListener = new CardDeleteClickListener(
                translationsActivity,
                translation,
                translationService,
                alertDialogBuilder);
    }

    @Test
    public void shouldSetTitleOfAlertDialog() throws Exception {
        cardDeleteClickListener.onClick(view);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(alertDialogBuilder).setTitle(argumentCaptor.capture());

        String expected = context.getString(R.string.delete_dialog_title);
        String actual = context.getString(argumentCaptor.getValue());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldSetMessageOfAlertDialog() throws Exception {
        cardDeleteClickListener.onClick(view);

        ArgumentCaptor<Integer> argumentCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(alertDialogBuilder).setMessage(argumentCaptor.capture());

        String expected = context.getString(R.string.delete_dialog_message);
        String actual = context.getString(argumentCaptor.getValue());
        assertEquals(expected, actual);
    }

    @Test
    public void shouldDeleteTranslationAndUpdateView() {
        cardDeleteClickListener.deleteTranslation();

        verify(translationService).deleteTranslation("translation label");
        verify(translationsActivity).updateView();
    }

    @Test
    public void shouldShowAlertDialog() throws Exception {
        cardDeleteClickListener.onClick(view);

        verify(alertDialogBuilder).show();
    }
}