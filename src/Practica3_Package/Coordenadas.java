/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import java.io.Serializable;


/**
 *
 * @author jordi
 */
public class Coordenadas implements Serializable{
    private int fila;
    private int columna;

    public Coordenadas(int fila, int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
    
    public void Show(){
        System.out.println("Fila: " + getFila() + " Columna: " + getColumna() + "\n");
    }
    
    public Boolean equals(Coordenadas pos){
        return this.fila == pos.getFila() && this.columna == pos.getColumna();
    }
}
