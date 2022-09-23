package com.goncalomb.bukkit.nbteditor.commands;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.goncalomb.bukkit.mylib.command.MyCommand;
import com.goncalomb.bukkit.mylib.command.MyCommandException;
import com.goncalomb.bukkit.mylib.utils.Utils;
import com.goncalomb.bukkit.mylib.utils.UtilsMc;
import com.goncalomb.bukkit.nbteditor.commands.inventories.InventoryForSpecialVariable;
import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ListVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.SpecialVariable;

public abstract class AbstractNBTCommand<T extends BaseNBT> extends MyCommand {

	private static List<String> _listOperations = Arrays.asList(new String[] { "add", "del" });

	private static List<String> getVariableNames(BaseNBT base, String prefix) {
		List<String> names = new ArrayList<String>();
		for (NBTVariableContainer container : base.getAllVariables()) {
			for (String name : container.getVariableNames()) {
				if (StringUtil.startsWithIgnoreCase(name, prefix)) {
					names.add(name);
				}
			}
		}
		Collections.sort(names, String.CASE_INSENSITIVE_ORDER);
		return names;
	}

	private static List<String> getVariablePossibleValues(NBTVariable variable, String prefix) {
		List<String> possibleValues = variable.getPossibleValues();
		if (possibleValues != null) {
			return Utils.getElementsWithPrefix(possibleValues, prefix, "minecraft:", false);
		}
		return null;
	}

	public AbstractNBTCommand(String name, String... aliases) {
		super(name, aliases);
	}

	protected abstract T getWrapper(Player player) throws MyCommandException;

	private T getWrapperSilent(Player player) {
		try {
			return getWrapper(player);
		} catch (Exception e) {
			return null;
		}
	}

	@Command(args = "info", type = CommandType.PLAYER_ONLY)
	public boolean info_Command(CommandSender sender, String[] args) throws MyCommandException {
		T wrapper = getWrapper((Player) sender);
		sender.sendMessage("§e" + wrapper.getId());
		for (NBTVariableContainer container : wrapper.getAllVariables()) {
			sender.sendMessage("§a" + container.getName());
			for (String name : container.getVariableNames()) {
				String value = container.getVariable(name).get();
				if (value == null) {
					value = "§onull";
				}
				sender.sendMessage("§b  " + name + ": §r" + value);
			}
		}
		return true;
	}

	@Command(args = "var", type = CommandType.PLAYER_ONLY, minargs = 1, maxargs = Integer.MAX_VALUE, usage = "<variable> ...")
	public boolean var_Command(CommandSender sender, String[] args) throws MyCommandException {
		T wrapper = getWrapper((Player) sender);
		NBTVariable variable = wrapper.getVariable(args[0]);
		if (variable != null) {
			if (args.length == 1 && variable instanceof SpecialVariable) {
				InventoryForSpecialVariable.openSpecialInventory((Player) sender, wrapper, (SpecialVariable) variable);
			} else if (args.length >= 2) {
				if (variable instanceof ListVariable) {
					if (args[1].equalsIgnoreCase("add") && args.length >= 3) {
						String value = UtilsMc.parseColors(StringUtils.join(args, " ", 2, args.length));
						if (((ListVariable) variable).add(value, (Player) sender)) {
							wrapper.save();
							sender.sendMessage("§aVariable updated (added to list).");
							return true;
						} else {
							sender.sendMessage(MessageFormat.format("§cInvalid format for variable {0}!", args[0]));
						}
					} else if (args[1].equalsIgnoreCase("del") && args.length == 3) {
						Integer index = Utils.parseInt(args[2], -1);
						if (index < 0) {
							sender.sendMessage("§cInvalid index. The index is an integer greater than or equal to 0.");
						} else if (((ListVariable) variable).remove(index)) {
							wrapper.save();
							sender.sendMessage("§aVariable updated (removed from list).");
							return true;
						} else {
							sender.sendMessage(MessageFormat.format("§cItem with index {0} doesn't exist!", index));
						}
					} else {
						sender.sendMessage("§cVariable is a list use 'add <value>' or 'del <index>'.");
					}
				} else {
					String value = UtilsMc.parseColors(StringUtils.join(args, " ", 1, args.length));
					if (variable.set(value, (Player) sender)) {
						wrapper.save();
						sender.sendMessage("§aVariable updated.");
						return true;
					} else {
						sender.sendMessage(MessageFormat.format("§cInvalid format for variable {0}!", args[0]));
					}
				}
			}
			sender.sendMessage(ChatColor.YELLOW + variable.getFormat());
		} else {
			sender.sendMessage(MessageFormat.format("§cVariable {0} not found!", args[0]));
		}
		return true;
	}

	@TabComplete(args = "var")
	public List<String> var_TabComplete(CommandSender sender, String[] args) {
		T wrapper = getWrapperSilent((Player) sender);
		if (wrapper != null) {
			if (args.length == 1) {
				return getVariableNames(getWrapperSilent((Player) sender), args[0]);
			} else if (args.length == 2 || args.length == 3) {
				NBTVariable variable = wrapper.getVariable(args[0]);
				if (variable != null) {
					if (variable instanceof ListVariable) {
						if (args.length == 2) {
							return Utils.getElementsWithPrefix(_listOperations, args[1]);
						} else if (args[1].equalsIgnoreCase("add")) {
							return getVariablePossibleValues(variable, args[2]);
						}
					} else {
						return getVariablePossibleValues(variable, args[1]);
					}
				}
			}
		}
		return null;
	}

	@Command(args = "clearvar", type = CommandType.PLAYER_ONLY, minargs = 1, usage = "<variable>")
	public boolean clearvar_Command(CommandSender sender, String[] args) throws MyCommandException {
		T wrapper = getWrapper((Player) sender);
		NBTVariable variable = wrapper.getVariable(args[0]);
		if (variable != null) {
			variable.clear();
			wrapper.save();
			sender.sendMessage("§aVariable cleared.");
		} else {
			sender.sendMessage(MessageFormat.format("§cVariable {0} not found!", args[0]));
		}
		return true;
	}

	@TabComplete(args = "clearvar")
	public List<String> clearvar_TabComplete(CommandSender sender, String[] args) {
		return getVariableNames(getWrapperSilent((Player) sender), args[0]);
	}

}
