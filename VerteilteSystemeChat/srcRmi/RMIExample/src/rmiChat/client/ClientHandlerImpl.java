package rmiChat.client;

import rmiChat.common.ClientHandler;
import rmiChat.common.Message;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by florian on 16.03.15.
 */
public class ClientHandlerImpl extends UnicastRemoteObject implements ClientHandler {
    private Client client;

    public ClientHandlerImpl(Client client) throws RemoteException {
        this.client = client;
    }

    @Override
    public void handleMessage(Message msg) {
        client.onMessage(msg);
    }
}
