package Practica3_Package;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Clase para manejar los mapas.
 */
public class Mapa {

    private int filas, columnas;
    private String[][] mapa;

    public Mapa(String ruta) throws IOException {
        leerFichero(ruta);
    }

    private void leerFichero(String ruta) throws IOException {
        File file = new File(ruta);
        if (!file.exists()) {
            throw new IOException("El archivo del mapa no existe: " + ruta);
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            // Leer el número de filas y columnas desde las dos primeras líneas
            filas = Integer.parseInt(br.readLine());
            columnas = Integer.parseInt(br.readLine());

            // Inicializar la matriz con las dimensiones leídas
            mapa = new String[filas][columnas];

            // Leer la matriz a partir de la línea 3
            for (int i = 0; i < filas; i++) {
                String linea = br.readLine();
                String[] elementos = linea.split("\t"); // Separador: tabulación
                for (int j = 0; j < columnas; j++) {
                    mapa[i][j] = elementos[j];
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
            throw e;
        }
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public String get(Coordenadas pos) {
        if (pos.getFila() >= 0 && pos.getFila() < filas && pos.getColumna() < columnas && pos.getColumna() >= 0) {
            return mapa[pos.getFila()][pos.getColumna()];
        } else {
            return "-1";
        }
    }

    public String[][] obtenerCopiaParaVisualizacion() {
        String[][] copia = new String[filas][columnas];
        for (int i = 0; i < filas; i++) {
            System.arraycopy(mapa[i], 0, copia[i], 0, columnas);
        }
        return copia;
    }

    public int[][] obtenerCopiaMismoTamano() {
        return new int[filas][columnas];
    }
}
