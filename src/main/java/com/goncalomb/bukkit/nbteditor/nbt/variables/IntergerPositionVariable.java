package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class IntergerPositionVariable extends NBTVariable {

	public IntergerPositionVariable(String key) {
		super(key);
	}

	@Override
	public boolean set(String value, Player player) {
		if (player != null && value.equalsIgnoreCase("Here")) {
			NBTTagCompound pos = new NBTTagCompound();
			Location loc = player.getLocation();
			pos.setInt("X", loc.getBlockX());
			pos.setInt("Y", loc.getBlockY());
			pos.setInt("Z", loc.getBlockZ());
			data().setCompound(_key, pos);
			return true;
		}
		String[] pieces = value.split("\\s+", 3);
		if (pieces.length == 3) {
			NBTTagCompound pos = new NBTTagCompound();
			try {
				pos.setInt("X", Integer.parseInt(pieces[0]));
				pos.setInt("Y", Integer.parseInt(pieces[1]));
				pos.setInt("Z", Integer.parseInt(pieces[2]));
			} catch (NumberFormatException e) {
				return false;
			}
			data().setCompound(_key, pos);
			return true;
		}
		return false;
	}

	@Override
	public String get() {
		NBTTagCompound data = data();
		if (data.hasKey(_key)) {
			NBTTagCompound pos = data.getCompound(_key);
			return pos.getInt("X") + " " + pos.getInt("Y") + " " + pos.getInt("Z");
		}
		return null;
	}

	@Override
	public String getFormat() {
		return "Set of 3 integer, '0 0 0'. Use 'Here' to set with your current position.";
	}

	@Override
	public List<String> getPossibleValues() {
		return Arrays.asList(new String[] { "Here" });
	}

}
