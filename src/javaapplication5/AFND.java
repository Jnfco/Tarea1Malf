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
public class AFND {
      
    public ArrayList <Integer> states; // conjunto de estados
    public ArrayList <Trans> transitions; // Conjunto de transiciones
    public int final_state; // Estado final del automata
    public int start_state; // Estado inicial del automata
    public static ArrayList<Integer>AFD;
    public static ArrayList<Trans>AFDT;
    private ArrayList<Integer>nextStates;

    public AFND(){
        this.states = new ArrayList <Integer> (); //Crear el arreglo de enteros para los estados
        this.transitions = new ArrayList <Trans> (); //Crear un arreglo de tipo transicion
        this.final_state = 0;// el estado final inicial es 0
        this.start_state = 0; // el estado inicial es 0
       
    }
    public AFND(int size){
        this.states = new ArrayList <Integer> ();
        this.transitions = new ArrayList <Trans> ();
        this.final_state = 0;
        this.setStateSize(size);// Asignar la cantidad de estados
         this.nextStates=new ArrayList<>();
    }
    public AFND(char c){
        this.states = new ArrayList<Integer> ();
        this.transitions = new ArrayList <Trans> ();
        this.setStateSize(2);
        this.final_state = 1;
        this.transitions.add(new Trans(0, 1, c));// crea la transicion del 0 al 1 usando el caracter c
    }

    public void setStateSize(int size){ // metodo que llena el arreglo de estados con la cantidad indicada
        for (int i = 0; i < size; i++)
            this.states.add(i);
    }

    public int getStart()
    {
        return start_state;
    }

    public void addState(int state)
    {
        states.add(state);
    }

    public ArrayList<Integer> getNextStatesEPS(AFND afnd, int from)
    {
        for(int i = 0; i < afnd.transitions.size(); i++)
        {
            if (afnd.transitions.get(i).state_from == from && afnd.transitions.get(i).trans_symbol == '_')
            {
                afnd.nextStates.add(afnd.transitions.get(i).state_to);
                getNextStatesEPS(afnd, afnd.transitions.get(i).state_to);
            }
        }
        
        return afnd.nextStates;
    }
    
     public ArrayList<Integer> getNextStates(AFND afnd, int from, char c)
    {
        for(int i = 0; i < afnd.transitions.size(); i++)
        {
            if (afnd.transitions.get(i).state_from == from && afnd.transitions.get(i).trans_symbol == c)
            {
                //afnd.nextStates.add(afnd.transitions.get(i).state_from);
                afnd.nextStates.add(afnd.transitions.get(i).state_to);
                getNextStatesEPS(afnd, afnd.transitions.get(i).state_to);
            }
        }
        
        return afnd.nextStates;
    }

    public void AFD(int estado)
    {
        for(int i =0;i<transitions.size();i++)
        {
            if(transitions.get(i).state_from ==estado && transitions.get(i).trans_symbol =='_')
            {

                AFD.add(transitions.get(i).state_to);
                AFDT.add(transitions.get(i));
                AFD(transitions.get(i).state_to);        
                //System.out.println("q"+AFD.get(i));
            }
        }
    }
    public void display(){ // Metodo que imprime las transiciones del automata convertido

        String estado="";
        for(Integer i: states)
        {
            
            estado=estado+"q"+i+",";

        }

        System.out.println("AFND:");
        System.out.println("K={"+estado+"}");
        System.out.println("Sigma=");
        System.out.println("Delta:");
        for (Trans t: transitions){
            System.out.println("("+"q"+t.state_from +", "+ t.trans_symbol +
                ", "+"q"+ t.state_to +")");
        }
        System.out.println("s="+"q"+states.get(0));
        System.out.println("F="+"q"+states.get(states.size()-1));
        System.out.println("Probando AFD: ");

        this.AFD= new ArrayList<Integer>();
        this.AFDT= new ArrayList<Trans>();
        AFD.add(0);
        AFD(0);
        boolean rep=false;
        //aqui en este for que viene lo hice para recorrer las transicions y probar con el estado inicial y sus transiciones espiron

        for(int i =0;i<transitions.size();i++)
        {
            if(transitions.get(i).state_from ==0 && transitions.get(i).trans_symbol =='_')
            {
                for(int j=0;j<AFD.size();j++)
                {
                    if(transitions.get(i).state_to == AFD.get(j))
                    {
                        rep=true;
                    }
                }
                if(rep==false)
                {                
                    AFD.add(transitions.get(i).state_to);
                    System.out.println("Vamos a agregar al AFD: "+AFD.get(i));                
                }
                //System.out.println("q"+AFD.get(i));
            }
        }
        
       

    }    
    
    public void limpiarArrayNextStates(AFND afnd)
    {
        afnd.nextStates = new ArrayList<Integer>();
    }
}
