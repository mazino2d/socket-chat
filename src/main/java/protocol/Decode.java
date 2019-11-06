package protocol;

import java.util.ArrayList;
import java.util.regex.Pattern;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class Decode {

	public static ArrayList<String> getAccountInformation(String message) {
		if (find_account.matcher(message).matches()) {
			ArrayList<String> account_infomation = new ArrayList<String>();
			
			Document document = convertStringToXML(message);

			String name = document
				.getElementsByTagName("PEER_NAME")
				.item(0).getTextContent();
			account_infomation.add(name);

			String port = document
				.getElementsByTagName("PORT")
				.item(0).getTextContent();
			account_infomation.add(port);

			return account_infomation;
		} else
			return null;
	}

	public static String getDiedAccount(String message) {
		if (request.matcher(message).matches()) {
			Document document = convertStringToXML(message);

			String status = document
				.getElementsByTagName("STATUS")
				.item(0).getTextContent();
			
			if(status.equals(Tags.SERVER_ONLINE)) return null;

			if(status.equals(Tags.SERVER_OFFLINE)) {
				String name = document
				.getElementsByTagName("PEER_NAME")
				.item(0).getTextContent();

				return name;
			}
		}
		return "";
	}

	public static ArrayList<Peer> getAllAccount(String message) {
		if (find_accounts.matcher(message).matches()) {
			ArrayList<Peer> account_list = new ArrayList<Peer>();

			Document document = convertStringToXML(message);

			NodeList node_list = document.getElementsByTagName("PEER");

			for(int i = 0; i < node_list.getLength(); i = i + 1) {
				Node node = node_list.item(i);
				if(node.getNodeType() == Node.ELEMENT_NODE) {
						Element element = (Element) node;

						String name = element
							.getElementsByTagName("PEER_NAME")
							.item(0).getTextContent();
						String host = element
							.getElementsByTagName("IP")
							.item(0).getTextContent();
						Integer port = Integer.parseInt(element
							.getElementsByTagName("PORT")
							.item(0).getTextContent());
						
						Peer new_peer = new Peer();
						new_peer.setPeer(name, host, port);

						account_list.add(new_peer);
				}	
			}

			return account_list;
		} else
			return null;
	}

	public static String getTextMessage(String message) {
		if (find_message.matcher(message).matches()) {
			int begin = Tags.CHAT_MSG_OPEN_TAG.length();
			int end = message.length() - Tags.CHAT_MSG_CLOSE_TAG.length();
			String text_message = message.substring(begin, end);
			return text_message;
		} else
			return "";
	}

	public static String getNameRequestChat(String msg) {
		if (checkRequest.matcher(msg).matches()) {
			int lenght = msg.length();
			String name = msg
					.substring(
							(Tags.CHAT_REQ_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG)
									.length(),
							lenght
									- (Tags.PEER_NAME_CLOSE_TAG + Tags.CHAT_REQ_CLOSE_TAG)
											.length());
			return name;
		}
		return "";
	}

	public static boolean checkFile(String name) {
		if (check_file.matcher(name).matches())
			return true;
		else
			return false;
	}

	public static boolean checkFeedBack(String message) {
		if (feed_back.matcher(message).matches())
			return true;
		else
			return false;
	}

	private static Document convertStringToXML(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
			Document document = builder.parse(
				new InputSource(new StringReader(xmlString))
			);
			
			//Normalize xml structure
			document.normalize();

            return document;
        }
        catch (Exception e)
        {
            e.printStackTrace();
		}
		
        return null;
    }

	private static Pattern find_account = Pattern.compile(
			Tags.SESSION_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG + ".*"
			+ Tags.PEER_NAME_CLOSE_TAG + Tags.PORT_OPEN_TAG + ".*"
			+ Tags.PORT_CLOSE_TAG + Tags.SESSION_CLOSE_TAG
		);

	private static Pattern find_accounts = Pattern.compile(
		Tags.SESSION_ACCEPT_OPEN_TAG + "(" + Tags.PEER_OPEN_TAG 
		+ Tags.PEER_NAME_OPEN_TAG + ".+" + Tags.PEER_NAME_CLOSE_TAG
		+ Tags.IP_OPEN_TAG + ".+" + Tags.IP_CLOSE_TAG + Tags.PORT_OPEN_TAG
		+ "[0-9]+" + Tags.PORT_CLOSE_TAG + Tags.PEER_CLOSE_TAG + ")*"
		+ Tags.SESSION_ACCEPT_CLOSE_TAG);

	private static Pattern find_message = Pattern.compile(
		Tags.CHAT_MSG_OPEN_TAG+ ".*" 
		+ Tags.CHAT_MSG_CLOSE_TAG
	);

	private static Pattern checkRequest = Pattern.compile(Tags.CHAT_REQ_OPEN_TAG
				+ Tags.PEER_NAME_OPEN_TAG + "[^<>]*" + Tags.PEER_NAME_CLOSE_TAG
				+ Tags.CHAT_REQ_CLOSE_TAG);
	
	private static Pattern check_file = Pattern.compile(
		Tags.FILE_REQ_OPEN_TAG + ".*" 
		+ Tags.FILE_REQ_CLOSE_TAG
	);

	private static Pattern feed_back = Pattern.compile(
			Tags.FILE_REQ_ACK_OPEN_TAG + ".*"
			+ Tags.FILE_REQ_ACK_CLOSE_TAG
	);

	private static Pattern request = Pattern.compile(
		Tags.SESSION_KEEP_ALIVE_OPEN_TAG + Tags.PEER_NAME_OPEN_TAG
			+ "[^<>]+" + Tags.PEER_NAME_CLOSE_TAG+ Tags.STATUS_OPEN_TAG
			+ "(" + Tags.SERVER_ONLINE + "|"+ Tags.SERVER_OFFLINE + ")" 
			+ Tags.STATUS_CLOSE_TAG + Tags.SESSION_KEEP_ALIVE_CLOSE_TAG);

}

