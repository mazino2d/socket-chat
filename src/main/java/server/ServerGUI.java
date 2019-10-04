package server;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.TextArea;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerGUI {

    private JFrame frame;
    private JTextField textIP, textPort;
    private static TextArea textMessage;

    public ServerGUI() {
        initializeFrame();
        initializeLabel();
        initializeTextBox();
        initializeButton();
    }

    private void initializeFrame() {
        frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(200, 200, 622, 442);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
    }

    private void initializeLabel() {
        JLabel lblIP = new JLabel("IP ADDRESS :");
		lblIP.setBounds(36, 55, 95, 16);
        frame.getContentPane().add(lblIP);
        
        JLabel lbPort = new JLabel("PORT : ");
		lbPort.setBounds(315, 55, 61, 16);
        frame.getContentPane().add(lbPort);
        
        JLabel lbGroupName = new JLabel("HPC Team");
		lbGroupName.setBounds(250, 6, 109, 16);
		frame.getContentPane().add(lbGroupName);
    }

    private void initializeTextBox() {
        textIP = new JTextField();
		textIP.setBounds(130, 49, 176, 28);
        frame.getContentPane().add(textIP);
        
        try {
            InetAddress localhost = Inet4Address.getLocalHost();
            String localhostAddress = localhost.getHostAddress(); 
			textIP.setText(localhostAddress);
        } catch (UnknownHostException e) {e.printStackTrace();}
        
        textPort = new JTextField();
        textPort.setBounds(366, 49, 208, 28);
        textPort.setText("8080");
        frame.getContentPane().add(textPort);
        
        textMessage = new TextArea();					
		textMessage.setEditable(false);
		textMessage.setBounds(6, 130, 602, 270);
		frame.getContentPane().add(textMessage);
    }

    private void initializeButton() {
        JButton btnStart = new JButton("START");
        btnStart.setBounds(36, 90, 269, 29);
		frame.getContentPane().add(btnStart);

		// btnStart.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
		// 		;
		// 	}
        // });

        JButton btnStop = new JButton("STOP");
        btnStop.setBounds(315, 90, 260, 29);
        frame.getContentPane().add(btnStop);
        
		// btnStop.addActionListener(new ActionListener() {
		// 	public void actionPerformed(ActionEvent arg0) {
        //         ;
		// 	}
		// });
    }

    public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI window = new ServerGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}