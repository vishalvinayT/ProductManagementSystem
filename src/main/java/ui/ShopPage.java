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
import java.util.List;

public class ShopPage extends ScreenStage {
    private JFrame productsFrame= new JFrame();
    private DataProcessor processor= new DataProcessor();
    List<ProductData> productsList;

    private  boolean error=false;
    @Override
    public void init(){
        productsList= processor.fetchProductData();
        //this.error=productsList==null;
        setProductsPage();
        back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cart.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsFrame.dispose();
                CheckoutPage checkout= new CheckoutPage();
                checkout.init();
            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productsFrame.dispose();
                RegistrationPage registrationPage= new RegistrationPage();
                registrationPage.init();
            }
        });


    }

    //fixme: the magic numbers of sizes can be done according to screen dpi? better to develop an algo that decides the int value to divide
    private void setProductsPage()  {
        resetFrame(productsFrame);
        productsFrame.setLayout(new BorderLayout());
        //fixme: adding head panel code can be improved
        productsFrame.add(headPanel(AddEnum.ADD_ALL, back,  cart),BorderLayout.NORTH);
        if(error){
            productsFrame.add(errorPanel());
        }else{
            JPanel basePanel= new JPanel();
            basePanel.setBackground(getBackground());
            int cols=5;
            int rows=productsList.size()/cols;
            basePanel.setLayout(new GridLayout(rows,cols));
            basePanel.setAutoscrolls(true);
            productsFrame.setVisible(true);
            int counter=0;
            for(int i=0;i<rows;i++){
                for(int j=0;j<cols;j++){
                    try {
                        ProductData productData=productsList.get(counter);
                        JPanel subPanel= new JPanel();
                        subPanel.setBackground(getBackground());
                        subPanel.setPreferredSize(new Dimension(screenWidth/6,screenHeight/4)); //magic numbers
                        subPanel.setLayout(new GridBagLayout());
                        GridBagConstraints c= new GridBagConstraints();
                        c.gridx=0;
                        c.gridy=0;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        BufferedImage img = readImage(productData.productImageData);
                        Image pic=img.getScaledInstance(screenWidth/10,screenHeight/5,Image.SCALE_DEFAULT); //magic numbers

                        JLabel label=new JLabel(new ImageIcon(pic));
                        subPanel.add(label,c);

                        c.gridx=0;
                        c.gridy=2;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        JButton name=new JButton(productData.productName);
                        buttonTransparent(name);
                        name.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        subPanel.add(name,c);

                        c.gridx=0;
                        c.gridy=3;
                        c.gridwidth=1;
                        c.weightx=1;
                        c.weighty=1;
                        c.anchor=GridBagConstraints.CENTER;
                        JLabel price=new JLabel("€"+productData.productPrice);
                        subPanel.add(price,c);
                        name.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                productsFrame.dispose();
                                ProductPage productPage= new ProductPage(productData);
                                productPage.init();
                            }
                        });
                        basePanel.add(subPanel,i,j);
                        counter++;

                    }catch (IOException e){
                        continue;
                    }
                }
            }
            basePanel.setBackground(getBackground());
            productsFrame.add(new JScrollPane(basePanel),BorderLayout.CENTER); //Note

        }

    }

}
