package com.Sortex.controller;



import java.io.*;
import java.net.*;



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
	
	final static int HEADER_EXPOSURE_TIME = 0x24;
	final static int HEADER_FRAME_LENGTH = 0x23;
	
	final static int HEADER_FRAME_PERIOD_CAMERA = 0x28;
	final static int HEADER_FRAME_PERIOD_HARDWARE = 0x29;
	final static int HEADER_FRAME_PERIOD_SOFTWARE = 0x2A;
	final static int HEADER_EXPOSURE_TIME_STATUS = 0x2D;
	final static int HEADER_REST_LENGTH_STATUS = 0x2E;
	
	final static int HEADER_GET_REG_VALUE = 50;
	final static int HEADER_SET_REG_VALUE = 51;

	//private static Timer timer = new Timer();
	static Socket clientSocket;
	static DataOutputStream outToServer;
	static InputStream in;
	static DataInputStream dis;
	public static boolean status = true;
	
	static boolean isRetringMessageShowing = false;
	static boolean retryingCancelled = false;
	public static boolean tcpReceive;
	public static boolean isSendDataEnabled = false;
	
	public static boolean buildServerConnection()  {
		
				//return false;
			try {
				clientSocket = new Socket("10.0.1.1", 7000);
	//			clientSocket = new Socket("192.168.1.12", 7000);
			
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
				in = clientSocket.getInputStream();
			} catch (IOException e) {
				
				//JOptionPane.showMessageDialog(null , "Not Connected!");
				System.out.println("Connection error. "+e.getMessage());
				//e.printStackTrace();
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
	public static void sendFrameLength(int frameLength) {
		System.out.println("send frame length: "+frameLength);
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_FRAME_LENGTH);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(frameLength/256);
		paramBuffer[0] = toByte(frameLength%256);
		sendDataToCamera(paramBuffer);

	}
	public static void sendExposureTime(int exposureTime) {
		System.out.println("send exposure time: "+exposureTime);
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_EXPOSURE_TIME);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(exposureTime/256);
		paramBuffer[0] = toByte(exposureTime%256);
		sendDataToCamera(paramBuffer);

	}
	public static void sendRegValue(int value,int reg) {
		System.out.println("set reg. addr:"+reg+ "value: "+value);
		
		byte[] paramBuffer = new byte[6];
		paramBuffer[3] = toByte(HEADER_SET_REG_VALUE);
		
		paramBuffer[0] = toByte(reg%256);
		paramBuffer[1] = toByte(reg/256);
		paramBuffer[2] = 0;
		
		paramBuffer[4] = toByte(value%256);
		paramBuffer[5] =toByte(value/256);
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
	public static int getFramePeriodHardware(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_FRAME_PERIOD_HARDWARE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256+ Byte.toUnsignedInt(recivedData[3])*256*256*256;

	}
public static int getRegValue(int addr){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_GET_REG_VALUE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(addr/256);
		paramBuffer[0] = toByte(addr%256);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256;

	}
	public static int getFramePeriodSoftware(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_FRAME_PERIOD_SOFTWARE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256 + Byte.toUnsignedInt(recivedData[3])*256*256*256;

	}
	public static int getExposure(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_EXPOSURE_TIME_STATUS);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256;

	}
	public static int getResetLength(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_REST_LENGTH_STATUS);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256;

	}
	public static int getFramePeriodCamera(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_FRAME_PERIOD_CAMERA);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256 + Byte.toUnsignedInt(recivedData[3])*256*256*256;

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
	//	Thread t = null;
		
		 //JOptionPane opt = new JOptionPane("Connection establishment failed. \nRetrying...", JOptionPane.ERROR_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}); // no buttons
	   // JDialog  dlg = opt.createDialog("Connection Failed !");
		do{
			System.out.print("sending "+paramBuffer.length+" bytes:");
			for(int i=0;i<paramBuffer.length;i++) {
				System.out.print(" "+Byte.toUnsignedInt(paramBuffer[i]));
			}
			System.out.println(" end");
			if(buildServerConnection()){
				try {
					outToServer.write(paramBuffer);
					clientSocket.close();
					
					if(isRetringMessageShowing){
						isRetringMessageShowing = false;
						System.out.println("Successfully Connected to the camera...");
						//dlg.dispose();
						//JOptionPane.showMessageDialog(null , "Successfully Connected to the Camera...","Connected!",1); 
			        	
					}
					
				} catch (IOException e) {
				//	JOptionPane.showMessageDialog(null , "Connection error!");
					System.out.println("Connection error!");
					
				
					e.printStackTrace();
				
				} 
			}
			else{
				if(!isRetringMessageShowing){
				     
				//	isRetringMessageShowing = true;
				//	t = new Thread(new Runnable(){
				//		public void run(){
							 //dlg.setVisible(true);
							 //dlg.addWindowListener(new WindowAdapter() {
							//	    @Override
							//	    public void windowClosed(WindowEvent e) {
							//	        isRetringMessageShowing = false;
							//	        retryingCancelled = true;
							//	        System.out.println("retrying window closed");
							//	    }
							//	});
			        		//JOptionPane.showMessageDialog(null , "Connection establishment failed. \nRetrying...","Connection Failed !",0); 
			        	//}
			    	//});
				//t.start();
				
			  
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
				System.out.println("Connection error!");
			
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
	
	

	public static void getFrames(String folderName,int timeout, int numberOfFrames,boolean filesave) throws IOException   {
		
		if(!isSendDataEnabled){
			System.out.println("communication is disabled");
			return;
		}
		WatchdogTimer.reset();
		// FileHandler.saveAsGIF(1280, 1024, "out.bin");
//		FileHandler.saveAllAsGif(1280, NUMBER_OF_LINES_PER_FRAME, "testInLeaf");
		
		byte[] _32bitframe = new byte[4];

//		for (byte b1 : _32bitframe) {
//			b1 = 0;
//
//		}

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

		Controller.start(numberOfFrames, folderName,width,height,filesave);
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		tcpReceive = true;
		
		buildServerConnection();
		// get frames
		for (int i = 0; (i < numberOfFrames||numberOfFrames==-1)&&tcpReceive; i++) {
			WatchdogTimer.reset();
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
					getFrames(folderName,timeout,numberOfFrames,filesave);
					return;
				}
				// frameNumber = dis.readInt();

				bytesRecived = 0;
				// get single packet
				if(bytesPerFrame-frameByteCount<PACKET_SIZE_IN_BYTES){
					bytefPerNextPacket = bytesPerFrame-frameByteCount;
				}
				else{
					bytefPerNextPacket = PACKET_SIZE_IN_BYTES;
				}
				while (bytesRecived < bytefPerNextPacket) {

					try{
						bytesRead = dis.read(byteBuf, frameByteCount,bytefPerNextPacket-bytesRecived);
					}
					catch(java.lang.ArrayIndexOutOfBoundsException e){
						System.out.println("read error: "+bytesRead );
					}

					bytesRecived += bytesRead;
					frameByteCount += bytesRead;
				
					try {
						Thread.sleep(40);
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

		System.out.println("Capturing ended");
		status = true;
	}
	
	public static int getFramesRealTime(int frameCount) {
		
		if(!isSendDataEnabled){
			System.out.println("communication is disabled");
			return 1;
		}

		byte[] _32bitframe = new byte[4];

//		for (byte b1 : _32bitframe) {
//			b1 = 0;
//
//		}
		int numberOfFrames= 0;
		int frameByteCount = 0;
 		int bytesRecived = 0;
		int bytesRead = 0;
		long startTime = System.nanoTime();
		SettingsManager settingsManager =  new SettingsManager();
		settingsManager.retriveSettingsFromCamera();
		
		int width = settingsManager.getFrameWidth();
		int height =  settingsManager.getFrameHeight();
		int bytesPerFrame = width*height*Constants.PIXEL_SIZE_IN_BYTES;
		
		int returnValue = 0;
		
		int bytesPerPacket;
		if(bytesPerFrame>Constants.MAX_BYTES_IN_PACKET){
			bytesPerPacket = Constants.MAX_BYTES_IN_PACKET;
		}
		else{
			bytesPerPacket = bytesPerFrame;
		}
		
		System.out.println("requesting frames. width:"+width+" height:"+height+" bytesPerFrame "+bytesPerFrame+" bytesPerPacket:"+bytesPerPacket);

		byte[] byteBuf = new byte[bytesPerFrame];
		
		
		Viewer viewer = new Viewer();
		viewer.initialize();

		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		tcpReceive = true;
		
		buildServerConnection();
		// get frames
		while(tcpReceive) {
			
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
					System.out.println("SocketException at tcp write");
					returnValue =  -1;
				}
				catch (IOException e) {
					System.out.println("IOException at tcp write");
					returnValue =  -1;
				}
				bytesRecived = 0;
				// get single packet
				if(bytesPerFrame-frameByteCount<PACKET_SIZE_IN_BYTES){
					bytefPerNextPacket = bytesPerFrame-frameByteCount;
				}
				else{
					bytefPerNextPacket = PACKET_SIZE_IN_BYTES;
				}
				while (bytesRecived < bytefPerNextPacket) {

					try{
						bytesRead = dis.read(byteBuf, frameByteCount,bytefPerNextPacket-bytesRecived);
					}
					catch(java.lang.ArrayIndexOutOfBoundsException e){
						System.out.println("read error: "+bytesRead );
					} catch (IOException x) {
						System.out.println("IOException at tco read");
					}

					bytesRecived += bytesRead;
					frameByteCount += bytesRead;
				
					try {
						Thread.sleep(40);
					} 
					catch (InterruptedException e) {	
						System.out.println("InterruptedException");
					}
				}
			}

			//view frame

			if(frameCount>0) {
				FileHandler.writeFile(byteBuf, new String(""+numberOfFrames),Constants.SNAPSHOT_SAVE_FOLDER);
			}
			else {
				viewer.convertToRGB8andView(byteBuf, width, height);
			}
			numberOfFrames++;
			
			if(numberOfFrames == frameCount) {
				break;
			}
		}

		long estimatedTime = System.nanoTime() - startTime;

		System.out.println("frame received,fps:" + numberOfFrames / (estimatedTime / 1000000000.0) + " time: "
				+ estimatedTime / 1000000 + "ms");

		
		try {
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("IOException at connection close");
			returnValue =  -2;
		}
		viewer.close();
		System.out.println("Realtime frame receiving ended");
	
		return returnValue;
	}
	
	
}
