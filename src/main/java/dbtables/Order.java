package dbtables;

public class Order {


    public String orderDate;//Not Null
    public Double totalOrderPrice;
    public Order(String orderDate, Double totalOrderPrice){
        this.orderDate=orderDate;
        this.totalOrderPrice=totalOrderPrice;
    }
}
