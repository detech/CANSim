package cansim;

public class CANDriver{

    /* Default constructor. */
    public CANDriver(){}
    
    public static void sendDataMessage(int devID, int msgID){

        /* Get handle of the corresponding CAN hardware controller. */
        CANhw canDevice = CANNetwork.getCANDevice(devID);
//        System.out.println("Now in driver transmission routine");
        /* Set the message in corresponding CAN hardware controller. */
        canDevice.ID = msgID;
        canDevice.SRR = true;
        canDevice.IDE = true;
        canDevice.RTR = false;
        canDevice.length = 2;
//        canDevice.data[0] = 68;
//        canDevice.data[2] = 127;
        
        /* Output the message stream on the bus. */
        CANbus.transmitMessage(devID);
    }    
}
