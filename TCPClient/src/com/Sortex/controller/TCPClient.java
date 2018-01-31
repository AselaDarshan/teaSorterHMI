package com.Sortex.controller;



import java.io.*;
import java.net.*;



public class TCPClient {
	final static int NUMBER_OF_BYTES_PER_PACKET = 40000;//61440
	final static int PACKET_SIZE_IN_BYTES = 1000;//61440
	final static int NUMBER_OF_BYTES_PER_LINE = 100*4;// 6 bytes
	final static int NUMBER_OF_LINES_PER_FRAME = 100;
	final static int NUMBER_OF_BYTES_PER_FRAME = NUMBER_OF_BYTES_PER_LINE * NUMBER_OF_LINES_PER_FRAME;

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
	final static int HEADER_CAPTURE_AND_FREEZE = 0x37; 
	final static int HEADER_RESUME_CONTINUOUS_CAPTURE = 0x38; 
	final static int HEADER_IS_DONE = 0x39;
	
	
	
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
	final static int HEADER_SWITCH_EJECTOR = 0x3D;

	final static int HEADER_PS_EJECTOR_MODE = 0x3C;
	final static int HEADER_GET_DECISION = 65;


	static Socket clientSocket;
	static DataOutputStream outToServer;
	static InputStream in;
	static DataInputStream dis;
	static int failedMessagedCount = 0;

	public static boolean status = true;
	
	static boolean isRetringMessageShowing = false;
	static boolean retryingCancelled = false;
	public static boolean tcpReceive;
	public static boolean isSendDataEnabled = false;
	private static boolean connectionDisabled = false;
	
	static Viewer viewer = null;
	
	public static boolean buildServerConnection()  {
			
			try {
				clientSocket = new Socket("10.0.1.1", 7000);
	//			clientSocket = new Socket("192.168.1.12", 7000);
			
				outToServer = new DataOutputStream(clientSocket.getOutputStream());
			
				in = clientSocket.getInputStream();
			} catch (IOException e) {
				
				
				System.out.println("Connection error. "+e.getMessage());
			
				return false;
			}
						dis = new DataInputStream(in);
			System.out.println("Connected to server");
			return true;
		
		
	}
	
	public static void sendEjectorMode(int mode){
		System.out.println("send switch ejector command");
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_PS_EJECTOR_MODE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(mode);
		sendDataToCamera(paramBuffer);

	}
	
	public static void sendSwitchEjectorCommand(int ejctor,int state){
		System.out.println("send switch ejector command");
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_SWITCH_EJECTOR);
		paramBuffer[2] = toByte(ejctor);
		paramBuffer[1] = toByte(state);
		paramBuffer[0] = toByte(0);
		sendDataToCamera(paramBuffer);

	}
	
	public static void sendCapAndFreeze(){
		System.out.println("send cap & freeze");
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_CAPTURE_AND_FREEZE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		sendDataToCamera(paramBuffer);

	}
	public static void sendResumeCap(){
		System.out.println("send resume cap");
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_RESUME_CONTINUOUS_CAPTURE);
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		sendDataToCamera(paramBuffer);

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
	public static int getIsDone(){
		
		byte[] recivedData;
		
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_IS_DONE);
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
	
	public static  int sendRawCommand(int command){
		byte[] recivedData;
		byte[] paramBuffer = new byte[4];
		
		paramBuffer[0] = toByte(command%256);
		paramBuffer[1] = toByte(command%(256*256)/256);
		paramBuffer[2] = toByte(command%(256*256*256)/(256*256));
		paramBuffer[3] = toByte(command/(256*256*256));
		
		recivedData = getDataFromCamera(paramBuffer,4);
		
		return  Byte.toUnsignedInt(recivedData[0])+ Byte.toUnsignedInt(recivedData[1])*256+ Byte.toUnsignedInt(recivedData[2])*256*256+Byte.toUnsignedInt(recivedData[3])*256*256*256;

	}
	
	public static byte[] getDecision(){
		byte[] recivedData;
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_GET_DECISION );
		paramBuffer[2] = toByte(0);
		paramBuffer[1] = toByte(0);
		paramBuffer[0] = toByte(0);
		
		recivedData = getDataFromCamera(paramBuffer,8);
		
		return  recivedData;

	}
	public static void sendBackgroundColorParameters(int _R, int _G, int _B)   {
		System.out.println("send bg threshold: "+_R);
	
		byte[] paramBuffer = new byte[4];
		paramBuffer[3] = toByte(HEADER_BACKGORUND_THRESHOLD);

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

		
	private static void sendDataToCamera(byte[] paramBuffer){
		int retryCount = 0;
		int waitingTime =0;
		if(!isSendDataEnabled){
			System.out.println("TCP disabled!");
			return;
		}
		do{
			System.out.print("sending "+paramBuffer.length+" bytes:");
			if(connectionDisabled ) {
				System.out.println("connection disabled");
				return;
			}
			for(int i=0;i<paramBuffer.length;i++) {
				System.out.print(" "+Byte.toUnsignedInt(paramBuffer[i]));
			}
			System.out.println(" end");
			if(buildServerConnection()){
				try {
					outToServer.write(paramBuffer);
					byte[] response= new byte[4];
					while(dis.available()<4) {
						Thread.sleep(10);
						waitingTime++;
						if(waitingTime>Constants.RESPONSE_TIMEOUT_MILIS/10) {
							System.out.println("Timeout while waiting for response!");
							//if timeout occurred stop waiting for response
							clientSocket.close();
							continue;
						}
					}
					int recivedCount = dis.read(response,0,4);
					if(recivedCount>0) {
						//response received
						int resCode = Byte.toUnsignedInt(response[0]) + Byte.toUnsignedInt(response[1])*256+ Byte.toUnsignedInt(response[2])*256*256+ Byte.toUnsignedInt(response[2])*256*256*256;
						if(resCode == 0) {
							//success response
							System.out.println("Setting success!");
							
							return;
						}
						else {
							//failed response
							System.out.println("Setting failed!");
						}
					}
					else {
						System.out.println("No response received!");
					}
					clientSocket.close();
					
						
				} catch (IOException e) {
					System.out.println("Connection error!");
				} catch (InterruptedException e) {
					System.out.println("Connection error!");
				} 
			}
			else {
				System.out.println("Connection failed!");
			}
			retryCount++;
			System.out.println("Retrying..");
		}while(retryCount<Constants.RETRY_COUNT);
		
		System.out.println("Sending faild!. stoped retrying after "+Constants.RETRY_COUNT+ " attempts");
		failedMessagedCount ++;
		
		
		//JOptionPane.showMessageDialog(null , "Successfully Connected to the Camera...","Connected!",1); 
	}
	
	private static byte[] getDataFromCamera(byte[] paramBuffer,int numberOfBytes){
		
		byte[] recivedData= new byte[numberOfBytes];
		System.out.println("sending 4 bytes:"+paramBuffer[3]+paramBuffer[2]+paramBuffer[1]+paramBuffer[0]);
		if(connectionDisabled) {
			System.out.println("connection disabled");
			return recivedData;
		}
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
	

	
	public static int getFramesRealTime(int frameCount,SettingsManager settingsManager) {
		
		if(!isSendDataEnabled){
			System.out.println("communication is disabled");
			return 1;
		}

		byte[] _32bitframe = new byte[4];

		int numberOfFrames= 0;
		int frameByteCount = 0;
 		int bytesRecived = 0;
		int bytesRead = 0;
		long startTime = System.nanoTime();
		 
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
		
		
		if(frameCount>0) {
			
		}
		else {
			viewer = new Viewer();
			viewer.initialize();

		}
		Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
		tcpReceive = true;
		
		buildServerConnection();
		
		int prevDecision=0,decision=0;
		try {
			PrintWriter decisionWriter = new PrintWriter("decision_log.txt", "UTF-8");
		
		try {
			clientSocket.setSoTimeout(Constants.READ_TIMEOUT_MILIS);
		} catch (SocketException e1) {
			System.out.println("SocketException while setting timeout");
			returnValue =  -1;
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		int retryCount = 1;
		// get frames
		try{
		while(tcpReceive) {
			
			frameByteCount = 0;
			bytesRecived = 0;
			bytesRead = 0;
			//System.out.println("receiving frame "+numberOfFrames+" attempt "+retryCount);
			int bytefPerNextPacket;
			// get single frame
			while (frameByteCount <bytesPerFrame && tcpReceive) {
				
				outToServer.write(_32bitframe);
				
				bytesRecived = 0;
				// get single packet
				if(bytesPerFrame-frameByteCount<PACKET_SIZE_IN_BYTES){
					bytefPerNextPacket = bytesPerFrame-frameByteCount;
				}
				else{
					bytefPerNextPacket = PACKET_SIZE_IN_BYTES;
				}
				while (bytesRecived < bytefPerNextPacket) {
						
					try {
						bytesRead = dis.read(byteBuf, frameByteCount,bytefPerNextPacket-bytesRecived);
					//	System.out.println("received "+bytesRead+" bytes");
						retryCount = 1;
					} catch (IOException e1) {
						retryCount++;
						if(retryCount>Constants.MAX_RETRY_COUNT) {
							tcpReceive = false;
							System.out.println("stoped retring after "+Constants.MAX_RETRY_COUNT+" attempts");
						}
						else {
						System.out.println("read timeout after "+Constants.READ_TIMEOUT_MILIS);
						}
						returnValue =  -1;
						e1.printStackTrace();
						break;
						
					}
					
					bytesRecived += bytesRead;
					frameByteCount += bytesRead;
				
//					try {
//						Thread.sleep(Constants.PACKET_DELAY_IN_MILIS);
//					} 
//					catch (InterruptedException e) {	
//						System.out.println("InterruptedException");
//					}
				}
			}
			numberOfFrames++; 
			
			decision = Byte.toUnsignedInt(byteBuf[bytesPerFrame-4]) + Byte.toUnsignedInt(byteBuf[bytesPerFrame-3])*256+ Byte.toUnsignedInt(byteBuf[bytesPerFrame-2])*256*256;
			if(decision!=prevDecision) {
				prevDecision = decision;
				
				decisionWriter.println(Integer.toBinaryString(decision));
			
			}
			
			if(frameCount>0) {
				//write frame
				FileHandler.writeFile(byteBuf, new String(""+numberOfFrames),Constants.SNAPSHOT_SAVE_FOLDER);
				
			}
			else {
				//view frame
				viewer.convertToRGB8andView(byteBuf, width, height);
			}
			
			
			if(numberOfFrames == frameCount) {
				break;
			}
		}
		}
		catch(java.net.SocketException ex){
			System.out.println("SocketException at tcp write");
			returnValue =  -1;
		}
		catch (IOException e) {
			System.out.println("IOException at tcp write");
			returnValue =  -1;
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
		if(frameCount>0) {
			PrintWriter writer;
			try {
				writer = new PrintWriter(Constants.SNAPSHOT_SAVE_FOLDER+"/settings.dat", "UTF-8");
				writer.print(settingsManager.getFrameHeight()+","+settingsManager.getFrameWidth()+","+frameCount);
				
				int[] marginArray = settingsManager.getMarginsArray();
				for(int i=0;i<Constants.NUMBER_OF_MARGINS;i++) {
					writer.print(","+marginArray[i]);
				}
				writer.print(","+settingsManager.getBgThrshold()+","+settingsManager.getStemLeafThreshold()+","+settingsManager.getMinStemCount()+","+settingsManager.getCertantity());

				writer.close();
				System.out.println("detail file write successful! ");
			} catch (FileNotFoundException e) {
				System.out.println("detail file write failed! "+e.getMessage());
			} catch (UnsupportedEncodingException e) {
				System.out.println("detail file write failed! "+e.getMessage());
			}
			
		}
		else {
		 viewer.close();
		 System.out.println("Closing view");
		}
		decisionWriter.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e1) {
			
			System.out.println("desicion log file open failed");
			
		} 
		System.out.println("Realtime frame receiving ended");
	
		return returnValue;
	}
	
	public static void closeView() {
		if(viewer != null) {
			viewer.close();
		}
		tcpReceive = false;
	}
}
