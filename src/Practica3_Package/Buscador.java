/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import java.io.IOException;
import java.util.ArrayList;
import Practica3_Package.Casillas.Abajo;
import Practica3_Package.Casillas.AbajoDerecha;
import Practica3_Package.Casillas.AbajoIzquierda;
import Practica3_Package.Casillas.Arriba;
import Practica3_Package.Casillas.ArribaDerecha;
import Practica3_Package.Casillas.ArribaIzquierda;
import Practica3_Package.Casillas.Casilla;
import Practica3_Package.Casillas.Derecha;
import Practica3_Package.Casillas.Izquierda;
import Practica3_Package.comportamientos.Moverse;
import Practica3_Package.comportamientos.comunicacion.HablarConRudolph;
import Practica3_Package.comportamientos.comunicacion.HablarConSanta;
import Practica3_Package.enums.Comportamiento;
import Practica3_Package.enums.OpcionesSanta;

/**
 *
 * @author acarriq
 */
public class Buscador extends Agent {

    private String codigo;
    private ACLMessage canalSanta;
    private Entorno entorno;
    private ArrayList<Casilla> casillas;
    private Comportamiento comportamiento;
    private Coordenadas objetivo;
    private Coordenadas actual;

    public Coordenadas getActual() {
        return actual;
    }

    public void setActual(Coordenadas actual) {
        this.actual = actual;
    }
    
    
    
    public Buscador() throws IOException {
            Mapa mapa = new Mapa("./Mapas/mapWithoutObstacle2.txt");
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
    }

    public Comportamiento getComportamieno() {
        return comportamiento;
    }

    public void setComportamiento(Comportamiento comunicacion) {
        this.comportamiento = comunicacion;
    }

    public Coordenadas getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(Coordenadas objetivo) {
        this.objetivo = objetivo;
    }
    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public ACLMessage getCanalSanta() {
        return canalSanta;
    }

    public void setCanalSanta(ACLMessage canalSanta) {
        this.canalSanta = canalSanta;
    }

    public Entorno getEntorno() {
        return entorno;
    }

    public void setEntorno(Entorno entorno) {
        this.entorno = entorno;
    }

    public ArrayList<Casilla> getCasillas() {
        return casillas;
    }

    public void setCasillas(ArrayList<Casilla> casillas) {
        this.casillas = casillas;
    }
    
    

    @Override
    protected void setup() {
        addBehaviour(new HablarConSanta(this, OpcionesSanta.BUSCAR_RENOS));
        addBehaviour(new HablarConRudolph(this));
        addBehaviour(new Moverse(casillas ,this));
        
        
    }
}
