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

package com.seasluggames.serenitypixeldungeon.android.levels.traps;

import com.seasluggames.serenitypixeldungeon.android.Dungeon;
import com.seasluggames.serenitypixeldungeon.android.actors.Actor;
import com.seasluggames.serenitypixeldungeon.android.actors.Char;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.FlavourBuff;
import com.seasluggames.serenitypixeldungeon.android.actors.mobs.Mob;
import com.seasluggames.serenitypixeldungeon.android.effects.CellEmitter;
import com.seasluggames.serenitypixeldungeon.android.effects.particles.PitfallParticle;
import com.seasluggames.serenitypixeldungeon.android.items.Heap;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.levels.features.Chasm;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.scenes.GameScene;
import com.seasluggames.serenitypixeldungeon.android.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

public class PitfallTrap extends Trap {

	{
		color = RED;
		shape = DIAMOND;
	}

	@Override
	public void activate() {
		
		if( Dungeon.bossLevel() || Dungeon.depth > 25){
			GLog.w(Messages.get(this, "no_pit"));
			return;
		}

		DelayedPit p = Buff.affect(Dungeon.hero, DelayedPit.class, 1);
		p.depth = Dungeon.depth;
		p.pos = pos;

		for (int i : PathFinder.NEIGHBOURS9){
			if (!Dungeon.level.solid[pos+i] || Dungeon.level.passable[pos+i]){
				CellEmitter.floor(pos+i).burst(PitfallParticle.FACTORY4, 8);
			}
		}

		if (pos == Dungeon.hero.pos){
			GLog.n(Messages.get(this, "triggered_hero"));
		} else if (Dungeon.level.heroFOV[pos]){
			GLog.n(Messages.get(this, "triggered"));
		}

	}

	public static class DelayedPit extends FlavourBuff {

		int pos;
		int depth;

		@Override
		public boolean act() {
			if (depth == Dungeon.depth) {
				for (int i : PathFinder.NEIGHBOURS9) {

					int cell = pos + i;

					if (Dungeon.level.solid[pos+i] && !Dungeon.level.passable[pos+i]){
						continue;
					}

					CellEmitter.floor(pos+i).burst(PitfallParticle.FACTORY8, 12);

					Heap heap = Dungeon.level.heaps.get(cell);

					if (heap != null) {
						for (Item item : heap.items) {
							Dungeon.dropToChasm(item);
						}
						heap.sprite.kill();
						GameScene.discard(heap);
						Dungeon.level.heaps.remove(cell);
					}

					Char ch = Actor.findChar(cell);

					//don't trigger on flying chars, or immovable neutral chars
					if (ch != null && !ch.flying
						&& !(ch.alignment == Char.Alignment.NEUTRAL && Char.hasProp(ch, Char.Property.IMMOVABLE))) {
						if (ch == Dungeon.hero) {
							Chasm.heroFall(cell);
						} else {
							Chasm.mobFall((Mob) ch);
						}
					}

				}
			}

			detach();
			return true;
		}

		private static final String POS = "pos";
		private static final String DEPTH = "depth";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(POS, pos);
			bundle.put(DEPTH, depth);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			pos = bundle.getInt(POS);
			depth = bundle.getInt(DEPTH);
		}

	}
}
