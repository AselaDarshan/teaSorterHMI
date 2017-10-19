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

public class ImageHandler {

	public BufferedImage rgb32Torgb24BufferdImage(byte[] buf,int width,int height){
		byte[] rgb8Buf = new byte[buf.length/4*3];
		int pixelCount = 0;
		long pixel;
		for(int i=0;i<buf.length;i+=4){
			pixel = getUInt32(Arrays.copyOfRange(buf, i, i+4));
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,30))/Math.pow(2,20)/4); 
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,10))/4); 
			rgb8Buf[pixelCount++] = (byte) (pixel%(Math.pow(2,20))/Math.pow(2,10)/4); 

		}

		return createRGBImage(rgb8Buf,width ,height);
	}
	private BufferedImage createRGBImage(byte[] bytes, int width, int height) throws java.awt.image.RasterFormatException {

		DataBufferByte buffer = new DataBufferByte(bytes, bytes.length);
	    ColorModel cm = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8, 8, 8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
	    return new BufferedImage(cm, Raster.createInterleavedRaster(buffer, width, height, width * 3, 3, new int[]{0, 1, 2}, null), false, null);
	}
	public long getUInt32(byte[] bytes) {
	    long value = byteAsULong(bytes[0]) | (byteAsULong(bytes[1]) << 8) | (byteAsULong(bytes[2]) << 16) | (byteAsULong(bytes[3]) << 24);
	    return value;
	}
	public long byteAsULong(byte b) {
	    return ((long)b) & 0x00000000000000FFL; 
	}
}
