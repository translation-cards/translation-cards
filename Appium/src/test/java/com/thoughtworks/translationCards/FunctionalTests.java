package com.thoughtworks.translationCards;

import com.saucelabs.junit.ConcurrentParameterized;
import com.thoughtworks.translationCards.page.*;
import com.thoughtworks.translationCards.utilities.Utilities;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.util.LinkedList;

/**
 * Created by nliao on 8/23/16.
 */
@RunWith(ConcurrentParameterized.class)
public class FunctionalTests {

    private final String platformName;
    private final String deviceName;
    private final String platformVersion;
    private final String appiumVersion;
    private final String deviceOrientation;

    public WebDriver driver;

    public static final String USERNAME = System.getenv("SAUCE_USERNAME");
    public static final String ACCESS_KEY = System.getenv("SAUCE_ACCESS_KEY");
    public static final String URL = "https://" + USERNAME + ":" + ACCESS_KEY + "@ondemand.saucelabs.com:443/wd/hub";

    public FunctionalTests(String platformName, String deviceName, String platformVersion, String appiumVersion, String deviceOrientation) {
        super();
        this.platformName = platformName;
        this.deviceName = deviceName;
        this.platformVersion = platformVersion;
        this.appiumVersion = appiumVersion;
        this.deviceOrientation = deviceOrientation;
    }

    @ConcurrentParameterized.Parameters
    public static LinkedList browsersStrings() {
        LinkedList<String[]> browsers = new LinkedList<>();

        browsers.add(new String[]{"Android", "Android Emulator", "5.0", "1.5.3", "portrait"});
        browsers.add(new String[]{"Android", "Samsung Galaxy S4 Emulator", "4.4", "1.5.3", "portrait"});
        browsers.add(new String[]{"Android", "Google Nexus 7 HD Emulator", "4.3", "1.5.3", "portrait"});
//        browsers.add(new String[]{"Android", "LG Nexus 4 Emulator", "4.2", "1.5.3", "portrait"});
//        browsers.add(new String[]{"Android", "Samsung Galaxy Nexus Emulator", "4.1", "1.5.3", "portrait"});

        return browsers;
    }

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = DesiredCapabilities.android();

        capabilities.setCapability("platformName",platformName);
        capabilities.setCapability("deviceName",deviceName);
        capabilities.setCapability("platformVersion",platformVersion);
        capabilities.setCapability("appiumVersion", appiumVersion);
        capabilities.setCapability("deviceOrientation", deviceOrientation);
        capabilities.setCapability("browserName", "");
        capabilities.setCapability("app","sauce-storage:app-debug.apk");

        driver = new AndroidDriver(new URL(URL), capabilities);
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

//        mdp.clickDeckNameButton(); // CREATE A CARD
//
//        NewCard ncp = new NewCard(driver);
//        ncp.clickAddNewCardButton();
//
//        CardMaker cmp = new CardMaker(driver);
//        cmp.clickGetStartedButton();
//
//        WriteYourPhrase wyp = new WriteYourPhrase(driver);
//        wyp.setYourPhrase("Hello");
////        ut.wait(1000);
//        wyp.clickNext();
//
//        Translation tp = new Translation(driver);
//        tp.setYourTranslation("Marhabaan");
//        tp.clickNext();
//
//        RecordYourPhrase ryp = new RecordYourPhrase(driver);
//        ryp.clickRecord(); // API Level 23 +
//        ryp.clickGotIt();
//        ryp.clickAllow(); // END API 23
//        ryp.clickRecord();
//        ut.wait(5000); // Maybe?
//        ryp.clickRecord();
//        ryp.clickNext();
//
//        ReviewSaveCard rsc = new ReviewSaveCard(driver);
//        rsc.clickSave();
//
//        ncp.clickBackButton();
//
//        mdp.clickDeckOptions();
//        mdp.clickSharePopUp();
//        mdp.clickConfirmSharePopUp();
//        assertEquals(str_assert,mdp.getConfirmationMessage());
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

