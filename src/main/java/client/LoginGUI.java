package client;

import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import client.MenuGUI;
import mdlaf.MaterialLookAndFeel;
import protocol.Encode;
import protocol.Tags;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginGUI {

	static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            UIManager.put("Button.mouseHoverEnable", true);
            JFrame.setDefaultLookAndFeelDecorated(false);
            
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
	}

	private static String URL_DIR = System.getProperty("user.dir");

	private static String NAME_FAILED = "CONNECT WITH OTHER NAME";
	private static String NAME_EXSIST = "NAME IS EXSISED";
	private static String SERVER_NOT_START = "SERVER NOT START";

	private JFrame fmLogin;
	private JLabel lbError;
	private JTextField txtIP, txtPort, txtUsername;

	public LoginGUI() {
		initializeFrame();
		initializeLabel();
		initializeTextBox();
		initializeButton();
	}

	private void initializeFrame() {
		fmLogin = new JFrame();
		fmLogin.setTitle("Login");
		ImageIcon image = new ImageIcon(URL_DIR + "/src/main/resources/login_icon.png");
		fmLogin.setIconImage(image.getImage());
		fmLogin.setResizable(false);
		fmLogin.setBounds(500, 200, 448, 150);
		fmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fmLogin.getContentPane().setLayout(null);
	}
	
	private void initializeLabel() {
		JLabel lbWelcome = new JLabel("Connect With Server\r\n");
		lbWelcome.setBounds(10, 11, 140, 14);
		fmLogin.getContentPane().add(lbWelcome);

		JLabel lbIP = new JLabel("IP : ");
		lbIP.setBounds(10, 50, 60, 20);
		fmLogin.getContentPane().add(lbIP);

		JLabel lbPort = new JLabel("Port : ");
		lbPort.setBounds(263, 50, 60, 20);
		fmLogin.getContentPane().add(lbPort);

		JLabel lbUsername = new JLabel("Name : ");
		lbUsername.setBounds(10, 82, 60, 20);
		fmLogin.getContentPane().add(lbUsername);

		lbError = new JLabel("CONNECT WITH OTHER NAME");
		lbError.setBounds(200, 11, 380, 14);
		fmLogin.getContentPane().add(lbError);
	}
	
	private void initializeTextBox() {
		txtIP = new JTextField();
		txtIP.setColumns(10);
		txtIP.setText("localhost");
		txtIP.setBounds(91, 46, 152, 30);
		fmLogin.getContentPane().add(txtIP);

		txtPort = new JTextField();
		txtPort.setColumns(10);
		txtPort.setText("8080");
		txtPort.setBounds(340, 46, 100, 30);
		fmLogin.getContentPane().add(txtPort);

		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		txtUsername.setText("noname");
		txtUsername.setBounds(91, 77, 152, 30);
		fmLogin.getContentPane().add(txtUsername);
	}
	
	private void initializeButton() {
		JButton btnlogin = new JButton("Login");
		btnlogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String server_ip = txtIP.getText();
				int server_port = Integer.parseInt(txtPort.getText());
				String username = txtUsername.getText();
				Pattern checkName = Pattern.compile("[a-zA-Z][^<>]*");
				lbError.setVisible(false);
				
				if (checkName.matcher(username).matches() && !server_ip.equals("")) {
					try {
						// Generate a random number for peer port
						Random rd = new Random();
						int peer_port = 10000 + rd.nextInt() % 1000;
						// Open a socket to connect with the server
						InetAddress server_ip_addr = InetAddress.getByName(server_ip);
						Socket socketClient = new Socket(server_ip_addr, server_port);
						// Encode message (user-defined protocol)
						String message = Encode.genAccountRequest(username,Integer.toString(peer_port));
						// Send message to the server
						ObjectOutputStream sender = new ObjectOutputStream(socketClient.getOutputStream());
						sender.writeObject(message); sender.flush();
						// Get acknowledgment from the server
						ObjectInputStream listener = new ObjectInputStream(socketClient.getInputStream());
						message = (String) listener.readObject();
						// Close socket
						socketClient.close();

						if (message.equals(Tags.SESSION_DENY_TAG)) {
							lbError.setText(NAME_EXSIST);
							lbError.setVisible(true);
							return;
						}
						new MenuGUI(server_ip, server_port, peer_port, username, message);
						fmLogin.dispose();
					} catch (Exception e) {
						lbError.setText(SERVER_NOT_START);
						lbError.setVisible(true);
						e.printStackTrace();
					}
				} else {
					lbError.setText(NAME_FAILED);
					lbError.setVisible(true);
				}
			}
		});
		btnlogin.setBounds(250, 78, 90, 29);
		fmLogin.getContentPane().add(btnlogin);
		lbError.setVisible(false);
		
		JButton btnclear = new JButton("Clear");
		btnclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtIP.setText("");
				txtPort.setText("");
				txtUsername.setText("");
				lbError.setText("");
			}
		});
		btnclear.setBounds(350, 78, 90, 29);
		fmLogin.getContentPane().add(btnclear);
		lbError.setVisible(false);
	}		

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI window = new LoginGUI();
					window.fmLogin.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}

