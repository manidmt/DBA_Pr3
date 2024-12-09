/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

public class Sensores {

    private static Mapa mapa = null;

    public Sensores(Mapa mapa) {
        if (this.mapa == null) {
            this.mapa = mapa;
        }

    }

    public boolean comprobar(Coordenadas a) {
        return mapa.get(a).equals("0");
    }

}
