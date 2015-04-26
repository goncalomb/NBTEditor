/*
 * Copyright (C) 2013, 2014, 2015 - Gonçalo Baltazar <http://goncalomb.com>
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

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import com.goncalomb.bukkit.bkglib.BKgLib;
import com.goncalomb.bukkit.bkglib.reflect.BukkitReflect;
import com.goncalomb.bukkit.bkglib.reflect.NBTBase;
import com.goncalomb.bukkit.customitemsapi.api.CustomItemManager;
import com.goncalomb.bukkit.customitemsapi.commands.CommandCustomItems;
import com.goncalomb.bukkit.customitemsapi.items.AntiMatterBomb;
import com.goncalomb.bukkit.customitemsapi.items.BatBomb;
import com.goncalomb.bukkit.customitemsapi.items.EnderBow;
import com.goncalomb.bukkit.customitemsapi.items.EscapePlan;
import com.goncalomb.bukkit.customitemsapi.items.FireBomb;
import com.goncalomb.bukkit.customitemsapi.items.GravitationalAxe;
import com.goncalomb.bukkit.customitemsapi.items.KingsCrown;
import com.goncalomb.bukkit.customitemsapi.items.LightningRod;
import com.goncalomb.bukkit.customitemsapi.items.MoonStick;
import com.goncalomb.bukkit.customitemsapi.items.RepulsionBomb;
import com.goncalomb.bukkit.customitemsapi.items.SimpleMine;
import com.goncalomb.bukkit.customitemsapi.items.SunStick;
import com.goncalomb.bukkit.customitemsapi.items.TorchBow;
import com.goncalomb.bukkit.customitemsapi.items.TreeVaporizer;
import com.goncalomb.bukkit.customitemsapi.items.WitherBow;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.commands.CommandBOS;
import com.goncalomb.bukkit.nbteditor.commands.CommandItemStorage;
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
	
	private static NBTEditor _instance;
	
	private static Field _Item_REGISTRY;
	private static Method _Item_getById; // Get Item instance from id.
	private static Method _RegistryMaterials_c; // Get item name from Item instance.
	
	public static String getMaterialName(Material material) {
		try {
			Object item = _Item_getById.invoke(null, material.getId());
			if (item != null) {
				Object REGISTRY = _Item_REGISTRY.get(null);
				return _RegistryMaterials_c.invoke(REGISTRY, item).toString();
			}
		} catch (Exception e) { }
		return "minecraft:air";
	}
	
	@Override
	public void onEnable() {
		try {
			NBTBase.prepareReflection();
			Class<?> minecraftItemClass = BukkitReflect.getMinecraftClass("Item");
			_Item_REGISTRY = minecraftItemClass.getField("REGISTRY");
			try {
				// craftbukkit-1.7.10
				_Item_getById = minecraftItemClass.getMethod("getById", int.class);
			} catch (NoSuchMethodException e) {
				// craftbukkit-1.7.9
				_Item_getById = minecraftItemClass.getMethod("d", int.class);
			}
			_RegistryMaterials_c = _Item_REGISTRY.getType().getMethod("c", Object.class);
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
		
		ItemStorage.setDataFolder(new File(getDataFolder(), "ItemStorage"));
		BKgLib.registerCommand(new CommandItemStorage(), this);
		
		BookOfSouls.initialize(this);
		CustomItemManager.register(new EntityInspectorTool(), this);
		CustomItemManager.register(new EntityRemoverTool(), this);
		CustomItemManager.register(new SuperLeadTool(), this);
		
		this.saveDefaultConfig();
		
		if (getConfig().getBoolean("enable-custom-items", false)) {
			CustomItemManager.register(new BatBomb(), this);
			CustomItemManager.register(new FireBomb(), this);
			CustomItemManager.register(new RepulsionBomb(), this);
			CustomItemManager.register(new LightningRod(), this);
			CustomItemManager.register(new EnderBow(), this);
			CustomItemManager.register(new WitherBow(), this);
			CustomItemManager.register(new SunStick(), this);
			CustomItemManager.register(new MoonStick(), this);
			CustomItemManager.register(new EscapePlan(), this);
			CustomItemManager.register(new KingsCrown(), this);
			CustomItemManager.register(new SimpleMine(), this);
			CustomItemManager.register(new TorchBow(), this);
			CustomItemManager.register(new AntiMatterBomb(), this);
			CustomItemManager.register(new GravitationalAxe(), this);
			CustomItemManager.register(new TreeVaporizer(), this);
			
			BKgLib.registerCommand(new CommandCustomItems(), this);
			getLogger().info("CustomItems enabled.");
		}
		
		_instance = this;
		getLogger().info("NBTEditor has been enabled.");
	}
	
	@Override
	public void onDisable() {
		BKgLib.unbind(this);
		_instance = null;
	}
	
	public static NBTEditor getInstance() {
		return _instance;
	}
	
}
