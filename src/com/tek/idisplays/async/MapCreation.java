package com.tek.idisplays.async;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

public class MapCreation {
	
	private Block block;
	private BlockFace blockFace;
	private ItemStack mapItem;
	
	public MapCreation(Block block, BlockFace blockFace, ItemStack mapItem) {
		this.block = block;
		this.blockFace = blockFace;
		this.mapItem = mapItem;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public BlockFace getBlockFace() {
		return blockFace;
	}
	
	public ItemStack getMapItem() {
		return mapItem;
	}
	
}
