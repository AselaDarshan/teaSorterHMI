package com.Sortex.frontendController;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.locks.LockSupport;

import javax.swing.JButton;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultCaret;

import com.Sortex.controller.Constants;
import com.Sortex.controller.TCPClient;


public class DecisionLogWindow {

	
	

	JTextField framePeriodCameraText;
	JTextField framePeriodHardwareText;
	JTextField framePeriodSoftwareText;
	JTextField resetLengthText;
	JTextField exposureText ;
	Thread updaterThread = null;

	boolean regEditEnabled = false;
	private boolean getUpdates;
	JTextArea logText ;
	public JPanel init() {

		JPanel container = new JPanel(new GridBagLayout());
		/**********************
		 * decision log panel
		 **************************************************/
		JPanel decisionLogPanel = new JPanel(new GridBagLayout());


		TitledBorder decisionLogPanelBorder = new TitledBorder("Decision Log");
		decisionLogPanelBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		decisionLogPanelBorder.setTitleColor(new Color(6, 154, 35));
		decisionLogPanelBorder.setTitleJustification(TitledBorder.LEFT);
		decisionLogPanelBorder.setTitlePosition(TitledBorder.TOP);
		decisionLogPanel.setBorder(decisionLogPanelBorder);
		
		
		logText = new JTextArea();
		DefaultCaret caret = (DefaultCaret)logText.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		JScrollPane scroll = new JScrollPane (logText, 
				   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//logText.setGrowByContent(false);
		/*****************************************************************/
		decisionLogPanel.add(scroll, new GridBagConstraints(0, 1, 1, 1, 0.6,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		/**********************
		 * control button panel
		 **************************************************/
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		JButton clearButton = new JButton("Clear");
		clearButton.setFont(new Font("Arial", Font.BOLD, Constants.SMALL_BUTTON_FONT_SIZE));
	
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				logText.setText("");
			}
			
		});
		
		
		buttonPanel.add(clearButton, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		/**********************
		 * empty panel
		 **************************************************/
		
		
		container.add(decisionLogPanel, new GridBagConstraints(0,0 , 1, 1, 1, 0.3, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	
		container.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.01, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 680, 2, 20), 0, 0));
//		container.add(emptyPanel, new GridBagConstraints(0, 2, 1, 1, 1, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH,
//				new Insets(2, 2, 2, 2), 0, 0));
		
		

		container.setPreferredSize(new Dimension(400, 100));
		
		
		container.addComponentListener ( new ComponentAdapter ()
		    {
		      

				public void componentShown ( ComponentEvent e )
		        {
		            System.out.println ( "decision log shown" );
		            TCPClient.sendMux(0);
		            
		            if(updaterThread !=null)
		            	 updaterThread.interrupt();
		            getUpdates = true;
		            updaterThread = getUpdaterThread();
		            updaterThread.start();
		            
		            
		        }

		        public void componentHidden ( ComponentEvent e )
		        {
		            System.out.println ( "dicision log hidden" );
		            getUpdates = false;
		            if(updaterThread !=null)
		            	 updaterThread.interrupt();
		            
		        }
		    } ); 
		
		
		
	
		return container;

	}

	private Thread getUpdaterThread(){
		return new Thread() {
			int decision=0,preDecision = 0,frameIndex;
			byte[] decisionData;
			public void run() {
				try {
				Thread.sleep(40);
				while(getUpdates) {
					LockSupport.parkNanos(400000);
					//logText.append("\nMissed "+(frameIndex-prvFrameIndex-1 )+ "frames from"+prvFrameIndex);
						//Thread.sleep(1);
						decisionData = TCPClient.getDecision();
						frameIndex  = Byte.toUnsignedInt(decisionData[0])+ Byte.toUnsignedInt(decisionData[1])*256+ Byte.toUnsignedInt(decisionData[2])*256*256 + Byte.toUnsignedInt(decisionData[3])*256*256*256;
						decision =  Byte.toUnsignedInt(decisionData[4])+ Byte.toUnsignedInt(decisionData[5])*256+ Byte.toUnsignedInt(decisionData[6])*256*256+ Byte.toUnsignedInt(decisionData[7])*256*256*256;
//						if(prvFrameIndex + 1 < frameIndex) {
//							logText.append("Missed "+(frameIndex-prvFrameIndex-1 )+ "frames from"+prvFrameIndex);
//						}
					//	prvFrameIndex  = frameIndex;
						if(decision != preDecision) {
							preDecision = decision;
							logText.append(frameIndex+" "+Integer.toBinaryString(decision)+"\n");
						}
						
						
						
					
				}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("frame log update stopped");
					//e.printStackTrace();
				}
					
				
			}
		};
	}
	


}
