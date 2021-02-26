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

package com.seasluggames.serenitypixeldungeon.items.scrolls.exotic;

import com.seasluggames.serenitypixeldungeon.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.actors.buffs.MagicImmune;
import com.seasluggames.serenitypixeldungeon.effects.Flare;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSpriteSheet;

public class ScrollOfAntiMagic extends ExoticScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_ANTIMAGIC;
	}
	
	@Override
	public void doRead() {
		
		Buff.affect( curUser, MagicImmune.class, MagicImmune.DURATION );
		new Flare( 5, 32 ).color( 0xFF0000, true ).show( curUser.sprite, 2f );

		identify();
		
		readAnimation();
	}
}
