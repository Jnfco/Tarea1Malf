/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

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
    private Map<EstadoAFD, ArrayList<EstadoAFD>> map;
    private int numEstado;
    ArrayList<Integer> est;
            
    //private Map<List<Integer>,List<List<Integer>>> map;
    // constructor para afnd de 1 transición
    public AFD(AFND afnd, char c)
    {
        numEstado = 0;
        EstadoAFD e0 = new EstadoAFD();
        e0.setEstadoInicial(true);
        ArrayList<Integer> ee0 = new ArrayList<Integer>();
        ee0.add(0);
        e0.setEstados(ee0);
        e0.setNumEstado(0);
        
        
        EstadoAFD e1 = new EstadoAFD();
        e1.setEstadoFinal(true);
        ArrayList<Integer> ee1 = new ArrayList<Integer>();
        ee1.add(1);
        e1.setEstados(ee1);
        e1.setNumEstado(1);
        
        ArrayList<EstadoAFD> ae = new ArrayList<EstadoAFD>();
        ae.add(e1);
        
        map = new HashMap<EstadoAFD, ArrayList<EstadoAFD>>();
        map.put(e0, ae);
        
        displayAFD2();
    }
    
    public AFD(AFND afnd)
    {
        super();
        numEstado = 0;
        this.afnd = afnd;
        transitions = afnd.transitions;
        estadoInicial = afnd.getStart();
        //estadosK = new EstadoAFD(afnd);
        estadoFinal = afnd.final_state;
        estadoK = new EstadoAFD();
        estadosV = new ArrayList<EstadoAFD>();
        estados=new ArrayList<>();
        
        
        map=new HashMap<EstadoAFD, ArrayList<EstadoAFD>>();
        
        
        alphabet=Thompson.inputAlphabet;
        alphabet.remove(0);
        crearTablaAFD(afnd);
        
        displayAFD();
        
        // intento de grafo by nico
        graph();
        
        
        System.out.println("Fin del programa!");
       
        
        
        
    }
    
    public void crearTablaAFD(AFND afnd)
    {
        // ------------- todo esto es para el nodo inicial del AFD -------------
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
        estadoK.setNumEstado(numEstado);
        numEstado++;
        
        //----------------------------------------------------------------------
        
        crearMap(estadoK);
        
        for (int i = 0; i < map.values().size(); i++)
        {
            for(int j=0;j<estadosV.size();j++)
            {
                if(!map.keySet().contains(map.values().iterator().next()))
                {
                    estadoK = new EstadoAFD();
                    estadoK.setEstados(estadosV.get(j).getEstados());
                    estadoK.setNumEstado(estadosV.get(j).getNumEstado());
                    if(estadoK.getEstados().contains(afnd.final_state))       
                    {
                        estadoK.setEstadoFinal(true);
                    }
                    if(estadoK.getEstados().contains(afnd.start_state))       
                    {
                        estadoK.setEstadoInicial(true);
                    }
                    //estadoK.setNumEstado(numEstado++);//---------------------------------------------------------------
                    crearMap(estadoK);
                }
            }  
        }
        
        
        // agregamos el sumidero al map
        boolean hayNull = false;
        int numEstadoSumidero = -1;
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            if(entry.getKey().getEstados().isEmpty())
            {
                numEstadoSumidero = entry.getKey().getNumEstado();
                hayNull = true;
                break;
            }
            else
            {
                for(int i = 0; i < alphabet.size(); i++)
                {
                    if(entry.getValue().get(i).getEstados().isEmpty())
                    {
                        numEstadoSumidero = entry.getValue().get(i).getNumEstado();
                        hayNull = true;
                        break;
                    }
                }
            }
        }
        if(hayNull)
        {
            EstadoAFD sumidero= new EstadoAFD();
            ArrayList<Integer> listaSumidero = new ArrayList<>();
            ArrayList<EstadoAFD> valuesSumidero = new ArrayList<>();
            sumidero.setEstados(listaSumidero);
            for (Character alphabet1 : alphabet)
            {
                valuesSumidero.add(sumidero);
            }
            //sumidero.setNumEstado(numEstado++);
            sumidero.setNumEstado(numEstadoSumidero);
            //numEstado = numEstadoSumidero + 1;
            map.put(sumidero, valuesSumidero);
        }
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
            boolean flag = false;
            EstadoAFD e = new EstadoAFD();
            list = new ArrayList<>();
            
            for(int j = 0; j < estadoK.getEstados().size(); j++)
            {
                list = getNextStates(afnd, estadoK.getEstados().get(j), alphabet.get(i));
                Collections.sort(list);
                e.setEstados(list);

                if(list.contains(estadoFinal))
                {
                    e.setEstadoFinal(true);
                }
                
                if(!map.isEmpty())
                {
                    for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
                    {
                        if(entry.getKey().getEstados().equals(e.getEstados()))
                        {
                            e.setNumEstado(entry.getKey().getNumEstado());
                            flag = true;
                            break;
                        }
                        else
                        {
                            for(int k = 0; k < alphabet.size(); k++)
                            {
                                if((entry.getValue().get(k).getEstados().equals(e.getEstados())) || 
                                        (entry.getValue().get(k).getEstados().isEmpty() && e.getEstados().isEmpty()))
                                {
                                    e.setNumEstado(entry.getValue().get(k).getNumEstado());
                                    flag = true;
                                    break;
                                }
                            }
                        }
                    }    
                }
            }
            
            if(!flag)
            {
                e.setNumEstado(numEstado);
                numEstado++;
            }
            estadosV.add(e);
            afnd.limpiarArrayNextStates(afnd);
        }
        map.put(estadoK, estadosV);
    }
    
    public void displayAFD()
    {
        System.out.println("AFD");
        
        est = new ArrayList<>();
        System.out.print("K={");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            est.add(entry.getKey().getNumEstado());
        }
        Collections.sort(est);
        for(int i = 0; i < est.size(); i++)
        {
            if(est.get(i) < est.size() - 1)
            {
                System.out.print("q" + est.get(i) + ",");
            }
            else
            {
                System.out.print("q" + est.get(i) + "}");
            }
        }
        System.out.println("");
        
        
        System.out.println("delta:");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            for(int i = 0; i < alphabet.size(); i++)
            {
                
                System.out.println("(q" + entry.getKey().getNumEstado() + "," + 
                        alphabet.get(i) + 
                        ",q" + entry.getValue().get(i).getNumEstado() + ")");
            }
        }
        
        System.out.print("s=q");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            if(entry.getKey().isEstadoInicial())
            {
                System.out.println("" + entry.getKey().getNumEstado());
            }
        }
        
        System.out.print("");
        
        System.out.print("F={");
        est = new ArrayList<>();
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            if(entry.getKey().isEstadoFinal())
            {
                est.add(entry.getKey().getNumEstado());
            }
        }
        Collections.sort(est);
        for(int i = 0; i < est.size(); i++)
        {
            if(est.get(i) < est.size())
            {
                System.out.print("q" + est.get(i) + ",");
            }
            else
            {
                System.out.print("q" + est.get(i) + "}");
            }
        }
        System.out.println("");
        
    }
    
    public void displayAFD2()
    {
        System.out.println("AFD");
        
        est = new ArrayList<>();
        System.out.print("K={");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            est.add(entry.getKey().getNumEstado());
        }
        Collections.sort(est);
        for(Integer i : est)
        {
            if(est.get(i) < est.size() - 1)
            {
                System.out.print("q" + i + ",");
            }
            else
            {
                System.out.print("q" + i + "}");
            }
        }
        System.out.println("");
        
        
        System.out.println("delta:");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {                
            System.out.println("(q" + entry.getKey().getNumEstado() + "," + 
                    Thompson.inputAlphabet.get(0) + 
                    ",q" + entry.getValue().get(0).getNumEstado() + ")");            
        }
        
        System.out.print("s=q");
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            if(entry.getKey().isEstadoInicial())
            {
                System.out.println("" + entry.getKey().getNumEstado());
            }
        }
        
        System.out.print("");
        
        System.out.print("F={");
        est = new ArrayList<>();
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            if(entry.getValue().get(0).isEstadoFinal())
            {
                est.add(entry.getValue().get(0).getNumEstado());
            }
        }
        Collections.sort(est);
        for(int i = 0; i < est.size(); i++)
        {
            if(est.get(i) < est.size())
            {
                System.out.print("q" + est.get(i) + ",");
            }
            else
            {
                System.out.print("q" + est.get(i) + "}");
            }
        }
        System.out.println("");
    }
            
            
    public int getNumEstadoSumidero()
    {
        est = new ArrayList<>();
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            est.add(entry.getKey().getNumEstado());
        }
        Collections.sort(est);
        return est.size();
    }
    
    public void graph()
    {
        HashMap<Integer,LinkedList<Integer>> map2 = new HashMap<>();
        //LinkedList<EstadoAFD> l = new LinkedList<>();
        for(Map.Entry<EstadoAFD, ArrayList<EstadoAFD>> entry : map.entrySet())
        {
            for(int i = 0; i < alphabet.size(); i++)
            {
                if(!map2.containsKey(entry.getKey()))
                {
                    LinkedList<Integer> l= new LinkedList<>();
                    l.add(entry.getValue().get(i).getNumEstado());
                    map2.put(entry.getKey().getNumEstado(), l);
                }

                else
                {
                    LinkedList<Integer> l = map2.get(entry.getKey());
                    l.add(entry.getValue().get(i).getNumEstado());
                    map2.put(entry.getKey().getNumEstado(), l);
                }
            }
        }
        
        for(Map.Entry m: map2.entrySet())
        {
            System.out.println(m.getKey()+"-->"+m.getValue());
        }
        
    }
    
}
