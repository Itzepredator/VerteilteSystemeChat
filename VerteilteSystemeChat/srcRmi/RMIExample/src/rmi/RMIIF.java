/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Florian
 */
public interface RMIIF extends Remote {
    
    public Object executeAction(Action action) throws RemoteException;
    
}
