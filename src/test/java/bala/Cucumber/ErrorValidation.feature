@ErrorValidations
Feature: Error Validations on different functionalities

  Background:
    Given I land on the Homepage

    Scenario Outline: Validate If we are prompted to Login to Place Order
      Given I search for the Category <categoryName>
      When I click on Add To Cart button on Product CategoryName <categoryName>, Products <product>
      And I navigate to Cart Page
      Then I click on Place Order Button and I am prompted to login
      Examples:
        | categoryName |  product         |
        | Tablets      |  OnePlus Pad 2   |


    Scenario Outline: Validate If a Product is Out Of Stock
      When I search for the Category <categoryName>
      Then I enable 'Include Out Of Stock' Option and Validate if Out of Stock Product <product> is present
      Examples:
        | categoryName |    product                           |
        | Bread        |    Britannia 100% Whole Wheat Bread  |
        | Mobiles      |    Vivo T3 5G 128 GB                 |


    Scenario Outline: Validate If Products in Product Catalogue Page have Broken Links
      When I search for the Category <categoryName>
      Then I get all Product Links and validate if they are Broken
      Examples:
      | categoryName |
      | Mobiles      |
      | SmartTV      |


    Scenario Outline: Validate If Product Image Links in Product Page have Broken Links
      When I search for the Category <categoryName>
      And I get to the product page of the Product <product>
      Then I get all Product's Image Links and validate if they are Broken
      Examples:
      | categoryName |  product                       |
      | Tablets      |  OnePlus Pad 2                 |
      | Juices       |  Real Fruit Power Mixed Fruit  |