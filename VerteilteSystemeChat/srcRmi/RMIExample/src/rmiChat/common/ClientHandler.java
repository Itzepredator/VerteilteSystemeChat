package rmiChat.common;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by florian on 16.03.15.
 */
public interface ClientHandler extends Remote {

    public void handleMessage(Message msg) throws RemoteException;

}
