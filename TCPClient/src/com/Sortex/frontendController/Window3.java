package com.Sortex.frontendController;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.IOException;

import java.io.PrintStream;


import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.JSpinner;

import javax.swing.JTextField;

import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


import com.Sortex.controller.AutoCalibration;

import com.Sortex.controller.CapturePane;

import com.Sortex.controller.Constants;

import com.Sortex.controller.SettingsManager;
import com.Sortex.controller.StreamCapturer;
import com.Sortex.controller.TCPClient;


public class Window3 {
	JPanel container;

	JPanel panel1;
	JPanel thresholdPanel,cameraSettingPanel;
	JPanel panel2;
	JPanel panel3;
	JPanel panel4;
	JPanel panel5;
	JButton startButton;
	JButton monitorButton;
	JLabel label4,bgThresholdLabel,slThresholdLabel;
	JLabel imgLabel,roiXStartLabel,roiXEndLabel,roiYStartLabel,roiYEndLabel,marginLabel;
	JButton resetButton;
	JButton cameraSettingApplyButton;
	int numberOfFrames;
	JButton autoThresholdingButton;
	JSlider sensitivitySlider;
	JLabel sensitivity ;
	JButton saveButton = new JButton("Save");
	JButton applyButton = new JButton("Apply");
	JSpinner delayGranulatySpinner,roiXStartSpinner,roiXEndSpinner,roiYStartSpinner,roiYEndSpinner;
	JSpinner ejectDurationSpinner,delayStepsSpinner;
	JSlider stemLeafThresholdSlider, backgroundThresholdSlider;
	JComboBox<String> categorySelection;
	JComboBox<Integer> marginIdSelector;
	// parameters
	int redValue;
	int greenValue;
	int blueValue;
	String type;
	String category;
	int sensitivityValue;
	int currentPosition;

	ImageIcon frameIcon;
	Image frameimage;
	Image newframeimg;

	JLabel frameLabel;

	boolean isMonitoring =false;

	private JSpinner marginSpinner;

	private JLabel muxLabel;

	private JSpinner muxSpinner;
	
	private int[] marginValueArray;

	private JLabel minCountLabel;

	private JSpinner minCountSpinner;

	private JPanel certantityAndMinCountPanel;

	private JLabel sensitivityLabel;

	private JTextField bgThrsholdValueLabel;

	private  JTextField  slThrsholdValueLabel;

	private  JTextField  sensitivityValueLabel;

	private int tcpTimeout;

	private JButton autoMarginButton;

	private JButton captureButton;

	private JButton restartButton;

	private JButton captureAndFreezeButton;

	private JButton resumeButton;
	
	public JPanel createTestPanel(SettingsManager settingsManager) {
		
		settingsManager.retriveSavedSettings();
		tcpTimeout = settingsManager.getTcpTimeOut(); 
		
		container = new JPanel();
		container.setLayout(new GridBagLayout());
		
		marginValueArray =  new int[25];
		
		/**************************** Threshold Panel **************************************/
		panel1 = new JPanel(new BorderLayout());
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.Y_AXIS));
		panel1.setBorder(BorderFactory.createEtchedBorder());
		TitledBorder border = new TitledBorder("Select Type");
		border.setTitleFont(new Font("Arial", Font.BOLD, 20));
		//border.setTitleColor(new Color(6, 104, 35));
		border.setTitleJustification(TitledBorder.CENTER);
		border.setTitlePosition(TitledBorder.TOP);
		panel1.setBorder(border);

		//threshold sliders

		stemLeafThresholdSlider = getSlider(0, 1023, 0, 100, 5);
		backgroundThresholdSlider = getSlider(0, 1023, 0, 100, 5);
		thresholdPanel = new JPanel(new GridBagLayout());
		
		stemLeafThresholdSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
//				sensitivityValue = stemLeafThresholdSlider.getValue();
				slThrsholdValueLabel.setText(String.valueOf(stemLeafThresholdSlider.getValue()));
				settingsManager.setStemLeafThreshold(stemLeafThresholdSlider.getValue());
				

			}
		});
		
		backgroundThresholdSlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
//				sensitivityValue = stemLeafThresholdSlider.getValue();
					bgThrsholdValueLabel.setText(String.valueOf(backgroundThresholdSlider.getValue()));
					settingsManager.setBgThrshold(backgroundThresholdSlider.getValue());		
			}
		});
		
		sensitivitySlider = getSlider(0, 100, 50, 10, 5);
		bgThresholdLabel=  new JLabel("Backgorund");
		bgThrsholdValueLabel = new JTextField();
		bgThrsholdValueLabel.setFont(new Font("Arial", Font.BOLD, Constants.TEXT_FEILD_FONT_SIZE));
		slThresholdLabel =  new JLabel("Stem/Leaf");
		slThrsholdValueLabel = new JTextField();
		slThrsholdValueLabel.setFont(new Font("Arial", Font.BOLD, Constants.TEXT_FEILD_FONT_SIZE));
		sensitivityLabel = new JLabel("Certainty(%)");
		sensitivityValueLabel = new JTextField();
		sensitivityValueLabel.setFont(new Font("Arial", Font.BOLD, Constants.TEXT_FEILD_FONT_SIZE));
		
		minCountLabel = new JLabel("Min Count");

		
		
		minCountSpinner = new JSpinner();
		minCountSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		certantityAndMinCountPanel = new JPanel(new GridBagLayout());
		
		certantityAndMinCountPanel.add(sensitivitySlider , new GridBagConstraints(0, 0, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		certantityAndMinCountPanel.add(minCountLabel, new GridBagConstraints(1, 0, 1, 1, 0.01, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 10, 2, 2), 0, 0));
		certantityAndMinCountPanel.add(minCountSpinner , new GridBagConstraints(2, 0, 1, 1, 0.05, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));
		
		
		thresholdPanel.add(slThresholdLabel, new GridBagConstraints(0, 0, 1, 1, 0.03, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		thresholdPanel.add(slThrsholdValueLabel, new GridBagConstraints(1, 0, 1, 1, 0.09, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));
		thresholdPanel.add(stemLeafThresholdSlider, new GridBagConstraints(2, 0, 5, 1, 1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		
		thresholdPanel.add(bgThresholdLabel, new GridBagConstraints(0, 1, 1, 1, 0.03, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		thresholdPanel.add(bgThrsholdValueLabel, new GridBagConstraints(1, 1, 1, 1, 0.09, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));
		thresholdPanel.add(backgroundThresholdSlider, new GridBagConstraints(2, 1,  2, 1, 1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		thresholdPanel.add(sensitivityLabel, new GridBagConstraints(0, 2, 1, 1, 0.03, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		thresholdPanel.add(sensitivityValueLabel, new GridBagConstraints(1, 2, 1, 1, 0.09, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(10, 2, 10, 2), 0, 0));
		thresholdPanel.add(certantityAndMinCountPanel, new GridBagConstraints(2, 2, 2, 1,1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		TitledBorder thresholdBorder = new TitledBorder("Thresholds");
		thresholdBorder .setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		thresholdBorder.setTitleColor(new Color(6, 154, 35));
		thresholdBorder .setTitleJustification(TitledBorder.CENTER);
		thresholdBorder .setTitlePosition(TitledBorder.TOP);
		thresholdPanel.setBorder(thresholdBorder);
		
		minCountSpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
	          
					settingsManager.setMinStemCount((int)minCountSpinner.getValue());	
	        }
	    });
		/*******************end threshold panel**************************************/
		/********************camera setting panel************************************/
		cameraSettingPanel = new JPanel(new GridBagLayout());
		cameraSettingApplyButton =  new JButton("Apply");

		
		TitledBorder cameraSettingBorder = new TitledBorder("Camera Settings");
		cameraSettingBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		cameraSettingBorder.setTitleColor(new Color(6, 154, 35));
		cameraSettingBorder.setTitleJustification(TitledBorder.CENTER);
		cameraSettingBorder.setTitlePosition(TitledBorder.TOP);
		cameraSettingPanel.setBorder(cameraSettingBorder);
		
		roiXStartSpinner = new JSpinner();
		roiXStartSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		roiXStartLabel = new JLabel("X Start");
		
		
		roiXEndSpinner = new JSpinner();
		
		roiXEndSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		roiXEndLabel = new JLabel("X End");
		
		//hide x roi settings for now
		roiXEndLabel.setVisible(false);
		roiXEndSpinner.setVisible(false);
		roiXStartLabel.setVisible(false);
		roiXStartSpinner.setVisible(false);
		
		roiYStartSpinner = new JSpinner(new SpinnerNumberModel(0,0,1022,2));
		roiYStartSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		roiYStartLabel = new JLabel("Y Start");
		
		
		roiYEndSpinner = new JSpinner();
		roiYEndSpinner = new JSpinner(new SpinnerNumberModel(0,0,1022,1));
		roiYEndSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		roiYEndLabel = new JLabel("Y End");
		
		marginIdSelector =  new JComboBox<>();
		marginLabel =  new JLabel("Margin");
		marginLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		
		marginIdSelector.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		for(int i=0;i<25;i++)
			marginIdSelector.addItem(i);
	
		
		marginSpinner = new JSpinner();
		marginSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		muxLabel =  new JLabel("MUX");
		muxLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		muxSpinner = new JSpinner();
		muxSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		autoMarginButton = new JButton("Auto Detect Margins");
		autoMarginButton.setFont(new Font("Arial", Font.BOLD, Constants.SMALL_BUTTON_FONT_SIZE));
		
		JLabel exposureTimeLabel = new JLabel("Exposure");
		exposureTimeLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JSpinner exposureTimeSpinner = new JSpinner();
		exposureTimeSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		JLabel frameLengthLabel = new JLabel("Frame Length");
		frameLengthLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JSpinner frameLengthSpinner = new JSpinner();
		frameLengthSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		 
		 
		cameraSettingPanel.add(roiXStartLabel, new GridBagConstraints(0, 0, 1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 6, 2, 2), 0, 0));
		cameraSettingPanel.add(roiXStartSpinner, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(roiXEndLabel, new GridBagConstraints(2, 0,1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(roiXEndSpinner, new GridBagConstraints(3, 0,1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(exposureTimeLabel, new GridBagConstraints(0, 0, 1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 6, 2, 2), 0, 0));
		cameraSettingPanel.add(exposureTimeSpinner, new GridBagConstraints(1, 0, 1, 1, 0.2, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(frameLengthLabel, new GridBagConstraints(2, 0,1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(frameLengthSpinner, new GridBagConstraints(3, 0,1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(roiYStartLabel, new GridBagConstraints(0, 1, 1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 6, 2, 2), 0, 0));
		cameraSettingPanel.add(roiYStartSpinner, new GridBagConstraints(1, 1,1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(roiYEndLabel, new GridBagConstraints(2, 1, 1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(roiYEndSpinner, new GridBagConstraints(3, 1,1, 1, 0.2, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(marginLabel, new GridBagConstraints(4, 0, 1, 1, 0.01, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(marginIdSelector, new GridBagConstraints(5, 0,1, 1, 0.02, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		cameraSettingPanel.add(marginSpinner, new GridBagConstraints(6, 0,1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		cameraSettingPanel.add(autoMarginButton, new GridBagConstraints(4, 1,1, 1, 0.01, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(muxLabel, new GridBagConstraints(5, 1,1, 1, 0.01, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 40, 2, 2), 0, 0));
		cameraSettingPanel.add(muxSpinner, new GridBagConstraints(6, 1,1, 1, 0.1, 0.1, GridBagConstraints.WEST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
//		cameraSettingPanel.add(cameraSettingApplyButton, new GridBagConstraints(3, 2, 1, 1, 0.2, 0.1, GridBagConstraints.WEST,
//				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		
		//add change actions
		frameLengthSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
					settingsManager.setFrameLength((int)frameLengthSpinner.getValue());
					
	        }
	    });
		exposureTimeSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
					settingsManager.setExposureTime((int)exposureTimeSpinner.getValue());
					//roiXStartSpinner.setValue(settingsManager.getRoiXStart());
	        }
	    });
		roiXStartSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
					settingsManager.setRoiXStart((int)roiXStartSpinner.getValue());
					roiXStartSpinner.setValue(settingsManager.getRoiXStart());
	        }
	    });
		roiXEndSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	           
					settingsManager.setRoiXEnd((int)roiXEndSpinner.getValue());		
					roiXEndSpinner.setValue(settingsManager.getRoiXEnd());
	        }
	    });
		roiYStartSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	          
					settingsManager.setRoiYStart((int)roiYStartSpinner.getValue());	
					roiYStartSpinner.setValue(settingsManager.getRoiYStart());
					
	        }
	    });
		roiYEndSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	        	settingsManager.setRoiYEnd((int)roiYEndSpinner.getValue());	
	            roiYEndSpinner.setValue(settingsManager.getRoiYEnd());
	           
	        }
	    });
		marginIdSelector.addActionListener((new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("margin id selected. val: "+marginValueArray[marginIdSelector.getSelectedIndex()] );
				marginSpinner.setValue(marginValueArray[marginIdSelector.getSelectedIndex()] );
            	
			}
		}));
		marginSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
	            	marginValueArray[marginIdSelector.getSelectedIndex()] = (int)marginSpinner.getValue();
	            	com.Sortex.controller.TCPClient.sendMargin((int)marginIdSelector.getSelectedItem(),(int)marginSpinner.getValue());
	           
	        }
	    });
		
		muxSpinner.addChangeListener(new ChangeListener() {
	        @Override
	        public void stateChanged(ChangeEvent e) {
	          
	        		settingsManager.setMux((int)muxSpinner.getValue());
	            	
	           
	        }
	    });
		
		autoMarginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				int[] marginsArray = new AutoCalibration().detectMarginsInBlackWhiteStrips(tcpTimeout);
				settingsManager.setMarginsArray(marginsArray);
				for(int i=Constants.NUMBER_OF_MARGINS-1;i>=0;i--){
			
					com.Sortex.controller.TCPClient.sendMargin(i,marginsArray[i]);
					
				}
				
			}
		});
		
		/*********************end camera setting panel*******************************************************/
		// add radio buttons
		JRadioButton stem = new JRadioButton("Stem");

		JRadioButton leaf = new JRadioButton("Leaf");
		ButtonGroup bG = new ButtonGroup();
		bG.add(stem);
		bG.add(leaf);
		panel1.add(stem);
		panel1.add(leaf);
		// read type of tea particles
		stem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				type = stem.getText();
				leaf.setEnabled(false);

			}
		});

		leaf.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				type = leaf.getText();
				stem.setEnabled(false);

			}
		});

		/**********************************
		 * Panel 2
		 ***********************************/
		panel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel categoryLabel = new JLabel("Tea Category");
		categoryLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		panel2.add(categoryLabel);
		
		categorySelection = new JComboBox<String>();
		categorySelection.addItem("OPA");
		categorySelection.addItem("OP1");
		categorySelection.addItem("BOP");
		categorySelection.addItem("BOPF");
		categorySelection.addItem("BOP 1");
		categorySelection.addItem("FBOP");
		categorySelection.addItem("FBOPF");
		categorySelection.setSize(new Dimension(60, 10));
		panel2.add(categorySelection);

		/****************************************** panel3 ******************/
		panel3 = new JPanel(new BorderLayout(10, 10));
		startButton = new JButton("Train");
		monitorButton = new JButton("Live View");
		
		captureButton = new JButton("Capture");
		captureAndFreezeButton = new JButton("Cap & Freeze");
		captureAndFreezeButton.setBackground(new Color(60, 10, 35));
		captureAndFreezeButton.setOpaque(true);
		resumeButton = new JButton("Resume Cap");
		resumeButton.setBackground(new Color(20, 80, 35));
		resumeButton.setOpaque(true);
		restartButton = new JButton("Restart");

		JLabel noOfFrames = new JLabel("Training Time (Minutes):");

		noOfFrames.setFont(new Font("Arial", Font.BOLD, 20));

		JPanel inputLabels = new JPanel();
		JPanel inputField = new JPanel();
		JPanel controlinputs = new JPanel(new BorderLayout());
		JPanel controls = new JPanel();
		
	//	controls.add(startButton);
		controls.add(saveButton);
		controls.add(applyButton);
		//controls.add(resetButton);
		controls.add(monitorButton);
		controls.add(captureButton);
		controls.add(captureAndFreezeButton);
		controls.add(resumeButton);
	//	controls.add(restartButton);
		inputLabels.add(noOfFrames);
		//inputField.add(timeList);
		controlinputs.add(inputLabels, BorderLayout.WEST);
		controlinputs.add(inputField, BorderLayout.CENTER);
		//panel3.add(controlinputs, BorderLayout.WEST);
		startButton.setBackground(new Color(6, 104, 35));
		startButton.setOpaque(true);
		monitorButton.setBackground(new Color(6, 104, 135));
		monitorButton.setOpaque(true);
		
		captureButton.setBackground(new Color(6, 54, 35));
		captureButton.setOpaque(true);
		restartButton.setBackground(new Color(120, 54, 35));
		restartButton.setOpaque(true);

		captureAndFreezeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Thread tcpThread = new Thread() {
					public void run() {
						TCPClient.sendCapAndFreeze();
						showCaptureAndFreezeProgress(settingsManager);
					}
				};
				tcpThread.start();
					
			
			}
		});
		resumeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Thread tcpThread = new Thread() {
					public void run() {
					TCPClient.sendResumeCap();
					settingsManager.setCaptureLimit(-1);
					}
				};
				tcpThread.start();
			
			}
		});
		restartButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				
					restartApplication();
			
			}
		});
		captureButton.addActionListener(new ActionListener() {
			Thread thread2;
			JFrame frame = new JFrame("Capture frames");
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int captureLimit = settingsManager.getCaptureLimit();
				String msg = "";
				if(captureLimit == -1) {
					msg = "Continuos Capture is enabled. ";
				}
				else {
					msg = "Frame buffer size: "+captureLimit;
				}
				 String numberOfFrames = JOptionPane.showInputDialog(frame, msg+"\nNumber of frames to capture");
				
				if(thread2!=null){
					TCPClient.tcpReceive = false;
					thread2.interrupt();
				}
				
				thread2 = new Thread() {
					public void run() {
						TCPClient.tcpReceive = true;
						
						try {
							if(numberOfFrames != null) {
								int number = Integer.parseInt(numberOfFrames);
								System.out.println("starting capture "+numberOfFrames+ " frames");
								TCPClient.getFramesRealTime(number,settingsManager);
							}
							
						}catch(java.lang.NumberFormatException e) {
							System.out.println("Invalid input");
						}
					}
						
				
			};
			thread2.start();
			}
		});
				

	

		saveButton.addActionListener(new ActionListener() {

			

			@Override
			public void actionPerformed(ActionEvent arg0) {
				settingsManager.saveSettings();

			}
		});
		applyButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				settingsManager.applySettings();

			}
		});
		


		/*******************************************
		 * panel 4
		 ***************************************/
		panel4 = new JPanel(new BorderLayout());
		ImageIcon loadingIcon = new ImageIcon(this.getClass().getResource("/com/Sortex/images/loader1.gif"));
		Image image = loadingIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(450, 30, Image.SCALE_FAST);
		loadingIcon = new ImageIcon(newimg); // transform it back
		imgLabel = new JLabel(loadingIcon);
		panel4.add(controls, BorderLayout.CENTER);

		/******************** panel 5 ***************************************/
		panel5 = new JPanel(new BorderLayout());
		ImageIcon logoIcon = new ImageIcon(this.getClass().getResource("/com/Sortex/images/SORTEX.png"));
		Image image2 = logoIcon.getImage(); // transform it
		Image newimg2 = image2.getScaledInstance(80, 60, Image.SCALE_FAST);
		logoIcon = new ImageIcon(newimg2);
		JLabel logo = new JLabel(logoIcon);
		
		
		panel5.add(logo, BorderLayout.EAST);

		/************************** delay panel ********************/
		
		JPanel delaySettingPanel = new JPanel();
		delaySettingPanel = new JPanel(new GridBagLayout());
		
		TitledBorder delayPanelBorder = new TitledBorder("Delay Settings");
		delayPanelBorder.setTitleFont(new Font("Arial", Font.BOLD, Constants.PANEL_TITLE_FONT_SIZE));
		delayPanelBorder.setTitleColor(new Color(6, 154, 35));
		delayPanelBorder.setTitleJustification(TitledBorder.CENTER);
		delayPanelBorder.setTitlePosition(TitledBorder.TOP);
		delaySettingPanel.setBorder(delayPanelBorder);
		
		
		JLabel delayLable = new JLabel("Granualty");
		delayLable.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JLabel ejectDurationLabel = new JLabel("On Steps");
		ejectDurationLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		JLabel delayStepsLabel = new JLabel("Delay Steps");
		delayStepsLabel.setFont(new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
		
		delayGranulatySpinner = new JSpinner();
		delayGranulatySpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		ejectDurationSpinner = new JSpinner();
		ejectDurationSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		delayStepsSpinner = new JSpinner();
		delayStepsSpinner.setFont(new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
		
		JPanel delayTextPanel = new JPanel(new GridBagLayout());
		JLabel delayTimeLabel = new JLabel("Delay(us)");
		JLabel onTimeLabel = new JLabel("On Time(us)");
		
		JTextField delayTime = new JTextField();
		delayTime.setFont(new Font("Arial", Font.BOLD, Constants.NON_EDITABLE_VALUE_FONT_SIZE));
		delayTime.setEditable(false);
		JTextField onTime = new JTextField();
		onTime.setFont(new Font("Arial", Font.BOLD, Constants.NON_EDITABLE_VALUE_FONT_SIZE));
		onTime.setEditable(false);
		
		delayTextPanel.add(delayTimeLabel,new GridBagConstraints(0, 0, 1, 1, 0.05, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		delayTextPanel.add(delayTime,new GridBagConstraints(1, 0, 1, 1, 0.05, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		delayTextPanel.add(onTimeLabel,new GridBagConstraints(0, 1, 1, 1, 0.05, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		delayTextPanel.add(onTime,new GridBagConstraints(1, 1, 1, 1, 0.05, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		
		
		delaySettingPanel.add(delayLable,new GridBagConstraints(3, 0, 1, 1, 0.05, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		delaySettingPanel.add(delayGranulatySpinner,new GridBagConstraints(4, 0, 1, 1, 0.3, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
			
		
		delaySettingPanel.add(delayStepsLabel,new GridBagConstraints(5, 0, 1, 1, 0.05, 0.1,	GridBagConstraints.EAST,	
				GridBagConstraints.BOTH, new Insets(2, 20, 2, 2), 0, 0));
		delaySettingPanel.add(delayStepsSpinner,new GridBagConstraints(6, 0, 1, 1, 0.3, 0.1, GridBagConstraints.EAST,	GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		delaySettingPanel.add(ejectDurationLabel,new GridBagConstraints(7, 0, 1, 1, 0.03, 0.1, GridBagConstraints.CENTER,
				GridBagConstraints.BOTH, new Insets(2, 20, 2, 2), 0, 0));
		delaySettingPanel.add(ejectDurationSpinner,new GridBagConstraints(8, 0, 1, 1, 0.3, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		delaySettingPanel.add(delayTextPanel,new GridBagConstraints(9, 0, 1, 1, 0.3, 0.1, GridBagConstraints.EAST,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		delayGranulatySpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
//	            System.out.println(delayGranulatySpinner.getValue());
	        		
	        		settingsManager.setDelayGranularity((int)delayGranulatySpinner.getValue());
					int delay = settingsManager.getDelayGranularity()*settingsManager.getEjectDelaySteps()*2*10/1000;
	        		delayTime.setText(Integer.toString(delay));
	        		
	        		int onTimeValue = settingsManager.getDelayGranularity()*settingsManager.getEjectorOndelaySteps()*2*10/1000;
	        		onTime.setText(Integer.toString(onTimeValue));
				
	        }
	    });
		
		
		delayStepsSpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
//	            System.out.println(delayGranulatySpinner.getValue());
	        		settingsManager.setEjectDelaySteps((int)delayStepsSpinner.getValue());
	        		int delay = settingsManager.getDelayGranularity()*settingsManager.getEjectDelaySteps()*2*10/1000;
	        		delayTime.setText(Integer.toString(delay));
//					com.Sortex.controller.TCPClient.sendDelaySteps();
				
	        }
	    });
		
		ejectDurationSpinner.addChangeListener(new ChangeListener() {

	        @Override
	        public void stateChanged(ChangeEvent e) {
	            
//	            System.out.println(delayGranulatySpinner.getValue());
	        		settingsManager.setEjectorOndelaySteps((int)ejectDurationSpinner.getValue());
	        		int onTimeValue = settingsManager.getDelayGranularity()*settingsManager.getEjectorOndelaySteps()*2*10/1000;
	        		onTime.setText(Integer.toString(onTimeValue));
//					com.Sortex.controller.TCPClient.sendEjectOnSteps();
				
	        }
	    });
		sensitivitySlider.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				sensitivityValue = sensitivitySlider.getValue();
				sensitivityValueLabel.setText(String.valueOf(sensitivityValue));
				
				settingsManager.setCertantity(sensitivityValue);
//					com.Sortex.controller.TCPClient.sendSensitivityParams(sensitivityValue);
			

			}
		});
		
		
		
		
		monitorButton.addActionListener(new ActionListener() {
			Thread thread2;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("Toggle monitoring "+isMonitoring);
				
				if(thread2!=null){
					TCPClient.tcpReceive = false;
//					thread2.interrupt();
				}
				
				thread2 = new Thread() {
					public void run() {
						if(isMonitoring) {
							TCPClient.tcpReceive = false;
							isMonitoring = false;
						}else {
							isMonitoring = true;
							TCPClient.tcpReceive = true;
							TCPClient.getFramesRealTime(0,settingsManager);
						}	
					}
				};
				thread2.start();
				
	
			}
		});

						
					
				
	
		container.add(thresholdPanel, new GridBagConstraints(0, 0, 2, 1, 1, 0.9, GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0));

		container.add(delaySettingPanel, new GridBagConstraints(0, 1, 2, 1, 1, 0.9, GridBagConstraints.WEST,GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

		container.add(cameraSettingPanel, new GridBagConstraints(0, 3, 2, 1, 1, 0.9, GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0));
		container.add(panel5, new GridBagConstraints(1, 5, 1, 1, 0.2, 0.9, GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0));
		container.add(panel4, new GridBagConstraints(0, 4, 1, 1, 4, 0.9, GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0));
		
		
		//log panel
		CapturePane capturePane = new CapturePane();
		  PrintStream ps = System.out;
          System.setOut(new PrintStream(new StreamCapturer("STDOUT", capturePane, ps)));

          container.add( capturePane , new GridBagConstraints(0, 5, 1, 2, 4, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,new Insets(2, 2, 2, 2), 0, 0));
  		
          TCPClient.isSendDataEnabled = false;
          
		try {
			
			backgroundThresholdSlider.setValue(settingsManager.getBgThrshold());
			Thread.sleep(5);
			stemLeafThresholdSlider.setValue(settingsManager.getStemLeafThreshold());
			Thread.sleep(5);
			sensitivitySlider.setValue(settingsManager.getCertantity());
			Thread.sleep(5);
			minCountSpinner.setValue(settingsManager.getMinStemCount());
			Thread.sleep(5);
			delayGranulatySpinner.setValue(settingsManager.getDelayGranularity());
			Thread.sleep(5);
			delayStepsSpinner.setValue(settingsManager.getEjectDelaySteps());
			Thread.sleep(5);
			ejectDurationSpinner.setValue(settingsManager.getEjectorOndelaySteps());
			Thread.sleep(5);
			//roiXStartSpinner.setValue(settingsManager.getRoiXStart());
			//roiXEndSpinner.setValue(settingsManager.getRoiXEnd());
	
			roiYStartSpinner.setValue(settingsManager.getRoiYStart());
			Thread.sleep(5);
			roiYEndSpinner.setValue(settingsManager.getRoiYEnd());
			Thread.sleep(5);
			muxSpinner.setValue(settingsManager.getMux());
			Thread.sleep(5);
			exposureTimeSpinner.setValue(settingsManager.getExposureTime());
			Thread.sleep(5);
			frameLengthSpinner.setValue(settingsManager.getFrameLength());
			Thread.sleep(5);
			
			marginValueArray = settingsManager.getMarginsArray();
			Thread.sleep(5);
			for(int i=Constants.NUMBER_OF_MARGINS-1;i>=0;i--){
				marginIdSelector.setSelectedIndex(i);
				marginSpinner.setValue(marginValueArray[i]);
				Thread.sleep(5);
			}
			
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			System.out.println("set setting values failed");
		}
		TCPClient.isSendDataEnabled = true;
		return container;
	}

	

	public JSlider getSlider(int min, int max, int init, int mjrTkSp, int mnrTkSp) {
		JSlider slider = new JSlider(JSlider.HORIZONTAL, min, max, init);
		slider.setPaintTicks(true);
		slider.setMajorTickSpacing(mjrTkSp);
		slider.setMinorTickSpacing(mnrTkSp);
		slider.setPaintLabels(true);
		return slider;
	}

	public boolean isLastLine() throws IOException {
		br = new BufferedReader(new FileReader("../TCPClient/result.txt"));

		String lastLine = "";

		String sCurrentLine;
		while ((sCurrentLine = br.readLine()) != null) {
			// System.out.println(sCurrentLine);
			lastLine = sCurrentLine;
		}
		System.out.println(lastLine);
		if (lastLine == "END") {
			return true;
		} else {
			return false;
		}

	}

	public void display(String folderName) throws IOException {
		JFrame frame = new JFrame();
		frame.setVisible(true);
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(6, 6, 6, 6);
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.fill = GridBagConstraints.VERTICAL;
		JButton backButton = new JButton("Back");
		frame.add(mainPanel);
		frame.setSize(800, 480);
		JPanel panel1 = new JPanel();
		JPanel panel2 = new JPanel();
		JButton nextButton = new JButton("Next");
		JButton prevButton = new JButton("Previous");
		panel2.add(nextButton);
		panel2.add(prevButton);
		panel2.add(backButton);
		File[] fileList = readImages(folderName);
		String imagepath = folderName + "/" + fileList[0].getName();
		frameIcon = new ImageIcon(imagepath);
		frameLabel = new JLabel(frameIcon);
		panel1.add(frameLabel);
		currentPosition = 0;
		backButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();

			}
		});

		nextButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				currentPosition++;
				if (currentPosition > fileList.length) {
					JOptionPane.showMessageDialog(null, "End of files");
				} else {
					panel1.remove(frameLabel);
					String imagepath = folderName + "/" + fileList[currentPosition].getName();
					System.out.println(imagepath);
					frameIcon = new ImageIcon(imagepath);
					frameLabel = new JLabel(frameIcon);

					panel1.add(frameLabel);
					panel1.repaint();
					panel1.validate();
				}

			}
		});

		prevButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				currentPosition--;
				if (currentPosition < 0) {
					JOptionPane.showMessageDialog(null, "Begin");
				} else {
					panel1.remove(frameLabel);
					String imagepath = folderName + "/" + fileList[currentPosition].getName();
					System.out.println(imagepath);
					frameIcon = new ImageIcon(imagepath);
					frameLabel = new JLabel(frameIcon);

					panel1.add(frameLabel);
					panel1.repaint();
					panel1.validate();
				}

			}
		});

		mainPanel.add(panel1, new GridBagConstraints(0, 1, 3, 1, 2, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH,
				new Insets(2, 2, 2, 2), 0, 0));

		mainPanel.add(panel2, new GridBagConstraints(0, 2, 1, 1, 2, 1, GridBagConstraints.PAGE_END,
				GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
		
		

	}

	public File[] readImages(String folderName) throws IOException {
		File folder = new File(folderName);
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				System.out.println(listOfFiles[i].getName());
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Directory " + listOfFiles[i].getName());
			}
		}

		return listOfFiles;
	}
	
	
	

//	private Thread createFrameReciveingThread(boolean createWindow) {
//		return new Thread() {
//			
//			public void run() {
//				try {
//					if(isMonitoring){
//						com.Sortex.controller.Controller.stopMonitoring();
//						isMonitoring = false;
//						WatchdogTimer.disable();
//					}
//					else{
//						if(createWindow){
//							com.Sortex.controller.Controller.startMonitoring();
//						}
//						isMonitoring = true;
//						
//						com.Sortex.controller.TCPClient.getFrames("stemRowData",tcpTimeout,-1,false);
//						
//						
//					}
//				} catch (UnknownHostException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//			}
//		};
//	}

	/** 
	 * Sun property pointing the main class and its arguments. 
	 * Might not be defined on non Hotspot VM implementations.
	 */
	public static final String SUN_JAVA_COMMAND = "sun.java.command";

	private BufferedReader br;
	public static void restartApplication()  {
		String[] mainCommand = System.getProperty(SUN_JAVA_COMMAND).split(" ");
		try {
			Runtime.getRuntime().exec("java -jar "+ new File(mainCommand[0]).getPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Restart Failed!");
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public void showCaptureAndFreezeProgress(SettingsManager settingsManager) {
		
		
		 JOptionPane opt = new JOptionPane("Capturing frames...", JOptionPane.WARNING_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}); // no buttons
     	  final JDialog dlg = opt.createDialog("Capturing");
    
	    
	    new Thread(new Runnable()
	          {
	            public void run()
	            {
	            		int isDone = 0;
	              try
	              {
	            	 
	          	  
	          	  
	          	  
	          	  System.out.println("geting isDone");
	            	  while(isDone == 0) {
	                Thread.sleep(1000);
	                isDone = TCPClient.getIsDone();
	                System.out.println("capturing.. "+isDone);
	               
	               
	            	  }
	            	 
	            		  System.out.println("captured "+isDone+ " frames");
	            	  
	            	  settingsManager.setCaptureLimit(isDone);
	            	  dlg.dispose();
	              }
	              
	              catch ( Throwable th )
	              {
	               
	              }
	            }
	          }).start();
	    dlg.setVisible(true);
	   
	}
}
