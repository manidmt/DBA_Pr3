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
                    System.out.println("Santa Claus: Mensaje recibido: " + content);

                    // Siempre responder con el formato: "Hyvää joulua ... Nähdään pian"

                    if (content.contains("Me ofrezco voluntario para la misión.")) {
                        System.out.println("Santa Claus: Hyvää joulua Recibida propuesta de voluntario. Nähdään pian");
                        if (esConfiable()) {
                            System.out.println("Santa Claus: Hyvää joulua Este agente es digno. Enviando código secreto.Nähdään pian");
                            enviarRespuesta(msg, ACLMessage.ACCEPT_PROPOSAL,
                                    "Hyvää joulua Has sido aceptado, aquí tienes el código MeEncantaDBA Nähdään pian");
                        } else {
                            System.out.println("Santa Claus: Hyvää joulua Este agente no es digno. Rechazando propuesta. Nähdään pian");
                            enviarRespuesta(msg, ACLMessage.REJECT_PROPOSAL,
                                    "Hyvää joulua No eres confiable. Nähdään pian");
                        }
                    } else if (content.contains("Santa ¿Me recibes?")) {
                        System.out.println("Santa Claus: Hyvää joulua El agente pregunta si estoy disponible. Nähdään pian");
                        enviarRespuesta(msg, ACLMessage.AGREE,
                                "Hyvää joulua Te escucho. Nähdään pian");
                    } else if (content.contains("Santa ¿Dónde estás?")) {
                        System.out.println("Santa Claus: Hyvää joulua El agente solicita mi ubicación, la enviaré. Nähdään pian");
                        enviarRespuesta(msg, ACLMessage.INFORM,
                                "Hyvää joulua Mi posición es: Fila " + inicio.getFila() + ", Columna " + inicio.getColumna() + " Nähdään pian");
                    } else if (content.contains("Misión completada")) {
                        System.out.println("Santa Claus: Hyvää joulua El agente ha completado la misión. ¡HoHoHo! Nähdään pian");
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