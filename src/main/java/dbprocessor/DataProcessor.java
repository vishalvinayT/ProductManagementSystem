package dbprocessor;

import com.google.gson.*;
import dbtables.ProductData;
import dbtables.WareHouseData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Random;


public class DataProcessor {
    Gson parserJson=new Gson();
    private static final String  WAREHOUSEDATA_KEY="WareHouseData";
    private static final String PRODUCTDATA_KEY="ProductData";
    private WareHouseData wareHouseData;
    private ProductData[] productDataArr;
    private QueryManager queryManager= new QueryManager();


    //note the below code

    /*fixme: the productImage column is manually altered to longblob
             and the product description is altered to varchar(35000)
             and the companyName in companies table is altered to varachar(1000)
             and the productName in products table is altered to varchar(1000)

     */
    public DataProcessor() throws IOException {
        JsonParser parser=new JsonParser();
        String json =parser.parse(new BufferedReader(new FileReader("output_data.json"))).toString();
        JsonObject jsonObject = parserJson.fromJson(json, JsonObject.class);
        JsonObject warehouseObject=jsonObject.getAsJsonObject(WAREHOUSEDATA_KEY);
        JsonArray jsonArray = jsonObject.getAsJsonArray(PRODUCTDATA_KEY);
        productDataArr = parserJson.fromJson(jsonArray, ProductData[].class);
        wareHouseData=parserJson.fromJson(warehouseObject, WareHouseData.class);
    }

    public void executeInsertProductData() throws SQLException, FileNotFoundException {
        if(queryManager.connection!=null && wareHouseData!=null && productDataArr!=null){
            int count=0;
            for(ProductData productData:productDataArr){
                Integer high=500;
                Integer low=50;
                productData.quantity=productData.quantity!=null? productData.quantity : new Random().nextInt(high-low)+low;
                queryManager.insertProductData(wareHouseData,productData);
                count++;
            }
            System.out.printf("Inserted %d Records",count);
        }
    }

}
