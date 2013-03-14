package com.goncalomb.bukkit.nbteditor.commands;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.FireworkNBT;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerEntityNBT;
import com.goncalomb.bukkit.nbteditor.nbt.SpawnerNBTWrapper;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTVariable;
import com.goncalomb.bukkit.Utils;
import com.goncalomb.bukkit.UtilsMc;
import com.goncalomb.bukkit.betterplugin.BetterCommand;
import com.goncalomb.bukkit.betterplugin.BetterCommandException;
import com.goncalomb.bukkit.betterplugin.BetterSubCommandType;
import com.goncalomb.bukkit.lang.Lang;

public class CommandNBTSpawner extends BetterCommand {
	
	private SpawnerNBTWrapper _clipboard;
	
	public CommandNBTSpawner() {
		super("nbtspawner", "nbteditor.spawner");
		setAlises("nbts");
		setDescription(Lang._("nbt.cmds.nbts.description"));
	}
	
	private static SpawnerNBTWrapper getSpawner(Player player) throws BetterCommandException {
		Block block = UtilsMc.getTargetBlock(player);
		if (block.getType() != Material.MOB_SPAWNER) {
			throw new BetterCommandException(Lang._("nbt.cmds.nbts.no-sight"));
		}
		return new SpawnerNBTWrapper(block);
	}
	
	private static int parseWeight(String[] args, int index) throws BetterCommandException {
		if (args.length > index) {
			int weight = Utils.parseInt(args[index], -1);
			if (weight < 1) {
				throw new BetterCommandException(Lang._("nbt.cmds.nbts.invalid-weight"));
			}
			return weight;
		}
		return 1;
	}
	
	private static int parseIndex(String str, List<SpawnerEntityNBT> entities) throws BetterCommandException {
		int index = Utils.parseInt(str, -1);
		if (index < 1) {
			throw new BetterCommandException(Lang._("bos.invalid-index"));
		} else if (index > entities.size()) {
			throw new BetterCommandException(Lang._format("nbt.cmds.nbts.entity-nop", index));
		}
		return index;
	}
	
	@SubCommand(args = "info", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean infoCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		Location loc = spawner.getLocation();
		sender.sendMessage(ChatColor.GREEN + "Spawner Information (" + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ")");
		sender.sendMessage("Current Entity: " + ChatColor.AQUA + spawner.getCurrentEntity().getName());
		for (SpawnerEntityNBT spawnerEntityNbt : spawner.getEntities()) {
			sender.sendMessage("   " + ChatColor.AQUA + spawnerEntityNbt.getEntityType().getName() + ", weight: " + spawnerEntityNbt.getWeight());
		}
		sender.sendMessage(ChatColor.GREEN + "Variables:");
		for (NBTVariable variable : spawner.getVariables()) {
			sender.sendMessage("   " + variable.getName() + ": " + ChatColor.AQUA + variable.getValue());
		}
		return true;
	}
	
	@SubCommand(args = "var", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 2, usage = "<variable> <value>")
	public boolean varCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		if(args.length > 0) {
			NBTVariable variable = spawner.getVariable(args[0]);
			if (variable != null) {
				if(args.length == 2) {
					if (variable.setValue(args[1])) {
						spawner.save();
						sender.sendMessage(Lang._("nbt.variable.updated"));
						return true;
					} else {
						sender.sendMessage(Lang._format("nbt.variable.invalid-format", args[0]));
					}
				}
				sender.sendMessage(ChatColor.YELLOW + variable.getFormat());
				return true;
			} else if(args.length <= 3) {
				sender.sendMessage(Lang._format("nbt.cmds.nbts.no-variable", args[0]));
			}
		}
		sender.sendMessage(Lang._("nbt.variables-sufix") + StringUtils.join(spawner.getVariables().getVarNames(), ", "));
		return false;
	}
	
	@SubCommand(args = "add", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 2, usage = "<entity> [weight]")
	public boolean addCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (args.length >= 1) {
			SpawnerNBTWrapper spawner = getSpawner((Player) sender);
			if (SpawnerEntityNBT.isValidCreature(args[0])) {
				int weight = parseWeight(args, 1);
				spawner.addEntity(new SpawnerEntityNBT(args[0], weight));
				spawner.save();
				sender.sendMessage(Lang._("nbt.cmds.nbts.entity-added"));
				return true;
			}
			sender.sendMessage(Lang._("nbt.invalid-entity"));
		}
		sender.sendMessage(Lang._("nbt.entities-sufix") + UtilsMc.entityTypeArrayToString(EntityNBT.getValidEntityTypes()));
		return false;
	}
	
	@SubCommand(args = "additem", type = BetterSubCommandType.PLAYER_ONLY, maxargs = 1, usage = "[weight]")
	public boolean addfireworkCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		ItemStack item = ((Player) sender).getItemInHand();
		if (item.getType() == Material.MONSTER_EGG) {
			int weight = parseWeight(args, 0);
			EntityType entityType = EntityType.fromId(item.getDurability());
			if (EntityNBT.isValidType(entityType)) {
				spawner.addEntity(new SpawnerEntityNBT(EntityNBT.fromEntityType(entityType), weight));
				spawner.save();
				sender.sendMessage(Lang._("nbt.cmds.nbts.entity-added"));
			} else {
				sender.sendMessage(Lang._("nbt.cmds.nbts.invalid-egg"));
			}
		} else if (item.getType() == Material.FIREWORK) {
			int weight = parseWeight(args, 0);
			spawner.addEntity(new SpawnerEntityNBT(new FireworkNBT(item), weight));
			spawner.save();
			sender.sendMessage(Lang._("nbt.cmds.nbts.firework-added"));
		} else {
			BookOfSouls bos = CommandBOS.getBos((Player) sender, true);
			if (bos != null) {
				int weight = parseWeight(args, 0);
				spawner.addEntity(new SpawnerEntityNBT(bos.getEntityNBT(), weight));
				spawner.save();
				sender.sendMessage(Lang._("nbt.cmds.nbts.bos-added"));
			} else {
				sender.sendMessage(Lang._("nbt.cmds.nbts.no-item"));
			}
		}
		return true;
	}
	
	@SubCommand(args = "del", type = BetterSubCommandType.PLAYER_ONLY, minargs = 1, usage = "<index>")
	public boolean delCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		int index = parseIndex(args[0], spawner.getEntities());
		spawner.removeEntity(index - 1);
		spawner.save();
		sender.sendMessage(Lang._("nbt.cmds.nbts.entity-removed"));
		return true;
	}
	
	@SubCommand(args = "setpos", type = BetterSubCommandType.PLAYER_ONLY, minargs = 3, maxargs = 4, usage = "<x> <y> <z> [index]")
	public boolean setposCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		double x, y, z;
		try {
			x = Double.parseDouble(args[0]);
			y = Double.parseDouble(args[1]);
			z = Double.parseDouble(args[2]);
		} catch (NumberFormatException e) {
			sender.sendMessage(Lang._("nbt.cmds.nbts.invalid-position"));
			return true;
		}
		if (args.length == 4) {
			List<SpawnerEntityNBT> entities = spawner.getEntities();
			int index = parseIndex(args[3], entities);
			EntityNBT entityNBT = entities.get(index - 1).getEntityNBT();
			entityNBT.setPos(x, y, z);
			sender.sendMessage(Lang._format("nbt.cmds.nbts.position-set", entityNBT.getEntityType().getName()));
		} else {
			for (SpawnerEntityNBT spawnerEntity : spawner.getEntities()) {
				spawnerEntity.getEntityNBT().setPos(x, y, z);
			}
			sender.sendMessage(Lang._("nbt.cmds.nbts.position-set-all"));
		}
		spawner.save();
		return true;
	}
	
	@SubCommand(args = "clear", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean clearentitiesCommand(CommandSender sender, String[] args) throws BetterCommandException {
		SpawnerNBTWrapper spawner = getSpawner((Player) sender);
		spawner.clearEntities();
		spawner.save();
		sender.sendMessage(Lang._("nbt.cmds.nbts.cleared"));
		return true;
	}
	
	@SubCommand(args = "copy", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean copyCommand(CommandSender sender, String[] args) throws BetterCommandException {
		_clipboard = getSpawner((Player) sender);
		sender.sendMessage(Lang._("nbt.cmds.nbts.copy"));
		return true;
	}
	
	@SubCommand(args = "paste", type = BetterSubCommandType.PLAYER_ONLY)
	public boolean pasteCommand(CommandSender sender, String[] args) throws BetterCommandException {
		if (_clipboard != null) {
			SpawnerNBTWrapper spawner = getSpawner((Player) sender);
			spawner.cloneFrom(_clipboard);
			spawner.save();
			sender.sendMessage(Lang._("nbt.cmds.nbts.paste"));
		} else {
			sender.sendMessage(Lang._("nbt.cmds.nbts.no-copy"));
		}
		return true;
	}
	
}