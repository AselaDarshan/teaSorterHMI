package com.Sortex.frontendController;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.Sortex.controller.Constants;
import com.Sortex.controller.TCPClient;

public class Window2 {

	
	




	public JPanel init() {

		JPanel container = new JPanel(new GridBagLayout());
		/**********************
		 * camera status panel
		 **************************************************/
		JPanel cameraStatusPanel = new JPanel(new GridBagLayout());
//		GridBagConstraints gbc = new GridBagConstraints();
//		gbc.insets = new Insets(6, 6, 6, 6);
//		gbc.gridwidth = GridBagConstraints.REMAINDER;
//		gbc.fill = GridBagConstraints.VERTICAL;

		JPanel framePeriodPanel = new JPanel(new GridBagLayout());
		
		JLabel framePeriodCameraLabel = new JLabel("Sensor");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JTextField framePeriodCameraText = new JTextField();
		framePeriodCameraText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodCameraText.setEditable(false);
		
		JLabel framePeriodHardwareLabel = new JLabel("Hardware");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JTextField framePeriodHardwareText = new JTextField();
		framePeriodHardwareText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodHardwareText.setEditable(false);
		
		JLabel framePeriodSoftwareLabel = new JLabel("Software");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JTextField framePeriodSoftwareText = new JTextField();
		framePeriodSoftwareText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		framePeriodSoftwareText.setEditable(false);
		
		JLabel exposureLabel = new JLabel("Exposure");
		framePeriodCameraLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JTextField exposureText = new JTextField();
		exposureText .setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		exposureText.setEditable(false);
		
		
		TitledBorder framePeriodBorder = new TitledBorder("Frame Period");
		framePeriodBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		framePeriodBorder.setTitleColor(new Color(6, 154, 35));
		framePeriodBorder.setTitleJustification(TitledBorder.CENTER);
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
		cameraStatusPanel.add(exposureLabel, new GridBagConstraints(1, 0, 1, 1, 0.1,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraStatusPanel.add( exposureText , new GridBagConstraints(2, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		TitledBorder cameraStatusBorder = new TitledBorder("Camera Status");
		cameraStatusBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		cameraStatusBorder.setTitleColor(new Color(16, 84, 185));
		cameraStatusBorder.setTitleJustification(TitledBorder.CENTER);
		cameraStatusBorder.setTitlePosition(TitledBorder.TOP);
		cameraStatusPanel.setBorder(cameraStatusBorder);
		
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
			}
			
		});
		
		
		buttonPanel.add(reloadButton, new GridBagConstraints(0, 0, 1, 1, 0.2,0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
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
		return container;

	}

	
	


}
