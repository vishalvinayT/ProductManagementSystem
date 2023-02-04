package dbprocessor;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public abstract class QueryBuilder {
    protected final String PRODUCTS_INSERT="insert into products values(?,?,?,?,?,?,?,?,?)";
    protected final String COMPANIES_INSERT="insert into companies values(?,?,?,?,?,?,?)";
    protected final String WAREHOUSE_INSERT="insert into warehouses values(?,?,?,?)";
    protected final String USERS_INSERT="insert into users values(?,?,?,?,?,?,?)";
    protected final String SHIPMENTS_INSERT="insert into shipments values(?,?,?,?,?,?,?,?)";
    protected final String ORDERS_INSERT="insert into orders values(?,?,?,?)";

    protected void insertData(PreparedStatement statement, Object[] objectData) throws SQLException {
        ResultSetMetaData set=statement.getMetaData();
        int count=set.getColumnCount();
        if(count==objectData.length){
            for(int i=0;i<objectData.length;i++){
                statement.setObject(i,objectData[i]);
            }
            statement.executeUpdate();
        }
    }

}
