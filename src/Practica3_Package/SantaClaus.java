package Practica3_Package;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SantaClaus extends Agent {

    private Coordenadas inicio;

    @Override
    protected void setup() {
        System.out.println("Santa Claus: Agente inicializado");
        inicio = new Coordenadas(19,19);

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = blockingReceive();

                if (msg != null) {
                    String content = msg.getContent();
                    System.out.println("Santa Claus recibió: " + content);

                    if (content.contains("Me ofrezco voluntario para la misión.")) {
                        if (esConfiable()) {
                            enviarRespuesta(msg, ACLMessage.ACCEPT_PROPOSAL,
                                    "Hyvää joulua Has sido aceptado, aquí tienes el código MeEncantaDBA Nähdään pian");
                        } else {
                            enviarRespuesta(msg, ACLMessage.REJECT_PROPOSAL,
                                    "Hyvää joulua No eres confiable. Nähdään pian");
                        }
                    } else if (content.contains("Santa ¿Me recibes?")) {
                        enviarRespuesta(msg, ACLMessage.AGREE,
                                "Hyvää joulua Te escucho. Nähdään pian");
                    } else if (content.contains("Santa ¿Dónde estás?")) {
                        enviarRespuesta(msg, ACLMessage.INFORM,
                                "Hyvää joulua Mi posición es: Fila " + inicio.getFila() + ", Columna " + inicio.getColumna() + " Nähdään pian");
                    } else if (content.contains("Misión completada")) {
                        enviarRespuesta(msg, ACLMessage.INFORM,
                                "Hyvää joulua ¡HoHoHo! Misión completada con éxito. Nähdään pian");
                    }
                } else {
                    block();
                }
            }
        });
    }

    private boolean esConfiable() {
        return Math.random() < 0.8;
    }

    private void enviarRespuesta(ACLMessage mensajeEntrante, int performative, String contenidoRespuesta) {
        ACLMessage respuesta = mensajeEntrante.createReply();
        respuesta.setPerformative(performative);
        respuesta.setContent(contenidoRespuesta);
        send(respuesta);
    }
}