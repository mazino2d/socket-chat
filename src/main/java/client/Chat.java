package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import protocol.Decode;
import protocol.Tags;

public class Chat {

	private String username = "";
	private int port = 0;
	private ServerSocket serverPeer;
	private boolean isStop = false;

	public Chat(String username, int port) throws Exception {
		this.username = username;
		this.port = port;
		serverPeer = new ServerSocket(port);
		(new WaitPeerConnect()).start();
	}
	
	public void exit() throws IOException {
		isStop = true;
		serverPeer.close();
	}

	class WaitPeerConnect extends Thread {

		Socket connection;
		ObjectInputStream getRequest;

		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					connection = serverPeer.accept();
					getRequest = new ObjectInputStream(connection.getInputStream());
					String msg = (String) getRequest.readObject();
					String name = Decode.getNameRequestChat(msg);
					int result = MenuGUI.showDialog("Would you like chat with " + name, true);
					ObjectOutputStream send = new ObjectOutputStream(connection.getOutputStream());
					if (result == 0) {
						send.writeObject(Tags.CHAT_ACCEPT_TAG);
						new ChatGUI(username, name, connection, port);
					} else if (result == 1) {
						send.writeObject(Tags.CHAT_DENY_TAG);
					}
					send.flush();
				} catch (Exception e) {
					break;
				}
			}
			try {
				serverPeer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
