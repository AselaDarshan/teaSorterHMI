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


public class AdminWindow {

	
	

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
		
		JLabel ejectorTestLabel = new JLabel("Command (HEX)");
		ejectorTestLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		
		JTextField commandText =new JTextField("");  
		
		JButton ejectorOnButton = new JButton("Send");
		ejectorOnButton.setFont(new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));

		
		JLabel ejectorAuotTestLabel = new JLabel("Response");
		ejectorAuotTestLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		
		JTextField responseText =new JTextField("");  

		
		ejectorOnButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int command = (int)Long.parseLong( commandText.getText(), 16);
				int response = TCPClient.sendRawCommand(command);
				
				responseText.setText(Integer.toString(response));
			}
			
		});
		
		

		TitledBorder framePeriodBorder = new TitledBorder("Send Commands");
		framePeriodBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		framePeriodBorder.setTitleColor(new Color(6, 154, 35));
		framePeriodBorder.setTitleJustification(TitledBorder.LEFT);
		framePeriodBorder.setTitlePosition(TitledBorder.TOP);
		ejectorTestPanel.setBorder(framePeriodBorder);
		
		
		
		
		
		ejectorTestPanel.add(ejectorTestLabel, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add(commandText, new GridBagConstraints(1, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 3, 2, 0), 0, 0));
		
		ejectorTestPanel.add(ejectorOnButton, new GridBagConstraints(2, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		
		
		
		ejectorTestPanel.add( ejectorAuotTestLabel, new GridBagConstraints(0,1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 30, 2, 0), 0, 0));
		ejectorTestPanel.add(responseText, new GridBagConstraints(1, 1, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 3, 2, 0), 0, 0));
		
		
	
		
		
		
		
		
		
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
