package kraken;

import java.util.ArrayList;
import java.util.Arrays;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void test_GetAccountInformation_Right()
    {
        ArrayList<String> test_code = Decode.getAccountInformation(
            "<SESSION><PEER_NAME>abc</PEER_NAME><PORT>8080</PORT></SESSION>"
        );
        ArrayList<String> expect_code = new ArrayList<String>(
            Arrays.asList("abc", "8080")
        );
        assertEquals(expect_code, test_code);
    }
    public void test_GetAccountInformation_Wrong()
    {
        ArrayList<String> test_code_one = Decode.getAccountInformation(
            "<PEER_NAME>abc</PEER_NAME><PORT>8080</PORT>"
        );
        ArrayList<String> test_code_two = Decode.getAccountInformation(
            "<SESSION><PORT>8080</PORT></SESSION>"
        );
        ArrayList<String> test_code_three = Decode.getAccountInformation(
            "<SESSION><PEER_NAME>abc</PEER_NAME></SESSION>"
        );
        ArrayList<String> expect_code = null;

        assertEquals(expect_code, test_code_one);
        assertEquals(expect_code, test_code_two);
        assertEquals(expect_code, test_code_three);
    }
    public void test_GetAllAccount_Right()
    {
        ArrayList<Peer> test_code = Decode.getAllAccount(
            "<SESSION_ACCEPT>"
            +"<PEER><PEER_NAME>abc</PEER_NAME><IP>127.0.0.1</IP><PORT>8080</PORT></PEER>"+
              "<PEER><PEER_NAME>xyz</PEER_NAME><IP>127.0.0.1</IP><PORT>8081</PORT></PEER>"+
            "</SESSION_ACCEPT>"
        );
        Peer abc = new Peer(); abc.setPeer("abc", "127.0.0.1", 8080);
        Peer xyz = new Peer(); xyz.setPeer("xyz", "127.0.0.1", 8081);

        ArrayList<Peer> expect_code = new ArrayList<Peer>(Arrays.asList(abc, xyz));
        assertEquals(expect_code.toString(), test_code.toString());
    }
    public void test_GetAllAccount_Wrong()
    {
        ArrayList<Peer> test_code = Decode.getAllAccount(
            "<PEER><PEER_NAME>abc</PEER_NAME><IP>127.0.0.1</IP><PORT>8080</PORT></PEER>" +
            "<PEER><PEER_NAME>xyz</PEER_NAME><IP>127.0.0.1</IP><PORT>8081</PORT></PEER>"
        );

        ArrayList<Peer> expect_code = null;
        assertEquals(expect_code, test_code);
    }
    public void test_GetTextMessage_Right()
    {
        String test_code = Decode.getTextMessage(
            "<CHAT_MSG>Di du dua di em oi</CHAT_MSG>"
        );
        String expect_code = "Di du dua di em oi";
        assertEquals(expect_code, test_code);
    }
}
