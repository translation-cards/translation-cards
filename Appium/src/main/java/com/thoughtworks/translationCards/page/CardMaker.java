package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/24/16.
 */
public class CardMaker {

    public WebDriver cardMakerDriver;

    By btn_getStarted = By.id("org.mercycorps.translationcards:id/get_started_button");

    public CardMaker(WebDriver driver){
        this.cardMakerDriver = driver;
    }

    public void clickGetStartedButton() {
        cardMakerDriver.findElement(btn_getStarted).click();
    }
}
