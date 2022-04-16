package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PotionVariable extends SingleItemVariable {

	public PotionVariable(String key) {
		super(key);
	}

	@Override
	public String getFormat() {
		return "A potion.";
	}

	@Override
	public void setItem(ItemStack item) {
		if (item != null) {
			Material type = item.getType();
			if (type != Material.SPLASH_POTION && type != Material.LINGERING_POTION) {
				item.setType(Material.SPLASH_POTION);
			}
		}
		super.setItem(item);
	}

	@Override
	public boolean isValidItem(Player player, ItemStack item) {
		Material type = item.getType();
		if (type != Material.POTION && type != Material.SPLASH_POTION && type != Material.LINGERING_POTION) {
			player.sendMessage("§cThat must be a must be a potion!");
			return false;
		}
		return true;
	}

}
