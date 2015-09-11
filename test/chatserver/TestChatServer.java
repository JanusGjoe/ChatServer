package chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestChatServer {

    @BeforeClass
    public static void setUpClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ChatServer.main(null);
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass() {
        ChatServer.stopServer();
    }

//    @Test
//    public void testConnect() throws IOException {
//        Socket socket = new Socket("localhost", 9090);
//        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//        Scanner input = new Scanner(socket.getInputStream());
//
//        output.println("USER#LARS");
//        Assert.assertEquals("USERLIST#LARS", input.nextLine());
//        
//        Socket socket2 = new Socket("localhost", 9090);
//        PrintWriter output2 = new PrintWriter(socket2.getOutputStream(), true);
//        Scanner input2 = new Scanner(socket2.getInputStream());
//
//        output2.println("USER#THOMAS");
//        Assert.assertEquals("USERLIST#LARS,THOMAS", input2.nextLine());
//    }
    
    @Test
    public void testMessage() throws IOException {
        
        // START 1....
        Socket socket = new Socket("localhost", 9090);
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
        Scanner input = new Scanner(socket.getInputStream());
        
        // IN
        output.println("USER#LARS");
        String text = input.nextLine();
        System.out.println(text);
        // OUT
        Assert.assertEquals("USERLIST#LARS", text);
        output.println("MSG#*#Hejjj");
        String text2 = input.nextLine();
        System.out.println(text2);
        
        Assert.assertEquals("MSG#LARS#Hejjj", text2);
        
        // START 2
        Socket socket2 = new Socket("localhost", 9090);
        PrintWriter output2 = new PrintWriter(socket2.getOutputStream(), true);
        Scanner input2 = new Scanner(socket2.getInputStream());
        
        // IN 2
        output2.println("USER#THOMAS");
        String text3 = input2.nextLine();
        System.out.println(text3);
        // OUT.
        Assert.assertEquals("USERLIST#LARS,THOMAS", text3);
        output2.println("MSG#*#Hej Alle");
        String text4 = input2.nextLine();
        System.out.println(text4);
        
        Assert.assertEquals("MSG#THOMAS#Hej Alle", text4);
       
    }
}
