package dev.tankpillow.superflat;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;

import java.time.Duration;
import java.util.Collection;

public class ClaimNotificationManager 
{
	private static final long FIVE_MINECRAFT_DAYS_MS = 5L * 24 * 60 * 1000; // 5 minecraft days (20 minutes per day)
	private static final long CLAIM_WINDOW_MS = 5 * 60 * 1000; // 5-minute claiming window
	private static final long CHECK_INTERVAL_TICKS = 20L; // Check every second (20 ticks)
	
	private final SuperflatRewards plugin;
	private BukkitTask notificationTask;
	private boolean claimingWindowActive = false;
	private long lastNotificationTime = 0L;
	private static final long NOTIFICATION_COOLDOWN_MS = 30 * 1000; // 30 seconds between notifications
	
	public ClaimNotificationManager(SuperflatRewards plugin) {
		this.plugin = plugin;
	}
	
	public void startNotificationScheduler() {
		notificationTask = new BukkitRunnable() {
			@Override
			public void run() {
				checkAndNotifyClaimingStatus();
			}
		}.runTaskTimer(plugin, 0L, CHECK_INTERVAL_TICKS);
	}
	
	public void stopNotificationScheduler() {
		if (notificationTask != null && !notificationTask.isCancelled()) {
			notificationTask.cancel();
		}
	}
	
	private void checkAndNotifyClaimingStatus() {
		Collection<? extends Player> onlinePlayers = Bukkit.getOnlinePlayers();
		if (onlinePlayers.isEmpty()) {
			return;
		}
		
		long currentTime = System.currentTimeMillis();
		boolean anyPlayerCanClaim = false;
		
		// Check if any player has a claiming window open
		for (Player player : onlinePlayers) {
			if (canPlayerClaim(player, currentTime)) {
				anyPlayerCanClaim = true;
				break;
			}
		}
		
		// If claiming window just opened
		if (anyPlayerCanClaim && !claimingWindowActive) {
			claimingWindowActive = true;
			lastNotificationTime = currentTime;
			broadcastClaimingAvailable();
		}
		// If claiming window just closed
		else if (!anyPlayerCanClaim && claimingWindowActive) {
			claimingWindowActive = false;
			broadcastClaimingEnded();
		}
		// If claiming window is active and enough time has passed, send reminder
		else if (anyPlayerCanClaim && claimingWindowActive && 
				(currentTime - lastNotificationTime) >= NOTIFICATION_COOLDOWN_MS) {
			lastNotificationTime = currentTime;
			broadcastClaimingReminder();
		}
	}
	
	private boolean canPlayerClaim(Player player, long currentTime) {
		long lastClaimTime = player.getPersistentDataContainer().getOrDefault(
			SuperflatRewards.LAST_CLAIM_KEY,
			PersistentDataType.LONG,
			0L
		);
		
		long timeSinceLastClaim = currentTime - lastClaimTime;
		
		if (timeSinceLastClaim < FIVE_MINECRAFT_DAYS_MS) {
			return false; // Less than 5 days have passed
		}
		
		long timeIntoCurrentCycle = timeSinceLastClaim % FIVE_MINECRAFT_DAYS_MS;
		return timeIntoCurrentCycle < CLAIM_WINDOW_MS; // Within the 5-minute claiming window
	}
	
	private void broadcastClaimingAvailable() {
		Component titleText = Component.text(ChatColor.translateAlternateColorCodes('&', "&a&lCLAIMING AVAILABLE!"));
		Component subtitleText = Component.text(ChatColor.translateAlternateColorCodes('&', "&e&l/claim to get your reward"));
		
		Title title = Title.title(
			titleText, 
			subtitleText,
			Title.Times.times(Duration.ofMillis(500), Duration.ofSeconds(3), Duration.ofMillis(500))
		);
		
		Component chatMessage = Component.text(ChatColor.translateAlternateColorCodes('&', 
			"&a&l[SUPERFLAT] &r&eClaiming is now available! Use &a/claim &eto get your reward (5 minutes remaining)"));
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (canPlayerClaim(player, System.currentTimeMillis())) {
				player.showTitle(title);
				player.sendMessage(chatMessage);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_CHIME, 1.0f, 1.2f);
			}
		}
	}
	
	private void broadcastClaimingReminder() {
		Component chatMessage = Component.text(ChatColor.translateAlternateColorCodes('&', 
			"&a&l[SUPERFLAT] &r&eReminder: Claiming is still available! Use &a/claim &eto get your reward"));
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (canPlayerClaim(player, System.currentTimeMillis())) {
				player.sendMessage(chatMessage);
				player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.7f, 1.0f);
			}
		}
	}
	
	private void broadcastClaimingEnded() {
		Component chatMessage = Component.text(ChatColor.translateAlternateColorCodes('&', 
			"&c&l[SUPERFLAT] &r&cThe claiming window has closed. Next window opens in approximately 20 minutes."));
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			player.sendMessage(chatMessage);
			player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BASS, 1.0f, 0.8f);
		}
	}
	
	/**
	 * Get the time remaining until the next claiming window for a specific player
	 */
	public long getTimeUntilNextClaimWindow(Player player) {
		long currentTime = System.currentTimeMillis();
		long lastClaimTime = player.getPersistentDataContainer().getOrDefault(
			SuperflatRewards.LAST_CLAIM_KEY,
			PersistentDataType.LONG,
			0L
		);
		
		long timeSinceLastClaim = currentTime - lastClaimTime;
		
		if (timeSinceLastClaim < FIVE_MINECRAFT_DAYS_MS) {
			// Less than 5 days have passed
			return FIVE_MINECRAFT_DAYS_MS - timeSinceLastClaim;
		}
		
		long timeIntoCurrentCycle = timeSinceLastClaim % FIVE_MINECRAFT_DAYS_MS;
		
		if (timeIntoCurrentCycle < CLAIM_WINDOW_MS) {
			// Currently in claiming window
			return 0L;
		} else {
			// Past the claiming window in current cycle
			return FIVE_MINECRAFT_DAYS_MS - timeIntoCurrentCycle;
		}
	}
}