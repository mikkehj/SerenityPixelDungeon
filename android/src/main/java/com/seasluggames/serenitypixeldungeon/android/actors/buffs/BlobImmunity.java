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

package com.seasluggames.serenitypixeldungeon.android.actors.buffs;

import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Blizzard;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.ConfusionGas;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.CorrosiveGas;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Electricity;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Fire;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Freezing;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Inferno;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.ParalyticGas;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Regrowth;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.SmokeScreen;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.StenchGas;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.StormCloud;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.ToxicGas;
import com.seasluggames.serenitypixeldungeon.android.actors.blobs.Web;
import com.seasluggames.serenitypixeldungeon.android.actors.mobs.NewTengu;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.ui.BuffIndicator;

public class BlobImmunity extends FlavourBuff {
	
	{
		type = buffType.POSITIVE;
	}
	
	public static final float DURATION	= 20f;
	
	@Override
	public int icon() {
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - visualcooldown()) / DURATION);
	}
	
	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	{
		//all harmful blobs
		immunities.add( Blizzard.class );
		immunities.add( ConfusionGas.class );
		immunities.add( CorrosiveGas.class );
		immunities.add( Electricity.class );
		immunities.add( Fire.class );
		immunities.add( Freezing.class );
		immunities.add( Inferno.class );
		immunities.add( ParalyticGas.class );
		immunities.add( Regrowth.class );
		immunities.add( SmokeScreen.class );
		immunities.add( StenchGas.class );
		immunities.add( StormCloud.class );
		immunities.add( ToxicGas.class );
		immunities.add( Web.class );

		immunities.add(NewTengu.FireAbility.FireBlob.class);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
