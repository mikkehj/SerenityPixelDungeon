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

package com.seasluggames.serenitypixeldungeon.android.items.spells;

import com.seasluggames.serenitypixeldungeon.android.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.Scroll;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfIdentify;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfLullaby;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfMagicMapping;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfMirrorImage;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfRage;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfRecharging;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfRemoveCurse;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfRetribution;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfTeleportation;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfTerror;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.ScrollOfTransmutation;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.exotic.ExoticScroll;
import com.seasluggames.serenitypixeldungeon.android.items.stones.Runestone;
import com.seasluggames.serenitypixeldungeon.android.plants.Plant;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.HashMap;

public class ArcaneCatalyst extends Spell {
	
	{
		image = ItemSpriteSheet.SCROLL_CATALYST;
	}
	
	private static HashMap<Class<? extends Scroll>, Float> scrollChances = new HashMap<>();
	static{
		scrollChances.put( ScrollOfIdentify.class,      3f );
		scrollChances.put( ScrollOfRemoveCurse.class,   2f );
		scrollChances.put( ScrollOfMagicMapping.class,  2f );
		scrollChances.put( ScrollOfMirrorImage.class,   2f );
		scrollChances.put( ScrollOfRecharging.class,    2f );
		scrollChances.put( ScrollOfLullaby.class,       2f );
		scrollChances.put( ScrollOfRetribution.class,   2f );
		scrollChances.put( ScrollOfRage.class,          2f );
		scrollChances.put( ScrollOfTeleportation.class, 2f );
		scrollChances.put( ScrollOfTerror.class,        2f );
		scrollChances.put( ScrollOfTransmutation.class, 1f );
	}
	
	@Override
	protected void onCast(Hero hero) {
		
		detach( curUser.belongings.backpack );
		updateQuickslot();
		
		Scroll s = Reflection.newInstance(Random.chances(scrollChances));
		s.anonymize();
		curItem = s;
		s.doRead();
	}
	
	@Override
	public int value() {
		return 40 * quantity;
	}
	
	public static class Recipe extends com.seasluggames.serenitypixeldungeon.android.items.Recipe {
		
		@Override
		public boolean testIngredients(ArrayList<Item> ingredients) {
			boolean scroll = false;
			boolean secondary = false;
			
			for (Item i : ingredients){
				if (i instanceof Plant.Seed || i instanceof Runestone){
					secondary = true;
					//if it is a regular or exotic potion
				} else if (ExoticScroll.regToExo.containsKey(i.getClass())
						|| ExoticScroll.regToExo.containsValue(i.getClass())) {
					scroll = true;
				}
			}
			
			return scroll && secondary;
		}
		
		@Override
		public int cost(ArrayList<Item> ingredients) {
			for (Item i : ingredients){
				if (i instanceof Plant.Seed){
					return 2;
				} else if (i instanceof Runestone){
					return 1;
				}
			}
			return 1;
		}
		
		@Override
		public Item brew(ArrayList<Item> ingredients) {
			
			for (Item i : ingredients){
				i.quantity(i.quantity()-1);
			}
			
			return sampleOutput(null);
		}
		
		@Override
		public Item sampleOutput(ArrayList<Item> ingredients) {
			return new ArcaneCatalyst();
		}
	}
}
