package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CartFunctionality extends BaseTest {

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void increaseProductQuantityTest(Map<String, String> input) {

        List<String> cartFunctionalityData = new ArrayList<>(Arrays.asList(input.get("cartFunctionalityTest").split(",")));

        String categoryName = cartFunctionalityData.get(0);
        String product = cartFunctionalityData.get(1);

        List<String> productsList = new ArrayList<>();
        productsList.add(product);

        productCataloguePage = homePage.searchForAProduct(categoryName);
        productCataloguePage.addProductsToCart(categoryName, productsList, false);

        cartPage = productCataloguePage.gotoCartPage();
        boolean match = cartPage.increaseQuantity(product);
        Assert.assertTrue(match, "Unable to increase quantity of the product! -> " +product);
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void decreaseProductQuantityTest(Map<String, String> input) {

        List<String> cartFunctionalityData = new ArrayList<>(Arrays.asList(input.get("cartFunctionalityTest").split(",")));

        String categoryName = cartFunctionalityData.get(0);
        String product = cartFunctionalityData.get(1);

        List<String> productsList = new ArrayList<>();
        productsList.add(product);

        productCataloguePage = homePage.searchForAProduct(categoryName);
        productCataloguePage.addProductsToCart(categoryName, productsList, false);

        cartPage = productCataloguePage.gotoCartPage();
        cartPage.increaseQuantity(product);

        boolean match = cartPage.decreaseQuantity(product);
        Assert.assertTrue(match, "Unable to decrease quantity of the product! -> " +product);
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void removeProductFromCartTest(Map<String, String> input) {

        List<String> cartFunctionalityData = new ArrayList<>(Arrays.asList(input.get("cartFunctionalityTest").split(",")));

        String categoryName = cartFunctionalityData.get(0);
        String product = cartFunctionalityData.get(1);

        List<String> productsList = new ArrayList<>();
        productsList.add(product);

        productCataloguePage = homePage.searchForAProduct(categoryName);
        productCataloguePage.addProductsToCart(categoryName, productsList,false);

        cartPage = productCataloguePage.gotoCartPage();
        cartPage.increaseQuantity(product);

        boolean match = cartPage.removeProductFromCart(product);
        Assert.assertTrue(match, "Unable to remove product from the Cart! -> " +product);
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void productQuantityTest(Map<String, String> input) {

        List<String> cartFunctionalityData = new ArrayList<>(Arrays.asList(input.get("cartFunctionalityTest").split(",")));

        String categoryName = cartFunctionalityData.get(0);
        String product = cartFunctionalityData.get(1);

        List<String> productsList = new ArrayList<>();
        productsList.add(product);

        productCataloguePage = homePage.searchForAProduct(categoryName);
        productCataloguePage.addProductsToCart(categoryName, productsList, false);

        cartPage = productCataloguePage.gotoCartPage();
        int actualQuantity = cartPage.getQuantity(product);
        Assert.assertEquals(actualQuantity, 1, "Quantity displayed in cart page is faulty!");
    }

    @BeforeMethod
    public void prepareTest() {

        launchApplication();
    }

    @AfterMethod
    public void tearDown() {

        closeDriver();
    }
}
