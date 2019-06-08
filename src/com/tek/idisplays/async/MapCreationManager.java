package com.tek.idisplays.async;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.bukkit.entity.ItemFrame;

public class MapCreationManager implements Runnable {

	private final int FRAME_COUNT = 32;
	private Queue<MapCreation> creationQueue;
	
	public MapCreationManager() {
		this.creationQueue = new LinkedBlockingQueue<MapCreation>();
	}
	
	@Override
	public void run() {
		for(int i = 0; i < FRAME_COUNT; i++) {
			if(!creationQueue.isEmpty()) {
				MapCreation map = creationQueue.poll();
				
				ItemFrame frame = (ItemFrame) map.getBlock().getWorld().spawn(map.getBlock().getLocation(), ItemFrame.class, f -> {
					f.setFacingDirection(map.getBlockFace());
				});
				
				frame.setItem(map.getMapItem());
			} else {
				break;
			}
		}
	}
	
	public void queue(MapCreation creation) {
		creationQueue.add(creation);
	}
	
}
