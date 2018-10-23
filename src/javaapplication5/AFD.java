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
    private EstadoAFD estadoK;
    private EstadoAFD estadoAFD;
    private ArrayList<EstadoAFD> estadosV;
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
        //estadosK = new EstadoAFD(afnd);
        estadoFinal = afnd.final_state;
        estadoK = new EstadoAFD();
        estadosV = new ArrayList<EstadoAFD>();
        
        
        
        map=new HashMap<>();
        
        
        alphabet=Thompson.inputAlphabet;
        alphabet.remove(0);
        crearTablaAFD(afnd);
        
        
     }
    
    public void crearTablaAFD(AFND afnd)
    {
        estadoK = crearEstadoAFD(estadoInicial, '_');
        System.out.println("estado K: " +estadoK.getEstados().toString());
        
        for(int i=0; i < alphabet.size(); i++)
        {
            estadosV.add(new EstadoAFD());
            for(int j=0; j < estadoK.getEstados().size(); j++)
            {
                estadosV.get(i).getEstados().add(estadoK.getEstados().get(j));
                estadosV.add(i, crearEstadoAFD(estadoK.getEstados().get(j), alphabet.get(i)));
                System.out.println("estadoV " + j + ": " + estadosV.get(j).getEstados().toString());
                                
                //crearEstadoAFD(afnd.states.get(i),alphabet.get(j));
            }
            map.put(estadoK, estadosV);
            
        }
    }
    
    public EstadoAFD crearEstadoAFD(int estado, char c)
    {
        estadoAFD = new EstadoAFD();
        estados = afnd.getNextStates(afnd, estado, c);
        estadoAFD.setEstados(estados);
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
        
        return estadoAFD;
    }
    
    
}
