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

package com.goncalomb.bukkit.nbteditor.nbt;

import com.goncalomb.bukkit.mylib.namemaps.EntityTypeMap;
import com.goncalomb.bukkit.mylib.reflect.BukkitVersion;
import com.goncalomb.bukkit.mylib.reflect.NBTTagCompound;
import com.goncalomb.bukkit.nbteditor.bos.BookOfSouls;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BlockStateVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.BooleanVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ByteVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ColorVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ContainerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.DoubleVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.EffectsVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.FireworksItemVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.FloatArrayVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.FloatVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.HorseVariantVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.IntegerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ItemsVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.LongVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.NBTUnboundVariableContainer;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ParticleVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.PassengersVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.PotionVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.RawJsonVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.RotationVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.ShortVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.SingleItemVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringListVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.StringVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.VectorVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.VillagerCareerVariable;
import com.goncalomb.bukkit.nbteditor.nbt.variables.VillagerOffersVariable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.EntityType;

public class EntityNBT extends EntityNBTBase {

	private static final HashMap<String, NBTUnboundVariableContainer> ENTITY_VARIABLES = new HashMap<String, NBTUnboundVariableContainer>();

	static {

		// Main Types

		NBTUnboundVariableContainer cEntity = new NBTUnboundVariableContainer("Entity");
		cEntity.add("Position", new VectorVariable("Pos", true));
		cEntity.add("Velocity", new VectorVariable("Motion"));
		cEntity.add("Rotation", new RotationVariable("Rotation"));
		cEntity.add("FallDistance", new FloatVariable("FallDistance", 0.0f));
		cEntity.add("Fire", new ShortVariable("Fire"));
		cEntity.add("Air", new ShortVariable("Air", (short) 0, (short) 200));
		cEntity.add("NoGravity", new BooleanVariable("NoGravity"));
		cEntity.add("Invulnerable", new BooleanVariable("Invulnerable"));
		cEntity.add("Silent", new BooleanVariable("Silent"));
		cEntity.add("Name", new RawJsonVariable("CustomName"));
		cEntity.add("NameVisible", new BooleanVariable("CustomNameVisible"));
		cEntity.add("Passengers", new PassengersVariable());
		cEntity.add("Glowing", new BooleanVariable("Glowing"));
		cEntity.add("Tags", new StringListVariable("Tags"));

		NBTUnboundVariableContainer cEquippable = new NBTUnboundVariableContainer("Equippable", cEntity);
		cEquippable.add("FallFlying", new BooleanVariable("FallFlying"));
		cEquippable.add("ArmorItems", new ItemsVariable("ArmorItems", new String[] { "Feet Equipment", "Legs Equipment", "Chest Equipment", "Head Equipment" }));
		cEquippable.add("HandItems", new ItemsVariable("HandItems", new String[] { "Main Hand Item", "Off Hand Item" }));

		NBTUnboundVariableContainer cMob = new NBTUnboundVariableContainer("Mob", cEquippable);
		cMob.add("Health", new FloatVariable("Health", 0.0f));
		cMob.add("AttackTime", new ShortVariable("AttackTime"));
		cMob.add("HurtTime", new ShortVariable("HurtTime"));
		cMob.add("DeathTime", new ShortVariable("DeathTime"));
		cMob.add("ActiveEffects", new EffectsVariable("ActiveEffects"));
		cMob.add("HandDropChances", new FloatArrayVariable("HandDropChances", 2, 0f, 2f));
		cMob.add("ArmorDropChances", new FloatArrayVariable("ArmorDropChances", 4, 0f, 2f));
		cMob.add("DeathLootTable", new StringVariable("DeathLootTable"));
		cMob.add("DeathLootTableSeed", new LongVariable("DeathLootTableSeed"));
		cMob.add("PickLoot", new BooleanVariable("CanPickUpLoot"));
		cMob.add("NoAI", new BooleanVariable("NoAI"));
		cMob.add("Persistent", new BooleanVariable("PersistenceRequired"));
		cMob.add("LeftHanded", new BooleanVariable("LeftHanded"));
		cMob.add("Team", new StringVariable("Team"));

		NBTUnboundVariableContainer cBreed = new NBTUnboundVariableContainer("Breed", cMob);
		cBreed.add("InLove", new IntegerVariable("InLove", 0));
		cBreed.add("Age", new IntegerVariable("Age"));
		cBreed.add("ForcedAge", new IntegerVariable("ForcedAge")); // XXX: not working?
		cBreed.add("AgeLocked", new BooleanVariable("AgeLocked"));

		NBTUnboundVariableContainer cTameable = new NBTUnboundVariableContainer("Tameable", cBreed);
		cTameable.add("Owner", new StringVariable("Owner"));
		cTameable.add("Sitting", new BooleanVariable("Sitting"));

		NBTUnboundVariableContainer cFish = new NBTUnboundVariableContainer("Fish", cMob);
		cFish.add("FromBucket", new BooleanVariable("FromBucket"));

		// Mob SubTypes

		NBTUnboundVariableContainer cCreeper = new NBTUnboundVariableContainer("Creeper", cMob);
		cCreeper.add("Powered", new BooleanVariable("powered"));
		cCreeper.add("ExplosionRadius", new ByteVariable("ExplosionRadius", (byte) 0, (byte) 25)); // Limited to 25
		cCreeper.add("Fuse", new ShortVariable("Fuse", (short) 0));
		cCreeper.add("Ignited", new BooleanVariable("ignited"));

		NBTUnboundVariableContainer cDolphin = new NBTUnboundVariableContainer("Dolphin", cMob);
		cDolphin.add("CanFindTreasure", new BooleanVariable("CanFindTreasure"));
		cDolphin.add("GotFish", new BooleanVariable("GotFish"));
		cDolphin.add("Moistness", new IntegerVariable("Moistness"));
		cDolphin.add("TreasurePosX", new IntegerVariable("TreasurePosX"));
		cDolphin.add("TreasurePosY", new IntegerVariable("TreasurePosY"));
		cDolphin.add("TreasurePosZ", new IntegerVariable("TreasurePosZ"));

		NBTUnboundVariableContainer cEnderDragon = new NBTUnboundVariableContainer("EnderDragon", cMob);
		cEnderDragon.add("DragonPhase", new IntegerVariable("DragonPhase", 0, 10));

		NBTUnboundVariableContainer cEnderman = new NBTUnboundVariableContainer("Enderman", cMob);
		cEnderman.add("BlockState", new BlockStateVariable("carriedBlockState"));

		NBTUnboundVariableContainer cEndermite = new NBTUnboundVariableContainer("Endermite", cMob);
		cEndermite.add("Lifetime", new IntegerVariable("Lifetime"));
		cEndermite.add("PlayerSpawned", new BooleanVariable("PlayerSpawned"));

		NBTUnboundVariableContainer cEvocationIllager = new NBTUnboundVariableContainer("EvocationIllager", cMob);
		cEvocationIllager.add("SpellTicks", new IntegerVariable("SpellTicks"));

		NBTUnboundVariableContainer cGhast = new NBTUnboundVariableContainer("Ghast", cMob);
		cGhast.add("ExplosionPower", new IntegerVariable("ExplosionPower", 0, 25)); // Limited to 25

		NBTUnboundVariableContainer cShulker = new NBTUnboundVariableContainer("Shulker", cMob);
		cShulker.add("Color", new ByteVariable("Color", (byte) 0, (byte) 15));

		NBTUnboundVariableContainer cSlime = new NBTUnboundVariableContainer("Slime", cMob);
		cSlime.add("Size", new IntegerVariable("Size", -50, 50)); // Limited to 50

		NBTUnboundVariableContainer cSnowman = new NBTUnboundVariableContainer("Snowman", cMob);
		cSnowman.add("Pumpkin", new BooleanVariable("Pumpkin"));

		NBTUnboundVariableContainer cVex = new NBTUnboundVariableContainer("Vex", cMob);
		cVex.add("LifeTicks", new IntegerVariable("LifeTicks"));

		NBTUnboundVariableContainer cTurtle = new NBTUnboundVariableContainer("Turtle", cBreed);
		cTurtle.add("HasEgg", new BooleanVariable("HasEgg"));
		cTurtle.add("HomePosX", new IntegerVariable("HomePosX"));
		cTurtle.add("HomePosY", new IntegerVariable("HomePosY"));
		cTurtle.add("HomePosZ", new IntegerVariable("HomePosZ"));
		cTurtle.add("TravelPosX", new IntegerVariable("TravelPosX"));
		cTurtle.add("TravelPosY", new IntegerVariable("TravelPosY"));
		cTurtle.add("TravelPosZ", new IntegerVariable("TravelPosZ"));

		NBTUnboundVariableContainer cVillagerGolem = new NBTUnboundVariableContainer("VillagerGolem", cMob);
		cVillagerGolem.add("PlayerCreated", new BooleanVariable("PlayerCreated"));

		NBTUnboundVariableContainer cVindicationIllager = new NBTUnboundVariableContainer("VindicationIllager", cMob);
		cVindicationIllager.add("Johnny", new BooleanVariable("Johnny"));

		NBTUnboundVariableContainer cWither = new NBTUnboundVariableContainer("Wither", cMob);
		cWither.add("InvulnerableTime", new IntegerVariable("Invul", 0));

		NBTUnboundVariableContainer cZombie = new NBTUnboundVariableContainer("Zombie", cMob);
		cZombie.add("IsBaby", new BooleanVariable("IsBaby"));
		cZombie.add("CanBreakDoors", new BooleanVariable("CanBreakDoors"));
		cZombie.add("InWaterTime", new IntegerVariable("InWaterTime"));
		cZombie.add("DrownedConversionTime", new IntegerVariable("DrownedConversionTime"));

		// 1.15 and below
		NBTUnboundVariableContainer cZombiePigman = new NBTUnboundVariableContainer("ZombiePigman", cZombie);
		cZombiePigman.add("Anger", new ShortVariable("Anger"));

		NBTUnboundVariableContainer cZoglin = new NBTUnboundVariableContainer("Zoglin", cMob);
		cZoglin.add("IsBaby", new BooleanVariable("IsBaby"));

		NBTUnboundVariableContainer cPiglin = new NBTUnboundVariableContainer("Piglin", cMob);
		cPiglin.add("IsBaby", new BooleanVariable("IsBaby"));
		cPiglin.add("IsImmuneToZombification", new BooleanVariable("IsImmuneToZombification"));
		cPiglin.add("CannotHunt", new BooleanVariable("CannotHunt"));
		cPiglin.add("TimeInOverworld", new IntegerVariable("TimeInOverworld"));
		// TODO: add villager inventory

		NBTUnboundVariableContainer cZombifiedPiglin = new NBTUnboundVariableContainer("ZombifiedPilgin", cZombie);
		cZombifiedPiglin.add("AngerTime", new IntegerVariable("AngerTime"));

		NBTUnboundVariableContainer cZombieVillager = new NBTUnboundVariableContainer("ZombieVillager", cZombie);
		cZombieVillager.add("Profession", new IntegerVariable("Profession", 0, 5));
		cZombieVillager.add("ConversionTime", new IntegerVariable("ConversionTime", -1));

		NBTUnboundVariableContainer cPhantom = new NBTUnboundVariableContainer("Phantom", cMob);
		cPhantom.add("AX", new IntegerVariable("AX"));
		cPhantom.add("AY", new IntegerVariable("AY"));
		cPhantom.add("AZ", new IntegerVariable("AZ"));
		cPhantom.add("Size", new IntegerVariable("Size"));

		// Breed SubTypes

		NBTUnboundVariableContainer cChicken = new NBTUnboundVariableContainer("Chicken", cBreed);
		cChicken.add("EggLayTime", new IntegerVariable("EggLayTime"));

		NBTUnboundVariableContainer cHorse = new NBTUnboundVariableContainer("Horse", cBreed);
		cHorse.add("Tamed", new BooleanVariable("Tame"));
		cHorse.add("Eating", new BooleanVariable("EatingHaystack"));
		cHorse.add("Owner", new StringVariable("OwnerName"));
		cHorse.add("Variant", new HorseVariantVariable());

		NBTUnboundVariableContainer cChestedHorse = new NBTUnboundVariableContainer("ChestedHorse", cHorse);
		cChestedHorse.add("Chested", new BooleanVariable("ChestedHorse"));

		NBTUnboundVariableContainer cLlama = new NBTUnboundVariableContainer("Llama", cBreed);
		cLlama.add("Chested", new BooleanVariable("ChestedHorse"));
		cLlama.add("Tamed", new BooleanVariable("Tame"));
		cLlama.add("Eating", new BooleanVariable("EatingHaystack"));
		cLlama.add("Variant", new IntegerVariable("Variant", 0, 3));
		cLlama.add("Strength", new IntegerVariable("Strength", 0, 5));

		NBTUnboundVariableContainer cSkeletonHorse = new NBTUnboundVariableContainer("SkeletonHorse", cHorse);
		cSkeletonHorse.add("Trap", new BooleanVariable("SkeletonTrap"));
		cSkeletonHorse.add("TrapTime", new IntegerVariable("SkeletonTrapTime", 0));

		NBTUnboundVariableContainer cPig = new NBTUnboundVariableContainer("Pig", cBreed);
		cPig.add("Saddle", new BooleanVariable("Saddle"));

		NBTUnboundVariableContainer cRabbit = new NBTUnboundVariableContainer("Rabbit", cBreed);
		cRabbit.add("Type", new IntegerVariable("RabbitType", 0, 99));

		NBTUnboundVariableContainer cSheep = new NBTUnboundVariableContainer("Sheep", cBreed);
		cSheep.add("Sheared", new BooleanVariable("Sheared"));
		cSheep.add("Color", new ByteVariable("Color", (byte) 0, (byte) 15));

		NBTUnboundVariableContainer cVillager = new NBTUnboundVariableContainer("Villager", cBreed);
		cVillager.add("Career", new VillagerCareerVariable());
		cVillager.add("CareerLevel", new IntegerVariable("CareerLevel", 0));
		cVillager.add("Willing", new BooleanVariable("Willing")); // XXX: not effect?
		cVillager.add("Offers", new VillagerOffersVariable());
		// TODO: add villager inventory

		NBTUnboundVariableContainer cFox = new NBTUnboundVariableContainer("Fox", cBreed);
		// XXX: missing TrustedUUIDs
		cFox.add("Sleeping", new BooleanVariable("Sleeping"));
		cFox.add("Type", new StringVariable("Type")); // TODO: add list of possibilities (red, snow)
		cFox.add("Sitting", new BooleanVariable("Sitting"));
		cFox.add("Crouching", new BooleanVariable("Crouching"));

		NBTUnboundVariableContainer cBee = new NBTUnboundVariableContainer("Bee", cBreed);
		cBee.add("AngerTime", new IntegerVariable("AngerTime"));
		cBee.add("CannotEnterHiveTicks", new IntegerVariable("CannotEnterHiveTicks"));
		cBee.add("CropsGrownSincePollination", new IntegerVariable("CropsGrownSincePollination"));
		cBee.add("TicksSincePollination", new IntegerVariable("TicksSincePollination"));
		cBee.add("HasNectar", new BooleanVariable("HasNectar"));
		cBee.add("HasStung", new BooleanVariable("HasStung"));
		// TODO: add FlowerPos / HivePos

		NBTUnboundVariableContainer cHoglin = new NBTUnboundVariableContainer("Hoglin", cBreed);
		cHoglin.add("IsImmuneToZombification", new BooleanVariable("IsImmuneToZombification"));
		cHoglin.add("CannotBeHunted", new BooleanVariable("CannotBeHunted"));
		cHoglin.add("TimeInOverworld", new IntegerVariable("TimeInOverworld"));

		NBTUnboundVariableContainer cAxolotl = new NBTUnboundVariableContainer("Axolotl", cBreed);
		cAxolotl.add("FromBucket", new BooleanVariable("FromBucket"));
		cAxolotl.add("Variant", new IntegerVariable("Variant", 0, 4));

		NBTUnboundVariableContainer cGoat = new NBTUnboundVariableContainer("Goat", cBreed);
		cGoat.add("IsScreamingGoat", new BooleanVariable("IsScreamingGoat"));
		if (BukkitVersion.isVersion(19)) {
			cGoat.add("HasLeftHorn", new BooleanVariable("HasLeftHorn"));
			cGoat.add("HasRightHorn", new BooleanVariable("HasRightHorn"));
		}

		// Tameable SubTypes

		NBTUnboundVariableContainer cOcelot = new NBTUnboundVariableContainer("Ocelot", cTameable);
		cOcelot.add("Type", new IntegerVariable("CatType", 0, 3));

		NBTUnboundVariableContainer cParrot = new NBTUnboundVariableContainer("Parrot", cTameable);
		cParrot.add("Variant", new IntegerVariable("Variant", 0, 4));

		NBTUnboundVariableContainer cWolf = new NBTUnboundVariableContainer("Wolf", cTameable);
		cWolf.add("AngerTime", new IntegerVariable("AngerTime"));
		cWolf.add("CollarColor", new ByteVariable("CollarColor", (byte) 0, (byte) 15));

		NBTUnboundVariableContainer cCat = new NBTUnboundVariableContainer("Cat", cTameable);
		cCat.add("CatType", new IntegerVariable("CatType", (byte) -1, (byte) 10)); // -1 = random type
		cCat.add("CollarColor", new IntegerVariable("CollarColor", (byte) 0, (byte) 15));

		// Fish types

		NBTUnboundVariableContainer cPufferfish = new NBTUnboundVariableContainer("Pufferfish", cFish);
		cPufferfish.add("PuffState", new IntegerVariable("PuffState", 0, 2));

		NBTUnboundVariableContainer cTropicalFish = new NBTUnboundVariableContainer("TropicalFish", cFish);
		cTropicalFish.add("Variant", new IntegerVariable("Variant"));

		// Mob Entities

		ENTITY_VARIABLES.put("minecraft:bat", cMob);
		ENTITY_VARIABLES.put("minecraft:blaze", cMob);
		ENTITY_VARIABLES.put("minecraft:cave_spider", cMob);
		ENTITY_VARIABLES.put("minecraft:chicken", cChicken);
		ENTITY_VARIABLES.put("minecraft:cow", cBreed);
		ENTITY_VARIABLES.put("minecraft:creeper", cCreeper);
		ENTITY_VARIABLES.put("minecraft:dolphin", cDolphin);
		ENTITY_VARIABLES.put("minecraft:donkey", cChestedHorse);
		ENTITY_VARIABLES.put("minecraft:drowned", cZombie);
		ENTITY_VARIABLES.put("minecraft:elder_guardian", cMob);
		ENTITY_VARIABLES.put("minecraft:ender_dragon", cEnderDragon);
		ENTITY_VARIABLES.put("minecraft:enderman", cEnderman);
		ENTITY_VARIABLES.put("minecraft:endermite", cEndermite);
		ENTITY_VARIABLES.put("minecraft:evoker", cEvocationIllager);
		ENTITY_VARIABLES.put("minecraft:ghast", cGhast);
		ENTITY_VARIABLES.put("minecraft:giant", cMob);
		ENTITY_VARIABLES.put("minecraft:guardian", cMob);
		ENTITY_VARIABLES.put("minecraft:horse", cHorse);
		ENTITY_VARIABLES.put("minecraft:husk", cZombie);
		ENTITY_VARIABLES.put("minecraft:illusioner", cEvocationIllager);
		ENTITY_VARIABLES.put("minecraft:llama", cLlama);
		ENTITY_VARIABLES.put("minecraft:magma_cube", cSlime);
		ENTITY_VARIABLES.put("minecraft:mooshroom", cBreed);
		ENTITY_VARIABLES.put("minecraft:mule", cChestedHorse);
		ENTITY_VARIABLES.put("minecraft:ocelot", cOcelot);
		ENTITY_VARIABLES.put("minecraft:parrot", cParrot);
		ENTITY_VARIABLES.put("minecraft:phantom", cPhantom);
		ENTITY_VARIABLES.put("minecraft:pig", cPig);
		ENTITY_VARIABLES.put("minecraft:polar_bear", cBreed);
		ENTITY_VARIABLES.put("minecraft:rabbit", cRabbit);
		ENTITY_VARIABLES.put("minecraft:sheep", cSheep);
		ENTITY_VARIABLES.put("minecraft:shulker", cShulker);
		ENTITY_VARIABLES.put("minecraft:silverfish", cMob);
		ENTITY_VARIABLES.put("minecraft:skeleton", cMob);
		ENTITY_VARIABLES.put("minecraft:skeleton_horse", cSkeletonHorse);
		ENTITY_VARIABLES.put("minecraft:slime", cSlime);
		ENTITY_VARIABLES.put("minecraft:snow_golem", cSnowman);
		ENTITY_VARIABLES.put("minecraft:spider", cMob);
		ENTITY_VARIABLES.put("minecraft:squid", cMob);
		ENTITY_VARIABLES.put("minecraft:stray", cMob);
		ENTITY_VARIABLES.put("minecraft:turtle", cTurtle);
		ENTITY_VARIABLES.put("minecraft:vex", cVex);
		ENTITY_VARIABLES.put("minecraft:villager", cVillager);
		ENTITY_VARIABLES.put("minecraft:iron_golem", cVillagerGolem);
		ENTITY_VARIABLES.put("minecraft:vindicator", cVindicationIllager);
		ENTITY_VARIABLES.put("minecraft:witch", cMob);
		ENTITY_VARIABLES.put("minecraft:wither", cWither);
		ENTITY_VARIABLES.put("minecraft:wither_skeleton", cMob);
		ENTITY_VARIABLES.put("minecraft:wolf", cWolf);
		ENTITY_VARIABLES.put("minecraft:zombie", cZombie);
		ENTITY_VARIABLES.put("minecraft:zombie_horse", cHorse);
		if (!BukkitVersion.isVersion(16)) {
			ENTITY_VARIABLES.put("minecraft:zombie_pigman", cZombiePigman);
		}
		ENTITY_VARIABLES.put("minecraft:zombie_villager", cZombieVillager);
		ENTITY_VARIABLES.put("minecraft:cod", cFish);
		ENTITY_VARIABLES.put("minecraft:salmon", cFish);
		ENTITY_VARIABLES.put("minecraft:pufferfish", cPufferfish);
		ENTITY_VARIABLES.put("minecraft:tropical_fish", cTropicalFish);

		// TODO: add variables for 1.14 mobs
		if (BukkitVersion.isVersion(14)) {
			ENTITY_VARIABLES.put("minecraft:pillager", cMob);
			ENTITY_VARIABLES.put("minecraft:panda", cMob);
			ENTITY_VARIABLES.put("minecraft:wandering_trader", cMob);
			ENTITY_VARIABLES.put("minecraft:trader_llama", cMob);
			ENTITY_VARIABLES.put("minecraft:fox", cFox);
			ENTITY_VARIABLES.put("minecraft:cat", cCat);
			ENTITY_VARIABLES.put("minecraft:ravager", cMob);
		}

		if (BukkitVersion.isVersion(15)) {
			ENTITY_VARIABLES.put("minecraft:bee", cBee);
		}

		if (BukkitVersion.isVersion(16)) {
			ENTITY_VARIABLES.put("minecraft:piglin", cPiglin);
			ENTITY_VARIABLES.put("minecraft:piglin_brute", cMob);
			ENTITY_VARIABLES.put("minecraft:zombified_piglin", cZombifiedPiglin);
			ENTITY_VARIABLES.put("minecraft:zoglin", cZoglin);
			ENTITY_VARIABLES.put("minecraft:hoglin", cHoglin);
			ENTITY_VARIABLES.put("minecraft:strider", cPig);
		}

		if (BukkitVersion.isVersion(17)) {
			ENTITY_VARIABLES.put("minecraft:axolotl", cAxolotl);
			ENTITY_VARIABLES.put("minecraft:goat", cGoat);
			ENTITY_VARIABLES.put("minecraft:glow_squid", cMob);
		}

		// Projectile Entities

		NBTUnboundVariableContainer cArrow = new NBTUnboundVariableContainer("Arrow", cEntity);
		cArrow.add("Pickup", new ByteVariable("pickup", (byte) 0, (byte) 2));
		cArrow.add("Player", new BooleanVariable("player"));
		cArrow.add("Life", new ShortVariable("life"));
		cArrow.add("Damage", new DoubleVariable("damage"));
		cArrow.add("Critical", new BooleanVariable("crit"));

		NBTUnboundVariableContainer cTippedArrow = new NBTUnboundVariableContainer("TippedArrow", cArrow);
		cTippedArrow.add("Potion", new StringVariable("Potion"));
		cTippedArrow.add("Color", new ColorVariable("Color"));
		cTippedArrow.add("CustomPotionEffects", new EffectsVariable("CustomPotionEffects"));

		NBTUnboundVariableContainer cSpectralArrow = new NBTUnboundVariableContainer("SpectralArrow", cArrow);
		cSpectralArrow.add("Duration", new IntegerVariable("Duration", 0));

		NBTUnboundVariableContainer cFireball = new NBTUnboundVariableContainer("Fireball", cEntity);
		cFireball.add("Direction", new VectorVariable("direction"));
		cFireball.add("Power", new VectorVariable("power"));

		NBTUnboundVariableContainer cLargeFireball = new NBTUnboundVariableContainer("LargeFireball", cFireball);
		cLargeFireball.add("ExplosionPower", new IntegerVariable("ExplosionPower", 0, 25)); // Limited to 25

		NBTUnboundVariableContainer cEnderPearl = new NBTUnboundVariableContainer("EnderPearl", cEntity);
		cEnderPearl.add("Owner", new StringVariable("ownerName"));

		NBTUnboundVariableContainer cPotion = new NBTUnboundVariableContainer("Potion", cEntity);
		cPotion.add("Item", new PotionVariable("Item"));

		NBTUnboundVariableContainer cTrident = new NBTUnboundVariableContainer("Trident", cArrow);
		cTrident.add("Trident", new SingleItemVariable("Trident"));

		ENTITY_VARIABLES.put("minecraft:arrow", cTippedArrow);
		ENTITY_VARIABLES.put("minecraft:dragon_fireball", cFireball);
		ENTITY_VARIABLES.put("minecraft:egg", cEntity);
		ENTITY_VARIABLES.put("minecraft:ender_pearl", cEnderPearl);
		ENTITY_VARIABLES.put("minecraft:fireball", cLargeFireball);
		ENTITY_VARIABLES.put("minecraft:llama_spit", cEntity);
		ENTITY_VARIABLES.put("minecraft:potion", cPotion);
		ENTITY_VARIABLES.put("minecraft:small_fireball", cFireball);
		// ENTITY_VARIABLES.put("minecraft:shulker_bullet", cEntity);
		ENTITY_VARIABLES.put("minecraft:snowball", cEntity);
		ENTITY_VARIABLES.put("minecraft:spectral_arrow", cSpectralArrow);
		ENTITY_VARIABLES.put("minecraft:trident", cTrident);
		ENTITY_VARIABLES.put("minecraft:wither_skull", cFireball);
		ENTITY_VARIABLES.put("minecraft:experience_bottle", cEntity);

		// Item Entities

		NBTUnboundVariableContainer cItem = new NBTUnboundVariableContainer("Item", cEntity);
		cItem.add("Age", new ShortVariable("Age"));

		NBTUnboundVariableContainer cDroppedItem = new NBTUnboundVariableContainer("DroppedItem", cItem);
		cDroppedItem.add("Health", new ShortVariable("Health"));
		cDroppedItem.add("PickupDelay", new ShortVariable("PickupDelay"));
		cDroppedItem.add("Item", new SingleItemVariable("Item"));

		NBTUnboundVariableContainer cXPOrb = new NBTUnboundVariableContainer("XPOrb", cItem);
		cXPOrb.add("Health", new ByteVariable("Health"));
		cXPOrb.add("Value", new ShortVariable("Value"));

		ENTITY_VARIABLES.put("minecraft:item", cDroppedItem);
		ENTITY_VARIABLES.put("minecraft:experience_orb", cXPOrb);

		// Vehicle Entities

		NBTUnboundVariableContainer cBoat = new NBTUnboundVariableContainer("Boat", cEntity);
		cBoat.add("Type", new StringVariable("Type"));

		NBTUnboundVariableContainer cMinecart = new NBTUnboundVariableContainer("Minecart", cEntity);
		cMinecart.add("DisplayTile", new BooleanVariable("CustomDisplayTile"));
		cMinecart.add("DisplayState", new BlockStateVariable("DisplayState"));
		cMinecart.add("TileOffset", new IntegerVariable("DisplayOffset"));
		cMinecart.add("Name", new StringVariable("CustomName"));

		NBTUnboundVariableContainer cChestMinecart = new NBTUnboundVariableContainer("ChestMinecart", cMinecart);
		cChestMinecart.add("Items", new ContainerVariable("Items", 27));
		cChestMinecart.add("LootTable", new StringVariable("LootTable"));
		cChestMinecart.add("LootTableSeed", new LongVariable("LootTableSeed"));

		NBTUnboundVariableContainer cCommandBlockMinecart = new NBTUnboundVariableContainer("CommandBlockMinecart", cMinecart);
		cCommandBlockMinecart.add("Command", new StringVariable("Command"));

		NBTUnboundVariableContainer cHopperMinecart = new NBTUnboundVariableContainer("HopperMinecart", cMinecart);
		cHopperMinecart.add("Items", new ContainerVariable("Items", 5));
		cHopperMinecart.add("LootTable", new StringVariable("LootTable"));
		cHopperMinecart.add("LootTableSeed", new LongVariable("LootTableSeed"));

		NBTUnboundVariableContainer cSpawnerMinecart = new NBTUnboundVariableContainer("SpawnerMinecart", cMinecart);
		cSpawnerMinecart.add("Count", new ShortVariable("SpawnCount", (short) 0));
		cSpawnerMinecart.add("Range", new ShortVariable("SpawnRange", (short) 0));
		cSpawnerMinecart.add("Delay", new ShortVariable("Delay", (short) 0));
		cSpawnerMinecart.add("MinDelay", new ShortVariable("MinSpawnDelay", (short) 0));
		cSpawnerMinecart.add("MaxDelay", new ShortVariable("MaxSpawnDelay", (short) 0));
		cSpawnerMinecart.add("MaxEntities", new ShortVariable("MaxNearbyEntities", (short) 0));
		cSpawnerMinecart.add("PlayerRange", new ShortVariable("RequiredPlayerRange", (short) 0));

		ENTITY_VARIABLES.put("minecraft:boat", cBoat);
		ENTITY_VARIABLES.put("minecraft:minecart", cMinecart);
		ENTITY_VARIABLES.put("minecraft:chest_minecart", cChestMinecart);
		ENTITY_VARIABLES.put("minecraft:command_block_minecart", cCommandBlockMinecart);
		ENTITY_VARIABLES.put("minecraft:furnace_minecart", cMinecart);
		ENTITY_VARIABLES.put("minecraft:hopper_minecart", cHopperMinecart);
		ENTITY_VARIABLES.put("minecraft:spawner_minecart", cSpawnerMinecart);
		ENTITY_VARIABLES.put("minecraft:tnt_minecart", cMinecart);

		// Dynamic Entities

		NBTUnboundVariableContainer cFallingBlock = new NBTUnboundVariableContainer("FallingBlock", cEntity);
		cFallingBlock.add("BlockState", new BlockStateVariable("BlockState"));
		cFallingBlock.add("Time", new ByteVariable("Time", (byte)0));
		cFallingBlock.add("DropItem", new BooleanVariable("DropItem"));
		cFallingBlock.add("HurtEntities", new BooleanVariable("HurtEntities"));
		cFallingBlock.add("FallHurtAmount", new FloatVariable("FallHurtAmount", 0));
		cFallingBlock.add("FallHurtMax", new IntegerVariable("FallHurtMax", 0));

		NBTUnboundVariableContainer cTNT = new NBTUnboundVariableContainer("TNT", cEntity);
		cTNT.add("Fuse", new ByteVariable("Fuse", (byte) 0));

		ENTITY_VARIABLES.put("minecraft:falling_block", cFallingBlock);
		ENTITY_VARIABLES.put("minecraft:tnt", cTNT);

		// Other Entities

		NBTUnboundVariableContainer cAreaEffectCloud = new NBTUnboundVariableContainer("AreaEffectCloud", cEntity);
		cAreaEffectCloud.add("Age", new IntegerVariable("Age", 0));
		cAreaEffectCloud.add("Color", new ColorVariable("Color"));
		cAreaEffectCloud.add("Duration", new IntegerVariable("Duration", 0));
		cAreaEffectCloud.add("ReapplicationDelay", new IntegerVariable("ReapplicationDelay", 0));
		cAreaEffectCloud.add("WaitTime", new IntegerVariable("WaitTime", 0));
		cAreaEffectCloud.add("Radius", new FloatVariable("Radius", 0f));
		cAreaEffectCloud.add("RadiusOnUse", new FloatVariable("RadiusOnUse"));
		cAreaEffectCloud.add("RadiusPerTick", new FloatVariable("RadiusPerTick"));
		cAreaEffectCloud.add("Particle", new ParticleVariable("Particle"));
		cAreaEffectCloud.add("ParticleParam1", new IntegerVariable("ParticleParam1"));
		cAreaEffectCloud.add("ParticleParam2", new IntegerVariable("ParticleParam2"));
		cAreaEffectCloud.add("Potion", new StringVariable("Potion"));
		cAreaEffectCloud.add("Effects", new EffectsVariable("Effects"));

		NBTUnboundVariableContainer cArmorStand = new NBTUnboundVariableContainer("ArmorStand", cEquippable);
		cArmorStand.add("Marker", new BooleanVariable("Marker"));
		cArmorStand.add("Invisible", new BooleanVariable("Invisible"));
		cArmorStand.add("NoBasePlate", new BooleanVariable("NoBasePlate"));
		cArmorStand.add("ShowArms", new BooleanVariable("ShowArms"));
		cArmorStand.add("Small", new BooleanVariable("Small"));
		cArmorStand.add("PoseBody", new RotationVariable("Pose/Body", true));
		cArmorStand.add("PoseLeftArm", new RotationVariable("Pose/LeftArm", true));
		cArmorStand.add("PoseRightArm", new RotationVariable("Pose/RightArm", true));
		cArmorStand.add("PoseLeftLeg", new RotationVariable("Pose/LeftLeg", true));
		cArmorStand.add("PoseRightLeg", new RotationVariable("Pose/RightLeg", true));
		cArmorStand.add("PoseHead", new RotationVariable("Pose/Head", true));

		NBTUnboundVariableContainer cEnderCrystal = new NBTUnboundVariableContainer("EnderCrystal", cEntity);
		cEnderCrystal.add("ShowBottom", new BooleanVariable("ShowBottom"));

		NBTUnboundVariableContainer cEvocationFangs = new NBTUnboundVariableContainer("EvocationFangs", cEntity);
		cEvocationFangs.add("Warmup", new IntegerVariable("Warmup"));

		NBTUnboundVariableContainer cFireworksRocket = new NBTUnboundVariableContainer("FireworksRocket", cEntity);
		cFireworksRocket.add("Life", new IntegerVariable("Life", 0, 200)); // Limited to 200
		cFireworksRocket.add("Lifetime", new IntegerVariable("LifeTime", 0, 200)); // Limited to 200
		cFireworksRocket.add("FireworksItem", new FireworksItemVariable());

		ENTITY_VARIABLES.put("minecraft:area_effect_cloud", cAreaEffectCloud);
		ENTITY_VARIABLES.put("minecraft:armor_stand", cArmorStand);
		ENTITY_VARIABLES.put("minecraft:end_crystal", cEnderCrystal);
		ENTITY_VARIABLES.put("minecraft:evoker_fangs", cEvocationFangs);
		// ENTITY_VARIABLES.put("minecraft:eye_of_ender_signal", cEntity);
		ENTITY_VARIABLES.put("minecraft:firework_rocket", cFireworksRocket);
		// ENTITY_VARIABLES.put("minecraft:item_frame", cEntity);
		// ENTITY_VARIABLES.put("minecraft:leash_knot", cEntity);
		// ENTITY_VARIABLES.put("minecraft:painting", cEntity);

	}

	@Deprecated
	public BookOfSouls _bos = null;
	@Deprecated
	@Override
	public void save() {
		if (_bos != null) {
			_bos.saveBook();
		}
	}

	static EntityNBT newInstance(EntityType type, NBTTagCompound data) {
		NBTUnboundVariableContainer c = ENTITY_VARIABLES.get(EntityTypeMap.getName(type));
		if (c != null) {
			EntityNBT instance = null;
			// special case for mobs
			do {
				if (c.getName() == "Mob") {
					instance = new MobNBT(type);
					break;
				}
				c = c.getParent();
			} while (c != null);
			if (instance == null) {
				// special case for some specific entities
				switch (type) {
				case FALLING_BLOCK:
					instance = new FallingBlockNBT();
					break;
				case MINECART_MOB_SPAWNER:
					instance =  new MinecartSpawnerNBT();
					break;
				default:
					instance =  new EntityNBT(type);
					break;
				}
			}
			if (data != null) {
				instance._data = data;
			} else {
				instance.setDefautData();
			}
			instance._data.setString("id", EntityTypeMap.getName(type));
			instance.backwardCompatibility();
			return instance;
		}
		return null;
	}

	public static boolean isValidTypeName(String type) {
		return ENTITY_VARIABLES.containsKey(type);
	}

	public static boolean isValidType(EntityType type) {
		return isValidTypeName(EntityTypeMap.getName(type));
	}

	public static List<String> getValidTypeNames() {
		return Collections.unmodifiableList(new ArrayList<String>(ENTITY_VARIABLES.keySet()));
	}

	protected EntityNBT(EntityType type) {
		super(type);
	}

	@Override
	protected NBTUnboundVariableContainer getVariableContainer(String id) {
		return ENTITY_VARIABLES.get(id);
	}

	@Override
	public void setDefautData() {
		switch (getId()) {
		case "minecraft:dragon_fireball":
		case "minecraft:fireball":
		case "minecraft:small_fireball":
		case "minecraft:wither_skull":
			_data.setList("direction", 0.0d, 0.0d, 0.0d);
			_data.setList("power", 0.0d, 0.0d, 0.0d);
			break;
		case "minecraft:falling_block":
			_data.setByte("Time", (byte) 1);
			break;
		case "minecraft:area_effect_cloud":
			_data.setInt("Duration", 500);
			_data.setInt("ReapplicationDelay", 10);
			_data.setInt("Radius", 3);
			break;
		}
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

}
