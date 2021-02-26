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

package com.seasluggames.serenitypixeldungeon.scenes;

import com.seasluggames.serenitypixeldungeon.Badges;
import com.seasluggames.serenitypixeldungeon.Challenges;
import com.seasluggames.serenitypixeldungeon.Chrome;
import com.seasluggames.serenitypixeldungeon.GamesInProgress;
import com.seasluggames.serenitypixeldungeon.Rankings;
import com.seasluggames.serenitypixeldungeon.SPDSettings;
import com.seasluggames.serenitypixeldungeon.SerenityPixelDungeon;
import com.seasluggames.serenitypixeldungeon.effects.BannerSprites;
import com.seasluggames.serenitypixeldungeon.messages.Messages;
import com.seasluggames.serenitypixeldungeon.ui.Icons;
import com.seasluggames.serenitypixeldungeon.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.ui.StyledButton;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.FileUtils;

public class WelcomeScene extends PixelScene {

	private static final int LATEST_UPDATE = SerenityPixelDungeon.v0_0_1a;

	@Override
	public void create() {
		super.create();

		final int previousVersion = SPDSettings.version();

		if (SerenityPixelDungeon.versionCode == previousVersion && !SPDSettings.intro()) {
			SerenityPixelDungeon.switchNoFade(TitleScene.class);
			return;
		}

		uiCamera.visible = false;

		int w = Camera.main.width;
		int h = Camera.main.height;

		Image title = new Image(BannerSprites.fetch(BannerSprites.Type.SERENITY)) {
			private float time = 0;

			@Override
			public void update() {
				super.update();
				am = Math.max(0f, (float) Math.sin(time += Game.elapsed));
				if (time >= 1.5f * Math.PI) time = 0;
			}

			@Override
			public void draw() {
				Blending.setLightMode();
				super.draw();
				Blending.setNormalMode();
			}
		};
		add(title);

		float topRegion = Math.max(title.height - 6, h * 0.45f);

		title.x = (w - title.width()) / 2f;
		title.y = 2 + (topRegion - title.height()) / 2f;

		align(title);
		
		StyledButton okay = new StyledButton(Chrome.Type.GREY_BUTTON_TR, Messages.get(this, "continue")){
			@Override
			protected void onClick() {
				super.onClick();
				if (previousVersion == 0 || SPDSettings.intro()){
					SPDSettings.version(SerenityPixelDungeon.versionCode);
					GamesInProgress.selectedClass = null;
					GamesInProgress.curSlot = 1;
					SerenityPixelDungeon.switchScene(HeroSelectScene.class);
				} else {
					updateVersion(previousVersion);
					SerenityPixelDungeon.switchScene(TitleScene.class);
				}
			}
		};

		float buttonY = Math.min(topRegion + (PixelScene.landscape() ? 60 : 120), h - 24);

		okay.text(Messages.get(TitleScene.class, "enter"));
		okay.setRect(title.x, buttonY, title.width(), 20);
		okay.icon(Icons.get(Icons.ENTER));
		add(okay);

		RenderedTextBlock text = PixelScene.renderTextBlock(6);
		String message;
		message = Messages.get(this, "welcome_msg");
		text.text(message, w-20);
		float textSpace = okay.top() - topRegion - 4;
		text.setPos((w - text.width()) / 2f, (topRegion + 2) + (textSpace - text.height())/2);
		add(text);

	}

	private void updateVersion(int previousVersion){
		//update rankings, to update any data which may be outdated
		if (previousVersion < LATEST_UPDATE){
			int highestChalInRankings = 0;
			try {
				Rankings.INSTANCE.load();
				for (Rankings.Record rec : Rankings.INSTANCE.records.toArray(new Rankings.Record[0])){
					try {
						Rankings.INSTANCE.loadGameData(rec);
						if (rec.win) highestChalInRankings = Math.max(highestChalInRankings, Challenges.activeChallenges());
						Rankings.INSTANCE.saveGameData(rec);
					} catch (Exception e) {
						//if we encounter a fatal per-record error, then clear that record
						Rankings.INSTANCE.records.remove(rec);
						SerenityPixelDungeon.reportException(e);
					}
				}
				Rankings.INSTANCE.save();
			} catch (Exception e) {
				//if we encounter a fatal error, then just clear the rankings
				FileUtils.deleteFile( Rankings.RANKINGS_FILE );
				SerenityPixelDungeon.reportException(e);
			}

			//fixes a bug from v0.9.0- where champion badges would rarely not save
			if (highestChalInRankings > 0){
				Badges.loadGlobal();
				if (highestChalInRankings >= 1) Badges.addGlobal(Badges.Badge.CHAMPION_1);
				if (highestChalInRankings >= 3) Badges.addGlobal(Badges.Badge.CHAMPION_2);
				if (highestChalInRankings >= 6) Badges.addGlobal(Badges.Badge.CHAMPION_3);
				Badges.saveGlobal();
			}
		}

		SPDSettings.version(SerenityPixelDungeon.versionCode);
	}
	
}
