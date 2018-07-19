package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworksItemVariable extends SingleItemVariable {

	public FireworksItemVariable() {
		super("FireworksItem");
	}

	@Override
	public String getFormat() {
		return "A firework rocket.";
	}

	@Override
	public void setItem(ItemStack item) {
		super.setItem(item);
		if (item != null) {
			data().setInt("LifeTime", 12 + 12 * ((FireworkMeta) item.getItemMeta()).getPower());
		}
	}

	@Override
	public boolean isValidItem(Player player, ItemStack item) {
		if (item.getType() != Material.FIREWORK_ROCKET) {
			player.sendMessage("§cThat must be a firework rocket!");
			return false;
		}
		return true;
	}
}
