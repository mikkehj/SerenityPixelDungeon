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

package com.seasluggames.serenitypixeldungeon.android.items.weapon.curses;

import com.seasluggames.serenitypixeldungeon.android.actors.Char;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Charm;
import com.seasluggames.serenitypixeldungeon.android.effects.Speck;
import com.seasluggames.serenitypixeldungeon.android.items.weapon.Weapon;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSprite;
import com.watabou.utils.Random;

public class Friendly extends Weapon.Enchantment {
	
	private static ItemSprite.Glowing BLACK = new ItemSprite.Glowing( 0x000000 );
	
	@Override
	public int proc(Weapon weapon, Char attacker, Char defender, int damage ) {
		
		if (Random.Int(10) == 0){
			
			Buff.affect( attacker, Charm.class, Charm.DURATION ).object = defender.id();
			attacker.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			
			Charm c = Buff.affect( defender, Charm.class, Charm.DURATION/2 );
			c.ignoreNextHit = true;
			c.object = attacker.id();
			defender.sprite.centerEmitter().start( Speck.factory( Speck.HEART ), 0.2f, 5 );
			
		}
		
		return damage;
	}
	
	@Override
	public boolean curse() {
		return true;
	}
	
	@Override
	public ItemSprite.Glowing glowing() {
		return BLACK;
	}

}
