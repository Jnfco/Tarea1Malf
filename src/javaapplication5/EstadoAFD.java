/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication5;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 *
 * @author jnfco
 */
public class EstadoAFD {
    
    private boolean estadoInicial;
    private boolean estadoFinal;
    private ArrayList<Integer>estados;

    public EstadoAFD(AFND afnd) {
        
        this.estados= new ArrayList<>();
        this.estadoInicial=false;
        this.estadoFinal=false;
    }

    public boolean isEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(boolean estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public boolean isEstadoFinal() {
        return estadoFinal;
    }

    public void setEstadoFinal(boolean estadoFinal) {
        this.estadoFinal = estadoFinal;
    }

    public ArrayList<Integer> getEstados() {
        return estados;
    }

    public void setEstados(ArrayList<Integer> estados) {
        this.estados = estados;
    }
    public void addEstado(int estado)
    {
        this.estados.add(estado);
    }
    
    
   
    
    @Override
    public boolean equals(Object o) 
    {
     if (o instanceof EstadoAFD) 
    {
        EstadoAFD eafd = (EstadoAFD)o;
        return this.estados.equals(eafd.estados);
    } 
     else 
     {
        return false;
    }
}
    
    @Override
    public int hashCode() {
    return  estados.toString().length()* this.estados.size();
}
    
}
