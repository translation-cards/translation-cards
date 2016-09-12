package com.thoughtworks.translationCards.page;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;

/**
 * Created by nliao on 8/24/16.
 */
public class ReviewSaveCard {

    public AndroidDriver reviewSaveCardDriver;

    By lnk_save = By.id("org.mercycorps.translationcards:id/recording_done_text");
    By lnk_back = By.id("org.mercycorps.translationcards:id/summary_activity_back");

    public ReviewSaveCard(AndroidDriver driver){
        this.reviewSaveCardDriver = driver;
    }

    public void clickSave() {
        reviewSaveCardDriver.findElement(lnk_save).click( );
    }

    public void clickBack(){
        reviewSaveCardDriver.findElement(lnk_back).click();
    }

}
