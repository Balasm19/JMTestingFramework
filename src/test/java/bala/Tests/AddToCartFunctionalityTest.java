package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.List;

public class AddToCartFunctionalityTest extends BaseTest {

    @Test
    public void searchShoppingList()  {

        String shoppingList = getShoppingList();
        Assert.assertNotNull(shoppingList, "The Shopping List is Empty!");

        homePage.searchShoppingList(shoppingList);
    }

    @Test(dependsOnMethods = {"searchShoppingList"}, dataProvider = "categoryAndProducts", dataProviderClass = DataProviders.class)
    public void purchaseShoppingList(String categoryName, List<String> products) {

        productCataloguePage.goToEachShoppingListItem(categoryName);
        productCataloguePage.addProductsToCart(categoryName, products, true);
    }

    @Test(dependsOnMethods = {"purchaseShoppingList"})
    public void onlySelectedProductsAddedTest() {

        cartPage = homePage.gotoCartPage();

        List<String> productsAdded = getProductsAddedList();
        boolean match = cartPage.validateProductsAddedWithCartProducts(productsAdded);
        Assert.assertTrue(match, "Products in cart does not match with the products added!");
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
