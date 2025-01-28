package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class TotalCartValueFunctionality extends BaseTest {

    @Test
    public void searchShoppingList()  {

        String shoppingList = getShoppingList();
        Assert.assertNotNull(shoppingList, "The Shopping List is Empty!");

        homePage.searchShoppingList(shoppingList);
    }

    @Test(dependsOnMethods = {"searchShoppingList"}, dataProvider = "categoryAndProducts", dataProviderClass = DataProviders.class)
    public void purchaseShoppingList(String categoryName, List<String> products) {

        productCataloguePage.goToEachShoppingListItem(categoryName);
        productCataloguePage.addProductsToCart(categoryName, products, false);
    }

    @Test(dependsOnMethods = {"purchaseShoppingList"})
    public void TotalCartValueTest() {

        cartPage = homePage.gotoCartPage();
        float price = cartPage.computeCartValue();

        System.out.println("Expected Cart Value: " +price);

        boolean match = cartPage.validateCartValue();
        Assert.assertTrue(match, "Actual Cart Value and Expected Cart Value does not match!");
    }

    @BeforeTest
    public void prepareTest() {

        launchApplication();
    }

    @AfterTest
    public void tearDown() {

        closeDriver();
    }
}
