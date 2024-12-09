/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package.Casillas;

import Practica3_Package.Coordenadas;
import Practica3_Package.Mapa;

/**
 *
 * @author jordi
 */
public class Arriba extends Casilla {

    public Arriba(Mapa mapa) {
        super(mapa);
    }

    @Override
    public double calcularDistancia(Coordenadas a, Coordenadas b, int[][] mapaVeces) {
        Coordenadas a2 = new Coordenadas(a.getFila() - 1, a.getColumna());
        return Math.sqrt(Math.pow(a2.getFila() - b.getFila(), 2) + Math.pow(a2.getColumna() - b.getColumna(), 2)) + mapaVeces[a2.getFila()][a2.getColumna()] * multiplicador;    
    }

    @Override
    public boolean see(Coordenadas a) {
        Coordenadas c = new Coordenadas(a.getFila() - 1, a.getColumna());
        return this.sensor.comprobar(c);
    }

    @Override
    public Coordenadas getPosicion(Coordenadas a) {
        return new Coordenadas(a.getFila() - 1, a.getColumna());
    }

}
