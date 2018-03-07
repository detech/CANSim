package cansim;

/****************************************************************************
 Data transmitter thread.
****************************************************************************/
class CANDataTransmitter implements Runnable{
    CANnode TxNode;
    
    public CANDataTransmitter(){
        TxNode = SimulatorWindow.nodes[0]; // Kaleem
    }

    public CANDataTransmitter(CANnode txNode){
        TxNode = txNode;
    }
    
    public void run(){
        while(TxNode.isNodeStarted()==true){
            try{
                System.out.println("Executing in Data Transmitter with nodeID " + TxNode.NodeID + " and msgID " + TxNode.getCurrentPriority());
                CANDriver.sendDataMessage(TxNode.NodeID, TxNode.getCurrentPriority());
                Thread.sleep(5000);
            }
            catch (InterruptedException e){
            }
        }
    }
    
//    public void finalize(){   }
}
