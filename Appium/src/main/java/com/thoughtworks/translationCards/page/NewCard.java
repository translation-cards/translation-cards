package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/24/16.
 */
public class NewCard {
    public WebDriver newCardDriver;

    By btn_addNewCard = By.id("org.mercycorps.translationcards:id/add_translation_button");
    By btn_back = By.xpath(" //android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.ImageButton[1]");

    public NewCard(WebDriver driver){
        this.newCardDriver = driver;
    }

    public void clickAddNewCardButton() {
        newCardDriver.findElement(btn_addNewCard).click();
    }

    public void clickBackButton() {
        newCardDriver.findElement(btn_back).click();
    }

}
