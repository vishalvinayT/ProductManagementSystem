package ui;

import javax.swing.*;
import java.awt.*;

public class FinalPage extends  ScreenStage{

    private JFrame finalFrame= new JFrame();
    @Override
    public void init() {
        setFinalPage();
        finalFrame.setVisible(true);

    }

    private void setFinalPage(){
        resetFrame(finalFrame);
        finalFrame.setLayout(new BorderLayout());
        finalFrame.add(headPanel(AddEnum.ADD_HEADING,null,null),BorderLayout.NORTH);
        JPanel finalPanel= new JPanel();
        finalPanel.setBackground(getBackground());
        finalPanel.setLayout(new BoxLayout(finalPanel,BoxLayout.X_AXIS));
        ImageIcon successIcon= new ImageIcon("./icons/success.png");
        Image successImg= successIcon.getImage().getScaledInstance(successIcon.getIconWidth()/4,successIcon.getIconHeight()/4,Image.SCALE_SMOOTH);
        JLabel successLabel= new JLabel("Thank you for using the Application");
        JLabel successImgLabel=new JLabel(new ImageIcon(successImg));
        successLabel.setHorizontalAlignment(SwingConstants.CENTER);
        successImgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalPanel.add(successImgLabel);
        finalPanel.add(successLabel);
        successLabel.setFont(subheadingFont);
        finalFrame.add(finalPanel,BorderLayout.CENTER);
    }
}
