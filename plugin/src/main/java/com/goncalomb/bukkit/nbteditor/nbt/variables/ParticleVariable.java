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

package com.goncalomb.bukkit.nbteditor.nbt.variables;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Effect;

public class ParticleVariable extends StringVariable {

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

	public ParticleVariable(String key) {
		super(key);
	}

	@Override
	public String getFormat() {
		return "A particle name (not all particles work, why: ?).";
	}

	@Override
	public List<String> getPossibleValues() {
		return _particles;
	}

}
