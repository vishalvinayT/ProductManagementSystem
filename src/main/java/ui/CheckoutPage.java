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
        checkoutFrame.setVisible(true);
    }
}
