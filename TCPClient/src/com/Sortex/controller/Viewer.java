package com.Sortex.controller;
import java.awt.FlowLayout;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
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

	 ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	public void convertToRGB8andView(byte[] buf,int width,int height) {
		int pixelCount = 0;
		long pixel;
		byte[] rgb8Buf = new byte[buf.length/4*3];
		for(int p=0;p<buf.length;p+=4){
			pixel = getUInt32(Arrays.copyOfRange(buf, p, p+4));
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,30))/Math.pow(2,20)/4); 
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,10))/4); 
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,20))/Math.pow(2,10)/4); 

		}
		
		DataBufferByte buffer = new DataBufferByte(rgb8Buf, rgb8Buf.length);
	   
	   
		icon.setImage( new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null));

		frame.pack();
		frame.setVisible(true);
		frame.repaint();
	}
	
	public static long getUInt32(byte[] bytes) {
	    long value = byteAsULong(bytes[0]) | (byteAsULong(bytes[1]) << 8) | (byteAsULong(bytes[2]) << 16) | (byteAsULong(bytes[3]) << 24);
	    return value;
	}
	public static long byteAsULong(byte b) {
	    return ((long)b) & 0x00000000000000FFL; 
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
