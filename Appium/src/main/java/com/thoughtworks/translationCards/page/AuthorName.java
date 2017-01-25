package com.thoughtworks.translationCards.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by nliao on 8/23/16.
 */
public class AuthorName {

    public WebDriver authorNameDriver;

    By fld_organization = By.id("org.mercycorps.translationcards:id/deck_author_input");
    By language = By.id("android:id/text1");
    By lnk_next = By.id("org.mercycorps.translationcards:id/next_button");
    By lnk_back = By.xpath("//android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.view.ViewGroup[1]/android.widget.FrameLayout[1]/android.widget.RelativeLayout[1]/android.widget.RelativeLayout[1]/android.widget.LinearLayout[1]/android.widget.ImageView[1]");

    public AuthorName(WebDriver driver){
        this.authorNameDriver = driver;
    }

    public void setOrganization(String organization){
        authorNameDriver.findElement(fld_organization).sendKeys(organization);
    }

    public void clickNext(){
        authorNameDriver.findElement(lnk_next).click();
    }

    public void clickBack(){
        authorNameDriver.findElement(lnk_back).click();
    }

}
