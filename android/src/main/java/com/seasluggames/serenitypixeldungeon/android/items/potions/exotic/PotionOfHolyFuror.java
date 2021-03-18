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

package com.seasluggames.serenitypixeldungeon.android.items.potions.exotic;

import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Bless;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;

public class PotionOfHolyFuror extends ExoticPotion {
	
	{
		icon = ItemSpriteSheet.Icons.POTION_HOLYFUROR;
	}
	
	@Override
	public void apply( Hero hero ) {
		identify();
		Buff.prolong(hero, Bless.class, Bless.DURATION*4f);
	}
	
}
