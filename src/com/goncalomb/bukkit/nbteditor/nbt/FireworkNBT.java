package com.goncalomb.bukkit.nbteditor.nbt;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.reflect.NBTUtils;

public final class FireworkNBT extends EntityNBT {
	
	static {
		NBTGenericVariableContainer variables = new NBTGenericVariableContainer("Firework");
		variables.add("life", new IntegerVariable("Life", 0, 200)); // Limited to 200
		variables.add("lifetime", new IntegerVariable("LifeTime", 0, 200)); // Limited to 200
		EntityNBTVariableManager.registerVariables(FireworkNBT.class, variables);
	}
	
	public FireworkNBT() {
		super(EntityType.FIREWORK);
	}
	
	public FireworkNBT(ItemStack firework) {
		this();
		if (firework.getType() != Material.FIREWORK) throw new IllegalArgumentException("Invalid argument firework.");
		_data.setInt("Life", 0);
		_data.setCompound("FireworksItem", NBTUtils.nbtTagCompoundFromItemStack(firework));
		setLifeTimeFromItem(firework);
	}
	
	private void setLifeTimeFromItem(ItemStack firework) {
		if (firework == null) {
			_data.remove("FireworksItem");
		} else {
			_data.setInt("LifeTime", 12 + 12 * ((FireworkMeta) firework.getItemMeta()).getPower());
		}
	}
	
	public void setFirework(ItemStack firework) {
		if (firework == null) {
			_data.remove("FireworksItem");
		} else {
			_data.setCompound("FireworksItem", NBTUtils.nbtTagCompoundFromItemStack(firework));
		}
		setLifeTimeFromItem(firework);
	}
	
	public ItemStack getFirework() {
		if (_data.hasKey("FireworksItem")) {
			return NBTUtils.itemStackFromNBTTagCompound(_data.getCompound("FireworksItem"));
		}
		return null;
	}
	
	public boolean isSet() {
		return _data.hasKey("FireworksItem");
	}
	
}
