package org.mercycorps.translationcards.porting;

import android.app.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class InputStreamBuilder {
    Application application;

    public InputStreamBuilder(Application application) {
        this.application = application;
    }

    public FileInputStream getFileInputStream(String filePath) throws FileNotFoundException {
        return new FileInputStream(new File(filePath));
    }

    public FileInputStream getAssetInputStream(String assetName) throws IOException {
        return application.getApplicationContext().getAssets().openFd(assetName).createInputStream();
    }
}
