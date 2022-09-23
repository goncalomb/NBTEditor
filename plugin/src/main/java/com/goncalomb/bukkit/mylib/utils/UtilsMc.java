/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
 *
 * This file is part of NBTEditor.
 *
 * NBTEditor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBTEditor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBTEditor.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.goncalomb.bukkit.mylib.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class UtilsMc {

	private static HashSet<Material> NON_TARGETABLE_BLOCKS = new HashSet<Material>();

	static {
		NON_TARGETABLE_BLOCKS.add(Material.AIR);
		for (Material mat : Material.values()) {
			if (mat.isBlock() && !mat.isSolid()) {
				NON_TARGETABLE_BLOCKS.add(mat);
			}
		}
		NON_TARGETABLE_BLOCKS.remove(Material.END_GATEWAY);
	}

	private UtilsMc() { }

	public static String parseColors(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
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
		while (y < maxY && !NON_TARGETABLE_BLOCKS.contains(world.getBlockAt(x, y, z).getType())) {
			y++;
		}
		return new Location(world, x + 0.5, y + 0.2, z + 0.5);
	}

	public static Block getTargetBlock(Player player) {
		return getTargetBlock(player, 50);
	}

	public static Block getTargetBlock(Player player, int distance) {
		List<Block> blocks = player.getLastTwoTargetBlocks(NON_TARGETABLE_BLOCKS, distance);
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

	public static ItemStack newSingleItemStack(Material material, String name) {
		return newSingleItemStack(material, name, (String[]) null);
	}

	public static ItemStack newSingleItemStack(Material material, String name, String... lore) {
		return newSingleItemStack(material, name, (lore == null ? null : Arrays.asList(lore)));
	}

	public static ItemStack newSingleItemStack(Material material, String name, List<String> lore) {
		ItemStack item = new ItemStack(material, 1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static boolean offsetItemStackDamage(ItemStack item, int offset) {
		ItemMeta meta = item.getItemMeta();
		if (meta instanceof Damageable) {
			((Damageable) meta).setDamage(((Damageable) meta).getDamage() + offset);
			item.setItemMeta(meta);
			return true;
		}
		return false;
	}

	public static Permission getRootPermission(Plugin plugin) {
		String permName = plugin.getName().toLowerCase() + ".*";
		Permission perm = Bukkit.getPluginManager().getPermission(permName);
		if (perm == null) {
			perm = new Permission(permName, PermissionDefault.OP);
			Bukkit.getPluginManager().addPermission(perm);
		}
		return perm;
	}

	public static UUID convertFromUUIDInts(int[] uuidData) {
		return new UUID(((long) uuidData[0] << 32) | uuidData[1], ((long) uuidData[2] << 32) | uuidData[3]);
	}

	public static int[] convertToUUIDInts(UUID uuid) {
		return new int[] {
			(int)((uuid.getMostSignificantBits() >> 32)  & 0xFFFFFFFF),
			(int)(uuid.getMostSignificantBits()          & 0xFFFFFFFF),
			(int)((uuid.getLeastSignificantBits() >> 32) & 0xFFFFFFFF),
			(int)(uuid.getLeastSignificantBits()         & 0xFFFFFFFF)
		};
	}
}
