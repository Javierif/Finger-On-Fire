package com.dreamlab.android.vista;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.MainLoopThread;
import com.dreamlab.android.modelo.Niveles;
import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.InterstitialAd;
import com.dreamlab.android.controlador.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.SoundPool;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

//ponerle abstract
public abstract class View extends SurfaceView implements AdListener {

	protected Context context;

	protected MainLoopThread mainLoopThread;
	protected Editor editor;
	protected SharedPreferences db;

	protected Paint paint;

	protected Bitmap estrellaVacia;
	protected Bitmap estrella;
	protected Bitmap fondo;
	protected Bitmap numeros;
	protected Bitmap soles;
	protected Bitmap ganastes;
	protected Bitmap bmpVentanaArcade;

	public float redimension;
	protected int textSize = 30;
	protected int mitadH;
	protected int mitadW;
	protected int heightSoles;
	protected int widthSoles;
	protected int heightNumeros;
	protected int widthNumeros;
	protected int xSol;
	protected int ySol;
	protected int yDecena;
	protected int xDecena;
	protected int yUnidad;
	protected int xUnidad;
	protected int tiempoUnidad = 1;
	protected int tiempoDecena = 1;
	protected int tiempoActualiza = 0;
	protected int srcYNumerosD;
	protected int srcYNumerosU;
	protected int columnaSoles;
	protected int filaSoles;
	protected int corteXSol;
	protected int corteYSol;
	protected int contadorTiempo;
	protected int nivelActual = -1;
	protected double tiempoUltimaAccion;
	private Integer[] nubesX = new Integer[3];
	private Integer[] nubesY = new Integer[3];

	private boolean execute = false;
	protected boolean ventana = true;
	protected boolean finTiempo = false;
	protected boolean tiempoLoop = true;
	protected boolean reproduceUnico = true;
	protected boolean acabadoTiempo = false;
	protected Object Lock = new Object();
	protected Bitmap nubes;
	protected boolean terminadoCanvas;

	protected Bitmap personaje;

	protected int widthPersonaje;
	protected String texto = "";

	protected Bitmap cajonTexto;
	protected Typeface font;
	protected Bitmap estrellaRecord;

	protected Bitmap estrellaReVacia;

	protected Bitmap humo;

	protected int widthHumo;

	protected int heightHumo;

	protected Bitmap cartel1;
	protected Bitmap cartel2;

	protected int xCartel1;

	protected int yCartel1;

	protected int xCartel2;

	protected int yCartel2;
	protected boolean cargado = false;
	protected ArrayList<Niveles> nivelesConf;

	protected InterstitialAd interstitialAd;

	protected SoundPool soundTemp;
	protected int victory;

	protected int click;

	protected int clang;

	protected int nivelintro;

	protected int score;

	protected int looser;

	protected int clink;

	public View(Context context) {
		super(context);
		this.context = context;
		limpiaGc();

		font = Typeface
				.createFromAsset(context.getAssets(), "comboRegular.ttf");
		if (!(this instanceof SelectorView) && !(this instanceof MainView)) {

			interstitialAd = new InterstitialAd((Activity) context,
					"ca-app-pub-1905433910211565/4145024933");

			// Set the AdListener.
			interstitialAd.setAdListener(this);

			AdRequest adRequest = new AdRequest();
			interstitialAd.loadAd(adRequest);

		}
		
		if (!(this instanceof SelectorView)) {
			db = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
			editor = db.edit();
			if (nivelActual < 23) {
				for (int i = 0; i < 3; i++) {
					nubesX[i] = 0;
					nubesY[i] = 0;
				}
			}

			soundTemp = AudioJuego.creaSoundTemp(context);
			victory = AudioJuego.loadTemp(R.raw.victory);
			click = AudioJuego.loadTemp(R.raw.click);
			clink = AudioJuego.loadTemp(R.raw.clink);
			clang = AudioJuego.loadTemp(R.raw.clang);
			looser = AudioJuego.loadTemp(R.raw.looser);
			nivelintro = AudioJuego.loadTemp(R.raw.nivelintro);
			score = AudioJuego.loadTemp(R.raw.score);

		}

		if (execute) {
			getHolder().addCallback(new SurfaceHolder.Callback() {

				public void surfaceDestroyed(SurfaceHolder holder) {

				}

				public void surfaceCreated(SurfaceHolder holder) {

				}

				public void surfaceChanged(SurfaceHolder holder, int format,
						int width, int height) {
				}
			});
			// TODO Auto-generated constructor stub
		}
	}

	protected void limpiaGc() {

		System.gc();
		Runtime.getRuntime().gc();
	}

	/** Called when an ad is clicked and about to return to the application. */

	public void onDismissScreen(Ad ad) {

	}

	/** Called when an ad was not received. */
	public void onFailedToReceiveAd(Ad ad, AdRequest.ErrorCode error) {

	}

	/**
	 * Called when an ad is clicked and going to start a new Activity that will
	 * leave the application (e.g. breaking out to the Browser or Maps
	 * application).
	 */
	public void onLeaveApplication(Ad ad) {

	}

	/**
	 * Called when an Activity is created in front of the app (e.g. an
	 * interstitial is shown, or an ad is clicked and launches a new Activity).
	 */
	public void onPresentScreen(Ad ad) {
		Toast.makeText(context, getResources().getString(R.string.sentimos),
				Toast.LENGTH_LONG).show();
	}

	/** Called when an ad is received. */
	public void onReceiveAd(Ad ad) {

	}

	protected void mostrarAds() {
		if (interstitialAd.isReady() && (db.getInt("partidasAds", 2) % 5 == 0))
			interstitialAd.show();
		editor.putInt("partidasAds", db.getInt("partidasAds", 0) + 1);
		editor.commit();

	}

	protected void iniciaMotor() {
		mainLoopThread = new MainLoopThread(this);
		mainLoopThread.setRunning(true);
		mainLoopThread.start();
		mainLoopThread.setCargado(true);
		cargado = true;
		limpiaGc();
		if (!(this instanceof MainView) && !(this instanceof SelectorView))
			tiempo();
		else if ((this instanceof SelectorView))
			AudioJuego.reproduceIntro(context, R.raw.fondointro);
	}

	public Editor getEditor() {
		return editor;
	}

	public SharedPreferences getDB() {
		return db;
	}

	protected void tiempo() {
		final Thread thread = new Thread(new Runnable() {

			public void run() {
				tiempoLoop = true;
				while (tiempoLoop) {

					if (!finTiempo && !ventana) {
						actualizaTiempo();
					}

					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		thread.start();
	}

	protected void limpieza() {
		fondo.recycle();
		if (!(this instanceof MainView)) {
			estrella.recycle();
			estrella = null;
			estrellaVacia.recycle();
			estrellaVacia = null;
		}
		if (!(this instanceof MainView) && !(this instanceof SelectorView)) {
			ganastes.recycle();
			ganastes = null;
			bmpVentanaArcade.recycle();
			bmpVentanaArcade = null;
			numeros.recycle();
			numeros = null;
			cartel1.recycle();
			cartel1 = null;
			cartel2.recycle();
			cartel2 = null;
		}
		if (this instanceof ClasicoView || this instanceof MultijugadorView) {

			estrellaRecord.recycle();
			estrellaRecord = null;
			estrellaReVacia.recycle();
			estrellaReVacia = null;
			humo.recycle();
			humo = null;

		}
		if (!(this instanceof SelectorView)) {
			nubes.recycle();
			nubes = null;
			soles.recycle();
			soles = null;
		}

	}

	protected void inicializaPosiciones() {

		mitadH = (this.getHeight() / 2);
		mitadW = (this.getWidth() / 2);
		if (!(this instanceof SelectorView)) {
			this.heightSoles = soles.getHeight() / 3;
			this.widthSoles = soles.getWidth() / 3;
			xSol = this.getWidth() - widthSoles;
			ySol = 0;

			for (int i = 0; i < 3; i++) {
				nubesX[i] = (int) (this.getWidth() + ((nubes.getWidth() * (1 + (((Math
						.random() * 4) + 1) / 10))) * i));
				nubesY[i] = (int) ((Math.random() * (nubes.getHeight() / 2)) + nubes
						.getHeight());
			}
		}

		if (!(this instanceof MainView) && !(this instanceof SelectorView)) {
			this.heightNumeros = numeros.getHeight();
			this.widthNumeros = numeros.getWidth() / 10;
			yDecena = 0;
			xDecena = mitadW - widthNumeros;

			yUnidad = 0;
			xUnidad = (mitadW);

			xCartel1 = 0;
			yCartel1 = this.getHeight() - cartel1.getHeight();

			xCartel2 = this.getWidth() - cartel2.getWidth();
			yCartel2 = yCartel1;
		}

		if (this instanceof ClasicoView || this instanceof MultijugadorView) {
			widthHumo = humo.getWidth() / 3;
			heightHumo = humo.getHeight() / 2;
		}
	}

	// x2 > x && x2 < x + width && y2 > y && y2 < y + height
	public boolean reintentar(float x, float y) {
		if (x > 0 && x < mitadW && y > yCartel1 && y < this.getHeight())
			return true;
		return false;
	}

	public boolean boton2(float x, float y) {
		if (x > mitadW && x < this.getWidth() && y > yCartel2
				&& y < this.getHeight())
			return true;
		return false;
	}

	protected boolean isScalable() {
		textSize = 30;
		if (!(this.getWidth() == 400 && this.getHeight() == 800)) {
			redimension = ((float) this.getWidth() + this.getHeight()) / 1354;
			fondo = redimensionarImagen(R.drawable.fondogeneral,
					this.getWidth(), this.getHeight());
			textSize = (int) (textSize * redimension);
			if (!(this instanceof MainView)) {
				estrella = redimensionarImagen(R.drawable.estrellavencer,
						redimension);
				estrellaVacia = redimensionarImagen(R.drawable.estrellavacia,
						redimension);
			}
			if (!(this instanceof MainView) && !(this instanceof SelectorView)) {
				ganastes = redimensionarImagen(R.drawable.pantallavictoria,
						this.getWidth(), this.getHeight());
				bmpVentanaArcade = redimensionarImagen(R.drawable.mensaje,
						redimension);
				numeros = redimensionarImagen(R.drawable.numeros, true,
						redimension, 1, 5, false);
				cartel1 = redimensionarImagen(R.drawable.cartel1, redimension);
				cartel2 = redimensionarImagen(R.drawable.cartel2, redimension);
			}
			if (this instanceof ClasicoView || this instanceof MultijugadorView) {

				estrellaRecord = redimensionarImagen(
						R.drawable.estrellarecordcillo, redimension);
				estrellaReVacia = redimensionarImagen(
						R.drawable.estrellavaciarecordcillo, redimension);
				humo = redimensionarImagen(R.drawable.smoke, true, redimension,
						2, 3, false);

			}
			if (!(this instanceof SelectorView)) {
				nubes = redimensionarImagen(R.drawable.nubes, redimension);

				soles = redimensionarImagen(R.drawable.soles, true,
						redimension, 3, 3, false);
			}
			return true;
		}
		return false;
	}

	public void actualizaTiempo() {
		tiempoUnidad -= 1;

		if (tiempoUnidad < 0 && tiempoDecena != 0) {
			tiempoDecena -= 1;
			tiempoUnidad = 9;

		}

		if ((tiempoUnidad == 0 && tiempoDecena == 0) || tiempoDecena < 0) {
			finTiempo = true;
		}

		srcYNumerosD = tiempoDecena * widthNumeros;
		srcYNumerosU = tiempoUnidad * widthNumeros;
	}

	public void moveSoles() {
		if (columnaSoles == 8) {
			columnaSoles = 0;
			filaSoles += 1;
		}
		if (filaSoles == 3) {
			filaSoles = 0;
		}
		columnaSoles += 1;

		corteXSol = (columnaSoles / 3) * heightSoles;
		corteYSol = filaSoles * widthSoles;
	}

	public Bitmap redimensionarImagen(int bitmap, double w, double h) {

		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false;
		bfOptions.inPurgeable = true;
		bfOptions.inInputShareable = true;
		bfOptions.inTempStorage = new byte[32 * 1024];
		bfOptions.inSampleSize = (int) redimension ^ (-1);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), bitmap,
				bfOptions);

		return Bitmap.createScaledBitmap(bm, (int) w, (int) h, false);
	}

	/**
	 * Redimensionar un Bitmap.
	 * 
	 * @param Bitmap
	 * @param float newHeight
	 * @param float newHeight
	 * @param float newHeight
	 * @return Bitmap
	 */
	public Bitmap redimensionarImagen(int bitmap, float redimension) {
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // Disable Dithering mode
		bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
		bfOptions.inInputShareable = true; // Which kind of reference will be
											// used to recover the Bitmap data
											// after being clear, when it will
											// be used in the future
		bfOptions.inTempStorage = new byte[32 * 1024];
		bfOptions.inSampleSize = (int) redimension ^ (-1);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), bitmap,
				bfOptions);

		return Bitmap.createScaledBitmap(bm,
				((int) (bm.getWidth() * redimension)),
				((int) (bm.getHeight() * redimension)), false);
	}

	// Como minimo siempre habrá una fila o una columna
	public Bitmap redimensionarImagen(int bitmap, boolean calculoexacto,
			float redimension, int filas, int columnas, boolean girar) {

		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // Disable Dithering mode
		bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
		bfOptions.inInputShareable = true; // Which kind of reference will be
											// used to recover the Bitmap data
											// after being clear, when it will

		bfOptions.inTempStorage = new byte[32 * 1024];
		bfOptions.inSampleSize = (int) redimension ^ (-1);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), bitmap,
				bfOptions);

		float heightNuevo;
		float widthNuevo;

		heightNuevo = ((bm.getHeight() * redimension) / filas);
		heightNuevo = Math.round(heightNuevo);
		widthNuevo = ((bm.getWidth() * redimension) / columnas);
		widthNuevo = Math.round(widthNuevo);

		heightNuevo = calculoExacto(heightNuevo, filas);
		widthNuevo = calculoExacto(widthNuevo, columnas);

		if (!girar) {
			return Bitmap.createScaledBitmap(bm, (int) widthNuevo,
					(int) heightNuevo, false);
		} else {
			bm = Bitmap.createScaledBitmap(bm, (int) widthNuevo,
					(int) heightNuevo, true);
			float[] mirrorX = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };

			Matrix matrixMirror = new Matrix();
			matrixMirror.setValues(mirrorX);

			Matrix matrox = new Matrix();
			matrox.postConcat(matrixMirror);
			bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
					matrox, false);
			return bm;
		}
	}

	public float calculoExacto(float calcular, int cantidad) {
		String numCadena = calcular + "";
		numCadena = numCadena.charAt(numCadena.length() - 3) + "";
		int redondeo = Integer.parseInt(numCadena);
		int resto = 1;
		while (resto != 0) {
			if (redondeo > 5)
				++calcular;
			else
				--calcular;
			resto = (int) (calcular % cantidad);
		}
		calcular = (int) calcular * cantidad;
		return calcular;
	}

	public Bitmap rotarImagen(int bitmap, float rotate) {
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // Disable Dithering mode
		bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
		bfOptions.inInputShareable = true; // Which kind of reference will be
											// used to recover the Bitmap data
											// after being clear, when it will
											// be used in the future
		bfOptions.inTempStorage = new byte[32 * 1024];
		bfOptions.inSampleSize = (int) redimension ^ (-1);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), bitmap,
				bfOptions);

		bm = Bitmap.createScaledBitmap(bm,
				((int) (bm.getWidth() * redimension)),
				(int) (bm.getHeight() * redimension), false);

		Matrix matrox = new Matrix();
		matrox.preRotate(rotate);

		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
				matrox, true);
	}

	public Bitmap girarImagen(int bitmap) {
		BitmapFactory.Options bfOptions = new BitmapFactory.Options();
		bfOptions.inDither = false; // Disable Dithering mode
		bfOptions.inPurgeable = true; // Tell to gc that whether it needs free
										// memory, the Bitmap can be cleared
		bfOptions.inInputShareable = true; // Which kind of reference will be
											// used to recover the Bitmap data
											// after being clear, when it will
											// be used in the future
		bfOptions.inTempStorage = new byte[32 * 1024];
		bfOptions.inSampleSize = (int) redimension ^ (-1);

		Bitmap bm = BitmapFactory.decodeResource(getResources(), bitmap,
				bfOptions);

		bm = Bitmap.createScaledBitmap(bm,
				((int) (bm.getWidth() * redimension)),
				(int) (bm.getHeight() * redimension), false);

		float[] mirrorX = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };

		Matrix matrixMirror = new Matrix();
		matrixMirror.setValues(mirrorX);

		Matrix matrox = new Matrix();
		matrox.postConcat(matrixMirror);

		return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
				matrox, true);
	}

	public void onDraw(Canvas canvas) {
	}

	public void draw(Canvas canvas) {
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setARGB(255, 160, 210, 75);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.SERIF);
		canvas.drawBitmap(fondo, 0, 0, null);
		moveSoles();
		if (nivelActual < 24 && !(this instanceof SelectorView)) {

			Rect srcSolesP1C = new Rect(corteXSol, corteYSol, corteXSol
					+ heightSoles, corteYSol + widthSoles);
			Rect dstSolesP1C = new Rect(xSol, ySol, xSol + widthSoles, ySol
					+ heightSoles);
			canvas.drawBitmap(soles, srcSolesP1C, dstSolesP1C, null);
			for (int i = 0; i < 3; i++) {
				canvas.drawBitmap(nubes, nubesX[i], nubesY[i], null);
				if (nubesX[i] + nubes.getWidth() < 0 && nubesX[i] != 0)
					switch (i) {
					case 0:
						nubesX[i] = (int) (nubesX[2] + ((nubes.getWidth() * (1 + (((Math
								.random() * 3) + 1) / 10)))));
						break;
					case 1:
						nubesX[i] = (int) (nubesX[0] + ((nubes.getWidth() * (1 + (((Math
								.random() * 3) + 1) / 10)))));
						break;
					case 2:
						nubesX[i] = (int) (nubesX[1] + ((nubes.getWidth() * (1 + (((Math
								.random() * 3) + 1) / 10)))));
						break;
					}

				else
					nubesX[i] -= 3;
			}
		}
		if (!ventana) {
			if (this instanceof ArcadeView) {
				if (nivelesConf.get(0).isBoss()) {
					Rect srcNumerosD = new Rect(srcYNumerosD, 0, srcYNumerosD
							+ widthNumeros, heightNumeros);
					Rect dstNumerosD = new Rect(xDecena, yDecena, xDecena
							+ widthNumeros, yDecena + heightNumeros);
					canvas.drawBitmap(numeros, srcNumerosD, dstNumerosD, null);

					Rect srcNumerosU = new Rect(srcYNumerosU, 0, srcYNumerosU
							+ widthNumeros, heightNumeros);
					Rect dstNumerosU = new Rect(xUnidad, yUnidad, xUnidad
							+ widthNumeros, yUnidad + heightNumeros);
					canvas.drawBitmap(numeros, srcNumerosU, dstNumerosU, null);
				} else if (Integer.parseInt(tiempoDecena + "" + tiempoUnidad) < 31) {
					Rect srcNumerosD = new Rect(srcYNumerosD, 0, srcYNumerosD
							+ widthNumeros, heightNumeros);
					Rect dstNumerosD = new Rect(xDecena, yDecena, xDecena
							+ widthNumeros, yDecena + heightNumeros);
					canvas.drawBitmap(numeros, srcNumerosD, dstNumerosD, null);

					Rect srcNumerosU = new Rect(srcYNumerosU, 0, srcYNumerosU
							+ widthNumeros, heightNumeros);
					Rect dstNumerosU = new Rect(xUnidad, yUnidad, xUnidad
							+ widthNumeros, yUnidad + heightNumeros);
					canvas.drawBitmap(numeros, srcNumerosU, dstNumerosU, null);
				}
			} else if ((Integer.parseInt(tiempoDecena + "" + tiempoUnidad) < 31)) {
				Rect srcNumerosD = new Rect(srcYNumerosD, 0, srcYNumerosD
						+ widthNumeros, heightNumeros);
				Rect dstNumerosD = new Rect(xDecena, yDecena, xDecena
						+ widthNumeros, yDecena + heightNumeros);
				canvas.drawBitmap(numeros, srcNumerosD, dstNumerosD, null);

				Rect srcNumerosU = new Rect(srcYNumerosU, 0, srcYNumerosU
						+ widthNumeros, heightNumeros);
				Rect dstNumerosU = new Rect(xUnidad, yUnidad, xUnidad
						+ widthNumeros, yUnidad + heightNumeros);
				canvas.drawBitmap(numeros, srcNumerosU, dstNumerosU, null);
			}
		} else if (!(this instanceof MainView)
				&& !(this instanceof SelectorView)
				&& !(nivelActual == 0 || (nivelActual == 23)
						|| nivelActual == 24 || nivelActual == 47
						|| nivelActual == 48 || nivelActual == 47)) {
			canvas.drawBitmap(bmpVentanaArcade,
					mitadW - (bmpVentanaArcade.getWidth() / 2), 0, null);
			paint.setShadowLayer(1.5f, -1, 1, Color.BLACK);
			paint.setTypeface(font);
			paint.setTextSize(textSize * 2);// corregir esto por que mensaje
											// esta partido en dos con \n
			canvas.drawText(getResources().getString(R.string.mensaje),
					mitadW
							- ((getResources().getString(R.string.mensaje)
									.length() - 2) * textSize / 2),
					textSize * 2, paint);
			canvas.drawText(getResources().getString(R.string.mensaje2),
					(mitadW - ((getResources().getString(R.string.mensaje2)
							.length() - 2) * textSize / 2))
							- textSize, textSize * 4, paint);

		}

	}
}
