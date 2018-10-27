/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
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
    private ArrayList<Integer>AFDArrayList;
    private ArrayList<Trans>AFDTransiciones;
            
    //private Map<List<Integer>,List<List<Integer>>> map;
    
    public AFD(AFND afnd, char c)
    {
        EstadoAFD e0 = new EstadoAFD();
        e0.setEstadoInicial(true);
        ArrayList<Integer> ee0 = new ArrayList<Integer>();
        ee0.add(0);
        e0.setEstados(ee0);
        
        
        EstadoAFD e1 = new EstadoAFD();
        e1.setEstadoFinal(true);
        ArrayList<Integer> ee1 = new ArrayList<Integer>();
        ee1.add(1);
        e1.setEstados(ee1);
        
        ArrayList<EstadoAFD> ae = new ArrayList<EstadoAFD>();
        ae.add(e1);
        
        map = new HashMap<>();
        map.put(e0, ae);
    }
    
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
        estados=new ArrayList<>();
        
        
        map=new HashMap<>();
        
        
        alphabet=Thompson.inputAlphabet;
        alphabet.remove(0);
        crearTablaAFD(afnd);
        
        System.out.println("Fin del programa!");
       
        
        
    }
    
    public void crearTablaAFD(AFND afnd)
    {
        // ------------------- todo esto es para el nodo inicial del AFD -------
        estadoK = new EstadoAFD();
        ArrayList<Integer> estados2 = new ArrayList<>();
        estados2.add(afnd.start_state);
        
        if(tieneTransicionVacio(afnd))
        {
            estados2 = afnd.getNextStatesEPS(afnd, afnd.start_state);  
        }
        
        
        
        Collections.sort(estados2);
        estadoK.setEstados(estados2);
        estadoK.setEstadoInicial(true);
        if(estadoK.getEstados().contains(afnd.final_state))       
        {
            estadoK.setEstadoFinal(true);
        }
        //----------------------------------------------------------------------
        
        crearMap(estadoK);
        // arreglar esto vvvvvvvvvvvvvvvvvvvvvvvvvv
        for (int i = 0; i < map.values().size(); i++)
        {
            for(int j=0;j<estadosV.size();j++)
            {
                if(!map.keySet().contains(map.values().iterator().next()))
                {
                    estadoK = new EstadoAFD();
                    estadoK.setEstados(estadosV.get(j).getEstados());// El problema lo causa el map, ya que este tiene menos elementos que estados V
                    
                     if(estadoK.getEstados().contains(afnd.final_state))       
                    {
                        estadoK.setEstadoFinal(true);
                    }
                      if(estadoK.getEstados().contains(afnd.start_state))       
                    {
                        estadoK.setEstadoInicial(true);
                    }
                    crearMap(estadoK);
                
                }
            }  
        }
        
        /*
        // agregamos el sumidero al map
        EstadoAFD sumidero= new EstadoAFD();
        ArrayList<Integer> listaSumidero = new ArrayList<>();
        ArrayList<EstadoAFD> valuesSumidero = new ArrayList<>();
        sumidero.setEstados(listaSumidero);
        for (Character alphabet1 : alphabet)
        {
            valuesSumidero.add(sumidero);
        }
        map.put(sumidero, valuesSumidero);
        */
    }
    
    public void crearMap(EstadoAFD estadoK)
    {
        afnd.limpiarArrayNextStates(afnd);
        //-------------------------------------------------------------
        // Ahora para los nodos siguientes
        ArrayList<Integer> list; 
        estadosV = new ArrayList<EstadoAFD>();
        for(int i = 0; i < alphabet.size(); i++)
        {
            EstadoAFD e = new EstadoAFD();
            list = new ArrayList<Integer>();
            
            for(int j = 0; j < estadoK.getEstados().size(); j++)
            {
                list = getNextStates(afnd, estadoK.getEstados().get(j), alphabet.get(i));
                Collections.sort(list);
                e.setEstados(list);

                if(list.contains(estadoFinal))
                {
                    e.setEstadoFinal(true);
                }
                
                //estadosV.add(i,crearEstadoAFD(estadoK.getEstados().get(j), alphabet.get(i)));
                //System.out.println("estadoV " + j + ": " + estadosV.get(j).getEstados().toString());             
                //crearEstadoAFD(afnd.states.get(i),alphabet.get(j));
            }
            //System.out.println("list: " + list.toString());
            estadosV.add(e);
            afnd.limpiarArrayNextStates(afnd);
        }
        map.put(estadoK, estadosV);
    }
    
    
    public void CrearAFD(int estado,Character c)
    {
        for(int i =0;i<afnd.transitions.size();i++)
        {
            if(afnd.transitions.get(i).state_from ==estado && afnd.transitions.get(i).trans_symbol ==c)
            {

                AFDArrayList.add(transitions.get(i).state_to);
                AFDTransiciones.add(transitions.get(i));
                CrearAFD(afnd.transitions.get(i).state_to,c);        
                //System.out.println("q"+AFD.get(i));
            }
        }
    }
}
