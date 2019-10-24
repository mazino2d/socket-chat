package client;

import java.awt.EventQueue;
import java.util.Random;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import client.MenuGUI;
import protocol.Encode;
import protocol.Tags;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class LoginGUI {

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
		fmLogin.setResizable(false);
		fmLogin.setBounds(100, 100, 448, 204);
		fmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fmLogin.getContentPane().setLayout(null);
	}
	
	private void initializeLabel() {
		JLabel lbWelcome = new JLabel("Connect With Server\r\n");
		lbWelcome.setBounds(10, 11, 258, 14);
		fmLogin.getContentPane().add(lbWelcome);

		JLabel lbIP = new JLabel("IP Server : ");
		lbIP.setBounds(10, 50, 86, 20);
		fmLogin.getContentPane().add(lbIP);

		JLabel lbPort = new JLabel("Port Server : ");
		lbPort.setBounds(263, 53, 79, 14);
		fmLogin.getContentPane().add(lbPort);

		JLabel lbUsername = new JLabel("User Name: ");
		lbUsername.setBounds(10, 82, 86, 17);
		fmLogin.getContentPane().add(lbUsername);

		lbError = new JLabel("");
		lbError.setBounds(120, 141, 380, 14);
		fmLogin.getContentPane().add(lbError);
	}
	
	private void initializeTextBox() {
		txtIP = new JTextField();
		txtIP.setBounds(101, 46, 152, 28);
		fmLogin.getContentPane().add(txtIP);
		txtIP.setColumns(10);

		txtPort = new JTextField();
		txtPort.setText("8080");
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(356, 50, 65, 20);
		fmLogin.getContentPane().add(txtPort);

		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		txtUsername.setBounds(101, 77, 152, 30);
		fmLogin.getContentPane().add(txtUsername);
	}
	
	private void initializeButton() {
		JButton btnlogin = new JButton("login");
		btnlogin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String IP = txtIP.getText();
				String name = txtUsername.getText();
				Pattern checkName = Pattern.compile("[a-zA-Z][^<>]*");
				lbError.setVisible(false);
				if (checkName.matcher(name).matches() && !IP.equals("")) {
					try {
						Random rd = new Random();
						int portPeer = 10000 + rd.nextInt() % 1000;
						InetAddress ipServer = InetAddress.getByName(IP);
						int portServer = Integer.parseInt("8080");
						Socket socketClient = new Socket(ipServer, portServer);
						String msg = Encode.genAccountRequest(name,Integer.toString(portPeer));
						ObjectOutputStream serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
						serverOutputStream.writeObject(msg);
						serverOutputStream.flush();
						ObjectInputStream serverInputStream = new ObjectInputStream(socketClient.getInputStream());
						msg = (String) serverInputStream.readObject();
						socketClient.close();
						if (msg.equals(Tags.SESSION_DENY_TAG)) {
							lbError.setText(NAME_EXSIST);
							lbError.setVisible(true);
							return;
						}
						new MenuGUI(IP, portPeer, name, msg);
						fmLogin.dispose();
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
		btnlogin.setBounds(263, 78, 169, 29);
		fmLogin.getContentPane().add(btnlogin);
		lbError.setVisible(false);
		
		JButton btnclear = new JButton("Clear");
		btnclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				txtIP.setText("");
				txtUsername.setText("");
			}
		});
		btnclear.setBounds(6, 120, 100, 29);
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

