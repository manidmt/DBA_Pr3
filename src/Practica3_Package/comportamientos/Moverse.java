/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package.comportamientos;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import java.util.ArrayList;
import Practica3_Package.Casillas.Casilla;
import Practica3_Package.Coordenadas;
import Practica3_Package.Entorno;
import Practica3_Package.Buscador;
import Practica3_Package.comportamientos.comunicacion.HablarConRudolph;
import Practica3_Package.enums.Comportamiento;

/**
 *
 * @author jordi
 */
public class Moverse extends CyclicBehaviour {

    int num_pasos = 0;
    Coordenadas objetivo;
    Coordenadas inicio;
    ArrayList<Casilla> casillas;
    Buscador agente;

    public Moverse(ArrayList<Casilla> casillas, Buscador agente) {
        this.casillas = casillas;
        this.agente = agente;
    }

    @Override
    public void action() {
        if (this.agente.getComportamieno() == Comportamiento.MOVER) {
            while (this.agente.getEntorno().getInterfazMapa().getInicioSeleccionado() == null 
                    || this.agente.getEntorno().getInterfazMapa().getObjetivoSeleccionado() == null) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (this.inicio == null) {
                this.inicio = this.agente.getEntorno().getInterfazMapa().getInicioSeleccionado();
            }

            this.objetivo = this.agente.getEntorno().getInterfazMapa().getObjetivoSeleccionado();
            Coordenadas viejaPosicion = this.agente.getEntorno().getPosicion();
            Coordenadas nuevaPosicion = elegirSiguientePosicion(this.agente.getEntorno());
            this.agente.getEntorno().actualizarMapaVeces(viejaPosicion);
            this.agente.getEntorno().setPosicion(nuevaPosicion); 
            actualizarYMostrarMapa(this.agente.getEntorno(), nuevaPosicion, viejaPosicion);
            try {
                Thread.sleep(200); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            num_pasos++;
            this.agente.getEntorno().getInterfazMapa().actualizarContadorPasos(num_pasos);
            boolean resultado = this.agente.getEntorno().getInterfazMapa().getObjetivoSeleccionado().equals(this.agente.getEntorno().getPosicion());
            if (resultado) {
                System.out.println("Bro He llegado a mi destino en " + num_pasos + " pasos. En Plan");
                // Aquí podrías añadir lógica para informar a Santa o a Rudolph si fuese necesario.
                // Conforme a tu lógica previa, cambia el comportamiento o haz la siguiente acción.
                agente.setComportamiento(Comportamiento.COMUNICACION_RUDOLPH);
            }
        }
    }

    private void actualizarYMostrarMapa(Entorno entorno, Coordenadas nuevaPosicion, Coordenadas viejaPosicion) {
        if (viejaPosicion != null) {
            entorno.marcarPosicion(viejaPosicion, "C"); 
        }

        this.agente.getEntorno().marcarPosicion(nuevaPosicion, "X"); 
        this.agente.getEntorno().marcarPosicion(objetivo, "F"); 
        this.agente.getEntorno().marcarPosicion(inicio, "I");
        this.agente.getEntorno().actualizarMapaVisual();
    }

    public Coordenadas rutaNormal(Coordenadas posActual) {
        Coordenadas mejorPosicion = posActual;
        double menorDistancia = Double.MAX_VALUE;
        for (Casilla casilla : casillas) {
            if (casilla.see(posActual)) {
                double dist = casilla.calcularDistancia(posActual, objetivo, this.agente.getEntorno().getMapaVeces());
                if (dist < menorDistancia) {
                    menorDistancia = dist;
                    mejorPosicion = casilla.getPosicion(posActual);
                }
            }
        }
        return mejorPosicion;
    }

    private Coordenadas elegirSiguientePosicion(Entorno entorno) {
        Coordenadas actual = entorno.getPosicion();
        actual = rutaNormal(actual);
        return actual;
    }
}