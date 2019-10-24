package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

import protocol.Peer;
import protocol.Decode;
import protocol.Encode;
import protocol.Tags;

public class Menu {

	public static ArrayList<Peer> client = null;
	private Chat server;
	private InetAddress ipServer;
	private int portServer = 8080;
	private String nameUser = "";
	private boolean isStop = false;
	private static int portclient = 10000;
	private Socket socketClient;
	private ObjectInputStream serverInputStream;
	private ObjectOutputStream serverOutputStream;

	public Menu(String arg, int arg1, String name, String dataUser) throws Exception {
		ipServer = InetAddress.getByName(arg);
		nameUser = name;
		portclient = arg1;
		client = Decode.getAllUser(dataUser);
		new Thread(new Runnable(){

			@Override
			public void run() {
				updateFriend();
			}
		}).start();
		server = new Chat(nameUser);
		(new Request()).start();
	}

	public static int getPort() {
		return portclient;
	}

	public void request() throws Exception {
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(ipServer, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.genOnlineMessage(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverInputStream = new ObjectInputStream(socketClient.getInputStream());
		msg = (String) serverInputStream.readObject();
		serverInputStream.close();
		client = Decode.getAllUser(msg);
		new Thread(new Runnable() {

			@Override
			public void run() {
				updateFriend();
			}
		}).start();
	}

	public class Request extends Thread {
		@Override
		public void run() {
			super.run();
			while (!isStop) {
				try {
					Thread.sleep(15000);
					request();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void requestChat(String IP, int host, String guest) throws Exception {
		final Socket connclient = new Socket(InetAddress.getByName(IP), host);
		ObjectOutputStream sendrequestChat = new ObjectOutputStream(connclient.getOutputStream());
		sendrequestChat.writeObject(Encode.genChatRequest(nameUser));
		sendrequestChat.flush();
		ObjectInputStream receivedChat = new ObjectInputStream(connclient.getInputStream());
		String msg = (String) receivedChat.readObject();
		if (msg.equals(Tags.CHAT_DENY_TAG)) {
			MenuGUI.request("May be your friend busy!", false);
			connclient.close();
			return;
		} else {
			new ChatGUI(nameUser, guest, connclient, portclient);
		}
		// TO DO SOMETHING
		// updateFiend
	}

	public void exit() throws IOException, ClassNotFoundException {
		isStop = true;
		socketClient = new Socket();
		SocketAddress addressServer = new InetSocketAddress(ipServer, portServer);
		socketClient.connect(addressServer);
		String msg = Encode.genOfflineMessage(nameUser);
		serverOutputStream = new ObjectOutputStream(socketClient.getOutputStream());
		serverOutputStream.writeObject(msg);
		serverOutputStream.flush();
		serverOutputStream.close();
		server.exit();
	}

	public void updateFriend() {
		int size = client.size();
		MenuGUI.clearAll();
		for (int i = 0; i < size; i++)
			if (!client.get(i).getName().equals(nameUser))
				MenuGUI.updateFiend(client.get(i).getName());
	}
}
