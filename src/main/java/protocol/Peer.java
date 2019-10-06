package protocol;

public class Peer {

	private String peerName = "";
	private String peerHost = "";
	private int peerPort = 0;

	public Peer(String name, String host, int port) {
        peerName = name; 
        peerHost = host; 
        peerPort = port;
	}

	public void setName(String name) {peerName = name;}

	public void setHost(String host) {peerHost = host;}

	public void setPort(int port) {peerPort = port;}

	public String getName() {return peerName;}

	public String getHost() {return peerHost;}

	public int getPort() {return peerPort;}
}
