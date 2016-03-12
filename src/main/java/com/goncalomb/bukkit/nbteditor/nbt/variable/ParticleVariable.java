package com.goncalomb.bukkit.nbteditor.nbt.variable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;

public class ParticleVariable extends NBTGenericVariable {

	private static List<String> _particles = new ArrayList<String>();

	static {
		try {
			// The particle API is only available on Spigot. We still use the Bukkit API.
			// This supports both Spigot and CraftBukkit.
			Effect.Type particleType = Enum.valueOf(Effect.Type.class, "PARTICLE");
			Method method_getName = Effect.class.getMethod("getName");
			for (Effect eff : Effect.values()) {
				if (eff.getType().equals(particleType)) {
					_particles.add((String) method_getName.invoke(eff));
				}
			}
		} catch (Exception e) { }
	}

	public ParticleVariable(String nbtKey) {
		super(nbtKey);
	}

	@Override
	boolean set(NBTTagCompound data, String value, Player player) {
		data.setString(_nbtKey, value);
		return true; // accepts any string, even unknown particle names
	}

	@Override
	String get(NBTTagCompound data) {
		return data.getString(_nbtKey);
	}

	@Override
	String getFormat() {
		return "A particle name (not all particles work, why: ?).";
	}

	@Override
	public List<String> getPossibleValues() {
		return _particles;
	}

}
