package ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;


public abstract class ScreenStage  {
    private  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private final ImageIcon logo= new ImageIcon("./icons/logo.png");
    protected final JButton back= new JButton("Back");
    protected final JButton cart= new JButton("Cart");
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
    protected Font errorFont =new Font("Serif-Fonts", Font.ITALIC,48);


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

    public JPanel headPanel(AddEnum ignoreCase, JButton back,JButton cart){
        //fixme: adding head panel code can be improved and remove the code repetitions in future

        JPanel main= new JPanel();
        JLabel heading=new JLabel(TITLE);
        main.setBackground(getBackground());
        main.setLayout(new BorderLayout());
        heading.setFont(headingFont);
        heading.setHorizontalAlignment(SwingConstants.CENTER); // note
        main.add(heading,BorderLayout.CENTER);
        switch (ignoreCase){
            case ADD_ALL:
                if(cart!=null && back!=null){
                    main.add(back,BorderLayout.WEST);
                    main.add(cart,BorderLayout.EAST);
                    back.setFocusable(false);
                    cart.setFocusable(false);
                }
                break;
            case ADD_BACK:
                if(back!=null){
                    main.add(back,BorderLayout.WEST);
                    back.setFocusable(false);
                }
                break;
            case ADD_CART:
                if(cart!=null){
                    cart.setFocusable(false);
                    main.add(cart,BorderLayout.EAST);
                }
                break;

        }
        return main;
    }



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
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setBackground(defaultBackgroundColor);

    }

    protected void buttonTransparent(JButton button){
        button.setBorder(null);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
    }

    protected String nullToStr(Object obj){
        return obj!=null? obj.toString():"null";
    }

    protected BufferedImage readImage(byte[] imageArray) throws IOException {
        if(imageArray!=null){
            ByteArrayInputStream inputImage= new ByteArrayInputStream(imageArray); //note
            BufferedImage img= ImageIO.read(inputImage);
            return  img;
        }
        throw new NullPointerException();
    }



}
