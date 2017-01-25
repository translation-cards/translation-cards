package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class ReviewSave {
    public WebDriver reviewSaveDriver;

    By lnk_save = By.id("org.mercycorps.translationcards:id/deck_review_and_save_button_text");
    By lnk_back = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");

    public ReviewSave(WebDriver driver){
        this.reviewSaveDriver = driver;
    }

    public void clickSave(){
        reviewSaveDriver.findElement(lnk_save).click();
    }

    public void clickBack(){
        reviewSaveDriver.findElement(lnk_back).click();
    }
}
