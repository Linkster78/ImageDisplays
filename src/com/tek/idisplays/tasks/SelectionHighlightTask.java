package com.tek.idisplays.tasks;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.tek.idisplays.Main;
import com.tek.idisplays.Reference;
import com.tek.idisplays.Selection;

public class SelectionHighlightTask implements Runnable {

	@Override
	public void run() {
		Iterator<UUID> uuidIterator = Main.getInstance().getSelections().keySet().iterator();
		
		while(uuidIterator.hasNext()) {
			UUID uuid = uuidIterator.next();
			Player player = Main.getInstance().getServer().getPlayer(uuid);
			if(player != null) {
				if(Main.getInstance().getSelections().containsKey(player.getUniqueId())) {
					ItemStack itemInHand = player.getInventory().getItemInMainHand();
					if(itemInHand != null && itemInHand.hasItemMeta() && itemInHand.getItemMeta().getPersistentDataContainer().has(Reference.wandIdentifier, PersistentDataType.BYTE)) {
						Selection selection = Main.getInstance().getSelections().get(player.getUniqueId());
						if(selection.getFrom() != null) {
							Block fromBlock = selection.getFrom();
							Block blockLook = player.getTargetBlockExact(5);
							BlockFace faceLooking = getBlockFace(player);
							if(faceLooking == null || blockLook == null) continue;
							Block toBlock = blockLook.getRelative(faceLooking);
							
							if(faceLooking == BlockFace.UP || faceLooking == BlockFace.DOWN) {
								highlight(player, blockLook, faceLooking, Color.RED);
							} else {
								boolean works = true;
								if(selection.getFace() == BlockFace.WEST || selection.getFace() == BlockFace.EAST) if(Math.abs(toBlock.getX() - fromBlock.getX()) != 0) works = false;
								if(selection.getFace() == BlockFace.SOUTH || selection.getFace() == BlockFace.NORTH) if(Math.abs(toBlock.getZ() - fromBlock.getZ()) != 0) works = false;
								
								if(works) {
									int minX = Math.min(fromBlock.getX(), toBlock.getX());
									int minY = Math.min(fromBlock.getY(), toBlock.getY());
									int minZ = Math.min(fromBlock.getZ(), toBlock.getZ());
									int maxX = Math.max(fromBlock.getX(), toBlock.getX());
									int maxY = Math.max(fromBlock.getY(), toBlock.getY());
									int maxZ = Math.max(fromBlock.getZ(), toBlock.getZ());
									
									for(int x = minX; x <= maxX; x++) {
										for(int y = minY; y <= maxY; y++) {
											for(int z = minZ; z <= maxZ; z++) {
												Block blockAt = fromBlock.getWorld().getBlockAt(x, y, z);
												if(blockAt.getLocation().distance(player.getLocation()) <= 10) {
													highlight(player, blockAt.getRelative(selection.getFace().getOppositeFace()), selection.getFace(), Color.LIME);
												}
											}
										}
									}
								} else {
									highlight(player, blockLook, faceLooking, Color.RED);
								}
							}
						} else {
							Block blockLook = player.getTargetBlockExact(5);
							BlockFace faceLooking = getBlockFace(player);
							if(faceLooking == null || blockLook == null) continue;
							
							if(faceLooking == BlockFace.UP || faceLooking == BlockFace.DOWN) {
								highlight(player, blockLook, faceLooking, Color.RED);
							} else {
								highlight(player, blockLook, faceLooking, Color.LIME);
							}
						}
					}
				}
			}
		}
	}
		
		
	public static BlockFace getBlockFace(Player player) {
		List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
	    if (lastTwoTargetBlocks.size() != 2 || !lastTwoTargetBlocks.get(1).getType().isOccluding()) return null;
		Block targetBlock = lastTwoTargetBlocks.get(1);
		Block adjacentBlock = lastTwoTargetBlocks.get(0);
		return targetBlock.getFace(adjacentBlock);
	}
	
	public void highlight(Player player, Block block, BlockFace face, Color color) {
		DustOptions options = new DustOptions(color, 1);
		
		if(face == BlockFace.UP || face == BlockFace.DOWN) {
			for(int x = 0; x < 3; x++) {
				for(int z = 0; z < 3; z++) {
					double bx = block.getX() + ((double)x / 2);
					double by = block.getY() + 0.5 + face.getModY() * 0.55;
					double bz = block.getZ() + ((double)z / 2);
					player.spawnParticle(Particle.REDSTONE, bx, by, bz, 1, 0.01, 0.01, 0.01, 0.00001, options);
				}
			}
		}
		
		if(face == BlockFace.NORTH || face == BlockFace.SOUTH) {
			for(int x = 0; x < 3; x++) {
				for(int y = 0; y < 3; y++) {
					double bx = block.getX() + ((double)x / 2);
					double by = block.getY() + ((double)y / 2);
					double bz = block.getZ() + 0.5 + face.getModZ() * 0.55;
					player.spawnParticle(Particle.REDSTONE, bx, by, bz, 1, 0.01, 0.01, 0.01, 0.00001, options);
				}
			}
		}
		
		if(face == BlockFace.EAST || face == BlockFace.WEST) {
			for(int y = 0; y < 3; y++) {
				for(int z = 0; z < 3; z++) {
					double bx = block.getX() + 0.5 + face.getModX() * 0.55;
					double by = block.getY() + ((double)y / 2);
					double bz = block.getZ() + ((double)z / 2);
					player.spawnParticle(Particle.REDSTONE, bx, by, bz, 1, 0.01, 0.01, 0.01, 0.00001, options);
				}
			}
		}
	}

}
