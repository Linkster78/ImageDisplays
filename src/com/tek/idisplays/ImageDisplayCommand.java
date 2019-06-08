package com.tek.idisplays;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ImageDisplayCommand implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage(Reference.PREFIX + Reference.color("&cYou must be a player to use this"));
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length >= 1) {
			if(args[0].equalsIgnoreCase("create")) {
				if(args.length == 2) {
					if(sender.hasPermission(Reference.PERMISSION_CREATE)) {
						if(!Main.getInstance().getSelections().containsKey(player.getUniqueId())) {
							Selection selection = new Selection(args[1]);
							if(selection.imageExists()) {
								Main.getInstance().getSelections().put(player.getUniqueId(), selection);
								player.getInventory().addItem(Reference.creationWand);
								sender.sendMessage(Reference.PREFIX + Reference.color("&aGiven you the display creation wand"));
								sender.sendMessage(Reference.color("&7- &6Right click on the face of the first block"));
								sender.sendMessage(Reference.color("&7- &6Right click on the other block"));
								sender.sendMessage(Reference.color("&7- &6You can cancel display creation by dropping the item"));
							} else {
								sender.sendMessage(Reference.PREFIX + Reference.color("&cThat image does not exist"));
							}
						} else {
							sender.sendMessage(Reference.PREFIX + Reference.color("&cYou're already creating a display"));
						}
					} else {
						sender.sendMessage(Reference.PREFIX + Reference.color("&cYou do not have enough permissions to use this"));
					}
				} else {
					sender.sendMessage(Reference.PREFIX + Reference.color("&cInvalid syntax"));
				}
			} 
			
			else if(args[0].equalsIgnoreCase("remove") || args[0].equalsIgnoreCase("delete")) {
				if(args.length == 1) {
					if(sender.hasPermission(Reference.PERMISSION_REMOVE)) {
						if(!Main.getInstance().getDeletions().contains(player.getUniqueId())) {
							Main.getInstance().getDeletions().add(player.getUniqueId());
							sender.sendMessage(Reference.PREFIX + Reference.color("&aHit a display and it will be completely removed"));
						} else {
							sender.sendMessage(Reference.PREFIX + Reference.color("&cYou're already removing a display"));
						}
					} else {
						sender.sendMessage(Reference.PREFIX + Reference.color("&cYou do not have enough permissions to use this"));
					}
				} else {
					sender.sendMessage(Reference.PREFIX + Reference.color("&cInvalid syntax"));
				}
			} 
			
			else if(args[0].equalsIgnoreCase("help")) {
				if(args.length == 1) {
					sender.sendMessage(Reference.color("&a&lImage&2Displays &6Help Menu"));
					sender.sendMessage(Reference.color("&8- &a/imagedisplays create <file/url> &8- &aCreates a display according to your selection"));
					sender.sendMessage(Reference.color("&8- &a/imagedisplays remove &8- &aPuts you in deletion mode (Hit a display to remove it)"));
					sender.sendMessage(Reference.color("&8- &a/imagedisplays help &8- &aProvides the help menu of the plugin"));
				} else {
					sender.sendMessage(Reference.PREFIX + Reference.color("&cInvalid syntax"));
				}
			} 
			
			else {
				sender.sendMessage(Reference.PREFIX + Reference.color("&cInvalid syntax"));
			}
		} else {
			sender.sendMessage(Reference.PREFIX + Reference.color("&cInvalid syntax"));
		}
		
		return false;
	}
	
	public static class ImageDisplayCompleter implements TabCompleter {
		@Override
		public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
			if(!(sender instanceof Player)) {
				return Arrays.asList();
			}
			
			if(args.length == 1) return Arrays.asList("help", "create", "remove", "delete").stream().filter(cmd -> cmd.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList());
			if(args.length == 2 && args[0].equalsIgnoreCase("create")) return Arrays.stream(Reference.bannerFolder.listFiles()).map(File::getName)
					.filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase())).collect(Collectors.toList());
			return Arrays.asList();
		}
	}

}
