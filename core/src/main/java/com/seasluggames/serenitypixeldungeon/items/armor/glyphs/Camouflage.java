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

package com.seasluggames.serenitypixeldungeon.items.armor.glyphs;

import com.seasluggames.serenitypixeldungeon.actors.Char;
import com.seasluggames.serenitypixeldungeon.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSprite;

public class Camouflage extends Armor.Glyph {

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing( 0x448822 );

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {
		//no proc effect, see HighGrass.trample
		return damage;
	}

	@Override
	public ItemSprite.Glowing glowing() {
		return GREEN;
	}

}

