package client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import javax.swing.JButton;

import protocol.Tags;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class MenuGUI {

	static {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel());
            UIManager.put("Button.mouseHoverEnable", true);
            JFrame.setDefaultLookAndFeelDecorated(false);
            
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
	}

	private Menu clientNode;
	private static String client_ip = "", username = "", message = "";
	private static int client_port = 0;
	
	private JFrame fmMenu;
	private JTextField txtUserame, txtFriendName;
	private static JTextArea txtPeerList;
	private JButton btnChat, btnExit;

	public MenuGUI(String ip, int port, String name, String msg) throws Exception {
		client_ip = ip;
		client_port = port;
		username = name;
		message = msg;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuGUI window = new MenuGUI();
					window.fmMenu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public MenuGUI() throws Exception {
		initializeFrame();
		initializeLabel();
		initializeTextBox();
		initializeButton();
		clientNode = new Menu(client_ip, client_port, username, message);
	}

	private void initializeFrame() {
		fmMenu = new JFrame();
		fmMenu.setResizable(false);
		fmMenu.setBounds(100, 100, 330, 540);
		fmMenu.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		fmMenu.getContentPane().setLayout(null);
	}

	private void initializeLabel() {
		JLabel lbUsername = new JLabel("Username: ");
		lbUsername.setBounds(10, 17, 100, 16);
		fmMenu.getContentPane().add(lbUsername);

		JLabel lbFirendName = new JLabel("Friend Name: ");
		lbFirendName.setBounds(10, 445, 110, 16);
		fmMenu.getContentPane().add(lbFirendName);
	}

	private void initializeTextBox() {
		txtUserame = new JTextField(username);
		txtUserame.setEditable(false);
		txtUserame.setColumns(10);
		txtUserame.setBounds(110, 11, 210, 28);
		fmMenu.getContentPane().add(txtUserame);

		txtPeerList = new JTextArea();
		txtPeerList.setText("");
		txtPeerList.setEditable(false);
		txtPeerList.setBounds(10, 53, 310, 372);
		fmMenu.getContentPane().add(txtPeerList);

		txtFriendName = new JTextField("");
		txtFriendName.setColumns(10);
		txtFriendName.setBounds(125, 439, 192, 28);
		fmMenu.getContentPane().add(txtFriendName);
	}

	private void initializeButton() {
		btnChat = new JButton("Chat");
		btnChat.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				String name = txtFriendName.getText();
				if (name.equals("") || Menu.client == null) {
					JOptionPane.showMessageDialog(fmMenu, "Name 's friend mistake!");
					return;
				}
				if (name.equals(username)) {
					JOptionPane.showMessageDialog(fmMenu, "You can't chat with yourself !");
					return;
				}
				int size = Menu.client.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Menu.client.get(i).getName())) {
						try {
							clientNode.requestChat(Menu.client.get(i).getHost(),Menu.client.get(i).getPort(), name);
							return;
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				JOptionPane.showMessageDialog(fmMenu, "Can't found your friend!");
			}
		});
		btnChat.setBounds(10, 478, 113, 29);
		fmMenu.getContentPane().add(btnChat);

		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(
					fmMenu, "Do you want exit now?", null, 
					JOptionPane.YES_NO_OPTION
				);
				if (result == 0) {
					try {
						clientNode.exit();
						fmMenu.dispose();
					} catch (Exception e) {
						fmMenu.dispose();
					}
				}
			}
		});
		btnExit.setBounds(200, 478, 113, 29);
		fmMenu.getContentPane().add(btnExit);
	}
		
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MenuGUI window = new MenuGUI();
					window.fmMenu.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static int request(String msg, boolean type) {
		JFrame frameMessage = new JFrame();
		if(type)
			return JOptionPane.showConfirmDialog(
				frameMessage, msg, null, 
				JOptionPane.YES_NO_OPTION
			);
		else
			JOptionPane.showMessageDialog(frameMessage, msg);
			return Tags.IN_VALID;

	}

	public static void updateFiend(String msg) {
		txtPeerList.append(msg + "\n");
	}

	public static void clearAll() {
		txtPeerList.setText("");
		txtPeerList.setText("");
	}
}
