package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.TextArea;

public class ServerGUI {

	public static int port = 8080;
	private JFrame fmServer;
	private JTextField txtIP, txtPort;
	private static TextArea txtMessage;
	private static JLabel lbNumber;
	Server server;

	public ServerGUI() {
		initializeFrame();
		initializeLabel();
		initializeTextBox();
		initializeButton();
	}

	private void initializeFrame() {
		fmServer = new JFrame();
		fmServer.setResizable(false);
		fmServer.setBounds(200, 200, 622, 442);
		fmServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fmServer.getContentPane().setLayout(null);
	}

	private void initializeLabel() {
		JLabel lblIP = new JLabel("IP ADDRESS :");
		lblIP.setBounds(36, 55, 76, 16);
		fmServer.getContentPane().add(lblIP);

		JLabel lblNewLabel = new JLabel("PORT : ");
		lblNewLabel.setBounds(315, 55, 61, 16);
		fmServer.getContentPane().add(lblNewLabel);

		JLabel lblNhom = new JLabel("Kraken Team");
		lblNhom.setBounds(290, 6, 109, 16);
		fmServer.getContentPane().add(lblNhom);
	}

	private void initializeTextBox() {
		txtIP = new JTextField();
		txtIP.setEditable(false);
		txtIP.setBounds(120, 49, 176, 28);
		fmServer.getContentPane().add(txtIP);
		txtIP.setColumns(10);
		try {
			txtIP.setText(Inet4Address.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		txtPort = new JTextField();
		txtPort.setEditable(false);
		txtPort.setColumns(10);
		txtPort.setBounds(366, 49, 208, 28);
		fmServer.getContentPane().add(txtPort);
		txtPort.setText("8080");

		txtMessage = new TextArea();
		txtMessage.setEditable(false);
		txtMessage.setBounds(6, 130, 602, 270);
		fmServer.getContentPane().add(txtMessage);
	}

	private void initializeButton() {
		JButton btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server = new Server(8080);
					ServerGUI.updateMessage("START SERVER");
				} catch (Exception e) {
					ServerGUI.updateMessage("START ERROR");
					e.printStackTrace();
				}
			}
		});
		btnStart.setBounds(36, 90, 260, 29);
		fmServer.getContentPane().add(btnStart);

		JButton btnStop = new JButton("STOP");
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					server.stop();
					ServerGUI.updateMessage("STOP SERVER");
				} catch (Exception e) {
					e.printStackTrace();
					ServerGUI.updateMessage("STOP SERVER");
				}
			}
		});
		btnStop.setBounds(315, 90, 260, 29);
		fmServer.getContentPane().add(btnStop);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.fmServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void updateMessage(String msg) {
		txtMessage.append(msg + "\n");
	}

	public static void updateNumberClient() {
		int number = Integer.parseInt(lbNumber.getText());
		lbNumber.setText(Integer.toString(number + 1));
	}
}
