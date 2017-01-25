package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class MyDecks {
    public WebDriver myDecksDriver;

    By btn_makeDeck = By.id("org.mercycorps.translationcards:id/create_deck_button");
    By btn_importDeck = By.id("org.mercycorps.translationcards:id/import_deck_button");
    By btn_giveFeedback = By.id("org.mercycorps.translationcards:id/feedback_button");
    By btn_deckName = By.id("org.mercycorps.translationcards:id/deck_name");
    By btn_deckOptions = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[2]/android.widget.RelativeLayout[1]/android.widget.ListView[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.widget.ImageView[1]");
    By btn_sharePopUp = By.id("android:id/title");
    By btn_confirmSharePopUp = By.id("android:id/button1");
    By confirmationMessage = By.id("android:id/alertTitle");

    public MyDecks(WebDriver driver){
        this.myDecksDriver = driver;
    }

    public void clickMakeDeckButton() {
        myDecksDriver.findElement(btn_makeDeck).click();
    }

    public void clickImportDeckButton() {
        myDecksDriver.findElement(btn_importDeck).click();
    }

    public void clickGiveFeedBackButton() {
        myDecksDriver.findElement(btn_giveFeedback).click();
    }

    public void clickDeckNameButton() {
        myDecksDriver.findElement(btn_deckName).click();
    }

    public void clickDeckOptions() {
        myDecksDriver.findElement(btn_deckOptions).click();
    }

    public void clickSharePopUp() {
        myDecksDriver.findElement(btn_sharePopUp).click();
    }

    public void clickConfirmSharePopUp() {
        myDecksDriver.findElement(btn_confirmSharePopUp).click();
    }

    public String getConfirmationMessage(){
        return myDecksDriver.findElement(confirmationMessage).getText();
    }
}
