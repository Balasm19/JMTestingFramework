@AddToCartFunctionality
Feature: Add To Cart Functionality Across Categories

  Background:
    Given I land on the Homepage
    And I search for shopping list of categories :
      | Milk, Bread, Fruit, Bella Vita Perfume, Mobiles |

  Scenario: Add products to the cart and validate correct products are added
    When I Add Products to the Cart from following categories :

      |     Milk             |   Sunfeast Dark Fantasy Chocolate Shake,Amul Gold,Cavin's Vanilla                  |
      |     Bread            |  Muffets & Tuffets Brown Bread,Modern 100% Whole Wheat                             |
      |     Fruit            |          Dragonfruit,Pineapple,Apple Indian 6 pcs                                  |
      | Bella Vita Perfume   |  Bella Vita Luxury Senorita,Bellavita Organic THUNDER                              |
      |     Mobiles          |  Apple iPhone 16 Pro Max 256 GB Black Titanium,Vivo T3 5G 128 GB,Vivo V40 Pro 5G   |

    And I navigate to Cart Page
    Then I see only the selected products in the cart