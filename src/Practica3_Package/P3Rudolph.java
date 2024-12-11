
package Practica3_Package;

import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.core.Runtime;
import jade.wrapper.StaleProxyException;

/**
 *
 * @author acarriq
 */
public class P3Rudolph {

    /**
     * @param args the command line arguments
     * @throws jade.wrapper.StaleProxyException
     */
    public static void main(String[] args) throws StaleProxyException {
        Runtime rt = Runtime.instance();
        
        ContainerController cc = rt.createAgentContainer(new ProfileImpl());
        
        AgentController ac = cc.createNewAgent("dba_rudolph","Practica3_Package.Rudolph", null);
        ac.start();
    }
    
}
