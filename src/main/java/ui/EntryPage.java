package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EntryPage extends ScreenStage{

    private JButton submitButton =new JButton("Login");
    private JButton registerButton =new JButton("Register");

    private JTextField username=new JTextField();
    private JFrame entryFrame= new JFrame();
    public EntryPage(){
        setStage(entryFrame);
    }

    @Override
    public void init(){
        setEntryPage();
    }

    private void setEntryPage(){
        entryFrame.setLayout(new BorderLayout());
        Insets headingSpace= new Insets(5,20,5,20);
        Insets textSpace = new Insets(5,20,15,20);
        Insets buttonSpace=new Insets(5,0,5,0);
        String loginText="Login";
        JPanel panel=new JPanel();
        JLabel label= new JLabel(loginText);
        label.setFont(new Font("Serif-Fonts",Font.ITALIC,24));
        JLabel usernameText= new JLabel("Username");
        panel.setLayout(new GridBagLayout());
        //label.setPreferredSize(new Dimension(100,50));
        usernameText.setFont(new Font("Serif-Fonts", Font.ITALIC,14));
        username.setPreferredSize(new Dimension(300,30));


        GridBagConstraints  input=new GridBagConstraints();
        input.anchor=GridBagConstraints.CENTER;
        input.gridy=1;
        input.insets=headingSpace;
        panel.add(label,input);

        input.gridy=2;
        input.anchor=GridBagConstraints.WEST;
        input.insets=headingSpace;
        panel.add(usernameText, input);

        input.gridy=3;
        input.anchor=GridBagConstraints.CENTER;
        input.insets= textSpace;
        panel.add(username, input);

        input.gridy=4;
        input.gridx=0;
        input.anchor=GridBagConstraints.CENTER;
        input.insets=buttonSpace;
        panel.add(submitButton, input);

        input.gridy=5;
        input.gridx=0;
        input.anchor=GridBagConstraints.CENTER;
        input.insets=buttonSpace;
        panel.setBackground(getBackground());
        panel.add(registerButton, input);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegistrationPage registrationPage=new RegistrationPage();
                registrationPage.init();
            }
        });

        registerButton.setFocusable(false);
        submitButton.setFocusable(false);



        entryFrame.add(panel, BorderLayout.CENTER);
        entryFrame.setVisible(true);
    }

}
