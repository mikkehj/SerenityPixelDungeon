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
import com.seasluggames.serenitypixeldungeon.android.Badges;
import com.seasluggames.serenitypixeldungeon.android.Dungeon;
import com.seasluggames.serenitypixeldungeon.android.Rankings;
import com.seasluggames.serenitypixeldungeon.android.Statistics;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Belongings;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.android.sprites.HeroSprite;
import com.seasluggames.serenitypixeldungeon.android.ui.BadgesGrid;
import com.seasluggames.serenitypixeldungeon.android.ui.BadgesList;
import com.seasluggames.serenitypixeldungeon.android.ui.Icons;
import com.seasluggames.serenitypixeldungeon.android.ui.ItemSlot;
import com.seasluggames.serenitypixeldungeon.android.ui.PurpleButton;
import com.seasluggames.serenitypixeldungeon.android.ui.RenderedTextBlock;
import com.seasluggames.serenitypixeldungeon.android.ui.TalentsPane;
import com.seasluggames.serenitypixeldungeon.android.ui.Window;
import com.watabou.noosa.ColorBlock;
import com.watabou.noosa.Game;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.audio.Sample;
import com.watabou.noosa.ui.Button;
import com.watabou.noosa.ui.Component;

import java.util.Locale;

public class WndRanking extends WndTabbed {
	
	private static final int WIDTH			= 115;
	private static final int HEIGHT			= 144;
	
	private static Thread thread;
	private String error = null;
	
	private Image busy;
	
	public WndRanking( final Rankings.Record rec ) {
		
		super();
		resize( WIDTH, HEIGHT );
		
		if (thread != null){
			hide();
			return;
		}
		
		thread = new Thread() {
			@Override
			public void run() {
				try {
					Badges.loadGlobal();
					Rankings.INSTANCE.loadGameData( rec );
				} catch ( Exception e ) {
					error = Messages.get(WndRanking.class, "error");
				}
			}
		};

		busy = Icons.BUSY.get();
		busy.origin.set( busy.width / 2, busy.height / 2 );
		busy.angularSpeed = 720;
		busy.x = (WIDTH - busy.width) / 2;
		busy.y = (HEIGHT - busy.height) / 2;
		add( busy );
		
		thread.start();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (thread != null && !thread.isAlive() && busy != null) {
			if (error == null) {
				remove( busy );
				busy = null;
				if (Dungeon.hero != null) {
					createControls();
				} else {
					hide();
				}
			} else {
				hide();
				Game.scene().add( new WndError( error ) );
			}
		}
	}
	
	@Override
	public void destroy() {
		super.destroy();
		thread = null;
	}
	
	private void createControls() {
		
		String[] labels =
			{Messages.get(this, "stats"), Messages.get(this, "items"), Messages.get(this, "badges")};
		Group[] pages =
			{new StatsTab(), new ItemsTab(), new BadgesTab()};
		
		for (int i=0; i < pages.length; i++) {
			
			add( pages[i] );
			
			Tab tab = new RankingTab( labels[i], pages[i] );
			add( tab );
		}

		layoutTabs();
		
		select( 0 );
	}

	private class RankingTab extends LabeledTab {
		
		private Group page;
		
		public RankingTab( String label, Group page ) {
			super( label );
			this.page = page;
		}
		
		@Override
		protected void select( boolean value ) {
			super.select( value );
			if (page != null) {
				page.visible = page.active = selected;
			}
		}
	}
	
	private class StatsTab extends Group {

		private int GAP	= 4;
		
		public StatsTab() {
			super();
			
			String heroClass = Dungeon.hero.className();
			
			IconTitle title = new IconTitle();
			title.icon( HeroSprite.avatar( Dungeon.hero.heroClass, Dungeon.hero.tier() ) );
			title.label( Messages.get(this, "title", Dungeon.hero.lvl, heroClass ).toUpperCase( Locale.ENGLISH ) );
			title.color(Window.TITLE_COLOR);
			title.setRect( 0, 0, WIDTH, 0 );
			add( title );
			
			float pos = title.bottom() + GAP;

			PurpleButton btnTalents = new PurpleButton( Messages.get(this, "talents") ){
				@Override
				protected void onClick() {
					Game.scene().addToFront( new Window(){
						{
							resize(120, 144);
							TalentsPane p = new TalentsPane(false);
							add(p);
							p.setRect(0, 0, width, height);
						}
					});
				}
			};
			btnTalents.icon(Icons.get(Icons.TALENT));
			btnTalents.setRect( (WIDTH - btnTalents.reqWidth()+2)/2, pos, btnTalents.reqWidth()+2 , 16 );
			add(btnTalents);

			pos = btnTalents.bottom();

			if (Dungeon.challenges > 0) {
				PurpleButton btnChallenges = new PurpleButton( Messages.get(this, "challenges") ) {
					@Override
					protected void onClick() {
						Game.scene().add( new WndChallenges( Dungeon.challenges, false ) );
					}
				};

				btnChallenges.icon(Icons.get(Icons.CHALLENGE_ON));
				btnChallenges.setSize( btnChallenges.reqWidth()+2, 16 );
				add( btnChallenges );

				float left = (WIDTH - btnTalents.width() - btnChallenges.width())/3f;

				btnTalents.setPos(left, btnTalents.top());
				btnChallenges.setPos(btnTalents.right() + left, btnTalents.top());
			}

			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "str"), Integer.toString( Dungeon.hero.STR() ), pos );
			pos = statSlot( this, Messages.get(this, "health"), Integer.toString( Dungeon.hero.HT ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "duration"), Integer.toString( (int)Statistics.duration ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "depth"), Integer.toString( Statistics.deepestFloor ), pos );
			pos = statSlot( this, Messages.get(this, "enemies"), Integer.toString( Statistics.enemiesSlain ), pos );
			pos = statSlot( this, Messages.get(this, "gold"), Integer.toString( Statistics.goldCollected ), pos );
			
			pos += GAP;
			
			pos = statSlot( this, Messages.get(this, "food"), Integer.toString( Statistics.foodEaten ), pos );
			pos = statSlot( this, Messages.get(this, "alchemy"), Integer.toString( Statistics.potionsCooked ), pos );
			pos = statSlot( this, Messages.get(this, "ankhs"), Integer.toString( Statistics.ankhsUsed ), pos );
		}
		
		private float statSlot( Group parent, String label, String value, float pos ) {
			
			RenderedTextBlock txt = PixelScene.renderTextBlock( label, 7 );
			txt.setPos(0, pos);
			parent.add( txt );
			
			txt = PixelScene.renderTextBlock( value, 7 );
			txt.setPos(WIDTH * 0.7f, pos);
			PixelScene.align(txt);
			parent.add( txt );
			
			return pos + GAP + txt.height();
		}
	}
	
	private class ItemsTab extends Group {
		
		private float pos;
		
		public ItemsTab() {
			super();
			
			Belongings stuff = Dungeon.hero.belongings;
			if (stuff.weapon != null) {
				addItem( stuff.weapon );
			}
			if (stuff.armor != null) {
				addItem( stuff.armor );
			}
			if (stuff.artifact != null) {
				addItem( stuff.artifact );
			}
			if (stuff.misc != null) {
				addItem( stuff.misc );
			}
			if (stuff.ring != null) {
				addItem( stuff.ring );
			}

			pos = 0;
			for (int i = 0; i < 4; i++){
				if (Dungeon.quickslot.getItem(i) != null){
					QuickSlotButton slot = new QuickSlotButton(Dungeon.quickslot.getItem(i));

					slot.setRect( pos, 120, 28, 23 );

					add(slot);

				} else {
					ColorBlock bg = new ColorBlock( 28, 23, 0x9953564D );
					bg.x = pos;
					bg.y = 120;
					add(bg);
				}
				pos += 29;
			}
		}
		
		private void addItem( Item item ) {
			ItemButton slot = new ItemButton( item );
			slot.setRect( 0, pos, width, ItemButton.HEIGHT );
			add( slot );
			
			pos += slot.height() + 1;
		}
	}
	
	private class BadgesTab extends Group {
		
		public BadgesTab() {
			super();
			
			camera = WndRanking.this.camera;

			Component badges;
			if (Badges.unlocked(false) <= 7){
				badges = new BadgesList(false);
			} else {
				badges = new BadgesGrid(false);
			}
			add(badges);
			badges.setSize( WIDTH, HEIGHT );
		}
	}

	private class ItemButton extends Button {
		
		public static final int HEIGHT	= 23;
		
		private Item item;
		
		private ItemSlot slot;
		private ColorBlock bg;
		private RenderedTextBlock name;
		
		public ItemButton( Item item ) {
			
			super();

			this.item = item;
			
			slot.item( item );
			if (item.cursed && item.cursedKnown) {
				bg.ra = +0.2f;
				bg.ga = -0.1f;
			} else if (!item.isIdentified()) {
				bg.ra = 0.1f;
				bg.ba = 0.1f;
			}
		}
		
		@Override
		protected void createChildren() {
			
			bg = new ColorBlock( 28, HEIGHT, 0x9953564D );
			add( bg );
			
			slot = new ItemSlot();
			add( slot );
			
			name = PixelScene.renderTextBlock( 7 );
			add( name );
			
			super.createChildren();
		}
		
		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;
			
			slot.setRect( x, y, 28, HEIGHT );
			PixelScene.align(slot);
			
			name.maxWidth((int)(width - slot.width() - 2));
			name.text(Messages.titleCase(item.name()));
			name.setPos(
					slot.right()+2,
					y + (height - name.height()) / 2
			);
			PixelScene.align(name);
			
			super.layout();
		}
		
		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}
		
		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}
		
		@Override
		protected void onClick() {
			Game.scene().add( new WndInfoItem( item ) );
		}
	}

	private class QuickSlotButton extends ItemSlot{

		public static final int HEIGHT	= 23;

		private Item item;
		private ColorBlock bg;

		QuickSlotButton(Item item){
			super(item);
			this.item = item;
		}

		@Override
		protected void createChildren() {
			bg = new ColorBlock( 28, HEIGHT, 0x9953564D );
			add( bg );

			super.createChildren();
		}

		@Override
		protected void layout() {
			bg.x = x;
			bg.y = y;

			super.layout();
		}

		@Override
		protected void onPointerDown() {
			bg.brightness( 1.5f );
			Sample.INSTANCE.play( Assets.Sounds.CLICK, 0.7f, 0.7f, 1.2f );
		}

		protected void onPointerUp() {
			bg.brightness( 1.0f );
		}

		@Override
		protected void onClick() {
			Game.scene().add(new WndInfoItem(item));
		}
	}
}
