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

package com.seasluggames.serenitypixeldungeon.android.items.weapon.melee;

import com.seasluggames.serenitypixeldungeon.android.Assets;
import com.seasluggames.serenitypixeldungeon.android.actors.Char;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.android.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Handwraps extends MeleeWeapon {

    {
        image = ItemSpriteSheet.GLOVES;
        hitSound = Assets.Sounds.HIT;
        hitSoundPitch = 1f;

        tier = 1;
        DLY = 0.5f; //2x speed

        bones = false;
    }

    @Override
    public int max(int lvl) {
        return Math.round(5f * (tier + 1)) +     //10 base
                lvl * Math.round(1f * (tier + 1));  //+2 per level
    }

    @Override
    public int damageRoll(Char owner) {
        if (owner instanceof Hero) {
            Hero hero = (Hero) owner;

            int diff = max() - min();
            int damage = augment.damageFactor(Random.NormalIntRange(
                    min() + Math.round(diff * 0.50f),
                    max()));

            damage += Random.IntRange(0, hero.STR/2);

            return damage;
        }
        return super.damageRoll(owner);
    }

}
