package dev.tankpillow.superflat;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class SuperflatRewards extends JavaPlugin
{
	
	private static final long FIVE_DAYS_TICKS = 5 * 24000L;
	
	@Override
	public void onEnable()
	{
		Bukkit.getScheduler().runTaskTimer(this, new RewardTask(), FIVE_DAYS_TICKS, FIVE_DAYS_TICKS);
	}
	
	@Override
	public void onDisable()
	{
		
	}
}
