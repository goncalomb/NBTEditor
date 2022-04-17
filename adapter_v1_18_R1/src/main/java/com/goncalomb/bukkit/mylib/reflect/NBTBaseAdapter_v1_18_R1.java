/*
 * Copyright (C) 2013-2018 Gonçalo Baltazar <me@goncalomb.com>
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

package com.goncalomb.bukkit.mylib.reflect;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NBTBaseAdapter_v1_18_R1 implements NBTBaseAdapter {
	public NBTBaseAdapter_v1_18_R1() {
		// No setup needed but this constructor must exist
	}

	@Override
	public NBTBase wrap(Object object) {
		if (object instanceof CompoundTag) {
			return new NBTTagCompound(object);
		} else if (object instanceof ListTag) {
			return new NBTTagList(object);
		} else if (object instanceof Tag) {
			return new NBTBase(object);
		} else {
			throw new RuntimeException(object.getClass() + " is not a valid NBT tag type.");
		}
	}

	@Override
	public Object clone(Object nbtBaseObject) {
		return ((Tag) nbtBaseObject).copy();
	}

	@Override
	public byte getTypeId(Object handle) {
		return ((Tag) handle).getId();
	}
}
