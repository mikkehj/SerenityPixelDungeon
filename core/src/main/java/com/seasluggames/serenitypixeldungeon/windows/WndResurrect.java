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

package com.seasluggames.serenitypixeldungeon.windows;

import com.seasluggames.serenitypixeldungeon.Rankings;
import com.seasluggames.serenitypixeldungeon.Statistics;
import com.seasluggames.serenitypixeldungeon.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.items.Ankh;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.scenes.InterlevelScene;
import com.seasluggames.serenitypixeldungeon.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.sprites.ItemSprite;
import com.seasluggames.serenitypixeldungeon.ui.PurpleButton;
import com.seasluggames.serenitypixeldungeon.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.ui.Window;
import com.watabou.noosa.Game;

public class WndResurrect extends Window {
	
	private static final int WIDTH		= 120;
	private static final int BTN_HEIGHT	= 20;
	private static final float GAP		= 2;
	
	public static WndResurrect instance;
	public static Object causeOfDeath;
	
	public WndResurrect( final Ankh ankh, Object causeOfDeath ) {
		
		super();
		
		instance = this;
		WndResurrect.causeOfDeath = causeOfDeath;
		
		IconTitle titlebar = new IconTitle();
		titlebar.icon( new ItemSprite( ankh.image(), null ) );
		titlebar.label( Messages.titleCase(ankh.name()) );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );
		
		RenderedTextBlock message = PixelScene.renderTextBlock( Messages.get(this, "message"), 6 );
		message.maxWidth(WIDTH);
		message.setPos(0, titlebar.bottom() + GAP);
		add( message );
		
		PurpleButton btnYes = new PurpleButton( Messages.get(this, "yes") ) {
			@Override
			protected void onClick() {
				hide();
				
				Statistics.ankhsUsed++;
				
				InterlevelScene.mode = InterlevelScene.Mode.RESURRECT;
				Game.switchScene( InterlevelScene.class );
			}
		};
		btnYes.setRect( 0, message.top() + message.height() + GAP, WIDTH, BTN_HEIGHT );
		add( btnYes );
		
		PurpleButton btnNo = new PurpleButton( Messages.get(this, "no") ) {
			@Override
			protected void onClick() {
				hide();

				Hero.reallyDie( WndResurrect.causeOfDeath );
				Rankings.INSTANCE.submit( false, WndResurrect.causeOfDeath.getClass() );
			}
		};
		btnNo.setRect( 0, btnYes.bottom() + GAP, WIDTH, BTN_HEIGHT );
		add( btnNo );
		
		resize( WIDTH, (int)btnNo.bottom() );
	}
	
	@Override
	public void destroy() {
		super.destroy();
		instance = null;
	}
	
	@Override
	public void onBackPressed() {
	}
}