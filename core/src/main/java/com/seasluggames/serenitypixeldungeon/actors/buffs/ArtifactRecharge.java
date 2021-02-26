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

package com.seasluggames.serenitypixeldungeon.actors.buffs;

import com.seasluggames.serenitypixeldungeon.actors.hero.Belongings;
import com.seasluggames.serenitypixeldungeon.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.items.artifacts.Artifact;
import com.seasluggames.serenitypixeldungeon.items.artifacts.HornOfPlenty;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.ui.BuffIndicator;
import com.watabou.noosa.Image;
import com.watabou.utils.Bundle;

public class ArtifactRecharge extends Buff {

	public static final float DURATION = 30f;

	{
		type = buffType.POSITIVE;
	}

	private int left;
	public boolean ignoreHornOfPlenty;
	
	@Override
	public boolean act() {
		
		if (target instanceof Hero){
			Belongings b = ((Hero) target).belongings;
			
			if (b.artifact instanceof Artifact){
				if (!(b.artifact instanceof HornOfPlenty) || !ignoreHornOfPlenty) {
					((Artifact) b.artifact).charge((Hero) target);
				}
			}
			if (b.misc instanceof Artifact){
				if (!(b.misc instanceof HornOfPlenty) || !ignoreHornOfPlenty) {
					((Artifact) b.misc).charge((Hero) target);
				}
			}
		}
		
		left--;
		if (left <= 0){
			detach();
		} else {
			spend(TICK);
		}
		
		return true;
	}
	
	public ArtifactRecharge set( int amount ){
		left = amount;
		return this;
	}
	
	public ArtifactRecharge prolong( int amount ){
		left += amount;
		return this;
	}
	
	@Override
	public int icon() {
		return BuffIndicator.RECHARGING;
	}
	
	@Override
	public void tintIcon(Image icon) {
		icon.hardlight(0, 1f, 0);
	}

	@Override
	public float iconFadePercent() {
		return Math.max(0, (DURATION - left) / DURATION);
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}
	
	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns(left+1));
	}
	
	private static final String LEFT = "left";
	private static final String IGNORE_HORN = "ignore_horn";
	
	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put( LEFT, left );
		bundle.put( IGNORE_HORN, ignoreHornOfPlenty );
	}
	
	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		left = bundle.getInt(LEFT);
		ignoreHornOfPlenty = bundle.getBoolean(IGNORE_HORN);
	}
}