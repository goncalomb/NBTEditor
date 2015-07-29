/*
 * Copyright (C) 2013-2015 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.inventory.ItemStack;

import com.goncalomb.bukkit.bkglib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.bkglib.reflect.NBTUtils;

public final class VillagerNBTOffer {
	
	private ItemStack _buyA;
	private ItemStack _buyB;
	private ItemStack _sell;
	private int _maxUses;
	private int _uses;
	
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell) {
		this(buyA, buyB, sell, 7);
	}
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell, int maxUses) {
		this(buyA, buyB, sell, maxUses, 0);
	}
	
	public VillagerNBTOffer(ItemStack buyA, ItemStack buyB, ItemStack sell, int maxUses, int uses) {
		_buyA = buyA;
		_buyB = buyB;
		_sell = sell;
		_maxUses = maxUses;
		_uses = uses;
	}
	
	VillagerNBTOffer(NBTTagCompound offer) {
		_buyA = NBTUtils.itemStackFromNBTData(offer.getCompound("buy"));
		if (offer.hasKey("buyB")) {
			_buyB = NBTUtils.itemStackFromNBTData(offer.getCompound("buyB"));
		} else {
			_buyB = null;
		}
		_sell = NBTUtils.itemStackFromNBTData(offer.getCompound("sell"));
		_maxUses = offer.getInt("maxUses");
		_uses = offer.getInt("uses");
	}
	
	NBTTagCompound getCompound() {
		NBTTagCompound offer = new NBTTagCompound();
		offer.setCompound("buy", NBTUtils.itemStackToNBTData(_buyA));
		if (_buyB != null) {
			offer.setCompound("buyB", NBTUtils.itemStackToNBTData(_buyB));
		}
		offer.setCompound("sell", NBTUtils.itemStackToNBTData(_sell));
		offer.setInt("maxUses", _maxUses);
		offer.setInt("uses", _uses);
		return offer;
	}
	
	public ItemStack getBuyA() {
		return _buyA;
	}
	
	public ItemStack getBuyB() {
		return _buyB;
	}
	
	public ItemStack getSell() {
		return _sell;
	}
	
	public int getMaxUses() {
		return _maxUses;
	}
	
	public int getUses() {
		return _uses;
	}
}
