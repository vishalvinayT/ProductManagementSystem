package dbprocessor;

import com.google.gson.*;
import dbtables.ProductData;
import dbtables.WareHouseData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;


public class DataProcessor {
    Gson parserJson=new Gson();
    private static final String  WAREHOUSEDATA_KEY="WareHouseData";
    private static final String PRODUCTDATA_KEY="ProductData";
    private WareHouseData wareHouseData;
    private ProductData[] productDataArr;
    private QueryManager queryManager= new QueryManager();
    public DataProcessor() throws IOException {
        JsonParser parser=new JsonParser();
        String json =parser.parse(new BufferedReader(new FileReader("output_data.json"))).toString();
        JsonObject jsonObject = parserJson.fromJson(json, JsonObject.class);
        JsonObject warehouseObject=jsonObject.getAsJsonObject(WAREHOUSEDATA_KEY);
        JsonArray jsonArray = jsonObject.getAsJsonArray(PRODUCTDATA_KEY);
        productDataArr = parserJson.fromJson(jsonArray, ProductData[].class);
        wareHouseData=parserJson.fromJson(warehouseObject, WareHouseData.class);
    }

    public void executeInsertProductData() throws SQLException {
        if(queryManager.connection!=null && wareHouseData!=null && productDataArr!=null){
            for(ProductData productData:productDataArr){
                queryManager.insertProductData(wareHouseData,productData);
            }
        }
    }

}
