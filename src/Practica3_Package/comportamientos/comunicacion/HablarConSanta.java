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
    OpcionesSanta opcion;
    

    public HablarConSanta(Buscador buscador, OpcionesSanta opcion) {
        this.buscador = buscador;
        this.opcion = opcion;
        
    }

    @Override
    public void action() {
        if(buscador.getComportamieno() == Comportamiento.COMUNICACION_SANTA){
            switch (opcion) {
                case BUSCAR_RENOS -> buscarRenos();
                case BUSCAR_SANTA -> buscarSanta();
                case LLEGAR_DESTINO->llegarDestino();
                default -> throw new AssertionError();
                
            }
       
        }
       
    }

    private void buscarRenos() {
        // Presentación ante Santa Claus:
        buscador.setCanalSanta(new ACLMessage(ACLMessage.PROPOSE));
        buscador.getCanalSanta().addReceiver(new AID("dba_santa", AID.ISLOCALNAME));
        buscador.getCanalSanta().setContent("Me ofrezco voluntario para la misión.");
        buscador.send(buscador.getCanalSanta());

        // Espera de la respuesta:
        ACLMessage respuestaS = buscador.blockingReceive();
        System.out.println("performativa: " + respuestaS.getPerformative());
        if (respuestaS.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
            buscador.setCodigo(respuestaS.getContent());
            System.out.println("¡Santa ha aceptado al agente!\nEl código secreto es " + buscador.getCodigo());

            // Comportamiento para la búsqueda de renos:
            
            opcion = BUSCAR_SANTA;
            buscador.setComportamiento(Comportamiento.COMUNICACION_RUDOLPH);
            
        }
    }

    private void buscarSanta() {
        buscador.getCanalSanta().setPerformative(ACLMessage.REQUEST);
        buscador.getCanalSanta().setContent("Santa ¿Me recibes?");
        buscador.send(buscador.getCanalSanta());

        ACLMessage respuestaS = buscador.blockingReceive();
        if (respuestaS.getPerformative() == ACLMessage.AGREE) {
            System.out.println("Comunicación  establecida con Santa.");
            buscador.getCanalSanta().setPerformative(ACLMessage.INFORM);
            buscador.getCanalSanta().setContent("Santa ¿Donde estas?");
            buscador.send(buscador.getCanalSanta());

            respuestaS = buscador.blockingReceive();
            if (respuestaS.getPerformative() == ACLMessage.INFORM) {
                try {
                    
                    Coordenadas coordenadas = (Coordenadas) respuestaS.getContentObject();
                    buscador.getEntorno().getInterfazMapa().setObjetivoSeleccionado(coordenadas);
                    opcion = OpcionesSanta.LLEGAR_DESTINO;
                    buscador.setComportamiento(Comportamiento.MOVER);
                    
                } catch (UnreadableException ex) {
                    Logger.getLogger(HablarConSanta.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                System.out.println("Santa no contesta...");
            }

        } else {
            System.out.println("Santa no aparece.");
            buscador.doDelete();
        }

    }
    
    private void llegarDestino(){
    buscador.getCanalSanta().setPerformative(ACLMessage.INFORM);
        buscador.getCanalSanta().setContent("Misión completada");
        buscador.send(buscador.getCanalSanta());

        ACLMessage respuestaS = buscador.blockingReceive();
        if (respuestaS.getPerformative() == ACLMessage.INFORM) {
            System.out.println(respuestaS.getContent());
        } else {
            System.out.println("Santa no aparece.");
        }
        opcion = OpcionesSanta.BUSCAR_RENOS;
        buscador.setComportamiento(Comportamiento.NADA);
        buscador.doDelete();
    }

}
