package usercommandhandler;

/**
 *
 * @author patel
 */
public class UserCommandHandler implements Runnable{

    client.Client myClient;
    clientinterface.ClientInterface myUI;
    boolean ledChanged = false;
    int ledChangedNum =-1;
    boolean[] ledStatus = {false,false,false,false};
    
    public UserCommandHandler(client.Client myClient,clientinterface.ClientInterface myUI){
        this.myClient = myClient;
        this.myUI = myUI;
    }
    
    public void handleUserCommand(String command){
        byte msg;
        if(command.equals("2")){
            myUI.update("Connecting to server");
            myClient.connectToServer(7777);
        }else if(command.equals("t")){
            msg = command.getBytes()[0];
            myClient.sendMessageToServer(msg);
        }
    }
    
    public void toggleLed(int num){
//        if(ledStatus[num-1]){
//            ledOff(num);
//            ledStatus[num-1]=false;
//        }else{
//            ledOn(num);
//            ledStatus[num-1]=true;
//        }
        ledChanged = true;
        ledChangedNum=num;
    }
    
    public void ledOn(int num){
        ledStatus[num-1]=true;
        String message = "L"+num+"on";
        myClient.sendMessageToServer(message);
    }
    
    public void ledOff(int num){
        ledStatus[num-1]=false;
        String message = "L"+num+"off";
        myClient.sendMessageToServer(message);
    }
    
    public void getPushButton(int num){
        String message = "gpb"+num;
        myClient.sendMessageToServer(message);
    }
    
    public void connectServer(int port){
        myClient.connectToServer(port);
    }

    public void updateLeds(){
        if(ledStatus[ledChangedNum-1]){
            ledOff(ledChangedNum);
        }else {
            ledOn(ledChangedNum);
        }
        ledChanged=false;
    }

    public void run(){
        while(true){
            if(ledChanged) {
                updateLeds();
            }
        }
    }
}
