package com.dreamlab.android.controlador;

import com.dreamlab.android.vista.ArcadeView;
import com.dreamlab.android.vista.MainView;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.dreamlab.android.controlador.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class Main extends BaseGameActivity implements OnClickListener {

	MainView mainview;
	private SignInButton signButton;
	private SharedPreferences db;
	private Editor editor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mainview = new MainView(this);
		db = getSharedPreferences("datos", this.MODE_PRIVATE);
		editor = db.edit();
		RelativeLayout layout = new RelativeLayout(this);
		layout.addView(mainview);
		try{
			signButton = new SignInButton(this);
			signButton.setVisibility(View.GONE);
			layout.addView(signButton);
			signButton.setOnClickListener(this);
		} catch(Exception e){
			
		}
		
		this.setContentView(layout);

		// //ESTO ES EL ANUNCIO DE ADMOB
		// AdView admobView = new AdView((Activity) this, AdSize.BANNER,
		// "ca-app-pub-1905433910211565/5562882537");
		// RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.WRAP_CONTENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		// admobView.setLayoutParams(lp);
		//
		// RelativeLayout layout = new RelativeLayout(this);
		// layout.addView(mainview);
		// layout.addView(admobView);
		// AdRequest pedida = new AdRequest();
		// pedida.addTestDevice(AdRequest.TEST_EMULATOR);
		// pedida.addTestDevice("TEST_DEVICE_ID");
		// admobView.loadAd(pedida);
		// this.setContentView(layout);
		//

		// //ESTO ES PARA SACAR LOS CONTACTOS
		// // Query: contacts with phone shorted by name
		// Cursor mCursor = getContentResolver().query(
		// Data.CONTENT_URI,
		// new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER,
		// Phone.TYPE },
		// Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
		// + Phone.NUMBER + " IS NOT NULL", null,
		// Data.DISPLAY_NAME + " ASC");
		//
		// startManagingCursor(mCursor);
		//
		// ListAdapter adapter = new SimpleCursorAdapter(this, // context
		// android.R.layout.simple_list_item_2, // Layout for the rows
		// mCursor, // cursor
		// new String[] { Data.DISPLAY_NAME, Phone.NUMBER }, // cursor
		// // fields
		// new int[] { android.R.id.text1, android.R.id.text2 } // view
		// // fields
		// );
		// for(int i = 0; i<adapter.getCount();i++){
		// Cursor c = (Cursor) adapter.getItem(i);
		// int colIdx = c.getColumnIndex(Phone.NUMBER);
		// String phone = c.getString(colIdx);

		// //ESTO ES PARA ENVIAR UNA PETICION HTTP
		// HttpHandler handler = new HttpHandler();
		// String txt =
		// handler.post("http://127.0.0.1/fof/index.php","1",phone);
		//
		//
		// System.out.println(" el telefono es -> " + phone);
		// }
	}

	private void comprobadorDeRecords() {

		
		if (db.getInt("angels", 0) > 50	&& db.getInt("angels", 0) < 60 && db.getInt("conectado",0)==1)
			getGamesClient().unlockAchievement(getResources().getString(R.string.achievement_god_walking_amongst_mere_mortals));

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

	public void singUser() {
		beginUserInitiatedSignIn();
	}

	public void mostrarRecords() {
		startActivityForResult(getGamesClient().getAchievementsIntent(), 6);

	}

	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSignInSucceeded() {
		Toast mensaje = Toast.makeText(this,
				getResources().getString(R.string.facebookbien),
				Toast.LENGTH_SHORT);
		mensaje.show();

		editor.putInt("conectado", 1);
		editor.commit();
		comprobadorDeRecords();
	}

	@Override
	public void onClick(View arg0) {
		beginUserInitiatedSignIn();

	}

}
