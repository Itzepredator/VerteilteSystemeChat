/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi.client;

import java.rmi.Naming;
import rmi.Action;
import rmi.RMIIF;

/**
 *
 * @author Florian
 */
public class ActionClient {
    
    public static void main(String[] args) {
        try {
            String url = "//127.0.0.1/Flos_ActionServer"; 
            RMIIF server = (RMIIF) Naming.lookup(url); 
            
            Action act = new Action() {
                @Override
                public Object doAction() {
                    System.out.println("Hallo Florian, oder wer auch immer diese Zeilen liest!");
                    return "RÜCKGABE";
                }
            };
            
            String s = (String) server.executeAction(act);
            System.out.println(s);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
}
