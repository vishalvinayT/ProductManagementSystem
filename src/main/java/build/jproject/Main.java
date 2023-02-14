package build.jproject;
import  javax.swing.*;
import dbprocessor.DataProcessor;
import ui.UI;


public class Main  {
    public static void main(String[] args) {

        // todo: take arguments from main and and decide to extract and insert data
//        DataInjector injector= new DataInjector();
        //injector.URLData();
//        try {
//            DataProcessor processor=new DataProcessor();
//            processor.executeInsertProductData();
//        }catch (Exception e) {
//            System.out.println("Error Occurred");
//            e.printStackTrace();
//        }

        UI ui= new UI();
        ui.displayEntryPage();

    }

}