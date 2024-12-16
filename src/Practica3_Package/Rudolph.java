/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Rudolph extends Agent {

    final private String codigoSecretoSanta = "MeEncantaDBA";
    private boolean aceptado = false;
    private Coordenadas[] renos = new Coordenadas[8];
    private int renoActual = 0;

    @Override
    protected void setup() {
        renos[0] = new Coordenadas(37, 12);
        renos[1] = new Coordenadas(20, 12);
        renos[2] = new Coordenadas(33, 22);
        renos[3] = new Coordenadas(7, 8);
        renos[4] = new Coordenadas(21, 9);
        renos[5] = new Coordenadas(39 ,39);
        renos[6] = new Coordenadas(30, 15);
        renos[7] = new Coordenadas(15, 15);

        System.out.println("Rudolph: Agente inicializado");

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = blockingReceive();

                if (msg != null) {
                    String content = msg.getContent();
                    if (content.equals("Solicitud de participacion")) {
                        if (esConfiable(msg)) {
                            aceptado = true;
                            enviarRespuesta(msg, ACLMessage.ACCEPT_PROPOSAL, "");
                        } else {
                            enviarRespuesta(msg, ACLMessage.REJECT_PROPOSAL, "");
                        }
                    } else if (content.equals("coordenada reno") && aceptado) {
                        Coordenadas coor = obtenerCoordenadaReno();
                        if (coor != null){
                            enviarRespuestaCoor(msg , ACLMessage.INFORM , coor);
                        } else {
                            enviarRespuesta(msg, ACLMessage.REFUSE, "");
                        }
                    }
                } else {
                    block();
                }
            }
        });
    }

    private boolean esConfiable(ACLMessage mensajeEntrante) {
        String codigoIngresado = mensajeEntrante.getConversationId();
        return codigoIngresado.equals(codigoSecretoSanta);
    }

    private Coordenadas obtenerCoordenadaReno() {
        if (renoActual < renos.length) {
            Coordenadas coordenada = renos[renoActual++];
            System.out.println("Rudolph: Coordenadas del reno: " + coordenada.getFila() + "," + coordenada.getColumna());
            return coordenada;
        } else {
            return null;
        }
    }

    private void enviarRespuesta(ACLMessage mensajeEntrante, int performative, String contenidoRespuesta) {
        ACLMessage respuesta = mensajeEntrante.createReply();
        respuesta.setPerformative(performative);
        respuesta.setContent(contenidoRespuesta);
        send(respuesta);
    }

    private void enviarRespuestaCoor(ACLMessage mensajeEntrante, int performative, Coordenadas coor) {
        ACLMessage respuesta = mensajeEntrante.createReply();
        respuesta.setPerformative(performative);
        try {
            respuesta.setContentObject(coor);
        } catch (IOException ex) {
            Logger.getLogger(Rudolph.class.getName()).log(Level.SEVERE, null, ex);
        }
        send(respuesta);
    }
}