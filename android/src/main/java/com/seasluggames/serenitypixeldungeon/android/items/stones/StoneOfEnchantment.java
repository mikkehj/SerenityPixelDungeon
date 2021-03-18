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

package com.seasluggames.serenitypixeldungeon.android.items.stones;

import com.seasluggames.serenitypixeldungeon.android.effects.Enchanting;
import com.seasluggames.serenitypixeldungeon.android.effects.Speck;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.android.items.weapon.Weapon;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;
import com.seasluggames.serenitypixeldungeon.android.utils.GLog;
import com.seasluggames.serenitypixeldungeon.android.windows.WndBag;

public class StoneOfEnchantment extends InventoryStone {
	
	{
		mode = WndBag.Mode.ENCHANTABLE;
		image = ItemSpriteSheet.STONE_ENCHANT;

		unique = true;
	}
	
	@Override
	protected void onItemSelected(Item item) {
		
		if (item instanceof Weapon) {
			
			((Weapon)item).enchant();
			
		} else {
			
			((Armor)item).inscribe();
			
		}
		
		curUser.sprite.emitter().start( Speck.factory( Speck.LIGHT ), 0.1f, 5 );
		Enchanting.show( curUser, item );
		
		if (item instanceof Weapon) {
			GLog.p(Messages.get(this, "weapon"));
		} else {
			GLog.p(Messages.get(this, "armor"));
		}
		
		useAnimation();
		
	}
	
	@Override
	public int value() {
		return 30 * quantity;
	}
}
