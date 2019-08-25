package com.tek.idisplays.listeners;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import com.tek.idisplays.Main;
import com.tek.idisplays.map.CachedMap;
import com.tek.idisplays.map.DisplayManager;

public class WorldListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onWorldLoad(WorldLoadEvent event) {
		event.getWorld().getEntitiesByClass(ItemFrame.class).forEach(frame -> {
			if(frame.getItem() != null) {
				if(frame.getItem().getType().equals(Material.FILLED_MAP)) {
					MapMeta meta = (MapMeta) frame.getItem().getItemMeta();
					Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
					if(mapCached.isPresent()) {
						MapView view = meta.getMapView();
						view.getRenderers().clear();
						view.addRenderer(new DisplayManager(mapCached.get().getPixelMap()));
						meta.setMapView(view);
						frame.getItem().setItemMeta(meta);
					}
				}
			}
		});
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event) {
		Arrays.stream(event.getChunk().getEntities()).forEach(entity -> {
			if(entity instanceof ItemFrame) {
				ItemFrame frame = (ItemFrame) entity;
				if(frame.getItem() != null) {
					if(frame.getItem().getType().equals(Material.FILLED_MAP)) {
						MapMeta meta = (MapMeta) frame.getItem().getItemMeta();
						Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
						if(mapCached.isPresent()) {
							MapView view = meta.getMapView();
							view.getRenderers().clear();
							view.addRenderer(new DisplayManager(mapCached.get().getPixelMap()));
							meta.setMapView(view);
							frame.getItem().setItemMeta(meta);
						}
					}
				}
			}
		});
	}
	
}
