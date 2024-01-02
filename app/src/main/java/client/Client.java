package client;

import java.io.IOException;
import java.net.*;
import servermessagehandler.ServerMessageHandler;
import usercommandhandler.UserCommandHandler;

import java.io.*;

/**
 *
 * @author patel
 */
public class Client implements Runnable{
    int DEFAULT_SERVER_PORT = 7777;
    char termination = 0xFFFF;
    boolean connectServer;
    boolean[] ledChanged = {false,false,false,false};
    int ledsChanged = -1;
    
    static String message="";
    
    OutputStream output; 
    InputStream input;
    UserCommandHandler myUserCommandHandle;
    
    int serverPort = DEFAULT_SERVER_PORT;
    Socket serverSocket;
    //ServerSocket serverSocket;
    boolean stopThread = false;
    byte[] InetAddressByte = new byte[]{(byte)10,(byte)0,(byte)0,(byte)167};
    InetAddress serverAddress;
    servermessagehandler.ServerMessageHandler serverMessageHandler;
    //userinterface.StandardIO myUI;
    clientinterface.ClientInterface myUI;
    
    public Client(clientinterface.ClientInterface myUI){
        this.myUI=myUI;
//        try{
//         serverAddress= InetAddress.getLocalHost();
//        }catch(UnknownHostException e){
//            System.out.println(e);
//        }
        serverMessageHandler = new ServerMessageHandler(this,myUI);
        connectServer=false;
    }
    
    public void connectToServer(){
        try{
            serverAddress = InetAddress.getByAddress(InetAddressByte);
        }catch(UnknownHostException e){
            myUI.update(e.toString());
        }
        if(serverSocket != null){
            sendMessageToUI("Server already connected");
        }else{
            try{
                serverSocket = new Socket(serverAddress,serverPort);
                output = serverSocket.getOutputStream();
                input = serverSocket.getInputStream();
                myUI.update("Server connected");
                connectServer=false;
            }catch(IOException e){
                System.out.println(e);
            }
        }
    }
    //overloaded for convinience
    public void connectToServer(int port){
        serverPort=port;
        connectServer=true;
        //connectToServer();
    }
    public void disconnctServer(){
        try{
            serverSocket.close();
        }catch(IOException e){
            sendMessageToUI("could not disconnect server because:"+e);
        }
    }
    public void sendMessageToServer(byte message){
        try{
            output.write(message);
            output.flush();
        }catch(IOException e){
            myUI.update("Failed to send message to server: "+e);
        }
    }
    
    public void sendMessageToServer(char message){
        try{
            output.write(message);
            output.flush();
        }catch(IOException e){
            myUI.update("Failed to send message to server: "+e);
        }
    }
    
    public void sendMessageToServer(String message){
        byte msg;
        for(int i=0;i<message.length();i++){
            msg = message.getBytes()[i];
            sendMessageToServer(msg);
        }
        sendMessageToServer(termination);
    }
    public boolean isConnected(){
        return (serverSocket != null);
    }
    
    public void stopThread(){
        stopThread = true;
    }
    
    public void setPort(int portNum){
        this.serverPort = portNum;
    }

    public void setUserCommandHandler(UserCommandHandler handler){
        this.myUserCommandHandle = handler;
    }
    public int getPort(){
        return this.serverPort;
    }
    
    public void sendMessageToUI(String message){
        myUI.update(message);
    }
    
    public InputStream getInputStream(){
        return input;
    }
    
    public OutputStream getOutputStream(){
        return output;
    }
    
    public void getServerMessage(){
        if(input != null && serverSocket != null){
            System.out.println("Handle message entered");
            byte msg;
            try{
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

    public void changeLedStatus(int i){
        ledChanged[i-1] = true;
        ledsChanged=i;
    }

    public void correctLeds(){
        myUserCommandHandle.toggleLed(ledsChanged);
        ledsChanged=-1;
    }
    
    @Override
    public void run(){
        while(!stopThread){
            if (connectServer){
                connectToServer();
            }
            if (ledsChanged>0) {
                correctLeds();
            }
            System.out.println("");
            getServerMessage();
        }
    }
}
