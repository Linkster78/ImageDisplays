package com.tek.idisplays.map;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

import com.tek.idisplays.Main;
import com.tek.idisplays.Reference;
import com.tek.idisplays.Selection;
import com.tek.idisplays.async.MapCreation;

import net.coobird.thumbnailator.Thumbnails;

public class DisplayManager extends MapRenderer {
	
	public static final int MAP_SIZE = 128;
	
	public static void attemptCreateDisplay(final Selection selection, final Player player) {
		Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			if(selection.imageExists()) {
				try {
					BufferedImage image;
					if(selection.isURL()) {
						image = ImageIO.read(new URL(selection.getImageName()));
					} else {
						image = ImageIO.read(selection.getImageFile());
					}
					
					int mapWidth = 0;
					int mapHeight = 0;
					if(selection.getFace() == BlockFace.NORTH || selection.getFace() == BlockFace.SOUTH) {
						mapWidth = Math.abs(selection.getFrom().getX() - selection.getTo().getX()) + 1;
						mapHeight = Math.abs(selection.getFrom().getY() - selection.getTo().getY()) + 1;
					}
					if(selection.getFace() == BlockFace.EAST || selection.getFace() == BlockFace.WEST) {
						mapWidth = Math.abs(selection.getFrom().getZ() - selection.getTo().getZ()) + 1;
						mapHeight = Math.abs(selection.getFrom().getY() - selection.getTo().getY()) + 1;
					}
					int width = mapWidth * MAP_SIZE;
					int height = mapHeight * MAP_SIZE;
					
					image = Thumbnails.of(image)
							.size(width, height)
							.asBufferedImage();
					
					ItemStack[] maps = new ItemStack[mapWidth * mapHeight];
					for(int mapX = 0; mapX < mapWidth; mapX++) {
						for(int mapY = 0; mapY < mapHeight; mapY++) {
							BufferedImage imagePart = image.getSubimage(mapX * MAP_SIZE, mapY * MAP_SIZE, 
									MAP_SIZE, MAP_SIZE);
							ItemStack map = new ItemStack(Material.FILLED_MAP);
							MapMeta mapMeta = (MapMeta) map.getItemMeta();
		                    MapView view = Bukkit.createMap(player.getWorld());
		                    view.getRenderers().clear();
		                    view.addRenderer(new DisplayManager(imagePart));
		                    mapMeta.setMapView(view);
		                    map.setItemMeta(mapMeta);
							maps[mapX * mapHeight + mapY] = map;
						}
					}
					
					Block fromBlock = selection.getFrom();
					Block toBlock = selection.getTo();
					int minX = Math.min(fromBlock.getX(), toBlock.getX());
					int minY = Math.min(fromBlock.getY(), toBlock.getY());
					int minZ = Math.min(fromBlock.getZ(), toBlock.getZ());
					int maxX = Math.max(fromBlock.getX(), toBlock.getX());
					int maxY = Math.max(fromBlock.getY(), toBlock.getY());
					int maxZ = Math.max(fromBlock.getZ(), toBlock.getZ());
					
					int mapIndex = 0;
					if(selection.getFace() == BlockFace.EAST) {
						for(int z = maxZ; z >= minZ; z--) {
							for(int y = maxY; y >= minY; y--) {
								Block blockAt = fromBlock.getWorld().getBlockAt(minX, y, z);
								ItemStack map = maps[mapIndex];
								mapIndex++;
								
								Main.getInstance().getMapCreationManager().queue(new MapCreation(blockAt, selection.getFace(), map));
							}
						}
					}
					if(selection.getFace() == BlockFace.WEST) {
						for(int z = minZ; z <= maxZ; z++) {
							for(int y = maxY; y >= minY; y--) {
								Block blockAt = fromBlock.getWorld().getBlockAt(minX, y, z);
								ItemStack map = maps[mapIndex];
								mapIndex++;
								
								Main.getInstance().getMapCreationManager().queue(new MapCreation(blockAt, selection.getFace(), map));
							}
						}
					}
					if(selection.getFace() == BlockFace.NORTH) {
						for(int x = maxX; x >= minX; x--) {
							for(int y = maxY; y >= minY; y--) {
								Block blockAt = fromBlock.getWorld().getBlockAt(x, y, minZ);
								ItemStack map = maps[mapIndex];
								mapIndex++;
								
								Main.getInstance().getMapCreationManager().queue(new MapCreation(blockAt, selection.getFace(), map));
							}
						}
					}
					if(selection.getFace() == BlockFace.SOUTH) {
						for(int x = minX; x <= maxX; x++) {
							for(int y = maxY; y >= minY; y--) {
								Block blockAt = fromBlock.getWorld().getBlockAt(x, y, minZ);
								ItemStack map = maps[mapIndex];
								mapIndex++;
								
								Main.getInstance().getMapCreationManager().queue(new MapCreation(blockAt, selection.getFace(), map));
							}
						}
					}
					
					player.sendMessage(Reference.PREFIX + Reference.color("&aSuccess! The display has been created."));
				} catch (IOException e) {
					player.sendMessage(Reference.PREFIX + Reference.color("&cCouldn't create display. The image is corrupted or unaccessible."));
				}
			} else {
				player.sendMessage(Reference.PREFIX + Reference.color("&cCouldn't create display. The image no longer exists."));
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	public static void attemptDisplayDeletion(ItemFrame baseFrame, Player p) {
		final long start = System.currentTimeMillis();
		Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
			p.sendMessage(Reference.PREFIX + Reference.color("&aStarted deletion of the display..."));
			
			List<ItemFrame> forRemoval = new ArrayList<ItemFrame>();
			List<ItemFrame> stack = new ArrayList<ItemFrame>();
			List<ItemFrame> addQueue = new ArrayList<ItemFrame>();
			stack.add(baseFrame);
			while(!stack.isEmpty()) {
				Iterator<ItemFrame> stackIterator = stack.iterator();
				while(stackIterator.hasNext()) {
					ItemFrame current = stackIterator.next();
					forRemoval.add(current);
					stackIterator.remove();
					for(Entity inRange : current.getNearbyEntities(2, 2, 2)) {
						if(inRange instanceof ItemFrame) {
							if(!stack.contains(inRange) && !addQueue.contains(inRange) && !forRemoval.contains(inRange)) {
								addQueue.add((ItemFrame) inRange);
							}
						}
					}
				}
				stack.clear();
				stack.addAll(addQueue);
				addQueue.clear();
			}
			
			for(ItemFrame toRemove : forRemoval) {
				if(toRemove.getItem() != null) {
					if(toRemove.getItem().getType().equals(Material.FILLED_MAP)) {
						MapMeta meta = (MapMeta) toRemove.getItem().getItemMeta();
						Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
						if(mapCached.isPresent()) {
							Main.getInstance().getMapManager().getMapCache().remove(mapCached.get());
							toRemove.remove();
						}
					}
				}
			}
			
			long now = System.currentTimeMillis();
			p.sendMessage(Reference.PREFIX + Reference.color("&aSuccess! Deleted &6" + forRemoval.size() + " &aitem frames, took &6" + (now - start) + "&ams"));
		});
	}
	
	private boolean drawn;
	private Image image;
	private byte[][] pixelMap;
	
	public DisplayManager(Image image) {
		this.image = image;
		this.drawn = false;
	}
	
	public DisplayManager(byte[][] pixelMap) {
		this.pixelMap = pixelMap;
		this.drawn = false;
	}
	
	@Override
	public void render(MapView view, MapCanvas canvas, Player player) {
		if(drawn) return;
		if(image != null) {
			canvas.drawImage(0, 0, image);
			CachedMap cachedMap = new CachedMap(view.getId(), canvasToBytes(canvas));
			Main.getInstance().getMapManager().getMapCache().add(cachedMap);
		} else {
			for(int x = 0; x < pixelMap.length; x++) {
				for(int y = 0; y < pixelMap[0].length; y++) {
					canvas.setPixel(x, y, pixelMap[x][y]);
				}
			}
		}
		drawn = true;
	}
	
	public static byte[][] canvasToBytes(MapCanvas canvas) {
		byte[][] pixelMap = new byte[MAP_SIZE][MAP_SIZE];
		for(int x = 0; x < MAP_SIZE; x++) {
			for(int y = 0; y < MAP_SIZE; y++) {
				pixelMap[x][y] = canvas.getPixel(x, y);
			}
		}
		return pixelMap;
	}
	
}
