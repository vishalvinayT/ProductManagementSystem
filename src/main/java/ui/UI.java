package ui;

import dbtables.ProductData;
import dbtables.User;

import java.util.HashMap;
import java.util.Map;

public class UI {

    protected static Map<ProductData,Integer> cartList= new HashMap<>();
    protected static User user;

    public void displayEntryPage(){
        EntryPage entryPage=new EntryPage();
        entryPage.init();
    }
}
