package cansim;

import java.awt.*;
import javax.swing.*;

/******************************************************************************
  This class implements a custom component that draws a wave in the window.
 *****************************************************************************/
class WaveWindow extends JComponent{

	private static final long serialVersionUID = 6211561578410508631L;
	int w;
	int x, y, width=20, height=20;
	int msgID = 0;
	final static int DOMINANT = 0;
	final static int RECESSIVE = 1;
	final static int START = 50;
	static boolean prevRecessive=false;
	protected void paintComponent(Graphics g){
        x = START; y = 30;
        g.setColor(Color.GREEN);
		g.drawLine(x, y, x+50, y);
        x+=50;
        
        g.setColor(Color.BLACK);
        for(int i=0; i<CANhw.ArbitrationFieldLength; i++){
        	//x+=1;
        	if(((msgID >> i) & 1) == DOMINANT){
        		drawDominant(g);
        		prevRecessive = false;
        	}
        	else{
        		drawReceissive(g);
        		prevRecessive = true;
        	}
        }

        g.setColor(Color.GREEN);
		g.drawLine(x, y, x+48, y);
		x = START;
	}
	
	private void drawDominant(Graphics g){
		int x1;
		x1 = x + width;
		if(prevRecessive == true){
			g.drawLine(x, y-height, x, y);
		}			
		g.drawLine(x, y, x1, y);
		x = x1;		
	}

	private void drawReceissive(Graphics g){
		int x1, y1;
		x1 = x + width;
		y1 = y - height;
		if(prevRecessive == false){
			g.drawLine(x, y, x, y1);
		}
		g.drawLine(x, y1, x1, y1);
		x = x1;	
	}
	
	public void setMsgID(int newID){
		msgID = newID;
		
//		System.out.println("In setMsgID of WaveWindow with ID " + msgID);
	}
}
