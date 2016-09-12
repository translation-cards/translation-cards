package com.thoughtworks.translationCards.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Created by nliao on 8/23/16.
 */
public class NewDeck {
    public AndroidDriver deckMakerDriver;

    By btn_getStarted = By.id("org.mercycorps.translationcards:id/deck_get_started_button");

    public NewDeck(AndroidDriver driver){
        this.deckMakerDriver = driver;
    }

    public void clickGetStartedButton() {
        deckMakerDriver.findElement(btn_getStarted).click();
    }

}
