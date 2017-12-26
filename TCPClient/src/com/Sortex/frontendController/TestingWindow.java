package com.Sortex.frontendController;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javax.swing.JSpinner;
import javax.swing.JTextField;

import javax.swing.border.TitledBorder;

import com.Sortex.controller.Constants;
import com.Sortex.controller.TCPClient;


public class TestingWindow {

	
	

	JTextField ejectorTestText;
	
	JTextField resetLengthText;
	JTextField exposureText ;

	boolean regEditEnabled = false;
	
	
	private boolean ejectorTestEnabled;
	public JPanel init() {

		JPanel container = new JPanel(new GridBagLayout());
		/**********************
		 * Ejector test panel
		 **************************************************/
		


		JPanel ejectorTestPanel = new JPanel(new GridBagLayout());
		
		JLabel ejectorTestLabel = new JLabel("Ejector");
		ejectorTestLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		
		JSpinner ejectorSpinner = new JSpinner();
		ejectorSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		
		
		JButton ejectorOnButton = new JButton("ON");
		ejectorOnButton.setFont(new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));
		
		JButton ejectorOffButton = new JButton("OFF");
		ejectorOffButton.setFont(new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));
		
		
		JLabel ejectorAuotTestLabel = new JLabel("Auto test ON period (ms)");
		ejectorAuotTestLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
	
		JSpinner ejectorAuotTestSpinner = new JSpinner();
		ejectorAuotTestSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		JButton ejectorAuotTestButton = new JButton("Start");
		ejectorAuotTestButton.setFont(new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));
		
		JButton ejectorAuotTestStopButton = new JButton("Stop");
		ejectorAuotTestStopButton.setFont(new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));
		
		ejectorOnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TCPClient.sendSwitchEjectorCommand((int) ejectorSpinner.getValue(), 1);
			}
			
		});
		
		ejectorOffButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TCPClient.sendSwitchEjectorCommand((int) ejectorSpinner.getValue(), 0);
			}
			
		});
		
		ejectorAuotTestButton.addActionListener(new ActionListener() {

			

			@Override
			public void actionPerformed(ActionEvent e) {
				ejectorTestEnabled = true;
				Thread thread2 = new Thread() {
					public void run() {
						for(int i=1 ; i<=24 && ejectorTestEnabled;i++) {
							System.out.println("ejector "+i+" ON");
							TCPClient.sendSwitchEjectorCommand(i, 1);
							try {
								Thread.sleep(new Long((int)ejectorAuotTestSpinner.getValue()).longValue());
							} catch (InterruptedException e1) {
								
							}
							TCPClient.sendSwitchEjectorCommand(i, 0);
							System.out.println("ejector "+i+" OFF");
						}
							
				
					}
				};
				thread2.start();
				}
			
		});
		
		ejectorAuotTestStopButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ejectorTestEnabled = false;
			}
			
		});
		

		TitledBorder framePeriodBorder = new TitledBorder("Ejector Test");
		framePeriodBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		framePeriodBorder.setTitleColor(new Color(6, 154, 35));
		framePeriodBorder.setTitleJustification(TitledBorder.LEFT);
		framePeriodBorder.setTitlePosition(TitledBorder.TOP);
		ejectorTestPanel.setBorder(framePeriodBorder);
		
		
		
		
		
		ejectorTestPanel.add(ejectorTestLabel, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add(ejectorSpinner, new GridBagConstraints(1, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add(ejectorOnButton, new GridBagConstraints(2, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		
		ejectorTestPanel.add(ejectorOffButton, new GridBagConstraints(3, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		
		ejectorTestPanel.add( ejectorAuotTestLabel, new GridBagConstraints(0, 1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add( ejectorAuotTestSpinner, new GridBagConstraints(1, 1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add( ejectorAuotTestButton, new GridBagConstraints(2, 1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		
		ejectorTestPanel.add(ejectorAuotTestStopButton, new GridBagConstraints(3, 1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
	
		
		
		
		
		
		
		/**********************
		 * control button panel
		 **************************************************/
		
		
		
//		buttonPanel.add(reloadButton, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		/**********************
		 * empty panel
		 **************************************************/
		JPanel emptyPanel = new JPanel(new GridBagLayout());
		
		
		container.add(ejectorTestPanel, new GridBagConstraints(0, 1, 1, 1, 1, 0.1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 20), 0, 0));
		container.add(emptyPanel, new GridBagConstraints(0, 2, 1, 1, 1, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));
		
		

		container.setPreferredSize(new Dimension(400, 100));
		
		
		
		
	
		return container;

	}

	
}
