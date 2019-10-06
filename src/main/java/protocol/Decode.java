package protocol;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Decode {

    private static Pattern accountPattern = Pattern.compile(
        Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + ".*"
        + Tags.PEER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG + ".*"
        + Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG);

        private static Pattern request = Pattern.compile(
            Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
            + "[^<>]+" + Tags.PEER_NAME_CLOSE_TAG+ Tags.STATUS_OPEN_TAG 
            + "(" + Tags.SERVER_ONLINE + "|" + Tags.SERVER_OFFLINE + ")" 
            + Tags.STATUS_CLOSE_TAG + Tags.SESSION_KEEP_ALIVE_CLOSE_TAG);

    public static ArrayList<String> getUser(String message) {
        ArrayList<String> user = new ArrayList<String>();
        
		if (accountPattern.matcher(message).matches()) {
			Pattern namePattern = Pattern.compile(
                Tags.PEER_NAME_OPEN_TAG + ".*"
                + Tags.PEER_NAME_CLOSE_TAG);
                
			Pattern portPattern = Pattern.compile(
                Tags.PORT_OPEN_TAG + "[0-9]*"
                + Tags.PORT_CLOSE_TAG);
                
			Matcher find = namePattern.matcher(message);
			if (find.find()) {
				String name = find.group(0);
				user.add(name.substring(11, name.length() - 12));
                find = portPattern.matcher(message);
                
				if (find.find()) {
					String port = find.group(0);
					user.add(port.substring(6, port.length() - 7));
				} else
					return null;
			} else
				return null;
		} else
			return null;
		return user;
    }
    
    public static ArrayList<Peer> getPeer(ArrayList<Peer> peerList, String msg) {
		Pattern onlineStatePattern = Pattern.compile(Tags.STATUS_OPEN_TAG
            + Tags.SERVER_ONLINE + Tags.STATUS_CLOSE_TAG);

        Pattern peerNamePattern = Pattern.compile(Tags.PEER_NAME_OPEN_TAG 
            + "[^<>]*" + Tags.PEER_NAME_CLOSE_TAG);

		if (request.matcher(msg).matches()) {
			Matcher findOnlineState = onlineStatePattern.matcher(msg);
            if (findOnlineState.find()) return peerList;
            
			Matcher findPeerName = peerNamePattern.matcher(msg);
			if (findPeerName.find()) {
				String peerName = findPeerName.group(0);
				String name = peerName.substring(11, peerName.length() - 12);
				int size = peerList.size();
				for (int i = 0; i < size; i++)
					if (name.equals(peerList.get(i).getName())) {
						peerList.remove(i);
						break;
					}
			}
		}
		return peerList;
	}
}
