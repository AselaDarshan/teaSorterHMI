package com.Sortex.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class AutoCalibration {
	public ArrayList<Integer> detectMargins(int tcpTimeout,int width,int height){
		ArrayList<Integer> margins = new ArrayList<Integer>();
		
		
		try {
			//Retrieve image from camera
			com.Sortex.controller.TCPClient.getFrames("stemRowData",tcpTimeout,3);
			
			BufferedImage bufferedImage = new ImageHandler().rgb32Torgb24BufferdImage( FrameBuffer.getFromBuffer(), width, height);
			

			for(int x=0;x<width;x++){
				bufferedImage.getRGB(x, height/2);
			}
			//detect margins from image
			margins.add(128);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return margins;
	}
}
