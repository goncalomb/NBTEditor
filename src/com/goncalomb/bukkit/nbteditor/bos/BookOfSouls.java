package com.goncalomb.bukkit.nbteditor.bos;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.customitems.api.CustomItem;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.customitems.api.PlayerDetails;
import com.goncalomb.bukkit.lang.Lang;
import com.goncalomb.bukkit.nbteditor.nbt.DroppedItemNBT;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MobNBT;
import com.goncalomb.bukkit.nbteditor.nbt.VillagerNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariableContainer;

public class BookOfSouls {
	
	private static final String _title = ChatColor.AQUA + "Book of Souls";
	private static final String _author = ChatColor.GOLD + "The Creator";
	private static final String _metaPageTitle = "" + ChatColor.DARK_PURPLE + ChatColor.BOLD + "Soul Data v0.1" + ChatColor.BLACK + "\n";
	private static final String _metaPagePre = ChatColor.MAGIC.toString();
	
	private static Plugin _plugin = null;
	private static final String[] _mobEquipSlotName = new String[] { "Head Equipment", "Chest Equipment", "Legs Equipment", "Feet Equipment", "Hand Item" };

	private EntityNBT _entityNbt;
	private ItemStack _book;
	
	public static void initialize(Plugin plugin, CustomItemManager itemManager) {
		if (_plugin != null) return;
		_plugin = plugin;
		
		itemManager.registerNew(new CustomItem("bos", _title, new MaterialData(Material.WRITTEN_BOOK)) {
			
			@Override
			public void onLeftClick(PlayerInteractEvent event, PlayerDetails details) {
				Player player = event.getPlayer();
				if (!player.hasPermission("nbteditor.bookofsouls")) {
					player.sendMessage(Lang._("general.no-perm"));
					return;
				}
				
				Location location = null;
				if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
					location = event.getClickedBlock().getLocation().add(UtilsMc.faceToDelta(event.getBlockFace(), 0.5));
				} else {
					Block block = UtilsMc.getTargetBlock(player);
					if (block.getType() != Material.AIR) {
						location = UtilsMc.airLocation(block.getLocation());
					}
				}
				
				if (location != null) {
					BookOfSouls bos = new BookOfSouls(event.getItem());
					if (bos.isValid()) {
						event.setCancelled(true);
						bos.getEntityNBT().spawn(location);
					} else {
						player.sendMessage(Lang._("nbt.bos.corrupted"));
					}
				} else {
					player.sendMessage(Lang._("general.no-sight"));
				}
				return;
			};
			
			@Override
			public ItemStack getItem() {
				return null;
			}
			
		}, plugin);
	}
	
	public BookOfSouls(EntityType entityType) {
		_entityNbt = EntityNBT.fromEntityType(entityType);
	}
	
	public BookOfSouls(ItemStack book) {
		_book = book;
		if (isValidBook(book)) {
			BookMeta meta = (BookMeta)_book.getItemMeta();
			int pageCount = meta.getPageCount();
			if (pageCount >= 1) {
				StringBuilder serializedData = new StringBuilder();
				for (int i = 1; i <= pageCount; ++i) {
					String page = meta.getPage(i);
					if (page.startsWith(_metaPageTitle)) {
						serializedData.append(page.substring(_metaPageTitle.length() + _metaPagePre.length()));
						for (++i; i <= pageCount; ++i) {
							serializedData.append(meta.getPage(i).substring(_metaPagePre.length()));
						}
						try {
							_entityNbt = EntityNBT.unserialize(serializedData.toString());
						} catch (Error e) { }
					}
				}
			}
		}
	}
	
	public static boolean isValidBook(ItemStack book) {
		if (book != null && book.getType() == Material.WRITTEN_BOOK) {
			ItemMeta meta = book.getItemMeta();
			if (meta != null && ((BookMeta) meta).getTitle() != null && ((BookMeta) meta).getTitle().startsWith(_title)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isValid() {
		return (_entityNbt != null);
	}
	
	public boolean openDroppedItemInventory(Player player) {
		if (_entityNbt instanceof DroppedItemNBT) {
			(new InventoryForDroppedItems(this, player)).openInventory(player, _plugin);
			return true;
		}
		return false;
	}
	
	public boolean openMobInventory(Player player) {
		if (_entityNbt instanceof MobNBT) {
			(new InventoryForMobs(this, player)).openInventory(player, _plugin);
			return true;
		}
		return false;
	}
	
	public boolean openOffersInventory(Player player) {
		if (_entityNbt instanceof VillagerNBT) {
			(new InventoryForVillagers(this, player)).openInventory(player, _plugin);
			return true;
		}
		return false;
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
			saveBook();
		}
		return _book;
	}
	
	public void saveBook() {
		BookMeta meta = (BookMeta)_book.getItemMeta();
		String entityName = _entityNbt.getEntityType().getName();
		
		meta.setDisplayName(_title + ChatColor.RESET + " (" + ChatColor.RED + entityName + ChatColor.RESET + ")");
		meta.setTitle(_title);
		meta.setAuthor(_author);
		
		meta.setPages((List<String>)null);
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("This book contains the soul of a " + ChatColor.RED + ChatColor.BOLD + entityName + "\n\n");
		int var_i = 7;
		for (NBTVariableContainer vc : _entityNbt.getAllVariables()) {
			if (var_i - 1 == 0) {
				meta.addPage(sb.toString());
				sb = new StringBuilder();
				var_i = 11;
			}
			sb.append("" + ChatColor.DARK_PURPLE + ChatColor.ITALIC + vc.getName() + ":\n");
			for (NBTVariable var : vc) {
				if (--var_i == 0) {
					meta.addPage(sb.toString());
					sb = new StringBuilder();
					var_i = 10;
				}
				String value = var.getValue();
				sb.append("  " + ChatColor.DARK_BLUE + var.getName() + ": " + ChatColor.BLACK + (value != null ? value : ChatColor.ITALIC + "-") + "\n");
			}
		}
		meta.addPage(sb.toString());
		
		

		if (_entityNbt instanceof MobNBT) {
			MobNBT mob = (MobNBT) _entityNbt;
			
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
				sb.append("" + ChatColor.BLACK + ChatColor.ITALIC + "not defined,\ndefault 0.85");
			}
			meta.addPage(sb.toString());
			
		}
		
		
		String serializedData = _entityNbt.serialize();
		int max;
		int true_max = 255 - _metaPagePre.length();
		for (int i = 0, l = serializedData.length(); i < l; i += max) {
			max = (i == 0 ? true_max - _metaPageTitle.length() : true_max);
			meta.addPage((i == 0 ? _metaPageTitle : "") + _metaPagePre + serializedData.substring(i, (i + max > l ? l : i + max)));
		}
		
		_book.setItemMeta(meta);
	}
	
}