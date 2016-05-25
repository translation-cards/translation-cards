package org.mercycorps.translationcards.activity.addTranslation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import org.mercycorps.translationcards.R;
import org.mercycorps.translationcards.activity.translations.TranslationsActivity;

import butterknife.OnClick;

public class GetStartedActivity extends AddTranslationActivity {

    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 0;

    @Override
    public void inflateView() {
        setContentView(R.layout.activity_card_get_started);
    }

    @Override
    public void setBitmapsForActivity() {
        setBitmap(R.id.get_started_image, R.drawable.get_started_image);
    }

    @OnClick(R.id.get_started_button)
    public void getStartedButtonClick() {
        if (checkRecordingPermission()) {
            startNextActivity(GetStartedActivity.this, EnterSourcePhraseActivity.class);
        } else {
            ActivityCompat.requestPermissions(GetStartedActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSIONS_REQUEST_RECORD_AUDIO);
        }
    }

    @OnClick(R.id.get_started_back)
    public void getStartedBackButtonClicked() {
        startNextActivity(GetStartedActivity.this, TranslationsActivity.class);
    }

    private boolean checkRecordingPermission() {
        if (Build.VERSION.SDK_INT < 23) {
            return true;
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startNextActivity(GetStartedActivity.this, EnterSourcePhraseActivity.class);
        } else {
            startNextActivity(GetStartedActivity.this, TranslationsActivity.class);
        }
    }

}
