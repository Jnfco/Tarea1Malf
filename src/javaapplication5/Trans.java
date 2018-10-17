/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.util.ArrayList;

/**
 *
 * @author jnfco
 */
public class Trans {
        
    public int state_from; // Estado inicial
    public int state_to; // Estado final
    public char trans_symbol; // Simbolo de transicion

    public Trans(int v1, int v2, char sym)
    {
        this.state_from = v1;
        this.state_to = v2;
        this.trans_symbol = sym;
    }
    
    public ArrayList<Integer> getAllTransitions(char c, AFND afnd) 
    {
        if (this.state_from == afnd.final_state)
        {
            return new ArrayList<Integer> ();	
        }
        else
        {	
            return this.
        }
    }
    
    
}
