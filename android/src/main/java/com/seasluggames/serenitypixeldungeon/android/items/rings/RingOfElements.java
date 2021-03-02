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

package com.seasluggames.serenitypixeldungeon.android.items.rings;

import com.seasluggames.serenitypixeldungeon.android.actors.Char;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Electricity;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.ToxicGas;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Burning;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Chill;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Corrosion;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Frost;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Ooze;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Paralysis;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Poison;
import com.seasluggames.serenitypixeldungeon.android.items.armor.glyphs.AntiMagic;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;

import java.text.DecimalFormat;
import java.util.HashSet;

public class RingOfElements extends Ring {

	{
		icon = ItemSpriteSheet.Icons.RING_ELEMENTS;
	}

	public String statsInfo() {
		if (isIdentified()){
			return Messages.get(this, "stats", new DecimalFormat("#.##").format(100f * (1f - Math.pow(0.825f, soloBuffedBonus()))));
		} else {
			return Messages.get(this, "typical_stats", new DecimalFormat("#.##").format(17.5f));
		}
	}
	
	@Override
	protected RingBuff buff( ) {
		return new Resistance();
	}

	public static final HashSet<Class> RESISTS = new HashSet<>();
	static {
		RESISTS.add( Burning.class );
		RESISTS.add( Chill.class );
		RESISTS.add( Frost.class );
		RESISTS.add( Ooze.class );
		RESISTS.add( Paralysis.class );
		RESISTS.add( Poison.class );
		RESISTS.add( Corrosion.class );

		RESISTS.add( ToxicGas.class );
		RESISTS.add( Electricity.class );

		RESISTS.addAll( AntiMagic.RESISTS );
	}
	
	public static float resist( Char target, Class effect ){
		if (getBuffedBonus(target, Resistance.class) == 0) return 1f;
		
		for (Class c : RESISTS){
			if (c.isAssignableFrom(effect)){
				return (float)Math.pow(0.825, getBuffedBonus(target, Resistance.class));
			}
		}
		
		return 1f;
	}
	
	public class Resistance extends RingBuff {
	
	}
}