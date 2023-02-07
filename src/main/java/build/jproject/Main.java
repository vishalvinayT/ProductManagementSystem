package build.jproject;
import dataextractor.DataExtractor;
import dataextractor.DataInjector;
import dbprocessor.DataProcessor;
import dbprocessor.QueryManager;

import java.io.IOException;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) {

//        DataInjector injector= new DataInjector();
//        injector.URLData();
        try {
            DataProcessor processor=new DataProcessor();
            processor.executeInsertProductData();
        }catch (IOException e){
            System.out.println("Error Occurred");
            e.printStackTrace();
        }catch (SQLException f){
            System.out.println("SQL Error");
            f.printStackTrace();
        }

    }
}