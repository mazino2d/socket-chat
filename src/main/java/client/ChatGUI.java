package client;

import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.UnsupportedLookAndFeelException;

import mdlaf.MaterialLookAndFeel;

import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import javax.swing.JProgressBar;
import javax.swing.JPanel;

import protocol.FileData;
import protocol.Decode;
import protocol.Encode;
import protocol.Tags;

public class ChatGUI {

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
	private static String TEMP = "/temp/";

	private JFrame fmChat;
	private JPanel panelMessage, panelFile;
	private JLabel textState, lblReceive;
	private JTextField textPath, textSend;
	private JTextArea textDisPlayChat;
	private JButton btnChoose, btnUpLoad, btnDel;
	private JProgressBar progressSendFile;

	private ChatRoom chat;
	private Socket socketChat;
	private String username = "", guest_name = "", file_name = "";
	public boolean isStop = false, isSendFile = false, isReceiveFile = false;
	private int portServer = 0;

	public ChatGUI(String user, String guest, Socket socket, int port) {
		username = user;
		guest_name = guest;
		socketChat = socket;
		this.portServer = port;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatGUI window = new ChatGUI(username, guest_name, socketChat, portServer, 0);
					window.fmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public ChatGUI() {
		initializeFrame();
		initializePanel();
		initializeLabel();
		initializeTextBox();
		initializeButton();
	}

	public ChatGUI(String user, String guest, Socket socket, int port, int a) throws Exception {
		username = user;
		guest_name = guest;
		socketChat = socket;
		this.portServer = port;

		initializeFrame();
		initializePanel();
		initializeLabel();
		initializeTextBox();
		initializeButton();
		initializeProgressBar();

		chat = new ChatRoom(socketChat, username, guest_name);
		chat.start();
	}

	private void initializeFrame() {
		fmChat = new JFrame();
		fmChat.setTitle("Chat Room");
		fmChat.setResizable(false);
		fmChat.setBounds(450, 100, 688, 540);
		fmChat.getContentPane().setLayout(null);
		fmChat.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	private void initializePanel() {
		panelFile = new JPanel();
		panelFile.setBounds(6, 363, 670, 60);
		panelFile.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "File"));
		fmChat.getContentPane().add(panelFile);
		panelFile.setLayout(null);

		panelMessage = new JPanel();
		panelMessage.setBounds(6, 420, 670, 71);
		panelMessage.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Message"));
		fmChat.getContentPane().add(panelMessage);
		panelMessage.setLayout(null);
	}

	private void initializeLabel() {
		JLabel lblClientIP = new JLabel("Chatting with: ");
		lblClientIP.setBounds(6, 12, 155, 20);
		fmChat.getContentPane().add(lblClientIP);

		JLabel label = new JLabel("Address: ");
		label.setBounds(10, 21, 100, 22);
		panelFile.add(label);

		textState = new JLabel("");
		textState.setBounds(6, 502, 81, 22);
		textState.setVisible(false);
		fmChat.getContentPane().add(textState);

		lblReceive = new JLabel("Receiving ...");
		lblReceive.setBounds(491, 510, 83, 14);
		lblReceive.setVisible(false);
		fmChat.getContentPane().add(lblReceive);
	}
	
	private void initializeTextBox() {
		JTextField textName = new JTextField(username);
		textName.setEditable(false);
		textName.setBounds(120, 7, 390, 28);
		fmChat.getContentPane().add(textName);
		textName.setText(guest_name);
		textName.setColumns(10);

		textDisPlayChat = new JTextArea();
		textDisPlayChat.setEditable(false);
		textDisPlayChat.setBounds(15, 50, 650, 300);
		fmChat.getContentPane().add(textDisPlayChat);

		textPath = new JTextField("");
		textPath.setBounds(70, 21, 340, 25);
		panelFile.add(textPath);
		textPath.setEditable(false);
		textPath.setColumns(10);

		textSend = new JTextField("");
		textSend.setBounds(10, 21, 479, 39);
		panelMessage.add(textSend);
		textSend.setColumns(10);

		textSend.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent arg0) {

			}

			@Override
			public void keyReleased(KeyEvent arg0) {

			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					String msg = textSend.getText();
					if (isStop) {
						updateChat("[" + username + "] : " + textSend.getText().toString());
						textSend.setText("");
						return;
					}
					if (msg.equals("")) {
						textSend.setText("");
						textSend.setCaretPosition(0);
						return;
					}
					try {
						chat.sendMessage(Encode.genTextChatMessage(msg));
						updateChat("["+ username +"] : " + msg);
						textSend.setText("");
						textSend.setCaretPosition(0);
					} catch (Exception e) {
						textSend.setText("");
						textSend.setCaretPosition(0);
					}
				}
			}
		});
	}

	private void initializeButton() {

		File fileTemp = new File(URL_DIR + "/temp");
		if (!fileTemp.exists()) {
			fileTemp.mkdirs();
		}
		
		JButton btnChoose = new JButton("Browse");
		btnChoose.setBounds(410, 21, 85, 25);
		panelFile.add(btnChoose);
		btnChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = fileChooser.showOpenDialog(fmChat);
				if (result == JFileChooser.APPROVE_OPTION) {
					isSendFile = true;
					String path_send = (fileChooser.getSelectedFile()
							.getAbsolutePath()) ;
					System.out.println(path_send);
					file_name = fileChooser.getSelectedFile().getName();
					textPath.setText(path_send);
				}
			}
		});
		
		JButton btnUpLoad = new JButton("Send");
		btnUpLoad.setBounds(495, 21, 85, 25);
		panelFile.add(btnUpLoad);
		btnUpLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (isSendFile)
					try {
						chat.sendMessage(Encode.genFileChatMessage(file_name));
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		});

		JButton btnDel = new JButton("Clear");
		btnDel.setBounds(580, 21, 85, 25);
		panelFile.add(btnDel);
		btnDel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isSendFile = false;
				textSend.setText("");
				textPath.setText("");
			}
		});

		JButton btnSend = new JButton("SEND");
		btnSend.setBounds(530, 29, 80, 23);
		panelMessage.add(btnSend);
		btnSend.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				if (isStop) {
					updateChat("[" + username +"] : " + textSend.getText().toString());
					textSend.setText("");
					return;
				}
				String msg = textSend.getText();
				if (msg.equals(""))
					return;
				try {
					chat.sendMessage(Encode.genTextChatMessage(msg));
					updateChat("["+ username +"] : " + msg);
					textSend.setText("");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		JButton btnDisconnect = new JButton("DISCONNECT");
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int result = JOptionPane.showConfirmDialog(
					fmChat, "Do you want close chat with " + guest_name, null, 
					JOptionPane.YES_NO_OPTION
				);

				if (result == 0) {
					try {
						isStop = true;
						fmChat.dispose();
						chat.sendMessage(Tags.CHAT_CLOSE_TAG);
						chat.stopChat();
						System.gc();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		btnDisconnect.setBounds(535, 5, 130, 29);
		fmChat.getContentPane().add(btnDisconnect);
	}

	private void initializeProgressBar() {
		progressSendFile = new JProgressBar(0, 100);
		progressSendFile.setBounds(93, 510, 388, 14);
		progressSendFile.setStringPainted(true);
		fmChat.getContentPane().add(progressSendFile);
		progressSendFile.setVisible(false);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatGUI window = new ChatGUI();
					window.fmChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateChat(String msg) {
		textDisPlayChat.append(msg + "\n");
	}

	public void copyFileReceive(InputStream inputStr, OutputStream outputStr,
			String path) throws IOException {
		byte[] buffer = new byte[1024];
		int lenght;
		while ((lenght = inputStr.read(buffer)) > 0) {
			outputStr.write(buffer, 0, lenght);
		}
		inputStr.close();
		outputStr.close();
		File fileTemp = new File(path);
		if (fileTemp.exists()) {
			fileTemp.delete();
		}
	}

	public class ChatRoom extends Thread {

		private Socket connect;
		private ObjectOutputStream outPeer;
		private ObjectInputStream inPeer;
		private boolean continueSendFile = true, finishReceive = false;
		private int sizeOfSend = 0, sizeOfData = 0, sizeFile = 0,
				sizeReceive = 0;
		private String nameFileReceive = "";
		private InputStream inFileSend;
		private FileData dataFile;

		public ChatRoom(Socket connection, String name, String guest)
				throws Exception {
			connect = new Socket();
			connect = connection;
			guest_name = guest;
		}

		@Override
		public void run() {
			super.run();
			OutputStream out = null;
			while (!isStop) {
				try {
					inPeer = new ObjectInputStream(connect.getInputStream());
					Object obj = inPeer.readObject();
					if (obj instanceof String) {
						String msgObj = obj.toString();
						if (msgObj.equals(Tags.CHAT_CLOSE_TAG)) {
							isStop = true;

							JOptionPane.showMessageDialog(
								fmChat, guest_name + " may be close chat with you!");

							connect.close();
							break;
						}
						if (Decode.checkFile(msgObj)) {
							isReceiveFile = true;
							nameFileReceive = msgObj.substring(10,
									msgObj.length() - 11);

							int result = JOptionPane.showConfirmDialog(
								fmChat, guest_name + " send file " + nameFileReceive
								+ " for you", null,  JOptionPane.YES_NO_OPTION
							);

							System.out.println("result" + result);
							
							if (result == 0) {
								File fileReceive = new File(URL_DIR + TEMP
										+ "/" + nameFileReceive);
								if (!fileReceive.exists()) {
									fileReceive.createNewFile();
								}
								String msg = Tags.FILE_REQ_ACK_OPEN_TAG
										+ Integer.toBinaryString(portServer)
										+ Tags.FILE_REQ_ACK_CLOSE_TAG;
								sendMessage(msg);
							} else {
								sendMessage(Tags.FILE_REQ_NOACK_TAG);
							}
						}
						if (Decode.checkFeedBack(msgObj)) {
							// btnChoose.setEnabled(false);
							// btnUpLoad.setEnabled(false);
							// btnDel.setEnabled(false);
							new Thread(new Runnable() {
								public void run() {
									try {
										sendMessage(Tags.FILE_DATA_BEGIN_TAG);
										updateChat("you are sending file:	" + file_name);
										isSendFile = false;
										sendFile(textPath.getText());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).start();
						} else if (msgObj.equals(Tags.FILE_REQ_NOACK_TAG)) {
							JOptionPane.showMessageDialog(
								fmChat, guest_name + " wantn't receive file");
						} else if (msgObj.equals(Tags.FILE_DATA_BEGIN_TAG)) {
							finishReceive = false;
							lblReceive.setVisible(true);
							out = new FileOutputStream(URL_DIR + TEMP
									+ nameFileReceive);
						} else if (msgObj.equals(Tags.FILE_DATA_CLOSE_TAG)) {
							updateChat("You receive file:	" + nameFileReceive + "with size" + sizeReceive + "KB");
							sizeReceive = 0;
							out.flush();
							out.close();
							lblReceive.setVisible(false);
							new Thread(new Runnable() {

								@Override
								public void run() {
									showSaveFile();
								}
							}).start();
							finishReceive = true;
						} else {
							String message = Decode.getTextMessage(msgObj);
							updateChat("[" + guest_name + "] : " + message);
						}
					} else if (obj instanceof FileData) {
						FileData data = (FileData) obj;
						++sizeReceive;
						out.write(data.data);
					}
				} catch (Exception e) {
					File fileTemp = new File(URL_DIR + TEMP + nameFileReceive);
					if (fileTemp.exists() && !finishReceive) {
						fileTemp.delete();
					}
				}
			}
		}

		private void getData(String path) throws Exception {
			File fileData = new File(path);
			if (fileData.exists()) {
				sizeOfSend = 0;
				dataFile = new FileData();
				sizeFile = (int) fileData.length();
				sizeOfData = sizeFile % 1024 == 0 ? (int) (fileData.length() / 1024)
						: (int) (fileData.length() / 1024) + 1;
				textState.setVisible(true);
				progressSendFile.setVisible(true);
				progressSendFile.setValue(0);
				inFileSend = new FileInputStream(fileData);
			}
		}

		public void sendFile(String path) throws Exception {
			getData(path);
			textState.setText("Sending ...");
			do {
				if (continueSendFile) {
					continueSendFile = false;
					new Thread(new Runnable() {

						@Override
						public void run() {
							try {
								inFileSend.read(dataFile.data);
								sendMessage(dataFile);
								sizeOfSend++;
								if (sizeOfSend == sizeOfData - 1) {
									int size = sizeFile - sizeOfSend * 1024;
									dataFile = new FileData(size);
								}
								progressSendFile
										.setValue((int) (sizeOfSend * 100 / sizeOfData));
								if (sizeOfSend >= sizeOfData) {
									inFileSend.close();
									isSendFile = true;
									sendMessage(Tags.FILE_DATA_CLOSE_TAG);
									progressSendFile.setVisible(false);
									textState.setVisible(false);
									isSendFile = false;
									textPath.setText("");
									btnChoose.setEnabled(true);
									btnUpLoad.setEnabled(true);
									btnDel.setEnabled(true);
									updateChat("!!!YOU ARE SEND FILE COMPLETE!!!");
									inFileSend.close();
								}
								continueSendFile = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
			} while (sizeOfSend < sizeOfData);
		}

		private void showSaveFile() {
			while (true) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System
						.getProperty("user.home")));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int result = fileChooser.showSaveDialog(fmChat);
				if (result == JFileChooser.APPROVE_OPTION) {
					File file = new File(fileChooser.getSelectedFile()
							.getAbsolutePath() + "/" + nameFileReceive );
					if (!file.exists()) {
						try {
							file.createNewFile();
							Thread.sleep(1000);
							InputStream input = new FileInputStream(URL_DIR
									+ TEMP + nameFileReceive);
							OutputStream output = new FileOutputStream(
									file.getAbsolutePath());
							copyFileReceive(input, output, URL_DIR + TEMP
									+ nameFileReceive);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(fmChat, "Your file receive has error!!!");
						}
						break;
					} else {
						int resultContinue = JOptionPane.showConfirmDialog(
							fmChat, "File is exists. You want save file?", null, 
							JOptionPane.YES_NO_OPTION
						);
						if (resultContinue == 0)
							continue;
						else
							break;
					}
				}
			}
		}

		public synchronized void sendMessage(Object obj) throws Exception {
			outPeer = new ObjectOutputStream(connect.getOutputStream());
			if (obj instanceof String) {
				String message = obj.toString();
				outPeer.writeObject(message);
				outPeer.flush();
				if (isReceiveFile)
					isReceiveFile = false;
			} else if (obj instanceof FileData) {
				outPeer.writeObject(obj);
				outPeer.flush();
			}
		}

		public void stopChat() {
			try {
				connect.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
