package com.dreamlab.android.controlador;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.dreamlab.android.controlador.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Record extends BaseGameActivity {

	private TextView[] r1;
	private TextView[] t1;
	private String area;
	private SharedPreferences db;
	private Editor editor;

	private static String ID_FB = "219037911592481";
	private Facebook facebook;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records);
		db = getSharedPreferences("datos", this.MODE_PRIVATE);
		editor = db.edit();

		Bundle bund = getIntent().getExtras();
		area = bund.getString("Area");
		r1 = new TextView[10];
		t1 = new TextView[10];

		t1[0] = (TextView) findViewById(R.id.t1);
		r1[0] = (TextView) findViewById(R.id.r1);

		t1[1] = (TextView) findViewById(R.id.t2);
		r1[1] = (TextView) findViewById(R.id.r2);

		t1[2] = (TextView) findViewById(R.id.t3);
		r1[2] = (TextView) findViewById(R.id.r3);

		t1[3] = (TextView) findViewById(R.id.t4);
		r1[3] = (TextView) findViewById(R.id.r4);

		t1[4] = (TextView) findViewById(R.id.t5);
		r1[4] = (TextView) findViewById(R.id.r5);

		t1[5] = (TextView) findViewById(R.id.t6);
		r1[5] = (TextView) findViewById(R.id.r6);

		t1[6] = (TextView) findViewById(R.id.t7);
		r1[6] = (TextView) findViewById(R.id.r7);

		t1[7] = (TextView) findViewById(R.id.t8);
		r1[7] = (TextView) findViewById(R.id.r8);

		t1[8] = (TextView) findViewById(R.id.t9);
		r1[8] = (TextView) findViewById(R.id.r9);

		t1[9] = (TextView) findViewById(R.id.t10);
		r1[9] = (TextView) findViewById(R.id.r10);
		if (area.toString().charAt(2) == 'r')
			nuevoRecord(Integer.parseInt(area.toString().substring(1, 2)));
		actualizaTablas();

	}

	public void nuevoRecord(final int posicion) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		if (area.charAt(0) == 'M')
			alert.setTitle(db.getString("MGanador", "").equals("0") ? getResources()
					.getString(R.string.empate) : (getResources().getString(
					R.string.Mrecord)
					+ " " + db.getString("MGanador", "")));
		else
			alert.setTitle(getResources().getString(R.string.record));
		alert.setMessage(getResources().getString(R.string.name));
		final EditText input = new EditText(this);
		alert.setView(input);
		final int posiciones = posicion + 1;
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				for (int i = posiciones; i < 10; i++) {
					editor.putString(area.charAt(0) + "" + (i + 1) + "t",
							db.getString(area.charAt(0) + "" + i + "t", ""));
					editor.putString(area.charAt(0) + "" + (i + 1) + "r",
							db.getString(area.charAt(0) + "" + i + "r", ""));
				}

				editor.putString(area.charAt(0) + "" + posiciones + "t", input
						.getText().toString().equals("") ? "........" : input
						.getText().toString());
				if (posiciones == 1)
					if (area.charAt(0) == 'C')
						editor.putString(
								area.charAt(0) + "" + posiciones + "r",
								db.getString("clasico", ""));
					else
						editor.putString(
								area.charAt(0) + "" + posiciones + "r",
								db.getString("multi", ""));
				else
					editor.putString(area.charAt(0) + "" + posiciones + "r",
							db.getString(area.charAt(0) + "T", ""));
				// editor.putInt("clasico", tiempoTardado);
				editor.commit();
				actualizaTablas();
				AlertDialog.Builder alertSocial = new AlertDialog.Builder(
						Record.this);
				alertSocial.setTitle("Facebook");
				alertSocial.setMessage("¿Quieres publicarlo en Facebook?");
				alertSocial.setPositiveButton("Publicar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								publicarMuro(input.getText().toString());
							}
						});
				alertSocial.setNegativeButton("No gracias",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {

							}
						});

				alertSocial.show();
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {

					}
				});

		alert.show();
	}

	public void actualizaTablas() {
		for (int i = 1; i < t1.length; i++) {
			t1[i].setText(db.getString(area.charAt(0) + "" + i + "t", ""));
			r1[i].setText(db.getString(area.charAt(0) + "" + i + "r", ""));
		}
	}

	@SuppressWarnings("deprecation")
	private void publicarMuro(final String nombre) {
		// TODO Auto-generated method stub

		// Creamos un objeto facebook con el id de nuestra app
		facebook = new Facebook(ID_FB);
		// Ejecutamos la sesion de facebook
		new AsyncFacebookRunner(facebook);

		// en función del estado de la sesión hacemos una cosa u otra
		if (!facebook.isSessionValid()) {
			// si la sesion es valida mostramos en pantalla que hemos accedido
			// correctamente y
			// publicamos el mensaje con el método mensaje_en_el_muro()
			facebook.authorize(this, new String[] {}, new DialogListener() {

				public void onComplete(Bundle values) {

					Toast mensaje = Toast.makeText(Record.this, getResources()
							.getString(R.string.facebookbien),
							Toast.LENGTH_SHORT);
					mensaje.show();
					// escribimos el mensaje en el muro
					mensaje_En_El_Muro(nombre);
				}

				// Si la sesion devuelve un error mostramos un mensaje de error

				@Override
				public void onFacebookError(FacebookError error) {

					Toast mensaje = Toast.makeText(Record.this, getResources()
							.getString(R.string.facebookmal),
							Toast.LENGTH_SHORT);
					mensaje.show();
				}

				@Override
				public void onError(DialogError e) {

					Toast mensaje = Toast.makeText(Record.this,
							"Ha ocurrido un error al intentar iniciar sesión",
							Toast.LENGTH_SHORT);
					mensaje.show();
				}

				@Override
				public void onCancel() {
				}
			});
		}
	}

	private void mensaje_En_El_Muro(String nombre) {
		// TODO Auto-generated method stub

		Bundle params = new Bundle();

		// introducimos dentro del objeto Bundle el texto que introduzcamos en
		// los editText.
		params.putString(
				"description",
				getResources().getString(R.string.facebook1)
						+ db.getString(area.charAt(0) + "T", "")
						+ getResources().getString(R.string.facebook2));
		params.putString("name", "Finger on Fire - Game Android");
		params.putString("caption", "New Record! by " + nombre);
		params.putString("picture",
				"http://www.dreamlabstudio.net/icon/ic_launcher.gif");
		params.putString("link", "https://play.google.com/store/apps/details?id=com.dreamlab.android.controlador");
		// ejecutamos un dialogo con el objeto Bundle rellenado
		facebook.dialog(Record.this, "feed", params, new SampleDialogListener());
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try{
			facebook.authorizeCallback(requestCode, resultCode, data);
		} catch(Exception e){
			
		}
		
	}

	public class SampleDialogListener extends BaseDialogListener implements
			DialogListener {

		public void onComplete(Bundle values) {
			Toast mensaje = Toast.makeText(Record.this, "Enviado",
					Toast.LENGTH_SHORT);
			mensaje.show();
		}

		@Override
		public void onFacebookError(FacebookError e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(DialogError e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}
	}

	public void scoreInternet(View view) {
		if (db.getInt("conectado", 0) == 1)
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(
							getResources().getString(
									R.string.leaderboard_the_best_score)), 10);
		else {
			// poner aqui una comprobacion por si le ha dado a que si.
			AlertDialog.Builder alertSocial = new AlertDialog.Builder(this);
			alertSocial.setTitle("Conectar con Google Play Services");
			alertSocial.setMessage(getResources().getString(
					R.string.playservices));
			alertSocial.setPositiveButton(
					getResources().getString(R.string.conectar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							beginUserInitiatedSignIn();
						}
					});
			alertSocial.setNegativeButton(
					getResources().getString(R.string.salir),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {

						}
					});

			alertSocial.show();
		}

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