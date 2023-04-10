package dbprocessor;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

import dataextractor.Utilities;
import dbtables.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class DataProcessor {
    Gson parserJson=new Gson();
    private static final String  WAREHOUSEDATA_KEY="WareHouseData";
    private static final String PRODUCTDATA_KEY="ProductData";
    private WareHouseData wareHouseData;
    private ProductData[] productDataArr;
    private QueryManager queryManager= new QueryManager();

    private boolean dataRead= false;


    //note the below code

    /*fixme: the productImage column is manually altered to longblob
             and the product description is altered to varchar(35000) // magicNumbes
             and the companyName in companies table is altered to varachar(1000)
             and the productName in products table is altered to varchar(1000)

     */


    public void read() throws IOException{
        JsonParser parser=new JsonParser();
        String json =parser.parse(new BufferedReader(new FileReader("./configs/output_data.json"))).toString();
        JsonObject jsonObject = parserJson.fromJson(json, JsonObject.class);
        JsonObject warehouseObject=jsonObject.getAsJsonObject(WAREHOUSEDATA_KEY);
        JsonArray jsonArray = jsonObject.getAsJsonArray(PRODUCTDATA_KEY);
        productDataArr = parserJson.fromJson(jsonArray, ProductData[].class);
        wareHouseData=parserJson.fromJson(warehouseObject, WareHouseData.class);
        this.dataRead=true;
    }

    public void executeInsertProductData() throws SQLException, FileNotFoundException {
        if(!dataRead){
            System.out.println("Please Read Data");
        }
        else if(queryManager.connection!=null && wareHouseData!=null && productDataArr!=null ){
            int count=0;
            for(ProductData productData:productDataArr){
                Integer high=500; // magicNumbers: these high and low values are used for deciding the sample quantity
                Integer low=50;
                productData.quantity=productData.quantity!=null? productData.quantity : new Random().nextInt(high-low)+low;
                queryManager.insertProductData(wareHouseData,productData);
                count++;
            }
            System.out.printf("Inserted %d Records",count);
        }
    }

    public User checkUser(String user) throws SQLException {
        if(user!=null && !user.isEmpty() && !user.isBlank() ){
            User extractedUser= queryManager.extractUser(user);
            return extractedUser;
        }
        throw new NullPointerException();

    }


    public boolean addUser(User user) throws  SQLException{
        if(user!=null){
            return queryManager.insertUserData(user);
        }
        return false;
    }

    public boolean addOrder(User user, Map<ProductData,Integer> cartItems){
        if(user!=null && cartItems!=null){
            try {
                Order order=generateOrder(user,cartItems);
                if(order!=null){
                    List<Shipment> shipments=fetchShipments(user, order,cartItems);
                    queryManager.insertOrder(user,order,shipments);
                    return true;
                }
            }catch (SQLException | NullPointerException e){
                return false;
            }
        }
        return false;
    }

    private List<Shipment> fetchShipments(User user, Order order, Map<ProductData, Integer> cartItems){
        if(user!=null && order!=null && cartItems!=null){
            List<Shipment> shipments=new LinkedList<>();
            for(Map.Entry<ProductData,Integer> entry: cartItems.entrySet()){
                Shipment shipment= new Shipment();
                ProductData productData=entry.getKey();
                shipment.userID=user.id;
                shipment.quantityOrdered=entry.getValue();
                shipment.pricePerUnit=productData.productPrice;
                shipment.productId=productData.productId;
                shipment.totalPrice=entry.getValue()*productData.productPrice;
                shipment.dateOfOrder=order.orderDate;
                shipments.add(shipment);
            }
            return shipments;
        }
        throw  new NullPointerException();
    }

    private Order generateOrder(User user, Map<ProductData,Integer> cartItems) throws SQLException{
        if(user!=null && cartItems!=null){
            Order order= new Order();
            User generatedUser=queryManager.extractUser(user.email);
            try{
                if(generatedUser!=null){
                    order.userId=generatedUser.id;
                    order.orderDate= Utilities.formatter.format(new Date());
                    order.totalOrderPrice=calculateOrderValue(cartItems);
                    return order;
                }

            }catch (NullPointerException e){
                return null;
            }

        }
        return null;
    }

    private Double calculateOrderValue(Map<ProductData, Integer> cartItems){
        if(cartItems!=null){
            Double totalPrice=0.0;
            for(Map.Entry<ProductData,Integer> entry: cartItems.entrySet()){
                Double price=entry.getKey().productPrice * entry.getValue();
                totalPrice+=price;
            }
            return totalPrice;
        }
        throw new NullPointerException();
    }

    public List<ProductData> fetchProductData(){
        try {
            List<ProductData> productDataList= new LinkedList<>();
            queryManager.setProductData(productDataList);
            return productDataList;
        }catch (SQLException e){
            return null;
        }

    }

}
