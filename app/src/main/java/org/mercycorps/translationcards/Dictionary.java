/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.mercycorps.translationcards;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Contains information about a set of phrases for a particular language.
 *
 * @author nick.c.worden@gmail.com (Nick Worden)
 */
public class Dictionary {

    private final String label;
    private final Translation[] translations;
    private final long dbId;

    public Dictionary(String label, Translation[] translations, long dbId) {
        this.label = label;
        this.translations = translations;
        this.dbId = dbId;
    }

    public Dictionary(String label, Translation[] translations) {
        this(label, translations, -1);
    }

    public String getLabel() {
        return label;
    }

    public int getTranslationCount() {
        return translations.length;
    }

    public Translation getTranslation(int index) {
        return translations[index];
    }

    public long getDbId() {
        return dbId;
    }

    /**
     * Contains information about a single phrase.
     */
    public static class Translation {

        private final String label;
        private final boolean isAsset;
        private final String filename;
        private final long dbId;

        public Translation(String label, boolean isAsset, String filename, long dbId) {
            this.label = label;
            this.isAsset = isAsset;
            this.filename = filename;
            this.dbId = dbId;
        }

        public Translation(String label, boolean isAsset, String filename) {
            this(label, isAsset, filename, -1);
        }

        public String getLabel() {
            return label;
        }

        public boolean getIsAsset() {
            return isAsset;
        }

        public String getFilename() {
            return filename;
        }

        public long getDbId() {
            return dbId;
        }

        public void setMediaPlayerDataSource(Context context, MediaPlayer mp) throws IOException {
            if (isAsset) {
                AssetFileDescriptor fd = context.getAssets().openFd(filename);
                mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                fd.close();
            } else {
                mp.setDataSource(new FileInputStream(filename).getFD());
            }
        }
    }
}
