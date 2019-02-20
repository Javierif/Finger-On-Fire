package com.dreamlab.android.controlador;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.dreamlab.android.vista.KidsView;


public class Kids extends Activity {

	KidsView gameview;

	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameview = new KidsView(this);
		setContentView(gameview);
	}
	
	@Override
	
	protected void onStart() {
		super.onStart();
		this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

	@Override

	protected void onStop() {
		super.onStop();
		this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}

}

