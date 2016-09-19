package org.mercycorps.translationcards.addDeck.presenter;

import android.app.Activity;

public interface AddDeckView {
    void setActivityBitmap(int resId, int drawableId);
    void startActivityWithClass(Class<? extends Activity> activityClass);
    void startActivityForResult(Class<? extends Activity> activityClass, int requestCode);
}
