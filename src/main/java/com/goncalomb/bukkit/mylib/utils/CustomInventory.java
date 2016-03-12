/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib.utils;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

public abstract class CustomInventory {

	private static Listener _mainListener;
	private static Plugin _plugin;
	private static HashMap<HumanEntity, CustomInventory> _openedInvsByPlayer = new HashMap<HumanEntity, CustomInventory>();
	private static HashMap<Plugin, HashSet<CustomInventory>> _openedInvsByPlugin = new HashMap<Plugin, HashSet<CustomInventory>>();

	private Plugin _owner;
	protected final Inventory _inventory;

	private final static void bindListener(Plugin plugin) {
		if (_plugin == null) {
			if (_mainListener == null) {
				_mainListener = new Listener() {

					@EventHandler
					private void pluginDisable(PluginDisableEvent event) {
						HashSet<CustomInventory> invs = _openedInvsByPlugin.remove(event.getPlugin());
						if (invs != null) {
							for (CustomInventory inv : invs) {
								for (HumanEntity human : inv._inventory.getViewers().toArray(new HumanEntity[0])) {
									_openedInvsByPlayer.remove(human);
									human.closeInventory();
								}
							}
						}

						if (_plugin == event.getPlugin()) {
							_plugin = null;
							HandlerList.unregisterAll(_mainListener);
							if (_openedInvsByPlugin.size() > 0) {
								bindListener(_openedInvsByPlugin.keySet().iterator().next());
							}
						}
					}

					@EventHandler
					private void inventoryClick(InventoryClickEvent event) {
						CustomInventory inv = _openedInvsByPlayer.get(event.getWhoClicked());
						if (inv != null) {
							inv.inventoryClick(event);
						}
					}

					@EventHandler
					private void inventoryClose(InventoryCloseEvent event) {
						CustomInventory inv = _openedInvsByPlayer.remove(event.getPlayer());
						if (inv != null) {
							_openedInvsByPlugin.get(inv._owner).remove(inv);
							inv.inventoryClose(event);
						}
					}

				};
			}

			Bukkit.getPluginManager().registerEvents(_mainListener, plugin);
			_plugin = plugin;
		}
	}

	public CustomInventory(Player owner, int size) {
		_inventory = Bukkit.createInventory(owner, size);
	}

	public CustomInventory(Player owner, int size, String title) {
		_inventory = Bukkit.createInventory(owner, size, title);
	}

	public final void openInventory(Player player, Plugin owner) {
		if (_owner == null) {
			player.openInventory(_inventory);
			_openedInvsByPlayer.put(player, this);

			this._owner = owner;

			HashSet<CustomInventory> set = _openedInvsByPlugin.get(player);
			if (set == null) {
				set = new HashSet<CustomInventory>();
				_openedInvsByPlugin.put(owner, set);
			}
			set.add(this);
			bindListener(owner);
		}
	}

	public Inventory getInventory() {
		return _inventory;
	}

	public Plugin getPlugin() {
		return _owner;
	}

	public final void close() {
		if (_owner != null) {
			for (HumanEntity human : _inventory.getViewers().toArray(new HumanEntity[0])) {
				human.closeInventory();
			}
		}
	}

	protected abstract void inventoryClick(InventoryClickEvent event);

	protected abstract void inventoryClose(InventoryCloseEvent event);

}
