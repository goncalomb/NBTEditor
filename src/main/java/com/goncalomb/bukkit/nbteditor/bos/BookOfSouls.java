/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.bos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.utils.BookSerialize;
import com.goncalomb.bukkit.nbteditor.nbt.DroppedItemNBT;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.EquippableNBT;
import com.goncalomb.bukkit.nbteditor.nbt.FallingBlockNBT;
import com.goncalomb.bukkit.nbteditor.nbt.FireworkNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MinecartContainerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MinecartSpawnerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MobNBT;
import com.goncalomb.bukkit.nbteditor.nbt.ThrownPotionNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.Attribute;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.Modifier;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;

import net.iharder.Base64;

public class BookOfSouls {
	
	private static final String _author = ChatColor.GOLD + "The Creator";
	private static final String _dataTitle = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Soul Data v0.2" + ChatColor.BLACK + "\n";
	private static final String _dataTitleOLD = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Soul Data v0.1" + ChatColor.BLACK + "\n";
	private static CustomItem _bosEmptyCustomItem;
	private static CustomItem _bosCustomItem;
	
	private static Plugin _plugin = null;
	private static final String[] _mobEquipSlotName = new String[] { "Head Equipment", "Chest Equipment", "Legs Equipment", "Feet Equipment", "Hand Item" };

	private ItemStack _book;
	private EntityNBT _entityNbt;
	
	public static void initialize(Plugin plugin) {
		if (_plugin != null) return;
		_plugin = plugin;
		
		_bosEmptyCustomItem = new BookOfSoulsEmptyCI();
		CustomItemManager.register(_bosEmptyCustomItem, plugin, "nbteditor");
		
		_bosCustomItem = new BookOfSoulsCI();
		CustomItemManager.register(_bosCustomItem, plugin, "nbteditor");
	}
	
	static EntityNBT bookToEntityNBT(ItemStack book) {
		if (isValidBook(book)) {
			try {
				String data = BookSerialize.loadData((BookMeta) book.getItemMeta(), _dataTitle);
				if (data == null) {
					// This is not a BoS v0.2, is BoS v0.1?
					data = BookSerialize.loadData((BookMeta) book.getItemMeta(), _dataTitleOLD);
					if (data != null) {
						// Yes, it is v0.1, do a dirty conversion.
						int i = data.indexOf(',');
						NBTTagCompound nbtData = NBTTagCompound.unserialize(Base64.decode(data.substring(i + 1)));
						nbtData.setString("id", data.substring(0, i));
						data = Base64.encodeBytes(nbtData.serialize(), Base64.GZIP);
					}
				}
				if (data != null) {
					if (data.startsWith("§k")) {
						// Dirty fix, for some reason 'data' can sometimes start with §k.
						// Remove it!!!
						data = data.substring(2);
					}
					return EntityNBT.unserialize(data);
				}
			} catch (Throwable e) {
				return null;
			}
		}
		return null;
	}
	
	public static BookOfSouls getFromBook(ItemStack book) {
		EntityNBT entityNbt = bookToEntityNBT(book);
		if (entityNbt != null) {
			return new BookOfSouls(book, entityNbt);
		}
		return null;
	}
	
	public static ItemStack getEmpty() {
		return _bosEmptyCustomItem.getItem();
	}
	
	public BookOfSouls(EntityNBT entityNBT) {
		this(null, entityNBT);
	}
	
	private BookOfSouls(ItemStack book, EntityNBT entityNBT) {
		_book = book;
		_entityNbt = entityNBT;
	}
	
	public static boolean isValidBook(ItemStack book) {
		if (book != null && book.getType() == Material.WRITTEN_BOOK) {
			ItemMeta meta = book.getItemMeta();
			String title = ((BookMeta) meta).getTitle();
			if (meta != null && title != null && title.equals(_bosCustomItem.getName())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean openInventory(Player player) {
		if (_entityNbt instanceof MobNBT) {
			(new InventoryForMobs(this, player)).openInventory(player, _plugin);
			return true;
		} else if (_entityNbt.getEntityType() == EntityType.ARMOR_STAND) {
			(new InventoryForEquippable<EquippableNBT>(this, player)).openInventory(player, _plugin);
			return true;
		} else if (_entityNbt instanceof DroppedItemNBT) {
			(new InventoryForDroppedItems(this, player)).openInventory(player, _plugin);
			return true;
		} else if (_entityNbt instanceof ThrownPotionNBT) {
			(new InventoryForThownPotion(this, player)).openInventory(player, _plugin);
			return true;
		} else if (_entityNbt instanceof FireworkNBT) {
			(new InventoryForFirework(this, player)).openInventory(player, _plugin);
			return true;
		}
		return false;
	}
	
	public boolean openOffersInventory(Player player, int page) {
		if (_entityNbt instanceof VillagerNBT) {
			(new InventoryForVillagers(this, page, player)).openInventory(player, _plugin);
			return true;
		}
		return false;
	}
	
	public void openRidingInventory(Player player) {
		(new InventoryForRiding(this, player)).openInventory(player, _plugin);
	}
	
	public boolean setMobDropChance(float head, float chest, float legs, float feet, float hand) {
		if (_entityNbt instanceof MobNBT) {
			((MobNBT) _entityNbt).setDropChances(hand, feet, legs, chest, head);
			return true;
		}
		return false;
	}
	
	public boolean clearMobDropChance() {
		if (_entityNbt instanceof MobNBT) {
			((MobNBT) _entityNbt).clearDropChances();
			return true;
		}
		return false;
	}
	
	public EntityNBT getEntityNBT() {
		return _entityNbt;
	}
	
	public ItemStack getBook() {
		if (_book == null) {
			_book = new ItemStack(Material.WRITTEN_BOOK);
			saveBook(true);
		}
		return _book;
	}
	
	public void saveBook() {
		saveBook(false);
	}
	
	public void saveBook(boolean resetName) {
		BookMeta meta = (BookMeta) _book.getItemMeta();
		String entityName = EntityTypeMap.getName(_entityNbt.getEntityType());
		
		if (resetName) {
			meta.setDisplayName(_bosCustomItem.getName() + ChatColor.RESET + " - " + ChatColor.RED + entityName);
			meta.setTitle(_bosCustomItem.getName());
			meta.setAuthor(_author);
		}
		
		meta.setPages(new ArrayList<String>());

		StringBuilder sb = new StringBuilder();
		sb.append("This book contains the soul of a " + ChatColor.RED + ChatColor.BOLD + entityName + "\n\n");
		
		int x = 7;
		if (_entityNbt instanceof MinecartSpawnerNBT) {
			sb.append(ChatColor.BLACK + "Left-click a existing spawner to copy the entities and variables from the spawner, left-click while sneaking to copy them back to the spawner.");
			meta.addPage(sb.toString());
			sb = new StringBuilder();
			x = 11;
		} else if (_entityNbt instanceof MinecartContainerNBT) {
			sb.append(ChatColor.BLACK + "Left-click a chest to copy the items from it, left-click while sneaking to copy them back to the chest.");
			meta.addPage(sb.toString());
			sb = new StringBuilder();
			x = 11;
		} else if (_entityNbt instanceof FallingBlockNBT) {
			sb.append(ChatColor.BLACK + "Left-click a block while sneaking to copy block data.\n\n");
			x = 5;
		}
		
		for (NBTVariableContainer vc : _entityNbt.getAllVariables()) {
			if (x == 1) {
				meta.addPage(sb.toString());
				sb = new StringBuilder();
				x = 11;
			}
			sb.append("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + vc.getName() + ":\n");
			for (NBTVariable var : vc) {
				if (--x == 0) {
					meta.addPage(sb.toString());
					sb = new StringBuilder();
					x = 10;
				}
				String value = var.getValue();
				sb.append("  " + ChatColor.DARK_BLUE + var.getName() + ": " + ChatColor.BLACK + (value != null ? value : ChatColor.ITALIC + "-") + "\n");
			}
		}
		meta.addPage(sb.toString());
		

		if (_entityNbt instanceof MobNBT) {
			MobNBT mob = (MobNBT) _entityNbt;
			
			sb = new StringBuilder();
			sb.append("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Attributes:\n");
			Collection<Attribute> attributes = mob.getAttributes().values();
			if (attributes.size() == 0) {
				sb.append("  " + ChatColor.BLACK + ChatColor.ITALIC +"none\n");
			} else {
				x = 11;
				for (Attribute attribute : attributes) {
					if (x <= 3) {
						meta.addPage(sb.toString());
						sb = new StringBuilder();
						x = 11;
					}
					sb.append("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + attribute.getType().getName() + ":\n");
					sb.append("  " + ChatColor.DARK_BLUE + "Base: " + ChatColor.BLACK + attribute.getBase() + "\n");
					sb.append("  " + ChatColor.DARK_BLUE + "Modifiers:\n");
					x -= 3;
					List<Modifier> modifiers = attribute.getModifiers();
					if (modifiers.size() == 0) {
						sb.append("    " + ChatColor.BLACK + ChatColor.ITALIC +"none\n");
					} else {
						for (Modifier modifier : modifiers) {
							if (x <= 3) {
								meta.addPage(sb.toString());
								sb = new StringBuilder();
								x = 11;
							}
							sb.append("    " + ChatColor.RED + modifier.getName() + ChatColor.DARK_GREEN + " Op: " + ChatColor.BLACK + modifier.getOperation() + "\n");
							sb.append("      " + ChatColor.DARK_GREEN + "Amount: " + ChatColor.BLACK + modifier.getAmount() + "\n");
							x -= 3;
						}
					}
					sb.append("\n");
					--x;
				}
			}
			meta.addPage(sb.toString());
			
			sb = new StringBuilder();
			sb.append("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Equipment:\n");
			ItemStack[] items = mob.getEquipment();
			for (int i = 0; i < 5; ++i) {
				sb.append(ChatColor.DARK_BLUE + _mobEquipSlotName[i] + ":\n");
				if (items[4 - i] != null) {
					sb.append("  " + ChatColor.BLACK + items[4 - i].getType().name() + ":" + items[4 - i].getDurability() + "(" + items[4 - i].getAmount() + ")" + "\n");
				} else {
					sb.append("  " + ChatColor.BLACK + ChatColor.ITALIC +"none\n");
				}
			}
			meta.addPage(sb.toString());
			
			sb = new StringBuilder();
			sb.append("" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Drop chance:\n");
			float[] chances = mob.getDropChances();
			if (chances != null) {
				for (int i = 0; i < 5; ++i) {
					sb.append(ChatColor.DARK_BLUE + _mobEquipSlotName[i] + ":\n");
					sb.append("  " + ChatColor.BLACK + chances[4 - i] + "\n");
				}
			} else {
				sb.append("" + ChatColor.BLACK + ChatColor.ITALIC + "not defined,\ndefault 0.085");
			}
			meta.addPage(sb.toString());
		}
		
		BookSerialize.saveToBook(meta, _entityNbt.serialize(), _dataTitle);
		meta.addPage("RandomId: " + Integer.toHexString((new Random()).nextInt()) + "\n\n\n"
				+ ChatColor.DARK_BLUE + ChatColor.BOLD + "      The END.");
		_book.setItemMeta(meta);
	}
	
}