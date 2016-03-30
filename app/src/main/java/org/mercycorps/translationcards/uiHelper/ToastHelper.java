package org.mercycorps.translationcards.uiHelper;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {
    private static final int DEFAULT_DURATION = 2;

    public static void showToast(Context context, String textToShow, int duration){
        Toast toast = Toast.makeText(context, textToShow, duration);
        toast.show();
    }

    public static void showToast(Context context, String textToShow){
        showToast(context, textToShow, DEFAULT_DURATION);
    }
}
