package kraken;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

public class Encode {

	public static String genAccountRequest(String name, String port) {
		return Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG 
			+ name + Tags.PEER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG 
			+ port + Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG;
	}

	public static String genOnlineMessage(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
			+ name + Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG
			+ Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG
			+ Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
	}

	public static String genOfflineMessage(String name) {
		return Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
			+ Tags.PEER_NAME_CLOSE_TAG + Tags.STATUS_OPEN_TAG + Tags.SERVER_OFFLINE
			+ Tags.STATUS_CLOSE_TAG + Tags.SESSION_KEEP_ALIVE_CLOSE_TAG;
	}
	
	public static String genPeerListMessage(ArrayList<Peer> peer_list) throws Exception {
		String msg = Tags.SESSION_ACCEPT_OPEN_TAG;
		int size = peer_list.size();	
		for (int i = 0; i < size; i++) {		
			Peer peer = peer_list.get(i);	
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

	public static String genChatRequest(String name) {
		return Tags.CHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + name
				+ Tags.PEER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG;
	}

	public static String genTextChatMessage(String message) {
		Pattern checkMessage = Pattern.compile("[^<>]*(<|>)");
		Matcher findMessage = checkMessage.matcher(message);
		String result = "";
		while (findMessage.find()) {
			String subMessage = findMessage.group(0);
			int begin = subMessage.length();
			char nextChar = message.charAt(subMessage.length() - 1);
			System.out.println(result);
			result += subMessage + nextChar;
			subMessage = message.substring(begin, message.length());
			message = subMessage;
			findMessage = checkMessage.matcher(message);
		}
		result += message;
		return Tags.CHAT_MSG_OPEN_TAG + result + Tags.CHAT_MSG_CLOSE_TAG;
	}

	public static String genFileChatMessage(String name) {
		return Tags.FILE_REQ_OPEN_TAG + name + Tags.FILE_REQ_CLOSE_TAG;
	}

}

