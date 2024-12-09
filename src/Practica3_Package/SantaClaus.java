/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SantaClaus extends Agent {

    private Coordenadas inicio;
    
    @Override
    protected void setup() {
        System.out.println("Santa Claus: Agente inicializado");
        
        inicio = new Coordenadas(19, 19);

        // Comportamiento para recibir mensajes
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                // Espera a recibir mensajes
                ACLMessage msg = blockingReceive();

                if (msg != null) {
                    System.out.println("Santa Claus");
                    System.out.println(msg.getContent());
                    // Procesa el mensaje recibido
                    String content = msg.getContent();
                    if (content.equals("Me ofrezco voluntario para la misión.")) {
                        // Simula la validación del agente y envía respuesta
                        if (esConfiable()) {
                            System.out.println("Santa Claus: Agente validado");
                            enviarRespuesta(msg, ACLMessage.ACCEPT_PROPOSAL, generarCodigoSecreto());
                        } else {
                            System.out.println("Santa Claus: Agente no confiable. Rechazando");
                            enviarRespuesta(msg, ACLMessage.REJECT_PROPOSAL, "");
                        }
                    } else if(content.equals("Santa ¿Me recibes?")){
                        System.out.println("Santa Claus: recibido mensaje");
                        enviarRespuestaCoor(msg, ACLMessage.AGREE, inicio) ;
                    }else if (content.equals("Santa ¿Donde estas?")) {
                        // Simula la respuesta con las coordenadas de Santa Claus
                        System.out.println("Santa Claus: Respondiendo ubicación");
                        System.out.println("Coordenadas de Santa Claus:(" + inicio.getFila() + "," + inicio.getColumna() + ")");                        
                        enviarRespuestaCoor(msg, ACLMessage.INFORM, inicio) ;
                        
                    } else if (content.equals("Misión completada")) {
                        System.out.println("Santa Claus: ¡HoHoHo! Misión completada con éxito.");
                        enviarRespuesta(msg, ACLMessage.INFORM, "Santa Claus: ¡HoHoHo! Misión completada con éxito.");
                    }
                } else {
                    // Si no hay mensajes, bloquea el comportamiento
                    block();
                }
            }
        });
    }

    private boolean esConfiable() {
        return Math.random() < 0.8;
    }

    private String generarCodigoSecreto() {
        return "MeEncantaDBA";
    }

    private void enviarRespuesta(ACLMessage mensajeEntrante, int performative, String contenidoRespuesta) {
        ACLMessage respuesta = mensajeEntrante.createReply();
        respuesta.setPerformative(performative);
        respuesta.setContent(contenidoRespuesta);
        System.out.println("Vamos a responder");
        send(respuesta);
    }
    
    private void enviarRespuestaCoor(ACLMessage mensajeEntrante, int performative, Coordenadas coor){
        ACLMessage respuesta = mensajeEntrante.createReply();
        respuesta.setPerformative(performative);    
        try {
            respuesta.setContentObject(coor);
        } catch (IOException ex) {
            Logger.getLogger(SantaClaus.class.getName()).log(Level.SEVERE, null, ex);
        }
        send(respuesta);
    }

}

