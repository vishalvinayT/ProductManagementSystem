package ui;

import dbprocessor.DataProcessor;
import dbtables.ProductData;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.jar.JarEntry;

public class ProductsPage extends ScreenStage {
    private JFrame productsFrame= new JFrame();
    private DataProcessor processor= new DataProcessor();

    List<ProductData> productsList;
    @Override
    public void init(){
        setProductsPage();

//        productsList=processor.fetchProductData();
//        if(productsList!=null){
//            setProductsPage();
//        }
    }


    @Override
    public JPanel headPanel(){
        JPanel main= new JPanel();
        JLabel heading=new JLabel(TITLE);
        JButton back= new JButton();
        JButton cart= new JButton();
        back.setFocusable(false);
        cart.setFocusable(false);
        main.setBackground(getBackground());
        main.setLayout(new GridLayout(0,3));
        heading.setFont(headingFont);
        main.add(back,0,0);
        main.add(heading,0,1);
        main.add(cart,0,2);
        return main;
    }


    private void setProductsPage(){
        resetFrame(productsFrame);
        productsFrame.setLayout(new BorderLayout());


        JPanel mainPanel= new JPanel();
        mainPanel.setLayout(new GridLayout(30,5));
        mainPanel.setAutoscrolls(true);
        int counter=0;
        for(int i=0;i<30;i++){
            for(int j=0;j<5;j++){
                JPanel subPanel= new JPanel();
                subPanel.setLayout(new FlowLayout());
                subPanel.add(new JLabel("val"+counter));
                mainPanel.add(subPanel,i,j);
                counter++;
            }
        }



        JPanel headPanel=headPanel();
        mainPanel.setBackground(getBackground());
        productsFrame.add(headPanel,BorderLayout.NORTH);
        productsFrame.add(new JScrollPane(mainPanel),BorderLayout.CENTER);
        productsFrame.setVisible(true);

    }

}
