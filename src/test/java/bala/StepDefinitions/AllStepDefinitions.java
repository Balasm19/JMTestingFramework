package bala.StepDefinitions;

import bala.TestComponents.BaseTest;
import bala.TestComponents.Listeners;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.util.*;

public class AllStepDefinitions extends BaseTest {

    private Scenario scenario;

    @Given("I land on the Homepage")
    public void I_landed_on_the_Homepage() {

        homePage = launchApplication();
    }

    @And("I search for shopping list of categories :")
    public void I_search_shopping_list_of_products(DataTable table) {

        String shoppingList = table.asList().get(0);
        Assert.assertNotNull(shoppingList, "The Shopping List is Empty!");

        productCataloguePage = homePage.searchShoppingList(shoppingList);
    }

    @When("I Add Products to the Cart from following categories :")
    public void I_click_on_add_to_cart_button_on_product(DataTable table) {

        boolean isItCorrectProductTest = scenario.getName().contains("validate correct products");

        Map<String, String> data = new HashMap<>(table.asMap(String.class, String.class));

        for (Map.Entry<String, String> entry : data.entrySet()) {

            String categoryName = entry.getKey();
            String prods = entry.getValue();

            List<String> products = new ArrayList<>(Arrays.asList(prods.split(",")));

            productCataloguePage.goToEachShoppingListItem(categoryName);

            productCataloguePage.addProductsToCart(categoryName, products, isItCorrectProductTest);
        }
    }

    @And("I navigate to Cart Page")
    public void I_navigate_to_the_cart_page() {

        cartPage = homePage.gotoCartPage();
    }

    @Then("I see only the selected products in the cart")
    public void validate_if_only_selected_products_are_added_to_cart() {

        List<String> productsAdded = getProductsAddedList();
        boolean match = cartPage.validateProductsAddedWithCartProducts(productsAdded);
        Assert.assertTrue(match, "Products in cart does not match with the products added!");
    }

    @Then("I see total computed cart value is correct")
    public void validate_if_cart_value_is_computed_correctly() {

        System.out.println("Expected Cart Value: " + cartPage.computeCartValue());
        boolean match = cartPage.validateCartValue();
        Assert.assertTrue(match, "Actual Cart Value and Expected Cart Value does not match!");
    }

    @And("I see the Cart Logo displays correct number of products in cart")
    public void validate_if_cart_logo_displays_correct_number_of_products() {

        int cartLogoQuantity = productCataloguePage.validateCartLogo();
        int cartProductsCount = cartPage.getProductsCountInCart();
        System.out.println("cartLogoQuantity: " + cartLogoQuantity + " cartProductsCount: " + cartProductsCount);
        Assert.assertEquals(cartLogoQuantity, cartProductsCount,
                "Cart Logo Quantity and Actual Number of Products in Cart Does Not Match!");
    }

    @Given("^I search for the Category (.+)$")
    public void I_search_for_the_category(String category) {

        productCataloguePage = homePage.searchForAProduct(category);
    }

    @When("^I click on Add To Cart button on Product CategoryName (.+), Products (.+)$")
    public void I_click_on_add_to_cart_button_on_product(String categoryName, String products) {

        List<String> productsList = new ArrayList<>(Arrays.asList(products.split(",")));

        productCataloguePage.goToEachShoppingListItem(categoryName);
        productCataloguePage.addProductsToCart(categoryName, productsList, false);
    }

    @When("^I Hover over Category (.+), SubCategory (.+), and Click on SubList (.+)$")
    public void Hover_over_category_subcategory_and_click_on_sublist(String Category, String SubCategory, String SubList) {

        List<String> dropdown = new ArrayList<>(Arrays.asList(Category, SubCategory, SubList));
        productCataloguePage = homePage.performDropDownSelection(dropdown);
    }

    @Then("^I am redirected to that section Category (.+), SubCategory (.+), SubList (.+)$")
    public void I_am_redirected_to_that_section_category_subcategory_sublist(String Category, String SubCategory, String SubList) {

        List<String> dropdown = new ArrayList<>(Arrays.asList(Category, SubCategory, SubList));
        boolean match = homePage.validateDropDownSelection(dropdown);
        Assert.assertTrue(match, "Current Section/Page Does Not Match With The DropDown Sequence Given!");
    }

    @When("^I click on Sort Options and Choose SortOption (.+)$")
    public void I_click_Sort_options_and_choose_SortOption(String sortByText) {

        productCataloguePage.sortProductsBy(sortByText);
    }

    @Then("^I am able to see products sorted according to their price SortOption (.+)$")
    public void I_am_to_see_products_sorted_according_to_their_price(String sortByText) {

        boolean match = productCataloguePage.validateSortByPrice(sortByText);
        System.out.println("Match for Sort Test: " +match);
        Assert.assertTrue(match, "Sort By Price is Faulty! " + sortByText);
    }

    @Then("^I enable 'Include Out Of Stock' Option and Validate if Out of Stock Product (.+) is present$")
    public void enable_out_stock_option_and_validate_if_the_out_of_stock_is_present(String product) {

        boolean match = productCataloguePage.validateOutOfStock(product);
        Assert.assertTrue(match, "The product is not Out of Stock! It is In-Stock! ");
    }

    @Then("I get all Product Links and validate if they are Broken")
    public void I_get_all_product_links_and_validate_if_they_are_broken() {

        boolean match = productCataloguePage.validateProductLinks();
        Assert.assertTrue(match, "Some Product links are Broken with Status Code > 400 !");
    }

    @And("^I get to the product page of the Product (.+)$")
    public void I_get_to_the_product_page_of_the_product(String product) {

        productPage = productCataloguePage.getToProductPage(product);
    }

    @Then("I get all Product's Image Links and validate if they are Broken")
    public void I_get_all_products_image_links_and_validate_if_they_are_broken() {

        boolean match = productPage.validateProductImageLinks();
        Assert.assertTrue(match, "Some Image Links of the Product are Broken!");
    }

    @Then("I click on Place Order Button and I am prompted to login")
    public void I_click_on_place_order_button_and_validate_if_i_am_prompted_to_login() {

        boolean match = cartPage.isLoginPromptedToPlaceOrder();
        Assert.assertTrue(match, "Not Prompted to Login before Placing an Order!");
    }

    @Then("^I navigate to Cart Page and increase quantity of a Product (.+)$")
    public void validate_if_i_am_able_to_increase_quantity_of_a_product(String product) {

        cartPage = productCataloguePage.gotoCartPage();
        boolean match = cartPage.increaseQuantity(product);
        Assert.assertTrue(match, "Unable to increase quantity of the product! -> " +product);
    }

    @Then("^I am able to decrease quantity of a Product (.+)$")
    public void validate_if_i_am_able_to_decrease_quantity_of_a_product(String product) {

        boolean match = cartPage.decreaseQuantity(product);
        Assert.assertTrue(match, "Unable to decrease quantity of the product! -> " +product);
    }

    @Then("^I am able to remove Product (.+) from Cart$")
    public void validate_if_i_am_able_to_remove_product_from_cart(String product) {

        boolean match = cartPage.removeProductFromCart(product);
        Assert.assertTrue(match, "Unable to remove product from the Cart! -> " +product);
    }

    @Then("^I am able to see correct Product (.+) Quantity in Cart page$")
    public void I_am_able_to_see_correct_product_quantity_in_cart_page(String product) {

        cartPage = productCataloguePage.gotoCartPage();
        int actualQuantity = cartPage.getQuantity(product);
        Assert.assertEquals(actualQuantity, 1, "Quantity displayed in cart page is faulty!");
    }

    @Then("I see Cart Logo displays No Number of products in Empty Cart")
    public void validate_if_cart_logo_displays_no_number_of_products_on_Empty_cart() {

        int count = productCataloguePage.validateCartLogo();
        Assert.assertEquals(count, 0, "Cart Logo is Faulty! ");
    }

    @Then("I see No products are in Cart")
    public void validate_if_No_products_are_in_cart() {

        boolean match = cartPage.validateEmptyCart();
        Assert.assertTrue(match, "Products are present in cart when none added!");
    }

    @Then("I enable 'Include Out Of Stock' Option and Validate if Out of Stock Products have Add to Cart Button")
    public void enable_out_of_stock_option_and_validate_if_out_of_stock_products_have_add_to_cart_button() {

        boolean match = productCataloguePage.validateAddToCartButtonOnOutOfStockProducts();
        Assert.assertTrue(match, "Add To Cart Button Present on Out Of Stock Product!");
    }

    @Then("I see All Products have Price Tags")
    public void validate_if_all_products_have_price_tag() {

        boolean match = productCataloguePage.validatePresenceOfPriceTags();
        Assert.assertTrue(match, "Some Products does not have Price Tags!");
    }

    @Before
    public void beforeScenario(Scenario scenario) {

        this.scenario = scenario;
        System.out.println("Setting Up.....\nExecuting Scenario...");
        scenario.getSourceTagNames().forEach(System.out::println);

        Listeners.setScenarioName(scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {

        String status = scenario.getStatus().toString();
        if (!status.equals("PASSED")) {
            String filePath = getScreenshot(scenario.getName(), driver);
            Listeners.setScreenshotFilePath(filePath);
        }
        closeDriver();
        System.out.println("Finishing the Scenario....!!");
    }
}
