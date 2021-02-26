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

import com.seasluggames.serenitypixeldungeon.Badges;
import com.seasluggames.serenitypixeldungeon.Dungeon;
import com.seasluggames.serenitypixeldungeon.Statistics;
import com.seasluggames.serenitypixeldungeon.actors.buffs.Degrade;
import com.seasluggames.serenitypixeldungeon.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.actors.hero.Talent;
import com.seasluggames.serenitypixeldungeon.effects.Speck;
import com.seasluggames.serenitypixeldungeon.effects.particles.ShadowParticle;
import com.seasluggames.serenitypixeldungeon.items.Item;
import com.seasluggames.serenitypixeldungeon.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.items.rings.Ring;
import com.seasluggames.serenitypixeldungeon.items.wands.Wand;
import com.seasluggames.serenitypixeldungeon.items.weapon.Weapon;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSpriteSheet;
import com.seasluggames.serenitypixeldungeon.utils.GLog;
import com.seasluggames.serenitypixeldungeon.windows.WndBag;

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

			w.upgrade();

			if (w.cursedKnown && hadCursedEnchant && !w.hasCurseEnchant()){
				removeCurse( Dungeon.hero );
			} else if (w.cursedKnown && wasCursed && !w.cursed){
				weakenCurse( Dungeon.hero );
			}
			if (hadGoodEnchant && !w.hasGoodEnchant()){
				GLog.w( Messages.get(Weapon.class, "incompatible") );
			}

		} else if (item instanceof Armor){
			Armor a = (Armor) item;
			boolean wasCursed = a.cursed;
			boolean hadCursedGlyph = a.hasCurseGlyph();
			boolean hadGoodGlyph = a.hasGoodGlyph();

			a.upgrade();

			if (a.cursedKnown && hadCursedGlyph && !a.hasCurseGlyph()){
				removeCurse( Dungeon.hero );
			} else if (a.cursedKnown && wasCursed && !a.cursed){
				weakenCurse( Dungeon.hero );
			}
			if (hadGoodGlyph && !a.hasGoodGlyph()){
				GLog.w( Messages.get(Armor.class, "incompatible") );
			}

		} else if (item instanceof Wand || item instanceof Ring) {
			boolean wasCursed = item.cursed;

			item.upgrade();

			if (wasCursed && !item.cursed){
				removeCurse( Dungeon.hero );
			}

		} else {
			item.upgrade();
		}

		Talent.onUpgradeScrollUsed( Dungeon.hero );
		
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
