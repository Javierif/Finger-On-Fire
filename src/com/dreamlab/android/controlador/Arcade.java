package com.dreamlab.android.controlador;

import com.dreamlab.android.vista.ArcadeView;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.WindowManager;

public class Arcade extends BaseGameActivity {

	ArcadeView gameview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bund = getIntent().getExtras();
		gameview = new ArcadeView(this, bund.getInt("nivel"));
		setContentView(gameview);
	}

	@Override
	protected void onStart() {
		super.onStart();
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override
	protected void onStop() {
		super.onStop();
		this.getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	
	
	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSignInSucceeded() {
		SharedPreferences db = getSharedPreferences("datos", Context.MODE_PRIVATE);
		Editor editor = db.edit();
		MyGameProgress.CompruebaLogros(db, editor, this);
	}

}
