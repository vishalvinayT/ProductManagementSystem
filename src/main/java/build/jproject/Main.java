package build.jproject;
import dataextractor.DataInjector;
import dbprocessor.DataProcessor;
import ui.UI;


public class Main  {
    private static boolean extractData=false;
    private static boolean insertData=false;
    public static void main(String[] args) {
        processArgs(args);
        try {
            if(extractData){
                DataInjector injector= new DataInjector();
                injector.URLData();
            }
            else if(insertData){
                DataProcessor processor=new DataProcessor();
                processor.read();
                processor.executeInsertProductData();
            }
            UI ui= new UI();
            ui.displayEntryPage();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Failure");
        }

    }

    private static void processArgs(String[] args){
        if(args!=null){
            for (String arg: args){
                switch (arg.toUpperCase()){
                    case "EXTRACTDATA":
                        extractData=true;
                        break;
                    case "INSERTDATA":
                        insertData=true;
                        break;
                }
            }
        }
    }


}