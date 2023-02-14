package ui;

import javax.swing.*;
import java.awt.*;


public abstract class ScreenStage  {
    private  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ImageIcon logo= new ImageIcon("./icons/logo.png");
    private Color defaultBackgroundColor= new Color(0xD2B1EA);
    protected  final int screenWidth = screenSize.width;
    protected final int screenHeight= screenSize.height;
    public void setStage(JFrame frame){
        frame.setSize(screenWidth,screenHeight);
        frame.setTitle("GroceryDeliverySystem");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.getContentPane().setBackground(defaultBackgroundColor);
        frame.setIconImage(logo.getImage());
    }


    protected  Color getBackground(){
        return defaultBackgroundColor;
    }

    public void init(){}

}
