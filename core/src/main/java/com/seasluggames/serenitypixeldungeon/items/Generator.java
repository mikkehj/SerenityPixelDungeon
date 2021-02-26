/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Serenity Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package com.seasluggames.serenitypixeldungeon.items;

import com.seasluggames.serenitypixeldungeon.Dungeon;
import com.seasluggames.serenitypixeldungeon.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.items.armor.ClothArmor;
import com.seasluggames.serenitypixeldungeon.items.armor.LeatherArmor;
import com.seasluggames.serenitypixeldungeon.items.armor.MailArmor;
import com.seasluggames.serenitypixeldungeon.items.armor.PlateArmor;
import com.seasluggames.serenitypixeldungeon.items.armor.ScaleArmor;
import com.seasluggames.serenitypixeldungeon.items.artifacts.AlchemistsToolkit;
import com.seasluggames.serenitypixeldungeon.items.artifacts.Artifact;
import com.seasluggames.serenitypixeldungeon.items.artifacts.CapeOfThorns;
import com.seasluggames.serenitypixeldungeon.items.artifacts.ChaliceOfBlood;
import com.seasluggames.serenitypixeldungeon.items.artifacts.CloakOfShadows;
import com.seasluggames.serenitypixeldungeon.items.artifacts.DriedRose;
import com.seasluggames.serenitypixeldungeon.items.artifacts.EtherealChains;
import com.seasluggames.serenitypixeldungeon.items.artifacts.HornOfPlenty;
import com.seasluggames.serenitypixeldungeon.items.artifacts.LloydsBeacon;
import com.seasluggames.serenitypixeldungeon.items.artifacts.MasterThievesArmband;
import com.seasluggames.serenitypixeldungeon.items.artifacts.SandalsOfNature;
import com.seasluggames.serenitypixeldungeon.items.artifacts.TalismanOfForesight;
import com.seasluggames.serenitypixeldungeon.items.artifacts.TimekeepersHourglass;
import com.seasluggames.serenitypixeldungeon.items.artifacts.UnstableSpellbook;
import com.seasluggames.serenitypixeldungeon.items.bags.Bag;
import com.seasluggames.serenitypixeldungeon.items.food.Food;
import com.seasluggames.serenitypixeldungeon.items.food.MysteryMeat;
import com.seasluggames.serenitypixeldungeon.items.food.Pasty;
import com.seasluggames.serenitypixeldungeon.items.potions.Potion;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfExperience;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfFrost;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfHaste;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfHealing;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfInvisibility;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfLevitation;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfLiquidFlame;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfMindVision;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfParalyticGas;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfPurity;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfStrength;
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfToxicGas;
import com.seasluggames.serenitypixeldungeon.items.rings.Ring;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfAccuracy;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfElements;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfEnergy;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfEvasion;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfForce;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfFuror;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfHaste;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfMight;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfSharpshooting;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfTenacity;
import com.seasluggames.serenitypixeldungeon.items.rings.RingOfWealth;
import com.seasluggames.serenitypixeldungeon.items.scrolls.Scroll;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfIdentify;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfLullaby;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfMagicMapping;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfMirrorImage;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfRage;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfRecharging;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfRemoveCurse;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfRetribution;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfTerror;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfTransmutation;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfUpgrade;
import com.seasluggames.serenitypixeldungeon.items.stones.Runestone;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfAffection;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfAggression;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfAugmentation;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfBlast;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfBlink;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfClairvoyance;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfDeepenedSleep;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfDisarming;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfEnchantment;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfFlock;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfIntuition;
import com.seasluggames.serenitypixeldungeon.items.stones.StoneOfShock;
import com.seasluggames.serenitypixeldungeon.items.wands.Wand;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfBlastWave;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfCorrosion;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfCorruption;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfDisintegration;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfFireblast;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfFrost;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfLightning;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfLivingEarth;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfMagicMissile;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfPrismaticLight;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfRegrowth;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfTransfusion;
import com.seasluggames.serenitypixeldungeon.items.wands.WandOfWarding;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.AssassinsBlade;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.BattleAxe;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Crossbow;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Dagger;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Dirk;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Flail;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Gauntlet;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Glaive;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Gloves;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Greataxe;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Greatshield;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Greatsword;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.HandAxe;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Longsword;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Mace;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.MagesStaff;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.MeleeWeapon;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Quarterstaff;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.RoundShield;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.RunicBlade;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Sai;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Scimitar;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Shortsword;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Spear;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Sword;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.WarHammer;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.Whip;
import com.seasluggames.serenitypixeldungeon.items.weapon.melee.WornShortsword;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Bolas;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.FishingSpear;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ForceCube;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.HeavyBoomerang;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Javelin;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Kunai;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.MissileWeapon;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Shuriken;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ThrowingClub;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ThrowingHammer;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ThrowingKnife;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ThrowingSpear;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.ThrowingStone;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Tomahawk;
import com.seasluggames.serenitypixeldungeon.items.weapon.missiles.Trident;
import com.seasluggames.serenitypixeldungeon.plants.Blindweed;
import com.seasluggames.serenitypixeldungeon.plants.Dreamfoil;
import com.seasluggames.serenitypixeldungeon.plants.Earthroot;
import com.seasluggames.serenitypixeldungeon.plants.Fadeleaf;
import com.seasluggames.serenitypixeldungeon.plants.Firebloom;
import com.seasluggames.serenitypixeldungeon.plants.Icecap;
import com.seasluggames.serenitypixeldungeon.plants.Plant;
import com.seasluggames.serenitypixeldungeon.plants.Rotberry;
import com.seasluggames.serenitypixeldungeon.plants.Sorrowmoss;
import com.seasluggames.serenitypixeldungeon.plants.Starflower;
import com.seasluggames.serenitypixeldungeon.plants.Stormvine;
import com.seasluggames.serenitypixeldungeon.plants.Sungrass;
import com.seasluggames.serenitypixeldungeon.plants.Swiftthistle;
import com.watabou.utils.Bundle;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class Generator {

	public enum Category {
		WEAPON	( 4,    MeleeWeapon.class),
		WEP_T1	( 0,    MeleeWeapon.class),
		WEP_T2	( 0,    MeleeWeapon.class),
		WEP_T3	( 0,    MeleeWeapon.class),
		WEP_T4	( 0,    MeleeWeapon.class),
		WEP_T5	( 0,    MeleeWeapon.class),
		
		ARMOR	( 3,    Armor.class ),
		
		MISSILE ( 3,    MissileWeapon.class ),
		MIS_T1  ( 0,    MissileWeapon.class ),
		MIS_T2  ( 0,    MissileWeapon.class ),
		MIS_T3  ( 0,    MissileWeapon.class ),
		MIS_T4  ( 0,    MissileWeapon.class ),
		MIS_T5  ( 0,    MissileWeapon.class ),
		
		WAND	( 2,    Wand.class ),
		RING	( 1,    Ring.class ),
		ARTIFACT( 1,    Artifact.class),
		
		FOOD	( 0,    Food.class ),
		
		POTION	( 16,   Potion.class ),
		SEED	( 2,    Plant.Seed.class ),
		
		SCROLL	( 16,   Scroll.class ),
		STONE   ( 2,    Runestone.class),
		
		GOLD	( 20,   Gold.class );
		
		public Class<?>[] classes;

		//some item types use a deck-based system, where the probs decrement as items are picked
		// until they are all 0, and then they reset. Those generator classes should define
		// defaultProbs. If defaultProbs is null then a deck system isn't used.
		//Artifacts in particular don't reset, no duplicates!
		public float[] probs;
		public float[] defaultProbs = null;
		
		public float prob;
		public Class<? extends Item> superClass;
		
		private Category( float prob, Class<? extends Item> superClass ) {
			this.prob = prob;
			this.superClass = superClass;
		}
		
		public static int order( Item item ) {
			for (int i=0; i < values().length; i++) {
				if (values()[i].superClass.isInstance( item )) {
					return i;
				}
			}
			
			return item instanceof Bag ? Integer.MAX_VALUE : Integer.MAX_VALUE - 1;
		}

		static {
			GOLD.classes = new Class<?>[]{
					Gold.class };
			GOLD.probs = new float[]{ 1 };
			
			POTION.classes = new Class<?>[]{
					PotionOfStrength.class, //2 drop every chapter, see Dungeon.posNeeded()
					PotionOfHealing.class,
					PotionOfMindVision.class,
					PotionOfFrost.class,
					PotionOfLiquidFlame.class,
					PotionOfToxicGas.class,
					PotionOfHaste.class,
					PotionOfInvisibility.class,
					PotionOfLevitation.class,
					PotionOfParalyticGas.class,
					PotionOfPurity.class,
					PotionOfExperience.class};
			POTION.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			POTION.probs = POTION.defaultProbs.clone();
			
			SEED.classes = new Class<?>[]{
					Rotberry.Seed.class, //quest item
					Sungrass.Seed.class,
					Fadeleaf.Seed.class,
					Icecap.Seed.class,
					Firebloom.Seed.class,
					Sorrowmoss.Seed.class,
					Swiftthistle.Seed.class,
					Blindweed.Seed.class,
					Stormvine.Seed.class,
					Earthroot.Seed.class,
					Dreamfoil.Seed.class,
					Starflower.Seed.class};
			SEED.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2 };
			SEED.probs = SEED.defaultProbs.clone();
			
			SCROLL.classes = new Class<?>[]{
					ScrollOfUpgrade.class, //3 drop every chapter, see Dungeon.souNeeded()
					ScrollOfIdentify.class,
					ScrollOfRemoveCurse.class,
					ScrollOfMirrorImage.class,
					ScrollOfRecharging.class,
					ScrollOfTeleportation.class,
					ScrollOfLullaby.class,
					ScrollOfMagicMapping.class,
					ScrollOfRage.class,
					ScrollOfRetribution.class,
					ScrollOfTerror.class,
					ScrollOfTransmutation.class
			};
			SCROLL.defaultProbs = new float[]{ 0, 6, 4, 3, 3, 3, 2, 2, 2, 2, 2, 1 };
			SCROLL.probs = SCROLL.defaultProbs.clone();
			
			STONE.classes = new Class<?>[]{
					StoneOfEnchantment.class,   //1 is guaranteed to drop on floors 6-19
					StoneOfIntuition.class,     //1 additional stone is also dropped on floors 1-3
					StoneOfDisarming.class,
					StoneOfFlock.class,
					StoneOfShock.class,
					StoneOfBlink.class,
					StoneOfDeepenedSleep.class,
					StoneOfClairvoyance.class,
					StoneOfAggression.class,
					StoneOfBlast.class,
					StoneOfAffection.class,
					StoneOfAugmentation.class  //1 is sold in each shop
			};
			STONE.defaultProbs = new float[]{ 0, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 0 };
			STONE.probs = STONE.defaultProbs.clone();

			WAND.classes = new Class<?>[]{
					WandOfMagicMissile.class,
					WandOfLightning.class,
					WandOfDisintegration.class,
					WandOfFireblast.class,
					WandOfCorrosion.class,
					WandOfBlastWave.class,
					WandOfLivingEarth.class,
					WandOfFrost.class,
					WandOfPrismaticLight.class,
					WandOfWarding.class,
					WandOfTransfusion.class,
					WandOfCorruption.class,
					WandOfRegrowth.class };
			WAND.probs = new float[]{ 4, 4, 4, 4, 4, 3, 3, 3, 3, 3, 3, 3, 3 };
			
			//see generator.randomWeapon
			WEAPON.classes = new Class<?>[]{};
			WEAPON.probs = new float[]{};
			
			WEP_T1.classes = new Class<?>[]{
					WornShortsword.class,
					Gloves.class,
					Dagger.class,
					MagesStaff.class
			};
			WEP_T1.probs = new float[]{ 1, 1, 1, 0 };
			
			WEP_T2.classes = new Class<?>[]{
					Shortsword.class,
					HandAxe.class,
					Spear.class,
					Quarterstaff.class,
					Dirk.class
			};
			WEP_T2.probs = new float[]{ 6, 5, 5, 4, 4 };
			
			WEP_T3.classes = new Class<?>[]{
					Sword.class,
					Mace.class,
					Scimitar.class,
					RoundShield.class,
					Sai.class,
					Whip.class
			};
			WEP_T3.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T4.classes = new Class<?>[]{
					Longsword.class,
					BattleAxe.class,
					Flail.class,
					RunicBlade.class,
					AssassinsBlade.class,
					Crossbow.class
			};
			WEP_T4.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			WEP_T5.classes = new Class<?>[]{
					Greatsword.class,
					WarHammer.class,
					Glaive.class,
					Greataxe.class,
					Greatshield.class,
					Gauntlet.class
			};
			WEP_T5.probs = new float[]{ 6, 5, 5, 4, 4, 4 };
			
			//see Generator.randomArmor
			ARMOR.classes = new Class<?>[]{
					ClothArmor.class,
					LeatherArmor.class,
					MailArmor.class,
					ScaleArmor.class,
					PlateArmor.class };
			ARMOR.probs = new float[]{ 0, 0, 0, 0, 0 };
			
			//see Generator.randomMissile
			MISSILE.classes = new Class<?>[]{};
			MISSILE.probs = new float[]{};
			
			MIS_T1.classes = new Class<?>[]{
					ThrowingStone.class,
					ThrowingKnife.class
			};
			MIS_T1.probs = new float[]{ 6, 5 };
			
			MIS_T2.classes = new Class<?>[]{
					FishingSpear.class,
					ThrowingClub.class,
					Shuriken.class
			};
			MIS_T2.probs = new float[]{ 6, 5, 4 };
			
			MIS_T3.classes = new Class<?>[]{
					ThrowingSpear.class,
					Kunai.class,
					Bolas.class
			};
			MIS_T3.probs = new float[]{ 6, 5, 4 };
			
			MIS_T4.classes = new Class<?>[]{
					Javelin.class,
					Tomahawk.class,
					HeavyBoomerang.class
			};
			MIS_T4.probs = new float[]{ 6, 5, 4 };
			
			MIS_T5.classes = new Class<?>[]{
					Trident.class,
					ThrowingHammer.class,
					ForceCube.class
			};
			MIS_T5.probs = new float[]{ 6, 5, 4 };
			
			FOOD.classes = new Class<?>[]{
					Food.class,
					Pasty.class,
					MysteryMeat.class };
			FOOD.probs = new float[]{ 4, 1, 0 };
			
			RING.classes = new Class<?>[]{
					RingOfAccuracy.class,
					RingOfEvasion.class,
					RingOfElements.class,
					RingOfForce.class,
					RingOfFuror.class,
					RingOfHaste.class,
					RingOfEnergy.class,
					RingOfMight.class,
					RingOfSharpshooting.class,
					RingOfTenacity.class,
					RingOfWealth.class};
			RING.probs = new float[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 };
			
			ARTIFACT.classes = new Class<?>[]{
					CapeOfThorns.class,
					ChaliceOfBlood.class,
					CloakOfShadows.class,
					HornOfPlenty.class,
					MasterThievesArmband.class,
					SandalsOfNature.class,
					TalismanOfForesight.class,
					TimekeepersHourglass.class,
					UnstableSpellbook.class,
					AlchemistsToolkit.class,
					DriedRose.class,
					LloydsBeacon.class,
					EtherealChains.class
			};
			ARTIFACT.defaultProbs = new float[]{ 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1};
			ARTIFACT.probs = ARTIFACT.defaultProbs.clone();
		}
	}

	private static final float[][] floorSetTierProbs = new float[][] {
			{0, 75, 20,  4,  1},
			{0, 25, 50, 20,  5},
			{0,  0, 40, 50, 10},
			{0,  0, 20, 40, 40},
			{0,  0,  0, 20, 80}
	};
	
	private static HashMap<Category,Float> categoryProbs = new LinkedHashMap<>();

	public static void fullReset() {
		generalReset();
		for (Category cat : Category.values()) {
			reset(cat);
		}
	}

	public static void generalReset(){
		for (Category cat : Category.values()) {
			categoryProbs.put( cat, cat.prob );
		}
	}

	public static void reset(Category cat){
		if (cat.defaultProbs != null) cat.probs = cat.defaultProbs.clone();
	}
	
	public static Item random() {
		Category cat = Random.chances( categoryProbs );
		if (cat == null){
			generalReset();
			cat = Random.chances( categoryProbs );
		}
		categoryProbs.put( cat, categoryProbs.get( cat ) - 1);
		return random( cat );
	}
	
	public static Item random( Category cat ) {
		switch (cat) {
			case ARMOR:
				return randomArmor();
			case WEAPON:
				return randomWeapon();
			case MISSILE:
				return randomMissile();
			case ARTIFACT:
				Item item = randomArtifact();
				//if we're out of artifacts, return a ring instead.
				return item != null ? item : random(Category.RING);
			default:
				int i = Random.chances(cat.probs);
				if (i == -1) {
					reset(cat);
					i = Random.chances(cat.probs);
				}
				if (cat.defaultProbs != null) cat.probs[i]--;
				return ((Item) Reflection.newInstance(cat.classes[i])).random();
		}
	}

	//overrides any deck systems and always uses default probs
	public static Item randomUsingDefaults( Category cat ){
		if (cat.defaultProbs == null) {
			return random(cat); //currently covers weapons/armor/missiles
		} else {
			return ((Item) Reflection.newInstance(cat.classes[Random.chances(cat.defaultProbs)])).random();
		}
	}
	
	public static Item random( Class<? extends Item> cl ) {
		return Reflection.newInstance(cl).random();
	}

	public static Armor randomArmor(){
		return randomArmor(Dungeon.depth / 5);
	}
	
	public static Armor randomArmor(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Armor a = (Armor)Reflection.newInstance(Category.ARMOR.classes[Random.chances(floorSetTierProbs[floorSet])]);
		a.random();
		return a;
	}

	public static final Category[] wepTiers = new Category[]{
			Category.WEP_T1,
			Category.WEP_T2,
			Category.WEP_T3,
			Category.WEP_T4,
			Category.WEP_T5
	};

	public static MeleeWeapon randomWeapon(){
		return randomWeapon(Dungeon.depth / 5);
	}
	
	public static MeleeWeapon randomWeapon(int floorSet) {

		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = wepTiers[Random.chances(floorSetTierProbs[floorSet])];
		MeleeWeapon w = (MeleeWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}
	
	public static final Category[] misTiers = new Category[]{
			Category.MIS_T1,
			Category.MIS_T2,
			Category.MIS_T3,
			Category.MIS_T4,
			Category.MIS_T5
	};
	
	public static MissileWeapon randomMissile(){
		return randomMissile(Dungeon.depth / 5);
	}
	
	public static MissileWeapon randomMissile(int floorSet) {
		
		floorSet = (int)GameMath.gate(0, floorSet, floorSetTierProbs.length-1);
		
		Category c = misTiers[Random.chances(floorSetTierProbs[floorSet])];
		MissileWeapon w = (MissileWeapon)Reflection.newInstance(c.classes[Random.chances(c.probs)]);
		w.random();
		return w;
	}

	//enforces uniqueness of artifacts throughout a run.
	public static Artifact randomArtifact() {

		Category cat = Category.ARTIFACT;
		int i = Random.chances( cat.probs );

		//if no artifacts are left, return null
		if (i == -1){
			return null;
		}

		cat.probs[i]--;
		return (Artifact) Reflection.newInstance((Class<? extends Artifact>) cat.classes[i]).random();

	}

	public static boolean removeArtifact(Class<?extends Artifact> artifact) {
		Category cat = Category.ARTIFACT;
		for (int i = 0; i < cat.classes.length; i++){
			if (cat.classes[i].equals(artifact) && cat.probs[i] > 0) {
				cat.probs[i] = 0;
				return true;
			}
		}
		return false;
	}

	private static final String GENERAL_PROBS = "general_probs";
	private static final String CATEGORY_PROBS = "_probs";
	
	public static void storeInBundle(Bundle bundle) {
		Float[] genProbs = categoryProbs.values().toArray(new Float[0]);
		float[] storeProbs = new float[genProbs.length];
		for (int i = 0; i < storeProbs.length; i++){
			storeProbs[i] = genProbs[i];
		}
		bundle.put( GENERAL_PROBS, storeProbs);

		for (Category cat : Category.values()){
			if (cat.defaultProbs == null) continue;
			boolean needsStore = false;
			for (int i = 0; i < cat.probs.length; i++){
				if (cat.probs[i] != cat.defaultProbs[i]){
					needsStore = true;
					break;
				}
			}

			if (needsStore){
				bundle.put(cat.name().toLowerCase() + CATEGORY_PROBS, cat.probs);
			}
		}
	}

	public static void restoreFromBundle(Bundle bundle) {
		fullReset();

		if (bundle.contains(GENERAL_PROBS)){
			float[] probs = bundle.getFloatArray(GENERAL_PROBS);
			for (int i = 0; i < probs.length; i++){
				categoryProbs.put(Category.values()[i], probs[i]);
			}
		}

		for (Category cat : Category.values()){
			if (bundle.contains(cat.name().toLowerCase() + CATEGORY_PROBS)){
				float[] probs = bundle.getFloatArray(cat.name().toLowerCase() + CATEGORY_PROBS);
				if (cat.defaultProbs != null && probs.length == cat.defaultProbs.length){
					cat.probs = probs;
				}
			}
		}

		//pre-0.8.1
		if (bundle.contains("spawned_artifacts")) {
			for (Class<? extends Artifact> artifact : bundle.getClassArray("spawned_artifacts")) {
				Category cat = Category.ARTIFACT;
				for (int i = 0; i < cat.classes.length; i++) {
					if (cat.classes[i].equals(artifact)) {
						cat.probs[i] = 0;
					}
				}
			}
		}
		
	}
}