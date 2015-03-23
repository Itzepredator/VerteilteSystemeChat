/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmi;

import java.io.Serializable;

/**
 *
 * @author Florian
 */
public abstract class Action implements Serializable {
    
    public abstract Object doAction();
    
}
