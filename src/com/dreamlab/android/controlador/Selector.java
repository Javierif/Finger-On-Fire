package com.dreamlab.android.controlador;

import com.dreamlab.android.vista.SelectorView;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;


public class Selector extends Activity {

	SelectorView gameview;

	@Override

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gameview = new SelectorView(this);
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

