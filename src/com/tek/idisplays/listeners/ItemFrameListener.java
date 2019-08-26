package com.tek.idisplays.listeners;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.inventory.meta.MapMeta;

import com.tek.idisplays.Main;
import com.tek.idisplays.map.CachedMap;
import com.tek.idisplays.map.DisplayManager;

public class ItemFrameListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFrameRemove(HangingBreakEvent event) {
		if(event.getEntity() instanceof ItemFrame) {
			ItemFrame f = (ItemFrame)event.getEntity();
			if(f.getItem() != null) {
				if(f.getItem().getType().equals(Material.FILLED_MAP)) {
					MapMeta meta = (MapMeta) f.getItem().getItemMeta();
					Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
					if(mapCached.isPresent()) {
						Main.getInstance().getMapManager().getMapCache().remove(mapCached.get());
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onFrameHit(EntityDamageByEntityEvent event) {
		if(event.getEntity() instanceof ItemFrame) {
			ItemFrame f = (ItemFrame)event.getEntity();
			if(f.getItem() != null) {
				if(f.getItem().getType().equals(Material.FILLED_MAP)) {
					if(event.getDamager() instanceof Player) {
						Player p = (Player) event.getDamager();
						if(Main.getInstance().getDeletions().contains(p.getUniqueId())) {
							event.setCancelled(true);
							Main.getInstance().getDeletions().remove(p.getUniqueId());
							DisplayManager.attemptDisplayDeletion(f, p);
						} else {
							MapMeta meta = (MapMeta) f.getItem().getItemMeta();
							Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
							if(mapCached.isPresent()) {
								event.setCancelled(true);
							}
						}
					} else {
						MapMeta meta = (MapMeta) f.getItem().getItemMeta();
						Optional<CachedMap> mapCached = Main.getInstance().getMapManager().getMapCache(meta.getMapId());
						if(mapCached.isPresent()) {
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
}
