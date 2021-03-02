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

package com.seasluggames.serenitypixeldungeon.android.ui;

import com.seasluggames.serenitypixeldungeon.android.Dungeon;
import com.seasluggames.serenitypixeldungeon.android.SPDMain;
import com.seasluggames.serenitypixeldungeon.android.items.Generator;
import com.seasluggames.serenitypixeldungeon.android.items.Item;
import com.seasluggames.serenitypixeldungeon.android.items.Recipe;
import com.seasluggames.serenitypixeldungeon.android.items.bombs.Bomb;
import com.seasluggames.serenitypixeldungeon.android.items.food.Blandfruit;
import com.seasluggames.serenitypixeldungeon.android.items.food.Food;
import com.seasluggames.serenitypixeldungeon.android.items.food.MeatPie;
import com.seasluggames.serenitypixeldungeon.android.items.food.MysteryMeat;
import com.seasluggames.serenitypixeldungeon.android.items.food.Pasty;
import com.seasluggames.serenitypixeldungeon.android.items.food.StewedMeat;
import com.seasluggames.serenitypixeldungeon.android.items.potions.AlchemicalCatalyst;
import com.seasluggames.serenitypixeldungeon.android.items.potions.Potion;
import com.seasluggames.serenitypixeldungeon.android.items.potions.brews.BlizzardBrew;
import com.seasluggames.serenitypixeldungeon.android.items.potions.brews.CausticBrew;
import com.seasluggames.serenitypixeldungeon.android.items.potions.brews.InfernalBrew;
import com.seasluggames.serenitypixeldungeon.android.items.potions.brews.ShockingBrew;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfAquaticRejuvenation;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfArcaneArmor;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfDragonsBlood;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfHoneyedHealing;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfIcyTouch;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfMight;
import com.seasluggames.serenitypixeldungeon.android.items.potions.elixirs.ElixirOfToxicEssence;
import com.seasluggames.serenitypixeldungeon.android.items.potions.exotic.ExoticPotion;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.Scroll;
import com.seasluggames.serenitypixeldungeon.android.items.scrolls.exotic.ExoticScroll;
import com.seasluggames.serenitypixeldungeon.android.items.spells.Alchemize;
import com.seasluggames.serenitypixeldungeon.android.items.spells.AquaBlast;
import com.seasluggames.serenitypixeldungeon.android.items.spells.ArcaneCatalyst;
import com.seasluggames.serenitypixeldungeon.android.items.spells.BeaconOfReturning;
import com.seasluggames.serenitypixeldungeon.android.items.spells.CurseInfusion;
import com.seasluggames.serenitypixeldungeon.android.items.spells.FeatherFall;
import com.seasluggames.serenitypixeldungeon.android.items.spells.MagicalInfusion;
import com.seasluggames.serenitypixeldungeon.android.items.spells.MagicalPorter;
import com.seasluggames.serenitypixeldungeon.android.items.spells.PhaseShift;
import com.seasluggames.serenitypixeldungeon.android.items.spells.ReclaimTrap;
import com.seasluggames.serenitypixeldungeon.android.items.spells.Recycle;
import com.seasluggames.serenitypixeldungeon.android.items.spells.WildEnergy;
import com.seasluggames.serenitypixeldungeon.android.items.stones.Runestone;
import com.seasluggames.serenitypixeldungeon.android.messages.Messages;
import com.seasluggames.serenitypixeldungeon.android.plants.Plant;
import com.seasluggames.serenitypixeldungeon.android.scenes.AlchemyScene;
import com.seasluggames.serenitypixeldungeon.android.scenes.PixelScene;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;
import com.seasluggames.serenitypixeldungeon.android.windows.WndBag;
import com.seasluggames.serenitypixeldungeon.android.windows.WndInfoItem;
import com.watabou.noosa.BitmapText;
import com.watabou.noosa.Group;
import com.watabou.noosa.Image;
import com.watabou.noosa.ui.Component;
import com.watabou.utils.Reflection;

import java.util.ArrayList;
import java.util.Arrays;

public class QuickRecipe extends Component {
	
	private ArrayList<Item> ingredients;
	
	private ArrayList<ItemSlot> inputs;
	private QuickRecipe.arrow arrow;
	private ItemSlot output;
	
	public QuickRecipe(Recipe.SimpleRecipe r){
		this(r, r.getIngredients(), r.sampleOutput(null));
	}
	
	public QuickRecipe(Recipe r, ArrayList<Item> inputs, final Item output) {
		
		ingredients = inputs;
		int cost = r.cost(inputs);
		boolean hasInputs = true;
		this.inputs = new ArrayList<>();
		for (final Item in : inputs) {
			anonymize(in);
			ItemSlot curr;
			curr = new ItemSlot(in) {
				@Override
				protected void onClick() {
					SPDMain.scene().addToFront(new WndInfoItem(in));
				}
			};
			
			ArrayList<Item> similar = Dungeon.hero.belongings.getAllSimilar(in);
			int quantity = 0;
			for (Item sim : similar) {
				//if we are looking for a specific item, it must be IDed
				if (sim.getClass() != in.getClass() || sim.isIdentified()) quantity += sim.quantity();
			}
			
			if (quantity < in.quantity()) {
				curr.sprite.alpha(0.3f);
				hasInputs = false;
			}
			curr.showExtraInfo(false);
			add(curr);
			this.inputs.add(curr);
		}
		
		if (cost > 0) {
			arrow = new arrow(Icons.get(Icons.ARROW), cost);
			arrow.hardlightText(0x00CCFF);
		} else {
			arrow = new arrow(Icons.get(Icons.ARROW));
		}
		if (hasInputs) {
			arrow.icon.tint(1, 1, 0, 1);
			if (!(SPDMain.scene() instanceof AlchemyScene)) {
				arrow.enable(false);
			}
		} else {
			arrow.icon.color(0, 0, 0);
			arrow.enable(false);
		}
		add(arrow);
		
		anonymize(output);
		this.output = new ItemSlot(output){
			@Override
			protected void onClick() {
				SPDMain.scene().addToFront(new WndInfoItem(output));
			}
		};
		if (!hasInputs){
			this.output.sprite.alpha(0.3f);
		}
		this.output.showExtraInfo(false);
		add(this.output);
		
		layout();
	}
	
	@Override
	protected void layout() {
		
		height = 16;
		width = 0;
		
		for (ItemSlot item : inputs){
			item.setRect(x + width, y, 16, 16);
			width += 16;
		}
		
		arrow.setRect(x + width, y, 14, 16);
		width += 14;
		
		output.setRect(x + width, y, 16, 16);
		width += 16;
	}
	
	//used to ensure that un-IDed items are not spoiled
	private void anonymize(Item item){
		if (item instanceof Potion){
			((Potion) item).anonymize();
		} else if (item instanceof Scroll){
			((Scroll) item).anonymize();
		}
	}
	
	public class arrow extends IconButton {
		
		BitmapText text;
		
		public arrow(){
			super();
		}
		
		public arrow( Image icon ){
			super( icon );
		}
		
		public arrow( Image icon, int count ){
			super( icon );
			text = new BitmapText( Integer.toString(count), PixelScene.pixelFont);
			text.measure();
			add(text);
		}
		
		@Override
		protected void layout() {
			super.layout();
			
			if (text != null){
				text.x = x;
				text.y = y;
				PixelScene.align(text);
			}
		}
		
		@Override
		protected void onClick() {
			super.onClick();
			
			//find the window this is inside of and close it
			Group parent = this.parent;
			while (parent != null){
				if (parent instanceof Window){
					((Window) parent).hide();
					break;
				} else {
					parent = parent.parent;
				}
			}
			
			((AlchemyScene) SPDMain.scene()).populate(ingredients, Dungeon.hero.belongings);
		}
		
		public void hardlightText(int color ){
			if (text != null) text.hardlight(color);
		}
	}
	
	//gets recipes for a particular alchemy guide page
	//a null entry indicates a break in section
	public static ArrayList<QuickRecipe> getRecipes( int pageIdx ){
		ArrayList<QuickRecipe> result = new ArrayList<>();
		switch (pageIdx){
			case 0: default:
				result.add(new QuickRecipe( new Potion.SeedToPotion(), new ArrayList<>(Arrays.asList(new Plant.Seed.PlaceHolder().quantity(3))), new WndBag.Placeholder(ItemSpriteSheet.POTION_HOLDER){
					@Override
					public String name() {
						return Messages.get(Potion.SeedToPotion.class, "name");
					}

					@Override
					public String info() {
						return "";
					}
				}));
				return result;
			case 1:
				Recipe r = new Scroll.ScrollToStone();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					if (!scroll.isKnown()) scroll.anonymize();
					ArrayList<Item> in = new ArrayList<Item>(Arrays.asList(scroll));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 2:
				result.add(new QuickRecipe( new StewedMeat.oneMeat() ));
				result.add(new QuickRecipe( new StewedMeat.twoMeat() ));
				result.add(new QuickRecipe( new StewedMeat.threeMeat() ));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new MeatPie.Recipe(),
						new ArrayList<Item>(Arrays.asList(new Pasty(), new Food(), new MysteryMeat.PlaceHolder())),
						new MeatPie()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe( new Blandfruit.CookFruit(),
						new ArrayList<>(Arrays.asList(new Blandfruit(), new Plant.Seed.PlaceHolder())),
						new Blandfruit(){

							public String name(){
								return Messages.get(Blandfruit.class, "cooked");
							}
							
							@Override
							public String info() {
								return "";
							}
						}));
				return result;
			case 3:
				r = new Bomb.EnhanceBomb();
				int i = 0;
				for (Class<?> cls : Bomb.EnhanceBomb.validIngredients.keySet()){
					if (i == 2){
						result.add(null);
						i = 0;
					}
					Item item = (Item) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(new Bomb(), item));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
					i++;
				}
				return result;
			case 4:
				r = new ExoticPotion.PotionToExotic();
				for (Class<?> cls : Generator.Category.POTION.classes){
					Potion pot = (Potion) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(pot, new Plant.Seed.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 5:
				r = new ExoticScroll.ScrollToExotic();
				for (Class<?> cls : Generator.Category.SCROLL.classes){
					Scroll scroll = (Scroll) Reflection.newInstance(cls);
					ArrayList<Item> in = new ArrayList<>(Arrays.asList(scroll, new Runestone.PlaceHolder().quantity(2)));
					result.add(new QuickRecipe( r, in, r.sampleOutput(in)));
				}
				return result;
			case 6:
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Plant.Seed.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(new QuickRecipe(new AlchemicalCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Potion.PlaceHolder(), new Runestone.PlaceHolder())), new AlchemicalCatalyst()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Runestone.PlaceHolder())), new ArcaneCatalyst()));
				result.add(new QuickRecipe(new ArcaneCatalyst.Recipe(), new ArrayList<>(Arrays.asList(new Scroll.PlaceHolder(), new Plant.Seed.PlaceHolder())), new ArcaneCatalyst()));
				return result;
			case 7:
				result.add(new QuickRecipe(new CausticBrew.Recipe()));
				result.add(new QuickRecipe(new InfernalBrew.Recipe()));
				result.add(new QuickRecipe(new BlizzardBrew.Recipe()));
				result.add(new QuickRecipe(new ShockingBrew.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new ElixirOfHoneyedHealing.Recipe()));
				result.add(new QuickRecipe(new ElixirOfMight.Recipe()));
				result.add(new QuickRecipe(new ElixirOfAquaticRejuvenation.Recipe()));
				result.add(new QuickRecipe(new ElixirOfDragonsBlood.Recipe()));
				result.add(new QuickRecipe(new ElixirOfIcyTouch.Recipe()));
				result.add(new QuickRecipe(new ElixirOfToxicEssence.Recipe()));
				result.add(new QuickRecipe(new ElixirOfArcaneArmor.Recipe()));
				return result;
			case 8:
				result.add(new QuickRecipe(new MagicalPorter.Recipe()));
				result.add(new QuickRecipe(new PhaseShift.Recipe()));
				result.add(new QuickRecipe(new WildEnergy.Recipe()));
				result.add(new QuickRecipe(new BeaconOfReturning.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new AquaBlast.Recipe()));
				result.add(new QuickRecipe(new FeatherFall.Recipe()));
				result.add(new QuickRecipe(new ReclaimTrap.Recipe()));
				result.add(null);
				result.add(null);
				result.add(new QuickRecipe(new CurseInfusion.Recipe()));
				result.add(new QuickRecipe(new MagicalInfusion.Recipe()));
				result.add(new QuickRecipe(new Alchemize.Recipe()));
				result.add(new QuickRecipe(new Recycle.Recipe()));
				return result;
		}
	}
	
}