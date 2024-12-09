/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author jordi
 */
public class Mapa {

    private int filas, columnas;
    private String mapa[][];

    public Mapa(String ruta) throws IOException {
        leerFichero(ruta);
    }

    private void leerFichero(String ruta) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            // Leer el número de filas y columnas desde las dos primeras líneas
            filas = Integer.parseInt(br.readLine());
            columnas = Integer.parseInt(br.readLine());

            // Inicializar la matriz con las dimensiones leídas
            mapa = new String[filas][columnas];

            // Leer la matriz a partir de la línea 3
            for (int i = 0; i < filas; i++) {
                String linea = br.readLine();
                String[] elementos = linea.split("\t"); // Suponiendo que los elementos están separados por tabulaciones

                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = elementos[j];
                }
            }
            /*
            System.out.println("El mapa leido es \n");
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    System.out.print(mapa[i][j] + "\t");
                }
                System.out.println();
            }*/
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
    
    public String get(Coordenadas pos){
        //Comprobamos que la posiicon este en el mapa
        if(pos.getFila() >= 0 && pos.getFila() < filas && pos.getColumna() < columnas && pos.getColumna() >= 0)
            return mapa[pos.getFila()][pos.getColumna()];
        else
            return "-1";
    }
    
    // Método para obtener una copia del mapa para visualización
    public String[][] obtenerCopiaParaVisualizacion() {
        String[][] mapaVisual = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                mapaVisual[i][j] = mapa[i][j]; 
            }
        }
        return mapaVisual;
    }
    
    public int[][] obtenerCopiaMismoTamano(){
        return new int[filas][columnas]; 
    }
    
    

    

}
