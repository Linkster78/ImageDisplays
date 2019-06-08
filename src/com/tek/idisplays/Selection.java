package com.tek.idisplays;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class Selection {
	
	private BlockFace face;
	private Block from;
	private Block to;
	private String imageName;
	
	public Selection(String imageName) {
		this.imageName = imageName;
	}
	
	public void setFace(BlockFace face) {
		this.face = face;
	}
	
	public BlockFace getFace() {
		return face;
	}
	
	public void setFrom(Block from) {
		this.from = from;
	}
	
	public Block getFrom() {
		return from;
	}
	
	public void setTo(Block to) {
		this.to = to;
	}
	
	public Block getTo() {
		return to;
	}
	
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	
	public String getImageName() {
		return imageName;
	}
	
	public File getImageFile() {
		return new File(Reference.bannerFolder.getPath() + "/" + imageName);
	}
	
	public boolean imageExists() {
		if(!isURL()) {
			return getImageFile().exists();
		} else {
			if(getCode() != 404) {
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean isURL() {
		try {
			new URL(imageName);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private int getCode() {
		HttpURLConnection huc;
		try {
			URL url = new URL(imageName);
			huc = (HttpURLConnection) url.openConnection();
			huc.setRequestMethod("GET");
			huc.connect(); 
			int code = huc.getResponseCode();
			return code;
		} catch (IOException e) {
			return 404;
		} 
	}
	
}
