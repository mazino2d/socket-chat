package client;

import protocol.Encode;
import protocol.Tags;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import java.util.regex.Pattern;
import java.util.Random;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginGUI {

    private static String WELCOME_MESSAGE = "CONNECT WITH SERVER\n";
    private static String ASWR_IP_MESSAGE = "IP Server : ";
    private static String ASWR_PORT_MESSAGE = "Port Server : ";
    private static String ASWR_NAME_MESSAGE = "User Name: ";
    private static String LOGIN_BTN_MESSAGE = "Login";

    private static String NAME_FAILED = "CONNECT WITH OTHER NAME";
	private static String NAME_EXSIST = "NAME IS EXSISED";
    private static String SERVER_NOT_START = "SERVER NOT START";
    
    private static String DEFAULT_SERVER_PORT = "8080";
    
    private JFrame frame;
    private JLabel lbError;
    private JTextField textIP, textPort, textName;
    private JButton btnLogin, btnClear;

    private Pattern checkName = Pattern.compile("[a-zA-Z][^<>]*");
    private String name = "", IP = "";

    public LoginGUI() {
        initializeFrame();
        initializeLabel();
        initializeTextBox();
        initializeButton();
    }
    
    private void initializeFrame() {
        frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 448, 204);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
    }

    private void initializeLabel() {
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

        lbError = new JLabel("");
		lbError.setBounds(120, 131, 380, 14);
		frame.getContentPane().add(lbError);
    }

    private void initializeTextBox() {
        textPort = new JTextField();
		textPort.setText(DEFAULT_SERVER_PORT);
		textPort.setBounds(356, 50, 65, 20);
        frame.getContentPane().add(textPort);
        
        textIP = new JTextField();
        textIP.setBounds(101, 46, 152, 28);
        frame.getContentPane().add(textIP);

        textName = new JTextField();
		textName.setBounds(101, 77, 152, 30);
        frame.getContentPane().add(textName);
    }

    private void initializeButton() {
        
        btnLogin = new JButton(LOGIN_BTN_MESSAGE);
        btnLogin.setBounds(263, 78, 169, 29);
        frame.getContentPane().add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                name = textName.getText();
                IP = textIP.getText();
                lbError.setVisible(false);

                if(checkName.matcher(name).matches()){
                    try {
                        InetAddress serverIP = InetAddress.getByName(IP);
                        int serverPort = Integer.parseInt(textPort.getText());
                        Socket clientSocket = new Socket(serverIP, serverPort);

                        Random random = new Random();
                        int peerPort = 10000 + random.nextInt() % 10000;
                        
                        String message = Encode.getAccount(name, Integer.toString(peerPort));

                        ObjectOutputStream  sender = new ObjectOutputStream(clientSocket.getOutputStream());
                        sender.writeObject(message); sender.flush();
                        ObjectInputStream listener = new ObjectInputStream(clientSocket.getInputStream());
                        message = (String) listener.readObject();

                        clientSocket.close();

                        if (message.equals(Tags.SESSION_DENY_TAG)) {
							lbError.setText(NAME_EXSIST);
							lbError.setVisible(true);
							return;
						}

                        new ClientGUI(IP, peerPort, name, message);
						frame.dispose();
                    } catch (Exception e) {
                        lbError.setText(SERVER_NOT_START);
						lbError.setVisible(true);
						e.printStackTrace();
                    }
                } else {
                    lbError.setText(NAME_FAILED);
					lbError.setVisible(true);
					lbError.setText(NAME_FAILED);
                }
            }
        
        });

        btnClear = new JButton("Clear");
        btnClear.setBounds(6, 120, 100, 29);
        frame.getContentPane().add(btnClear);
        
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textIP.setText("");
                textName.setText("");
                lbError.setVisible(false);
			}
        });
    }

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
}
