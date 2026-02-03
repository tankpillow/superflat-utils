package dev.tankpillow.superflat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class ClaimCmd implements CommandExecutor
{
	private static final long FIVE_MINECRAFT_DAYS_MS = 5L * 24 * 60 * 1000; // 5 minecraft days (20 minutes per day)
	private static final long CLAIM_WINDOW_MS = 5 * 60 * 1000; // 5-minute claiming window

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if(!(sender instanceof Player)) {
			return false;
		}
		
		Player player = (Player) sender;
		
		// Check if player can claim
		long lastClaimTime = player.getPersistentDataContainer().getOrDefault(
			SuperflatRewards.LAST_CLAIM_KEY,
			PersistentDataType.LONG,
			0L
		);
		
		long currentTime = System.currentTimeMillis();
		long timeSinceLastClaim = currentTime - lastClaimTime;
		
		// Calculate which cycle we're in and time into current cycle
		long timeIntoCurrentCycle = timeSinceLastClaim % FIVE_MINECRAFT_DAYS_MS;
		
		if (timeSinceLastClaim < FIVE_MINECRAFT_DAYS_MS) {
			// Less than 5 days have passed
			long timeRemaining = FIVE_MINECRAFT_DAYS_MS - timeSinceLastClaim;
			long minutesRemaining = timeRemaining / (60 * 1000);
			long secondsRemaining = (timeRemaining % (60 * 1000)) / 1000;
			
			player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
				"&cYou can claim again in " + minutesRemaining + "m " + secondsRemaining + "s")));
			return true;
		} else if (timeIntoCurrentCycle < CLAIM_WINDOW_MS) {
			// We're in the 5-minute claiming window - allow claim
			RewardManager.giveRandomReward(player);
			player.getPersistentDataContainer().set(
				SuperflatRewards.LAST_CLAIM_KEY,
				PersistentDataType.LONG,
				currentTime
			);
			return true;
		} else {
			// We're past the claiming window
			long timeUntilNextWindow = FIVE_MINECRAFT_DAYS_MS - timeIntoCurrentCycle;
			long minutesRemaining = timeUntilNextWindow / (60 * 1000);
			long secondsRemaining = (timeUntilNextWindow % (60 * 1000)) / 1000;
			
			player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', 
				"&cYou missed the claiming window! Next window in " + minutesRemaining + "m " + secondsRemaining + "s")));
			return true;
		}
	}

}
