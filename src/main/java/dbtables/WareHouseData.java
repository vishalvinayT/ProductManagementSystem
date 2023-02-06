package dbtables;

public class WareHouseData {
    public String warehouseName;
    public String city=null;
    public String country=null;

    public WareHouseData(String warehouseName, String city, String country){
        this.warehouseName=warehouseName;
        this.city=city;
        this.country=country;
    }
}
