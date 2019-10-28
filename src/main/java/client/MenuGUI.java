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
import javax.swing.ImageIcon;
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

	private static String URL_DIR = System.getProperty("user.dir");

	private Menu client_node;
	private String server_ip = "";
	private int server_port = 8080;
	
	private int peer_port = 0;
	private String username = "";
	private String message = "";
	
	private JFrame fmMenu;
	private JTextField txtUsername, txtFriendName;
	private static JTextArea txtPeerList;
	private JButton btnChat, btnExit;

	public MenuGUI(String server_ip, int server_port, int peer_port, String username, String message) throws Exception {
		this.server_ip = server_ip;
		this.server_port = server_port;
		this.peer_port = peer_port;
		this.username = username;
		this.message = message;

		initializeFrame();
		initializeLabel();
		initializeTextBox();
		initializeButton();

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					fmMenu.setVisible(true);
					client_node = new Menu(server_ip, server_port, peer_port, username, message);
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
		client_node = new Menu(server_ip, server_port, peer_port, username, message);
	}

	private void initializeFrame() {
		fmMenu = new JFrame();
		fmMenu.setTitle("Menu");
		ImageIcon image = new ImageIcon(URL_DIR + "/src/main/resources/menu_icon.png");
		fmMenu.setIconImage(image.getImage());
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
		txtUsername = new JTextField(this.username);
		txtUsername.setEditable(false);
		txtUsername.setColumns(10);
		txtUsername.setBounds(110, 11, 210, 28);
		fmMenu.getContentPane().add(txtUsername);

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
				if (name.equals("") || Menu.friend_list == null) {
					JOptionPane.showMessageDialog(fmMenu, "Name 's friend mistake!");
					return;
				}
				if (name.equals(username)) {
					JOptionPane.showMessageDialog(fmMenu, "You can't chat with yourself !");
					return;
				}
				int size = Menu.friend_list.size();
				for (int i = 0; i < size; i++) {
					if (name.equals(Menu.friend_list.get(i).getName())) {
						try {
							client_node.requestChat(Menu.friend_list.get(i).getHost(),Menu.friend_list.get(i).getPort(), name);
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
						client_node.requestExit();
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

	public static int showDialog(String msg, boolean type) {
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

	public static void updateFiendList(String msg) {
		txtPeerList.append(msg + "\n");
	}

	public static void clearFriendList() {
		txtPeerList.setText("");
		txtPeerList.setText("");
	}
}
