/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
 *
 * Serenity Pixel Dungeon
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

    public static RewardedAd mRewardedAd;
    private static InterstitialAd mInterstitialAd;

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
        // V19.7.0


        MobileAds.initialize(this, initializationStatus -> {
        });

        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712",
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }

                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });

        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "onAdFailedToLoad");
                    }
                });

         */

        /*
        // TEST ID
        MobileAds.initialize(this,
                "ca-app-pub-3940256099942544~3347511713");

         */
        MobileAds.initialize(this,
                "ca-app-pub-8412258401353384~2614412636");

        mInterstitialAd = new InterstitialAd(this);
        /*
        // TEST ID
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");

         */
        mInterstitialAd.setAdUnitId("ca-app-pub-8412258401353384/4072347428");
        reloadInterstitialAd();

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reloadInterstitialAd();
            }
        });

        /*
        // TEST ID
        mRewardedAd = new RewardedAd(this,
                "ca-app-pub-3940256099942544/5224354917");

         */
        mRewardedAd = new RewardedAd(this,
                "ca-app-pub-8412258401353384/2891127693");

        reloadRewardedAd();

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

    public static void reloadInterstitialAd() {
        if (!mInterstitialAd.isLoaded()) {
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
        } else {
            Log.d("TAG", "ad is already loaded");
        }

    }

    public static void reloadRewardedAd() {
        if (!mRewardedAd.isLoaded()) {
            RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
                @Override
                public void onRewardedAdLoaded() {
                }

                @Override
                public void onRewardedAdFailedToLoad(LoadAdError adError) {
                }
            };
            Log.d("TAG", "Loading new ad");
            mRewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        } else {
            Log.d("TAG", "ad is already loaded");
        }

    }

    public static void showInterstitialAd() {
        /*
        //V19.7.0

        if (mInterstitialAd != null) {
            mInterstitialAd.show(admobActivity);
            Log.d("TAG", "Showing ad");
        } else {
            Log.d("TAG", "The interstitial wasn't loaded yet.");
        }

         */
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
            reloadInterstitialAd();
        } else {
            reloadInterstitialAd();
            Log.d("TAG", "Loading new ad");
        }
    }

    public static void showRewardedAd() {
        /*
        //V19.7
        if (mRewardedAd != null) {

            mRewardedAd.show(admobActivity, rewardItem -> {
                // Handle the reward.
                Log.d("TAG", "The user earned the reward.");
                int rewardAmount = rewardItem.getAmount();

            });
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
        }

         */
        if (mRewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    reloadRewardedAd();
                }

                @Override
                public void onRewardedAdClosed() {
                    reloadRewardedAd();
                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    Dungeon.gold += 1000;
                    watchedAD = true;
                    reloadRewardedAd();
                }

                @Override
                public void onRewardedAdFailedToShow(AdError adError) {
                    // Ad failed to display.
                    reloadRewardedAd();
                }
            };
            mRewardedAd.show(admobActivity, adCallback);
        } else {
            reloadRewardedAd();
            Log.d("TAG", "The rewarded ad wasn't loaded yet.");
        }
    }
}
