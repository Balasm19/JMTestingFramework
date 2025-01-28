@UIBehaviorValidations
  Feature: Focuses on various UI and Behavioral functionalities of AUT

    Background:
      Given I land on the Homepage


    Scenario: Validate Cart Logo with Zero Products in Cart
      Then I see Cart Logo displays No Number of products in Empty Cart


    Scenario: Validate Cart is Empty on New Session
      When I navigate to Cart Page
      Then I see No products are in Cart


    Scenario Outline: Validate if Out Of Stock Products have Add To Cart Button
      When I search for the Category <categoryName>
      Then I enable 'Include Out Of Stock' Option and Validate if Out of Stock Products have Add to Cart Button
      Examples:
        | categoryName |
        | Fruits       |
        | Bread        |

    Scenario Outline: Validate if All Products have Price Tag
      When I search for the Category <categoryName>
      Then I see All Products have Price Tags
      Examples:
        | categoryName |
        | Mobiles      |
        | SmartTV      |
        | Tablets      |