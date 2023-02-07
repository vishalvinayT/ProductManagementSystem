package dbtables;

import java.util.Date;

public class Shipment {
    public Integer productId;//Not Null
    public Integer userID;//Not NUll
    public Integer quantityOrdered;//Not Null
    public String dateOfOrder;//Not Null
    public Double pricePerUnit;//Not Null
    public Double totalPrice;// Not Null

    public Shipment(){}
    public Shipment(Integer productId, Integer userID, Integer quantityOrdered,String dateOfOrder, Double pricePerUnit, Double totalPrice ){
        this.productId=productId;
        this.userID=userID;
        this.quantityOrdered=quantityOrdered;
        this.dateOfOrder=dateOfOrder;
        this.pricePerUnit=pricePerUnit;
        this.totalPrice=totalPrice;
    }
}
