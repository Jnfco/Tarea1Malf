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
import java.util.Map;
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
        estados=new ArrayList<>();
        
        
        map=new HashMap<>();
        
        
        alphabet=Thompson.inputAlphabet;
        alphabet.remove(0);
        crearTablaAFD(afnd);
        AFDArrayList= new ArrayList<>();
        AFDTransiciones= new ArrayList<>();
        AFDArrayList.add(0);
        for(int i =0;i<afnd.states.size();i++)
        {
            for(int j=0;j<alphabet.size();j++)
            {
                CrearAFD(afnd.states.get(i),alphabet.get(j));
                CrearAFD(afnd.states.get(i),'_');
            }
        }
       
        boolean rep=false;
        for(int i =0;i<afnd.transitions.size();i++)
        {
            for(int j=0;j<AFDArrayList.size();j++)
            {
                if(afnd.transitions.get(i).state_to == AFDArrayList.get(j))
                {
                    rep=true;
                }
            }
            if(rep==false)
            {                
                AFDArrayList.add(afnd.transitions.get(i).state_to);
                System.out.println("Vamos a agregar al AFD: "+AFDArrayList.get(i));                
            }
            //System.out.println("q"+AFD.get(i));
            
        }
        
        
    }
    
    public void crearTablaAFD(AFND afnd)
    {
        // ------------------- todo esto es para el nodo inicial del AFD
        estadoK = new EstadoAFD();
        ArrayList<Integer> estados2 = new ArrayList<>();
        estados2 = afnd.getNextStatesEPS(afnd, afnd.start_state);
        estados2.add(afnd.start_state);
        Collections.sort(estados2);
        estadoK.setEstados(estados2);
        estadoK.setEstadoInicial(true);
        if(estados.contains(afnd.final_state))       
        {
            estadoK.setEstadoFinal(true);
        }
        
        afnd.limpiarArrayNextStates(afnd);
        //-------------------------------------------------------------
        // Ahora para los nodos siguientes
        ArrayList<Integer> list; 
        for(int i = 0; i < alphabet.size(); i++)
        {
            EstadoAFD e = new EstadoAFD();
            list = new ArrayList<Integer>();
            
            for(int j = 0; j < estadoK.getEstados().size(); j++)
            {
                list = getNextStates(afnd, estadoK.getEstados().get(j), alphabet.get(i));
                Collections.sort(list);
                e.setEstados(list);
                
                //estadosV.add(i,crearEstadoAFD(estadoK.getEstados().get(j), alphabet.get(i)));
                //System.out.println("estadoV " + j + ": " + estadosV.get(j).getEstados().toString());             
                //crearEstadoAFD(afnd.states.get(i),alphabet.get(j));
            }
            System.out.println("list: " + list.toString());
            estadosV.add(e);
            afnd.limpiarArrayNextStates(afnd);
        }
        map.put(estadoK, estadosV);
        
    }
    
    public EstadoAFD crearEstadoAFD(int estado, char c)
    {
        estadoAFD = new EstadoAFD();
        estados = afnd.getNextStates(afnd, estado, c);
        estadoAFD.setEstados(estados);
        System.out.println("Estadoss:"+estados.toString());
        //Collections.sort(estados);
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
