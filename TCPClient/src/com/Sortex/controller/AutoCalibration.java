package com.Sortex.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;



public class AutoCalibration {
	public ArrayList<Integer> detectMargins(int tcpTimeout){
		ArrayList<Integer> margins = new ArrayList<Integer>();
		SettingsManager settingsManager =  new SettingsManager();
		settingsManager.retriveSettingsFromCamera();
		int width = settingsManager.getFrameWidth();
		int height = settingsManager.getFrameHeight();
		try {
			//Retrieve image from camera
			com.Sortex.controller.TCPClient.getFrames("stemRowData",tcpTimeout,3);
			
			byte[]  image = new ImageHandler().rgb32torgb24(FrameBuffer.getFromBuffer());
			int row = 0;
			int startIndex = height/2*width*3;
			int endIndex = startIndex+(width-1)*3;
			if(image != null){
				int startRow = 0;
				int margin;
				for(int x=startIndex;x<endIndex;x = x+3){
					//detect margins from image
					row = (x-startIndex)/3;
					System.out.println(row);
					int r =  Byte.toUnsignedInt(image[x] );
					int r1 =  Byte.toUnsignedInt(image[x+3] );//right pixel
					int r2 =  Byte.toUnsignedInt(image[x+width] );//down pixel
					System.out.println("pixel "+(x/3)+" "+r);
					
					if(r < Constants.MARGIN_THRESHOLD ){
						
						startRow = row;
						while(r < Constants.MARGIN_THRESHOLD && x<endIndex){
							x=x+3;
							
							r =  Byte.toUnsignedInt(image[x] );
						}
						row = (x-startIndex)/3;
						margin = (startRow+row)/2;
						System.out.println("margin "+margin );
						margins.add(margin );
					
						for(int y=0;y<height;y++){
							image[y*width*3+(margin *3)] = (byte) 0;
							image[y*width*3+(margin *3)+1] = (byte) 0;
							image[y*width*3+(margin *3)+2] = (byte) 255;
						}
						
					}
					
						
					
				}
				
				
				BufferedImage bufferedImage = new ImageHandler().createRGBImage(image, width, height);
				
				//show margins
				Viewer viewer = new Viewer();
				viewer.initialize();
				viewer.visibleImage(bufferedImage);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(margins.size()+ "margins detected");
		return margins;
	}
}
