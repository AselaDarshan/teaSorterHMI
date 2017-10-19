package com.Sortex.controller;

import java.awt.TrayIcon.MessageType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.*;
import java.util.Timer;

import javax.sound.midi.Soundbank;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

public class TCPClient {
	final static int NUMBER_OF_BYTES_PER_PACKET = 40000;//61440
	final static int PACKET_SIZE_IN_BYTES = 61440;//61440
	final static int NUMBER_OF_BYTES_PER_LINE = 100*4;// 6 bytes
	final static int NUMBER_OF_LINES_PER_FRAME = 100;
	final static int NUMBER_OF_BYTES_PER_FRAME = NUMBER_OF_BYTES_PER_LINE * NUMBER_OF_LINES_PER_FRAME;
//	public static int numberOfFrames;
	final static int HEADER_BACKGORUND_THRESHOLD = 0x0F;
	final static int HEADER_STEM_LEAF_THRESHOLD = 0x10;
	final static int HEADER_MONITOR_ON = 160;
	final static int HEADER_MONITOR_OFF = 128;
	final static int HEADER_SENSITIVITY_VALUE =0x12;
	final static int HEADER_DELAY_STEPS = 0x15;
	final static int HEADER_DELAY_GRANULARITY = 0x14;
	final static int HEADER_EJECT_ON_STEPS = 0x16;
	final static int HEADER_ROI_X = 0x05; 
	final static int HEADER_ROI_Y_START = 0x06; 
	final static int HEADER_ROI_Y_END = 0x07; 
	final static int HEADER_MARGIN = 0x0A; 
	final static int HEADER_MUX =0x19; 
	final static int HEADER_MIN_STEM_COUNT =0x11; 
	
	final static int HEADER_CURRENT_VIEW_WIDTH =0x1E; 
	final static int HEADER_CURRENT_VIEW_HEIGHT =0x1F; 

	private static Timer timer = new Timer();
	static Socket clientSocket;
	static DataOutputStream outToServer;
	static InputStream in;
	static DataInputStream dis;
	public static boolean status = false;
	
	
	public static boolean tcpReceive;
	public static boolean isSendDataEnabled = true;
	
	public static boolean buildServerConnection()  {
		
				//return false;
			try {
				clientSocket = new Socket("10.0.1.1", 7000);
	//			clientSocket = new Socket("192.168.1.12", 7000);
			
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
				in = clientSocket.getInputStream();
			} catch (IOException e) {
				
				JOptionPane.showMessageDialog(null , "Not Connected!");
				e.printStackTrace();
				return false;
			}
			dis = new DataInputStream(in);
			System.out.println("Connected to server");
			return true;
		
		
	}

	public static void sendStemLeafTresholds(int param1, int param2, int param3) {
	
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_STEM_LEAF_THRESHOLD);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(param1/256);
		paramBuffer[0] = toByte(param1%256);
		sendDataToCamera(paramBuffer);

	}
	
	public static void sendMinStemCount(int value) {
		System.out.println("send Min stem count: "+value);
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_MIN_STEM_COUNT);
		
		paramBuffer[0] = toByte(value%256);
		paramBuffer[1] = toByte(value%(256*256)/256);
		paramBuffer[2] = toByte(value%(256*256*256)/(256*256));
		sendDataToCamera(paramBuffer);

	}
	public static void sendMux(int value){
		System.out.println("send Mux: "+value);
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_MUX);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(value);
		sendDataToCamera(paramBuffer);

	}
	public static void sendROIX(int xStart, int xEnd)
			  {
		System.out.println("send xROI, xStart:"+xStart+" xEnd"+xEnd);
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_ROI_X);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(xStart);
		paramBuffer[0] = toByte(xEnd);
		sendDataToCamera(paramBuffer);
	}
	public static void sendROIYStart(int yStart)
			  {
		System.out.println("send yStart: "+yStart);
	

		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_ROI_Y_START);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(yStart/256);
		paramBuffer[0] = toByte(yStart%256);
		sendDataToCamera(paramBuffer);

	}
	public static void sendROIYEnd(int yEnd)
			  {
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_ROI_Y_END);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(yEnd/256);
		paramBuffer[0] = toByte(yEnd%256);
		
		sendDataToCamera(paramBuffer);
		
	}
	
	public static int getFrameWidth(){
	
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_CURRENT_VIEW_WIDTH);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256;

	}
	
	public static int getFrameHeight(){
		byte[] recivedData;
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_CURRENT_VIEW_HEIGHT);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256;

	}
	
	public static void sendBackgroundColorParameters(int _R, int _G, int _B)   {
		System.out.println("send bg threshold: "+_R);
	
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_BACKGORUND_THRESHOLD);
//		paramBuffer[2] = toByte(_R);
//		paramBuffer[1] = toByte(_G);
//		paramBuffer[0] = toByte(_B);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(_R/256);
		paramBuffer[0] = toByte(_R%256);

		sendDataToCamera(paramBuffer);
	}

	public static void controlMonitor(int tabId)   {
		
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[1] = toByte(0);
		paramBuffer[2] = toByte(0);
		paramBuffer[0] = toByte(0);

		if (tabId == 1) {
			paramBuffer[3] = toByte(HEADER_MONITOR_ON);
		}
		if (tabId == 0) {
			paramBuffer[3] = toByte(HEADER_MONITOR_OFF);
		}

		sendDataToCamera(paramBuffer);
	}

	public static void sendSensitivityParams(int sensitivityValue)   {
		System.out.println("send certantity: "+sensitivityValue);
	
		byte[] paramBuffer = new byte[4];
		paramBuffer[1] = toByte(0);
		paramBuffer[2] = toByte(0);
		paramBuffer[0] = toByte(sensitivityValue);
		paramBuffer[3] = toByte(HEADER_SENSITIVITY_VALUE);
		

		sendDataToCamera(paramBuffer);

	}
	
	public static void sendEjectDelayGranularity(int value)   {
		System.out.println("send granulaty: "+value);
		
		byte[] paramBuffer = new byte[4];
		
		paramBuffer[0] = toByte(value%256);
		paramBuffer[1] = toByte(value%(256*256)/256);
		paramBuffer[2] = toByte(value%(256*256*256)/(256*256));
		
		paramBuffer[3] = toByte(HEADER_DELAY_GRANULARITY);
		sendDataToCamera(paramBuffer);

	}
	public static void sendEjectOnSteps(int value)   {
		System.out.println("send ejector on steps: "+value);
		
		byte[] paramBuffer = new byte[4];
		
		paramBuffer[0] = toByte(value%256);
		paramBuffer[1] = toByte(value%(256*256)/256);
		paramBuffer[2] = toByte(value%(256*256*256)/(256*256));
		
		paramBuffer[3] = toByte(HEADER_EJECT_ON_STEPS);
		sendDataToCamera(paramBuffer);

	}
	public static void sendDelaySteps(int value)   {
		System.out.println("send delay steps: "+value);
		
		byte[] paramBuffer = new byte[4];
		
		paramBuffer[0] = toByte(value%256);
		paramBuffer[1] = toByte((value%(256*256))/256);
		paramBuffer[2] = toByte((value%(256*256*256))/(256*256));
		
		paramBuffer[3] = toByte(HEADER_DELAY_STEPS);
		sendDataToCamera(paramBuffer);

	}
	public static void sendMargin(int marginId,int value)   {
		System.out.println("send margin id:"+marginId+" value:"+value);
    	
		
		byte[] paramBuffer = new byte[4];
		
		paramBuffer[0] = toByte(value%256);
		paramBuffer[1] = toByte(value/256);
		paramBuffer[2] = toByte(marginId);
		
		paramBuffer[3] = toByte(HEADER_MARGIN);
		
		sendDataToCamera(paramBuffer);
		

	}
	static boolean isRetringMessageShowing = false;
	static boolean retryingCancelled = false;
	
	private static  void sendDataToCamera(byte[] paramBuffer){
		Thread t = new Thread(new Runnable(){
			public void run(){
				sendDataToCameraThread(paramBuffer);
			}
    	});
		t.start();
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void sendDataToCameraThread(byte[] paramBuffer){
		if(isSendDataEnabled){
		Thread t = null;
		
		 JOptionPane opt = new JOptionPane("Connection establishment failed. \nRetrying...", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}); // no buttons
	     final JDialog dlg = opt.createDialog("Connection Failed !");
		do{
			System.out.println("sending 4 bytes:"+paramBuffer[3]+paramBuffer[2]+paramBuffer[1]+paramBuffer[0]);
			if(buildServerConnection()){
				try {
					outToServer.write(paramBuffer);
					clientSocket.close();
					
					if(isRetringMessageShowing){
						isRetringMessageShowing = false;
						dlg.dispose();
						JOptionPane.showMessageDialog(null , "Successfully Connected to the Camera...","Connected!",1); 
			        	
					}
					
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null , "Connection error!");
				
					e.printStackTrace();
				
				} 
			}
			else{
				if(!isRetringMessageShowing){
				     
					isRetringMessageShowing = true;
					t = new Thread(new Runnable(){
						public void run(){
							 dlg.setVisible(true);
							 dlg.addWindowListener(new WindowAdapter() {
								    @Override
								    public void windowClosed(WindowEvent e) {
								        isRetringMessageShowing = false;
								        retryingCancelled = true;
								        System.out.println("retrying window closed");
								    }
								});
			        		//JOptionPane.showMessageDialog(null , "Connection establishment failed. \nRetrying...","Connection Failed !",0); 
			        	}
			    	});
				t.start();
				
			  
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			
				
			}
		}while(isRetringMessageShowing&&!retryingCancelled);
		}
	}
	
	private static byte[] getDataFromCamera(byte[] paramBuffer,int numberOfBytes){
		byte[] recivedData= new byte[numberOfBytes];
		System.out.println("sending 4 bytes:"+paramBuffer[3]+paramBuffer[2]+paramBuffer[1]+paramBuffer[0]);
		if(buildServerConnection()){
			try {
				outToServer.write(paramBuffer);
				int recivedCount = dis.read(recivedData,0,numberOfBytes);
				clientSocket.close();
				
				System.out.print("recived "+recivedCount+" bytes. start ");
				for(int i=0;i<recivedCount;i++){
					System.out.print(Byte.toUnsignedInt(recivedData[i])+" ");
				}
				System.out.println(" end");
				return recivedData;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null , "Connection error!");
			
				e.printStackTrace();
			}
			
		}
		else{
			//JOptionPane.showMessageDialog(null , "Not Connected!");
		}
		
		return null;
	}
	
	public static byte toByte(int number) {
		int tmp = number & 0xff;
		return (byte) ((tmp & 0x80) == 0 ? tmp : tmp - 256);
	}
	
	
	public static void getFrames(String folderName,int timeout, int numberOfFrames) throws IOException   {

		// FileHandler.saveAsGIF(1280, 1024, "out.bin");
//		FileHandler.saveAllAsGif(1280, NUMBER_OF_LINES_PER_FRAME, "testInLeaf");
		
		byte[] _32bitframe = new byte[4];
		for (byte b1 : _32bitframe) {
			b1 = 0;

		}

		

		int frameByteCount = 0;
 		int bytesRecived = 0;
		int bytesRead = 0;
		long startTime = System.nanoTime();
		
		SettingsManager settingsManager =  new SettingsManager();
		settingsManager.retriveSettingsFromCamera();
		
		int width = settingsManager.getFrameWidth();
		int height =  settingsManager.getFrameHeight();
		int bytesPerFrame = width*height*Constants.PIXEL_SIZE_IN_BYTES;
		
		int bytesPerPacket;
		if(bytesPerFrame>Constants.MAX_BYTES_IN_PACKET){
			bytesPerPacket = Constants.MAX_BYTES_IN_PACKET;
		}
		else{
			bytesPerPacket = bytesPerFrame;
		}
		
		System.out.println("requesting frames. width:"+width+" height:"+height+" bytesPerFrame "+bytesPerFrame+" bytesPerPacket:"+bytesPerPacket);
		byte[] byteBuf = new byte[bytesPerFrame];
		
		FrameBuffer.clearBuffer();
		// ... the code being measured ...

		Controller.start(numberOfFrames, folderName,width,height);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		tcpReceive = true;
		
		buildServerConnection();
		// get 1000 frames
		for (int i = 0; (i < numberOfFrames||numberOfFrames==-1)&&tcpReceive; i++) {
			// long start = System.nanoTime();
			frameByteCount = 0;
			bytesRecived = 0;
			bytesRead = 0;

			int bytefPerNextPacket;
			// get single frame
			while (frameByteCount <bytesPerFrame) {
				try{
				outToServer.write(_32bitframe);
				}
				catch(java.net.SocketException ex){
					//if connection is closed try again
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getFrames(folderName,timeout,numberOfFrames);
					return;
				}
				// frameNumber = dis.readInt();

				bytesRecived = 0;
				// get single packet(24 ,lines)
				if(bytesPerFrame-frameByteCount<PACKET_SIZE_IN_BYTES){
					bytefPerNextPacket = bytesPerFrame-frameByteCount;
				}
				else{
					bytefPerNextPacket = PACKET_SIZE_IN_BYTES;
				}
				while (bytesRecived < bytefPerNextPacket) {

					try{
						// System.out.println("reading : "+(bytesPerPacket-bytesRecived));
						int waitingTime = 0;
//						try{
//						while(dis.available()<bytefPerNextPacket){
//							try {
//								Thread.sleep(1);
//								waitingTime++;
//								if(waitingTime>timeout){  
//									train(folderName,timeout);
//									return;
//								}
//								
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//							
//						}
//						}catch (java.io.IOException e) {
//							train(folderName,timeout);
//							return;
//							// TODO: handle exception
//						}
						bytesRead = dis.read(byteBuf, frameByteCount,bytefPerNextPacket-bytesRecived);
					}
					catch(java.lang.ArrayIndexOutOfBoundsException e){
						System.out.println("read error: "+bytesRead );
					}

					bytesRecived += bytesRead;
					frameByteCount += bytesRead;
					//System.out.println("bytes read: "+bytesRead);
					try {
						Thread.sleep(35);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
//			System.out.println("pc: "+Byte.toUnsignedInt(byteBuf[bytesPerFrame-4])+" "+Byte.t oUnsignedInt(byteBuf[bytesPerFrame-3])+" "+Byte.toUnsignedInt(byteBuf[bytesPerFrame-2])+" "+Byte.toUnsignedInt(byteBuf[bytesPerFrame-1]));
			FrameBuffer.addToBuffer(byteBuf);

		}

		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("frame received,fps:" + numberOfFrames / (estimatedTime / 1000000000.0) + " time: "
				+ estimatedTime / 1000000 + "ms");

		//FileHandler.saveAllAsGif(1280, NUMBER_OF_LINES_PER_FRAME, folderName);
		clientSocket.close();

		System.out.println("Converting Succefull");
		status = true;
	}

}
