package dbtables;

public class Order {



    public Integer orderId;
    public String orderDate;//Not Null
    public Integer userId;
    public Double totalOrderPrice;

    public Order(){}
    public Order(Integer orderId,String orderDate,Integer userId, Double totalOrderPrice){
        this.orderId=orderId;
        this.orderDate=orderDate;
        this.userId=userId;
        this.totalOrderPrice=totalOrderPrice;
    }
}
