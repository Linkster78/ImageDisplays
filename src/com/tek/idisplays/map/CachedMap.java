package com.tek.idisplays.map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class CachedMap {
	
	private int mapId;
	private byte[][] pixelMap;
	
	public CachedMap(int mapId, byte[][] pixelMap) {
		this.mapId = mapId;
		this.pixelMap = pixelMap;
	}
	
	public static CachedMap fromByteBuffer(ByteBuffer buffer) {
		int mapId = buffer.getInt();
		byte[][] pixelMap = new byte[DisplayManager.MAP_SIZE][DisplayManager.MAP_SIZE];
		for(int x = 0; x < pixelMap.length; x++) {
			byte[] yAxis = new byte[DisplayManager.MAP_SIZE];
			for(int y = 0; y < yAxis.length; y++) {
				yAxis[y] = buffer.get();
			}
			pixelMap[x] = yAxis;
		}
		return new CachedMap(mapId, pixelMap);
	}
	
	public byte[] toByteArray() throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		bos.write(intToBytes(mapId));
		for(int i = 0; i < pixelMap.length; i++) {
			bos.write(pixelMap[i]);
		}
		return bos.toByteArray();
	}
	
	public int getMapId() {
		return mapId;
	}
	
	public byte[][] getPixelMap() {
		return pixelMap;
	}
	
	public byte[] intToBytes(int i) {
		return ByteBuffer.allocate(4).putInt(i).array();
	}
	
}
