package rmiChat.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by florian on 16.03.15.
 */
public interface IRemote extends Remote {

    public void registerClient(ClientHandler handler, String nickname) throws RemoteException;

    public void sendMessage(Message msg, ClientHandler sender) throws RemoteException;

}
