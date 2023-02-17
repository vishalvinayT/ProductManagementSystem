package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.jar.JarEntry;

public class RegistrationPage extends ScreenStage {
    private JFrame registrationPage= new JFrame();
    private JButton submitButton= new JButton("Submit");
    private JTextField usernameBox= new JTextField();
    private JTextField emailBox=new JTextField();
    private JTextField phoneBox=new JTextField();
    private JTextField countryBox=new JTextField();
    private JTextField streetBox=new JTextField();
    private JTextField pincodeBox=new JTextField();

    private final JButton back= new JButton("Back");

    @Override
    public void init(){
        setRegistrationPage();
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationPage.dispose();
                ProductsPage productsPage= new ProductsPage();
                productsPage.init();
            }
        });
    }



    private void setRegistrationPage(){
        resetFrame(registrationPage);
        registrationPage.setLayout(new BorderLayout());
        JPanel panel= new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(getBackground());

        JLabel usernameLabel= new JLabel("Username");
        JLabel emailLabel= new JLabel("Email");
        JLabel phoneLabel= new JLabel("Phone");
        JLabel countryLabel= new JLabel("Country");
        JLabel streetLabel= new JLabel("Street");
        JLabel pincodeLabel= new JLabel("Pincode");
        usernameBox.setPreferredSize(textBoxDimension);
        emailBox.setPreferredSize(textBoxDimension);
        phoneBox.setPreferredSize(textBoxDimension);
        countryBox.setPreferredSize(textBoxDimension);
        streetBox.setPreferredSize(textBoxDimension);
        pincodeBox.setPreferredSize(textBoxDimension);
        usernameLabel.setFont(labelFont);
        emailLabel.setFont(labelFont);
        phoneLabel.setFont(labelFont);
        countryLabel.setFont(labelFont);
        streetLabel.setFont(labelFont);
        pincodeLabel.setFont(labelFont);
        GridBagConstraints input= new GridBagConstraints(); //note
        panel.add(usernameLabel,formatGrid(input,0,1,headingSpace,GridBagConstraints.WEST));
        panel.add(usernameBox,formatGrid(input,0,2,boxSpace,GridBagConstraints.CENTER));
        panel.add(emailLabel,formatGrid(input,0,3,headingSpace,GridBagConstraints.WEST));
        panel.add(emailBox,formatGrid(input,0,4,boxSpace,GridBagConstraints.CENTER));
        panel.add(phoneLabel,formatGrid(input,0,5,headingSpace,GridBagConstraints.WEST));
        panel.add(phoneBox,formatGrid(input,0,6,boxSpace,GridBagConstraints.CENTER));
        panel.add(countryLabel,formatGrid(input,0,7,headingSpace,GridBagConstraints.WEST));
        panel.add(countryBox,formatGrid(input,0,8,boxSpace,GridBagConstraints.CENTER));
        panel.add(streetLabel,formatGrid(input,0,9,headingSpace,GridBagConstraints.WEST));
        panel.add(streetBox,formatGrid(input,0,10,boxSpace,GridBagConstraints.CENTER));
        panel.add(pincodeLabel,formatGrid(input,0,11,headingSpace,GridBagConstraints.WEST));
        panel.add(pincodeBox,formatGrid(input,0,12,boxSpace,GridBagConstraints.CENTER));
        panel.add(submitButton,formatGrid(input,0,13,buttonSpace,GridBagConstraints.CENTER));
        submitButton.setFocusable(false);
        //fixme: improve the header code
        registrationPage.add(headPanel(AddEnum.ADD_BACK,back, null),BorderLayout.NORTH);
        registrationPage.add(panel);
        registrationPage.setVisible(true);

    }
}
