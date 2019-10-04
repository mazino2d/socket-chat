package client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;
import java.awt.Label;


import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.BorderFactory;

public class ChatGUI {
    private JFrame frame;
    private JPanel panelFile, panelMessage;
    private JTextField textName, textPath, textSend;
    private TextArea textDisplayChat;
    private JButton btnDisconnect, btnSend, btnChoose, btnDel, btnUpLoad;

    public ChatGUI() {
        initializeFrame();
        initializeChatBox();
        initalizeMessagePanel();
        initalizeFilePanel();
    }

    private void initializeFrame() {
        frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(200, 200, 688, 559);
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void initializeChatBox() {

        JLabel lblClientIP = new JLabel("Chatting with: ");
		lblClientIP.setBounds(6, 12, 155, 16);
        frame.getContentPane().add(lblClientIP);

        textName = new JTextField("nameUser");
		textName.setEditable(false);
		textName.setBounds(110, 6, 450, 28);
		frame.getContentPane().add(textName);
		textName.setText("nameGuest");
        textName.setColumns(10);
        
        textDisplayChat = new TextArea();
		textDisplayChat.setEditable(false);
		textDisplayChat.setBounds(6, 40, 668, 317);
        frame.getContentPane().add(textDisplayChat);
        
        btnDisconnect = new JButton("DISCONNECT");
        btnDisconnect.setBounds(560, 6, 113, 29);
        frame.getContentPane().add(btnDisconnect);
        
		// btnDisconnect.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
		// });
    }


    private void initalizeMessagePanel() {
        panelMessage = new JPanel();
		panelMessage.setBounds(6, 420, 670, 71);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		frame.getContentPane().add(panelMessage);
        panelMessage.setLayout(null);
        
        textSend = new JTextField("");
		textSend.setBounds(10, 21, 479, 39);
		panelMessage.add(textSend);
        textSend.setColumns(10);
        
        // textSend.addKeyListener(new KeyListener() {

		// 	@Override
		// 	public void keyTyped(KeyEvent arg0) {

		// 	}

		// 	@Override
		// 	public void keyReleased(KeyEvent arg0) {

		// 	}

		// 	@Override
		// 	public void keyPressed(KeyEvent arg0) {
		// 		;
		// 	}
		// });


		btnSend = new JButton("SEND");
		btnSend.setBounds(530, 29, 80, 23);
        panelMessage.add(btnSend);
        
		// btnSend.addActionListener(new ActionListener() {

		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
		// });
    }

    private void initalizeFilePanel() {
        panelFile = new JPanel();
		panelFile.setBounds(6, 363, 670, 60);
		frame.getContentPane().add(panelFile);
        panelFile.setLayout(null);

        Label label = new Label("Link send file: ");
		label.setBounds(10, 21, 80, 22);
        panelFile.add(label);
        
        textPath = new JTextField("");
		textPath.setBounds(100, 21, 388, 25);
		panelFile.add(textPath);
		textPath.setEditable(false);
        
        btnChoose = new JButton("Browse");
		btnChoose.setBounds(500, 21, 50, 25);
        panelFile.add(btnChoose);
        btnChoose.setBorder(BorderFactory.createEmptyBorder());
        btnChoose.setContentAreaFilled(false);
        
		// btnChoose.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
        // });
        
        btnUpLoad = new JButton("Send");
		btnUpLoad.setBounds(550, 21, 50, 25);
        panelFile.add(btnUpLoad);
        
        btnUpLoad.setContentAreaFilled(false);
		btnUpLoad.setBorder(BorderFactory.createEmptyBorder());

		// btnUpLoad.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
        // });

        btnDel = new JButton("Clear");
		btnDel.setBounds(600, 21, 50, 25);
        panelFile.add(btnDel);
        btnDel.setContentAreaFilled(false);
        btnDel.setBorder(BorderFactory.createEmptyBorder());
        
		// btnDel.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
		// });
		
    }

    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatGUI window = new ChatGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}