package com.thoughtworks.translationCards;

import com.thoughtworks.translationCards.page.AuthorName;
import com.thoughtworks.translationCards.page.CardMaker;
import com.thoughtworks.translationCards.page.DeckTitle;
import com.thoughtworks.translationCards.page.DestinationLanguage;
import com.thoughtworks.translationCards.page.MyDecks;
import com.thoughtworks.translationCards.page.NewCard;
import com.thoughtworks.translationCards.page.NewDeck;
import com.thoughtworks.translationCards.page.RecordYourPhrase;
import com.thoughtworks.translationCards.page.ReviewSave;
import com.thoughtworks.translationCards.page.ReviewSaveCard;
import com.thoughtworks.translationCards.page.SourceLanguage;
import com.thoughtworks.translationCards.page.Translation;
import com.thoughtworks.translationCards.page.WriteYourPhrase;
import com.thoughtworks.translationCards.utilities.Utilities;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.URL;

import io.appium.java_client.android.AndroidDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by nliao on 8/23/16.
 */
public class FunctionalTests {

    public AndroidDriver driver;

    @Before
    public void setUp() throws Exception {
        File appDir = new File("./");
        File app = new File(appDir, "app-debug.apk");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device", "android23Emulator");

        capabilities.setCapability("deviceName", "test_device");
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", "6.0");
//        capabilities.setCapability("browserName", MobileBrowserType.BROWSER);
        capabilities.setCapability("unicodekeyboard", true);
        capabilities.setCapability("resetkeyboard", true);
        capabilities.setCapability("app", app.getAbsolutePath());

//        capabilities.setCapability("app", "Chrome");
//        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "test_device");
//        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
//        capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
//        capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
//        capabilities.setCapability(MobileCapabilityType.VERSION, "6.0");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown() throws Exception {
        driver.quit();
    }

    @Test
    public void createDeck() throws InterruptedException {
        String str_assert = "Select conversation";

        MyDecks mdp = new MyDecks(driver);
        mdp.clickMakeDeckButton();

        NewDeck dmp = new NewDeck(driver);
        dmp.clickGetStartedButton();

        DeckTitle dtp = new DeckTitle(driver);
        dtp.setDeckTitle("TEST DECK");
        dtp.clickNext();

        SourceLanguage slp = new SourceLanguage(driver);
        slp.clickNext();

        DestinationLanguage dlp = new DestinationLanguage(driver);
        dlp.clickAddLanguage();
        dlp.setLanguage("Arabic");
        dlp.clickNext();

        AuthorName anp = new AuthorName(driver);
        anp.setOrganization("Thoughtworks");

        Utilities ut = new Utilities(driver);
        ut.wait(3000);

        anp.clickNext();

        ReviewSave rsp = new ReviewSave(driver);
        rsp.clickSave();

        mdp.clickDeckNameButton();

        NewCard ncp = new NewCard(driver);
        ncp.clickAddNewCardButton();

        CardMaker cmp = new CardMaker(driver);
        cmp.clickGetStartedButton();

        WriteYourPhrase wyp = new WriteYourPhrase(driver);
        wyp.setYourPhrase("Hello");
//        ut.wait(1000);
        wyp.clickNext();

        Translation tp = new Translation(driver);
        tp.setYourTranslation("Marhabaan");
        tp.clickNext();

        RecordYourPhrase ryp = new RecordYourPhrase(driver);
        ryp.clickRecord();
        ryp.clickGotIt();
        ryp.clickAllow();
        ryp.clickRecord();
        ryp.clickRecord();
        ryp.clickNext();

        ReviewSaveCard rsc = new ReviewSaveCard(driver);
        rsc.clickSave();

        ncp.clickBackButton();

        mdp.clickDeckOptions();
        mdp.clickSharePopUp();
        mdp.clickConfirmSharePopUp();
        assertEquals(str_assert,mdp.getConfirmationMessage());
    }

//    @Test
//    public void importDeckFromBrowser() {
//
////        By default_deck = By.xpath("//android.widget.FrameLayout[1]/android.widget.FrameLayout[1]/android.widget.LinearLayout[1]/android.widget.FrameLayout[2]/android.widget.LinearLayout[1]/android.widget.FrameLayout[1]/android.webkit.WebView[1]/android.webkit.WebView[1]/android.view.View[1]/android.widget.ListView[1]/android.view.View[1]/android.view.View[2]");
////        Utilities ut = new Utilities(driver);
////        ut.wait(10000);
//        driver.get("http://patrickdale.github.io/");
//////        driver.get("");
//        Utilities ut = new Utilities(driver);
//        ut.wait(5000);
////
//////        driver.findElement(default_deck).click();
//        driver.findElementByLinkText("Default Deck (json)").click();
//        ut.wait(7000);
////
//////        driver.findElementById("com.android.packageinstaller:id/permission_allow_button").click();
//////        ut.wait(5000);
////
//////        driver.findElementByName("IMPORT");
////        driver.findElementById("android:id/button1").click();
////        ut.wait(2000);
//    }
}

