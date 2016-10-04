package org.mercycorps.translationcards.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.addDeck.NewDeckContext;
import org.mercycorps.translationcards.addDeck.activity.AddDeckActivity;
import org.mercycorps.translationcards.addDeck.activity.GetStartedDeckActivity;

public class Router {
    public static final int REQUEST_CODE_IMPORT_FILE_PICKER = 1;
    public static final int REQUEST_CODE_IMPORT_FILE = 2;
    public static final int REQUEST_CODE_CREATE_DECK = 3;

    public void launchFeedbackActivity(Activity activity) {
        Uri feedbackURL = Uri.parse(activity.getString(R.string.feedback_url) + BuildConfig.VERSION_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW, feedbackURL);
        activity.startActivity(intent);
    }

    public void launchImportActivityForResult(Activity activity, Uri data) {
        Intent intent = new Intent(activity, ImportActivity.class);
        intent.setData(data);
        activity.startActivityForResult(intent, REQUEST_CODE_IMPORT_FILE);
    }

    public void launchCreateDeckFlow(Activity activity, NewDeckContext newDeckContext) {
        Intent createIntent = new Intent(activity, GetStartedDeckActivity.class);
        createIntent.putExtra(AddDeckActivity.INTENT_KEY_DECK, newDeckContext);
        activity.startActivityForResult(createIntent, Router.REQUEST_CODE_CREATE_DECK);
    }

    public void launchFilePicker(Activity activity) {
        Intent samsungIntent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
        samsungIntent.addCategory(Intent.CATEGORY_DEFAULT);
        if (activity.getPackageManager().resolveActivity(samsungIntent, 0) != null) {
            // TODO: Which one of these do we need for samsung devices?
            samsungIntent.setType("file/*");
            samsungIntent.putExtra("CONTENT_TYPE", "*/*");
            activity.startActivityForResult(samsungIntent, Router.REQUEST_CODE_IMPORT_FILE_PICKER);
        } else {
            Intent fileIntent = new Intent(Intent.ACTION_GET_CONTENT);
            fileIntent.setType("file/*");
            activity.startActivityForResult(fileIntent, Router.REQUEST_CODE_IMPORT_FILE_PICKER);
        }
    }
}
