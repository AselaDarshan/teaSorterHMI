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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import com.Sortex.controller.Constants;
import com.Sortex.controller.TCPClient;


public class Window2 {

	
	

	JTextField framePeriodCameraText;
	JTextField framePeriodHardwareText;
	JTextField framePeriodSoftwareText;
	JTextField resetLengthText;
	JTextField exposureText ;
	Thread updaterThread = null;
	private JTextField regValueText;
	private JSpinner regAddrSpinner;
	boolean regEditEnabled = false;
	private boolean getUpdates;
	public JPanel init() {

		JPanel container = new JPanel(new GridBagLayout());
		/**********************
		 * camera status panel
		 **************************************************/
		JPanel cameraStatusPanel = new JPanel(new GridBagLayout());


		JPanel framePeriodPanel = new JPanel(new GridBagLayout());
		
		JLabel framePeriodCameraLabel = new JLabel("Sensor");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		framePeriodCameraText = new JTextField();
		framePeriodCameraText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodCameraText.setEditable(false);
		
		JLabel framePeriodHardwareLabel = new JLabel("Hardware");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		framePeriodHardwareText = new JTextField();
		framePeriodHardwareText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodHardwareText.setEditable(false);
		
		JLabel framePeriodSoftwareLabel = new JLabel("Software");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		framePeriodSoftwareText = new JTextField();
		framePeriodSoftwareText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodSoftwareText.setEditable(false);
		
		
		
		

		TitledBorder framePeriodBorder = new TitledBorder("Frame Period");
		framePeriodBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		framePeriodBorder.setTitleColor(new Color(6, 154, 35));
		framePeriodBorder.setTitleJustification(TitledBorder.LEFT);
		framePeriodBorder.setTitlePosition(TitledBorder.TOP);
		framePeriodPanel.setBorder(framePeriodBorder);
		
		framePeriodPanel.add(framePeriodCameraLabel, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		framePeriodPanel.add(framePeriodCameraText , new GridBagConstraints(1, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 0, 0));
		framePeriodPanel.add(framePeriodHardwareLabel, new GridBagConstraints(2, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		framePeriodPanel.add(framePeriodHardwareText , new GridBagConstraints(3, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 0, 0));
		framePeriodPanel.add(framePeriodSoftwareLabel, new GridBagConstraints(4, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		framePeriodPanel.add(framePeriodSoftwareText , new GridBagConstraints(5, 0, 1, 1, 0.5, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 0, 2, 2), 0, 0));
		
		cameraStatusPanel.add(framePeriodPanel, new GridBagConstraints(0, 0, 1, 1, 0.6,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		
		TitledBorder cameraStatusBorder = new TitledBorder("Camera Status");
		cameraStatusBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		cameraStatusBorder.setTitleColor(new Color(16, 84, 185));
		cameraStatusBorder.setTitleJustification(TitledBorder.CENTER);
		cameraStatusBorder.setTitlePosition(TitledBorder.TOP);
		cameraStatusPanel.setBorder(cameraStatusBorder);
		/****integration time panel**/
		JPanel integrationTimePanel = new JPanel(new GridBagLayout());
		
		TitledBorder integrationTimeBorder = new TitledBorder("Integration Time");
		integrationTimeBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		integrationTimeBorder.setTitleColor(new Color(6, 154, 35));
		integrationTimeBorder.setTitleJustification(TitledBorder.LEFT);
		integrationTimeBorder.setTitlePosition(TitledBorder.TOP);
		 integrationTimePanel.setBorder(integrationTimeBorder);
		 
		JLabel exposureLabel = new JLabel("Exposure");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		exposureText = new JTextField();
		exposureText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		exposureText.setEditable(false);
		
		JLabel resetLengthLabel = new JLabel("Reset Length");
		resetLengthLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		resetLengthText = new JTextField();
		resetLengthText.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		resetLengthText.setEditable(false);
		
		

		integrationTimePanel.add(resetLengthLabel , new GridBagConstraints(1, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		integrationTimePanel.add(resetLengthText , new GridBagConstraints(2, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		integrationTimePanel.add(exposureLabel, new GridBagConstraints(3, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		integrationTimePanel.add( exposureText , new GridBagConstraints(4, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		/***register status panel******/
		JPanel regStatusPanel = new JPanel(new GridBagLayout());
		
		TitledBorder regStatusBorder = new TitledBorder("Register Values");
		 regStatusBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		 regStatusBorder.setTitleColor(new Color(6, 154, 35));
		 regStatusBorder.setTitleJustification(TitledBorder.LEFT);
		 regStatusBorder.setTitlePosition(TitledBorder.TOP);
		 regStatusPanel .setBorder(regStatusBorder);
		
		
		JLabel regAddrLabel = new JLabel("Address");
		regAddrLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		regAddrSpinner = new JSpinner();
		regAddrSpinner = new JSpinner(new SpinnerNumberModel(0,0,279,1));
		regAddrSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		regAddrSpinner.setValue(0);
	
		
		JLabel regValueLabel = new JLabel("Value");
		regValueLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		regValueText = new JTextField();
		regValueText.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		regValueText.setEditable(false);
		
		JButton editRegValButton = new JButton("Change");
		editRegValButton.setFont(new Font("Arial", Font.BOLD, Constants.SMALL_BUTTON_FONT_SIZE));
		
		editRegValButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(regEditEnabled) {
					
					TCPClient.sendRegValue(Integer.valueOf(regValueText.getText()), (int)regAddrSpinner.getValue());
					regEditEnabled = false;
					editRegValButton.setText("Change");
					regValueText.setEditable(false);
				}
				else {
					regEditEnabled = true;
					editRegValButton.setText("Send");
					regValueText.setEditable(true);
				}
				
			}
		});
		
		regStatusPanel.add( regAddrLabel , new GridBagConstraints(0, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 120, 2, 2), 0, 0));
		regStatusPanel.add( regAddrSpinner , new GridBagConstraints(1, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		regStatusPanel.add( regValueLabel , new GridBagConstraints(2, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 50, 2, 2), 0, 0));
		regStatusPanel.add( regValueText , new GridBagConstraints(3, 0, 1, 1, 0.3, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 10), 0, 0));
		regStatusPanel.add( editRegValButton , new GridBagConstraints(4, 0, 1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 120), 0, 0));
		
		/*****************************************************************/
		cameraStatusPanel.add(integrationTimePanel, new GridBagConstraints(0, 1, 1, 1, 0.6,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		cameraStatusPanel.add(regStatusPanel, new GridBagConstraints(0, 2, 1, 1, 0.6,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		/**********************
		 * control button panel
		 **************************************************/
		JPanel buttonPanel = new JPanel(new GridBagLayout());
		JButton reloadButton = new JButton("Update");
		reloadButton.setFont(new Font("Arial", Font.BOLD, Constants.SMALL_BUTTON_FONT_SIZE));
	
		reloadButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				framePeriodCameraText.setText(String.valueOf(TCPClient.getFramePeriodCamera()));
				framePeriodSoftwareText.setText(String.valueOf(TCPClient.getFramePeriodSoftware()));
				framePeriodHardwareText.setText(String.valueOf(TCPClient.getFramePeriodHardware()));
				exposureText.setText(String.valueOf(TCPClient.getExposure()));
				resetLengthText.setText(String.valueOf(TCPClient.getResetLength()));
			}
			
		});
		
		
//		buttonPanel.add(reloadButton, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		/**********************
		 * empty panel
		 **************************************************/
		JPanel emptyPanel = new JPanel(new GridBagLayout());
		
		container.add(cameraStatusPanel, new GridBagConstraints(0,0 , 1, 1, 1, 0.08, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
	
		container.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 680, 2, 20), 0, 0));
		container.add(emptyPanel, new GridBagConstraints(0, 2, 1, 1, 1, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		

		container.setPreferredSize(new Dimension(400, 100));
		
		
		container.addComponentListener ( new ComponentAdapter ()
		    {
		      

				public void componentShown ( ComponentEvent e )
		        {
		            System.out.println ( "status shown" );
		            if(updaterThread !=null)
		            	 updaterThread.interrupt();
		            getUpdates = true;
		            updaterThread = getUpdaterThread();
		            updaterThread.start();
		            
		            
		        }

		        public void componentHidden ( ComponentEvent e )
		        {
		            System.out.println ( "status hidden" );
		            getUpdates = false;
		            if(updaterThread !=null)
		            	 updaterThread.interrupt();
		            
		        }
		    } ); 
		
		
		
	
		return container;

	}

	private Thread getUpdaterThread(){
		return new Thread() {
		
			public void run() {
				
				while(getUpdates) {
					try {
						
						Thread.sleep(Constants.UPDATE_TIMER);
						framePeriodCameraText.setText(String.valueOf(TCPClient.getFramePeriodCamera()));
						Thread.sleep(10);
						framePeriodSoftwareText.setText(String.valueOf(TCPClient.getFramePeriodSoftware()));
						Thread.sleep(10);
						framePeriodHardwareText.setText(String.valueOf(TCPClient.getFramePeriodHardware()));
						Thread.sleep(10);
						exposureText.setText(String.valueOf(TCPClient.getExposure()));
						Thread.sleep(10);
						resetLengthText.setText(String.valueOf(TCPClient.getResetLength()));
						if(!regEditEnabled) {
							Thread.sleep(10);
							regValueText.setText(String.valueOf(TCPClient.getRegValue((int)regAddrSpinner.getValue())));
						}
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("status update stopped");
						//e.printStackTrace();
					}
				}
					
				
			}
		};
	}
	


}
