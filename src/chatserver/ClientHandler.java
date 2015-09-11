
package chatserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread
{
    Socket socket;
    ChatServer es;
    Scanner input;
    PrintWriter writer;
    String username;

    public ClientHandler(Socket soc, ChatServer echo) throws IOException
    {
        socket = soc;
        es = echo;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    // Runs forever and holds the connection to one client, sending messages to this client from other clients, and forwards messages from this client to other clients
    @Override
    public void run()
    {
        // ClientHandler waiting to receive the first message from EchoClient. Expecting following:   USER#{NAME}   (Example: USER#Peter)
        String message;
        try
        {
            message = input.nextLine();
        } catch (NoSuchElementException no)
        {
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Run() - Error trying to read 1st input.nextLine: ", no));
            closeConnection();
            return;
        }
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        
        // Splitting 'message' with # to get Command and Description
        String splitter = "[#]";
        String[] tokens = message.split(splitter);
        
        // Testing if server received correct protocol (2 Strings, after split: USER-Command + NAME). If not, close connection
        if (tokens.length != 2)
        {
            closeConnection();
            return;
        }
        
        // Getting Command-String and test if it says "USER". If true, save Description-String (Username), Otherwise close connection
        String command = tokens[0].toUpperCase();
        if (command.equalsIgnoreCase("USER"))
        {
            username = tokens[1];
            if(es.mapUserToClient(username, this))
            {
                es.sendUserList();
            } else
            {
                closeConnection();
                return;
            }
        } else
        {
            closeConnection();
            return;
        }
        
        // Connection established and username mapped to ClientHandler. Chat now possible
        boolean continueClient = true;
        while (continueClient)
        {
            // Waiting for message from user
            try
            {
                message = input.nextLine();
            } catch (NoSuchElementException no)
            {
                Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("ClientHandler, Run() - Error trying to read 2nd input.nextLine: ", no));
                break;
            }
            Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
            
            // Splitting chat-message, and get Command-String
            splitter = "[#]";
            tokens = message.split(splitter);
            command = tokens[0].toUpperCase();
            
            // Switch between Commands
            switch (command)
            {
                case "MSG":
                    // Send message (MSG) to all clients or as a personal message, to the specified client:
                    if(tokens.length > 2)
                    {
                        String receivers = tokens[1];
                        String content = tokens[2];
                        if (receivers.contains("*"))
                        {
                            prepareMessageToAll(content);
                        } else
                        {
                            prepareMessageToPrivate(receivers, content);
                        }
                    }
                    break;
                case "STOP":
//                    continueClient = false;
                    break;
                default:
                    continueClient = false;
                    break;
            }
        }
        closeConnection();
    }
    
    // Close input and writer before closing socket
    private void closeConnection()
    {
        Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Closing connection..."));
        
        es.removeClient(username, this);
        es.sendUserList();
        
        input.close();
        writer.close();
        
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // Send message to this ClientHandler. (This method is used by other ClientHandlers when trying to send a message to this ClientHandler)
    private synchronized void sendMessage(String message)
    {
        writer.println(message);
    }
    
    // Send a list of users to all online ClientHandlers
    public void sendUserList(String userString)
    {
        writer.println(userString);
    }
    
    // Send a message to all ClientHandlers (All users), with the specified content
    private void prepareMessageToAll(String content)
    {
        ArrayList<ClientHandler> clientList = es.getClientList();
        String message = "MSG#" + username + "#" + content;
        for (ClientHandler ch : clientList)
        {
            ch.sendMessage(message);
            
//            // Check if ClientHandler is still alive, in attempt to avoid running method in non-existing Thread
//            if(ch.isAlive())
//            {
//                ch.sendMessage(message);
//            }
        }
    }
    
    // Send a private message to one or more receivers, with the specified content
    private void prepareMessageToPrivate(String receivers, String content)
    {
        ArrayList<String> receiverList = getPrivateMessengers(receivers);
        Map<String,ClientHandler> userList = es.getUserList();
        ArrayList<ClientHandler> clientList = new ArrayList<>();
        
        // Find and save ClientHandler's of all intended receivers
        Iterator iterator = userList.entrySet().iterator();
        while (iterator.hasNext())
        {
            Map.Entry mapEntry = (Map.Entry) iterator.next();
            String user = mapEntry.getKey().toString();
            if(receiverList.contains(user))
            {
                clientList.add((ClientHandler) mapEntry.getValue());
            }
        }
        
        // Send message to the saved ClientHandlers
        String message = "MSG#" + username + "#" + content;
        for (ClientHandler ch : clientList)
        {
            ch.sendMessage(message);
        }
    }
    
    // Split receivers-String into a list of receivers (Receivers-String example: Peter,Martin,Jonas)
    private ArrayList<String> getPrivateMessengers(String receivers)
    {
        String splitter = "[,]";
        String[] tokens = receivers.split(splitter);
        ArrayList<String> list = new ArrayList<>();
        
        for(int i = 0; i < tokens.length; i++)
        {
            list.add(tokens[i]);
        }
        list.add(username);
        return list;
    }
}
