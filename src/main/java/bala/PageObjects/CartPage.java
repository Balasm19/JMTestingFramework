package bala.PageObjects;

import bala.AbstractComponents.AbstractComponent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class CartPage extends AbstractComponent {

    WebDriver driver;

    public CartPage(WebDriver driver, WebDriverWait wait, Wait<WebDriver> fluentWait) {
        super(driver, wait, fluentWait);
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//span[contains(text(), 'Basket')]")
    List<WebElement> baskets;

    @FindBy(xpath = "//div[contains(text(), 'Place Order')]")
    WebElement placeOrderButton;

    By basket = By.xpath("//span[contains(text(), 'Basket')]");
    By cartSectionBy = By.xpath("./ancestor::div[contains(@class,'product-section')]");
    By productNameBy = By.xpath("//div[contains(@class, 'product-name cursor-pointer')]");
    By productPriceBy = By.xpath(".//span[contains(@id, 'itmdiscprice')]");
    By quantityTextBy = By.xpath(".//div[contains(@class, 'qty-action')]//span[contains(@class, 'j-text')]");

    By cartValueBy = By.xpath("//span[contains(@class, 'j-text-heading-xxs')]");
    By totalTagBy = By.xpath("//span[text()='Total']");
    By buttonDisappearAttributeBy = By.xpath("//div[@class='qty-action ng-star-inserted']//button[contains(@class, 'disabled')]");
    By productCardBy = By.xpath("./ancestor::div[@valign='middle']");
    By plusButtonBy = By.xpath(".//span[@aria-label='icon IcAdd']");
    By minusButtonBy = By.xpath(".//span[@aria-label='icon IcMinus']");


    private float computeBasketPrice(WebElement basket) {

        if (basket != null) {
            WebElement cartSection = basket.findElement(cartSectionBy);

            List<WebElement> productPriceElements = cartSection.findElements(productPriceBy);

            List<Float> productPrices = productPriceElements.stream()
                    .map(productPrice -> convertPriceToFloat(productPrice.getText()))
                    .collect(Collectors.toList());

            float basketPrice = 0;

            for (float price : productPrices) {
                basketPrice = basketPrice + price;
            }
            return basketPrice;
        }
        return 0.0f;
    }

    private float computeHyperLocalBasketPrice() {

        WebElement hyperLocalBasket = baskets.stream()
                .filter(basket -> basket.getText().contains("Hyper"))
                .findFirst().orElse(null);

        return computeBasketPrice(hyperLocalBasket);
    }

    private float computeScheduledDeliveryBasketPrice() {

        WebElement scheduleDeliveryBasket = baskets.stream()
                .filter(basket -> basket.getText().contains("Schedule"))
                .findFirst().orElse(null);

        return computeBasketPrice(scheduleDeliveryBasket);
    }

    public float computeCartValue() {

        return computeHyperLocalBasketPrice() + computeScheduledDeliveryBasketPrice();
    }

    public boolean validateCartValue() {

        WebElement cartValueElement = driver.findElement(with(cartValueBy).toRightOf(totalTagBy));

        float actualCartValue = convertPriceToFloat(cartValueElement.getText());

        float expectedCartValue = computeCartValue();

        return actualCartValue == expectedCartValue;
    }

    public boolean validateEmptyCart() {

        try {
            driver.findElement(basket);
            return false;
        }
        catch (NoSuchElementException e) {
            System.out.println("Cart is Empty!");
            return true;
        }
    }

    public int getProductsCountInCart() {

        int noOfProductsInCart = 0;

        List<WebElement> products = driver.findElements(productNameBy);

        if (!products.isEmpty())
            noOfProductsInCart = products.size();

        return noOfProductsInCart;
    }

    private WebElement reInitializeCartElements(String productName) {

        List<WebElement> productNames = driver.findElements(productNameBy);

        WebElement product = productNames.stream()
                .filter(name -> name.getText().contains(productName))
                .findFirst().orElse(null);
        
        WebElement productCard = null;

        if (product != null) {
            scrollElementIntoView(product);
            productCard = product.findElement(productCardBy);

            waitForElementToBeVisible(productCard);
            waitForElementToBeClickable(productCard);
            scrollElementIntoView(productCard);
        }

        return product != null ? productCard : null;
    }

    public boolean increaseQuantity(String productName) {

        int initialCount = getQuantity(productName);

        try {
            WebElement productCard = reInitializeCartElements(productName);

            if (productCard != null) {
                WebElement plusButton = productCard.findElement(plusButtonBy);
                waitForElementToBeClickable(plusButton);

                plusButton.click();
                waitForElementToDisappearLocatedBy(buttonDisappearAttributeBy);
            }

            return getQuantity(productName) > initialCount;
        }
        catch (StaleElementReferenceException | NoSuchElementException e) {
            System.out.println("Unable to increase quantity of product!" +e.getMessage());
            return false;
        }
    }

    public boolean decreaseQuantity(String productName) {

        int initialCount = getQuantity(productName);

        try {
            WebElement productCard = reInitializeCartElements(productName);

            if (productCard != null) {
                WebElement minusButton = productCard.findElement(minusButtonBy);
                waitForElementToBeClickable(minusButton);
                minusButton.click();

                waitForElementToDisappearLocatedBy(buttonDisappearAttributeBy);
            }

            return getQuantity(productName) < initialCount;
        }
        catch (StaleElementReferenceException | NoSuchElementException e) {
            System.out.println("Unable to decrease quantity of product!" + e.getMessage());
            return false;
        }
    }

    public int getQuantity(String productName) {

        WebElement productCard = reInitializeCartElements(productName);

        if (productCard != null) {

            WebElement quantityText = productCard.findElement(quantityTextBy);

            return Integer.parseInt(quantityText.getText());
        }
        else
            return 0;
    }

    public boolean removeProductFromCart(String productName) {

        int initialCount = getQuantity(productName);

        for (int i = 0; i < initialCount; i++) {
            decreaseQuantity(productName);
        }

        return getQuantity(productName) == 0;
    }

    public boolean validateProductsAddedWithCartProducts(List<String> productsAdded) {

        List<WebElement> productNameElements = driver.findElements(productNameBy);
        List<String> productNames = productNameElements.stream()
                .map(name -> name.getText().replaceAll(",", ""))
                .collect(Collectors.toList());

        List<String> matchingProducts = new ArrayList<>();

        for (String productName : productNames) {
            for (String productAdded : productsAdded) {
                if (productName.contains(productAdded)) {
                    matchingProducts.add(productName);
                    break;
                }
            }
        }

        return productNames.equals(matchingProducts);
    }

    public boolean isLoginPromptedToPlaceOrder() {

        String currentUrl = driver.getCurrentUrl();

        waitForElementToBeClickable(placeOrderButton);
        placeOrderButton.click();

        waitForPageWithNewURL(currentUrl);
        waitForPageToLoad();

        String url = driver.getCurrentUrl();
        String loginUrlMatch = properties.getProperty("loginPageMatch");

        if (url != null)
            return url.contains(loginUrlMatch);
        return false;
    }

}
