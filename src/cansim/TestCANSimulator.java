package cansim;

public class TestCANSimulator
{
	final static int nodeCount = 4;

	public static void main(String[] args)
	{	    
		System.out.println("Starting CAN network and simulator with " + nodeCount + " nodes\n\n\n");

        /* Setup CAN network. */
        CANNetwork.setupNetwork(nodeCount);
        
		/* Create the CAN simulator for simulating the functionality of DPSIDWA. */
		CANSimulator simFrontEnd = new CANSimulator("DPSIDWA Simulator", nodeCount);

/*
		final CANSimulator simFrontEnd = new CANSimulator();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				simFrontEnd.initGUI("DPSIDWA Simulator", nodeCount);
			}
		});
*/		
        /* Start the bus simulator. */
        CANbus busSim = new CANbus();
        busSim.start();

        //Kaleem: Temp
        
//        CANSimulator.waveform.setMsgID((int)CANNetwork.bitStreams[0]); 
//        CANSimulator.waveform.repaint();
        
		/* Enable the data transmitting thread. */
//		CANDataTransmitter tx = new CANDataTransmitter();
//		tx.start();
	}
}

