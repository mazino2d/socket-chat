package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import protocol.Peer;
import protocol.Decode;
import protocol.Encode;
import protocol.Tags;

public class Server {

	private ArrayList<Peer> peer_list = null;	
	private ServerSocket server;						
	private Socket connection;			
	private ObjectOutputStream sender;		
	private ObjectInputStream listener;			
	public boolean isStop = false, isExit = false;		
	
	
	public Server(int port) throws Exception {
		server = new ServerSocket(port);		
		peer_list = new ArrayList<Peer>();
		(new WaitForConnect()).start();			
	}
	
	public void stopserver() throws Exception {
		isStop = true;
		server.close();							
		connection.close();						
	}
	
	private boolean waitForConnection() throws Exception {
		connection = server.accept();			
		listener = new ObjectInputStream(connection.getInputStream());	

		String message = (String) listener.readObject();						
		ArrayList<String> getData = Decode.getUser(message);					
		ServerGUI.updateMessage(message);											
		if (getData != null) {
			if (!isExsistName(getData.get(0))) {						
				saveNewPeer(getData.get(0), connection.getInetAddress()			
						.toString(), Integer.parseInt(getData.get(1)));			
				ServerGUI.updateMessage(getData.get(0));						
			} else
				return false;
		} else {
			int size = peer_list.size();					
			Decode.updatePeerOnline(peer_list, message);			
			if (size != peer_list.size()) {					
				isExit = true;								
			}
		}
		return true;
	}
	
	private void saveNewPeer(String user, String ip, int port) throws Exception {
		Peer new_peer = new Peer();		
		if (peer_list.size() == 0)				
			peer_list = new ArrayList<Peer>();
		new_peer.setPeer(user, ip, port);		
		peer_list.add(new_peer);					
	}
	
	private boolean isExsistName(String name) throws Exception {
		if (peer_list == null)
			return false;
		int size = peer_list.size();
		for (int i = 0; i < size; i++) {
			Peer peer = peer_list.get(i);
			if (peer.getName().equals(name))
				return true;
		}
		return false;
	}
	
	public class WaitForConnect extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (!isStop) {
					if (waitForConnection()) {
						if (isExit) {
							isExit = false;
						} else {
							sender = new ObjectOutputStream(connection.getOutputStream());
							sender.writeObject(Encode.genPeerListMessage(peer_list));
							sender.flush(); sender.close();
						}
					} else {
						sender = new ObjectOutputStream(connection.getOutputStream());
						sender.writeObject(Tags.SESSION_DENY_TAG);
						sender.flush(); sender.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}

