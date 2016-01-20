package org.mercycorps.translationcards;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.File;

/**
 * Activity for exporting.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class ExportActivity extends AppCompatActivity {

    private static final String TAG = "ExportActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_options);
    }

    private TxcPortingUtility.ExportException buildFile(File targetFile) {
        TxcPortingUtility portingUtility = new TxcPortingUtility();
        if (targetFile.exists()) {
            targetFile.delete();
        }
        DbManager dbm = new DbManager(this);
        try {
            portingUtility.exportData(dbm.getAllDictionaries(), targetFile);
        } catch (TxcPortingUtility.ExportException e) {
            return e;
        }
        return null;
    }

    public void onSelectOptionEmailAttachment(View v) {
        File targetFile = new File(getExternalCacheDir(), "export.txc");
        if (targetFile.exists()) {
            targetFile.delete();
        }
        TxcPortingUtility.ExportException buildError = buildFile(targetFile);
        if (buildError != null) {
            Log.d(TAG, "Failed to build file.");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(targetFile));
        startActivity(intent);
    }
}
