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
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.games.AchievementsClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayersClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;

public class AndroidLauncher extends Activity {

    public static RewardedAd myThat;
    private static InterstitialAd myThis;

    private final String TAG = "AndroidGame";
    public static Handler UIHandler;
    public static Handler signInHandler;
    public static Activity myActivity;
    public static boolean watchedAD = false;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 5001;

    // Client variables
    private AchievementsClient mAchievementsClient;
    private PlayersClient mPlayersClient;

    // The diplay name of the signed in user.
    private String mDisplayName = "";

    // Client used to sign in with Google APIs
    private static GoogleSignInClient mGoogleSignInClient;
    private static FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    FirebaseAuth mAuth;

    // achievements and scores we're pending to push to the cloud
    // (waiting for the user to sign in, for instance)
    private final AccomplishmentsOutbox mOutbox = new AccomplishmentsOutbox();


    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);

    }

    static {
        signInHandler = new Handler(Looper.getMainLooper());
    }

    public static void runSignIn(Runnable runnable) {
        signInHandler.post(runnable);

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myActivity = this;
        mDisplayName = "";

        MobileAds.initialize(this,
                "ca-app-pub-8412258401353384~2614412636");

        myThis = new InterstitialAd(this);

        myThis.setAdUnitId("ca-app-pub-8412258401353384/4072347428");
        reloadThis();

        myThis.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                reloadThis();
            }
        });


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

        mAuth = FirebaseAuth.getInstance();
        firebaseAuth = com.google.firebase.auth.FirebaseAuth.getInstance();
        authStateListener = firebaseAuth -> {
            // Get signedIn user
            FirebaseUser user = firebaseAuth.getCurrentUser();

            //if user is signed in, we call a helper method to save the user details to Firebase
            if (user != null) {
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            } else {
                // User is signed out
                Log.d(TAG, "onAuthStateChanged:signed_out");
            }
        };

        configureGoogleClient();

    }

    public PlayersClient getPlayersClient() {
        return mPlayersClient;
    }

    private class AccomplishmentsOutbox {
        boolean mEnlightened = false;


        boolean isEmpty() {
            return !mEnlightened;
        }

    }

    private void onConnected(GoogleSignInAccount googleSignInAccount) {
        Log.d(TAG, "onConnected(): connected to Google APIs");

        mAchievementsClient = Games.getAchievementsClient(myActivity, googleSignInAccount);

        // Set the greeting appropriately on main menu
        mPlayersClient.getCurrentPlayer()
                .addOnCompleteListener(new OnCompleteListener<Player>() {
                    @Override
                    public void onComplete(@NonNull Task<Player> task) {
                        String displayName;
                        if (task.isSuccessful()) {
                            displayName = task.getResult().getDisplayName();
                        } else {
                            Exception e = task.getException();
                            displayName = "???";

                        }
                        mDisplayName = displayName;
                    }
                });


        // if we have accomplishments to push, push them
        if (!mOutbox.isEmpty()) {
            pushAccomplishments();
            Toast.makeText(myActivity, "Congratulations!", Toast.LENGTH_SHORT).show();
        }

    }

    private void pushAccomplishments() {
        if (!isSignedIn()) {
            // can't push to the cloud, try again later
            return;
        }
        if (mOutbox.mEnlightened) {
            mAchievementsClient.unlock(getString(R.string.achievement_enlightened));
            mOutbox.mEnlightened = false;
        }
    }

    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(myActivity, gso);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static void signIn() {
        myActivity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(intent);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                onConnected(account);
            } catch (ApiException apiException) {
                String message = apiException.getMessage();
                if (message == null || message.isEmpty()) {
                    message = getString(R.string.signin_other_error);
                }

                new AlertDialog.Builder(myActivity)
                        .setMessage(message)
                        .setNeutralButton(android.R.string.ok, null)
                        .show();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(myActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            Toast.makeText(myActivity, "Firebase auth success", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(myActivity, "Firebase auth fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(myActivity,
                task -> {

                    if (task.isSuccessful()) {
                        Toast.makeText(myActivity,"Signed out", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static boolean isSignedIn() {
        return GoogleSignIn.getLastSignedInAccount(myActivity) != null;
    }

    public static void checkLogIn() {
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //if user is signed in, we call a helper method to save the user details to Firebase
        if (user != null) {
            Toast.makeText(myActivity, "You are logged in",
                    Toast.LENGTH_SHORT).show();
        } else {
            /*
            Toast.makeText(myActivity, "You are not logged in",
                    Toast.LENGTH_SHORT).show();

             */
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
            myThat.show(myActivity, adCallback);
        } else {
            reloadThat();
        }
    }
}
