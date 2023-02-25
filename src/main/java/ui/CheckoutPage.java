package ui;

import dbtables.ProductData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class CheckoutPage extends ScreenStage{
    private JFrame checkoutFrame= new JFrame();

    private JButton back= new JButton("Back");
    private JButton checkoutButton= new JButton("Checkout");

    private Double totalPrice=0.0;
    protected  static Double taxPercent=0.5;

    @Override
    public void init(){
        try {
            back.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setCheckoutPage();

            back.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ShopPage page= new ShopPage();
                    page.init();
                }
            });

            checkoutButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkoutFrame.dispose();
                    FinalPage finalPage= new FinalPage();
                    finalPage.init();
                }
            });

        }catch (RuntimeException e){
            checkoutFrame.add(errorPanel());

        }
        checkoutFrame.setVisible(true);

    }

    private void setCheckoutPage() throws RuntimeException{
        resetFrame(checkoutFrame);
        if(cartList.size()<=0){
            checkoutFrame.add(errorPanel());
            return;
        }
        checkoutFrame.setLayout(new BorderLayout());
        checkoutFrame.add(headPanel(AddEnum.ADD_BACK, back,  null),BorderLayout.NORTH);
        JPanel base=new JPanel(new GridBagLayout());
        base.setBackground(getBackground());
        JPanel address=new JPanel();
        address.setBackground(getBackground());
        address.setLayout(new BoxLayout(address,BoxLayout.Y_AXIS));
        JLabel shippingAddress=new JLabel("Shipping Address");
        shippingAddress.setFont(subheadingFont);
        JLabel userName=new JLabel("user.name");
        JLabel userStreet=new JLabel("user.street");
        JLabel userCountry=new JLabel("user.pincode"+" "+"user.country");
        JLabel userPhone= new JLabel("user.phone");
        address.add(shippingAddress);
        address.add(userName);
        address.add(userStreet);
        address.add(userCountry);
        address.add(userPhone);
        add_component(base,address,0,0,5,1,5,(float) 0.2,GridBagConstraints.CENTER);
        generateInvoice(base);
        checkoutFrame.add(new JScrollPane(base));
    }


    //fixme: check for out of stock

    private JPanel generateInvoice(JPanel invoicePanel){
        int counter=2;
        invoicePanel.setBackground(getBackground());
        JLabel product= new JLabel("Product");
        product.setFont(subheadingFont);
        JLabel quantity=new JLabel("Quantity");
        quantity.setFont(subheadingFont);
        JLabel price= new JLabel("Price");
        price.setFont(subheadingFont);
        add_component(invoicePanel,product,0,1,1,1, (float) 0.2,(float)0.05, GridBagConstraints.CENTER);
        add_component(invoicePanel,quantity,1,1,1,1,(float) 0.2,(float)0.05, GridBagConstraints.CENTER);
        add_component(invoicePanel,price,2,1,1,1,(float) 0.2,(float)0.05, GridBagConstraints.CENTER);
        for(Map.Entry<ProductData,Integer> cart:cartList.entrySet()){
            ProductData productData=cart.getKey();
            JButton productButton= new JButton(productData.productName);
            buttonTransparent(productButton);
            Integer value=cart.getValue();
            productButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            JPanel quantityEdit= new JPanel();
            quantityEdit.setLayout(new BoxLayout(quantityEdit,BoxLayout.X_AXIS));
            quantityEdit.setBackground(getBackground());
            JButton addButton=new JButton();
            JButton removeButton= new JButton();
            JButton deleteButton= new JButton();
            ImageIcon add= new ImageIcon("./icons/add.png");
            ImageIcon remove= new ImageIcon("./icons/substract.png");
            ImageIcon delete= new ImageIcon("./icons/delete.png");
            Image addImg=add.getImage().getScaledInstance(add.getIconWidth()/12,add.getIconHeight()/12,Image.SCALE_SMOOTH);
            Image removeImg=remove.getImage().getScaledInstance(add.getIconWidth()/12,add.getIconHeight()/12,Image.SCALE_SMOOTH);
            Image deleteImg=delete.getImage().getScaledInstance(add.getIconWidth()/12,add.getIconHeight()/12,Image.SCALE_SMOOTH);
            addButton.setIcon(new ImageIcon(addImg));
            removeButton.setIcon(new ImageIcon(removeImg));
            deleteButton.setIcon(new ImageIcon(deleteImg));
            JLabel quantityLabel=new JLabel(value.toString());
            buttonTransparent(addButton);
            buttonTransparent(removeButton);
            buttonTransparent(deleteButton);
            addButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            removeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            deleteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            quantityEdit.add(addButton);
            quantityEdit.add(removeButton);
            quantityEdit.add(deleteButton);
            quantityEdit.add(quantityLabel);
            Double totalPrice=cart.getValue() * productData.productPrice;
            this.totalPrice+=totalPrice;
            JLabel priceLabel= new JLabel(String.format("€ %.2f",totalPrice));
            add_component(invoicePanel,productButton,0,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTH);
            add_component(invoicePanel,quantityEdit,1,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTH);
            add_component(invoicePanel,priceLabel,2,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTH);
            counter++;
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cartList.put(productData, value+1);
                    refreshFrame();
                }
            });
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(value<1){
                        cartList.remove(productData);

                    }else {
                        cartList.put(productData,value-1);
                    }
                    refreshFrame();
                }
            });

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cartList.remove(productData);
                    refreshFrame();
                }
            });
        }
        JPanel plainPanel=new JPanel();
        plainPanel.setBackground(getBackground());
        JPanel totalPricePanel=new JPanel();
        totalPricePanel.setBackground(getBackground());
        totalPricePanel.setLayout(new BoxLayout(totalPricePanel,BoxLayout.Y_AXIS));
        JLabel finalPrice=new JLabel(String.format("Total Price: € %.2f ", this.totalPrice));
        Double tax= calculateTax();
        JLabel taxPrice=new JLabel(String.format("Tax: € %.2f",tax));
        Double finalPricewTax=this.totalPrice+tax;
        JLabel finalPriceWithTax=new JLabel(String.format("Total Price: € %.2f",finalPricewTax));

        checkoutButton.setBackground(Color.GREEN);
        checkoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        //checkoutButton.setPreferredSize(new Dimension(totalPricePanel.getWidth(),totalPricePanel.getHeight()/4));
        totalPricePanel.add(finalPrice);
        totalPricePanel.add(taxPrice);
        totalPricePanel.add(finalPriceWithTax);
        totalPricePanel.add(checkoutButton);
        add_component(invoicePanel,plainPanel,0,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTHEAST);
        add_component(invoicePanel,plainPanel,1,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTHEAST);
        add_component(invoicePanel,totalPricePanel,2,counter,1,1,(float) 0.2,(float)0.05,GridBagConstraints.NORTH);
        return invoicePanel;
    }

    private Double calculateTax(){
        Double tax= taxPercent/100;
        Double taxValue=tax*this.totalPrice;
        return taxValue;
    }

    private void refreshFrame(){
        checkoutFrame.dispose();
        CheckoutPage page=new CheckoutPage();
        page.init();
    }


    protected void add_component(JPanel panel, Component component, int x, int y, int width, int height, float xgridweight, float ygridweight, int  anchor ){
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.gridx=x;
        constraints.gridy=y;
        constraints.gridwidth=width;
        constraints.gridheight=height;
        constraints.weightx=xgridweight;
        constraints.weighty=ygridweight;
        constraints.anchor=anchor;
        panel.add(component,constraints);
    }
}
