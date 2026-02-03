package dev.tankpillow.superflat;

import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;

public class RewardManager 
{
	public static final Random random = new Random();
	
	private static class Reward
	{
		private final Material material;
		private final int minAmount;
		private final int maxAmount;
		
		public Reward(Material material, int minAmount, int maxAmount)
		{
			this.material = material;
			this.minAmount = minAmount;
			this.maxAmount = maxAmount;
		}
	};
	
	private static final List<Reward> REWARDS = List.of(
		new Reward(Material.STICK, 1, 4),
		new Reward(Material.STRING, 8, 12),
		new Reward(Material.ARROW, 8, 12),
		new Reward(Material.GLASS_BOTTLE, 1, 1),
		new Reward(Material.SUGAR_CANE, 1, 2),
		new Reward(Material.TORCH, 4, 8),
		new Reward(Material.LAVA_BUCKET, 1, 1),
		new Reward(Material.WATER_BUCKET, 1, 1),
		new Reward(Material.LEATHER, 1, 4),
		new Reward(Material.SADDLE, 1, 1),
		new Reward(Material.TOTEM_OF_UNDYING, 1, 1),
		
		new Reward(Material.APPLE, 2, 4),
		new Reward(Material.CARROT, 1, 3),
		new Reward(Material.POTATO, 1, 3),
		
		new Reward(Material.WOODEN_SWORD, 1, 1),
		new Reward(Material.WOODEN_AXE, 1, 1),
		new Reward(Material.WOODEN_PICKAXE, 1, 1),
		new Reward(Material.WOODEN_SHOVEL, 1, 1),
		new Reward(Material.WOODEN_HOE, 1, 1),
		new Reward(Material.SHIELD, 1, 1),
		new Reward(Material.FLINT_AND_STEEL, 1, 1),
		
		new Reward(Material.COAL, 1, 4),
		new Reward(Material.CHARCOAL, 1, 4),
		new Reward(Material.IRON_INGOT, 1, 2),
		new Reward(Material.GOLD_INGOT, 1, 4),
		new Reward(Material.REDSTONE, 2, 4),
		new Reward(Material.DIAMOND, 1, 2),
		new Reward(Material.EMERALD, 1, 1),
		
		new Reward(Material.SAND, 2, 4),
		new Reward(Material.GRAVEL, 2, 4),
		new Reward(Material.GLASS, 2, 4),
		new Reward(Material.GLOWSTONE_DUST, 1, 4),
		
		new Reward(Material.MUD, 1, 3),
		new Reward(Material.OAK_LOG, 1, 4),
		new Reward(Material.COBBLESTONE, 3, 6),
		new Reward(Material.COBBLESTONE_SLAB, 4, 8),
		new Reward(Material.OAK_SLAB, 4, 8),
		new Reward(Material.WHITE_WOOL, 1, 2),
		new Reward(Material.OBSIDIAN, 1, 1),
		
		new Reward(Material.RED_BED, 1, 1),
		new Reward(Material.FURNACE, 1, 1),
		new Reward(Material.CHEST, 1, 2),
		new Reward(Material.CAMPFIRE, 1, 1),
		new Reward(Material.BARREL, 1, 1),
		new Reward(Material.OAK_DOOR, 1, 1),
		new Reward(Material.OAK_TRAPDOOR, 1, 1),
		new Reward(Material.JUKEBOX, 1, 1)
	);
	
	public static void giveRandomReward(Player player)
	{
		Reward reward = REWARDS.get(random.nextInt(REWARDS.size()));

        int amount = reward.minAmount;
        if (reward.maxAmount > reward.minAmount) {
            amount = reward.minAmount + random.nextInt(
                    reward.maxAmount - reward.minAmount + 1
            );
        }

        ItemStack item = new ItemStack(reward.material, amount);
        player.getInventory().addItem(item);
		player.sendMessage(Component.text(ChatColor.translateAlternateColorCodes('&', "&aYou recieved a superflat survival reward.")));
	}
	
}
