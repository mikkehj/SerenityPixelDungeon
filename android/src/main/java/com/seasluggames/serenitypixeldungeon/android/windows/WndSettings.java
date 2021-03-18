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

import com.seasluggames.serenitypixeldungeon.android.Assets;
import com.seasluggames.serenitypixeldungeon.android.SPDSettings;
import com.seasluggames.serenitypixeldungeon.android.SPDMain;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.scenes.GameScene;
import com.seasluggames.serenitypixeldungeon.android.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.android.ui.CheckBox;
import com.seasluggames.serenitypixeldungeon.android.ui.Icons;
import com.seasluggames.serenitypixeldungeon.android.ui.OptionSlider;
import com.seasluggames.serenitypixeldungeon.android.ui.PurpleButton;
import com.seasluggames.serenitypixeldungeon.android.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.android.ui.Toolbar;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.DeviceCompat;
import com.watabou.utils.Random;

public class WndSettings extends WndTabbed {

    private static final int WIDTH_P = 122;
    private static final int WIDTH_L = 223;

    private static final int SLIDER_HEIGHT = 24;
    private static final int BTN_HEIGHT = 18;
    private static final float GAP = 2;

    private DisplayTab display;
    private UITab ui;
    private AudioTab audio;

    public static int last_index = 0;

    public WndSettings() {
        super();

        float height;

        int width = PixelScene.landscape() ? WIDTH_L : WIDTH_P;

        display = new DisplayTab();
        display.setSize(width, 0);
        height = display.height();
        add(display);

        add(new IconTab(Icons.get(Icons.DISPLAY)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                display.visible = display.active = value;
                if (value) last_index = 0;
            }
        });

        ui = new UITab();
        ui.setSize(width, 0);
        height = Math.max(height, ui.height());
        add(ui);

        add(new IconTab(Icons.get(Icons.PREFS)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                ui.visible = ui.active = value;
                if (value) last_index = 1;
            }
        });


        audio = new AudioTab();
        audio.setSize(width, 0);
        height = Math.max(height, audio.height());
        add(audio);

        add(new IconTab(Icons.get(Icons.AUDIO)) {
            @Override
            protected void select(boolean value) {
                super.select(value);
                audio.visible = audio.active = value;
                if (value) last_index = 3;
            }
        });


        resize(width, (int) Math.ceil(height));

        layoutTabs();

        select(last_index);

    }

    @Override
    public void hide() {
        super.hide();
        //resets generators because there's no need to retain chars for languages not selected
        SPDMain.seamlessResetScene(new Game.SceneChangeCallback() {
            @Override
            public void beforeCreate() {
                Game.platform.resetGenerators();
            }

            @Override
            public void afterCreate() {
                //do nothing
            }
        });
    }

    private static class DisplayTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        OptionSlider optScale;
        CheckBox chkSaver;
        PurpleButton btnOrientation;
        ColorBlock sep2;
        OptionSlider optBrightness;
        OptionSlider optVisGrid;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            if (!DeviceCompat.isDesktop() && PixelScene.maxScreenZoom >= 2) {
                chkSaver = new CheckBox(Messages.get(this, "saver")) {
                    @Override
                    protected void onClick() {
                        super.onClick();
                        if (checked()) {
                            checked(!checked());
                            SPDMain.scene().add(new WndOptions(
                                    Messages.get(DisplayTab.class, "saver"),
                                    Messages.get(DisplayTab.class, "saver_desc"),
                                    Messages.get(DisplayTab.class, "okay"),
                                    Messages.get(DisplayTab.class, "cancel")) {
                                @Override
                                protected void onSelect(int index) {
                                    if (index == 0) {
                                        checked(!checked());
                                        SPDSettings.powerSaver(checked());
                                    }
                                }
                            });
                        } else {
                            SPDSettings.powerSaver(checked());
                        }
                    }
                };
                chkSaver.checked(SPDSettings.powerSaver());
                add(chkSaver);
            }

            if ((int) Math.ceil(2 * Game.density) < PixelScene.maxDefaultZoom) {
                optScale = new OptionSlider(Messages.get(this, "scale"),
                        (int) Math.ceil(2 * Game.density) + "X",
                        PixelScene.maxDefaultZoom + "X",
                        (int) Math.ceil(2 * Game.density),
                        PixelScene.maxDefaultZoom) {
                    @Override
                    protected void onChange() {
                        if (getSelectedValue() != SPDSettings.scale()) {
                            SPDSettings.scale(getSelectedValue());
                            SPDMain.seamlessResetScene();
                        }
                    }
                };
                optScale.setSelectedValue(PixelScene.defaultZoom);
                add(optScale);
            }

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optBrightness = new OptionSlider(Messages.get(this, "brightness"),
                    Messages.get(this, "dark"), Messages.get(this, "bright"), -1, 1) {
                @Override
                protected void onChange() {
                    SPDSettings.brightness(getSelectedValue());
                }
            };
            optBrightness.setSelectedValue(SPDSettings.brightness());
            add(optBrightness);

            optVisGrid = new OptionSlider(Messages.get(this, "visual_grid"),
                    Messages.get(this, "off"), Messages.get(this, "high"), -1, 2) {
                @Override
                protected void onChange() {
                    SPDSettings.visualGrid(getSelectedValue());
                }
            };
            optVisGrid.setSelectedValue(SPDSettings.visualGrid());
            add(optVisGrid);

        }

        @Override
        protected void layout() {

            float bottom = y;

            title.setPos((width - title.width()) / 2, bottom + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2 * GAP;

            bottom = sep1.y + 1;

            if (optScale != null) {
                optScale.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
                bottom = optScale.bottom();
            }

            if (width > 200 && chkSaver != null && btnOrientation != null) {
                chkSaver.setRect(0, bottom + GAP, width / 2 - 1, BTN_HEIGHT);
                btnOrientation.setRect(chkSaver.right() + GAP, bottom + GAP, width / 2 - 1, BTN_HEIGHT);
                bottom = btnOrientation.bottom();
            } else {
                if (chkSaver != null) {
                    chkSaver.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                    bottom = chkSaver.bottom();
                }

                if (btnOrientation != null) {
                    btnOrientation.setRect(0, bottom + GAP, width, BTN_HEIGHT);
                    bottom = btnOrientation.bottom();
                }
            }

            sep2.size(width, 1);
            sep2.y = bottom + GAP;
            bottom = sep2.y + 1;

            if (width > 200) {
                optBrightness.setRect(0, bottom + GAP, width / 2 - GAP / 2, SLIDER_HEIGHT);
                optVisGrid.setRect(optBrightness.right() + GAP, optBrightness.top(), width / 2 - GAP / 2, SLIDER_HEIGHT);
            } else {
                optBrightness.setRect(0, bottom + GAP, width, SLIDER_HEIGHT);
                optVisGrid.setRect(0, optBrightness.bottom() + GAP, width, SLIDER_HEIGHT);
            }

            height = optVisGrid.bottom();
        }

    }

    private static class UITab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        RenderedTextBlock barDesc;
        PurpleButton btnSplit;
        PurpleButton btnGrouped;
        PurpleButton btnCentered;
        CheckBox chkFlipToolbar;
        CheckBox chkFlipTags;
        ColorBlock sep2;
        CheckBox chkFullscreen;
        CheckBox chkFont;
        ColorBlock sep3;
        PurpleButton btnKeyBindings;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            barDesc = PixelScene.renderTextBlock(Messages.get(this, "mode"), 9);
            add(barDesc);

            btnSplit = new PurpleButton(Messages.get(this, "split")) {
                @Override
                protected void onClick() {
                    textColor(TITLE_COLOR);
                    btnGrouped.textColor(WHITE);
                    btnCentered.textColor(WHITE);
                    SPDSettings.toolbarMode(Toolbar.Mode.SPLIT.name());
                    Toolbar.updateLayout();
                }
            };
            if (SPDSettings.toolbarMode().equals(Toolbar.Mode.SPLIT.name()))
                btnSplit.textColor(TITLE_COLOR);
            add(btnSplit);

            btnGrouped = new PurpleButton(Messages.get(this, "group")) {
                @Override
                protected void onClick() {
                    btnSplit.textColor(WHITE);
                    textColor(TITLE_COLOR);
                    btnCentered.textColor(WHITE);
                    SPDSettings.toolbarMode(Toolbar.Mode.GROUP.name());
                    Toolbar.updateLayout();
                }
            };
            if (SPDSettings.toolbarMode().equals(Toolbar.Mode.GROUP.name()))
                btnGrouped.textColor(TITLE_COLOR);
            add(btnGrouped);

            btnCentered = new PurpleButton(Messages.get(this, "center")) {
                @Override
                protected void onClick() {
                    btnSplit.textColor(WHITE);
                    btnGrouped.textColor(WHITE);
                    textColor(TITLE_COLOR);
                    SPDSettings.toolbarMode(Toolbar.Mode.CENTER.name());
                    Toolbar.updateLayout();
                }
            };
            if (SPDSettings.toolbarMode().equals(Toolbar.Mode.CENTER.name()))
                btnCentered.textColor(TITLE_COLOR);
            add(btnCentered);

            chkFlipToolbar = new CheckBox(Messages.get(this, "flip_toolbar")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.flipToolbar(checked());
                    Toolbar.updateLayout();
                }
            };
            chkFlipToolbar.checked(SPDSettings.flipToolbar());
            add(chkFlipToolbar);

            chkFlipTags = new CheckBox(Messages.get(this, "flip_indicators")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.flipTags(checked());
                    GameScene.layoutTags();
                }
            };
            chkFlipTags.checked(SPDSettings.flipTags());
            add(chkFlipTags);

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            chkFullscreen = new CheckBox(Messages.get(this, "fullscreen")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.fullscreen(checked());
                }
            };
            chkFullscreen.checked(SPDSettings.fullscreen());
            chkFullscreen.enable(DeviceCompat.supportsFullScreen());
            add(chkFullscreen);

            chkFont = new CheckBox(Messages.get(this, "system_font")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDMain.seamlessResetScene(new Game.SceneChangeCallback() {
                        @Override
                        public void beforeCreate() {
                            SPDSettings.systemFont(checked());
                        }

                        @Override
                        public void afterCreate() {
                            //do nothing
                        }
                    });
                }
            };
            chkFont.checked(SPDSettings.systemFont());
            add(chkFont);

        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2 * GAP;

            barDesc.setPos((width - barDesc.width()) / 2f, sep1.y + 1 + GAP);
            PixelScene.align(barDesc);

            int btnWidth = (int) (width - 2 * GAP) / 3;
            btnSplit.setRect(0, barDesc.bottom() + GAP, btnWidth, 16);
            btnGrouped.setRect(btnSplit.right() + GAP, btnSplit.top(), btnWidth, 16);
            btnCentered.setRect(btnGrouped.right() + GAP, btnSplit.top(), btnWidth, 16);

            if (width > 200) {
                chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP, width / 2 - 1, BTN_HEIGHT);
                chkFlipTags.setRect(chkFlipToolbar.right() + GAP, chkFlipToolbar.top(), width / 2 - 1, BTN_HEIGHT);
                sep2.size(width, 1);
                sep2.y = chkFlipTags.bottom() + 2;
                chkFullscreen.setRect(0, sep2.y + 1 + GAP, width / 2 - 1, BTN_HEIGHT);
                chkFont.setRect(chkFullscreen.right() + GAP, chkFullscreen.top(), width / 2 - 1, BTN_HEIGHT);
            } else {
                chkFlipToolbar.setRect(0, btnGrouped.bottom() + GAP, width, BTN_HEIGHT);
                chkFlipTags.setRect(0, chkFlipToolbar.bottom() + GAP, width, BTN_HEIGHT);
                sep2.size(width, 1);
                sep2.y = chkFlipTags.bottom() + 2;
                chkFullscreen.setRect(0, sep2.y + 1 + GAP, width, BTN_HEIGHT);
                chkFont.setRect(0, chkFullscreen.bottom() + GAP, width, BTN_HEIGHT);
            }

            if (btnKeyBindings != null) {
                sep3.size(width, 1);
                sep3.y = chkFont.bottom() + 2;
                btnKeyBindings.setRect(0, sep3.y + 1 + GAP, width, BTN_HEIGHT);
                height = btnKeyBindings.bottom();
            } else {
                height = chkFont.bottom();
            }
        }

    }

    private static class AudioTab extends Component {

        RenderedTextBlock title;
        ColorBlock sep1;
        OptionSlider optMusic;
        CheckBox chkMusicMute;
        ColorBlock sep2;
        OptionSlider optSFX;
        CheckBox chkMuteSFX;

        @Override
        protected void createChildren() {
            title = PixelScene.renderTextBlock(Messages.get(this, "title"), 9);
            title.hardlight(TITLE_COLOR);
            add(title);

            sep1 = new ColorBlock(1, 1, 0xFF000000);
            add(sep1);

            optMusic = new OptionSlider(Messages.get(this, "music_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    SPDSettings.musicVol(getSelectedValue());
                }
            };
            optMusic.setSelectedValue(SPDSettings.musicVol());
            add(optMusic);

            chkMusicMute = new CheckBox(Messages.get(this, "music_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.music(!checked());
                }
            };
            chkMusicMute.checked(!SPDSettings.music());
            add(chkMusicMute);

            sep2 = new ColorBlock(1, 1, 0xFF000000);
            add(sep2);

            optSFX = new OptionSlider(Messages.get(this, "sfx_vol"), "0", "10", 0, 10) {
                @Override
                protected void onChange() {
                    SPDSettings.SFXVol(getSelectedValue());
                    if (Random.Int(100) == 0) {
                        Sample.INSTANCE.play(Assets.Sounds.MIMIC);
                    } else {
                        Sample.INSTANCE.play(Random.oneOf(Assets.Sounds.GOLD,
                                Assets.Sounds.HIT,
                                Assets.Sounds.ITEM,
                                Assets.Sounds.SHATTER,
                                Assets.Sounds.EVOKE,
                                Assets.Sounds.SECRET));
                    }
                }
            };
            optSFX.setSelectedValue(SPDSettings.SFXVol());
            add(optSFX);

            chkMuteSFX = new CheckBox(Messages.get(this, "sfx_mute")) {
                @Override
                protected void onClick() {
                    super.onClick();
                    SPDSettings.soundFx(!checked());
                    Sample.INSTANCE.play(Assets.Sounds.CLICK);
                }
            };
            chkMuteSFX.checked(!SPDSettings.soundFx());
            add(chkMuteSFX);
        }

        @Override
        protected void layout() {
            title.setPos((width - title.width()) / 2, y + GAP);
            sep1.size(width, 1);
            sep1.y = title.bottom() + 2 * GAP;

            optMusic.setRect(0, sep1.y + 1 + GAP, width, SLIDER_HEIGHT);
            chkMusicMute.setRect(0, optMusic.bottom() + GAP, width, BTN_HEIGHT);

            sep2.size(width, 1);
            sep2.y = chkMusicMute.bottom() + GAP;

            optSFX.setRect(0, sep2.y + 1 + GAP, width, SLIDER_HEIGHT);
            chkMuteSFX.setRect(0, optSFX.bottom() + GAP, width, BTN_HEIGHT);

            height = chkMuteSFX.bottom();
        }
    }
}
