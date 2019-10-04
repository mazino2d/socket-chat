package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;


public class LoginGUI {

    private static String WELCOME_MESSAGE = "CONNECT WITH SERVER\n";
    private static String ASWR_IP_MESSAGE = "IP Server : ";
    private static String ASWR_PORT_MESSAGE = "Port Server : ";
    private static String ASWR_NAME_MESSAGE = "User Name: ";

    private static String LOGIN_BTN_MESSAGE = "Login";
    
    private JFrame frame;

    private JTextField textIP;
    private JTextField textPort;
    private JTextField textName;

    private JButton btnLogin;
    private JButton btnClear;

    private JLabel lbError;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
    }

    public LoginGUI() {
		initialize();
    }
    
    private void initialize() {

        frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 448, 204);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        
        JLabel lbWelcome = new JLabel(WELCOME_MESSAGE);
		lbWelcome.setBounds(10, 11, 258, 14);
        frame.getContentPane().add(lbWelcome);
        
        JLabel lbAnswerIP = new JLabel(ASWR_IP_MESSAGE);
		lbAnswerIP.setBounds(10, 50, 86, 20);
        frame.getContentPane().add(lbAnswerIP);
        
        JLabel lbAnswerPort = new JLabel(ASWR_PORT_MESSAGE);
		lbAnswerPort.setBounds(263, 53, 95, 14);
        frame.getContentPane().add(lbAnswerPort);

        JLabel lbAnswerName = new JLabel(ASWR_NAME_MESSAGE);
		lbAnswerName.setBounds(10, 82, 86, 17);
        frame.getContentPane().add(lbAnswerName);

        lbError = new JLabel("Erorr message");
		lbError.setBounds(120, 141, 380, 14);
		frame.getContentPane().add(lbError);
        
        textPort = new JTextField();
		textPort.setText("8080");
		textPort.setBounds(356, 50, 65, 20);
        frame.getContentPane().add(textPort);
        
        textIP = new JTextField();
        textIP.setBounds(101, 46, 152, 28);
        frame.getContentPane().add(textIP);

        textName = new JTextField();
		textName.setBounds(101, 77, 152, 30);
        frame.getContentPane().add(textName);
        
        btnLogin = new JButton(LOGIN_BTN_MESSAGE);

        // btnLogin.addActionListener(new ActionListener() {

        //     public void actionPerformed(ActionEvent arg0) {;}
        
        // });

        btnLogin.setBounds(263, 78, 169, 29);
        frame.getContentPane().add(btnLogin);

        btnClear = new JButton("Clear");
        
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textIP.setText("");
				textName.setText("");
			}
        });

        btnClear.setBounds(6, 120, 100, 29);
		frame.getContentPane().add(btnClear);
    }

}