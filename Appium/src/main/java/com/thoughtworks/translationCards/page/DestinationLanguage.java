package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class DestinationLanguage {

    public WebDriver destinationLanguageDriver;

    By btn_addLanguage = By.id("org.mercycorps.translationcards:id/add_language_button");
    By fld_addLanguage = By.id("org.mercycorps.translationcards:id/language_filter_field");
    By language = By.id("android:id/text1");
    By lnk_next = By.id("org.mercycorps.translationcards:id/next_button");
    By lnk_back = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");

    public DestinationLanguage(WebDriver driver){
        this.destinationLanguageDriver = driver;
    }

    public void clickAddLanguage(){
        destinationLanguageDriver.findElement(btn_addLanguage).click();
    }

    public void setLanguage(String destLanguage){
        destinationLanguageDriver.findElement(fld_addLanguage).sendKeys(destLanguage);
        destinationLanguageDriver.findElement(language).click();
    }

    public void clickNext(){
        destinationLanguageDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        destinationLanguageDriver.findElement(lnk_back).click();
    }

}
