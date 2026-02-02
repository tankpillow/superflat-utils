package dev.tankpillow.superflat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class RewardTask implements Runnable
{

	@Override
	public void run() {
		for(Player player : Bukkit.getOnlinePlayers()) {
			RewardManager.giveRandomReward(player);
		}
	}

}
