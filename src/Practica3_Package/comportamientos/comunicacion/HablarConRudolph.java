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
            // Establecer canal seguro:
            ACLMessage peticion = new ACLMessage(ACLMessage.PROPOSE);
            peticion.addReceiver(new AID("dba_rudolph", AID.ISLOCALNAME));
            
            peticion.setConversationId(buscador.getCodigo());
            peticion.setContent("Solicitud de participacion");

            buscador.send(peticion);

            // Espera de la respuesta:
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
                        System.out.println("Obtenidas coordenadas del reno");
                        
                        //buscador.addBehaviour(new ); Tengo que llamar al método
                        Coordenadas coordenadas = (Coordenadas) respuestaR.getContentObject();
                        buscador.getEntorno().getInterfazMapa().setObjetivoSeleccionado(coordenadas);
                        buscador.setComportamiento(Comportamiento.MOVER);
                        
                    } catch (UnreadableException ex) {
                        Logger.getLogger(HablarConRudolph.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if(respuestaR.getPerformative() == ACLMessage.REFUSE){
                    buscador.setComportamiento(Comportamiento.COMUNICACION_SANTA);                
                   
                }
                else{
                    System.out.println("Rudolph no contesta...");
                }
            } else {
                System.out.println("No se ha podido establecer comunicación con Rudolph.");
                buscador.doDelete();
            }
        }
       
    }
    
}
