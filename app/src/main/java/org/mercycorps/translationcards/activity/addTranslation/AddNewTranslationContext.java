package org.mercycorps.translationcards.activity.addTranslation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class AddNewTranslationContext implements Parcelable {

    private List<NewTranslation> newTranslations;
    private boolean isEdit;

    public AddNewTranslationContext(List<NewTranslation> newTranslations, boolean isEdit) {
        this.newTranslations = newTranslations;
        this.isEdit = isEdit;
    }

    public AddNewTranslationContext(List<NewTranslation> newTranslations) {
        this(newTranslations, false);
    }

    protected AddNewTranslationContext(Parcel in) {
        newTranslations = in.createTypedArrayList(NewTranslation.CREATOR);
        isEdit = in.readByte() != 0;
    }

    public static final Creator<AddNewTranslationContext> CREATOR = new Creator<AddNewTranslationContext>() {
        @Override
        public AddNewTranslationContext createFromParcel(Parcel in) {
            return new AddNewTranslationContext(in);
        }

        @Override
        public AddNewTranslationContext[] newArray(int size) {
            return new AddNewTranslationContext[size];
        }
    };

    public List<NewTranslation> getNewTranslations() {
        return newTranslations;
    }

    public String getSourcePhrase() {
        return newTranslations.isEmpty() ? "" : getTranslationsSourcePhrase();
    }

    private String getTranslationsSourcePhrase() {
        return newTranslations.get(0).getTranslation().getLabel();
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setSourceText(String sourceText) {
        for (NewTranslation newTranslation : newTranslations) {
            newTranslation.setSourceText(sourceText);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(newTranslations);
        parcel.writeByte((byte) (isEdit ? 1 : 0));
    }
}
