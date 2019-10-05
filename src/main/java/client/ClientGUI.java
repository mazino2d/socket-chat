package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ClientGUI {
    
    private JFrame frame;
    private JButton btnChat, btnExit;
    private JTextField textYourName, textFriendName;
	private TextArea textList;
	
	private static String clientIP = "";
	private static String userName = "";
	private static String userData = "";
	private static int clientPort = 0;

    public ClientGUI() {
        initializeFrame();
        initializeLabel();
        initializeTextBox();
        initializeButton();
	}
	
	public ClientGUI(String ip, int port, String name, String message) throws Exception {
		clientIP = ip;
		clientPort = port;
		userName = name;
		userData = message;
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

    private void initializeFrame() {
        frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 330, 556);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
    }

    private void initializeLabel() {
        JLabel label = new JLabel("User Name: ");
		label.setBounds(10, 17, 90, 16);
        frame.getContentPane().add(label);
        
        JLabel lblFriendsName = new JLabel("Friend Name: ");
		lblFriendsName.setBounds(10, 445, 110, 16);
		frame.getContentPane().add(lblFriendsName);
    }

    private void initializeTextBox() {
        textYourName = new JTextField("user name");
		textYourName.setEditable(false);
		textYourName.setColumns(10);
		textYourName.setBounds(90, 11, 210, 28);
        frame.getContentPane().add(textYourName);
        
        textFriendName = new JTextField("friend name");
		textFriendName.setColumns(10);
		textFriendName.setBounds(105, 439, 205, 28);
        frame.getContentPane().add(textFriendName);
        
        textList = new TextArea();
		textList.setText("");
		textList.setEditable(false);
		textList.setBounds(10, 53, 310, 372);
		frame.getContentPane().add(textList);
    }

    private void initializeButton() {
        btnChat = new JButton("Chat");
        btnChat.setBounds(10, 478, 113, 29);
		frame.getContentPane().add(btnChat);

		// btnChat.addActionListener(new ActionListener() {

		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
        // });
        
        btnExit = new JButton("Exit");
        btnExit.setBounds(190, 478, 113, 29);
        frame.getContentPane().add(btnExit);
        
		// btnExit.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
        //         ;
		// 	}
		// });
		
    }

    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI window = new ClientGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
