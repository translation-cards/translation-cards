package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/24/16.
 */
public class WriteYourPhrase {

    public WebDriver writeYourPhraseDriver;

    By lnk_next = By.id("org.mercycorps.translationcards:id/activity_enter_source_phrase_next_text");
    By lnk_back = By.id("org.mercycorps.translationcards:id/enter_source_phrase_activity_back_label");
    By fld_yourPhrase = By.id("org.mercycorps.translationcards:id/source_phrase_field");

    public WriteYourPhrase(WebDriver driver){
        this.writeYourPhraseDriver = driver;
    }

    public void setYourPhrase(String phrase) {
        writeYourPhraseDriver.findElement(fld_yourPhrase).sendKeys(phrase);
    }

    public void clickNext(){
        writeYourPhraseDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        writeYourPhraseDriver.findElement(lnk_back).click();
    }
}
