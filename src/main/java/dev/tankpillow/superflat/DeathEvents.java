package dev.tankpillow.superflat;

import org.bukkit.Material;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class DeathEvents implements Listener
{
	@EventHandler
	public void onZombieDeath(EntityDeathEvent event) {
		if (event.getEntity() instanceof Zombie) {
	        // 10% chance for iron ingot
	        double ironChance = 0.1;
	        if (Math.random() < ironChance) {
	            event.getDrops().add(new ItemStack(Material.IRON_INGOT, 1));
	        }
	        
	        // 15% chance for carrots (1-3)
	        double carrotChance = 0.15;
	        if (Math.random() < carrotChance) {
	            int carrotAmount = 1 + (int)(Math.random() * 3); // 1-3 carrots
	            event.getDrops().add(new ItemStack(Material.CARROT, carrotAmount));
	        }
	        
	        // 15% chance for potatoes (1-3)
	        double potatoChance = 0.15;
	        if (Math.random() < potatoChance) {
	            int potatoAmount = 1 + (int)(Math.random() * 3); // 1-3 potatoes
				if (event.getEntity().getFireTicks() > 0) {
					event.getDrops().add(new ItemStack(Material.BAKED_POTATO, 1));
				} else {
					event.getDrops().add(new ItemStack(Material.POTATO, potatoAmount));
				}
	        }
	    }
	}
}
