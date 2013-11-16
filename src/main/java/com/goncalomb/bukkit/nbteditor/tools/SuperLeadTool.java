package com.goncalomb.bukkit.nbteditor.tools;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.material.MaterialData;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;

public final class SuperLeadTool extends CustomItem {

	public SuperLeadTool() {
		super("super-lead", ChatColor.GOLD + "Super Lead", new MaterialData(Material.LEASH));
		setLore(ChatColor.YELLOW + "Right-click on entities to rope them.",
				ChatColor.YELLOW + "Right-click another entity while sneaking",
				ChatColor.YELLOW + "  to rope them together.");
	}
	
	private static List<LivingEntity> findLeashPrisoners(Entity holder) {
		List<LivingEntity> entities = new ArrayList<LivingEntity>();
		for (LivingEntity living : holder.getWorld().getEntitiesByClass(LivingEntity.class)) {
			if (living.isLeashed() && living.getLeashHolder().equals(holder)) {
				entities.add(living);
			}
		}
		return entities;
	}
	
	@Override
	public  void onInteractEntity(PlayerInteractEntityEvent event, PlayerDetails details) {
		Entity other = event.getRightClicked();
		if (event.getPlayer().isSneaking()) {
			for (LivingEntity living : findLeashPrisoners(event.getPlayer())) {
				if (!living.equals(other)) {
					living.setLeashHolder(other);
				}
			}
		} else if (other instanceof LivingEntity) {
			LivingEntity living = (LivingEntity) other;
			if (living.isLeashed()) {
				living.setLeashHolder(null);
			} else {
				living.setLeashHolder(event.getPlayer());
			}
		} else {
			event.getPlayer().sendMessage(ChatColor.RED + "Not a valid entity!");
		}
		event.setCancelled(true);
	}
	
	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
