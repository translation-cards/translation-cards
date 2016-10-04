package org.mercycorps.translationcards.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.R;

public class Router {
    public void launchFeedbackActivity(Context context) {
        Uri feedbackURL = Uri.parse(context.getString(R.string.feedback_url) + BuildConfig.VERSION_NAME);
        Intent intent = new Intent(Intent.ACTION_VIEW, feedbackURL);
        context.startActivity(intent);
    }
}
