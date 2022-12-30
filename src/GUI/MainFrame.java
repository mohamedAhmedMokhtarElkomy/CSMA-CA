package GUI;

import stations.BaseStation;
import stations.MobileStation;
import stations.Station;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    int count = 0;// store number of clicks

    JButton button1;
    JButton button2;
    JButton button3;

    public static JLabel baseLabel;
    public static JLabel mobile1Label;
    public static JLabel mobile2Label;
    public static JLabel mobile3Label;

    MobileStation mobileStation1;
    MobileStation mobileStation2;
    MobileStation mobileStation3;

    BaseStation baseStation;
    MainFrame(){

        JFrame frame = new JFrame();
        button1 = new JButton("mobile 1");
        button2 = new JButton("Mobile 2");
        button3 = new JButton("Mobile 3");
        baseLabel = new JLabel();
        mobile1Label = new JLabel();
        mobile2Label = new JLabel();
        mobile3Label = new JLabel();




        // add text to label


        mobileStation1 = new MobileStation("1", baseStation);
//        mobileStation2 = new MobileStation("2");
//        mobileStation3 = new MobileStation("3");

        baseStation = new BaseStation(mobileStation1);
        Thread baseThread = new Thread(baseStation);
        baseThread.start();

        mobileStation1.setBaseStation(baseStation);

        Thread thread1 = new Thread(mobileStation1);
//        Thread thread2 = new Thread(mobileStation2);
//        Thread thread3 = new Thread(mobileStation3);

        thread1.start();
//        thread2.start();
//        thread3.start();



        button1.addActionListener(this);
        mobile1Label.setText("mobile1");
        button2.addActionListener(this);
        mobile2Label.setText("mobile2");
        button3.addActionListener(this);
        mobile3Label.setText("mobile3");

        baseLabel.setText("base");

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        frame.add(mobile1Label);
        frame.add(button1);

        frame.add(mobile2Label);
        frame.add(button2);

        frame.add(mobile3Label);
        frame.add(button3);

        frame.add(baseLabel);


        frame.getRootPane().setDefaultButton(button1); // sets default button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450,450);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() ==  button1){
            mobileStation1.hitButton();
        }
        else if(e.getSource() ==  button2){
            mobileStation2.hitButton();
        }
        else if(e.getSource() ==  button3){
            mobileStation3.hitButton();
        }
    }

    public static void main(String[] args){
        MainFrame mobileStations = new MainFrame();
    }
}
