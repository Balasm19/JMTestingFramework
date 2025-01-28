package bala.PageObjects;

import bala.AbstractComponents.AbstractComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.stream.Collectors;

public class HomePage extends AbstractComponent {

    WebDriver driver;

    public HomePage(WebDriver driver, WebDriverWait wait, Wait<WebDriver> fluentWait) {
        super(driver, wait, fluentWait);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@placeholder='Search JioMart']")
    WebElement searchBox;

    @FindBy(xpath = "//span[@id='btn_search_list']")
    WebElement searchList;

    @FindBy(xpath = "//textarea[@name='shopping-list']")
    WebElement listSearchBox;

    @FindBy(xpath = "//button[contains(text(),'Search All')]")
    WebElement searchAllButton;

    @FindBy(xpath = "//li[@class='header-nav-l1-item']")
    List<WebElement> categories;

    @FindBy(xpath = "//a[@id='top_menu_cat']")
    WebElement allCategories;

    @FindBy(xpath = "//ul[@class='jm-breadcrumbs-list']/li[position() > 2]")
    List<WebElement> breadCrumbs;

    By subCategoriesBy = By.xpath(".//li[@class='header-nav-l2-item']/a");
    By subListBy = By.xpath(".//parent::li//li/a");


    public ProductCataloguePage searchForAProduct(String searchText) {

        searchBox.sendKeys(searchText);
        searchBox.sendKeys(Keys.ENTER);

        return new ProductCataloguePage(driver,wait,fluentWait);
    }

    public ProductCataloguePage searchShoppingList(String shoppingList) {

        searchList.click();
        waitForElementToBeVisible(listSearchBox);

        listSearchBox.sendKeys(shoppingList);
        searchAllButton.click();
        waitForPageToLoad();

        return new ProductCataloguePage(driver, wait, fluentWait);
    }

    public ProductCataloguePage performDropDownSelection(List<String> dropDownOptions) {

        WebElement selectedCategory = locateAndHover(categories, dropDownOptions.get(0));

        List<WebElement> subCategories = selectedCategory.findElements(subCategoriesBy);
        WebElement selectedSubCategory = locateAndHover(subCategories, dropDownOptions.get(1));

        List<WebElement> subLists = selectedSubCategory.findElements(subListBy);
        WebElement selectedSubList = locateAndHover(subLists, dropDownOptions.get(2));

        selectedSubList.click();
        waitForPageToLoad();

        return new ProductCataloguePage(driver, wait, fluentWait);
    }

    public boolean validateDropDownSelection(List<String> dropDownOptions) {

        List<String> actualBreadCrumbs = breadCrumbs.stream()
                .map(breadCrumb -> breadCrumb.getText().trim())
                .collect(Collectors.toList());

        return actualBreadCrumbs.equals(dropDownOptions);
    }

    public void goTo() {

        String url = properties.getProperty("URL");
        if (url.isEmpty())
            throw new RuntimeException("URL to the Target Website not provided !");
        driver.get(url);
    }

}
