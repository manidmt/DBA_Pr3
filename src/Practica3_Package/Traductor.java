package Practica3_Package;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.AID;
import jade.lang.acl.ACLMessage;

public class Traductor extends Agent {
    @Override
    protected void setup() {
        System.out.println("Traductor: Agente inicializado");
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = blockingReceive();
                if (msg != null) {
                    String content = msg.getContent();
                    String sender = msg.getSender().getLocalName();

                    if (sender.equals("dba_buscador")) {
                        System.out.println("Traductor: Traduciendo mensaje del Buscador para Santa.");
                        String nuevoContenido = content.replaceFirst("Bro", "Rakas Joulupukki").replace("En Plan", "Kiitos");
                        ACLMessage mensajeASanta = new ACLMessage(msg.getPerformative());
                        mensajeASanta.addReceiver(new AID("dba_santa", AID.ISLOCALNAME));
                        mensajeASanta.setContent(nuevoContenido);
                        mensajeASanta.setConversationId(msg.getConversationId());
                        send(mensajeASanta);

                    } else if (sender.equals("dba_santa")) {
                        System.out.println("Traductor: Traduciendo respuesta de Santa para el Buscador.");
                        String nuevoContenido = content.replaceFirst("Hyvää joulua", "Bro").replace("Nähdään pian", "En Plan");
                        ACLMessage mensajeABuscador = new ACLMessage(msg.getPerformative());
                        mensajeABuscador.addReceiver(new AID("dba_buscador", AID.ISLOCALNAME));
                        mensajeABuscador.setContent(nuevoContenido);
                        mensajeABuscador.setConversationId(msg.getConversationId());
                        send(mensajeABuscador);
                    } else {
                        block();
                    }
                } else {
                    block();
                }
            }
        });
    }
}