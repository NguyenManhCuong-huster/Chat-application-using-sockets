/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Thinkpad T14
 */
public class ChatServer implements Runnable{
    private static final List<ClientHandler> clients = new ArrayList<>();
    private final int PORT;
    private final WindowServer ws;


    public ChatServer(int PORT,WindowServer rs) {
        this.PORT = PORT;
        this.ws=rs;
    }
    

    
    /**
     *
     */
    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);

            // clients ket noi server
            ws.setText("Server started. Listening on port: "+PORT);
            while(true){
                Socket clientSocket = serverSocket.accept();
                ws.setText("New client connected: "+ clientSocket.getInetAddress().getHostAddress());
                
                ClientHandler ch = new ClientHandler(clientSocket, System.currentTimeMillis()+"", this);
                clients.add(ch);
                new Thread(ch).start();
            }
            
	} catch(IOException e) {
            ws.setText("Fail. Try again");
            ws.setStartServerButtonVisible(true);
	}
    }

    
    public void broadcastMessage(String id, String message){
        for(ClientHandler client: clients){
                client.sendMessage(message);
        }
    }
    public void broadcastData(String id, byte[] data){
        for(ClientHandler client: clients){
                if(!client.getId().contentEquals(id)&&data[0]==(byte)1)client.sendData(data);
                if(data[0]==(byte)0)client.sendData(data);
        }
    }
}
