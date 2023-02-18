package ui;
import dbprocessor.DataProcessor;
import dbtables.ProductData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ProductsPage extends ScreenStage {
    private JFrame productsFrame= new JFrame();
    private DataProcessor processor= new DataProcessor();
    List<ProductData> productsList;
    private final JButton back= new JButton("Back");
    private final JButton cart= new JButton("Cart");
    private  boolean error=false;
    @Override
    public void init(){
        productsList= processor.fetchProductData();
        //this.error=productsList==null;
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

    private void setProductsPage()  {
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
            productsFrame.setVisible(true);
            int counter=0;
            for(int i=0;i<rows;i++){
                for(int j=0;j<cols;j++){
                    try {
                        ProductData productData=productsList.get(counter);
                        JPanel subPanel= new JPanel();
                        subPanel.setSize(new Dimension(screenWidth/6,screenHeight/5));
                        JButton button= new JButton();
                        subPanel.setLayout(new GridBagLayout());
                        GridBagConstraints c= new GridBagConstraints();
                        c.gridx=0;
                        c.gridy=0;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        ByteArrayInputStream inputImage= new ByteArrayInputStream(productData.productImageData); //note
                        BufferedImage img= ImageIO.read(inputImage);
                        Image pic=img.getScaledInstance(screenWidth/10,screenHeight/5,Image.SCALE_DEFAULT);

                        JLabel label=new JLabel(new ImageIcon(pic));
                        subPanel.add(label,c);

                        c.gridx=0;
                        c.gridy=2;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        JLabel name=new JLabel(productData.productName);
                        subPanel.add(name,c);

                        c.gridx=0;
                        c.gridy=3;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        JLabel price=new JLabel("€"+productData.productPrice);
                        subPanel.add(price,c);
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

                    }catch (IOException e){
                        continue;
                    }
                }
            }
            mainPanel.setBackground(getBackground());
            productsFrame.add(new JScrollPane(mainPanel),BorderLayout.CENTER); //Note

        }

    }

}
