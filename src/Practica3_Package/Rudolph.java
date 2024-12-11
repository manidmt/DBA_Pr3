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
        // Inicializa las coordenadas de cada reno
        renos[0] = new Coordenadas(47, 12); // Coordenadas de Dasher
        renos[1] = new Coordenadas(10, 12); // Coordenadas de Dancer
        renos[2] = new Coordenadas(33, 29); // Coordenadas de Vixen
        renos[3] = new Coordenadas(7, 18); // Coordenadas de Prancer
        renos[4] = new Coordenadas(21, 49); // Coordenadas de Cupido
        renos[5] = new Coordenadas(19 ,39); // Coordenadas de Comet
        renos[6] = new Coordenadas(1, 15); // Coordenadas de blitzen
        renos[7] = new Coordenadas(15, 35); // Coordenadas de Donner
        
        System.out.println("Rudolph: Agente inicializado");

        // Comportamiento para recibir mensajes
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Espera a recibir mensajes
                ACLMessage msg = blockingReceive();

                if (msg != null) {
                    // Procesa el mensaje recibido
                    String content = msg.getContent();
                    if (content.equals("Solicitud de participacion")) {
                        // Simula la validación del agente y envía respuesta
                        if (esConfiable(msg)) {
                            aceptado = true;
                            System.out.println("Rudolph: Agente validado por Santa");
                            enviarRespuesta(msg, ACLMessage.ACCEPT_PROPOSAL, "");
                        } else {
                            System.out.println("Rudolph: Agente no confiable. Rechazando");
                            enviarRespuesta(msg, ACLMessage.REJECT_PROPOSAL, "");
                        }
                    } else if (content.equals("coordenada reno") && aceptado) {
                        // Simula el envío de las coordenadas de los renos
                        System.out.println("Rudolph: Enviando coordenadas de los renos");
                        Coordenadas coor = obtenerCoordenadaReno();
                        if (coor != null){
                            enviarRespuestaCoor(msg , ACLMessage.INFORM , coor);                           
                        }
                        else{
                            enviarRespuesta(msg, ACLMessage.REFUSE, "");
                        }
                        
                        
                    }
                } else {
                    // Si no hay mensajes, bloquea el comportamiento
                    block();
                }
            }
        });
    }

    private boolean esConfiable(ACLMessage mensajeEntrante) {
        String codigoIngresado = mensajeEntrante.getConversationId();

        // Verifica si el código ingresado coincide con el código secreto de Santa
        return codigoIngresado.equals(codigoSecretoSanta);
    }


    private Coordenadas obtenerCoordenadaReno() {
        if (renoActual < renos.length) {
            Coordenadas coordenada = renos[renoActual++];
            System.out.println("Coordenadas del reno: " + coordenada.getFila() + "," + coordenada.getColumna());
            return coordenada;
        } else {
            System.out.println("Ya se han encontrado todos los renos");
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
