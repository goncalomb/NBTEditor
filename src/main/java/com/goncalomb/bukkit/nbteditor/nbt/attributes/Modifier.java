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

package com.goncalomb.bukkit.nbteditor.nbt.attributes;

import java.util.UUID;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class Modifier {

	private String _name;
	private double _amount;
	private int _operation;
	private UUID _uuid;

	public static Modifier fromNBT(NBTTagCompound data) {
		return new Modifier(data.getString("Name"), data.getDouble("Amount"), data.getInt("Operation"), new UUID(data.getLong("UUIDMost"), data.getLong("UUIDLeast")));
	}

	public Modifier(String name, double amount, int operation) {
		this(name, amount, operation, UUID.randomUUID());
	}

	public Modifier(String name, double amount, int operation, UUID uuid) {
		_name = name;
		_amount = amount;
		_operation = Math.max(Math.min(operation, 2), 0);
		_uuid = uuid;
	}

	public final String getName() {
		return _name;
	}

	public final double getAmount() {
		return _amount;
	}

	public final int getOperation() {
		return _operation;
	}

	public final UUID getUUID() {
		return _uuid;
	}

	public NBTTagCompound toNBT() {
		NBTTagCompound data = new NBTTagCompound();
		data.setString("Name", _name);
		data.setDouble("Amount", _amount);
		data.setInt("Operation", _operation);
		data.setLong("UUIDMost", _uuid.getMostSignificantBits());
		data.setLong("UUIDLeast", _uuid.getLeastSignificantBits());
		return data;
	}

}
