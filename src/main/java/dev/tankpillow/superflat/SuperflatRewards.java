package dev.tankpillow.superflat;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperflatRewards extends JavaPlugin
{
	
	public static NamespacedKey LAST_CLAIM_KEY;
	private ClaimNotificationManager notificationManager;
	
	@Override
	public void onEnable()
	{
		LAST_CLAIM_KEY = new NamespacedKey(this, "last_claim_time");
		this.getCommand("claim").setExecutor(new ClaimCmd());
		this.getCommand("claimstatus").setExecutor(new ClaimStatusCmd());
		this.getCommand("forceclaim").setExecutor(new ForceClaimCmd());
		
		Bukkit.getPluginManager().registerEvents(new DeathEvents(), this);
		
		// Start the notification system
		notificationManager = new ClaimNotificationManager(this);
		notificationManager.startNotificationScheduler();
	}
	
	@Override
	public void onDisable()
	{
		if (notificationManager != null) {
			notificationManager.stopNotificationScheduler();
		}
	}
}
