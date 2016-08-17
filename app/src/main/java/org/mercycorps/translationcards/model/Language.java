package org.mercycorps.translationcards.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Language implements Parcelable {
    private String name;
    private String iso;

    public Language(String iso, String name) {
        this.iso = iso;
        this.name = name;
    }

    protected Language(Parcel in) {
        name = in.readString();
        iso = in.readString();
    }

    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(iso);
    }
}
