package com.Sortex.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import javax.swing.JOptionPane;

public class SettingsManager {
	private int bgThrshold;
	private int stemLeafThreshold;
	private int roiXStart;
	private int roiXEnd;
	private int roiYStart;
	private int roiYEnd;
	private int marginsArray[];
	private int minStemCount;
	private int certantity;
	private int delayGranularity;
	private int ejectDelaySteps;
	private int ejectorOndelaySteps;
	private int mux;
	
	private int exposureTime;
	private int frameLength;
	
	

	private int frameWidth;
	private int frameHeight;
	
	private int tcpTimeOut;
	
	public SettingsManager(){
		
//		retriveSavedSettings();
//		retriveSettingsFromCamera();
		bgThrshold = 800;
		stemLeafThreshold = 450;
		certantity = 50;
		minStemCount = 400;
		delayGranularity = 800000;
		ejectDelaySteps = 0;
		ejectorOndelaySteps = 1;
		roiXStart = 0;
		roiXEnd = 159;//*8
		roiYStart = 526;
		roiYEnd = 625;
		mux = 1;
		
		marginsArray =  new int[25];
		marginsArray[0] = 590;
		marginsArray[1] = 690;
		
		setTcpTimeOut(1000);
	}
	public int getExposureTime() {
		return exposureTime;
	}

	public void setExposureTime(int exposureTime) {
		com.Sortex.controller.TCPClient.sendExposureTime(exposureTime);
		this.exposureTime = exposureTime;
	}

	public int getFrameLength() {
		return frameLength;
	}

	public void setFrameLength(int frameLength) {
		com.Sortex.controller.TCPClient.sendFrameLength(frameLength);
		this.frameLength = frameLength;
	}
	public int getFrameWidth() {
		if(frameWidth==0){
			return Constants.DEFAULT_IMAGE_WIDTH;
		}
		return frameWidth;
	}

	public void setFrameWidth(int frameWidth) {
		this.frameWidth = frameWidth;
	}

	public int getFrameHeight() {
		if(frameHeight==0){
			return Constants.DEFAULT_IMAGE_HEIGHT;
		}
		return frameHeight;
	}

	public void setFrameHeight(int frameHeight) {
		this.frameHeight = frameHeight;
	}

	
	
	public int getBgThrshold() {
		return bgThrshold;
	}
	public void setBgThrshold(int bgThrshold) {
		com.Sortex.controller.TCPClient.sendBackgroundColorParameters(bgThrshold,0,0);
		
		this.bgThrshold = bgThrshold;
	}
	public int getStemLeafThreshold() {
		return stemLeafThreshold;
	}
	public void setStemLeafThreshold(int stemLeafThreshold) {
		com.Sortex.controller.TCPClient.sendStemLeafTresholds(stemLeafThreshold,0,0);
		this.stemLeafThreshold = stemLeafThreshold;
	}
	public int getRoiXStart() {
		return roiXStart;
	}
	public void setRoiXStart(int roiXStart) {
		if(roiXEnd>roiXStart){
			com.Sortex.controller.TCPClient.sendROIX(roiXStart,roiXEnd);
		
			this.roiXStart = roiXStart;
		}
		else{
			JOptionPane.showMessageDialog(null , "X start should be less than X end","Invalied Setting !",0); 
        	
		}
	}
	public int getRoiXEnd() {
		return roiXEnd;
	}
	public void setRoiXEnd(int roiXEnd) {
		if(roiXEnd>roiXStart){
			com.Sortex.controller.TCPClient.sendROIX(roiXStart,roiXEnd);

			this.roiXEnd = roiXEnd;
		}
		else{
			JOptionPane.showMessageDialog(null , "X end should be larger than X start","Invalied Setting !",0); 
        	
		}
	}
	public int getRoiYStart() {
		return roiYStart;
	}
	public void setRoiYStart(int roiYStart) {
		if(roiYStart%2!=0){
			JOptionPane.showMessageDialog(null , "Y start should an even number","Invalied Setting !",0); 
        	return;
		}
		if(roiYEnd>roiYStart){
			if(roiYEnd - roiYStart<Constants.MAX_HEIGHT) {
				com.Sortex.controller.TCPClient.sendROIYStart(roiYStart);
			
				this.roiYStart = roiYStart;
			}
			else {
				JOptionPane.showMessageDialog(null ,"Max height is "+Constants.MAX_HEIGHT,"Height excessed maximum height",0); 
			}
		}
		else{
			JOptionPane.showMessageDialog(null , "Y start should be less than Y end","Invalied Setting !",0); 
        	
		}
	}
	public int getRoiYEnd() {
		return roiYEnd;
	}
	public void setRoiYEnd(int roiYEnd) {
		
		
		
		if(roiYEnd>roiYStart){
			if(roiYEnd - roiYStart<Constants.MAX_HEIGHT) {
				com.Sortex.controller.TCPClient.sendROIYEnd(roiYEnd);
				this.roiYEnd = roiYEnd;
			}
			else {
				JOptionPane.showMessageDialog(null ,"Max height is "+Constants.MAX_HEIGHT,"Height excessed maximum height",0); 
			}
		}
		else{
			JOptionPane.showMessageDialog(null , "Y end should be larger than Y start","Invalied Setting !",0); 
        	
		}
	}
	public int[] getMarginsArray() {
		return marginsArray;
	}
	public void changeMargins(int marginId,int value){
		this.marginsArray[marginId] = value;
	}
	public void setMarginsArray(int[] marginsArray) {
		this.marginsArray = marginsArray;
	}
	public int getMinStemCount() {
		return minStemCount;
	}
	public void setMinStemCount(int minStemCount) {
		com.Sortex.controller.TCPClient.sendMinStemCount(minStemCount);
		
		this.minStemCount = minStemCount;
	}
	public int getCertantity() {
		return certantity;
	}
	public void setCertantity(int certantity) {
		com.Sortex.controller.TCPClient.sendSensitivityParams(certantity);
		this.certantity = certantity;
	}
	public int getDelayGranularity() {
		return delayGranularity;
	}
	public void setDelayGranularity(int delayGranularity) {
		com.Sortex.controller.TCPClient.sendEjectDelayGranularity(delayGranularity);
		this.delayGranularity = delayGranularity;
	}
	public int getEjectDelaySteps() {
		return ejectDelaySteps;
	}
	public void setEjectDelaySteps(int ejectDelaySteps) {
		com.Sortex.controller.TCPClient.sendDelaySteps(ejectDelaySteps);
		this.ejectDelaySteps = ejectDelaySteps;
	}
	public int getEjectorOndelaySteps() {
		return ejectorOndelaySteps;
	}
	public void setEjectorOndelaySteps(int ejectorOndelaySteps) {
		com.Sortex.controller.TCPClient.sendEjectOnSteps(ejectorOndelaySteps);
		this.ejectorOndelaySteps = ejectorOndelaySteps;
	}
	public int getMux() {
		return mux;
	}
	public void setMux(int mux) {
		com.Sortex.controller.TCPClient.sendMux(mux);
		this.mux = mux;
	}
	
	
	public void saveSettings(){
		 // Save Settings
	    Properties saveProps = new Properties();
	    saveProps.setProperty(Constants.BG_THRESHOLD_KEY, String.valueOf(bgThrshold));
	    saveProps.setProperty(Constants.SL_THRESHOLD_KEY,  String.valueOf(stemLeafThreshold));
	    saveProps.setProperty(Constants.CERTAINTITY_KEY,  String.valueOf(certantity));
	    saveProps.setProperty(Constants.MIN_STEM_COUNT_KEY,  String.valueOf(minStemCount));
	    saveProps.setProperty(Constants.DELAY_GRANULATY_KEY,  String.valueOf(delayGranularity));
	    saveProps.setProperty(Constants.DELAY_STEPS_KEY,  String.valueOf(ejectDelaySteps));
	    saveProps.setProperty(Constants.EJECTOR_ON_STEPS_KEY,  String.valueOf(ejectorOndelaySteps));
	    saveProps.setProperty(Constants.MUX_KEY,  String.valueOf(mux));
	    saveProps.setProperty(Constants.Y_START_KEY,  String.valueOf(roiYStart));
	    saveProps.setProperty(Constants.Y_END_KEY,  String.valueOf(roiYEnd));
	    saveProps.setProperty(Constants.TCP_TIMEOUT_KEY,  String.valueOf(getTcpTimeOut()));
	    saveProps.setProperty(Constants.FRAME_LENGTH_KEY,  String.valueOf(getFrameLength()));
	    saveProps.setProperty(Constants.EXPOSURE_TIME_KEY,  String.valueOf(getExposureTime()));
	    for(int i=0;i<Constants.NUMBER_OF_MARGINS;i++){
			
			saveProps.setProperty(Constants.MARGIN_KEY+i,  String.valueOf(marginsArray[i]));
		}
	    try {
			saveProps.storeToXML(new FileOutputStream("settings.xml"), "");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void retriveSavedSettings(){
		
		 // Load Settings
	    Properties loadProps = new Properties();
	    try {
			loadProps.loadFromXML(new FileInputStream("settings.xml"));
		
				
				bgThrshold = Integer.parseInt(loadProps.getProperty(Constants.BG_THRESHOLD_KEY));
				stemLeafThreshold = Integer.parseInt(loadProps.getProperty(Constants.SL_THRESHOLD_KEY));
				certantity = Integer.parseInt(loadProps.getProperty(Constants.CERTAINTITY_KEY));
				minStemCount = Integer.parseInt(loadProps.getProperty(Constants.MIN_STEM_COUNT_KEY));
				delayGranularity = Integer.parseInt(loadProps.getProperty(Constants.DELAY_GRANULATY_KEY));
				ejectDelaySteps = Integer.parseInt(loadProps.getProperty(Constants.DELAY_STEPS_KEY));
				ejectorOndelaySteps = Integer.parseInt(loadProps.getProperty(Constants.EJECTOR_ON_STEPS_KEY));
				mux = Integer.parseInt(loadProps.getProperty(Constants.MUX_KEY));
				roiYStart = Integer.parseInt(loadProps.getProperty(Constants.Y_START_KEY));
				roiYEnd = Integer.parseInt(loadProps.getProperty(Constants.Y_END_KEY));
				frameLength = Integer.parseInt(loadProps.getProperty(Constants.FRAME_LENGTH_KEY));
				exposureTime = Integer.parseInt(loadProps.getProperty(Constants.EXPOSURE_TIME_KEY));
				
				  for(int i=0;i<Constants.NUMBER_OF_MARGINS;i++){
					  marginsArray[i] = Integer.parseInt(loadProps.getProperty(Constants.MARGIN_KEY+i));
				  }
				  
				  setTcpTimeOut(Integer.parseInt(loadProps.getProperty(Constants.TCP_TIMEOUT_KEY)));
			
			
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    catch (NumberFormatException e) {
		// TODO Auto-generated catch block
	    	e.printStackTrace();
	    }
	   
	}
	
	public void applySettings(){
		com.Sortex.controller.TCPClient.sendBackgroundColorParameters(bgThrshold,0,0);
		com.Sortex.controller.TCPClient.sendStemLeafTresholds(stemLeafThreshold,0,0);
//		com.Sortex.controller.TCPClient.sendROIX(roiXStart,roiXEnd);
//		com.Sortex.controller.TCPClient.sendROIX(roiXStart,roiXEnd);
		com.Sortex.controller.TCPClient.sendROIYStart(roiYStart);
		com.Sortex.controller.TCPClient.sendROIYEnd(roiYEnd);
		com.Sortex.controller.TCPClient.sendMinStemCount(minStemCount);
		com.Sortex.controller.TCPClient.sendSensitivityParams(certantity);
		com.Sortex.controller.TCPClient.sendEjectDelayGranularity(delayGranularity);
		com.Sortex.controller.TCPClient.sendDelaySteps(ejectDelaySteps);
		com.Sortex.controller.TCPClient.sendEjectOnSteps(ejectorOndelaySteps);
		com.Sortex.controller.TCPClient.sendMux(mux);
		com.Sortex.controller.TCPClient.sendFrameLength(frameLength);
		com.Sortex.controller.TCPClient.sendExposureTime(exposureTime);
		for(int i=Constants.NUMBER_OF_MARGINS-1;i>=0;i--){
				com.Sortex.controller.TCPClient.sendMargin(i,marginsArray[i]);
				
		}
	}
	
	public void retriveSettingsFromCamera(){
		this.frameHeight = TCPClient.getFrameHeight();
		this.frameWidth = TCPClient.getFrameWidth();
	}

	public int getTcpTimeOut() {
		return tcpTimeOut;
	}

	public void setTcpTimeOut(int tcpTimeOut) {
		this.tcpTimeOut = tcpTimeOut;
	}
	 
	
}
