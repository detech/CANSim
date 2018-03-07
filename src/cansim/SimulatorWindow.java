package cansim;

import java.awt.*;
import javax.swing.*;

/******************************************************************************
 This class implements a the pane containing the specified number of 
 simulated CAN nodes.
 *****************************************************************************/
class SimulatorWindow
{
    static CANnode nodes[];
    public SimulatorWindow(int simNodeCount, JPanel simPane){
        nodes = new CANnode[simNodeCount];
        initGUI(simNodeCount, simPane);
    }

    private void initGUI(int simNodeCount, JPanel simPane){
        simPane.setLayout(new GridLayout(0, 2));

        /* Add simulated nodes to the simulator window. */
        JPanel canNode;
        for(int i=0; i<simNodeCount; i++){
            canNode = new JPanel();
            if((i&1) == 0){
                nodes[i] = new CANnode(canNode, "Sensor " + i, i);
            }
            else{
                nodes[i] = new CANnode(canNode, "Controller " + i, i);
            }
            simPane.add(canNode);
        }
    }
}
