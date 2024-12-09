/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import java.io.IOException;

/**
 *
 * @author jordi
 */
public class Entorno {
    private Coordenadas posicion;
    private Mapa mapa;
    private Sensores sensores;
    private String [][] mapaVisual;
    private int[][] mapaVeces;
    private InterfazMapa mapaInterfaz;
    @SuppressWarnings("empty-statement")
    public Entorno(Mapa mapa, Buscador buscador) throws IOException {
        this.mapa = mapa;
        
        //Inicializo el mapaVisual
        mapaVisual = mapa.obtenerCopiaParaVisualizacion();
        mapaVeces= mapa.obtenerCopiaMismoTamano();
        mapaInterfaz = new InterfazMapa(mapa.getFilas(), mapa.getColumnas(), buscador);
        
    }
    
    public void setPosicion(Coordenadas pos){
        this.posicion = pos;
    }
    
    public Coordenadas getPosicion(){
        return posicion;
    }
    
    public void marcarPosicion(Coordenadas pos, String simbolo) {
        // Comprobamos limites
        if(pos.getFila() >= 0 && pos.getFila() < mapa.getFilas() && pos.getColumna() < mapa.getColumnas() && pos.getColumna() >= 0) {
            mapaVisual[pos.getFila()][pos.getColumna()] = simbolo;
        }
    } 
    
    public void actualizarMapaVisual() {
        for (int i = 0; i < mapa.getFilas(); i++) {
            for (int j = 0; j < mapa.getColumnas(); j++) {
                mapaInterfaz.actualizarBoton(i, j, mapaVisual[i][j]);
            }
        }
    }

    public void actualizarMapaVeces(Coordenadas coord){
        mapaVeces[coord.getFila()][coord.getColumna()]++;
    }
    
    public int[][] getMapaVeces(){
        return mapaVeces;
    }

    public InterfazMapa getInterfazMapa() {
        return mapaInterfaz;
    }
    
    

}
