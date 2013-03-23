package com.goncalomb.bukkit;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public final class UtilsMc {
	
	private static HashSet<Byte> NON_SOLID_BLOCK_IDS = new HashSet<Byte>();
	
	static {
		NON_SOLID_BLOCK_IDS.add((byte) 0);
		for (Material mat : Material.values()) {
			if (mat.isBlock() && !mat.isSolid()) {
				NON_SOLID_BLOCK_IDS.add((byte) mat.getId());
			}
		}
	}
	
	private UtilsMc() { }
	
	public static String parseColors(String str) {
		return str.replaceAll("&", "§").replaceAll("§§", "&");
	}
	
	public static int parseTickDuration(String str) {
		int duration;
		try { 
			duration = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			duration = Utils.parseTimeDuration(str)*20;
		}
		if (duration < 0) {
			return -1;
		}
		return duration;
	}
	
	public static Location airLocation(Location loc) {
		World world = loc.getWorld();
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		int maxY = world.getMaxHeight();
		while (y < maxY && !NON_SOLID_BLOCK_IDS.contains((byte) world.getBlockAt(x, y, z).getTypeId())) {
			y++;
		}
		return new Location(world, x + 0.5, y + 0.2, z + 0.5);
	}
	
	public static Block getTargetBlock(Player player) {
		return getTargetBlock(player, 50);
	}
	
	public static Block getTargetBlock(Player player, int distance) {
		List<Block> blocks = player.getLastTwoTargetBlocks(NON_SOLID_BLOCK_IDS, distance);
		return blocks.get(blocks.size() - 1);
	}
	
	public static ItemStack newWrittenBook(String title, String author) {
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		BookMeta meta = (BookMeta) book.getItemMeta();
		meta.setTitle(title);
		meta.setAuthor(author);
		book.setItemMeta(meta);
		return book;
	}
	
	public static void broadcastToWorld(World world, String message) {
		for (Player player : world.getPlayers()) {
			player.sendMessage(message);
		}
	}
	
	public static Vector faceToDelta(BlockFace face) {
		return new Vector(1, 1, 1).add(new Vector(face.getModX(), face.getModY(), face.getModZ())).multiply(0.5);
	}
	
	public static Vector faceToDelta(BlockFace face, double distance) {
		Vector delta = faceToDelta(face);
		return new Vector(-0.5, -0.5, -0.5).add(delta).normalize().multiply(distance).add(delta);
	}
	
	public static ItemStack newItem(Material material, String name) {
		return newItem(new MaterialData(material), name);
	}
	
	public static ItemStack newItem(Material material, String name, String... lore) {
		return newItem(new MaterialData(material), name, lore);
	}
	
	public static ItemStack newItem(MaterialData material, String name) {
		return newItem(material, name, (String[]) null);
	}
	
	public static ItemStack newItem(MaterialData material, String name, String... lore) {
		ItemStack item = material.toItemStack();
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore == null ? null : Arrays.asList(lore));
		item.setItemMeta(meta);
		return item;
	}
	
}
