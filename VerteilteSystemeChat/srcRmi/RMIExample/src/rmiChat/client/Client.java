package rmiChat.client;

import rmiChat.common.ClientHandler;
import rmiChat.common.IRemote;
import rmiChat.common.Message;
import rmiChat.server.Server;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * Created by florian on 16.03.15.
 */
public class Client {
    private static final String DEFAULT_REMOTE = "//127.0.0.1/Flos_ActionServer";
    private static final String DEFAULT_NICKNAME = "Anonymous";

    public Client() throws RemoteException, NotBoundException, MalformedURLException {

    }

    public static void main(String[] args) {
        String rmiUrl = "rmi://" + Server.SERVER_HOST + ":" + Server.SERVER_PORT + "/";
        try {
//            String url = JOptionPane.showInputDialog("Please enter address of remote server", DEFAULT_REMOTE);
            //String nickname = JOptionPane.showInputDialog("Please enter your nickname", DEFAULT_NICKNAME);

            //String nickname = "Anonymous";

            IRemote server = (IRemote) Naming.lookup(rmiUrl + Server.SERVER_NAME);

            ClientHandler clientHandler = new ClientHandlerImpl(new Client());


            System.out.println("Nickname:");
            Scanner scn = new Scanner(System.in);
            String nickname = scn.nextLine();

            server.registerClient(clientHandler, nickname);

            String txt = scn.nextLine();


            server.sendMessage(new Message(txt, nickname), clientHandler);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onMessage(Message msg) {
        System.out.println(msg);
    }
}
