package dev.tankpillow.superflat;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperflatRewards extends JavaPlugin
{
	
	public static NamespacedKey LAST_CLAIM_KEY;
	
	@Override
	public void onEnable()
	{
		LAST_CLAIM_KEY = new NamespacedKey(this, "last_claim_time");
		this.getCommand("claim").setExecutor(new ClaimCmd());
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
