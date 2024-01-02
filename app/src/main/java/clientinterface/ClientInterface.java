/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clientinterface;

/**
 *
 * @author chint
 */
public interface ClientInterface {
    public abstract void update(String message);
    public abstract void setCommandHandler(usercommandhandler.UserCommandHandler commandHandler);
}
