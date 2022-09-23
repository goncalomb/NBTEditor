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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.command.MyCommandManager;
import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.namemaps.SpawnEggMap;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTUtils;
import com.goncalomb.bukkit.mylib.utils.Utils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.MobNBT;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.Attribute;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeContainer;
import com.goncalomb.bukkit.nbteditor.nbt.attributes.AttributeType;
import com.goncalomb.bukkit.nbteditor.nbt.variables.PassengersVariable;

public class CommandBOS extends AbstractNBTCommand<EntityNBT> {

	public CommandBOS() {
		super("bookofsouls", "bos");
	}

	static BookOfSouls getBos(Player player) throws MyCommandException {
		return getBos(player, false);
	}

	static BookOfSouls getBos(Player player, boolean nullIfMissing) throws MyCommandException {
		ItemStack item = player.getInventory().getItemInMainHand();
		if (BookOfSouls.isValidBook(item)) {
			BookOfSouls bos = BookOfSouls.getFromBook(item);
			if (bos != null) {
				return bos;
			}
			throw new MyCommandException("§cThat Book of Souls is corrupted!");
		} else if (!nullIfMissing) {
			throw new MyCommandException("§cYou must be holding a Book of Souls!");
		}
		return null;
	}

	@Override
	protected EntityNBT getWrapper(Player player) throws MyCommandException {
		BookOfSouls bos = getBos(player);
		bos.getEntityNBT()._bos = bos;
		return bos.getEntityNBT();
	}

	@Command(args = "get", type = CommandType.PLAYER_ONLY, maxargs = 1, usage = "<entity>")
	public boolean getCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length == 1) {
			EntityType entityType = EntityTypeMap.getByName(args[0]);
			if (entityType != null && EntityNBT.isValidType(entityType)) {
				PlayerInventory inv = CommandUtils.checkFullInventory((Player) sender);
				BookOfSouls bos = new BookOfSouls(EntityNBT.fromEntityType(entityType));
				inv.addItem(bos.getBook());
				sender.sendMessage("§aEnjoy your Book of Souls.");
				return true;
			}
			sender.sendMessage("§cInvalid entity!");
		}
		sender.sendMessage("§7Entities: " + StringUtils.join(EntityNBT.getValidTypeNames(), ", "));
		return false;
	}

	@TabComplete(args = "get")
	public List<String> get_tab(CommandSender sender, String[] args) {
		return (args.length == 1 ? Utils.getElementsWithPrefix(EntityNBT.getValidTypeNames(), args[0], "minecraft:", true) : null);
	}

	@Command(args = "getempty", type = CommandType.PLAYER_ONLY)
	public boolean getemptyCommand(CommandSender sender, String[] args) throws MyCommandException {
		CommandUtils.checkFullInventory((Player) sender).addItem(BookOfSouls.getEmpty());
		sender.sendMessage("§aEnjoy your Book of Souls.");
		return true;
	}

	@Command(args = "riding", type = CommandType.PLAYER_ONLY)
	public boolean ridingCommand(CommandSender sender, String[] args) throws MyCommandException {
		sender.sendMessage("§eCOMMAND REMOVED in NBTEditor 3.0.");
		sender.sendMessage("§7From now on, most NBT changes are done using variables!");
		sender.sendMessage("§7To edit entity passengers, use");
		sender.sendMessage("§7  '§b/bos var Passengers§7'");
		return true;
	}

	@Command(args = "attr add", type = CommandType.PLAYER_ONLY, maxargs = 2, usage = "<attribute> <base>")
	public boolean attr_addCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length == 2) {
			BookOfSouls bos = getBos((Player) sender);
			EntityNBT entityNbt = bos.getEntityNBT();
			if (!(entityNbt instanceof MobNBT)) {
				sender.sendMessage("§cThat must be a Mob entity!");
				return true;
			} else {
				AttributeType attributeType = AttributeType.getByName(args[0]);
				if (attributeType == null) {
					sender.sendMessage("§cInvalid attribute!");
				} else {
					double base;
					try {
						base = Double.parseDouble(args[1]);
					} catch (NumberFormatException e) {
						sender.sendMessage("§cBase must be a number!");
						return true;
					}
					AttributeContainer attributes = ((MobNBT) entityNbt).getAttributes();
					attributes.setAttribute(new Attribute(attributeType, base));
					((MobNBT) entityNbt).setAttributes(attributes);
					bos.saveBook();
					sender.sendMessage("§aEntity attribute added.");
					return true;
				}
			}
		}
		sender.sendMessage("§7Attributes: " + StringUtils.join(AttributeType.values(), ", "));
		return false;
	}

	@TabComplete(args = "attr add")
	public List<String> tab_attr_add(CommandSender sender, String[] args) {
		return (args.length == 1 ? Utils.getElementsWithPrefixGeneric(Arrays.asList(AttributeType.values()), args[0], true) : null);
	}

	@Command(args = "attr del", type = CommandType.PLAYER_ONLY, maxargs = 1, usage = "<attribute>")
	public boolean attr_delCommand(CommandSender sender, String[] args) throws MyCommandException {
		if (args.length == 1) {
			BookOfSouls bos = getBos((Player) sender);
			EntityNBT entityNbt = bos.getEntityNBT();
			if (!(entityNbt instanceof MobNBT)) {
				sender.sendMessage("§cThat must be a Mob entity!");
				return true;
			} else {
				AttributeType attributeType = AttributeType.getByName(args[0]);
				if (attributeType == null) {
					sender.sendMessage("§cInvalid attribute!");
				} else {
					AttributeContainer attributes = ((MobNBT) entityNbt).getAttributes();
					if (attributes.removeAttribute(attributeType) != null) {
						((MobNBT) entityNbt).setAttributes(attributes);
						bos.saveBook();
						sender.sendMessage("§aEntity attribute removed.");
						return true;
					}
					sender.sendMessage(MessageFormat.format("§cThis entity does no have the attribute {0}!", attributeType.toString()));
					return true;
				}
			}
		}
		sender.sendMessage("§7Attributes: " + StringUtils.join(AttributeType.values(), ", "));
		return false;
	}

	@TabComplete(args = "attr del")
	public List<String> tab_del_add(CommandSender sender, String[] args) {
		if (args.length == 1) {
			ItemStack item = ((Player) sender).getInventory().getItemInMainHand();
			if (BookOfSouls.isValidBook(item)) {
				BookOfSouls bos = BookOfSouls.getFromBook(item);
				if (bos != null) {
					EntityNBT entityNbt = bos.getEntityNBT();
					if (entityNbt instanceof MobNBT) {
						return Utils.getElementsWithPrefixGeneric(((MobNBT) entityNbt).getAttributes().types(), args[0], true);
					}
				}
			}
		}
		return null;
	}

	@Command(args = "attr delall", type = CommandType.PLAYER_ONLY)
	public boolean attr_delallCommand(CommandSender sender, String[] args) throws MyCommandException {
		BookOfSouls bos = getBos((Player) sender);
		EntityNBT entityNbt = bos.getEntityNBT();
		if (entityNbt instanceof MobNBT) {
			((MobNBT) entityNbt).setAttributes(null);
			bos.saveBook();
			sender.sendMessage("§aEntity attributes cleared.");
			return true;
		}
		sender.sendMessage("§cThat must be a Mob entity!");
		return true;
	}

	@Command(args = "tocommand", type = CommandType.PLAYER_ONLY)
	public boolean tocommandCommand(CommandSender sender, String[] args) throws MyCommandException {
		BookOfSouls bos = getBos((Player) sender);
		Block block = UtilsMc.getTargetBlock((Player) sender, 5);
		BlockState state = block.getState();
		if (state instanceof CommandBlock) {
			EntityNBT entityNbt = bos.getEntityNBT();
			String command = "summon";
			if (!MyCommandManager.isVanillaCommand(command)) {
				sender.sendMessage(MessageFormat.format("§7Non-vanilla /{0} command detected, using /minecraft:{0}.", command));
				command = "minecraft:" + command;
			}
			command = "/" + command + " " + EntityTypeMap.getName(entityNbt.getEntityType()) + " ~ ~1 ~ " + entityNbt.getMetadataString();
			// We spare 50 characters of space so people can change the position.
			if (command.length() > 32767 - 50) {
				sender.sendMessage("§cEntity too complex!");
				return true;
			}
			((CommandBlock) state).setCommand(command);
			state.update();
			sender.sendMessage("§aCommand set.");
			return true;
		}
		sender.sendMessage("§cNo Command Block in sight!");
		return true;
	}

	@Command(args = "toegg", type = CommandType.PLAYER_ONLY)
	public boolean toeggCommand(CommandSender sender, String[] args) throws MyCommandException {
		BookOfSouls bos = getBos((Player) sender);
		sender.sendMessage("§eSome entities may not spawn from eggs.");
		EntityNBT entityNbt = bos.getEntityNBT();
		NBTTagCompound entityData = entityNbt.getData();
		entityData.remove("Pos");
		if (entityData.hasKey("Passengers")) {
			entityData.remove("Passengers");
			sender.sendMessage("§eEntities spawned from eggs don't passengers.");
		}
		if (!entityData.hasKey("CustomName")) {
			entityData.setString("CustomName", ""); // prevent entity from acquiring the egg's name
		}
		Material eggType = SpawnEggMap.getEggForEntity(entityData.getString("id"));
		if (eggType == null) {
			eggType = Material.TURTLE_SPAWN_EGG;
		}
		ItemStack item = NBTUtils.itemStackToCraftItemStack(UtilsMc.newSingleItemStack(eggType, "§rSpawn Egg - " + EntityTypeMap.getName(entityNbt.getEntityType()), "Created from a BoS."));
		NBTTagCompound itemData = NBTUtils.getItemStackTag(item);
		itemData.setCompound("EntityTag", entityData);
		NBTUtils.setItemStackTag(item, itemData);
		CommandUtils.giveItem((Player) sender, item);
		sender.sendMessage("§aEgg created.");
		return true;
	}

	private void refreshEntityPassengers(EntityNBT entity) {
		PassengersVariable variable = (PassengersVariable) entity.getVariable("Passengers");
		EntityNBT[] passengers = variable.getPassengers();
		for (EntityNBT passager : passengers) {
			refreshEntityPassengers(passager);
		}
		variable.setPassengers(passengers);
	}

	@Command(args = "refresh", type = CommandType.PLAYER_ONLY)
	public boolean refreshCommand(CommandSender sender, String[] args) throws MyCommandException {
		// loading and saving the BoS refreshes the base entity
		BookOfSouls bos = getBos((Player) sender);
		// but we also refresh the passengers (depth first)
		refreshEntityPassengers(bos.getEntityNBT());
		bos.saveBook(true);
		sender.sendMessage("§aBook of Souls refreshed.");
		return true;
	}

}