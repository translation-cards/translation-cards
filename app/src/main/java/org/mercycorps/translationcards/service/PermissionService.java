package org.mercycorps.translationcards.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by abeltamrat on 6/29/16.
 */
public class PermissionService {

    public static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 0;

    public boolean checkPermission(Context context, String permissionToCheck){
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, permissionToCheck) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean permissionGranted(int[] grantResults) {
        return grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }

    public void requestPermissions(Activity activity, String permissionToRequest, int reqCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permissionToRequest}, reqCode);
    }
}
