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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import Practica3_Package.Buscador;
import Practica3_Package.Coordenadas;
import Practica3_Package.comportamientos.Moverse;
import Practica3_Package.enums.Comportamiento;
import Practica3_Package.enums.OpcionesSanta;

/**
 *
 * @author jordi
 */
public class HablarConRudolph extends CyclicBehaviour {

    private Buscador buscador;

    public HablarConRudolph(Buscador buscador) {
        this.buscador = buscador;
    }

    @Override
    public void action() {
        if(buscador.getComportamieno() == Comportamiento.COMUNICACION_RUDOLPH){
            System.out.println("Bro Hablemos con Rudolph usando el código de Santa. En Plan");
            ACLMessage peticion = new ACLMessage(ACLMessage.PROPOSE);
            peticion.addReceiver(new AID("dba_rudolph", AID.ISLOCALNAME));
            peticion.setConversationId(buscador.getCodigo());
            peticion.setContent("Solicitud de participacion");
            buscador.send(peticion);

            ACLMessage respuestaR = buscador.blockingReceive();
            if (respuestaR.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                peticion = respuestaR.createReply();
                peticion.setPerformative(ACLMessage.INFORM);
                peticion.setConversationId(buscador.getCodigo());
                peticion.setContent("coordenada reno");
                buscador.send(peticion);

                respuestaR = buscador.blockingReceive();
                if (respuestaR.getPerformative() == ACLMessage.INFORM) {
                    try {
                        Coordenadas coordenadas = (Coordenadas) respuestaR.getContentObject();
                        System.out.println("Bro Coordenadas del reno recibidas: (" + coordenadas.getFila() + "," + coordenadas.getColumna() + "). ¡Vamos a por él! En Plan");
                        buscador.getEntorno().getInterfazMapa().setObjetivoSeleccionado(coordenadas);
                        buscador.setComportamiento(Comportamiento.MOVER);

                    } catch (UnreadableException ex) {
                        Logger.getLogger(HablarConRudolph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if(respuestaR.getPerformative() == ACLMessage.REFUSE){
                    System.out.println("Bro Rudolph dice que ya no hay más renos. En Plan");
                    buscador.setComportamiento(Comportamiento.COMUNICACION_SANTA);                
                }
                else{
                    System.out.println("Bro Rudolph no contesta... En Plan");
                }
            } else {
                System.out.println("Bro No se ha podido establecer comunicación con Rudolph. En Plan");
                buscador.doDelete();
            }
        }
    }
}