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

package com.seasluggames.serenitypixeldungeon.android.windows;

import com.seasluggames.serenitypixeldungeon.android.Challenges;
import com.seasluggames.serenitypixeldungeon.android.SPDSettings;
import com.seasluggames.serenitypixeldungeon.android.SPDMain;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.android.ui.CheckBox;
import com.seasluggames.serenitypixeldungeon.android.ui.IconButton;
import com.seasluggames.serenitypixeldungeon.android.ui.Icons;
import com.seasluggames.serenitypixeldungeon.android.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.android.ui.Window;

import java.util.ArrayList;

public class WndChallenges extends Window {

	private static final int WIDTH		= 120;
	private static final int TTL_HEIGHT = 16;
	private static final int BTN_HEIGHT = 16;
	private static final int GAP        = 1;

	private boolean editable;
	private ArrayList<CheckBox> boxes;

	public WndChallenges( int checked, boolean editable ) {

		super();

		this.editable = editable;

		RenderedTextBlock title = PixelScene.renderTextBlock( Messages.get(this, "title"), 12 );
		title.hardlight( TITLE_COLOR );
		title.setPos(
				(WIDTH - title.width()) / 2,
				(TTL_HEIGHT - title.height()) / 2
		);
		PixelScene.align(title);
		add( title );

		boxes = new ArrayList<>();

		float pos = TTL_HEIGHT;
		for (int i=0; i < Challenges.NAME_IDS.length; i++) {

			final String challenge = Challenges.NAME_IDS[i];
			
			CheckBox cb = new CheckBox( Messages.titleCase(Messages.get(Challenges.class, challenge)) );
			cb.checked( (checked & Challenges.MASKS[i]) != 0 );
			cb.active = editable;

			if (i > 0) {
				pos += GAP;
			}
			cb.setRect( 0, pos, WIDTH-16, BTN_HEIGHT );

			add( cb );
			boxes.add( cb );
			
			IconButton info = new IconButton(Icons.get(Icons.INFO)){
				@Override
				protected void onClick() {
					super.onClick();
					SPDMain.scene().add(
							new WndMessage(Messages.get(Challenges.class, challenge+"_desc"))
					);
				}
			};
			info.setRect(cb.right(), pos, 16, BTN_HEIGHT);
			add(info);
			
			pos = cb.bottom();
		}

		resize( WIDTH, (int)pos );
	}

	@Override
	public void onBackPressed() {

		if (editable) {
			int value = 0;
			for (int i=0; i < boxes.size(); i++) {
				if (boxes.get( i ).checked()) {
					value |= Challenges.MASKS[i];
				}
			}
			SPDSettings.challenges( value );
		}

		super.onBackPressed();
	}
}