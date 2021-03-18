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

package com.seasluggames.serenitypixeldungeon.android.items.bombs;

import com.seasluggames.serenitypixeldungeon.android.Dungeon;
import com.seasluggames.serenitypixeldungeon.android.actors.Actor;
import com.seasluggames.serenitypixeldungeon.android.actors.Char;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Blindness;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Cripple;
import com.seasluggames.serenitypixeldungeon.android.levels.Level;
import com.seasluggames.serenitypixeldungeon.android.scenes.GameScene;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;

public class Flashbang extends Bomb {
	
	{
		image = ItemSpriteSheet.FLASHBANG;
	}

	@Override
	public void explode(int cell) {
		super.explode(cell);

		Level l = Dungeon.level;
		for (Char ch : Actor.chars()){
			if (ch.fieldOfView != null && ch.fieldOfView[cell]){
				int power = 16 - 4*l.distance(ch.pos, cell);
				if (power > 0){
					Buff.prolong(ch, Blindness.class, power);
					Buff.prolong(ch, Cripple.class, power);
				}
				if (ch == Dungeon.hero){
					GameScene.flash(0xFFFFFF);
				}
			}
		}
		
	}
	
	@Override
	public int value() {
		//prices of ingredients
		return quantity * (20 + 40);
	}
}
