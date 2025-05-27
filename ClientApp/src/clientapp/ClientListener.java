/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientapp;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 *
 * @author Thinkpad T14
 */
public class ClientListener implements Runnable{
    private Socket socket;
    private InputStream input;
    private WindowClient wc;

    public ClientListener(Socket socket,WindowClient rc) {
        this.socket = socket;
        this.wc=rc;
        try {
            this.input = socket.getInputStream();
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
                if(buffer[0]==(byte)0){
                    byte[] data=new byte[bytesRead-1];
                    
                    System.arraycopy(buffer,1,data,0,data.length);
                    
                    String message = new String(data, 0, data.length);
                    wc.appendMS(message);
                }
                else if(buffer[0]==(byte)(1)){
                    
                    byte[] bytename=new byte[(int)buffer[1]];
                    System.arraycopy(buffer, 2, bytename, 0, bytename.length);
                    byte[] bytedata=new byte[bytesRead-2-(int)buffer[1]];
                    System.arraycopy(buffer, 2+bytename.length, bytedata, 0, bytedata.length);
                    String filename=new String(bytename);
                    
                    //ghi file

                    File f=new File(wc.getFolder()+"\\"+filename);
                    if(f.createNewFile()){
                        FileOutputStream out = new FileOutputStream(wc.getFolder()+"\\"+filename);
                        wc.appendMS("da tai: "+wc.getFolder()+"\\"+filename);
                        out.write(bytedata,0,bytedata.length);
                        out.close();
                        
                    }else wc.appendMS("Khong the tai File: "+wc.getFolder()+"\\"+filename);
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
