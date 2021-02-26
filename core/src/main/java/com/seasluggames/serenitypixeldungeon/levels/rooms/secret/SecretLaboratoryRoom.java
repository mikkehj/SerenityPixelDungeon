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

package com.seasluggames.serenitypixeldungeon.levels.rooms.secret;

import com.seasluggames.serenitypixeldungeon.actors.blobs.Alchemy;
import com.seasluggames.serenitypixeldungeon.actors.blobs.Blob;
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
import com.seasluggames.serenitypixeldungeon.items.potions.PotionOfToxicGas;
import com.seasluggames.serenitypixeldungeon.levels.Level;
import com.seasluggames.serenitypixeldungeon.levels.Terrain;
import com.seasluggames.serenitypixeldungeon.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;
import com.watabou.utils.Reflection;

import java.util.HashMap;

public class SecretLaboratoryRoom extends SecretRoom {
	
	private static HashMap<Class<? extends Potion>, Float> potionChances = new HashMap<>();
	static{
		potionChances.put(PotionOfHealing.class,        1f);
		potionChances.put(PotionOfMindVision.class,     2f);
		potionChances.put(PotionOfFrost.class,          3f);
		potionChances.put(PotionOfLiquidFlame.class,    3f);
		potionChances.put(PotionOfToxicGas.class,       3f);
		potionChances.put(PotionOfHaste.class,          4f);
		potionChances.put(PotionOfInvisibility.class,   4f);
		potionChances.put(PotionOfLevitation.class,     4f);
		potionChances.put(PotionOfParalyticGas.class,   4f);
		potionChances.put(PotionOfPurity.class,         4f);
		potionChances.put(PotionOfExperience.class,     6f);
	}
	
	public void paint( Level level ) {
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY_SP );
		
		entrance().set( Door.Type.HIDDEN );
		
		Point pot = center();
		Painter.set( level, pot, Terrain.ALCHEMY );
		
		Blob.seed( pot.x + level.width() * pot.y, 1+Random.NormalIntRange(20, 30), Alchemy.class, level );
		
		int n = Random.IntRange( 2, 3 );
		HashMap<Class<? extends Potion>, Float> chances = new HashMap<>(potionChances);
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY_SP || level.heaps.get( pos ) != null);
			
			Class<?extends Potion> potionCls = Random.chances(chances);
			chances.put(potionCls, 0f);
			level.drop( Reflection.newInstance(potionCls), pos );
		}
		
	}
	
}