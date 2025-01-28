package bala.PageObjects;

import bala.AbstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class ProductPage extends AbstractComponent {

    WebDriver driver;

    public ProductPage(WebDriver driver, WebDriverWait wait, Wait<WebDriver> fluentWait) {
        super(driver, wait, fluentWait);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    By imageLinksBy = By.xpath("//img[contains(@class, 'swiper-thumb-slides-img')]");

    public boolean validateProductImageLinks() {

        String attribute = "src";
        List<String> brokenLinks = getBrokenLinks(imageLinksBy, attribute, false);

        return brokenLinks.isEmpty();
    }
}
