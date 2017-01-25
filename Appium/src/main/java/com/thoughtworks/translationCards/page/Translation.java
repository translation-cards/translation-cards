package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/24/16.
 */
public class Translation {

    public WebDriver translationDriver;

    By lnk_next = By.id("org.mercycorps.translationcards:id/recording_label_next_text");
    By lnk_back = By.id("org.mercycorps.translationcards:id/enter_translated_phrase_back_label");
    By fld_yourTranslation = By.id("org.mercycorps.translationcards:id/translated_phrase_field");

    public Translation(WebDriver driver){
        this.translationDriver = driver;
    }

    public void setYourTranslation(String translation) {
        translationDriver.findElement(fld_yourTranslation).sendKeys(translation);
    }

    public void clickNext(){
        translationDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        translationDriver.findElement(lnk_back).click();
    }
}
