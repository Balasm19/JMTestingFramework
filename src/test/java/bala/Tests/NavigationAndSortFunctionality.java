package bala.Tests;

import bala.TestComponents.BaseTest;
import bala.TestComponents.DataProviders;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

public class NavigationAndSortFunctionality extends BaseTest {

    @Test(dataProvider = "dropDownData", dataProviderClass = DataProviders.class)
    public void dropDownNavigationTest(List<String> dropdown, String sortOption) {

        productCataloguePage = homePage.performDropDownSelection(dropdown);
        boolean dropDownMatch = homePage.validateDropDownSelection(dropdown);
        Assert.assertTrue(dropDownMatch, "Current DropDown and Expected DropDown Options does not match!");
    }

    @Test(dataProvider = "dropDownData", dataProviderClass = DataProviders.class)
    public void sortByPriceTest(List<String> dropdown, String sortOption) {

        productCataloguePage = homePage.performDropDownSelection(dropdown);
        boolean dropDownMatch = homePage.validateDropDownSelection(dropdown);
        Assert.assertTrue(dropDownMatch, "Current DropDown and Expected DropDown Options does not match!");

        productCataloguePage.sortProductsBy(sortOption);
        boolean sortPriceMatch = productCataloguePage.validateSortByPrice(sortOption);
        Assert.assertTrue(sortPriceMatch, "Sort By Price is Faulty! " + sortOption);
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
