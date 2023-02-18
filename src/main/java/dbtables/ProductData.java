package dbtables;

import dataextractor.Utilities;

import java.io.FileInputStream;
import java.util.stream.Stream;

public class ProductData {
    public Integer productId=null;
    public Integer companyId=null;
    public Integer warehouse_id=null;

    public String company= Utilities.DEFAULT_COMPANY; //Notnull
    public String country=Utilities.DEFAULT_COUNTRY;//Notnull
    public String street=null;
    public String city=null;
    public String pincode=null;
    public String productName=null; //Not null
    public String productInformation=null;
    public String mfdate=Utilities.DEFAULT_MFD_DATE;
    public String expdate=null;
    public Integer quantity=null;
    public Double productPrice=Utilities.DEFAULT_PRICE; //Notnull
    public String productImage=Utilities.DEFAULT_IMAGE;//Notnull

    public byte[] productImageData=null;
    public ProductData(){}

    public ProductData(Integer productId,Integer companyId, Integer warehouse_id,  String company, String country, String street,String city, String pincode,String productName,String productInformation, String mfdate,String expdate, Integer quantity, Double productPrice, byte[] productImageData){
        this.productId=productId;
        this.companyId=companyId;
        this.warehouse_id=warehouse_id;
        this.company=company;
        this.country=country;
        this.street=street;
        this.city=city;
        this.pincode=pincode;
        this.productName=productName;
        this.productInformation=productInformation;
        this.mfdate=mfdate;
        this.expdate=expdate;
        this.quantity=quantity;
        this.productPrice=productPrice;
        this.productImageData=productImageData;
    }



}
