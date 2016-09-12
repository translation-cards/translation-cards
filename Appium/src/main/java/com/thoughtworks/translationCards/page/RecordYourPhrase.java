package com.thoughtworks.translationCards.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Created by nliao on 8/24/16.
 */
public class RecordYourPhrase {
    public AndroidDriver recordYourPhraseDriver;

    By lnk_next = By.id("org.mercycorps.translationcards:id/recording_audio_next_text");
    By lnk_back = By.id("org.mercycorps.translationcards:id/record_activity_back");
    By btn_record = By.id("org.mercycorps.translationcards:id/record_audio_button");
    By btn_gotIt = By.id("android:id/button1");
    By btn_allow = By.id("com.android.packageinstaller:id/permission_allow_button");

    public RecordYourPhrase(AndroidDriver driver){
        this.recordYourPhraseDriver = driver;
    }

    public void clickRecord() {
        recordYourPhraseDriver.findElement(btn_record).click( );
    }

    public void clickGotIt() {
        recordYourPhraseDriver.findElement(btn_gotIt).click( );
    }

    public void clickAllow() {
        recordYourPhraseDriver.findElement(btn_allow).click( );
    }

    public void clickNext(){
        recordYourPhraseDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        recordYourPhraseDriver.findElement(lnk_back).click();
    }

}
