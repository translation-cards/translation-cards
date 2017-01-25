package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class DeckTitle {
    public WebDriver deckTitleDriver;

    By fld_deckTitle = By.id("org.mercycorps.translationcards:id/deck_title_input");
    By lnk_next = By.id("org.mercycorps.translationcards:id/enter_deck_title_next_text");
    By lnk_back = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");


    public DeckTitle(WebDriver driver){
        this.deckTitleDriver = driver;
    }

    public void setDeckTitle(String title) {
        deckTitleDriver.findElement(fld_deckTitle).sendKeys(title);
    }

    public void clickNext(){
        deckTitleDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        deckTitleDriver.findElement(lnk_back).click();
    }

}
