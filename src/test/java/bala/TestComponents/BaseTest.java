package bala.TestComponents;

import bala.PageObjects.CartPage;
import bala.PageObjects.HomePage;
import bala.PageObjects.ProductCataloguePage;
import bala.PageObjects.ProductPage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class BaseTest {

    public WebDriver driver;
    public WebDriverWait wait;
    public Wait<WebDriver> fluentWait;
    public HomePage homePage;
    public ProductCataloguePage productCataloguePage;
    public CartPage cartPage;
    public ProductPage productPage;

    private WebDriver initializeDriver() {

        Properties properties = getProperties();

        String browserName = properties.getProperty("browser");
        int implicitWait = Integer.parseInt(properties.getProperty("implicitWait"));
        int explicitWait = Integer.parseInt(properties.getProperty("explicitWait"));
        int fluentWaitTime = Integer.parseInt(properties.getProperty("fluentWait"));
        int pageLoadTimeout = Integer.parseInt(properties.getProperty("pageLoadTimeout"));

        if (browserName.contains("chrome")) {
            ChromeOptions options = new ChromeOptions();

            if (browserName.contains("headless"))
                options.addArguments("--headless");

            driver = new ChromeDriver(options);
        }
        else if (browserName.equalsIgnoreCase("firefox")) {
            driver = new FirefoxDriver();
        }
        else if (browserName.equalsIgnoreCase("edge")) {
            driver = new EdgeDriver();
        }
        else
            System.out.println("The Browser Name in Properties File is not Valid");

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoadTimeout));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
        wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
        fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(fluentWaitTime))
                //.pollingEvery(Duration.ofSeconds(1))
                .ignoring(Exception.class);

        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();

        return driver;
    }

    private void settingUp() {

        driver = initializeDriver();
        homePage = new HomePage(driver, wait, fluentWait);
        productCataloguePage = new ProductCataloguePage(driver, wait, fluentWait);
        cartPage = new CartPage(driver, wait, fluentWait);
        productPage = new ProductPage(driver, wait, fluentWait);
    }

    public Properties getProperties() {

        Properties properties = new Properties();
        String filePath = System.getProperty("user.dir")+"/src/main/java/bala/Resources/GlobalData.properties";
        try (FileInputStream fis = new FileInputStream(filePath)) {
            properties.load(fis);
        }
        catch (FileNotFoundException e) {
            System.out.println("GlobalData.properties File Not Found!" +e.getMessage());
        }
        catch (IOException error) {
            System.out.println("IOException for GlobalData.properties File!" +error.getMessage());
        }
        return properties;
    }

    public List<Map<String, Object>> parseJSONToListOfMaps(String filePath) throws IOException {

        String jsonContent = FileUtils.readFileToString(
                new File(filePath), StandardCharsets.UTF_8);

        ObjectMapper mapper = new ObjectMapper();

        List<Map<String, Object>> data = mapper.readValue(jsonContent,
                new TypeReference<List<Map<String, Object>>>() {});
        return data;
    }

    public String getScreenshot(String testCaseName, WebDriver driver)  {

        String filePath = System.getProperty("user.dir") + "/ExtentReports/Screenshots/" + testCaseName + ".png";

        TakesScreenshot ts = (TakesScreenshot) driver;

        File source = ts.getScreenshotAs(OutputType.FILE);

        File file = new File(filePath);

        try {
            FileUtils.copyFile(source, file);
        }
        catch (IOException e) {
            throw new RuntimeException("Unable to take Screenshot! " +e.getMessage());
        }

        return filePath;
    }

    private String generateShoppingList(String filePath) throws IOException {

        List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);

        StringBuilder shoppingList = new StringBuilder();

        for (Map<String, Object> category : data) {

            shoppingList.append(category.get("name"));
            shoppingList.append(", ");
        }
        return shoppingList.substring(0, shoppingList.length()-2);
    }

    public String getShoppingList() {

        String filePath = System.getProperty("user.dir") + "/src/test/java/bala/data/Products.json";
        String shoppingList;

        try {
            shoppingList = generateShoppingList(filePath);
        }
        catch (IOException e) {
            throw new RuntimeException("Products File does not exist! " +e.getMessage());
        }
        return shoppingList;
    }

    private List<String> generateProducts(String filePath) throws IOException {

        List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);
        List<String> allProducts = new ArrayList<>();

        for (Map<String, Object> mapData : data) {
            List<String> products = (List<String>) mapData.get("products");
            allProducts.addAll(products);
        }

        return allProducts;
    }

    public List<String> getProductsAddedList() {

        String productsFilePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/OutputData/ProductsAdded.json";

        List<String> productsAdded;

        try {
            productsAdded = generateProducts(productsFilePath);
        }
        catch (IOException e) {
            throw new RuntimeException("ProductsAdded.json File not found!" +e.getMessage());
        }

        return productsAdded;
    }

    public HomePage launchApplication() {

        settingUp();
        homePage.goTo();
        return homePage;
    }

    public void closeDriver() {

        if (driver != null)
            driver.quit();
    }
}
