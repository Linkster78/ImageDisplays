package com.tek.idisplays;

import java.io.File;

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
		return getImageFile().exists();
	}
	
}
