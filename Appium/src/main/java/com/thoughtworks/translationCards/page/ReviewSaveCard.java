package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/24/16.
 */
public class ReviewSaveCard {

    public WebDriver reviewSaveCardDriver;

    By lnk_save = By.id("org.mercycorps.translationcards:id/recording_done_text");
    By lnk_back = By.id("org.mercycorps.translationcards:id/summary_activity_back");

    public ReviewSaveCard(WebDriver driver){
        this.reviewSaveCardDriver = driver;
    }

    public void clickSave() {
        reviewSaveCardDriver.findElement(lnk_save).click( );
    }

    public void clickBack(){
        reviewSaveCardDriver.findElement(lnk_back).click();
    }

}
