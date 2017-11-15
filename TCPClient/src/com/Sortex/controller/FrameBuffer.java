package com.Sortex.controller;
//import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class FrameBuffer {
	//private static ArrayList<char[]> buffer= new ArrayList<>();
	
	private static LinkedList<byte[]> buffer = new LinkedList<byte[]>();
	private final static Object lock = new Object();
	
	public static void addToBuffer(byte[] frame){
		
		 synchronized (lock) {
			 if(buffer.size() > Constants.BUFFER_SIZE) {
				 buffer.removeFirst();
			 }
			 buffer.addLast(frame);
		 }
	}
	
	public static byte[] getFromBuffer(){
		synchronized (lock) {
			try{
					byte[] frame = buffer.getFirst();
					buffer.removeFirst();
					return frame;
				}catch(NoSuchElementException e){
				//	System.out.println("Frame Buffer is empty");
					return null;
			}
		}
		
	}
	
	public static void clearBuffer(){
		synchronized (lock) {
			buffer = new LinkedList<byte[]>();
		}
	}

}
