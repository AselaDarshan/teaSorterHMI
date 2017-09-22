package com.Sortex.frontendController;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.Sortex.controller.Constants;
import com.Sortex.controller.SettingsManager;

public class Window1 {

	JFrame frame;
	Window2 window2;
	JPanel panel;
	JPanel panel2;
	JPanel panel3;
	JPanel container;
	JButton backButton;
	Window3 window3;
	JPanel panel4;
	JTabbedPane jtp;
	int selectedIndex = 0;
	JPanel mainPanel;

	public void init() {
		mainPanel = new JPanel();
		frame = new JFrame("SORTEX");
		frame.setResizable(false);
		jtp = new JTabbedPane();
		frame.getContentPane().add(jtp);
		window2 = new Window2();
		panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		container = new JPanel();
		panel3 = new JPanel();
		panel3.setBackground(Color.cyan);
		window3 = new Window3();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
			UIManager.put("Button.font", new Font("Arial", Font.BOLD, Constants.BUTTON_FONT_SIZE));
			UIManager.put("Label.font",new Font("Arial", Font.BOLD, Constants.TITLE_FONT_SIZE));
			UIManager.put("RadioButton.font", new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
			UIManager.put("ComboBox.font", new Font("Arial", Font.BOLD, Constants.VALUE_FONT_SIZE));
			UIManager.put("Spinner.font", new Font("Arial", Font.BOLD, 25));
			UIManager.put("Slider.font", new Font("Arial", Font.BOLD, Constants.SLIDER_VALUE_FONT_SIZE));
		//	UIManager.put("TextField.font", new Font("Arial", Font.BOLD, Constants.SLIDER_VALUE_FONT_SIZE));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Window1 window = new Window1();
		window.init();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				window.makeUI();
			}
		});
	}

	public void makeUI() {

		panel2 = window2.init();
		SettingsManager settingsManger = new SettingsManager();
		panel4 = window3.createTestPanel(settingsManger);
		
		
		window3.setValues(settingsManger);
		frame.setSize(800, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jtp.addTab("Settings", panel4);
		jtp.addTab("Statistics", panel2);
		frame.setVisible(true);
		jtp.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				selectedIndex = jtp.getSelectedIndex();
				System.out.println("You are in tab : " + selectedIndex);

				
			
			}
		});
		

		frame.addWindowListener(new WindowAdapter(){
		                public void windowClosing(WindowEvent e){
		                	System.out.println("Closing application..");
		                    settingsManger.saveSettings();
		                    System.out.println("settings saved");
		                }
		            });

	}

}
