package com.dreamlab.android.controlador;

import com.dreamlab.android.vista.ArcadeView;
import com.dreamlab.android.controlador.R;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

public class OlaKAse extends BaseGameActivity {

	ArcadeView gameview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.olakase);
		SharedPreferences db = getSharedPreferences("datos",
				Context.MODE_PRIVATE);
		Editor editor = db.edit();
		MyGameProgress.AddLogro(db, editor, (BaseGameActivity) this,
				getResources().getString(R.string.achievement_ola_k_ase));

	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub

	}

}
