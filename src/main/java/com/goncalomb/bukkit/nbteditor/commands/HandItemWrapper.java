/*
 * Copyright (C) 2013, 2014 - Gonçalo Baltazar <http://goncalomb.com>
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
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;

import com.goncalomb.bukkit.bkglib.Lang;
import com.goncalomb.bukkit.bkglib.bkgcommand.BKgCommandException;
import com.goncalomb.bukkit.nbteditor.NBTEditor;

public abstract class HandItemWrapper<T extends ItemMeta> {
	
	public static final class Item extends HandItemWrapper<ItemMeta> {
		
		public Item(Player player) throws BKgCommandException {
			super(ItemMeta.class, player, true);
		}
		
	}
	
	public static final class Potion extends HandItemWrapper<PotionMeta> {
		
		public Potion(Player player) throws BKgCommandException {
			super(PotionMeta.class, player, true);
		}
		
	}
	
	public static final class Book extends HandItemWrapper<BookMeta> {
		
		public static enum BookType { BOTH, BOOK_AND_QUILL, WRITTEN }
		
		public Book(Player player, BookType bookType) throws BKgCommandException {
			super(BookMeta.class, player, (bookType == BookType.BOTH));
			if (meta == null) {
				throw new BKgCommandException(Lang._(NBTEditor.class, "meta-error.format", Lang._(NBTEditor.class, "meta-error.book-" + (bookType == BookType.BOOK_AND_QUILL ? "quill" : "written"))));
			}
		}
		
	}
	
	public static final class LeatherArmor extends HandItemWrapper<LeatherArmorMeta> {
		
		public LeatherArmor(Player player) throws BKgCommandException {
			super(LeatherArmorMeta.class, player, true);
		}
		
	}
	
	public final ItemStack item;
	public final T meta;
	
	@SuppressWarnings("unchecked")
	private HandItemWrapper(Class<T> clazz, Player player, boolean throwEx) throws BKgCommandException {
		item = player.getItemInHand();
		if (item.getType() != Material.AIR) {
			ItemMeta m = item.getItemMeta();
			if (clazz.isInstance(m)) {
				meta = (T) m;
				return;
			}
		}
		meta = null;
		if (throwEx) {
			throw new BKgCommandException(Lang._(NBTEditor.class, "meta-error.format", Lang._(NBTEditor.class, "meta-error." + this.getClass().getSimpleName().toLowerCase())));
		}
	}
	
	public void save() {
		item.setItemMeta(meta);
	}
	
}
