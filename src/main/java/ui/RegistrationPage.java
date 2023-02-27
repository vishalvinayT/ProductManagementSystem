package ui;

import dbtables.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class RegistrationPage extends ScreenStage {
    private JFrame registrationPage= new JFrame();
    private JButton submitButton= new JButton("Submit");
    private JTextField usernameBox= new JTextField();
    private JTextField emailBox=new JTextField();
    private JTextField phoneBox=new JTextField();
    private JTextField countryBox=new JTextField();
    private JTextField streetBox=new JTextField();
    private JTextField pincodeBox=new JTextField();


    @Override
    public void init(){
        setRegistrationPage();
        submitButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userName=checkField(usernameBox.getText(),"Username");
                String email=checkField(emailBox.getText(),"Email");
                String phone= checkField(phoneBox.getText(),"Phone");
                String country=checkField(countryBox.getText(),"Country");
                String street= checkField(streetBox.getText(),"Street");
                String pincode=checkField(pincodeBox.getText(),"Pincode");
                if(userName!=null && email!=null && phone!=null && country!=null && street!=null && pincode!=null){
                    try {
                        User checkUser=processor.checkUser(email);
                        if(checkUser!=null){
                            JOptionPane.showMessageDialog(null,"User exists in the database","Please Login",JOptionPane.WARNING_MESSAGE);
                        }else{
                            User registeredUser= new User();
                            registeredUser.name=userName;
                            registeredUser.email=email;
                            registeredUser.phone=phone;
                            registeredUser.country=country;
                            registeredUser.street=street;
                            registeredUser.pincode=pincode;
                            boolean added=processor.addUser(registeredUser);
                            if(added){
                                JOptionPane.showMessageDialog(null,"Thank you for Registration","Successful",JOptionPane.INFORMATION_MESSAGE);
                                registrationPage.dispose();
                                ShopPage productsPage= new ShopPage();
                                productsPage.init();
                            }

                        }

                    }catch (NullPointerException | SQLException ns){
                        JOptionPane.showMessageDialog(null,"Something Went Wrong","Error 404",JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });

        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrationPage.dispose();
                EntryPage page=new EntryPage();
                page.init();
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

    private String checkField(String name, String field){
        if(name==null || name.isEmpty() || name.isBlank()){
            JOptionPane.showMessageDialog(null,String.format("Please Enter a Valid %s",field),"Field Error",JOptionPane.ERROR_MESSAGE);
            return null;
        }else{
            boolean match;
            switch (field.toUpperCase()){
                case "EMAIL":
                    match=name.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
                    return match? name:null;
                case "PHONE":
                    match=name.matches("(\\+49)?( )?[0-9]{10,}");
                    return match? name:null;
                case "COUNTRY":
                    match=name.matches("[a-zA-Z]+");
                    return match? name:null;
                case "PINCODE":
                    match=name.matches("[0-9]{5}");
                    return match? name:null;
                default:
                    return name;

            }
        }
    }


}
