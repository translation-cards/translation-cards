package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class NewDeck {
    public WebDriver deckMakerDriver;

    By btn_getStarted = By.id("org.mercycorps.translationcards:id/deck_get_started_button");

    public NewDeck(WebDriver driver){
        this.deckMakerDriver = driver;
    }

    public void clickGetStartedButton() {
        deckMakerDriver.findElement(btn_getStarted).click();
    }

}
