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
	
	@Override
	public void onEnable() {
		try {
			NBTBaseWrapper.prepareReflection();
			WorldUtils.prepareReflection();
		} catch (Throwable e) {
			getLogger().log(Level.SEVERE, "Error preparing reflection objects. This means that this version of NBTEditor is not compatible with this version of Bukkit.", e);
			getLogger().warning("NBTEditor version not compatible with this version of Bukkit. Please install the apropriated version.");
			return;
		}
		BKgLib.bind(this);
		
		BKgLib.registerCommand(new CommandBOS(), this);
		BKgLib.registerCommand(new CommandNBTSpawner(), this);
		BKgLib.registerCommand(new CommandNBTItem(), this);
		BKgLib.registerCommand(new CommandNBTEnchant(), this);
		BKgLib.registerCommand(new CommandNBTBook(), this);
		BKgLib.registerCommand(new CommandNBTPotion(), this);
		BKgLib.registerCommand(new CommandNBTArmor(), this);
		BKgLib.registerCommand(new CommandNBTHead(), this);
		BKgLib.registerCommand(new CommandNBTTile(), this);
		
		CustomItemManager itemManager = CustomItemManager.getInstance(this);
		BookOfSouls.initialize(this, itemManager);
		itemManager.registerNew(new MobInspectorTool(), this);
		itemManager.registerNew(new MobRemoverTool(), this);
		
		getLogger().info("NBTEditor has been enabled.");
	}
	
	@Override
	public void onDisable() {
		BKgLib.unbind(this);
	}
	
}
