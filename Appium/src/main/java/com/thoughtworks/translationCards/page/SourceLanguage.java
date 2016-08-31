package com.thoughtworks.translationCards.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Created by nliao on 8/23/16.
 */
public class SourceLanguage {
    public AndroidDriver sourceLanguageDriver;

    By lnk_next = By.id("org.mercycorps.translationcards:id/deck_source_language_next_text");
    By lnk_back = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");

    public SourceLanguage(AndroidDriver driver){
        this.sourceLanguageDriver = driver;
    }

    public void clickNext(){
        sourceLanguageDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        sourceLanguageDriver.findElement(lnk_back).click();
    }
}
