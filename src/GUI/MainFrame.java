package GUI;

import stations.BaseStation;
import stations.Channel;
import stations.MobileStation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    public static Channel mainChannel;
    JFrame frame;
    JButton mobile1Button;
    JButton mobile2Button;
    JButton mobile3Button;

    public static JLabel baseLabel;
    public static JLabel mobile1Label;
    public static JLabel mobile2Label;
    public static JLabel mobile3Label;

    MobileStation mobileStation1;
    MobileStation mobileStation2;
    MobileStation mobileStation3;

    BaseStation baseStation;

    JPanel mobile1Panel;
    JPanel mobile2Panel;
    JPanel mobile3Panel;
    JPanel basePanel;

    private void initializeFrame() {
        frame = new JFrame();
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1500, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        constructFrame();
    }

    private void addToFrame() {
        frame.add(basePanel);
        frame.add(mobile1Panel);
        frame.add(mobile2Panel);
        frame.add(mobile3Panel);
    }

    MainFrame() {

        initializeFrame();

        mobileStation1 = new MobileStation("1");
        mobileStation2 = new MobileStation("2");
        mobileStation3 = new MobileStation("3");

        baseStation = new BaseStation();

        mainChannel = new Channel(baseStation, mobileStation1, mobileStation2, mobileStation3);

        mobile1Button.addActionListener(this);
        mobile2Button.addActionListener(this);
        mobile3Button.addActionListener(this);

        startThreads();

        addToFrame();
    }

    void startThreads(){

        Thread baseThread = new Thread(baseStation);
        baseThread.start();

        Thread thread1 = new Thread(mobileStation1);
        Thread thread2 = new Thread(mobileStation2);
        Thread thread3 = new Thread(mobileStation3);

        thread1.start();
        thread2.start();
        thread3.start();
    }

    private void constructFrame() {

        baseLabel = new JLabel();
        baseLabel.setFont(new Font("Calibri", Font.BOLD, 20));
        basePanel = new JPanel();
        basePanel.setBackground(Color.GREEN);
        basePanel.setBounds(0, 0, 100, 100);
        basePanel.add(baseLabel);

        mobile1Label = new JLabel();
        mobile1Label.setFont(new Font("Calibri", Font.BOLD, 20));
        mobile1Button = new JButton("Mobile 1");
        mobile1Panel = new JPanel();
        mobile1Panel.setLayout(new GridLayout(2, 1, 0,0));
        mobile1Panel.setBackground(Color.gray);
        mobile1Panel.setBounds(0, 100, 100, 100);
        mobile1Panel.add(mobile1Label);
        mobile1Panel.add(mobile1Button);

        mobile2Label = new JLabel();
        mobile2Label.setFont(new Font("Calibri", Font.BOLD, 20));
        mobile2Button = new JButton("Mobile 2");
        mobile2Panel = new JPanel();
        mobile2Panel.setLayout(new GridLayout(2, 1, 0,0));
        mobile2Panel.setBackground(Color.CYAN);
        mobile2Panel.setBounds(0, 200, 100, 100);
        mobile2Panel.add(mobile2Label);
        mobile2Panel.add(mobile2Button);

        mobile3Label = new JLabel();
        mobile3Label.setFont(new Font("Calibri", Font.BOLD, 20));
        mobile3Button = new JButton("Mobile 3");
        mobile3Panel = new JPanel();
        mobile3Panel.setLayout(new GridLayout(2, 1, 0,0));
        mobile3Panel.setBackground(Color.ORANGE);
        mobile3Panel.setBounds(0, 300, 100, 100);
        mobile3Panel.add(mobile3Label);
        mobile3Panel.add(mobile3Button);




    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mobile1Button) {
            mobileStation1.hitButton();
        } else if (e.getSource() == mobile2Button) {
            mobileStation2.hitButton();
        } else if (e.getSource() == mobile3Button) {
            mobileStation3.hitButton();
        }
    }

    public static void main(String[] args) {
        MainFrame mobileStations = new MainFrame();
    }
}
