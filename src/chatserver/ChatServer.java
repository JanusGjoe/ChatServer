package chatserver;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Utils;

public class ChatServer
{
    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Utils.initProperties("server.properties");
    private Map<String,ClientHandler> userList = new HashMap<String,ClientHandler>();
    private ArrayList<ClientHandler> clientList = new ArrayList<>();
    
    public static void main(String[] args)
    {
        String logFile = properties.getProperty("logFile");
        Utils.setLogFile(logFile, ChatServer.class.getName());
        Utils.closeLogger(ChatServer.class.getName());
        new ChatServer().runServer();
    }
    
    // Runs forever and assigns new connections to their own new ClientHandler
    private void runServer()
    {
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Server started. Listening on: {0}, bound to: {1}", new Object[]{port, ip});
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do
            {
                Socket socket = serverSocket.accept(); //Important Blocking call
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client. Waiting for username...");
                ClientHandler ch = new ClientHandler(socket, this);
                clientList.add(ch);
                ch.start();
            } while (keepRunning);
        } catch (IOException ex)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Stop server from ClientHandler-Class
    public static void stopServer()
    {
        keepRunning = false;
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Stop server!");
    }
    
    // Map username to its ClientHandler, shortly after a new chat-client is connected and ClientHandler-Thread is started
    public synchronized boolean mapUserToClient(String user, ClientHandler ch)
    {
        if(userList.containsKey(user))
        {
            return false;
        } else
        {
            userList.put(user, ch);
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Registered ClientHandler: {0}, with username: {1}", new Object[]{ch, user});
            return true;
        }
    }
    
//    // Fix username in case the username already exists in userlist of online 'chatters', whenever a new 'chatter' connects to server
//    private void checkUsername(String user)
//    {
//        
//    }
    
    // Remove user from userList
    public synchronized void removeClient(String user, ClientHandler ch)
    {
        userList.remove(user, ch);
        clientList.remove(ch);
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Removed client: {0} - username: {1}", new Object[]{ch, user});
    }
    
    // Send a list of users to all online ClientHandlers
    public synchronized void sendUserList()
    {
        String userString = "USERLIST#" + getUsersToString();
        for (ClientHandler ch : clientList)
        {
            ch.sendUserList(userString);
        }
    }
    
    // Return a String of all online usernames, each username seperated by "," (Example: Peter,Martin,Jonas)
    private String getUsersToString()
    {
        ArrayList<String> list = new ArrayList<>();
        Iterator iterator = userList.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            list.add("" + mapEntry.getKey());
        }
        
        String returnString = "";
        if(list.size() > 0)
        {
            returnString = list.get(0);
            for(int i = 1; i < list.size(); i++)
            {
                returnString = returnString + "," + list.get(i);
            }
        }
        return returnString;
    }
    
    // Return clientList (...to ClientHandler)
    public ArrayList<ClientHandler> getClientList()
    {
        return clientList;
    }
    
    // Return userList (...to ClientHandler)
    public Map<String,ClientHandler> getUserList()
    {
        return userList;
    }
}
