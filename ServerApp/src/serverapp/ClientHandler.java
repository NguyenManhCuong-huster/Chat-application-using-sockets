/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverapp;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author Thinkpad T14
 */
public class ClientHandler implements Runnable{
    private Socket socket;
    private String id;
    private ChatServer chatserver;
    private InputStream input;
    private OutputStream output;

    public ClientHandler(Socket s, String id, ChatServer Server) {
        this.socket = s;
        this.id = id;
        this.chatserver = Server;
        try {
            this.input = socket.getInputStream();
            this.output = socket.getOutputStream();
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
    

    

    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024*1024*1024];
            int bytesRead;
            while((bytesRead= input.read(buffer))!=-1){
                byte[] data=new byte[bytesRead];
                System.arraycopy(buffer,0,data,0,bytesRead);
                chatserver.broadcastData(id, data);
            }
        } catch (Exception e) {
        }
    }
    
    public void sendMessage(String message){
        try {
            output.write(message.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sendData(byte[] datas){
        try {
            output.write(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getId() {
        return id;
    }
}
