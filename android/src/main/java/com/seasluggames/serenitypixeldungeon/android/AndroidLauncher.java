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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.widget.TextView;

import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.GdxNativesLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import androidx.annotation.NonNull;

public class AndroidLauncher extends Activity {

	private InterstitialAd mInterstitialAd;
	private final String TAG = "AndroidGame";
	
	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});

		AdRequest adRequest = new AdRequest.Builder().build();

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

		try {
			GdxNativesLoader.load();
			FreeType.initFreeType();
			
			Intent intent = new Intent(this, AndroidGame.class);
			startActivity(intent);
			finish();
		} catch (Exception e){
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

	private AdSize getFullWidthAdaptiveSize() {
		Display display = getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);

		float widthPixels = outMetrics.widthPixels;
		float density = outMetrics.density;

		int adWidth = (int) (widthPixels / density);
		return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
	}

	public void showInterstitialAd() {
		if (mInterstitialAd != null) {
			mInterstitialAd.show(this);
		} else {
			Log.d("TAG", "The interstitial wasn't loaded yet.");
		}
	}
}
