@NavigationAndSortFunctionality
  Feature: Dropdown Navigation And Sort By Price Functionality

    Background:
      Given I land on the Homepage

     @DropDownValidation
     Scenario Outline: Dropdown Navigation to the expected section
       When I Hover over Category <Category>, SubCategory <SubCategory>, and Click on SubList <SubList>
       Then I am redirected to that section Category <Category>, SubCategory <SubCategory>, SubList <SubList>
       Examples:
         | Category       | SubCategory          | SubList    |
         | Electronics    | Mobiles & Tablets    | Mobiles    |
         |   Fashion      |      Men             | Watches    |


     @SortByPriceValidation
     Scenario Outline: Validating Sort By Price Functionality On Product Catalogue Page
       Given I Hover over Category <Category>, SubCategory <SubCategory>, and Click on SubList <SubList>
       When I click on Sort Options and Choose SortOption <SortOption>
       Then I am able to see products sorted according to their price SortOption <SortOption>
       Examples:
         | Category       | SubCategory          | SubList             |    SortOption       |
         | Electronics    | Mobiles & Tablets    | Mobiles             | Price: Low to High  |
         | Electronics    | Mobiles & Tablets    | Mobiles             | Price: High to Low  |