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

package com.seasluggames.serenitypixeldungeon.android.windows;

import com.seasluggames.serenitypixeldungeon.android.Assets;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Talent;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.android.ui.PurpleButton;
import com.seasluggames.serenitypixeldungeon.android.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.android.ui.Window;
import com.watabou.gltextures.SmartTexture;
import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Image;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class WndInfoTalent extends Window {

	private static final float GAP	= 2;

	private static final int WIDTH = 120;

	private SmartTexture icons;
	private TextureFilm film;

	public WndInfoTalent(Talent talent, int points, Callback onUpgradeButton){
		super();

		IconTitle titlebar = new IconTitle();

		icons = TextureCache.get( Assets.Interfaces.TALENT_ICONS );
		film = new TextureFilm( icons, 16, 16 );

		Image buffIcon = new Image( icons );
		buffIcon.frame( film.get(talent.icon()) );

		titlebar.icon( buffIcon );
		String title = Messages.titleCase(talent.title());
		if (points > 0){
			title += " +" + points;
		}
		titlebar.label( title, Window.TITLE_COLOR );
		titlebar.setRect( 0, 0, WIDTH, 0 );
		add( titlebar );

		RenderedTextBlock txtInfo = PixelScene.renderTextBlock(talent.desc(), 6);
		txtInfo.maxWidth(WIDTH);
		txtInfo.setPos(titlebar.left(), titlebar.bottom() + 2*GAP);
		add( txtInfo );

		resize( WIDTH, (int)(txtInfo.bottom() + GAP) );

		if (onUpgradeButton != null) {
			PurpleButton upgrade = new PurpleButton( Messages.get(this, "upgrade") ) {
				@Override
				protected void onClick() {
					super.onClick();
					hide();
					onUpgradeButton.call();
				}
			};
			upgrade.setRect(0, txtInfo.bottom() + 2*GAP, WIDTH, 16);
			add(upgrade);
			resize( WIDTH, (int)upgrade.bottom()+1 );
		}


	}

}
