package bala.TestComponents;

import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DataProviders extends BaseTest {

    @DataProvider(name = "categoryAndProducts")
    public Object[][] productsData() throws IOException {

        String filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/Products.json";

        List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);

        Object[][] dataArray = new Object[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> category = data.get(i);
            dataArray[i][0] = category.get("name");
            dataArray[i][1] = category.get("products");
        }
        return dataArray;
    }

    @DataProvider(name = "dropDownData")
    public Object[][] dropDownData() throws IOException {

        String filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/DropDownData.json";

        List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);

        Object[][] dataArray = new Object[data.size()][2];

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> category = data.get(i);
            dataArray[i][0] = category.get("DropDownSequence");
            dataArray[i][1] = category.get("SortOption");
        }

        return dataArray;
    }

    @DataProvider (name = "outOfStockProducts")
    public Object[][] outOfStockData() throws IOException {

        String filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/OutputData/OutOfStockProducts.json";

        File file = new File(filePath);

        if (file.exists()) {
            List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);

            Object[][] dataArray = new Object[data.size()][2];

            for (int i = 0; i < data.size(); i++) {
                Map<String, Object> category = data.get(i);
                dataArray[i][0] = category.get("name");
                dataArray[i][1] = category.get("products");
            }
            return dataArray;
        }
        else
            return new Object[0][0];
    }

    @DataProvider (name = "otherData")
    public Object[][] data() throws IOException {

        String filePath = System.getProperty("user.dir") + "/src/test/java/bala/Data/OtherData.json";

        List<Map<String, Object>> data = parseJSONToListOfMaps(filePath);

        Object[][] dataArray = new Object[1][1];

        dataArray[0][0] = data.get(0).get("Products");

        return dataArray;
    }
}
