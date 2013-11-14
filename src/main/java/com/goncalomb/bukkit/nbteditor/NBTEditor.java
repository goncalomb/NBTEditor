package com.goncalomb.bukkit.nbteditor;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.bkglib.reflect.NBTBaseWrapper;
import com.goncalomb.bukkit.bkglib.reflect.WorldUtils;
import com.goncalomb.bukkit.customitems.api.CustomItemManager;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.commands.CommandBOS;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTArmor;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTBook;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTEnchant;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTHead;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTItem;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTPotion;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTSpawner;
import com.goncalomb.bukkit.nbteditor.commands.CommandNBTTile;
import com.goncalomb.bukkit.nbteditor.tools.MobInspectorTool;
import com.goncalomb.bukkit.nbteditor.tools.MobRemoverTool;

public final class NBTEditor extends JavaPlugin {
	
	private static NBTEditor _instance;
	
	public static NBTEditor getInstance() {
		return _instance;
	}
	
	@Override
	public void onEnable() {
		_instance = this;
		try {
			NBTBaseWrapper.prepareReflection();
			WorldUtils.prepareReflection();
		} catch (Throwable e) {
			getLogger().log(Level.SEVERE, "Error preparing reflection objects. This means that this version of NBTEditor is not compatible with this version of Bukkit.", e);
			getLogger().warning("NBTEditor version not compatible with this version of Bukkit. Please install the apropriated version.");
			return;
		}
		BKgLib.bind(this);
		
		BKgLib.registerCommands(new CommandBOS(), this);
		BKgLib.setCommandAliases("bookofsouls", "bos");
		BKgLib.registerCommands(new CommandNBTSpawner(), this);
		BKgLib.setCommandAliases("nbtspawner", "nbts");
		BKgLib.registerCommands(new CommandNBTItem(), this);
		BKgLib.setCommandAliases("nbtitem", "nbti");
		BKgLib.registerCommands(new CommandNBTEnchant(), this);
		BKgLib.setCommandAliases("nbtenchant", "nbte");
		BKgLib.registerCommands(new CommandNBTBook(), this);
		BKgLib.setCommandAliases("nbtbook", "nbtb");
		BKgLib.registerCommands(new CommandNBTPotion(), this);
		BKgLib.setCommandAliases("nbtpotion", "nbtp");
		BKgLib.registerCommands(new CommandNBTArmor(), this);
		BKgLib.setCommandAliases("nbtarmor", "nbta");
		BKgLib.registerCommands(new CommandNBTHead(), this);
		BKgLib.setCommandAliases("nbthead", "nbth");
		BKgLib.registerCommands(new CommandNBTTile(), this);
		BKgLib.setCommandAliases("nbttile", "nbtt");
		
		CustomItemManager itemManager = CustomItemManager.getInstance(this);
		BookOfSouls.initialize(this, itemManager);
		itemManager.registerNew(new MobInspectorTool(), this);
		itemManager.registerNew(new MobRemoverTool(), this);
		
		getLogger().info("NBTEditor has been enabled.");
	}
	
	@Override
	public void onDisable() {
		BKgLib.unbind(this);
		_instance = null;
	}
	
}
