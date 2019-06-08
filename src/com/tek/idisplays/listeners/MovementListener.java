package com.tek.idisplays.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataType;

import com.tek.idisplays.Main;
import com.tek.idisplays.Reference;
import com.tek.idisplays.Selection;

public class MovementListener implements Listener {
	
	@EventHandler
	public void onItemDrop(PlayerDropItemEvent event) {
		if(Main.getInstance().getSelections().containsKey(event.getPlayer().getUniqueId())) {
			if(event.getItemDrop().getItemStack().hasItemMeta()) {
				if(event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(Reference.wandIdentifier, PersistentDataType.BYTE)) {
					event.getItemDrop().remove();
					Main.getInstance().getSelections().remove(event.getPlayer().getUniqueId());
					event.getPlayer().sendMessage(Reference.PREFIX + Reference.color("&cCancelled display creation"));
				}
			}
		}
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent event) {
		if(Main.getInstance().getSelections().containsKey(event.getPlayer().getUniqueId())) {
			Main.getInstance().getSelections().remove(event.getPlayer().getUniqueId());
			event.getPlayer().sendMessage(Reference.PREFIX + Reference.color("&cCancelled display creation"));
		}
	}
	
	@EventHandler
	public void onMoveTooFar(PlayerMoveEvent event) {
		if(Main.getInstance().getSelections().containsKey(event.getPlayer().getUniqueId())) {
			Selection selection = Main.getInstance().getSelections().get(event.getPlayer().getUniqueId());
			if(selection.getFrom() != null) {
				if(selection.getFrom().getLocation().distance(event.getPlayer().getLocation()) >= 128) {
					Main.getInstance().getSelections().remove(event.getPlayer().getUniqueId());
					event.getPlayer().sendMessage(Reference.PREFIX + Reference.color("&cCancelled display creation (Too Large)"));
				}
			}
		}
	}
	
}
