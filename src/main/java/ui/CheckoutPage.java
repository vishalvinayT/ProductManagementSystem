package ui;

import javax.swing.*;
import java.awt.*;

public class CheckoutPage extends ScreenStage{
    private JFrame checkoutFrame= new JFrame();

    private JButton back= new JButton("Back");
    @Override
    public void init(){
        setCheckoutPage();
    }

    private void setCheckoutPage(){
        resetFrame(checkoutFrame);
        checkoutFrame.setLayout(new BorderLayout());
        checkoutFrame.add(headPanel(AddEnum.ADD_BACK, back,  null),BorderLayout.NORTH);
        JPanel base=new JPanel(new GridBagLayout());
        base.setBackground(getBackground());
        JPanel address=new JPanel();
        address.setBackground(getBackground());
        address.setLayout(new BoxLayout(address,BoxLayout.Y_AXIS));
        JLabel shippingAddress=new JLabel("Shipping Address");
        shippingAddress.setFont(subheadingFont);
        JLabel userName=new JLabel(user.name);
        JLabel userStreet=new JLabel(user.street);
        JLabel userCountry=new JLabel(user.pincode+" "+user.country);
        JLabel userPhone= new JLabel(user.phone);
        address.add(shippingAddress);
        address.add(userName);
        address.add(userStreet);
        address.add(userCountry);
        address.add(userPhone);
        //testing

        checkoutFrame.setVisible(true);

    }
}
