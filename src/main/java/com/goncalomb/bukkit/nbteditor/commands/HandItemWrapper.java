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

package com.goncalomb.bukkit.nbteditor.commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import com.goncalomb.bukkit.mylib.command.MyCommandException;

public abstract class HandItemWrapper<T extends ItemMeta> {

	public static final class Item extends HandItemWrapper<ItemMeta> {

		public Item(Player player) throws MyCommandException {
			super(ItemMeta.class, player);
			if (meta == null) {
				throw new MyCommandException("§cYou must be holding an Item.");
			}
		}

	}

	public static final class Potion extends HandItemWrapper<PotionMeta> {

		public Potion(Player player) throws MyCommandException {
			super(PotionMeta.class, player);
			if (meta == null) {
				throw new MyCommandException("§cYou must be holding a Potion.");
			}
		}

	}

	public final ItemStack item;
	public final T meta;

	@SuppressWarnings("unchecked")
	private HandItemWrapper(Class<T> clazz, Player player) {
		item = player.getInventory().getItemInMainHand();
		if (item.getType() != Material.AIR) {
			ItemMeta m = item.getItemMeta();
			if (clazz.isInstance(m)) {
				meta = (T) m;
				return;
			}
		}
		meta = null;
	}

	public void save() {
		item.setItemMeta(meta);
	}

}
