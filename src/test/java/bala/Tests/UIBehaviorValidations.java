package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class UIBehaviorValidations extends BaseTest {

    @Test
    public void cartLogoOnEmptyCartTest() {

        int count = productCataloguePage.validateCartLogo();
        Assert.assertEquals(count, 0, "Cart Logo is Faulty! ");
    }

    @Test
    public void cartIsEmptyOnNewSession() {

        cartPage = homePage.gotoCartPage();
        boolean match = cartPage.validateEmptyCart();
        Assert.assertTrue(match, "Products are present in cart when none added!");
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void addToCartButtonOnOutOfStockProductsTest(Map<String, String> input) {

        String categoryName = input.get("addToCartButtonTest");
        productCataloguePage = homePage.searchForAProduct(categoryName);

        boolean match = productCataloguePage.validateAddToCartButtonOnOutOfStockProducts();
        Assert.assertTrue(match, "Add To Cart Button Present on Out Of Stock Product!");
    }

    @Test (dataProvider = "otherData", dataProviderClass = DataProviders.class)
    public void priceTagOnProductsTest(Map<String, String> input) {

        String categoryName = input.get("priceTagTest");

        productCataloguePage = homePage.searchForAProduct(categoryName);

        boolean match = productCataloguePage.validatePresenceOfPriceTags();
        Assert.assertTrue(match, "Some Products does not have Price Tags!");
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
