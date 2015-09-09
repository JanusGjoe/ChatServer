
package chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class ClientHandlerTest
{
    ChatServer chatserver = new ChatServer();
    static Socket socket;
    static Scanner input;
    static PrintWriter output;
    ClientHandler instance;
    
    public ClientHandlerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() throws IOException
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
    public void setUp() throws IOException
    {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientHandlerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        socket = new Socket(InetAddress.getByName("localhost"), 9090);
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);
    }
    
    @After
    public void tearDown() {
    }
    
    /**
     * Test of run method, of class ClientHandler.
     */
    @Test
    public void testRun()
    {
        System.out.println("run");
        output.println("USER#Janus");
        
        String expectedResult = "USERLIST#Janus";
        String userlist = input.nextLine();
        assertEquals(expectedResult, userlist);
    }
    
    /**
     * Test of sendUserList method, of class ClientHandler.
     */
    @Test
    public void testSendUserList() throws IOException
    {
        ClientHandler instance = new ClientHandler(socket, chatserver);
        System.out.println("sendUserList");
        
        
        
        instance.sendUserList("USERLIST#Janus,Peter");
        
        String expectedResult = "test";
        String userlist = input.nextLine();
        assertEquals(expectedResult, userlist);
    }
}
