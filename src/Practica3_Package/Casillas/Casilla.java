/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Practica3_Package.Casillas;

import Practica3_Package.Coordenadas;
import Practica3_Package.Mapa;
import Practica3_Package.Sensores;

/**
 *
 * @author jordi
 */
public abstract class Casilla {
    protected Sensores sensor;
    protected double multiplicador;
    
    public Casilla(Mapa mapa) {
        this.sensor = new Sensores(mapa);
        this.multiplicador = 0.75;
    }
   
   
   public abstract double calcularDistancia(Coordenadas a, Coordenadas b, int[][] mapaVeces);
   public abstract boolean see(Coordenadas a);
   public abstract Coordenadas getPosicion(Coordenadas a);
}
