/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2021 Evan Debenham
 *
 * Serenity Pixel Dungeon
 * Copyright (C) 2021-2021 Mikael Hjønnevåg
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

package com.seasluggames.serenitypixeldungeon.android.levels.rooms.special;

import com.seasluggames.serenitypixeldungeon.android.items.Generator;
import com.seasluggames.serenitypixeldungeon.android.items.Honeypot;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.items.potions.PotionOfLiquidFlame;
import com.seasluggames.serenitypixeldungeon.android.levels.Level;
import com.seasluggames.serenitypixeldungeon.android.levels.Terrain;
import com.seasluggames.serenitypixeldungeon.android.levels.painters.Painter;
import com.watabou.utils.Random;

public class StorageRoom extends SpecialRoom {

	public void paint( Level level ) {
		
		final int floor = Terrain.EMPTY_SP;
		
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, floor );

		boolean honeyPot = Random.Int( 2 ) == 0;
		
		int n = Random.IntRange( 3, 4 );
		for (int i=0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != floor);
			if (honeyPot){
				level.drop( new Honeypot(), pos);
				honeyPot = false;
			} else
				level.drop( prize( level ), pos );
		}
		
		entrance().set( Door.Type.BARRICADE );
		level.addItemToSpawn( new PotionOfLiquidFlame() );
	}
	
	private static Item prize( Level level ) {

		if (Random.Int(2) != 0){
			Item prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}
		
		return Generator.random( Random.oneOf(
			Generator.Category.POTION,
			Generator.Category.SCROLL,
			Generator.Category.FOOD,
			Generator.Category.GOLD
		) );
	}
}
