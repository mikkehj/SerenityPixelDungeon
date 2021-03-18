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

package com.seasluggames.serenitypixeldungeon.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import androidx.annotation.NonNull;

public class AndroidLauncher extends Activity {

    public static RewardedAd myThat;
    private static InterstitialAd myThis;

    private final String TAG = "AndroidGame";
    public static Handler UIHandler;
    public static Activity admobActivity;
    public static boolean watchedAD = false;

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        admobActivity = this;

        /*
        // TEST ID
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

         */
        MobileAds.initialize(this,
                "ca-app-pub-8412258401353384~2614412636");

        myThis = new InterstitialAd(this);
        /*
        // TEST ID
        myThis.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

         */
        myThis.setAdUnitId("ca-app-pub-8412258401353384/4072347428");
        reloadThis();

        myThis.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reloadThis();
            }
        });

        /*
        // TEST ID
        myThat = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

         */
        myThat = new RewardedAd(this,
                "ca-app-pub-8412258401353384/2891127693");

        reloadThat();

        try {
            GdxNativesLoader.load();
            FreeType.initFreeType();

            Intent intent = new Intent(this, AndroidGame.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            TextView text = new TextView(this);
            text.setText("Serenity Pixel Dungeon cannot start because some of its code is missing!\n\n" +
                    "This usually happens when the Google Play version of the game is installed from somewhere outside of Google Play.\n\n" +
                    "If you're unsure of how to fix this, please email the developer (seasluggames@gmail.com), and include this error message:\n\n" +
                    e.getMessage());
            text.setTextSize(16);
            text.setTextColor(0xFFFFFFFF);
            text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/pixel_font.ttf"));
            text.setGravity(Gravity.CENTER_VERTICAL);
            text.setPadding(10, 10, 10, 10);
            setContentView(text);
        }
    }

    public static void reloadThis() {
        if (!myThis.isLoaded()) {
            myThis.loadAd(new AdRequest.Builder().build());
        }
    }

    public static void reloadThat() {
        if (!myThat.isLoaded()) {
            RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
                @Override
                public void onRewardedAdLoaded() {
                }

                @Override
                public void onRewardedAdFailedToLoad(LoadAdError adError) {
                }
            };
            myThat.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        }
    }

    public static void doThis() {
        if (myThis.isLoaded()) {
            myThis.show();
            reloadThis();
        } else {
            reloadThis();
        }
    }

    public static void doThat() {
        if (myThat.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    reloadThat();
                }

                @Override
                public void onRewardedAdClosed() {
                    reloadThat();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    Dungeon.gold += 1000;
                    watchedAD = true;
                    reloadThat();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    reloadThat();
                }
            };
            myThat.show(admobActivity, adCallback);
        } else {
            reloadThat();
        }
    }
}
