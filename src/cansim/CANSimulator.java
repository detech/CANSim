package cansim;

import java.awt.*;
import javax.swing.*;

public class CANSimulator
{
    public static WaveWindow waveform;
    public CANSimulator(){}

    public CANSimulator(String title, int nodeCount){
        initGUI(title, nodeCount);
    }

	public void initGUI(String title, int nodeCount)
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		
		/* Create the main GUI frame. */
		JFrame frame = new JFrame(title);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Create the window to display CAN nodes. */
		JPanel nodesWindow = new JPanel();
        nodesWindow.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));		
		SimulatorWindow simPane = new SimulatorWindow(nodeCount, nodesWindow);

        /* Create the window to display bus wave. */
		JPanel waveWindow = new JPanel(new GridLayout(1,1));
		waveform = new WaveWindow();
	    waveform.setSize(300, 100);
        waveWindow.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));		
		waveWindow.add(waveform);

        JScrollPane nodesScrollPane = new JScrollPane(nodesWindow);
        JScrollPane waveScrollPane = new JScrollPane(waveWindow);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, 
                                              nodesScrollPane, 
                                              waveScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(0.75);
		
		frame.getContentPane().add(splitPane);

		/* Minimum sizes for the two scroll panes. */
		
		frame.setSize(1024, 768);
		frame.setVisible(true);
	}
}
