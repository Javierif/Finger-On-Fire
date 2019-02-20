package com.dreamlab.android.vista;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Looper;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.Clasico;
import com.dreamlab.android.controlador.Kids;
import com.dreamlab.android.controlador.Main;
import com.dreamlab.android.controlador.Multijugador;
import com.dreamlab.android.controlador.OlaKAse;
import com.dreamlab.android.controlador.Record;
import com.dreamlab.android.controlador.Selector;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.ads.InterstitialAd;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.dreamlab.android.controlador.R;

public class MainView extends View implements AdListener {

	private Bitmap arcade;
	private Bitmap clasico;
	private Bitmap multijugador;
	private Bitmap salir;

	private int xArcade;
	private int yArcade;

	private int xMultijugador;
	private int yMultijugador;

	private int xClasico;
	private int yClasico;

	private int xSalir;
	private int ySalir;

	private Vibrator vibrar;
	private int contador;
	private Bitmap transparencia;

	private int xKid;

	private int yKid;

	private Bitmap kid;
	private Bitmap rate;
	private int xRate;
	private int yRate;
	private boolean intro = true;
	private Bitmap introduc;
	private InterstitialAd interstitialAd;
	private boolean mostrado = false;
	private int salto;
	private int saludo;

	public MainView(Context context) {
		super(context);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		editor.putInt("ejecutado", db.getInt("ejecutado", 0) + 1);
		editor.commit();
		if (db.getInt("ejecutado", 0) < 3 || db.getInt("ejecutado", 0) % 7 == 0)
			intro = false;
		vibrar = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		// Create an ad.
		interstitialAd = new InterstitialAd((Activity) context,
				"ca-app-pub-1905433910211565/4145024933");
		salto = AudioJuego.loadTemp(R.raw.salto);
		saludo = AudioJuego.loadTemp(R.raw.saludo);
		// Set the AdListener.
		interstitialAd.setAdListener(this);

		AdRequest adRequest = new AdRequest();
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR);
		interstitialAd.loadAd(adRequest);

		super.estrella = null;
		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				AudioJuego.para_intro();
				boolean retry = true;
				cargado = false;

				while (retry) {
					try {
						mainLoopThread.setRunning(false);
						mainLoopThread.setCargado(false);
						limpieza();
						mainLoopThread.join();
						retry = false;
						arcade.recycle();
						arcade = null;
						clasico.recycle();
						clasico = null;
						multijugador.recycle();
						multijugador = null;
						salir.recycle();
						salir = null;
						transparencia.recycle();
						transparencia = null;
						kid.recycle();
						kid = null;
						limpiaGc();

					} catch (InterruptedException e) {
					}
				}
			}

			public void surfaceCreated(SurfaceHolder holder) {
				redimensionarImagenes();
				inicializaPosiciones();
				iniciaMotor();
				terminadoCanvas = false;
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});

	}

	public void redimensionarImagenes() {
		if (isScalable()) {

			transparencia = redimensionarImagen(R.drawable.transparencia,
					redimension);
			arcade = redimensionarImagen(R.drawable.arcade, redimension);
			clasico = redimensionarImagen(R.drawable.clasico, redimension);
			multijugador = redimensionarImagen(R.drawable.multijugador,
					redimension);
			kid = redimensionarImagen(R.drawable.kids, redimension);
			rate = redimensionarImagen(R.drawable.rate, redimension);
			salir = redimensionarImagen(R.drawable.salir, redimension);

		}
	}

	public void inicializaPosiciones() {
		super.inicializaPosiciones();
		xMultijugador = mitadW - (multijugador.getWidth() / 2);
		yMultijugador = this.getHeight() - multijugador.getHeight();

		xArcade = mitadW - (arcade.getWidth() / 2);
		yArcade = this.getHeight() - (arcade.getHeight() * 2);

		xClasico = 0;
		yClasico = mitadH - (clasico.getHeight() / 2);

		xKid = 0;
		yKid = this.getHeight() - kid.getHeight();

		xRate = (int) (this.getWidth() - (rate.getWidth() + rate.getWidth() / 1.2));
		yRate = this.getHeight() - (rate.getHeight() + rate.getHeight() / 10);

		xSalir = this.getWidth() - salir.getWidth();
		ySalir = mitadH - (salir.getHeight() / 2);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!cargado || !intro)
			return true;
		if (!mostrado) {
			if(db.getInt("ejecutado", 0) < 3 || db.getInt("ejecutado", 0) % 7 == 0)
				llamarGoogle();
			mostrado = true;
			return true;
		}
		synchronized (getHolder()) {
			if (event.getX() > xSol && event.getX() < this.getWidth()
					&& event.getY() > 0 && event.getY() < heightSoles
					&& event.getAction() == 1) {
				contador++;
				if (contador >= 13) {
					Intent intent = new Intent(context, OlaKAse.class);
					context.startActivity(intent);
				}
			}
			// x2 > x && x2 < x + width && y2 > y && y2 < y + height;
			if (event.getAction() == 1
					&& tiempoUltimaAccion < System.currentTimeMillis() - 1000) {
				tiempoUltimaAccion = System.currentTimeMillis();
				if (event.getX() > xArcade
						&& event.getX() < xArcade + arcade.getWidth()
						&& event.getY() > yArcade
						&& event.getY() < yArcade + arcade.getHeight()) {

					Intent intent = new Intent(context, Selector.class);
					context.startActivity(intent);
					vibrar.vibrate(800);
				}

				if (event.getX() > xMultijugador
						&& event.getX() < xMultijugador
								+ multijugador.getWidth()
						&& event.getY() > yMultijugador
						&& event.getY() < yMultijugador
								+ multijugador.getHeight()) {

					Intent intent = new Intent(context, Multijugador.class);
					context.startActivity(intent);
					vibrar.vibrate(800);
				}

				if (event.getX() > xClasico
						&& event.getX() < xClasico + clasico.getWidth()
						&& event.getY() > yClasico
						&& event.getY() < yClasico + clasico.getHeight()) {

					Intent intent = new Intent(context, Clasico.class);
					context.startActivity(intent);
					vibrar.vibrate(800);
				}
				if (event.getX() > xKid && event.getX() < xKid + kid.getWidth()
						&& event.getY() > yKid
						&& event.getY() < yKid + kid.getHeight()) {

					Intent intent = new Intent(context, Kids.class);
					context.startActivity(intent);
					vibrar.vibrate(800);
				}

				if (event.getX() > xRate
						&& event.getX() < xRate + rate.getWidth()
						&& event.getY() > yRate
						&& event.getY() < yRate + rate.getHeight()) {
					if (db.getInt("conectado", 0) == 1)
						((Main) context).mostrarRecords();
					else
						llamarGoogle();
					//
					// Intent intent = new Intent(Intent.ACTION_VIEW);
					// intent.setData(Uri
					// .parse("market://details?id=com.trackeen.sea"));
					// context.startActivity(intent);
				}

				if (event.getX() > xSalir
						&& event.getX() < xSalir + salir.getWidth()
						&& event.getY() > ySalir
						&& event.getY() < ySalir + salir.getHeight()) {
					if (db.getInt("votado", 0) == 0)
						ventanaSalir();
					else
						System.exit(0);
				}
			}
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 4) {
			// do something on back.
			if (db.getInt("votado", 0) == 0)
				ventanaSalir();
			else
				System.exit(0);
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	private void ventanaSalir() {
		// poner aqui una comprobacion por si le ha dado a que si.
		AlertDialog.Builder alertSocial = new AlertDialog.Builder(context);
		alertSocial.setTitle("Valora la aplicación");
		alertSocial.setMessage(getResources().getString(R.string.salida));
		alertSocial.setPositiveButton(getResources().getString(R.string.votar),
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Toast mensaje = Toast.makeText(context, getResources()
								.getString(R.string.gracias),
								Toast.LENGTH_SHORT);
						mensaje.show();
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri
								.parse("market://details?id=com.dreamlab.android.controlador"));
						context.startActivity(intent);
						editor.putInt("votado", 1);
						editor.commit();
					}
				});
		alertSocial.setNegativeButton(getResources().getString(R.string.salir),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						System.exit(0);
					}
				});

		alertSocial.show();
	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			if (cargado) {
				if (intro) {
					try {
						super.draw(canvas);
						canvas.drawBitmap(transparencia, 0, 0, null);
						canvas.drawBitmap(arcade, xArcade, yArcade, null);
						canvas.drawBitmap(multijugador, xMultijugador,
								yMultijugador, null);
						canvas.drawBitmap(clasico, xClasico, yClasico, null);
						canvas.drawBitmap(kid, xKid, yKid, null);
						canvas.drawBitmap(rate, xRate, yRate, null);
						canvas.drawBitmap(salir, xSalir, ySalir, null);
					} catch (NullPointerException ex) {

					}
				} else {
					if (contador % 2 == 0) {
						if (contador % 3 == 0 && contador < 48
								|| contador == 60)
							AudioJuego.playTemp(salto);
						else if (contador == 66)
							AudioJuego.playTemp(saludo);
					}

					++contador;
					cargaImagen();
					canvas.drawBitmap(introduc, 0, 0, null);

					if (contador == 73) {
						contador = 0;
						intro = true;
						introduc.recycle();
						introduc = null;
						limpiaGc();
						AudioJuego.reproduceIntro(context, R.raw.fondointro);

					}
				}
			} else {
				terminadoCanvas = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void llamarGoogle() {
		if (db.getInt("conectado", 0) == 0) {
			// poner aqui una comprobacion por si le ha dado a que si.
			AlertDialog.Builder alertSocial = new AlertDialog.Builder(context);
			alertSocial.setTitle("Conectar con Google Play Services");

			alertSocial.setMessage(getResources().getString(
					R.string.playservices));
			
			alertSocial.setPositiveButton(
					getResources().getString(R.string.conectar),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							((Main) context).singUser();
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
		} else {
			((Main) context).singUser();
		}

	}

	private void cargaImagen() {

		introduc = null;

		switch (contador / 2) {
		case 1:
			introduc = redimensionarImagen(R.drawable.a1, this.getWidth(),
					this.getHeight());
			break;
		case 2:
			introduc = redimensionarImagen(R.drawable.a2, this.getWidth(),
					this.getHeight());
			break;
		case 3:
			introduc = redimensionarImagen(R.drawable.a3, this.getWidth(),
					this.getHeight());
			break;
		case 4:
			introduc = redimensionarImagen(R.drawable.a4, this.getWidth(),
					this.getHeight());
			break;
		case 5:
			introduc = redimensionarImagen(R.drawable.a5, this.getWidth(),
					this.getHeight());
			break;
		case 6:
			introduc = redimensionarImagen(R.drawable.a6, this.getWidth(),
					this.getHeight());
			break;
		case 7:
			introduc = redimensionarImagen(R.drawable.a7, this.getWidth(),
					this.getHeight());
			break;
		case 8:
			introduc = redimensionarImagen(R.drawable.a8, this.getWidth(),
					this.getHeight());
			break;
		case 9:
			introduc = redimensionarImagen(R.drawable.a9, this.getWidth(),
					this.getHeight());
			break;
		case 10:
			introduc = redimensionarImagen(R.drawable.a10, this.getWidth(),
					this.getHeight());
			break;
		case 11:
			introduc = redimensionarImagen(R.drawable.a11, this.getWidth(),
					this.getHeight());
			break;
		case 12:
			introduc = redimensionarImagen(R.drawable.a12, this.getWidth(),
					this.getHeight());
			break;
		case 13:
			introduc = redimensionarImagen(R.drawable.a13, this.getWidth(),
					this.getHeight());
			break;
		case 14:
			introduc = redimensionarImagen(R.drawable.a14, this.getWidth(),
					this.getHeight());
			break;
		case 15:
			introduc = redimensionarImagen(R.drawable.a15, this.getWidth(),
					this.getHeight());
			break;
		case 16:
			introduc = redimensionarImagen(R.drawable.a16, this.getWidth(),
					this.getHeight());
			break;
		case 17:
			introduc = redimensionarImagen(R.drawable.a17, this.getWidth(),
					this.getHeight());
			break;
		case 18:
			introduc = redimensionarImagen(R.drawable.a18, this.getWidth(),
					this.getHeight());
			break;
		case 19:
			introduc = redimensionarImagen(R.drawable.a19, this.getWidth(),
					this.getHeight());
			break;
		case 20:
			introduc = redimensionarImagen(R.drawable.a20, this.getWidth(),
					this.getHeight());
			break;
		case 21:
			introduc = redimensionarImagen(R.drawable.a21, this.getWidth(),
					this.getHeight());
			break;
		case 22:
			introduc = redimensionarImagen(R.drawable.a22, this.getWidth(),
					this.getHeight());
			break;
		case 23:
			introduc = redimensionarImagen(R.drawable.a23, this.getWidth(),
					this.getHeight());
			break;
		case 24:
			introduc = redimensionarImagen(R.drawable.a24, this.getWidth(),
					this.getHeight());
			break;

		case 25:
			introduc = redimensionarImagen(R.drawable.a25, this.getWidth(),
					this.getHeight());
			break;
		case 26:
			introduc = redimensionarImagen(R.drawable.a26, this.getWidth(),
					this.getHeight());
			break;
		case 27:
			introduc = redimensionarImagen(R.drawable.a27, this.getWidth(),
					this.getHeight());
			break;
		case 28:
			introduc = redimensionarImagen(R.drawable.a28, this.getWidth(),
					this.getHeight());
			break;
		case 29:
			introduc = redimensionarImagen(R.drawable.a29, this.getWidth(),
					this.getHeight());
			break;
		case 30:
			introduc = redimensionarImagen(R.drawable.a30, this.getWidth(),
					this.getHeight());
			break;
		case 31:
			introduc = redimensionarImagen(R.drawable.a31, this.getWidth(),
					this.getHeight());
			break;
		case 32:
			introduc = redimensionarImagen(R.drawable.a32, this.getWidth(),
					this.getHeight());
			break;
		case 33:
			introduc = redimensionarImagen(R.drawable.a33, this.getWidth(),
					this.getHeight());
			break;
		case 34:
			introduc = redimensionarImagen(R.drawable.a34, this.getWidth(),
					this.getHeight());
			break;
		case 35:
			introduc = redimensionarImagen(R.drawable.a35, this.getWidth(),
					this.getHeight());
			break;
		case 36:
			introduc = redimensionarImagen(R.drawable.a36, this.getWidth(),
					this.getHeight());
			break;
		case 37:
			introduc = redimensionarImagen(R.drawable.a37, this.getWidth(),
					this.getHeight());
			break;

		}
	}
}
