package org.mercycorps.translationcards.activity.addDeck.presenter;

import android.app.Activity;

public interface AddDeckView {
    void setActivityBitmap(int resId, int drawableId);
    void startActivityWithClass(Class<? extends Activity> activityClass);
}