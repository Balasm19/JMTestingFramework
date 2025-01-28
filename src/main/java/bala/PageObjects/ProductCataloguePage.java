package bala.PageObjects;

import bala.AbstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.*;
import java.util.stream.Collectors;

public class ProductCataloguePage extends AbstractComponent {

    WebDriver driver;

    public ProductCataloguePage(WebDriver driver, WebDriverWait wait, Wait<WebDriver> fluentWait) {
        super(driver, wait, fluentWait);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//button[text()='Add to Cart']") // Product Page Add to Cart Button
    WebElement addToCartButton;

    @FindBy(xpath = "//button[@data-toggle='dropdown']")
    WebElement sortByButton;

    @FindBy(xpath = "//label[@class='switch']")
    List<WebElement> sortOptions;

    @FindBy(xpath = "//span[text()='Include Out of stock']")
    WebElement includeOutOfStockCheckbox;

    @FindBy(xpath = "//div[@class='cartbox']/span")
    WebElement cartLogo;

    @FindBy(xpath = "//div[@class='search-list-buttons']")
    List<WebElement> productTabs;


    By productBy = By.xpath("//ol[contains(@class, 'ais-InfiniteHits-list')]/li");
    By productPriceBy = By.xpath("//span[@class='jm-heading-xxs jm-mb-xxs']");
    By productNameBy = By.xpath(".//div[contains(@class, 'plp-card-details-name')]");
    By addToCartBy = By.xpath(".//div[@class='plp-card-cart']//button[contains(text(), 'Add')]");
    By linksBy = By.xpath("//ol[contains(@class, 'ais-InfiniteHits-list')]/li/a");
    By productCard = By.xpath("./ancestor::div[@class='plp-card-container']");
    By priceBy = By.xpath(".//span[@class='jm-heading-xxs jm-mb-xxs']");
    By prodsBy = By.xpath("//div[@class='plp-card-container']");

    private String checkPageType() {

        boolean isOutOfEnabled = isOutOfStockEnabled();

        if (!isOutOfEnabled) {
            try {
                waitForPageToLoad();

                WebElement firstProduct = driver.findElement(productBy);

                WebElement addToCartButton = firstProduct.findElement(addToCartBy);
            }
            catch (NoSuchElementException e) {
                return "Page Type2";
            }
        }
        return "Page Type 1";
    }

    private boolean addProductToCart(String productName) {

        scrollToLoadProducts();

        List<WebElement> products = driver.findElements(productBy);

        WebElement selectedProduct = products.stream()
                .filter(product -> product.getText().replaceAll(",", "").contains(productName))
                .findFirst().orElse(null);

        if (selectedProduct == null) {
            System.out.println("Selected Product not Found! : " + productName);
            return false;
        }

        WebElement addToCartButton = selectedProduct.findElement(addToCartBy);
        scrollElementIntoView(selectedProduct);
        addToCartButton.click();
        return true;
    }

    private boolean addToCartFromProductPage(String productName) {

        scrollToLoadProducts();

        List<WebElement> products = driver.findElements(productBy);

        WebElement selectedProduct = products.stream()
                .filter(product -> product.getText().replaceAll(",", "").contains(productName))
                .findFirst().orElse(null);

        if (selectedProduct == null) {
            System.out.println("Selected Product not Found! : " + productName);
            return false;
        }

        scrollElementIntoView(selectedProduct);

        selectedProduct.click();

        // Navigating to Product Page
        waitForPageToLoad();

        scrollElementIntoView(addToCartButton);
        addToCartButton.click();

        // Navigate Back
        driver.navigate().back();
        waitForPageToLoad();
        return true;
    }

    public void addProductsToCart(String categoryName, List<String> products, boolean isFileNeeded) {

        List<String> outOfStockList = new ArrayList<>();

        String pageType = checkPageType();

        if (pageType.equals("Page Type 1")) {
            for (String product : products) {
                boolean match = addProductToCart(product);
                if (!match) {
                    outOfStockList.add(product);
                }
            }
        }
        else {
            for (String product : products) {
                boolean match = addToCartFromProductPage(product);
                if (!match) {
                    outOfStockList.add(product);
                }
            }
        }
        products.removeAll(outOfStockList);
        if (isFileNeeded)
            prepareToWriteData(categoryName, products, outOfStockList);
    }

    public void goToEachShoppingListItem(String categoryName) {

        if (!productTabs.isEmpty()) {
            scrollToTop();
            waitForPageToLoad();
            By productTab = By.xpath("//div[@class='search-list-buttons']/a[contains(text(),'" + categoryName + "')]");

            WebElement productFilterTab = driver.findElement(productTab);
            productFilterTab.click();
        }
    }

    public void sortProductsBy(String sortText) {

        scrollToTop();
        waitForElementToBeVisible(sortByButton);
        sortByButton.click();

        WebElement selectedSortElement = locateAndHover(sortOptions, sortText);
        waitForDynamicLoad(productPriceBy, selectedSortElement);
    }

    public boolean validateSortByPrice(String sortText) {

        scrollToLoadProducts();

        List<WebElement> priceElements = driver.findElements(productPriceBy);
        System.out.println("Price element size: " +priceElements.size());

        List<Float> actualPrices = priceElements.stream()
                .map(price -> convertPriceToFloat(price.getText()))
                .collect(Collectors.toList());
        List<Float> expectedPrices;

        if (sortText.contains("Low to High")) {
            expectedPrices = actualPrices.stream().sorted().collect(Collectors.toList());
        }
        else if (sortText.contains("High to Low")) {
            expectedPrices = actualPrices.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        }
        else {
            throw new InputMismatchException("Price Sort Validation cannot be performed on! " +sortText);
        }
        boolean match = actualPrices.equals(expectedPrices);
        if (!match)
            locateWhereSortByPriceFailed(priceElements, expectedPrices);

        return match;
    }

    private void locateWhereSortByPriceFailed(List<WebElement> priceElements, List<Float> expectedPrices) {

        WebElement mismatchedProduct = null;

        int flag = 0;
        for (int i = 0; i < expectedPrices.size(); i++) {

            float price = convertPriceToFloat(priceElements.get(i).getText());

            if ( price != expectedPrices.get(i)) {

                for (int j = 0; j < priceElements.size(); j++) {

                    float expectedPrice = expectedPrices.get(i);
                    float actualPrice = convertPriceToFloat(priceElements.get(j).getText());

                    if (actualPrice == expectedPrice) {
                        mismatchedProduct = priceElements.get(j).findElement(productCard);
                        flag = 1;
                        break;
                    }
                }
                if (flag == 1)
                    break;
            }
        }
        if (mismatchedProduct != null) {
            scrollElementIntoView(mismatchedProduct);
            System.out.println(mismatchedProduct.findElement(productNameBy).getText());
        }
    }

    private List<WebElement> getOutOfStockProductElements() {

        scrollToTop();

        boolean isOutOfStockEnabled = isOutOfStockEnabled();
        if (!isOutOfStockEnabled)
            includeOutOfStockCheckbox.click();

        waitForPageToLoad();

        scrollToLoadProducts();

        List<WebElement> products = driver.findElements(productBy);

        List<WebElement> outOfStockProducts = products.stream()
                .filter(product -> product.getText().contains("Out of Stock"))
                .collect(Collectors.toList());

        return outOfStockProducts;
    }

    public boolean validateOutOfStock(String productName) {

        List<WebElement> outOfStockProductElements = getOutOfStockProductElements();

        List<String> outOfStockProductNames = outOfStockProductElements.stream()
                .map(product -> product.findElement(productNameBy).getText())
                .collect(Collectors.toList());

        boolean match = outOfStockProductNames.stream().anyMatch(name -> !name.contains(productName));
        System.out.println("Is the product '" +productName+ "' Out of Stock? : " +match);
        return match;
    }

    public boolean validateAddToCartButtonOnOutOfStockProducts() {

        List<WebElement> outOfStockProducts = getOutOfStockProductElements();

        boolean match = outOfStockProducts.stream()
                .filter(product -> product.getText().contains("Add"))
                .allMatch(product -> {
                    try{
                        product.findElement(addToCartBy);
                        return false;
                    }
                    catch (NoSuchElementException e) {
                        return true;
                    }});
        return match;
    }

    public int validateCartLogo() {

        int noOfProducts = 0;

        try {
            String cartLogoText = cartLogo.getText().trim();

            if (!cartLogoText.isEmpty()) {
                noOfProducts = Integer.parseInt(cartLogoText);
            }
        }
        catch (NoSuchElementException e) {
            System.out.println("Products are not added to Cart!");
        }
        catch (NumberFormatException e) {
            System.out.println("Cart Logo text is not a Valid Number: " +e.getMessage());
        }
        return noOfProducts;
    }

    public boolean validateProductLinks() {

        String attribute = "href";
        List<String> brokenLinks = getBrokenLinks(linksBy, attribute, true);

        return brokenLinks.isEmpty();
    }

    public ProductPage getToProductPage(String productName) {

        scrollToLoadProducts();

        List<WebElement> products = driver.findElements(productBy);

        WebElement selectedProduct = products.stream()
                .filter(product -> product.getText().replaceAll(",", "").contains(productName))
                .findFirst().orElse(null);

        if (selectedProduct == null) {
            throw new RuntimeException("Selected Product is not found, Unable to navigate to Product Page!");
        }

        scrollElementIntoView(selectedProduct);

        selectedProduct.click();

        waitForPageToLoad();

        return new ProductPage(driver, wait, fluentWait);
    }

    public boolean validatePresenceOfPriceTags() {

        scrollToLoadProducts();

        List<WebElement> products = driver.findElements(prodsBy);

        List<WebElement> productWithNoPrice = new ArrayList<>();

        boolean match = products.stream()
                .allMatch(product -> {
                    try {
                        product.findElement(priceBy);
                        return true;
                    }
                    catch (NoSuchElementException e) {
                        productWithNoPrice.add(product);
                        return false;
                    }
                });

        if (!match) {
            String noPriceTagProductName = productWithNoPrice.get(0).findElement(productNameBy).getText();
            System.out.println("Name of Product with Misplaced/No Price Tag: " + noPriceTagProductName);
            scrollElementIntoView(productWithNoPrice.get(0));
        }
        return match;
    }

}
