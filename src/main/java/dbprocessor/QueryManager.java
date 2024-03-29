package dbprocessor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dataextractor.Utilities;
import dbtables.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.util.Date;

public class QueryManager extends QueryBuilder{

    //private Config config= ConfigFactory.parseFile(new File("./configs/dbConfig.json"));

    private Config config= ConfigFactory.load("dbConfig.json");
    public static Connection connection;
    private final String DRIVER=config.getString("driver");
    private final String HOST= config.getString("host");
    private final String PORT=config.getString("port");
    private final String DB=config.getString("db");
    private final String USERNAME=System.getenv("MySqlUserName");
    private final String DEADBEEF=System.getenv("MySqlPassword");

    private static final Integer PRIME=37;

    private PreparedStatement insertProducts;
    private PreparedStatement insertCompanies;
    private PreparedStatement insertWarehouses;

    private PreparedStatement insertUsers;

    private PreparedStatement insertOrders;

    private PreparedStatement insertShipments;

    private PreparedStatement extractProductData;
    private PreparedStatement extractUserData;


    public QueryManager(){
        connect();
    }

    private void connect(){
        try {
            Class.forName(String.format("com.%s.cj.jdbc.Driver",DRIVER)); // note
            String connectionUrl=String.format("jdbc:%s://%s:%s/%s?verifyServerCertificate=false&useSSL=false",DRIVER,HOST,PORT,DB);
            connection= DriverManager.getConnection(connectionUrl,USERNAME,DEADBEEF);
            insertProducts=connection.prepareStatement(PRODUCTS_INSERT);
            insertCompanies= connection.prepareStatement(COMPANIES_INSERT);
            insertWarehouses= connection.prepareStatement(WAREHOUSE_INSERT);
            insertUsers=connection.prepareStatement(USERS_INSERT);
            insertOrders=connection.prepareStatement(ORDERS_INSERT);
            insertShipments=connection.prepareStatement(SHIPMENTS_INSERT);
            extractProductData=connection.prepareStatement(PRODUCTS_EXTRACT);
            extractUserData=connection.prepareStatement(USER_EXTRACT);
            /*extractUsername=connection.prepareStatement(USERNAME_EXTRACT);
            extractUserEmail=connection.prepareStatement(USEREMAIL_EXTRACT);*/
            System.out.println();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


//todo: the dates and images can be inserted in more dynamic way not by hardcoding
    public void insertProductData(WareHouseData wareHouseData, ProductData productData) throws SQLException, FileNotFoundException {
        if(productData!=null && wareHouseData!=null){
            connection.setAutoCommit(false);
            Integer companyId=getCompanyID(wareHouseData,productData);
            PreparedStatement statement=connection.prepareStatement("select id from products where productName=? and company_id=?");
            if(companyId!=null && productData.productName!=null){
                statement.setString(1,productData.productName);
                statement.setInt(2,companyId);
                ResultSet set=statement.executeQuery();
                Integer productId=set.next()!=false? set.getInt(1):null;
                if (productId==null){
                    productId=generateUniqueId("Product"+productData.productName);
                    Date mfdDate=convertStringDate(productData.mfdate); // the date and images can be set while defining product data
                    Date expDate=convertStringDate(productData.expdate); // this is hard coded setting can be improved
                    FileInputStream productImage=new FileInputStream(productData.productImage);
                    Object[] product=new Object[]{productId,companyId,productData.productName,productData.productInformation,mfdDate,expDate,productData.quantity,productData.productPrice,productImage};
                    insertData(insertProducts,product);
                }
            }
            connection.commit();
            connection.setAutoCommit(true);
        }
    }



    // fixme later: check also with the warehouse (what if an company is registered at another warehouse)
    private Integer getCompanyID(WareHouseData wareHouseData,ProductData productData) throws SQLException{
        if(productData!=null && wareHouseData!=null){
            PreparedStatement statement=connection.prepareStatement("select id from companies where companyName=?");
            statement.setString(1,productData.company);
            Integer warehouseId=getWarehouseID(wareHouseData);
            ResultSet set=statement.executeQuery();
            Integer companyID=set.next()!=false? set.getInt(1):null;
            if(companyID==null){
                companyID=generateUniqueId("company"+productData.company);
                Object[] companyData=new Object[]{companyID,warehouseId,productData.company,productData.street,productData.city,productData.pincode,productData.country};
                insertData(insertCompanies,companyData);
            }
            return companyID;
        }
        throw  new NullPointerException();
    }


    private Integer getWarehouseID(WareHouseData wareHouseData) throws SQLException{
        if(wareHouseData!=null){
            PreparedStatement statement=connection.prepareStatement("select id from warehouses where warehouseName=?");
            statement.setString(1,wareHouseData.warehouseName);
            ResultSet set=statement.executeQuery();
            Integer warehouseID=set.next()!=false? set.getInt(1):null;
            if(warehouseID==null){
                warehouseID=generateUniqueId("Warehouse"+wareHouseData.warehouseName);
                Object[] warehouse=new Object[]{warehouseID,wareHouseData.warehouseName,wareHouseData.city,wareHouseData.country};
                insertData(insertWarehouses,warehouse);
            }
            return warehouseID;
        }
        throw new NullPointerException();
    }



    // todo: can add password field in future
    public boolean insertUserData(User user) throws SQLException{
        if(user!=null){
            Integer userID=generateUniqueId(user.email);
            Object[] userData= new Object[]{userID,user.name,user.email,user.phone,user.country,user.street,user.pincode};
            insertData(insertUsers,userData);
            return true;
        }
        return false;
    }

    public void insertOrder(User user, Order order, List<Shipment> shipments) throws SQLException{
        if(user!=null && shipments!=null && order!=null){
            Integer userID=user.id;
            if(userID!=null){
                connection.setAutoCommit(false);
                String uniqueString=(order.orderDate+order.totalOrderPrice+user.email).replaceAll("[.-/]","");
                Integer orderID=generateUniqueId(uniqueString);
                Object[] orderData= new Object[]{orderID,convertStringDate(order.orderDate), userID,order.totalOrderPrice};
                insertData(insertOrders,orderData);
                for(Shipment shipment:shipments){
                    if(shipment!=null){
                        insertShipment(userID,orderID,shipment);
                    }
                }
                connection.commit();
                connection.setAutoCommit(true);
                return;
            }
            throw new NullPointerException();
        }
    }


    private void insertShipment(Integer userID,Integer orderID, Shipment shipment) throws SQLException{
        if(userID!=null&&orderID!=null&&shipment!=null){
            if(userID==shipment.userID){
                String uniqueString=(shipment.productId+shipment.userID+shipment.dateOfOrder).replaceAll("[.-/]","");
                Integer shipmentID=generateUniqueId(uniqueString);
                Object[] shipmentsData=new Object[]{shipmentID,shipment.productId,orderID,shipment.userID,shipment.quantityOrdered,convertStringDate(shipment.dateOfOrder),shipment.pricePerUnit,shipment.totalPrice};
                insertData(insertShipments,shipmentsData);
                return;
            }
            throw new NullPointerException();

        }
    }
    protected User extractUser(String user) throws SQLException{
        extractUserData.setString(1,user);
        extractUserData.setString(2,user);
        ResultSet set=extractData(extractUserData);
        if(set.next()){
            return new User(set.getInt("id"),strCoverter(set.getString("name")),
                    strCoverter(set.getString("email")),strCoverter(set.getString("phone")),
                    strCoverter(set.getString("country")),strCoverter(set.getString("street")),
                    set.getString("pincode"));
        }
        return null;
    }

    protected void setProductData(List<ProductData> productsList) throws  SQLException{


        ResultSet productSet=extractData(extractProductData);
        while(productSet.next()){
            ProductData productData= new ProductData(productSet.getInt("id"), productSet.getInt("company_id"),productSet.getInt("warehouse_id"),strCoverter(productSet.getString("companyName")),strCoverter(productSet.getString("country")),
                    strCoverter(productSet.getString("street")),strCoverter(productSet.getString("city")),strCoverter(productSet.getString("pincode")),
                    strCoverter(productSet.getString("productName")),strCoverter(productSet.getString("description")),
                    strCoverter(productSet.getDate("mfd_date")),strCoverter(productSet.getDate("exp_date")) ,
                    productSet.getInt("quantity"),productSet.getDouble("price"), processBlob( productSet.getBlob("productImage")));
            productsList.add(productData);
        }
    }




    private String strCoverter(Object object){
        return object!=null? object.toString():null;
    }

    private byte[] processBlob(Blob blob){
        try {
            if(blob!=null){
                return blob.getBytes(1L,  (int) blob.length());
            }
        }catch (SQLException e){
            return null;
        }
        return null;
    }




    // fixme: is uniqueID is generated by hardcoding?
    private Integer generateUniqueId(String value){
        if(value!=null){
            value=value.replaceAll("[-_ *&()@.,/]","");
            Integer val=value.hashCode()*PRIME+new Random().nextInt(Short.MAX_VALUE);
            return  val<0?val*-1:val;
        }
        return null;
    }



    private Date convertStringDate(String stringDate){
        Date date;
        if(stringDate!=null){
            try {
                stringDate=stringDate.replaceAll("T"," ");
                date= Utilities.formatter.parse(stringDate);
            } catch (ParseException e) {
                SimpleDateFormat format=new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
                try {
                    date=format.parse(stringDate);
                } catch (ParseException ex) {
                    return null;
                }
            }
            return date;
        }
        return null;
    }





}
