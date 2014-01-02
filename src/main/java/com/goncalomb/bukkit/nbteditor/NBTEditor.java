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

package com.goncalomb.bukkit.nbteditor;

import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.bkglib.reflect.NBTBase;
import com.goncalomb.bukkit.bkglib.reflect.WorldUtils;
import com.goncalomb.bukkit.customitemsapi.api.CustomItemManager;
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
import com.goncalomb.bukkit.nbteditor.tools.EntityInspectorTool;
import com.goncalomb.bukkit.nbteditor.tools.EntityRemoverTool;
import com.goncalomb.bukkit.nbteditor.tools.SuperLeadTool;

public final class NBTEditor extends JavaPlugin {
	
	@Override
	public void onEnable() {
		try {
			NBTBase.prepareReflection();
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
		
		BookOfSouls.initialize(this);
		CustomItemManager.register(new EntityInspectorTool(), this);
		CustomItemManager.register(new EntityRemoverTool(), this);
		CustomItemManager.register(new SuperLeadTool(), this);
		
		getLogger().info("NBTEditor has been enabled.");
	}
	
	@Override
	public void onDisable() {
		BKgLib.unbind(this);
	}
	
}
