package ui;


import dbtables.ProductData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ProductPage extends  ScreenStage{

    private ProductData productData;
    private JFrame productFrame=new JFrame();

    private JButton add= new JButton("Add");
    private JButton remove= new JButton("Remove");

    private JButton quantityButton= new JButton();
    private Integer productQuantity= 0;

    public ProductPage(ProductData productData){
        this.productData=productData;
    }
    @Override
    public void init() {
        try {
            setProductPage();
            add.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            remove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            add.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cartList.add(productData);
                    productQuantity+=1;
                    refreshButton(quantityButton);
                }
            });

            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(productQuantity>0){
                        cartList.remove(productData);
                        productQuantity-=1;
                        refreshButton(quantityButton);
                    }
                }
            });

            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    productFrame.dispose();
                    ShopPage shopPage=new ShopPage();
                    shopPage.init();
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void setProductPage() throws IOException {
        resetFrame(productFrame);
        productFrame.setLayout(new BorderLayout());
        productFrame.add(headPanel(AddEnum.ADD_BACK, back, null),BorderLayout.NORTH);
        JPanel productPanel= new JPanel();
        productPanel.setLayout(new GridBagLayout());
        BufferedImage img= readImage(productData.productImageData);
        Image pic= img.getScaledInstance(screenWidth/5,screenHeight/2,Image.SCALE_DEFAULT);
        JLabel image= new JLabel(new ImageIcon(pic));
        add_component(productPanel,image,0,0,1,1,GridBagConstraints.CENTER);
        add_component(productPanel,introPanel(),1,0,1,1,GridBagConstraints.WEST);
        add_component(productPanel,descriptionPanel(),0,1,1,1,GridBagConstraints.CENTER);
        productFrame.add(new JScrollPane(productPanel));
        productFrame.setVisible(true);
    }

    private JPanel introPanel(){
        JPanel psubPanel=new JPanel();
        psubPanel.setLayout(new GridBagLayout());
        JLabel productName=new JLabel(productData.productName);
        productName.setFont(subheadingFont);
        add_component(psubPanel,productName,0,0,5,1,GridBagConstraints.WEST);
        JLabel productPrice=new JLabel("€"+productData.productPrice);
        productPrice.setFont(subheadingFont);
        add_component(psubPanel,productPrice,0,1,3,1,GridBagConstraints.WEST);
        buttonTransparent(quantityButton);
        refreshButton(quantityButton);
        buttonTransparent(add);
        buttonTransparent(remove);
        JPanel buttonPanel=new JPanel();
        buttonPanel.setLayout(new GridBagLayout());
        add_component(buttonPanel,add,0,0,1,1,GridBagConstraints.WEST);
        add_component(buttonPanel,quantityButton,1,0,1,1,GridBagConstraints.CENTER);
        add_component(buttonPanel,remove,2,0,1,1,GridBagConstraints.EAST);
        add_component(psubPanel,buttonPanel,0,2,3,1,GridBagConstraints.WEST);
        return psubPanel;
    }

    private void refreshButton(JButton button){
        String buttonText=productQuantity>0 ? ""+productQuantity:"";
        button.setText(buttonText);
    }

    private JPanel descriptionPanel(){
        Map<Object,Object> infoMap= new HashMap<>();
        JPanel desPanel=new JPanel();
        desPanel.setLayout(new GridBagLayout());
        JLabel desHeading= new JLabel("Product Description");
        desHeading.setFont(subheadingFont);

        JPanel infoTable=new JPanel();
        infoTable.setLayout(new GridBagLayout());
        infoMap.put("Product Information:", productData.productInformation);
        infoMap.put("Manufacturing Date:", productData.mfdate);
        infoMap.put("Expiry Date:", productData.expdate);
        infoMap.put("Manufacturer:", productData.company);
        infoMap.put("Street:", productData.street);
        infoMap.put("Pincode:", productData.pincode);
        infoMap.put("City:", productData.city);
        int row=0;
        for (Map.Entry<Object,Object> vals: infoMap.entrySet()){
            JLabel name=new JLabel(nullToStr(vals.getKey()));
            String valueString=nullToStr(vals.getValue());
            JPanel valuePanel=processLargeText(valueString);
            add_component(infoTable,name,0,row,1,1,GridBagConstraints.WEST);
            add_component(infoTable,valuePanel,1,row,1,1,GridBagConstraints.WEST);
            row++;
        }

        add_component(desPanel,desHeading,0,0,1,1,GridBagConstraints.WEST);
        add_component(desPanel,infoTable,0,1,1,1,GridBagConstraints.WEST);
        return desPanel;
    }





    private JPanel processLargeText(String value){
        JPanel panel = new JPanel(new GridLayout(0,1));
        if(value!=null){
            String[] valueArr;
            if(value.length()>150){ //magic number
                valueArr=value.split("\\.");
            }else{
                valueArr=new String[]{value};
            }
            for(String val:valueArr){
             JLabel label= new JLabel(val);
             panel.add(label);
            }
        }
        return panel;
    }



    private void add_component(JPanel panel, Component component, int x , int y, int width, int height, int anchor){
        GridBagConstraints constraints= new GridBagConstraints();
        constraints.gridy=y;
        constraints.gridx=x;
        constraints.weightx=width;
        constraints.weighty=height;
        constraints.anchor=anchor;
        panel.add(component,constraints);
    }


}
