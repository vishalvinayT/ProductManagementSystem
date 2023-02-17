package ui;
import dbprocessor.DataProcessor;
import dbtables.ProductData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class ProductsPage extends ScreenStage {
    private JFrame productsFrame= new JFrame();
    private DataProcessor processor= new DataProcessor();
    List<String> productsList;
    private final JButton back= new JButton("Back");
    private final JButton cart= new JButton("Cart");
    private  boolean error=false;
    @Override
    public void init(){
        productsList= Arrays.asList(new String[]{"a","b","c","d","e"});
        this.error=productsList==null;
        setProductsPage();
        cart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsFrame.dispose();
                CheckoutPage checkout= new CheckoutPage();
                checkout.init();
            }
        });


    }

    private void setProductsPage(){
        resetFrame(productsFrame);
        productsFrame.setLayout(new BorderLayout());
        //fixme: adding head panel code can be improved
        productsFrame.add(headPanel(AddEnum.ADD_ALL, back,  cart),BorderLayout.NORTH);
        if(error){
            JLabel error= new JLabel("!!! Oops No Product Data To Display");
            error.setFont(errorFont);
            error.setHorizontalAlignment(SwingConstants.CENTER);
            productsFrame.add(error);
        }else{
            JPanel mainPanel= new JPanel();
            int cols=5;
            int rows=productsList.size()/cols;
            mainPanel.setLayout(new GridLayout(rows,cols));
            mainPanel.setAutoscrolls(true);
            int counter=0;
            for(int i=0;i<rows;i++){
                for(int j=0;j<cols;j++){
                    JPanel subPanel= new JPanel();
                    JButton button= new JButton(productsList.get(counter));
                    subPanel.setLayout(new GridBagLayout());
                    GridBagConstraints c= new GridBagConstraints();
                    c.gridx=0;
                    c.gridy=0;
                    c.weightx=2;
                    c.weighty=2;
                    c.anchor=GridBagConstraints.CENTER;
                    subPanel.add(button,c);
                    //subPanel.add(new JLabel("val"+counter));
                    button.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            productsFrame.dispose();
                            CheckoutPage checkout= new CheckoutPage();
                            checkout.init();
                        }
                    });
                    mainPanel.add(subPanel,i,j);
                    counter++;
                }
            }
            mainPanel.setBackground(getBackground());
            productsFrame.add(new JScrollPane(mainPanel),BorderLayout.CENTER); //Note

        }
        productsFrame.setVisible(true);
    }

}
