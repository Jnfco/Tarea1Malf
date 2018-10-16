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
import java.util.Queue;

/**
 *
 * @author Nicolas
 */
public class AFD extends AFND
{
    public AFD(AFND afnd)
    {
        super();
        ArrayList<Trans> transitions = afnd.transitions;
        
        HashMap<ArrayList<Integer>, Integer> map = new HashMap<ArrayList<Integer>, Integer>();
        
        ArrayList<Integer> startSet = new ArrayList<Integer>();
	startSet.add(afnd.getStart());
	startSet.addAll(statesReachableFrom(afnd.getStart()));

	//setStart(startSet.toState(parser));
	map.put(startSet, getStart());

	Queue<ArrayList<Integer>> queue = new LinkedList<ArrayList<Integer>>();
        queue.offer(startSet);
        
        ArrayList<Character> inputAlphabet = new ArrayList<Character>();
        inputAlphabet = Thompson.inputAlphabet;
        /*
        for (int k = 0; k < inputAlphabet.size(); k++)
        {
            System.out.println("alfabeto: "+inputAlphabet.get(k));
        }
        */
        while(!queue.isEmpty()) 
        {
            ArrayList<Integer> set = queue.poll();
            System.out.println("set inicial: "+set.toString());

            for (int i = 0; i < inputAlphabet.size(); i++) 
            {
                System.out.println("letra: " + inputAlphabet.get(i));
                System.out.println("trasitions: " + transitions.size());
                // transiciones que van desde el estado set por la transicion i
                for (Trans t : transitions)
                {
                    System.out.println("tamos dentro del for");
                    ArrayList<Integer> trans = new ArrayList<>();
                    if(t.state_from == set.get(set.indexOf(t)) && t.trans_symbol == inputAlphabet.get(i))
                    {
                        System.out.println("se agrega state to: "+t.state_to);
                        trans.add(t.state_to);
                    }
                    
                    int to = -1;
                    if(!map.containsKey(trans))
                    {
                        System.out.println("if mapa no contiene trans");
                        to = trans.get(i);
                        map.put(trans, to);
                        if (!queue.contains(trans)) queue.offer(trans); System.out.println("fila no contiene trans");
                    }
                    else { to = map.get(trans); }
                    
                    int from = map.get(set);
                    System.out.println("from: " + from);
                    addState(from);
                    addState(to);
                    transitions.add( new Trans(from, to, inputAlphabet.get(i)) );
                }
            }
        }        
        
        for (ArrayList<Integer> name: map.keySet())
        {
            String key =name.toString();
            String value = map.get(name).toString();
            System.out.println("clave: " + key + "\nvalor: " + value);
        }
    }
    
    // Which states can be reached on a certain character?
    public HashSet<Integer> statesReachableOn(int from, Character on) 
    {
        HashSet<Integer> reachable = new HashSet<Integer>();
        Iterator<Trans> iter = transitions.iterator();
        while (iter.hasNext()) 
        {
            Trans t = iter.next();

            // Do they want the epsilon transitions?
            boolean equals = false;
            if (on == '_')
                equals = on == t.trans_symbol;
            else
                equals = on.equals(t.trans_symbol);

            // Add all matching transitions
            // Skip if already added to prevent loops
            if (equals  && !reachable.contains(t.state_to)) 
            {
                // Add this state
                reachable.add(t.state_to);

                // Recurse and add all reachable from this state (epsilon transitions)
                reachable.addAll(statesReachableFrom(t.state_to));
            }
        }
        return reachable;
    }
    
    public HashSet<Integer> statesReachableFrom(int from)
    {
        return statesReachableOn(from, '_');
    }
    
}
