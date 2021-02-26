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

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.seasluggames.serenitypixeldungeon.SPDSettings;
import com.seasluggames.serenitypixeldungeon.SerenityPixelDungeon;
import com.watabou.noosa.Game;
import com.watabou.utils.FileUtils;

import androidx.annotation.NonNull;

public class AndroidGame extends AndroidApplication {
	
	public static AndroidApplication instance;
	
	private static AndroidPlatformSupport support;

	private InterstitialAd mInterstitialAd;
	private RewardedAd mRewardedAd;
	private final String TAG = "AndroidGame";
	
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		AdRequest adRequest = new AdRequest.Builder().build();

		RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917",
				adRequest, new RewardedAdLoadCallback(){
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

		InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712",
				adRequest, new InterstitialAdLoadCallback(){
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


		//there are some things we only need to set up on first launch
		if (instance == null) {

			instance = this;

			try {
				Game.version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			} catch (PackageManager.NameNotFoundException e) {
				Game.version = "???";
			}
			try {
				Game.versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
			} catch (PackageManager.NameNotFoundException e) {
				Game.versionCode = 0;
			}

			FileUtils.setDefaultFileProperties(Files.FileType.Local, "");

			// grab preferences directly using our instance first
			// so that we don't need to rely on Gdx.app, which isn't initialized yet.
			// Note that we use a different prefs name on android for legacy purposes,
			// this is the default prefs filename given to an android app (.xml is automatically added to it)
			SPDSettings.set(instance.getPreferences("SerenityPixelDungeon"));

		} else {
			instance = this;
		}
		
		//set desired orientation (if it exists) before initializing the app.
		if (SPDSettings.landscape() != null) {
			instance.setRequestedOrientation( SPDSettings.landscape() ?
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE :
					ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			//use rgb888 on more modern devices for better visuals
			config.r = config.g = config.b = 8;
		} else {
			//and rgb565 (default) on older ones for better performance
		}
		
		config.useCompass = false;
		config.useAccelerometer = false;
		
		if (support == null) support = new AndroidPlatformSupport();
		else                 support.reloadGenerators();
		
		support.updateSystemUI();
		
		initialize(new SerenityPixelDungeon(support), config);
		
	}

	@Override
	protected void onResume() {
		//prevents weird rare cases where the app is running twice
		if (instance != this){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
				finishAndRemoveTask();
			} else {
				finish();
			}
		}
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		//do nothing, game should catch all back presses
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		support.updateSystemUI();
	}
	
	@Override
	public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
		super.onMultiWindowModeChanged(isInMultiWindowMode);
		support.updateSystemUI();
	}

	private AdSize getFullWidthAdaptiveSize() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float widthPixels = outMetrics.widthPixels;
		float density = outMetrics.density;

		int adWidth = (int) (widthPixels / density);
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}

	public void showRewardedAd() {
		if (mRewardedAd != null) {
			Activity activityContext = AndroidGame.this;
			mRewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
				@Override
				public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
					// Handle the reward.
					Log.d("TAG", "The user earned the reward.");
					int rewardAmount = rewardItem.getAmount();
					String rewardType = rewardItem.getType();
				}
			});
		} else {
			Log.d("TAG", "The rewarded ad wasn't ready yet.");
		}
	}

	public void showInterstitialAd() {
		if (mInterstitialAd != null) {
			mInterstitialAd.show(this);
		} else {
			Log.d("TAG", "The interstitial wasn't loaded yet.");
		}
	}
}