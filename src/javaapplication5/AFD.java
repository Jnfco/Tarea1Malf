/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.util.ArrayList;
import java.util.Collections;
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
    private EstadoAFD estadoAFD;
    private ArrayList<EstadoAFD> estadosAFD;
    private ArrayList<Integer> estados;
    private AFND afnd;
    private int estadoInicial;
    private int estadoFinal;
    private Map map;
            
    //private Map<List<Integer>,List<List<Integer>>> map;
    
    public AFD(){}
    
    public AFD(AFND afnd)
    {
        super();
        this.afnd = afnd;
        transitions = afnd.transitions;
        estadoInicial = afnd.getStart();
        estadoAFD = new EstadoAFD(afnd);
        estadoFinal = afnd.final_state;
        
        
        map=new HashMap<>();
        
        
        alphabet=Thompson.inputAlphabet;
        crearTablaAFD(afnd);
        System.out.println("EstadoAFD:"+estadoAFD.getEstados());
        System.out.println("Estados:");
        for(int i=0;i<estados.size();i++)
        {
            System.out.println(estados.get(i));
        }
        
     }
    
    public void crearTablaAFD(AFND afnd)
    {
        estados=afnd.getNextStates(afnd,estadoInicial,'_');
        for(int i=0;i<afnd.states.size();i++)
        {
            for(int j=0;j<alphabet.size();j++)
            {
                
                System.out.println("Alphabet:"+alphabet.get(j));
                
                agregarEstadoAFD(afnd.states.get(i),alphabet.get(j));
            }

        }
    }
    
    public void agregarEstadoAFD(int estado, char c)
    {
        estadoAFD = new EstadoAFD(afnd);
        estados = afnd.getNextStates(afnd, estado, c);
        System.out.println("Estadoss:"+estados.toString());
        Collections.sort(estados);
        if(estados.contains(estadoInicial))
        {
            estadoAFD.setEstadoInicial(true);
        }
        
        if(estados.contains(estadoFinal))
        {
            estadoAFD.setEstadoFinal(true);
        }
    }
    
    
}
