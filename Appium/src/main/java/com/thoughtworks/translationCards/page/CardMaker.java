package com.thoughtworks.translationCards.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Created by nliao on 8/24/16.
 */
public class CardMaker {

    public AndroidDriver cardMakerDriver;

    By btn_getStarted = By.id("org.mercycorps.translationcards:id/get_started_button");

    public CardMaker(AndroidDriver driver){
        this.cardMakerDriver = driver;
    }

    public void clickGetStartedButton() {
        cardMakerDriver.findElement(btn_getStarted).click();
    }
}
