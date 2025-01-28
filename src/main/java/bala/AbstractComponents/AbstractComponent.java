package bala.AbstractComponents;

import bala.PageObjects.CartPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AbstractComponent {

    public WebDriver driver;
    public WebDriverWait wait;
    public JavascriptExecutor js;
    public Actions actions;
    public Properties properties;
    public Wait<WebDriver> fluentWait;

    @FindBy(xpath = "//button[@id='btn_minicart']")
    WebElement cartButton;

    @FindBy(xpath = "//h1")
    WebElement myCartText;

    @FindBy(xpath = "//input[@id='in_stock_check']")
    WebElement checkbox;

    By productsBy = By.xpath("//ol[contains(@class, 'ais-InfiniteHits-list')]/li");
    By spinnerBy = By.xpath("//p[text()='Loading...']");

    public AbstractComponent(WebDriver driver, WebDriverWait wait, Wait<WebDriver> fluentWait) {

        this.driver = driver;
        this.wait = wait;
        this.fluentWait = fluentWait;
        js = (JavascriptExecutor) driver;
        actions = new Actions(driver);
        properties = getProperties();
        PageFactory.initElements(driver, this);
    }

    public void scrollElementIntoView(WebElement element) {

        js.executeScript("arguments[0].scrollIntoView(true);", element);

        wait.until(ExpectedConditions.elementToBeClickable(element));

        //actions.moveToElement(element).perform();
    }

    public void scrollToTop() {

        js.executeScript("window.scrollTo(0,0);");
    }

    public void scrollToLoadProducts() {

        int requiredNoOfProducts = Integer.parseInt(properties.getProperty("requiredNoOfProductsToTest"));
        int retryCount = 0;
        int maxRetryCount = 1;

        List<WebElement> productCards = driver.findElements(productsBy);

        while (productCards.size() < requiredNoOfProducts) {

            try {
                fluentWait.until(driver -> {
                    js.executeScript("window.scrollTo(0, window.scrollY + window.innerHeight * 0.5);");
                    int currentProductCount = driver.findElements(productsBy).size();
                    return currentProductCount >= requiredNoOfProducts;
                });
                productCards = driver.findElements(productsBy);
                if (productCards.size() >= requiredNoOfProducts)
                    break;
            }
            catch (TimeoutException e) {
                if (retryCount > maxRetryCount)
                    break;
                try {
                    int size = productCards.size();
                    scrollToTop();
                    fluentWait.until(driver -> {
                        js.executeScript("window.scrollTo(0, window.scrollY + window.innerHeight * 0.5);");
                        int currentProductCount = driver.findElements(productsBy).size();
                        return currentProductCount > requiredNoOfProducts;
                    });
                    productCards = driver.findElements(productsBy);
                    if (size == productCards.size())
                        break;
                }
                catch (TimeoutException error) {
                    System.out.println("Continue Loop" +error);
                }
                retryCount++;
            }
            productCards = driver.findElements(productsBy);
        }
        System.out.println("Scroll Retry Count: " +retryCount);

        if (productCards.size() >= requiredNoOfProducts) {
            System.out.println("Successfully loaded the required number of products: " + productCards.size());
        } else {
            System.out.println("Failed to load the required number of products. Total loaded: " + productCards.size());
        }
    }

    public boolean isOutOfStockEnabled() {

        String afterContent = (String) js.executeScript(
                "return window.getComputedStyle(arguments[0], '::after').getPropertyValue('content')", checkbox);

        if (afterContent != null && afterContent.equals("none")) {
            return false;
        }
        else
            return true;
    }

    private void hoverOverElement(WebElement element) {

        actions.moveToElement(element).perform();
    }

    public WebElement locateAndHover(List<WebElement> elements, String text) {

        WebElement selectedElement = elements.stream()
                .filter(element -> element.getText().equalsIgnoreCase(text))
                .findFirst().orElse(null);
        if (selectedElement != null) {
            hoverOverElement(selectedElement);
        }
        if (selectedElement == null) {
            throw new RuntimeException("'"+text + "' the category is not found!");
        }
        return selectedElement;
    }

    public void waitForElementToBeClickable(WebElement element) {

        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementToBeVisible(WebElement element) {

        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToDisappearLocatedBy(By by) {

        wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    public void waitForPageToLoad(){

        wait.until(ExpectedConditions.jsReturnsValue("return document.readyState == 'complete';"));
    }

    public void waitForPageWithNewURL(String currentUrl) {

        wait.until(explicitDriver ->
                !currentUrl.equals(driver.getCurrentUrl())
        );
    }

    public void waitForDynamicLoad(By by, WebElement sortOption) {

        String flag = driver.findElement(by).getText();

        sortOption.click();

        wait.until(explicitDriver -> {
            List<WebElement> elements = driver.findElements(by);
            return !flag.equals(elements.get(0).getText());
        });
    }

    public CartPage gotoCartPage() {

        try {
            scrollToTop();
            cartButton.click();

            myCartText.click();
        }
        catch (ElementClickInterceptedException | NoSuchElementException e) {
            driver.navigate().to(properties.getProperty("CartURL"));
            System.out.println("Unable to Click on Cart Button in Headless Mode!, navigating using URL! " + e.getMessage());
        }
        waitForPageToLoad();
        waitForElementToDisappearLocatedBy(spinnerBy);

        return new CartPage(driver, wait, fluentWait);
    }

    public float convertPriceToFloat(String productPrice) {

        return Float.parseFloat(
                productPrice.substring(1, productPrice.indexOf(".")).replaceAll(",", ""));
    }

    private Properties getProperties() {

        properties = new Properties();
        String filePath = System.getProperty("user.dir")+"/src/main/java/bala/Resources/GlobalData.properties";

        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException("GlobalData.properties File Not Found!" +e.getMessage());
        }
        catch (IOException error) {
            throw new RuntimeException("IOException for GlobalData.properties File!" +error.getMessage());
        }
        return properties;
    }

    public void prepareToWriteData(String categoryName, List<String> productsAddedList, List<String> outOfStockList) {

        Map<String, Object> productsAdded = new HashMap<>();
        Map<String, Object> outOfStockProducts = new HashMap<>();

        outOfStockProducts.put("name", categoryName);
        outOfStockProducts.put("products", outOfStockList);

        if (!outOfStockList.isEmpty())
            writeJSONToListOfMaps(outOfStockProducts, true);

        productsAdded.put("name", categoryName);
        productsAdded.put("products", productsAddedList);

        if (!productsAddedList.isEmpty())
            writeJSONToListOfMaps(productsAdded, false);
    }

    public void writeJSONToListOfMaps(Map<String, Object> input, boolean isOutOfStock) {

        String filePath;

        if (isOutOfStock)
            filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/OutputData/OutOfStockProducts.json";
        else
            filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/OutputData/ProductsAdded.json";

        try {
            ObjectMapper mapper = new ObjectMapper();

            List<Map<String, Object>> data;
            File file = new File(filePath);
            if (file.exists()) {
                String jsonContent = FileUtils.readFileToString(
                        new File(filePath), StandardCharsets.UTF_8);
                data = mapper.readValue(jsonContent,
                        new TypeReference<List<Map<String, Object>>>() {});
            }
            else {
                data = new ArrayList<>();
            }

            data.add(input);
            // Pretty Format
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            mapper.writeValue(file, data);
        }
        catch (IOException e) {
            System.out.println("Error Writing Data to File!" +e.getMessage());
        }
    }

    public List<String> getBrokenLinks(By linksBy, String attribute, boolean toScroll) {

        if (toScroll)
            scrollToLoadProducts();

        List<WebElement> links = driver.findElements(linksBy);

        List<String> brokenLinks = new ArrayList<>();

        for (WebElement linkElement : links) {

            try {
                String link = linkElement.getAttribute(attribute);

                if (link != null) {
                    URL url = new URL(link);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");

                    int statusCode = connection.getResponseCode();

                    if (statusCode >= 400) {
                        brokenLinks.add(linkElement.getAttribute(attribute));
                    }
                }
            }
            catch (IOException e) {
                throw new RuntimeException("Unable to validate broken links! " +e.getMessage());
            }
        }
        return brokenLinks;
    }

}
