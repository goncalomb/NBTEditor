package com.goncalomb.bukkit.nbteditor.nbt.variables;

import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.nbteditor.nbt.EntityNBT;

public class PassengersVariable extends NBTVariable implements SpecialVariable {

	public PassengersVariable() {
		super("Passengers");
	}

	@Override
	public boolean set(String value, Player player) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String get() {
		return (data().getList(_key) == null ? null : "passengers set");
	}

	@Override
	public String getFormat() {
		return "The passengers riding the entity.";
	}

	public void setPassengers(EntityNBT... passengers) {
		if (passengers == null || passengers.length == 0) {
			clear();
			return;
		}
		NBTTagList list = new NBTTagList();
		for (EntityNBT passenger : passengers) {
			list.add(passenger.getData());
		}
		data().setList(_key, list);
	}

	public EntityNBT[] getPassengers() {
		NBTTagList list = data().getList(_key);
		if (list != null && list.size() > 0) {
			int l = list.size();
			EntityNBT[] passengers = new EntityNBT[l];
			for (int i = 0; i < l; i++) {
				passengers[i] = EntityNBT.fromEntityData((NBTTagCompound) list.get(i));
			}
			return passengers;
		}
		return new EntityNBT[0];
	}

}
