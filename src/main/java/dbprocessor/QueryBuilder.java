package dbprocessor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class QueryBuilder {

    protected final String PRODUCTS_INSERT="insert into products values(?,?,?,?,?,?,?,?,?)";
    protected final String COMPANIES_INSERT="insert into companies values(?,?,?,?,?,?,?)";
    protected final String WAREHOUSE_INSERT="insert into warehouses values(?,?,?,?)";
    protected final String USERS_INSERT="insert into users values(?,?,?,?,?,?,?)";
    protected final String SHIPMENTS_INSERT="insert into shipments values(?,?,?,?,?,?,?,?)";
    protected final String ORDERS_INSERT="insert into orders values(?,?,?,?)";

    protected final String USER_EXTRACT="select * from users where email=? or name=? ";
    //protected final String USERNAME_EXTRACT="select id from users where name=?";
    //protected final String USEREMAIL_EXTRACT="select id from users where email=?";

    protected  final String PRODUCTS_EXTRACT="select p.id,p.company_id,p.productName,p.description,p.mfd_date," +
            "p.exp_date, p.quantity, p.price, p.productImage, c.warehouse_id, c.companyName, c.street,"+
            "c.city, c.pincode, c.country from products p join companies c where p.company_id=c.id";

    protected void insertData(PreparedStatement statement, Object[] objectData) throws SQLException {
        if(objectData!=null){
            for(int i=1;i<=objectData.length;i++){
                statement.setObject(i,objectData[i-1]);
            }
            statement.executeUpdate();
        }
    }

    protected ResultSet extractData(PreparedStatement statement) throws SQLException{
        return statement.executeQuery();
    }
}
