package servermessagehandler;

import java.io.*;
/**
 *
 * @author chintan
 */
public class ServerMessageHandler {
    client.Client myClient;
    clientinterface.ClientInterface myUI;
    
    
    static String message = "";
    
    InputStream input;
    
    public ServerMessageHandler(client.Client client,clientinterface.ClientInterface myUI) {
        this.myClient = client;
        this.myUI=myUI;
        input = client.getInputStream();
    }
    
//    public void handleServerMessage(InputStream input){
//        this.input=input;
//        String msg;
//        byte[] byteArray;
//        try{
//            byteArray = input.readAllBytes();
//            msg = new String(byteArray);
//            myUI.update("The time is: " + msg);
//        }catch(IOException e){
//            myUI.update("Failed to read incoming message: "+e);
//        }
//    }
    
    public void handleServerMessage(InputStream input){
        this.input= input;
        byte[] byteArray;
        byte msg;
        try{
            if(input.available()==0)return;
            msg = (byte)input.read();
            if(msg != -1){
                message += (char)msg;
            }else{
                myUI.update(message);
                message = "";
            }
        }catch(IOException e) {
            myUI.update("Failed to read incoming message: "+e);
        }
    }
    
}
