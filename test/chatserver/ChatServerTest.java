
package chatserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ChatServerTest {
    
    public ChatServerTest() {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                ChatServer.main(null);
            }
        }).start();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of main method, of class EchoServer.
     */
    @Test
    public void testMain() {
        System.out.println("main");
        String[] args = null;
        ChatServer.main(args);
    }
    
    /**
     * Test of stopServer method, of class EchoServer.
     */
    @Test
    public void testStopServer() {
        System.out.println("stopServer");
        ChatServer.stopServer();
    }
    
    /**
     * Test of mapUserToClient method, of class EchoServer.
     */
    @Test
    public void testMapUserToClient() {
        System.out.println("mapUserToClient");
        String user = "";
        ClientHandler ch = null;
        ChatServer instance = new ChatServer();
        boolean expResult = true;
        boolean result = instance.mapUserToClient(user, ch);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of removeClient method, of class EchoServer.
     */
    @Test
    public void testRemoveClient() {
        System.out.println("removeClient");
        String user = "";
        ClientHandler ch = null;
        ChatServer instance = new ChatServer();
        instance.removeClient(user, ch);
    }
    
    /**
     * Test of sendUserList method, of class EchoServer.
     */
    @Test
    public void testSendUserList() {
        System.out.println("sendUserList");
        ChatServer instance = new ChatServer();
        instance.sendUserList();
    }
    
    /**
     * Test of getClientList method, of class EchoServer.
     */
    @Test
    public void testGetClientList() {
        System.out.println("getClientList");
        ChatServer instance = new ChatServer();
        ArrayList<ClientHandler> expResult = new ArrayList<>();
        ArrayList<ClientHandler> result = instance.getClientList();
        assertEquals(expResult, result);
    }
    
    /**
     * Test of getUserList method, of class EchoServer.
     */
    @Test
    public void testGetUserList() {
        System.out.println("getUserList");
        ChatServer instance = new ChatServer();
        Map<String, ClientHandler> expResult = new HashMap<>();
        Map<String, ClientHandler> result = instance.getUserList();
        assertEquals(expResult, result);
    }
}
