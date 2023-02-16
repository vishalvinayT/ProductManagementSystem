package ui;

import javax.swing.*;
import java.awt.*;


public abstract class ScreenStage  {
    private  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ImageIcon logo= new ImageIcon("./icons/logo.png");
    private Color defaultBackgroundColor= new Color(0xD2B1EA);
    protected  final int screenWidth = screenSize.width;
    protected final int screenHeight= screenSize.height;
    protected Insets headingSpace= new Insets(5,20,5,20);
    protected Insets boxSpace = new Insets(5,20,15,20);
    protected Insets buttonSpace=new Insets(5,0,5,0);
    protected  Dimension textBoxDimension= new Dimension(300,30);


    protected Font headingFont= new Font("Serif-Fonts", Font.ITALIC,32);
    protected  Font subheadingFont= new Font("Serif-Fonts", Font.ITALIC,24);
    protected  Font labelFont= new Font("Serif-Fonts", Font.ITALIC,14);

    protected final String TITLE="MiniMart";
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

    public abstract void init();

    public abstract JPanel headPanel();


    //note
    protected GridBagConstraints formatGrid(GridBagConstraints input, int gridx, int gridy, Insets insets, int side){
        input.gridx=gridx;
        input.gridy=gridy;
        input.insets=insets;
        input.anchor=side;
        return input;
    }

    protected void resetFrame(JFrame frame){
        frame.setSize(screenWidth,screenHeight);
        frame.setIconImage(logo.getImage());
        frame.setBackground(defaultBackgroundColor);

    }

    protected void addItem(Object item, JPanel panel){
        if(item!=null && panel!=null){
            JPanel spanel= new JPanel();
            spanel.setLayout(new BorderLayout());
            spanel.add((Component) item, BorderLayout.CENTER);
            panel.add(spanel);
        }
    }

}
