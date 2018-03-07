package cansim;

/****************************************************************************
 This class implements a CAN bus simulator. It performs the multimaster
 arbitration using wired AND operation.
****************************************************************************/
class CANbus extends Thread{
    final int DOMINANT = 0;
    final int RECEISSIVE = 1;
    boolean busAccessNeeded = true;
    public CANbus(){}
    
    public void run(){
        while(true){
            busAccessNeeded = true;
            try{
//                System.out.println("Executing in CANbus doing wired AND");
                doWiredAND();
                Thread.sleep(1000); //Sleep for 100 millisec.
            }
            catch(InterruptedException e){
            }
        }
    }

    private void doWiredAND(){
//        System.out.println("In doWiredAND");
        int nodeCount = CANNetwork.NetworkNodes;
        boolean[] arbitratingNode = new boolean[nodeCount];

        while(busAccessNeeded == true){
            int nodeTxCount=0;
            int dominantNodeID=0;
            
            for(int i=0; i<nodeCount; i++){
                if(SimulatorWindow.nodes[i].isNodeStarted() == true){            
                    arbitratingNode[i]=true;
                    nodeTxCount++;
                }
            }

//            System.out.println("CANbus found " + nodeTxCount + "node transmitter");            
            /* Check if there is at least one transmitter. */
            if(nodeTxCount > 0){
        
                /* Arbitrate for all the control field bits. */
                for(int idBit=0; idBit<CANhw.ArbitrationFieldLength; idBit++){
                    int dominantNodeCount=0;
                    dominantNodeID = 0;
                    
                    /* Scan over all the nodes. */
                    for(int node=0; node<nodeCount; node++){
        
                        /* Include only those nodes in aribtration that have tried to 
                           transmit a message. */
                        if(CANNetwork.controllers[node].MessageSent == true){
                            if((CANNetwork.bitStreams[node] & idBit) == RECEISSIVE){
                                arbitratingNode[node] = false;
                            }
                            else{
                                dominantNodeCount++;
                                dominantNodeID = node;
                            }                   
                        }
                    }
                    
                    if(dominantNodeCount == 1){
//                        System.out.println("Dominant ndoe with stream  " + CANNetwork.bitStreams[dominantNodeID] + "  found");
                        /* A node has one the arbitration. */
                        break;            
                    }
                }
                
                /* Draw the wave of the winning node. */
 //               System.out.println("ID sent for waveform" + CANNetwork.bitStreams[dominantNodeID]);
   
                /* Set the flag to false to indicate that this message has been sent 
                   and no other message from the node has to be sent yet. */
                CANNetwork.controllers[dominantNodeID].MessageSent = false;
//                System.out.println("Node " + dominantNodeID + " won the bus");             

                CANSimulator.waveform.setMsgID((int)CANNetwork.bitStreams[dominantNodeID]); //Kaleem: Temp
                CANSimulator.waveform.repaint();
            }
            else{
                /* Set the flag to indicate that no message is waiting to gain 
                   access to bus. */
                busAccessNeeded = false;
                
                /* Display in wavewindow that no node is transmitting. */
//                System.out.println("No transmitter sent the message yet");               

                CANSimulator.waveform.setMsgID(0); //Kaleem: Temp
                CANSimulator.waveform.repaint();
            }
        }                
    }
    
    public static void transmitMessage(int devID){

        /* Get handle of the corresponding CAN hardware controller. */
        CANhw canDevice = CANNetwork.getCANDevice(devID);
        
//        System.out.println("Now in bus transmit service");
            
        /* Save the bit stream for arbitration on the bus. */
        CANNetwork.bitStreams[devID] = canDevice.synthesizeStream();
        
//        System.out.println("Generated Stream:" + CANNetwork.bitStreams[devID]);
    }    
}
