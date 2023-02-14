package dbprocessor;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dataextractor.Utilities;
import dbtables.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.File;
import java.sql.*;
import java.text.ParseException;
import java.util.List;
import java.util.Random;

public class QueryManager extends QueryBuilder{

    private Config config= ConfigFactory.parseFile(new File("./dbConfig.json"));
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
    public void insertUserData(User user) throws SQLException{
        if(user!=null){
            Integer userID=generateUniqueId(user.email);
            Object[] userData= new Object[]{userID,user.name,user.email,user.phone,user.country,user.street,user.pincode};
            insertData(insertUsers,userData);
        }
    }

    public void insertOrder(User user, Order order, List<Shipment> shipments) throws SQLException{
        if(user!=null && shipments!=null && order!=null){
            PreparedStatement statement= connection.prepareStatement("select id from users where email= ?");
            statement.setString(1,user.email);
            ResultSet set=statement.executeQuery();
            Integer userID=set.getInt(1);
            if(userID!=null){
                String uniqueString=(order.orderDate+order.totalOrderPrice+user.email).replaceAll("[.-/]","");
                Integer orderID=generateUniqueId(uniqueString);
                for(Shipment shipment:shipments){
                    if(shipment!=null){
                        insertShipment(userID,orderID,shipment);
                    }
                }
                Object[] orderData= new Object[]{orderID,order.orderDate,order.totalOrderPrice};
                insertData(insertOrders,orderData);
                return;
            }
            System.out.println("Please Register!");
        }
    }


    private void insertShipment(Integer userID,Integer orderID, Shipment shipment) throws SQLException{
        if(userID!=null&&orderID!=null&&shipment!=null){
            if(userID==shipment.userID){
                String uniqueString=(shipment.productId+shipment.userID+shipment.dateOfOrder).replaceAll("[.-/]","");
                Integer shipmentID=generateUniqueId(uniqueString);
                Object[] shipmentsData=new Object[]{shipmentID,shipment.productId,orderID,shipment.userID,shipment.quantityOrdered,shipment.dateOfOrder,shipment.pricePerUnit,shipment.totalPrice};
                insertData(insertShipments,shipmentsData);
                return;
            }
            throw new RuntimeException();

        }
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
