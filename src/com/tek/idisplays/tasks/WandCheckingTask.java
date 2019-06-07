package com.tek.idisplays.tasks;

import java.util.Iterator;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import com.tek.idisplays.Main;
import com.tek.idisplays.Reference;

public class WandCheckingTask implements Runnable {

	@Override
	public void run() {
		Iterator<UUID> uuidIterator = Main.getInstance().getSelections().keySet().iterator();
		
		while(uuidIterator.hasNext()) {
			UUID uuid = uuidIterator.next();
			Player player = Main.getInstance().getServer().getPlayer(uuid);
			if(player != null) {
				boolean found = false;
				for(int i = 0; i < player.getInventory().getContents().length; i++) {
					ItemStack item = player.getInventory().getItem(i);
					if(item != null && item.hasItemMeta()) {
						if(item.getItemMeta().getPersistentDataContainer().has(Reference.wandIdentifier, PersistentDataType.BYTE)) {
							found = true;
						}
					}
				}
				
				if(!found) {
					player.sendMessage(Reference.PREFIX + Reference.color("&cCancelled display creation"));
					uuidIterator.remove();
					Main.getInstance().getSelections().remove(uuid);
				}
			} else {
				uuidIterator.remove();
				Main.getInstance().getSelections().remove(uuid);
			}
		}
	}

}
