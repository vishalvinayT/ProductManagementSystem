package ui;

import dbtables.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class EntryPage extends ScreenStage{

    private JButton loginButton =new JButton("Login");
    private JButton registerButton =new JButton("Register");

    private JTextField username=new JTextField();
    private JFrame entryFrame= new JFrame();
    public EntryPage(){
        setStage(entryFrame);
    }

    @Override
    public void init(){
        setEntryPage();
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));


        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user=username.getText();
                try {
                    User entryUser=processor.checkUser(user);
                    if(entryUser!=null){
                        UI.user=entryUser;
                        entryFrame.dispose();
                        ShopPage shopPage=new ShopPage();
                        shopPage.init();
                    }else{
                        JOptionPane.showMessageDialog(null,"PLease Register","User Not Exists",JOptionPane.ERROR_MESSAGE);
                    }
                }catch (NullPointerException ne){
                    JOptionPane.showMessageDialog(null,"Please Enter a Valid Username or Email","Invalid Login",JOptionPane.ERROR_MESSAGE);
                }catch (SQLException se){
                    entryFrame.add(errorPanel());
                }

            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                entryFrame.dispose();
                RegistrationPage registrationPage=new RegistrationPage();
                registrationPage.init();
            }
        });
    }


    private void setEntryPage(){
        entryFrame.setLayout(new BorderLayout());

        String loginText="Login";
        JPanel panel=new JPanel();
        JLabel label= new JLabel(loginText);
        label.setFont(subheadingFont);
        JLabel usernameText= new JLabel("Username");
        panel.setLayout(new GridBagLayout());
        //label.setPreferredSize(new Dimension(100,50));
        usernameText.setFont(labelFont);
        username.setPreferredSize(textBoxDimension);
        //note this
        GridBagConstraints  input=new GridBagConstraints();
        panel.add(label,formatGrid(input,0,1,headingSpace,GridBagConstraints.CENTER));
        panel.add(usernameText,formatGrid(input,0,2,headingSpace,GridBagConstraints.WEST));
        panel.add(username,formatGrid(input,0,3,boxSpace,GridBagConstraints.CENTER));
        panel.add(loginButton,formatGrid(input,0,4,buttonSpace,GridBagConstraints.CENTER));
        panel.add(registerButton,formatGrid(input,0,5,buttonSpace,GridBagConstraints.CENTER));
        panel.setBackground(getBackground());
        registerButton.setFocusable(false);
        loginButton.setFocusable(false);

        //fixme: improve the header code
        entryFrame.add(headPanel(AddEnum.ADD_HEADING,null,null),BorderLayout.NORTH);
        entryFrame.add(panel, BorderLayout.CENTER);
        entryFrame.setVisible(true);
    }

}
