package build.jproject;
import dataextractor.DataInjector;
import dbprocessor.QueryManager;


public class Main {
    public static void main(String[] args) {

//        DataInjector injector= new DataInjector();
//        injector.URLData();
        QueryManager manager=new QueryManager();
        manager.connect();
    }
}