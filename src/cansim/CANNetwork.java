package cansim;

public class CANNetwork{
    static int NetworkNodes;
    final static long InvalidBitStream = 0xFFFFFFFF; 
    
    /* Array containing the CAN bit stream from each controller. */
    static long bitStreams[];

    /* Array of CAN hardware controller onjects. */
    static CANhw controllers[];
    
    CANNetwork(){}
            
    /* Attach the CAN hardware controllers to the network. */
    public static void setupNetwork(int nodeCount){
        NetworkNodes = nodeCount;
        
        controllers = new CANhw[nodeCount];
        
        for(int i=0; i<nodeCount; i++){
            /* Initialize hardware for each CAN node. */
            controllers[i] = new CANhw();
        }

        bitStreams = new long[nodeCount];
        
        /* Reset bit streams values. */
        for(int i=0; i<bitStreams.length; i++){
            bitStreams[i] = InvalidBitStream;
        }
    }
    
    public static CANhw getCANDevice(int devID){
        return (controllers[devID]);
    }
}
