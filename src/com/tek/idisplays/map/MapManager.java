package com.tek.idisplays.map;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tek.idisplays.Main;

public class MapManager {
	
	private static final int READ_SIZE = 4096;
	private File cacheFile;
	private List<CachedMap> mapCache;
	
	public MapManager() {
		cacheFile = new File("plugins/" + Main.getInstance().getName() + "/cache.dat");
		mapCache = new ArrayList<CachedMap>();
	}
	
	public List<CachedMap> readCache() {
		List<CachedMap> maps = new ArrayList<CachedMap>();
		if(cacheFile.exists()) {
			try{
				FileInputStream fis = new FileInputStream(cacheFile);
				ByteArrayOutputStream fileByteStream = new ByteArrayOutputStream();
				byte[] byteBuffer = new byte[READ_SIZE];
				int read;
				while((read = fis.read(byteBuffer)) > 0) {
					fileByteStream.write(byteBuffer, 0, read);
				}
				fis.close();
				ByteBuffer buffer = ByteBuffer.wrap(fileByteStream.toByteArray());
				while(buffer.hasRemaining()) {
					CachedMap map = CachedMap.fromByteBuffer(buffer);
					maps.add(map);
				}
			} catch(IOException e) { e.printStackTrace(); }
		}
		return maps;
	}
	
	public void saveCache() {
		if(cacheFile.exists()) cacheFile.delete();
		try {
			cacheFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(cacheFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			for(CachedMap map : mapCache) {
				bos.write(map.toByteArray());
			}
			fos.write(bos.toByteArray());
			fos.flush();
			mapCache.clear();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<CachedMap> getMapCache(int mapId) {
		return mapCache.stream().filter(map -> map.getMapId() == mapId).findFirst();
	}
	
	public File getCacheFile() {
		return cacheFile;
	}
	
	public List<CachedMap> getMapCache() {
		return mapCache;
	}
	
}
