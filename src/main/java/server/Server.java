package server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import protocol.Tags;
import protocol.Decode;
import protocol.Peer;

public class Server {
    private ArrayList<Peer> peerList = null;
    private Socket socket;
    private ServerSocket server;						
    private ObjectOutputStream sender;		
    private ObjectInputStream listener;
    public boolean isStop = false, isExit = false;

    public Server(int port) throws Exception {
		server = new ServerSocket(port);		
		peerList = new ArrayList<Peer>();
		(new WaitForConnect()).start();			
    }
    
    private String sendSessionAccept() throws Exception {
		String msg = Tags.SESSION_ACCEPT_OPEN_TAG;
		int size = peerList.size();				
		for (int i = 0; i < size; i++) {		
			Peer peer = peerList.get(i);	
			msg += Tags.PEER_OPEN_TAG;			
			msg += Tags.PEER_NAME_OPEN_TAG;
			msg += peer.getName();
			msg += Tags.PEER_NAME_CLOSE_TAG;
			msg += Tags.IP_OPEN_TAG;
			msg += peer.getHost();
			msg += Tags.IP_CLOSE_TAG;
			msg += Tags.PORT_OPEN_TAG;
			msg += peer.getPort();
			msg += Tags.PORT_CLOSE_TAG;
			msg += Tags.PEER_CLOSE_TAG;			
		}
		msg += Tags.SESSION_ACCEPT_CLOSE_TAG;	
		return msg;
	}

    private boolean isExsistName(String name) throws Exception {
		int size = peerList.size();
		for (int i = 0; i < size; i++) {
			Peer peer = peerList.get(i);
			if (peer.getName().equals(name))
				return true;
		}
		return false;
	}

    public void stopserver() throws Exception {
		isStop = true; server.close();	socket.close();						
    }
    
    private boolean checkConnect() throws Exception {
		socket = server.accept();			
		listener = new ObjectInputStream(socket.getInputStream());		
        String message = (String) listener.readObject();
		ServerGUI.updateMessage(message);											
        					
		ArrayList<String> userList = Decode.getUser(message);					
		if (userList != null) {
            String userName = userList.get(0);

			if (!isExsistName(userName)) {						
                peerList.add(new Peer(userName, 
                    socket.getInetAddress().toString(),
                    Integer.parseInt(userList.get(1))
                ));
            
				ServerGUI.updateMessage(userName);						
            } else 
                return false;
		} else {
			int size = peerList.size();					
			Decode.getPeer(peerList, message);			
			if (size != peerList.size()) isExit = true;
        }
        
		return true;
	}

    public class WaitForConnect extends Thread {

		@Override
		public void run() {
			super.run();
			try {
				while (!isStop) {
					if (checkConnect()) {
						if (isExit) {
							isExit = false;
						} else {
							sender = new ObjectOutputStream(socket.getOutputStream());
							sender.writeObject(sendSessionAccept()); sender.flush();
							sender.close();
						}
					} else {
						sender = new ObjectOutputStream(socket.getOutputStream());
						sender.writeObject(Tags.SESSION_DENY_TAG); sender.flush(); 
                        sender.close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    }
    
}
