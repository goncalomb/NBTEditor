package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;

import com.goncalomb.bukkit.nbteditor.nbt.variable.BlockVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.reflect.NBTUtils;

public class FallingBlockNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("FallingBlock");
		variables.add("block", new BlockVariable("TileID", "Data"));
		variables.add("time", new ByteVariable("Time", (byte)0));
		variables.add("drop-item", new BooleanVariable("DropItem"));
		variables.add("hurt-entities", new BooleanVariable("HurtEntities"));
		variables.add("fall-hurt-amount", new FloatVariable("FallHurtAmount", 0));
		variables.add("fall-hurt-max", new IntegerVariable("FallHurtMax", 0));
		EntityNBTVariableManager.registerVariables(FallingBlockNBT.class, variables);
	}

	@Override
	public Entity spawn(Location location) {
		int blockId = (_data.hasKey("TileID") ? _data.getInt("TileID") : Material.SAND.getId());
		byte blockData = (_data.hasKey("Data") ? _data.getByte("Data") : 0);
		Entity entity = location.getWorld().spawnFallingBlock(location, blockId, blockData);
		NBTUtils.setEntityNBTTagCompound(entity, _data);
		return entity;
	}
	
}
