package com.Sortex.controller;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;





public class AutoCalibration {
	public int[] detectMarginsInBlackWhiteStrips(int tcpTimeout){
		int[] margins = new int[Constants.NUMBER_OF_MARGINS];
		SettingsManager settingsManager =  new SettingsManager();
		settingsManager.retriveSettingsFromCamera();
		int width = settingsManager.getFrameWidth();
		int height = settingsManager.getFrameHeight();
		int marginIndex = 0;
		System.out.println("auto detecting margins using black and white strips. h:"+height+" w:"+width);
	
			//Retrieve image from camera
			System.out.println("capturing frame..");
			//Retrieve image from camera
			com.Sortex.controller.TCPClient.getFramesRealTime(3, settingsManager);
			
			byte[]  image = new byte[width*height*4];
			
			try {
				InputStream inputStream = new FileInputStream( Constants.SNAPSHOT_SAVE_FOLDER+"/2");
				System.out.println("reading saved image file...");
				while (inputStream.read(image) != -1) {}
				System.out.println("successfully read the image file.");
				inputStream.close();
			} catch (IOException ex) {
				System.out.println("image file reading failed !");
				image = null;
				ex.printStackTrace();
			}
			int row = 0;
			int startIndex = height/2*width*3;
			int endIndex = startIndex+(width-1)*3;
			
			image =  new ImageHandler().rgb32torgb24(image);
			if(image != null){
				//int startRow = 0;
				int margin;
				boolean nextIsWhite = true;
				System.out.println("capturing completed. start detecting margins..");
				for(int x=startIndex;x<endIndex;x = x+3){
					//detect margins from image
					row = (x-startIndex)/3;
					System.out.println(row);
					int r =  Byte.toUnsignedInt(image[x] );
				//	int r1 =  Byte.toUnsignedInt(image[x+3] );//right pixel
				//	int r2 =  Byte.toUnsignedInt(image[x+width] );//down pixel
					System.out.println("pixel#: "+((x-startIndex)/3)+" value:"+r);
					
					if(nextIsWhite && r >= Constants.MARGIN_THRESHOLD ){
							
							
						margin = (x-startIndex)/3;
						
							System.out.println("detected white margin. margin:"+margin +" index: "+marginIndex);
							
							margins[marginIndex++] = margin;
							for(int y=0;y<height;y++){
								image[y*width*3+(margin *3)] = (byte) 0;
								image[y*width*3+(margin *3)+1] = (byte) 0;
								image[y*width*3+(margin *3)+2] = (byte) 255;
							}
							nextIsWhite =false;
						}
					
					if(!nextIsWhite && r < Constants.MARGIN_THRESHOLD){
						margin = (x-startIndex)/3;
						System.out.println("detected black margin. margin:"+margin +" index: "+marginIndex);
						margins[marginIndex++] = margin;
						for(int y=0;y<height;y++){
							image[y*width*3+(margin *3)] = (byte) 0;
							image[y*width*3+(margin *3)+1] = (byte) 0;
							image[y*width*3+(margin *3)+2] = (byte) 255;
						}
						nextIsWhite =true;
					}
					if(marginIndex>=Constants.NUMBER_OF_MARGINS) {
						//System.out.println(Constants.NUMBER_OF_MARGINS+" margins detected");
						break;
					}
					
				}
				
				System.out.println("Margin deceting completed");
				BufferedImage bufferedImage = new ImageHandler().createRGBImage(image, width, height);
				
				//show margins
				Viewer viewer = new Viewer();
				viewer.initialize();
				viewer.visibleImage(bufferedImage);
				//viewer.convertToRGB8andView(image, width, height);
			}
			else {
				System.out.println("margin detecting failed. capturing failed! try again");
			}
		
		
		System.out.println(marginIndex+ "margins detected");
		return margins;
	}
	public int[] detectMarginsBlackLines(int tcpTimeout){
		int[] margins = new int[Constants.NUMBER_OF_MARGINS];
		SettingsManager settingsManager =  new SettingsManager();
		settingsManager.retriveSettingsFromCamera();
		int width = settingsManager.getFrameWidth();
		int height = settingsManager.getFrameHeight();
		int marginIndex = 0;
		System.out.println("auto detecting margins using black lines. h:"+height+" w:"+width);
			//Retrieve image from camera
			com.Sortex.controller.TCPClient.getFramesRealTime(3, settingsManager);
			
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
			//		int r1 =  Byte.toUnsignedInt(image[x+3] );//right pixel
			//		int r2 =  Byte.toUnsignedInt(image[x+width] );//down pixel
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
						margins[marginIndex++] = margin;
						if(marginIndex >= Constants.NUMBER_OF_MARGINS){
							break;
						}
					
						for(int y=0;y<height;y++){
							image[y*width*3+(margin *3)] = (byte) 0;
							image[y*width*3+(margin *3)+1] = (byte) 0;
							image[y*width*3+(margin *3)+2] = (byte) 255;
						}
						
					}
					
				}
					
				}
				
				
				BufferedImage bufferedImage = new ImageHandler().createRGBImage(image, width, height);
				
				//show margins
				Viewer viewer = new Viewer();
				viewer.initialize();
				viewer.visibleImage(bufferedImage);
			
		
		System.out.println(marginIndex+ "margins detected");
		return margins;
	}
}
