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

package com.seasluggames.serenitypixeldungeon.actors.mobs.npcs;

import com.seasluggames.serenitypixeldungeon.Dungeon;
import com.seasluggames.serenitypixeldungeon.actors.Char;
import com.seasluggames.serenitypixeldungeon.actors.buffs.Buff;
import com.seasluggames.serenitypixeldungeon.effects.CellEmitter;
import com.seasluggames.serenitypixeldungeon.effects.particles.ElmoParticle;
import com.seasluggames.serenitypixeldungeon.items.Heap;
import com.seasluggames.serenitypixeldungeon.items.Item;
import com.seasluggames.serenitypixeldungeon.items.armor.Armor;
import com.seasluggames.serenitypixeldungeon.journal.Notes;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.scenes.GameScene;
import com.seasluggames.serenitypixeldungeon.sprites.ShopkeeperSprite;
import com.seasluggames.serenitypixeldungeon.windows.WndBag;
import com.seasluggames.serenitypixeldungeon.windows.WndTradeItem;
import com.watabou.noosa.Game;
import com.watabou.utils.Callback;

public class Shopkeeper extends NPC {

	{
		spriteClass = ShopkeeperSprite.class;

		properties.add(Property.IMMOVABLE);
	}
	
	@Override
	protected boolean act() {

		if (Dungeon.level.heroFOV[pos]){
			Notes.add(Notes.Landmark.SHOP);
		}
		
		sprite.turnTo( pos, Dungeon.hero.pos );
		spend( TICK );
		return super.act();
	}
	
	@Override
	public void damage( int dmg, Object src ) {
		flee();
	}
	
	@Override
	public void add( Buff buff ) {
		flee();
	}
	
	public void flee() {
		destroy();

		Notes.remove(Notes.Landmark.SHOP);
		
		sprite.killAndErase();
		CellEmitter.get( pos ).burst( ElmoParticle.FACTORY, 6 );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		for (Heap heap: Dungeon.level.heaps.valueList()) {
			if (heap.type == Heap.Type.FOR_SALE) {
				CellEmitter.get( heap.pos ).burst( ElmoParticle.FACTORY, 4 );
				if (heap.size() == 1) {
					heap.destroy();
				} else {
					heap.items.remove(heap.size()-1);
					heap.type = Heap.Type.HEAP;
				}
			}
		}
	}
	
	@Override
	public boolean reset() {
		return true;
	}

	//shopkeepers are greedy!
	public static int sellPrice(Item item){
		return item.value() * 5 * (Dungeon.depth / 5 + 1);
	}
	
	public static WndBag sell() {
		return GameScene.selectItem( itemSelector, WndBag.Mode.FOR_SALE, Messages.get(Shopkeeper.class, "sell"));
	}

	public static boolean willBuyItem( Item item ){
		if (item.value() <= 0)                                               return false;
		if (item.unique && !item.stackable)                                 return false;
		if (item instanceof Armor && ((Armor) item).checkSeal() != null)    return false;
		if (item.isEquipped(Dungeon.hero) && item.cursed)                   return false;
		return true;
	}
	
	private static WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect( Item item ) {
			if (item != null) {
				WndBag parentWnd = sell();
				GameScene.show( new WndTradeItem( item, parentWnd ) );
			}
		}
	};

	@Override
	public boolean interact(Char c) {
		if (c != Dungeon.hero) {
			return true;
		}
		Game.runOnRenderThread(new Callback() {
			@Override
			public void call() {
				sell();
			}
		});
		return true;
	}
}
