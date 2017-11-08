package com.Sortex.controller;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
public class Controller {
	private static Timer timer = new Timer();
	
	private static int imageNumber=0;
	private static boolean monitor;
	
	private static Viewer viewer;
	public static void stopMonitoring(){
		System.out.println("stop monitoring");

		monitor = false;
		TCPClient.tcpReceive = false;
		viewer.close();
	}
	public static void startMonitoring(){
		System.out.println("start monitoring");
		viewer = new Viewer();
		viewer.initialize();
		monitor = true;
	
	}
	public static void start(int numberOfFrames,String directoryName,final int width,final int height,final boolean fileSave){
		
		
		
		try{
		timer.scheduleAtFixedRate(new TimerTask() {
			  @Override
			  public void run() {
				 // new Thread(new Runnable() {
				  //       public void run(){
							  byte[] buf = FrameBuffer.getFromBuffer();
							  if(buf!=null){
								  
							 
							//	FileHandler.convertImages(buf, imageNumber++);
								byte[] rgb8Buf = new byte[buf.length/4*3];
								int pixelCount = 0;
								long pixel;
								for(int i=0;i<buf.length;i+=4){
									pixel = getUInt32(Arrays.copyOfRange(buf, i, i+4));
									rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,30))/Math.pow(2,20)/4); 
									rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,10))/4); 
									rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,20))/Math.pow(2,10)/4); 
//									
									
									
//									if(i%4!=0){
//										rgb8Buf[pixelCount++] = buf[i];
//									}
								}
								if(monitor){
									viewer.visibleImage(createRGBImage(rgb8Buf,width ,height));
									//System.out.println("monitoring");
								}
								else if(fileSave){
									FileHandler.writeFile(rgb8Buf, new String(""+imageNumber),directoryName);
									imageNumber++;
									System.out.println(imageNumber);
									if(imageNumber>numberOfFrames){
										System.out.println("**********************");
										imageNumber=0;
										
											this.cancel();
										
									}
								}
								else{
									try{
										this.cancel();
									}
									catch (java.lang.IllegalStateException e) {
										// TODO: handle exception
									}
								}
													  
							  }
				//         }
				//  }).start(); 

			  }
			  
			}, 5, 20);
		}
		catch (java.lang.IllegalStateException e) {
			// TODO: handle exception
		}
	}
	private static BufferedImage createRGBImage(byte[] bytes, int width, int height) throws java.awt.image.RasterFormatException {
//	    System.out.println("creating:"+width+"*"+height);
		DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
	    ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
	}
	public static long getUInt32(byte[] bytes) {
	    long value = byteAsULong(bytes[0]) | (byteAsULong(bytes[1]) << 8) | (byteAsULong(bytes[2]) << 16) | (byteAsULong(bytes[3]) << 24);
	    return value;
	}
	public static long byteAsULong(byte b) {
	    return ((long)b) & 0x00000000000000FFL; 
	}
}
