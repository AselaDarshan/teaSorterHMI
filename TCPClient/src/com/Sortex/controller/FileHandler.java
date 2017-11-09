package com.Sortex.controller;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class FileHandler {
	
	public static void saveAllAsGif(int width, int height, String folder) {
		System.out.println("saving as gif..");
		BufferedImage currentImage;
	     final File dir = new File(folder);
		File[] list = dir.listFiles();
		
		String outPutFolder = null;
		
		if(folder=="stemRowData")
		{
			outPutFolder="testInStem";
			
		}
		if(folder=="leafRowData")
		{
			outPutFolder="testInLeaf";
		}
		else outPutFolder = folder+"/gif";
		
		new File(outPutFolder).mkdir();
		
		for (int inc = 0; inc < list.length; inc++) {
			String filename = dir.getName() + "/" + list[inc].getName();
			ImageReader imageReader = new ImageReader(width, height);

			currentImage = imageReader.getBufferedImage24bit(filename);
			
			String path = outPutFolder+"/" + list[inc].getName().split("[.]")[0]+".gif" ;
			try {

				ImageIO.write(currentImage, "GIF", new File(path));
				
			} catch (IOException e) {

				e.printStackTrace();
			}

		}
		System.out.println("Completed!");
	}
	
	public static void saveAsGIF(int width,int height,String path){
		ImageReader imageReader = new ImageReader(width, height);

		BufferedImage currentImage = imageReader.getBufferedImage24bit(path);
		
		String outFileName = path.split("[.]")[0]+".gif" ;
		try {

			ImageIO.write(currentImage, "GIF", new File(outFileName));
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	public static void writeFile(byte[] rawData, String fileName,String directoryName) {
//		System.out.println("Writing row datafiles ..............");
		new File(directoryName).mkdir();
		// long starttime;
		// long time;
		String path = directoryName+"/" + fileName;
		try {
			// starttime=System.nanoTime();
			Files.write(Paths.get(path), rawData);
			// time=System.nanoTime()-starttime;
		} catch (IOException e) {

			e.printStackTrace();
		}

	}
}