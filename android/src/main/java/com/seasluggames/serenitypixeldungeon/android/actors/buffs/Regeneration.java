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

package com.seasluggames.serenitypixeldungeon.android.actors.buffs;

import com.seasluggames.serenitypixeldungeon.android.Dungeon;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Hero;
import com.seasluggames.serenitypixeldungeon.android.actors.hero.Talent;
import com.seasluggames.serenitypixeldungeon.android.items.artifacts.ChaliceOfBlood;
import com.seasluggames.serenitypixeldungeon.android.items.rings.RingOfEnergy;

public class Regeneration extends Buff {

    {
        //unlike other buffs, this one acts after the hero and takes priority against other effects
        //healing is much more useful if you get some of it off before taking damage
        actPriority = HERO_PRIO - 1;
    }

    private static final float REGENERATION_DELAY = 10;

    @Override
    public boolean act() {
        if (target.isAlive()) {

            if (!((Hero) target).isStarving()) {
                if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_PROTECTION)) {
                    double shield = 0;

                    switch (Dungeon.hero.pointsInTalent(Talent.BLESSING_OF_PROTECTION)) {
                        case 1:
                            shield = Math.round(Dungeon.hero.HT * 0.1);
                            break;
                        case 2:
                            shield = Math.round(Dungeon.hero.HT * 0.15);
                            break;
                        case 3:
                            shield = Math.round(Dungeon.hero.HT * 0.20);
                            break;
                    }
                    int curShield = 0;
                    if (Dungeon.hero.buff(Barrier.class) != null) curShield = Dungeon.hero.buff(Barrier.class).shielding();

                    if (target.HP < regencap()) {
                        LockedFloor lock = target.buff(LockedFloor.class);
                        if (target.HP > 0 && (lock == null || lock.regenOn())) {
                            if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_REGENERATION)) {
                                target.HP += Dungeon.hero.pointsInTalent(Talent.BLESSING_OF_REGENERATION) + 1;
                            } else {
                                target.HP += 1;
                            }
                        }
                    } else {
                        if (target.HP < (regencap() + shield)) {
                            if (curShield <= 0) {
                                int shieldValue = (int) shield;
                                Buff.affect(Dungeon.hero, Barrier.class).incShield(shieldValue);
                            }
                        } else {
                            ((Hero) target).resting = false;
                        }
                    }

                } else {
                    if (target.HP < regencap() && !((Hero) target).isStarving()) {
                        LockedFloor lock = target.buff(LockedFloor.class);
                        if (target.HP > 0 && (lock == null || lock.regenOn())) {
                            if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_REGENERATION)) {
                                target.HP += Dungeon.hero.pointsInTalent(Talent.BLESSING_OF_REGENERATION) + 1;
                            } else {
                                target.HP += 1;
                            }
                        }
                    } else {
                        ((Hero) target).resting = false;
                    }
                }
            }

            /*

            if (target.HP < regencap() && !((Hero) target).isStarving()) {
                LockedFloor lock = target.buff(LockedFloor.class);
                if (target.HP > 0 && (lock == null || lock.regenOn())) {
                    if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_REGENERATION)) {
                        target.HP += Dungeon.hero.pointsInTalent(Talent.BLESSING_OF_REGENERATION) + 1;
                    } else {
                        target.HP += 1;
                    }
                }
            } else {
                int maxShield = Math.round(Dungeon.hero.HT * 0.1f * Dungeon.hero.pointsInTalent(Talent.BLESSING_OF_PROTECTION));
                if (target.HP < regencap() + maxShield) {
                    if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_PROTECTION)){
                        int effect = maxShield/10;
                        int shield = 0;
                        if (Dungeon.hero.hasTalent(Talent.BLESSING_OF_PROTECTION)){
                            shield = effect;
                            int curShield = 0;
                            if (Dungeon.hero.buff(Barrier.class) != null) curShield = Dungeon.hero.buff(Barrier.class).shielding();
                            shield = Math.min(shield, maxShield-curShield);
                        }

                        if (shield > 0) {
                            Buff.affect(Dungeon.hero, Barrier.class).incShield(shield);
                            Dungeon.hero.sprite.showStatus( CharSprite.POSITIVE, Messages.get(Dewdrop.class, "shield", shield) );
                        }
                    } else {
                        ((Hero) target).resting = false;
                    }
                }
            }

             */

            ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class);

            float delay = REGENERATION_DELAY;
            if (regenBuff != null) {
                if (regenBuff.isCursed()) {
                    delay *= 1.5f;
                } else {
                    delay -= regenBuff.itemLevel() * 0.9f;
                    delay /= RingOfEnergy.artifactChargeMultiplier(target);
                }
            }
            spend(delay);

        } else {

            deactivate();

        }

        return true;
    }

    public int regencap() {
        return target.HT;
    }
}
