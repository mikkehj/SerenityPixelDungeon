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

package com.seasluggames.serenitypixeldungeon.items.scrolls;

import com.seasluggames.serenitypixeldungeon.Assets;
import com.seasluggames.serenitypixeldungeon.actors.Char;
import com.seasluggames.serenitypixeldungeon.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.actors.buffs.Recharging;
import com.seasluggames.serenitypixeldungeon.effects.SpellSprite;
import com.seasluggames.serenitypixeldungeon.effects.particles.EnergyParticle;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSpriteSheet;
import com.seasluggames.serenitypixeldungeon.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.particles.Emitter;

public class ScrollOfRecharging extends Scroll {

	{
		icon = ItemSpriteSheet.Icons.SCROLL_RECHARGE;
	}

	@Override
	public void doRead() {

		Buff.affect(curUser, Recharging.class, Recharging.DURATION);
		charge(curUser);

		Sample.INSTANCE.play( Assets.Sounds.READ );
		Sample.INSTANCE.play( Assets.Sounds.CHARGEUP );

		GLog.i( Messages.get(this, "surge") );
		SpellSprite.show( curUser, SpellSprite.CHARGE );
		identify();

		readAnimation();
	}
	
	public static void charge( Char user ) {
		Emitter e = user.sprite.centerEmitter();
		if (e != null) e.burst( EnergyParticle.FACTORY, 15 );
	}
	
	@Override
	public int value() {
		return isKnown() ? 30 * quantity : super.value();
	}
}
