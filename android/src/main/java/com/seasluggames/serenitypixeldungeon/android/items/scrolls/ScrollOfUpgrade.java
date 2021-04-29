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

package com.seasluggames.serenitypixeldungeon.android.items.scrolls;

import com.seasluggames.serenitypixeldungeon.android.Badges;
import com.seasluggames.serenitypixeldungeon.android.Statistics;
import com.seasluggames.serenitypixeldungeon.android.actors.buffs.Degrade;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Talent;
import com.seasluggames.serenitypixeldungeon.android.effects.Speck;
import com.seasluggames.serenitypixeldungeon.android.effects.particles.ShadowParticle;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.android.items.rings.Ring;
import com.seasluggames.serenitypixeldungeon.android.items.wands.Wand;
import com.seasluggames.serenitypixeldungeon.android.items.weapon.Weapon;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;
import com.seasluggames.serenitypixeldungeon.android.utils.GLog;
import com.seasluggames.serenitypixeldungeon.android.windows.WndBag;
import com.watabou.utils.Random;

import static com.seasluggames.serenitypixeldungeon.android.Dungeon.hero;
import static com.seasluggames.serenitypixeldungeon.android.actors.hero.Talent.RISKY_UPGRADE;

public class ScrollOfUpgrade extends InventoryScroll {
	
	{
		icon = ItemSpriteSheet.Icons.SCROLL_UPGRADE;
		mode = WndBag.Mode.UPGRADEABLE;

		unique = true;
	}
	
	@Override
	protected void onItemSelected( Item item ) {

		upgrade( curUser );

		Degrade.detach( curUser, Degrade.class );

		//logic for telling the user when item properties change from upgrades
		//...yes this is rather messy
		if (item instanceof Weapon){
			Weapon w = (Weapon) item;
			boolean wasCursed = w.cursed;
			boolean hadCursedEnchant = w.hasCurseEnchant();
			boolean hadGoodEnchant = w.hasGoodEnchant();

			if (hero.hasTalent(RISKY_UPGRADE)) {
				int dice = Random.Int(0, 9);

				if ((dice + hero.pointsInTalent(RISKY_UPGRADE) >= 4)) {
					//60% rank 1, 70% rank 2
					w.upgrade(1 + hero.pointsInTalent(RISKY_UPGRADE));
				} else {
					//40% rank 1, 30% rank 2
					w.degrade(10);
				}
			} else {
				w.upgrade();
			}

			if (w.cursedKnown && hadCursedEnchant && !w.hasCurseEnchant()){
				removeCurse( hero );
			} else if (w.cursedKnown && wasCursed && !w.cursed){
				weakenCurse( hero );
			}
			if (hadGoodEnchant && !w.hasGoodEnchant()){
				GLog.w( Messages.get(Weapon.class, "incompatible") );
			}

		} else if (item instanceof Armor){
			Armor a = (Armor) item;
			boolean wasCursed = a.cursed;
			boolean hadCursedGlyph = a.hasCurseGlyph();
			boolean hadGoodGlyph = a.hasGoodGlyph();

			if (hero.hasTalent(RISKY_UPGRADE)) {
				int dice = Random.Int(0, 9);

				if ((dice + hero.pointsInTalent(RISKY_UPGRADE) >= 4)) {
					//60% rank 1, 70% rank 2
					a.upgrade(1 + hero.pointsInTalent(RISKY_UPGRADE));
				} else {
					//40% rank 1, 30% rank 2
					a.degrade(10);
				}
			} else {
				a.upgrade();
			}

			if (a.cursedKnown && hadCursedGlyph && !a.hasCurseGlyph()){
				removeCurse( hero );
			} else if (a.cursedKnown && wasCursed && !a.cursed){
				weakenCurse( hero );
			}
			if (hadGoodGlyph && !a.hasGoodGlyph()){
				GLog.w( Messages.get(Armor.class, "incompatible") );
			}

		} else if (item instanceof Wand || item instanceof Ring) {
			boolean wasCursed = item.cursed;

			if (hero.hasTalent(RISKY_UPGRADE)) {
				int dice = Random.Int(0, 9);

				if ((dice + hero.pointsInTalent(RISKY_UPGRADE) >= 4)) {
					//60% rank 1, 70% rank 2
					item.upgrade(1 + hero.pointsInTalent(RISKY_UPGRADE));
				} else {
					//40% rank 1, 30% rank 2
					item.degrade(10);
				}
			} else {
				item.upgrade();
			}

			if (wasCursed && !item.cursed){
				removeCurse( hero );
			}

		} else {
			if (hero.hasTalent(RISKY_UPGRADE)) {
				int dice = Random.Int(0, 9);

				if ((dice + hero.pointsInTalent(RISKY_UPGRADE) >= 4)) {
					//60% rank 1, 70% rank 2
					item.upgrade(1 + hero.pointsInTalent(RISKY_UPGRADE));
				} else {
					//40% rank 1, 30% rank 2
					item.degrade(10);
				}
			} else {
				item.upgrade();
			}
		}

		Talent.onUpgradeScrollUsed( hero );
		
		Badges.validateItemLevelAquired( item );
		Statistics.upgradesUsed++;
		Badges.validateMageUnlock();
	}
	
	public static void upgrade( Hero hero ) {
		hero.sprite.emitter().start( Speck.factory( Speck.UP ), 0.2f, 3 );
	}

	public static void weakenCurse( Hero hero ){
		GLog.p( Messages.get(ScrollOfUpgrade.class, "weaken_curse") );
		hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 5 );
	}

	public static void removeCurse( Hero hero ){
		GLog.p( Messages.get(ScrollOfUpgrade.class, "remove_curse") );
		hero.sprite.emitter().start( ShadowParticle.UP, 0.05f, 10 );
	}
	
	@Override
	public int value() {
		return isKnown() ? 50 * quantity : super.value();
	}
}
