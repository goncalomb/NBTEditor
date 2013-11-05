package com.goncalomb.bukkit.nbteditor;

import java.util.logging.Level;

import com.goncalomb.bukkit.bkglib.betterplugin.BetterPlugin;
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

public final class NBTEditor extends BetterPlugin {
	
	@Override
	public void onBetterEnable() {
		try {
			NBTBaseWrapper.prepareReflection();
			WorldUtils.prepareReflection();
		} catch (Throwable e) {
			getLogger().log(Level.SEVERE, "Error preparing reflection objects. This means that this version of NBTEditor is not compatible with this version of Bukkit.", e);
			getLogger().warning("NBTEditor version not compatible with this version of Bukkit. Please install the apropriated version.");
			return;
		}
		
		registerCommand(new CommandBOS());
		registerCommand(new CommandNBTSpawner());
		registerCommand(new CommandNBTItem());
		registerCommand(new CommandNBTEnchant());
		registerCommand(new CommandNBTBook());
		registerCommand(new CommandNBTPotion());
		registerCommand(new CommandNBTArmor());
		registerCommand(new CommandNBTHead());
		registerCommand(new CommandNBTTile());
		
		CustomItemManager itemManager = CustomItemManager.getInstance(this);
		BookOfSouls.initialize(this, itemManager);
		itemManager.registerNew(new MobInspectorTool(), this);
		itemManager.registerNew(new MobRemoverTool(), this);
		
		getLogger().info("NBTEditor has been enabled.");
	}
	
}
