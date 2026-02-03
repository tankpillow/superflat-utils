package dev.tankpillow.superflat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class ForceClaimCmd implements CommandExecutor 
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		// Only allow console to use this command
		if (!(sender instanceof ConsoleCommandSender)) {
			sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
				"&cThis command can only be used from the console.")));
			return true;
		}
		
		if (args.length != 1) {
			sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
				"&cUsage: /forceclaim <player>")));
			return true;
		}
		
		String playerName = args[0];
		Player targetPlayer = Bukkit.getPlayer(playerName);
		
		if (targetPlayer == null) {
			sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
				"&cPlayer '" + playerName + "' is not online.")));
			return true;
		}
		
		// Force the claim by giving the reward and updating the timestamp
		RewardManager.giveRandomReward(targetPlayer);
		
		long currentTime = System.currentTimeMillis();
		targetPlayer.getPersistentDataContainer().set(
			SuperflatRewards.LAST_CLAIM_KEY,
			PersistentDataType.LONG,
			currentTime
		);
		
		// Notify console
		sender.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
			"&aForced claim completed for player: " + targetPlayer.getName())));
		
		// Notify the player
		targetPlayer.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
			"&a&l[SUPERFLAT] &r&eAn administrator has granted you a forced claim!")));
		
		return true;
	}
}