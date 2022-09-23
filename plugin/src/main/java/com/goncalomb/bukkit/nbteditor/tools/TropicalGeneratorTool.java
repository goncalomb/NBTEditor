package com.goncalomb.bukkit.nbteditor.tools;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;

public class TropicalGeneratorTool extends CustomItem {

	private int _count = 10;
	private boolean _dropItems = true;

	public TropicalGeneratorTool() {
		super("tropical-generator", ChatColor.AQUA + "Tropical Fish Generator", Material.STICK);
		setLore(ChatColor.YELLOW + "Left-click to spawn tropical fish at the cursor position.",
				ChatColor.YELLOW + "Right-click to spawn tropical fish here.");
		setDefaultConfig("count", _count);
		setDefaultConfig("drop-items", _dropItems);
	}

	@Override
	protected void applyConfig(ConfigurationSection section) {
		super.applyConfig(section);
		_count = section.getInt("count", _count);
		_dropItems = section.getBoolean("drop-items", _dropItems);
	}

	private void spawnFish(Location location) {
		location.add(0.5d, 1.3d, 0.5d);
		NBTTagCompound data = new NBTTagCompound();
		data.setString("id", "minecraft:tropical_fish");
		if (!_dropItems) {
			data.setString("DeathLootTable", "empty");
		}
		Random r = new Random();
		for (int i = 0; i < _count; i++) {
			int v = r.nextInt(2); // size
			v += r.nextInt(6) << 8; // pattern
			v += r.nextInt(16) << 16; // color body
			v += r.nextInt(16) << 24; // color pattern
			data.setInt("Variant", v);
			NBTUtils.spawnEntity(data, location.clone().add(r.nextDouble()*6 - 3, r.nextDouble()*3, r.nextDouble()*6 - 3));
		}
	}

	@Override
	public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
		Player player = event.getPlayer();
		Block block = UtilsMc.getTargetBlock(player);
		if (block.getType() != Material.AIR) {
			spawnFish(block.getLocation());
		} else {
			player.sendMessage("§cNo block in sight!");
		}
	}

	@Override
	public void onRightClick(PlayerInteractEvent event, PlayerDetails details) {
		spawnFish(event.getPlayer().getLocation());
	}

	@Override
	public void onDrop(PlayerDropItemEvent event) {
		event.getItemDrop().remove();
	}

}
