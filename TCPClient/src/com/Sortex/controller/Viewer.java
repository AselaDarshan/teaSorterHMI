package com.Sortex.controller;
import java.awt.FlowLayout;
import java.awt.List;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Viewer {

	private JFrame frame = new JFrame();
	private  ImageIcon icon;
	private  JLabel label;

	public  void initialize() {

		frame.getContentPane().setLayout(new FlowLayout());
		frame.setLocation(350, 310);

		icon = new ImageIcon();
		label = new JLabel();
		label.setIcon(icon);
		frame.getContentPane().add(label);
		frame.pack();
		frame.setVisible(true);
	}

	public void updateFrame() throws IOException {
		Queue<String> imageList = new LinkedList<String>();

		Files.walk(Paths.get("OutputImages")).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				imageList.add(filePath.getFileName().toString());

				System.out.println("File name: " + filePath.getFileName());
			}
		});
		String path;
		for (int i = 0; i < imageList.size(); i++) {
			path = "OutputImages/" + imageList.poll();
			visibleImage(ImageIO.read(new File(path)));
		}

	}

	public void visibleImage(BufferedImage image) {

		icon.setImage(image);
//		frame.pack();
//		frame.setVisible(true);
		// label.setIcon(icon);
		frame.pack();
		frame.setVisible(true);
		frame.repaint();

	}
	public void close() {
		frame.dispose();
	}
}
