/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import rmi.Action;
import rmi.RMIIF;

/**
 *
 * @author Florian
 */
public class ActionServer extends UnicastRemoteObject implements RMIIF{
    
    public static void main(String[] args) {
        try {
            //LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
            Naming.rebind("Flos_ActionServer", new ActionServer()); 
            System.out.println("ActionServer als Server registriert!");             
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public ActionServer() throws RemoteException {
        super();
    }
    
    @Override
    public Object executeAction(Action action) {
        return action.doAction();
    }
    
}
