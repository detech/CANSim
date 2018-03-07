package cansim;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

    /******************************************************************
        Node Description:            Sensor1
        Message Window Width(Nw)        Label
        Prioties in Message Window      _ _ _ _ _ _ _
        Current Message Priority
        Increase priority           Decrease Priority
        Start Node                  Stop Node
    *******************************************************************/

/******************************************************************************
 This class implements a single CAN node simulation and GUI capabilities.
 *****************************************************************************/
class CANnode{

    /* Message ID window width. It contains the number of priorities in the  
       message ID window. */
    private int Nw;

    /* Message ID window. This lsit contains the number of priorities in the  
       message ID window in a sorted manner. */
    private int[] SlavePriorities;

    /* Current priority value of the CAN node. This is the priority 
       that is transmitted on the CAN bus. */
    private int CurrentPriority;
    
    /* Index of the current priority value in the sorted list of priorites for  
       message ID window. */
    private int CPIndex;

    /* Flag to keep track of node's operating state. */
    private boolean NodeStarted;
    
    /* Node ID assigned to this node. */
    public int NodeID;
    
    /* Constructor. */
    public CANnode(JPanel mainPanel, String nodeName, int nodeID){
    
        /* Set the priorities and default priority to default values. */
        setDefaultValues();
        
        /* Set the node ID. */
        NodeID = nodeID;
        
        /* Draw the GUI. */
        initGUI(mainPanel, nodeName);        
    }
    
    /* GUI builder. */
    private void initGUI(JPanel mainPanel, String nodeName){
    
        /* Components in the GUI that will respond to the events. */    
        JLabel NwValue;
        final JLabel CurrentNodeState;
        JLabel NwPriorities;
        final JLabel CurrentPriorityDisplay;
        final JButton IncPriorityButton;
        final JButton DecPriorityButton;
        JButton StartNode;
        JButton StopNode;

        /* Main panel to contain other GUI components. */
//        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));    
        mainPanel.setLayout(new GridLayout(0,2, 5, 5));

        mainPanel.add(new JLabel(nodeName));
        CurrentNodeState = new JLabel();
        CurrentNodeState.setText("<html><h3><color=RED>Stopped</color></h3></html>");
        mainPanel.add(CurrentNodeState);

        mainPanel.add(new JLabel("Message Windows Width (Nw)"));
        NwValue = new JLabel("" + Nw);
        mainPanel.add(NwValue);

        mainPanel.add(new JLabel("Message Windows Priorities"));
        String priorityList="";
        for(int i=0; i<SlavePriorities.length; i++){
            priorityList += SlavePriorities[i] + " ";
        }
        NwPriorities = new JLabel("" + priorityList);
        mainPanel.add(NwPriorities);

        mainPanel.add(new JLabel("Current Message Priority"));
        CurrentPriorityDisplay = new JLabel("" + CurrentPriority);
        mainPanel.add(CurrentPriorityDisplay);

        IncPriorityButton = new JButton("Increase Priority");
        DecPriorityButton = new JButton("Decrease Priority");

//        IncPriorityButton.setMnemonic('i');
        IncPriorityButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                increasePriority();
                CurrentPriorityDisplay.setText("" + CurrentPriority);
                if(CPIndex == (SlavePriorities.length - 1)){
                    IncPriorityButton.setEnabled(false);
                }

                /* Enable the increase priority button if not already. */
                if(DecPriorityButton.isEnabled() == false){
                      DecPriorityButton.setEnabled(true);
                }
            }
        });
        mainPanel.add(IncPriorityButton );

//        DecPriorityButton.setMnemonic('d');
        DecPriorityButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                decreasePriority();
                CurrentPriorityDisplay.setText("" + CurrentPriority);
                if(CPIndex == 0){
                    DecPriorityButton.setEnabled(false);
                }
                
                /* Enable the increase priority button if not already. */
                if(IncPriorityButton.isEnabled() == false){
                      IncPriorityButton.setEnabled(true);
                }
            }
        });
        mainPanel.add(DecPriorityButton);

        StartNode = new JButton("Start Node");
//        StartNode.setMnemonic('a');
        StartNode.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                startNode();
                CurrentNodeState.setText("<html><h3><color=GREEN>Started</color></h3></html>");
            }
        });
        mainPanel.add(StartNode);

        StopNode = new JButton("Stop Node");
//        StopNode.setMnemonic('o');
        StopNode.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                stopNode();
                CurrentNodeState.setText("<html><h3><color=RED>Stopped</color></h3></html>");
            }
        });
        mainPanel.add(StopNode);
        
        /* Add border to the main panel. */
//        Border panelBorder = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
//        mainPanel.setBorder(panelBorder);
    }

    /* Sets the default priority to minimum priority value from the 
       message ID window. As per wired AND operation, minimum priority value
       has maximum numeric value. */
    public void setDefaultValues(){
        /* Set the message ID window to contain three priorities. */
        Nw = 5;

        /* Create the slave priority window. */
        SlavePriorities = new int[Nw];
        
        /* Set the default priority values in the message ID window. */
        for(int i=0; i<Nw; i++){
            SlavePriorities[i] = 1 << i;
        }
    
        /* Sort the priority list in increasing priority order 
           (decreasing numerical value). */
        sortPriorityList();

        CPIndex = 0;
        CurrentPriority = SlavePriorities[CPIndex];
        
        /* Set node state to not-started. */
        NodeStarted = false;
    }

    /* Sets the message window size i.e. the number of priorities in the
       window and the values of different priorities in the message window.
       The message ID window is adjusted to contain the priorities in in 
       numerically decreasing order. */
    public void setSlavePriorities(int []priorities, int nW){
        /* Set the message ID window width. */
        Nw = nW;  
        
        SlavePriorities = new int[nW];
        
        /* Set the message ID window. */
        for(int i=0; i<SlavePriorities.length; i++){
                    SlavePriorities[i] = priorities[i];
        }
        
        /* Sort the priority list in increasing priority order 
           (decreasing numerical value). */
        sortPriorityList();
    }

    public boolean isNodeStarted(){
        return NodeStarted;
    }
    
    public int getCurrentPriority(){
        return (CurrentPriority);
    }

    /* Increases the priority of the node. */    
    protected void increasePriority(){
        /* Check if the node isn't already at maximum priority. */    
        if(CPIndex < (Nw-1))
        {
            CPIndex++;
            CurrentPriority = SlavePriorities[CPIndex];        
        }
    }

    /* Decreases the priority of the node. */    
    protected void decreasePriority(){
        /* Check if the node isn't already at minimum priority. */    
        if(CPIndex != 0)
        {
            CPIndex--;
            CurrentPriority = SlavePriorities[CPIndex];        
        }
    }

    /* Start the current node. */    
    protected void startNode(){
        NodeStarted = true;
        System.out.println("Node started. Current ID " + CurrentPriority);
        
/*        Runnable temp = new CANDataTransmitter(this);
        Thread t = new Thread(temp);
        t.start();
*/
        Timer tx = new Timer(3000, new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.out.println("Executing in Tx Time with nodeID " + NodeID + " and msgID " + CurrentPriority);
                CANDriver.sendDataMessage(NodeID, CurrentPriority);            
            }
        });
        
//        CANDriver.sendDataMessage(NodeID, CurrentPriority); //kALEEM
//        CANSimulator.waveform.setMsgID((int)CANNetwork.bitStreams[0]); 
//        CANSimulator.waveform.repaint();

    }

    /* Stop the current node. */    
    protected void stopNode(){
        NodeStarted = false;        
    }

    /* Sorts the priorities in message ID window in a numerically descending 
       order. As per the wired AND operation, the smaller the numerical value 
       the higher the priority on the CAN bus.  */
    private void sortPriorityList(){
        int i=0, p;
        p = SlavePriorities[0];
        
        /* Sort the priority list using bubble sort. */
        for(i = 1; i<SlavePriorities.length; i++){
            for(int j=i; j>0; j--)
                if(SlavePriorities[j] > SlavePriorities[j-1]){
                    p = SlavePriorities[j-1];
                    SlavePriorities[j-1] = SlavePriorities[j];
                    SlavePriorities[j] = p;
                }
        }
    }
}
