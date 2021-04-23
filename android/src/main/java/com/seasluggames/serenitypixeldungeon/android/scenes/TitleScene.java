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

package com.seasluggames.serenitypixeldungeon.android.scenes;

import com.seasluggames.serenitypixeldungeon.android.AndroidLauncher;
import com.seasluggames.serenitypixeldungeon.android.Assets;
import com.seasluggames.serenitypixeldungeon.android.Chrome;
import com.seasluggames.serenitypixeldungeon.android.GamesInProgress;
import com.seasluggames.serenitypixeldungeon.android.SPDMain;
import com.seasluggames.serenitypixeldungeon.android.effects.BannerSprites;
import com.seasluggames.serenitypixeldungeon.android.messages.Languages;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.sprites.CharSprite;
import com.seasluggames.serenitypixeldungeon.android.ui.Archs;
import com.seasluggames.serenitypixeldungeon.android.ui.Icons;
import com.seasluggames.serenitypixeldungeon.android.ui.StyledButton;
import com.seasluggames.serenitypixeldungeon.android.windows.WndSettings;
import com.watabou.glwrap.Blending;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Music;
import com.watabou.utils.ColorMath;
import com.watabou.utils.DeviceCompat;

public class TitleScene extends PixelScene {

    StyledButton btnSignIn;
    StyledButton btnSignOut;

    @Override
    public void create() {

        super.create();

        Music.INSTANCE.play(Assets.Music.THEME, true);

        uiCamera.visible = false;

        int w = Camera.main.width;
        int h = Camera.main.height;

        Archs archs = new Archs();
        archs.setSize(w, h);
        add(archs);

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

        final Chrome.Type GREY_TR = Chrome.Type.GREY_BUTTON_TR;

        StyledButton btnPlay = new StyledButton(GREY_TR, Messages.get(this, "enter")) {
            @Override
            protected void onClick() {
                AndroidLauncher.runOnUI(() -> AndroidLauncher.doThis());

                if (GamesInProgress.checkAll().size() == 0) {
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    SPDMain.switchScene(HeroSelectScene.class);
                } else {
                    SPDMain.switchNoFade(StartScene.class);
                }
            }

            @Override
            protected boolean onLongClick() {
                //making it easier to start runs quickly while debugging
                if (DeviceCompat.isDebug()) {
                    GamesInProgress.selectedClass = null;
                    GamesInProgress.curSlot = 1;
                    SPDMain.switchScene(HeroSelectScene.class);
                    return true;
                }
                return super.onLongClick();
            }
        };
        btnPlay.icon(Icons.get(Icons.ENTER));
        add(btnPlay);

        StyledButton btnRankings = new StyledButton(GREY_TR, Messages.get(this, "rankings")) {
            @Override
            protected void onClick() {
                SPDMain.switchNoFade(RankingsScene.class);
            }
        };
        btnRankings.icon(Icons.get(Icons.RANKINGS));
        add(btnRankings);

        StyledButton btnBadges = new StyledButton(GREY_TR, Messages.get(this, "badges")) {
            @Override
            protected void onClick() {
                SPDMain.switchNoFade(BadgesScene.class);
            }
        };
        btnBadges.icon(Icons.get(Icons.BADGES));
        add(btnBadges);

        StyledButton btnSettings = new SettingsButton(GREY_TR, Messages.get(this, "settings"));
        add(btnSettings);

        StyledButton btnAbout = new StyledButton(GREY_TR, Messages.get(this, "about")) {
            @Override
            protected void onClick() {
                SPDMain.switchScene(AboutScene.class);
            }
        };
        btnAbout.icon(Icons.get(Icons.SSG));
        add(btnAbout);

        /*
        btnSignIn = new StyledButton(GREY_TR, Messages.get(this, "signin")) {
            @Override
            protected void onClick() {
                //AndroidLauncher.runOnUI(() -> AndroidLauncher.doThis());
            AndroidLauncher.runSignIn(AndroidLauncher::signIn);
            removeBtnSignIn();
            }
        };

        add(btnSignIn);

        btnSignOut = new StyledButton(GREY_TR, Messages.get(this, "signout")) {
            @Override
            protected void onClick() {
                //AndroidLauncher.runOnUI(() -> AndroidLauncher.doThis());
                AndroidLauncher.runSignIn(AndroidLauncher::signOut);
                removeBtnSignOut();
            }
        };
        //add(btnSignOut);

         */

        final int BTN_HEIGHT = 20;
        int GAP = (int) (h - topRegion - (landscape() ? 3 : 4) * BTN_HEIGHT) / 3;
        GAP /= landscape() ? 3 : 5;
        GAP = Math.max(GAP, 2);

        btnPlay.setRect(title.x, topRegion + GAP, title.width(), BTN_HEIGHT);
        align(btnPlay);
        btnRankings.setRect(btnPlay.left(), btnPlay.bottom() + GAP, (btnPlay.width() / 2) - 1, BTN_HEIGHT);
        btnBadges.setRect(btnRankings.right() + 2, btnRankings.top(), btnRankings.width(), BTN_HEIGHT);
        btnSettings.setRect(btnRankings.left(), btnRankings.bottom() + GAP, btnRankings.width(), BTN_HEIGHT);
        btnAbout.setRect(btnSettings.right() + 2, btnSettings.top(), btnSettings.width(), BTN_HEIGHT);
        //btnSignIn.setRect(title.x, btnSettings.bottom() + GAP, title.width(), BTN_HEIGHT);
        //btnSignOut.setRect(title.x, btnSettings.bottom() + GAP, title.width(), BTN_HEIGHT);

        BitmapText version = new BitmapText("v" + Game.version, pixelFont);
        version.measure();
        version.hardlight(0x888888);
        version.x = w - version.width() - 4;
        version.y = h - version.height() - 2;
        add(version);

        fadeIn();

        AndroidLauncher.runSignIn(AndroidLauncher::checkLogIn);
    }

    private static class SettingsButton extends StyledButton {

        public SettingsButton(Chrome.Type type, String label) {
            super(type, label);
            if (Messages.lang().status() == Languages.Status.INCOMPLETE) {
                icon(Icons.get(Icons.LANGS));
                icon.hardlight(1.5f, 0, 0);
            } else {
                icon(Icons.get(Icons.PREFS));
            }
        }

        @Override
        public void update() {
            super.update();

            if (Messages.lang().status() == Languages.Status.INCOMPLETE) {
                textColor(ColorMath.interpolate(0xFFFFFF, CharSprite.NEGATIVE, 0.5f + (float) Math.sin(Game.timeTotal * 5) / 2f));
            }
        }

        @Override
        protected void onClick() {
            if (Messages.lang().status() == Languages.Status.INCOMPLETE) {
                WndSettings.last_index = 4;
            }
            SPDMain.scene().add(new WndSettings());
        }
    }

    public void removeBtnSignIn() {
        //remove(btnSignIn);
        btnSignIn.destroy();
    }

    private void removeBtnSignOut() {
        remove(btnSignOut);
        add(btnSignIn);
    }
}
