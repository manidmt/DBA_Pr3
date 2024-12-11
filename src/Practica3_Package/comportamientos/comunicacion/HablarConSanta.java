/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Practica3_Package.comportamientos.comunicacion;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import java.util.logging.Level;
import java.util.logging.Logger;
import jdk.dynalink.Operation;
import Practica3_Package.Buscador;
import Practica3_Package.Coordenadas;
import Practica3_Package.comportamientos.Moverse;
import Practica3_Package.enums.Comportamiento;
import Practica3_Package.enums.OpcionesSanta;
import static Practica3_Package.enums.OpcionesSanta.BUSCAR_RENOS;
import static Practica3_Package.enums.OpcionesSanta.BUSCAR_SANTA;

/**
 *
 * @author jordi
 */
public class HablarConSanta extends CyclicBehaviour {

    private Buscador buscador;
    private OpcionesSanta opcion;

    public HablarConSanta(Buscador buscador, OpcionesSanta opcion) {
        this.buscador = buscador;
        this.opcion = opcion;
    }

    @Override
    public void action() {
        if (buscador.getComportamieno() == Comportamiento.COMUNICACION_SANTA) {
            switch (opcion) {
                case BUSCAR_RENOS -> buscarRenos();
                case BUSCAR_SANTA -> buscarSanta();
                case LLEGAR_DESTINO -> llegarDestino();
                default -> {}
            }
        } 
        // Quitar el block() del else para que este comportamiento siga ejecutándose
        // y detecte si en el futuro se cambia a COMUNICACION_SANTA.
    }

    private void buscarRenos() {
        System.out.println("HablarConSanta: Solicitando misión a Santa");
        buscador.setCanalSanta(new ACLMessage(ACLMessage.PROPOSE));
        buscador.getCanalSanta().addReceiver(new AID("dba_traductor", AID.ISLOCALNAME));
        buscador.getCanalSanta().setContent("Bro Me ofrezco voluntario para la misión. En Plan");
        buscador.send(buscador.getCanalSanta());

        ACLMessage respuestaS = buscador.blockingReceive();
        if (respuestaS != null && respuestaS.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
            String content = respuestaS.getContent();
            content = limpiarFormato(content);
            String codigo = extraerCodigo(content);
            buscador.setCodigo(codigo);
            System.out.println("¡Santa ha aceptado al agente!\nEl código secreto es " + buscador.getCodigo());
            opcion = OpcionesSanta.BUSCAR_SANTA;
            buscador.setComportamiento(Comportamiento.COMUNICACION_RUDOLPH);

        } else if (respuestaS != null && respuestaS.getPerformative() == ACLMessage.REJECT_PROPOSAL) {
            System.out.println("Santa no confía en nosotros. Finalizando...");
            buscador.doDelete();
        } else {
            System.out.println("No se recibió respuesta esperada de Santa.");
            buscador.doDelete();
        }
    }

    private void buscarSanta() {
        // Preguntamos si Santa nos recibe
        buscador.getCanalSanta().setPerformative(ACLMessage.REQUEST);
        buscador.getCanalSanta().clearAllReceiver();
        buscador.getCanalSanta().addReceiver(new AID("dba_traductor", AID.ISLOCALNAME));
        buscador.getCanalSanta().setContent("Bro Santa ¿Me recibes? En Plan");
        buscador.send(buscador.getCanalSanta());

        ACLMessage respuestaS = buscador.blockingReceive();
        if (respuestaS != null && respuestaS.getPerformative() == ACLMessage.AGREE) {
            // Pedimos su ubicación
            buscador.getCanalSanta().setPerformative(ACLMessage.INFORM);
            buscador.getCanalSanta().clearAllReceiver();
            buscador.getCanalSanta().addReceiver(new AID("dba_traductor", AID.ISLOCALNAME));
            buscador.getCanalSanta().setContent("Bro Santa ¿Dónde estás? En Plan");
            buscador.send(buscador.getCanalSanta());

            respuestaS = buscador.blockingReceive();
            if (respuestaS != null && respuestaS.getPerformative() == ACLMessage.INFORM) {
                String cont = limpiarFormato(respuestaS.getContent());
                int fila = extraerNumero(cont, "Fila");
                int col = extraerNumero(cont, "Columna");
                Coordenadas c = new Coordenadas(fila, col);
                buscador.getEntorno().getInterfazMapa().setObjetivoSeleccionado(c);
                opcion = OpcionesSanta.LLEGAR_DESTINO;
                buscador.setComportamiento(Comportamiento.MOVER);
            } else {
                System.out.println("Santa no proporciona su ubicación.");
                buscador.doDelete();
            }

        } else {
            System.out.println("Santa no responde a nuestra llamada.");
            buscador.doDelete();
        }
    }

    private void llegarDestino() {
        buscador.getCanalSanta().setPerformative(ACLMessage.INFORM);
        buscador.getCanalSanta().clearAllReceiver();
        buscador.getCanalSanta().addReceiver(new AID("dba_traductor", AID.ISLOCALNAME));
        buscador.getCanalSanta().setContent("Bro Misión completada En Plan");
        buscador.send(buscador.getCanalSanta());

        ACLMessage respuestaS = buscador.blockingReceive();
        if (respuestaS != null && respuestaS.getPerformative() == ACLMessage.INFORM) {
            String cont = limpiarFormato(respuestaS.getContent());
            System.out.println(cont);
        } else {
            System.out.println("Santa no confirma la finalización.");
        }
        opcion = OpcionesSanta.BUSCAR_RENOS;
        buscador.setComportamiento(Comportamiento.NADA);
        buscador.doDelete();
    }

    private String limpiarFormato(String content) {
        return content.replace("Bro", "").replace("En Plan", "").trim();
    }

    private String extraerCodigo(String texto) {
        if (texto.contains("MeEncantaDBA")) {
            return "MeEncantaDBA";
        }
        return "CODIGO_DESCONOCIDO";
    }

    private int extraerNumero(String texto, String palabraClave) {
        int index = texto.indexOf(palabraClave);
        if (index == -1) return -1;
        String sub = texto.substring(index + palabraClave.length());
        sub = sub.replaceAll("[^0-9]", " ").trim();
        try {
            return Integer.parseInt(sub.split("\\s+")[0]);
        } catch (Exception e) {
            return -1;
        }
    }
}