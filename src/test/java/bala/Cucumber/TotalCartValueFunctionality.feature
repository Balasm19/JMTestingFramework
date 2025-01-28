@TotalCartValueFunctionality
Feature: Cart Value Computation Functionality

  Background:
    Given I land on the Homepage
    And I search for shopping list of categories :
      | Milk, Bread, Fruit, Bella Vita Perfume, Mobiles |

  Scenario: Validate Total Cart Value with Different Category Products
    When I Add Products to the Cart from following categories :

      |     Milk             |   Sunfeast Dark Fantasy Chocolate Shake,Amul Gold,Cavin's Vanilla                  |
      |     Bread            |  Muffets & Tuffets Brown Bread,Modern 100% Whole Wheat                             |
      |     Fruit            |          Dragonfruit,Pineapple,Apple Indian 6 pcs                                  |
      | Bella Vita Perfume   |  Bella Vita Luxury Senorita,Bellavita Organic THUNDER                              |
      |     Mobiles          |  Apple iPhone 16 Pro Max 256 GB Black Titanium,Vivo T3 5G 128 GB,Vivo V40 Pro 5G   |

    And I navigate to Cart Page
    Then I see total computed cart value is correct
    And I see the Cart Logo displays correct number of products in cart