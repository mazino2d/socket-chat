package kraken;

public class Tags {

	public static int IN_VALID = -1;

	public static int MAX_MSG_SIZE = 1024;

	public static String SESSION_OPEN_TAG = "<SESSION>";
	public static String SESSION_CLOSE_TAG = "</SESSION>";
	public static String PEER_NAME_OPEN_TAG = "<PEER_NAME>";
	public static String PEER_NAME_CLOSE_TAG = "</PEER_NAME>";
	public static String PORT_OPEN_TAG = "<PORT>";
	public static String PORT_CLOSE_TAG = "</PORT>";
	public static String SESSION_KEEP_ALIVE_OPEN_TAG = "<SESSION_KEEP_ALIVE>";
	public static String SESSION_KEEP_ALIVE_CLOSE_TAG = "</SESSION_KEEP_ALIVE>";
	public static String STATUS_OPEN_TAG = "<STATUS>";
	public static String STATUS_CLOSE_TAG = "</STATUS>";
	public static String SESSION_DENY_TAG = "<SESSION_DENY />";
	public static String SESSION_ACCEPT_OPEN_TAG = "<SESSION_ACCEPT>";
	public static String SESSION_ACCEPT_CLOSE_TAG = "</SESSION_ACCEPT>";
	public static String CHAT_REQ_OPEN_TAG = "<CHAT_REQ>";
	public static String CHAT_REQ_CLOSE_TAG = "</CHAT_REQ>";
	public static String IP_OPEN_TAG = "<IP>";
	public static String IP_CLOSE_TAG = "</IP>";
	public static String CHAT_DENY_TAG = "<CHAT_DENY />";
	public static String CHAT_ACCEPT_TAG = "<CHAT_ACCEPT />";
	public static String CHAT_MSG_OPEN_TAG = "<CHAT_MSG>";
	public static String CHAT_MSG_CLOSE_TAG = "</CHAT_MSG>";
	public static String PEER_OPEN_TAG = "<PEER>";
	public static String PEER_CLOSE_TAG = "</PEER>";
	public static String FILE_REQ_OPEN_TAG = "<FILE_REQ>";
	public static String FILE_REQ_CLOSE_TAG = "</FILE_REQ>";
	public static String FILE_REQ_NOACK_TAG = "<FILE_REQ_NOACK />";
	public static String FILE_REQ_ACK_OPEN_TAG = "<FILE_REQ_ACK>";
	public static String FILE_REQ_ACK_CLOSE_TAG = "</FILE_REQ_ACK>";
	public static String FILE_DATA_BEGIN_TAG = "<FILE_DATA_BEGIN />";
	public static String FILE_DATA_OPEN_TAG = "<FILE_DATA>";
	public static String FILE_DATA_CLOSE_TAG = "</FILE_DATA>";
	public static String FILE_DATA_END_TAG = "<FILE_DATA_END />";
	public static String CHAT_CLOSE_TAG = "<CHAT_CLOSE />";

	public static String SERVER_ONLINE = "ALIVE";
	public static String SERVER_OFFLINE = "DIED";
}

