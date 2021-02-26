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

package com.seasluggames.serenitypixeldungeon.items.stones;

import com.seasluggames.serenitypixeldungeon.actors.Actor;
import com.seasluggames.serenitypixeldungeon.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.items.scrolls.ScrollOfTeleportation;
import com.seasluggames.serenitypixeldungeon.mechanics.Ballistica;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSpriteSheet;

public class StoneOfBlink extends Runestone {
	
	{
		image = ItemSpriteSheet.STONE_BLINK;
	}
	
	private static Ballistica throwPath;
	
	@Override
	public int throwPos(Hero user, int dst) {
		throwPath = new Ballistica( user.pos, dst, Ballistica.PROJECTILE );
		return throwPath.collisionPos;
	}
	
	@Override
	protected void onThrow(int cell) {
		if (Actor.findChar(cell) != null && throwPath.dist >= 1){
			cell = throwPath.path.get(throwPath.dist-1);
		}
		throwPath = null;
		super.onThrow(cell);
	}
	
	@Override
	protected void activate(int cell) {
		ScrollOfTeleportation.teleportToLocation(curUser, cell);
	}
}
