package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ErrorValidations extends BaseTest {

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void loginToPlaceOrderTest(Map<String, String> input) {

        List<String> placeOrderData = new ArrayList<>(Arrays.asList(input.get("placeOrderTest").split(",")));

        String categoryName = placeOrderData.get(0);
        String product = placeOrderData.get(1);

        List<String> productsList = new ArrayList<>();
        productsList.add(product);

        productCataloguePage = homePage.searchForAProduct(categoryName);

        productCataloguePage.addProductsToCart(categoryName, productsList, false);

        cartPage = productCataloguePage.gotoCartPage();

        boolean match = cartPage.isLoginPromptedToPlaceOrder();
        Assert.assertTrue(match, "Not Prompted to Login before Placing an Order!");
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void outOfStockProductTest(Map<String, String> input) {

        List<String> outOfStockData = new ArrayList<>(Arrays.asList(input.get("outOfStockTest").split(",")));

        String categoryName = outOfStockData.get(0);
        String product = outOfStockData.get(1);

        productCataloguePage = homePage.searchForAProduct(categoryName);

        boolean match = productCataloguePage.validateOutOfStock(product);
        Assert.assertTrue(match, "The product is not Out of Stock! It is In-Stock! ");
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void productBrokenLinkTest(Map<String, String> input) {

        String categoryName = input.get("productBrokenLinkTest");
        productCataloguePage = homePage.searchForAProduct(categoryName);

        boolean match = productCataloguePage.validateProductLinks();
        Assert.assertTrue(match, "Some Product links are Broken with Status Code > 400 !");
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void productImagesBrokenLinkTest(Map<String, String> input) {

        List<String> productImageBrokenLinkData = new ArrayList<>(
                Arrays.asList(input.get("productImageBrokenLinkTest").split(",")));

        String categoryName = productImageBrokenLinkData.get(0);
        String product = productImageBrokenLinkData.get(1);

        productCataloguePage = homePage.searchForAProduct(categoryName);

        productPage = productCataloguePage.getToProductPage(product);

        boolean match = productPage.validateProductImageLinks();
        Assert.assertTrue(match, "Some Image Links of the Product are Broken!");
    }

    @BeforeMethod
    public void beforeMethod() {

        launchApplication();
    }

    @AfterMethod
    public void afterMethod() {

        closeDriver();
    }

}
