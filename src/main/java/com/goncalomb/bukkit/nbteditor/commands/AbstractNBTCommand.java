package com.goncalomb.bukkit.nbteditor.commands;

import java.text.MessageFormat;
import java.util.ArrayList;
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
import com.goncalomb.bukkit.nbteditor.nbt.BaseNBT;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTVariableContainer;

public abstract class AbstractNBTCommand<T extends BaseNBT> extends MyCommand {

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

	public boolean info_Command(CommandSender sender, String[] args) throws MyCommandException {
		T wrapper = getWrapper((Player) sender);
		for (NBTVariableContainer container : wrapper.getAllVariables()) {
			sender.sendMessage("§a" + container.getName());
			for (String name : container.getVariableNames()) {
				String value = container.getVariable(name).get();
				if (value == null) {
					value = "§onull";
				}
				sender.sendMessage("  " + name + ": §b" + value);
			}
		}
		return true;
	}

	public boolean var_Command(CommandSender sender, String[] args) throws MyCommandException {
		T wrapper = getWrapper((Player) sender);
		NBTVariable variable = wrapper.getVariable(args[0]);
		if (variable != null) {
			if(args.length >= 2) {
				String value = StringUtils.join(args, " ", 1, args.length);
				if (variable.set(value, (Player) sender)) {
					wrapper.save();
					sender.sendMessage("§aVariable updated.");
					return true;
				} else {
					sender.sendMessage(MessageFormat.format("§cInvalid format for variable {0}!", args[0]));
				}
			}
			sender.sendMessage(ChatColor.YELLOW + variable.getFormat());
		} else {
			sender.sendMessage(MessageFormat.format("§cVariable {0} not found!", args[0]));
		}
		return true;
	}

	public List<String> var_TabComplete(CommandSender sender, String[] args) {
		T wrapper = getWrapperSilent((Player) sender);
		if (wrapper != null) {
			if (args.length == 1) {
				return getVariableNames(getWrapperSilent((Player) sender), args[0]);
			} else if (args.length == 2) {
				NBTVariable variable = wrapper.getVariable(args[0]);
				if (variable != null) {
					List<String> possibleValues = variable.getPossibleValues();
					if (possibleValues != null) {
						return Utils.getElementsWithPrefix(possibleValues, args[1]);
					}
				}
			}
		}
		return null;
	}

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

	public List<String> clearvar_TabComplete(CommandSender sender, String[] args) {
		return getVariableNames(getWrapperSilent((Player) sender), args[0]);
	}

}
