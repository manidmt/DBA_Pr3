package Practica3_Package;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

/**
 * Clase principal para lanzar el contenedor y el agente
 */
public class P3 {

    public static void main(String[] args) throws StaleProxyException {
        // Ruta predeterminada del mapa
        String mapaPath = "/home/ignacio/Escritorio/DBA/p3/DBA_Pr3/mapas-pr3/100x100-sinObstaculos.txt";
        
        // Si se proporcionan argumentos, usar el primero como ruta del mapa
        if (args.length > 0) {
            mapaPath = args[0];
        }

        // Configuración del contenedor principal de JADE
        Runtime rt = Runtime.instance();
        ProfileImpl p = new ProfileImpl();
        p.setParameter(Profile.MAIN_PORT, "1200"); // Cambia al puerto 1200 o elimina esta línea para un puerto dinámico
        ContainerController cc = rt.createMainContainer(p);

        // Creación del agente y paso de la ruta del mapa como argumento
        AgentController ac = cc.createNewAgent("dba_buscador", "Practica3_Package.Buscador", new Object[]{mapaPath});
        ac.start();
        
        AgentController ac1 = cc.createNewAgent("dba_santa","Practica3_Package.SantaClaus", null);
        ac1.start();
        
        AgentController ac2 = cc.createNewAgent("dba_rudolph","Practica3_Package.Rudolph", null);
        ac2.start();
        
        AgentController ac3 = cc.createNewAgent("dba_traductor","Practica3_Package.Traductor", null);
        ac3.start();
       
    }
}
