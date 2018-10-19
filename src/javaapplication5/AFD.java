/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import static javaapplication5.Thompson.alphabet;

/**
 *
 * @author Nicolas
 */
public class AFD extends AFND
{
    private ArrayList<Character>alphabet;
    private ArrayList<Trans>transitions;
    private Map<List<Integer>,List<List<Integer>>> map;
    
    public AFD(){}
    
    public AFD(AFND afnd)
    {
        super();
        transitions = afnd.transitions;
        int estadoInicial = afnd.getStart();
        map=new HashMap<>();
        alphabet=Thompson.inputAlphabet;
        crearTablaAFD(afnd);
        
     }
    
    public void crearTablaAFD(AFND afnd)
    {
        for(int i=0;i<transitions.size();i++)
        {
            for(int j=0;j<alphabet.size();j++)
            {
                
            }
        }
    }
}
