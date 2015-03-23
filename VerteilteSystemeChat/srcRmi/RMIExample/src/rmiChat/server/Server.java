package rmiChat.server;

import rmiChat.common.ClientHandler;
import rmiChat.common.IRemote;
import rmiChat.common.Message;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by florian on 16.03.15.
 */
public class Server extends UnicastRemoteObject implements IRemote {
    public static final String SERVER_NAME = "chatServer";
    private ArrayList<ClientConnection> clients;
    public static final int SERVER_PORT = 50000;
    public static final String SERVER_HOST = "127.0.0.1";

    public static void main(String[] args) {
        try {
            try {
                try {
                    LocateRegistry.createRegistry(50000);
                } catch (RemoteException e) {
                    LocateRegistry.getRegistry(50000);
                    e.printStackTrace();
                }
                System.setProperty("java.rmi.server.hostname", SERVER_HOST);

                String rmiUrl = "rmi://" + SERVER_HOST + ":" + SERVER_PORT + "/";
                Naming.rebind(rmiUrl + SERVER_NAME, new Server());

                System.out.println("Server successfully started!");
            } catch (RemoteException e1) {
                e1.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected Server() throws RemoteException {
        super();

        this.clients = new ArrayList<ClientConnection>();
    }

    @Override
    public void registerClient(ClientHandler handler, String nickname) {
        this.clients.add(new ClientConnection(handler, nickname));
    }

    private void propagateMessage(Message msg, ClientHandler sender) {
        for (ClientConnection client : this.clients) {
            if (client.getNickname().equals(msg.getNickname())) {
                continue;
            }

            try {
                client.getHandler().handleMessage(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendMessage(Message msg, ClientHandler sender) {
        this.propagateMessage(msg, sender);

        System.out.println(msg);
    }
}
