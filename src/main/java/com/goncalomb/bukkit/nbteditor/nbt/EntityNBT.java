/*
 * Copyright (C) 2013-2016 Gonçalo Baltazar <me@goncalomb.com>
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

import org.bukkit.entity.EntityType;

import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.mylib.reflect.NBTTagList;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BlockVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.NBTGenericVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variable.RotationVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.StringVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variable.VectorVariable;

public class EntityNBT extends EntityNBTBase {

	static {
		registerEntity(EntityType.PIG, BreedNBT.class);
		registerEntity(EntityType.SHEEP, BreedNBT.class);
		registerEntity(EntityType.COW, BreedNBT.class);
		registerEntity(EntityType.CHICKEN, BreedNBT.class);
		registerEntity(EntityType.MUSHROOM_COW, BreedNBT.class);
		registerEntity(EntityType.POLAR_BEAR, BreedNBT.class);
		registerEntity(EntityType.SQUID, MobNBT.class);

		registerEntity(EntityType.WOLF, TamedNBT.class);
		registerEntity(EntityType.OCELOT, TamedNBT.class);
		registerEntity(EntityType.HORSE, HorseNBT.class);

		registerEntity(EntityType.VILLAGER, VillagerNBT.class);
		registerEntity(EntityType.IRON_GOLEM, MobNBT.class);
		registerEntity(EntityType.SNOWMAN, MobNBT.class);

		registerEntity(EntityType.ZOMBIE, ZombieNBT.class);
		registerEntity(EntityType.PIG_ZOMBIE, ZombieNBT.class);
		registerEntity(EntityType.SLIME, SlimeNBT.class);
		registerEntity(EntityType.MAGMA_CUBE, SlimeNBT.class);
		registerEntity(EntityType.GHAST, MobNBT.class);
		registerEntity(EntityType.SKELETON, MobNBT.class);
		registerEntity(EntityType.CREEPER, MobNBT.class);
		registerEntity(EntityType.BAT, MobNBT.class);
		registerEntity(EntityType.BLAZE, MobNBT.class);
		registerEntity(EntityType.SPIDER, MobNBT.class);
		registerEntity(EntityType.CAVE_SPIDER, MobNBT.class);
		registerEntity(EntityType.GIANT, MobNBT.class);
		registerEntity(EntityType.ENDERMAN, MobNBT.class);
		registerEntity(EntityType.SILVERFISH, MobNBT.class);
		registerEntity(EntityType.WITCH, MobNBT.class);
		registerEntity(EntityType.GUARDIAN, MobNBT.class);
		registerEntity(EntityType.ENDERMITE, MobNBT.class);
		registerEntity(EntityType.RABBIT, MobNBT.class);
		registerEntity(EntityType.SHULKER, MobNBT.class);

		registerEntity(EntityType.ENDER_DRAGON, MobNBT.class);
		registerEntity(EntityType.WITHER, MobNBT.class);

		registerEntity(EntityType.PRIMED_TNT, EntityNBT.class);
		registerEntity(EntityType.FALLING_BLOCK, FallingBlockNBT.class);
		registerEntity(EntityType.DROPPED_ITEM, DroppedItemNBT.class);
		registerEntity(EntityType.EXPERIENCE_ORB, XPOrbNBT.class);
		registerEntity(EntityType.ENDER_CRYSTAL, EntityNBT.class);
		registerEntity(EntityType.FIREWORK, FireworkNBT.class);

		registerEntity(EntityType.ARROW, ArrowNBT.class);
		registerEntity(EntityType.SPECTRAL_ARROW, ArrowNBT.class);
		registerEntity(EntityType.TIPPED_ARROW, TippedArrowNBT.class);
		registerEntity(EntityType.ENDER_PEARL, EntityNBT.class);
		registerEntity(EntityType.THROWN_EXP_BOTTLE, EntityNBT.class);
		registerEntity(EntityType.SNOWBALL, EntityNBT.class);
		registerEntity(EntityType.EGG, EntityNBT.class);
		registerEntity(EntityType.SPLASH_POTION, ThrownPotionNBT.class);
		registerEntity(EntityType.FIREBALL, FireballNBT.class);
		registerEntity(EntityType.SMALL_FIREBALL, FireballNBT.class);
		registerEntity(EntityType.DRAGON_FIREBALL, FireballNBT.class);
		registerEntity(EntityType.WITHER_SKULL, FireballNBT.class);
		registerEntity(EntityType.ARMOR_STAND, EquippableNBT.class);
		registerEntity(EntityType.AREA_EFFECT_CLOUD, AreaEffectCloudNBT.class);

		registerEntity(EntityType.BOAT, EntityNBT.class);

		registerEntity(EntityType.MINECART, MinecartNBT.class);
		registerEntity(EntityType.MINECART_CHEST, MinecartContainerNBT.class);
		registerEntity(EntityType.MINECART_FURNACE, MinecartNBT.class);
		registerEntity(EntityType.MINECART_HOPPER, MinecartContainerNBT.class);
		registerEntity(EntityType.MINECART_MOB_SPAWNER, MinecartSpawnerNBT.class);
		registerEntity(EntityType.MINECART_TNT, MinecartNBT.class);
		registerEntity(EntityType.MINECART_COMMAND, MinecartCommandNBT.class);


		NBTGenericVariableContainer variables = null;

		variables = new NBTGenericVariableContainer("Entity");
		variables.add("Position", new VectorVariable("Pos", true));
		variables.add("Velocity", new VectorVariable("Motion"));
		variables.add("Rotation", new RotationVariable("Rotation"));
		variables.add("FallDistance", new FloatVariable("FallDistance", 0.0f));
		variables.add("Fire", new ShortVariable("Fire"));
		variables.add("Air", new ShortVariable("Air", (short) 0, (short) 200));
		variables.add("Invulnerable", new BooleanVariable("Invulnerable"));
		variables.add("Silent", new BooleanVariable("Silent"));
		variables.add("Glowing", new BooleanVariable("Glowing"));
		registerVariables(EntityNBT.class, variables);

		variables = new NBTGenericVariableContainer("Pig");
		variables.add("Saddle", new BooleanVariable("Saddle"));
		registerVariables(EntityType.PIG, variables);

		variables = new NBTGenericVariableContainer("Sheep");
		variables.add("Sheared", new BooleanVariable("Sheared"));
		variables.add("Color", new ByteVariable("Color", (byte) 0, (byte) 15));
		registerVariables(EntityType.SHEEP, variables);

		variables = new NBTGenericVariableContainer("Chicken");
		variables.add("EggLayTime", new IntegerVariable("EggLayTime"));
		registerVariables(EntityType.CHICKEN, variables);

		variables = new NBTGenericVariableContainer("Wolf");
		variables.add("Angry", new BooleanVariable("Angry"));
		variables.add("CollarColor", new ByteVariable("CollarColor", (byte) 0, (byte) 15));
		registerVariables(EntityType.WOLF, variables);

		variables = new NBTGenericVariableContainer("Ocelot");
		variables.add("Type", new IntegerVariable("CatType", 0, 3));
		registerVariables(EntityType.OCELOT, variables);


		variables = new NBTGenericVariableContainer("IronGolem");
		variables.add("PlayerCreated", new BooleanVariable("PlayerCreated"));
		registerVariables(EntityType.IRON_GOLEM, variables);


		variables = new NBTGenericVariableContainer("PigZombie");
		variables.add("Anger", new ShortVariable("Anger"));
		registerVariables(EntityType.PIG_ZOMBIE, variables);

		variables = new NBTGenericVariableContainer("Ghast");
		variables.add("ExplosionPower", new IntegerVariable("ExplosionPower", 0, 25)); // Limited to 25
		registerVariables(EntityType.GHAST, variables);

		variables = new NBTGenericVariableContainer("Skeleton");
		variables.add("IsWither", new BooleanVariable("SkeletonType"));
		registerVariables(EntityType.SKELETON, variables);

		variables = new NBTGenericVariableContainer("Creeper");
		variables.add("Powered", new BooleanVariable("powered"));
		variables.add("ExplosionRadius", new ByteVariable("ExplosionRadius", (byte) 0, (byte) 25)); // Limited to 25
		variables.add("Fuse", new ShortVariable("Fuse", (short) 0));
		variables.add("Ignited", new BooleanVariable("ignited"));
		registerVariables(EntityType.CREEPER, variables);

		variables = new NBTGenericVariableContainer("Enderman");
		variables.add("Block", new BlockVariable("carried", "carriedData", true));
		registerVariables(EntityType.ENDERMAN, variables);

		variables = new NBTGenericVariableContainer("Guardian");
		variables.add("Elder", new BooleanVariable("Elder"));
		registerVariables(EntityType.GUARDIAN, variables);

		variables = new NBTGenericVariableContainer("Endermite");
		variables.add("Lifetime", new IntegerVariable("Lifetime"));
		variables.add("PlayerSpawned", new BooleanVariable("PlayerSpawned"));
		registerVariables(EntityType.ENDERMITE, variables);

		variables = new NBTGenericVariableContainer("Rabbit");
		variables.add("Type", new IntegerVariable("RabbitType", 0, 99));
		registerVariables(EntityType.RABBIT, variables);


		variables = new NBTGenericVariableContainer("EnderDragon");
		variables.add("DragonPhase", new IntegerVariable("DragonPhase", 0, 10));
		registerVariables(EntityType.ENDER_DRAGON, variables);

		variables = new NBTGenericVariableContainer("Wither");
		variables.add("InvulnerableTime", new IntegerVariable("Invul", 0));
		registerVariables(EntityType.WITHER, variables);


		variables = new NBTGenericVariableContainer("PrimedTNT");
		variables.add("Fuse", new ByteVariable("Fuse", (byte) 0));
		registerVariables(EntityType.PRIMED_TNT, variables);

		variables = new NBTGenericVariableContainer("EnderCrystal");
		variables.add("ShowBottom", new BooleanVariable("ShowBottom"));
		registerVariables(EntityType.ENDER_CRYSTAL, variables);


		variables = new NBTGenericVariableContainer("SpectralArrow");
		variables.add("Duration", new IntegerVariable("Duration", 0));
		registerVariables(EntityType.SPECTRAL_ARROW, variables);

		variables = new NBTGenericVariableContainer("Enderpearl");
		variables.add("Owner", new StringVariable("ownerName"));
		registerVariables(EntityType.ENDER_PEARL, variables);

		variables = new NBTGenericVariableContainer("LargeFireball");
		variables.add("ExplosionPower", new IntegerVariable("ExplosionPower", 0, 25)); // Limited to 25
		registerVariables(EntityType.FIREBALL, variables);

		variables = new NBTGenericVariableContainer("Boat");
		variables.add("Type", new StringVariable("Type"));
		registerVariables(EntityType.BOAT, variables);

		variables = new NBTGenericVariableContainer("ArmorStand");
		variables.add("Marker", new BooleanVariable("Marker"));
		variables.add("Invisible", new BooleanVariable("Invisible"));
		variables.add("NoBasePlate", new BooleanVariable("NoBasePlate"));
		variables.add("NoGravity", new BooleanVariable("NoGravity"));
		variables.add("ShowArms", new BooleanVariable("ShowArms"));
		variables.add("Small", new BooleanVariable("Small"));
		variables.add("PoseBody", new RotationVariable("Body", true, "Pose"));
		variables.add("PoseLeftArm", new RotationVariable("LeftArm", true, "Pose"));
		variables.add("PoseRightArm", new RotationVariable("RightArm", true, "Pose"));
		variables.add("PoseLeftLeg", new RotationVariable("LeftLeg", true, "Pose"));
		variables.add("PoseRightLeg", new RotationVariable("RightLeg", true, "Pose"));
		variables.add("PoseHead", new RotationVariable("Head", true, "Pose"));
		registerVariables(EntityType.ARMOR_STAND, variables);

	}

	protected EntityNBT() {
		super(null);
	}

	protected EntityNBT(EntityType entityType) {
		super(entityType);
	}

	public void setPos(double x, double y, double z) {
		_data.setList("Pos", x, y, z);
	}

	public void removePos() {
		_data.remove("Pos");
	}

	public void setMotion(double x, double y, double z) {
		_data.setList("Motion", x, y, z);
	}

	public void removeMotion() {
		_data.remove("Motion");
	}

	public EntityNBT getFirstPassenger() {
		NBTTagList passengers = _data.getList("Passengers");
		if (passengers != null && passengers.size() > 0) {
			NBTTagCompound passenger = (NBTTagCompound) passengers.get(0);
			return fromEntityData(passenger);
		}
		return null;
	}

	// TODO: implement a way to set multiple passengers per entity

	public void setRiders(EntityNBT... riders) {
		if (riders == null || riders.length == 0) {
			_data.remove("Passengers");
			return;
		}
		NBTTagCompound now = _data;
		for (EntityNBT rider : riders) {
			NBTTagCompound next = rider._data.clone();
			now.setList("Passengers", new NBTTagList(next));
			now = next;
		}
	}

}
