@CartFunctionality
Feature: Different Cart Functionalities

  Background:
    Given I land on the Homepage

  @IncreaseQuantity
  Scenario Outline: Validate Increase Product Quantity Functionality on a Product
    Given I search for the Category <categoryName>
    When I click on Add To Cart button on Product CategoryName <categoryName>, Products <products>
    Then I navigate to Cart Page and increase quantity of a Product <product>
    Examples:
      |   categoryName          |          products              | product                   |
      |     Bella Vita Perfume  | Bellavita Organic THUNDER      | Bellavita Organic THUNDER |


  @DecreaseQuantity
  Scenario Outline: Validate Decrease Product Quantity Functionality on a Product
    Given I search for the Category <categoryName>
    When I click on Add To Cart button on Product CategoryName <categoryName>, Products <products>
    And I navigate to Cart Page and increase quantity of a Product <product>
    Then I am able to decrease quantity of a Product <product>
    Examples:
      |   categoryName          |          products              | product                   |
      |     Bella Vita Perfume  | Bellavita Organic THUNDER      | Bellavita Organic THUNDER |

  @Remove
  Scenario Outline: Validate Remove Product from Cart Functionality
    Given I search for the Category <categoryName>
    When I click on Add To Cart button on Product CategoryName <categoryName>, Products <products>
    And I navigate to Cart Page and increase quantity of a Product <product>
    Then I am able to remove Product <product> from Cart
    Examples:
      |   categoryName          |          products              | product                   |
      |     Bella Vita Perfume  | Bellavita Organic THUNDER      | Bellavita Organic THUNDER |

  @Quantity
  Scenario Outline: Validate Quantity of Product Functionality
    Given I search for the Category <categoryName>
    When I click on Add To Cart button on Product CategoryName <categoryName>, Products <products>
    Then I am able to see correct Product <product> Quantity in Cart page
    Examples:
      |   categoryName          |          products              | product                   |
      |     Bella Vita Perfume  | Bellavita Organic THUNDER      | Bellavita Organic THUNDER |