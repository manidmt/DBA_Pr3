package Practica3_Package;

import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.ArrayList;
import Practica3_Package.Casillas.*;
import Practica3_Package.comportamientos.Moverse;
import Practica3_Package.comportamientos.comunicacion.HablarConRudolph;
import Practica3_Package.comportamientos.comunicacion.HablarConSanta;
import Practica3_Package.enums.Comportamiento;
import Practica3_Package.enums.OpcionesSanta;

/**
 * Agente Buscador que opera con un mapa.
 */
public class Buscador extends Agent {

    private String codigo;
    private ACLMessage canalSanta;
    private Entorno entorno;
    private ArrayList<Casilla> casillas;
    private Comportamiento comportamiento;
    private Coordenadas objetivo;
    private Coordenadas actual;

    public Buscador() {
        // Constructor vacío para instanciación predeterminada
    }

    @Override
    protected void setup() {
        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            String mapaPath = (String) args[0];
            try {
                Mapa mapa = new Mapa(mapaPath);
                entorno = new Entorno(mapa, this);
                casillas = new ArrayList<>();
                casillas.add(new Arriba(mapa));
                casillas.add(new Abajo(mapa));
                casillas.add(new Derecha(mapa));
                casillas.add(new Izquierda(mapa));
                casillas.add(new ArribaDerecha(mapa));
                casillas.add(new AbajoDerecha(mapa));
                casillas.add(new ArribaIzquierda(mapa));
                casillas.add(new AbajoIzquierda(mapa));
                this.comportamiento = Comportamiento.NADA;
                entorno.actualizarMapaVisual();
            } catch (IOException e) {
                System.err.println("Error al leer el mapa: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("No se proporcionó una ruta para el mapa.");
        }

        // Añadir comportamientos
        addBehaviour(new HablarConSanta(this, OpcionesSanta.BUSCAR_RENOS));
        addBehaviour(new HablarConRudolph(this));
        addBehaviour(new Moverse(casillas, this));
    }

    // Métodos getter y setter (sin cambios)
}
