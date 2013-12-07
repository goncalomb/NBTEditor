/*
 * Copyright (C) 2013 - Gonçalo Baltazar <http://goncalomb.com>
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

import org.bukkit.block.Block;
import org.bukkit.potion.PotionEffectType;

public final class BeaconNBTWrapper extends TileNBTWrapper {
	
	public BeaconNBTWrapper(Block block) {
		super(block);
	}
	
	public void setPrimary(PotionEffectType effect) {
		if (effect == null) {
			_data.setInt("Primary", 0);
		} else {
			_data.setInt("Primary", effect.getId());
		}
	}
	
	public void setSecondary(PotionEffectType effect) {
		if (effect == null) {
			_data.setInt("Secondary", 0);
		} else {
			_data.setInt("Secondary", effect.getId());
		}
	}

}
