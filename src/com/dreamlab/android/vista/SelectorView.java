package com.dreamlab.android.vista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.dreamlab.android.controlador.Arcade;
import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.R;

public class SelectorView extends View {

	private Vibrator vibrar;

	private Bitmap dedo;
	private Bitmap candado;
	private Bitmap fondo2;
	private Bitmap fondo3;

	private int xDedoInicial;
	private int yDedoInicial;
	private int textSize = 40;
	private int pantalla;

	private Bitmap siguiente;
	private Bitmap atras;

	private Bitmap estrellita;

	private Bitmap estrellaRe;
	private boolean cargando = false;
	private Integer[] color = new Integer[3];

	public SelectorView(Context context) {
		super(context);
		this.context = context;
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		db = context.getSharedPreferences("datos", Context.MODE_PRIVATE);
		vibrar = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		AudioJuego.reproduce(context, R.raw.fondointro);
		color[0] = (int) (Math.random() * 155) + 90;
		color[1] = (int) (Math.random() * 200) + 20;
		color[2] = (int) (Math.random() * 255);
		tiempoUltimaAccion = System.currentTimeMillis() - 1000;
		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				AudioJuego.para_musica();
				boolean retry = true;
				cargado = false;
				mainLoopThread.setRunning(false);
				mainLoopThread.setCargado(false);
				while (retry) {
					try {
							mainLoopThread.join();
							retry = false;
							limpieza();
							dedo.recycle();
							dedo = null;
							candado.recycle();
							candado = null;
							fondo2.recycle();
							fondo2 = null;
							fondo3.recycle();
							fondo3 = null;
							siguiente.recycle();
							siguiente = null;
							atras.recycle();
							atras = null;
							estrellita.recycle();
							atras = null;
							estrellaRe.recycle();
							estrellaRe = null;
							System.gc();
							Runtime.getRuntime().gc();
						
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

	public void limpieza() {
		fondo.recycle();
		fondo2.recycle();
		fondo3.recycle();
		dedo.recycle();
		siguiente.recycle();
		candado.recycle();
		estrellita.recycle();
		atras.recycle();
	}

	public void redimensionarImagenes() {
		textSize = 40;
		if (!(this.getWidth() == 480 && this.getHeight() == 854)) {
			redimension = ((float) this.getWidth() + this.getHeight()) / 1354;

			fondo = redimensionarImagen(R.drawable.selector, this.getWidth(),
					this.getHeight());
			fondo2 = redimensionarImagen(R.drawable.selector2, this.getWidth(),
					this.getHeight());
			fondo3 = redimensionarImagen(R.drawable.selector3, this.getWidth(),
					this.getHeight());
			dedo = redimensionarImagen(R.drawable.dedo, redimension);
			siguiente = redimensionarImagen(R.drawable.siguiente, redimension);
			candado = redimensionarImagen(R.drawable.candado, redimension);
			estrellaRe = redimensionarImagen(R.drawable.estrella, redimension);
			estrellita = redimensionarImagen(R.drawable.estrellitas,
					redimension);
			atras = girarImagen(R.drawable.siguiente);
			textSize = (int) (textSize * redimension);
		}
		cargando = false;

	}

	public void inicializaPosiciones() {
		yDedoInicial = this.getHeight() - dedo.getHeight();
		xDedoInicial = (this.getWidth() - (dedo.getWidth() * 8)) / 2;
		super.inicializaPosiciones();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!cargado)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!cargando) {
			if (!cargado)
				return true;
			// x2 > x && x2 < x + width && y2 > y && y2 < y + height
			synchronized (getHolder()) {

				if (event.getAction() == 1) {
					if (tiempoUltimaAccion < System.currentTimeMillis() - 1000) {
						tiempoUltimaAccion = System.currentTimeMillis();
						if (event.getX() > xDedoInicial
								&& event.getX() < xDedoInicial
										+ dedo.getWidth() * 8
								&& event.getY() > this.getHeight()
										- dedo.getHeight() * 3
								&& event.getY() < this.getHeight()) {
							int xnivel = (int) ((event.getX() - xDedoInicial) / dedo
									.getWidth());
							int ynivel = (int) (((event.getY() - (this
									.getHeight() - dedo.getHeight() * 3)) / dedo
									.getHeight()));
							int nivel = xnivel + (ynivel * 8) + (pantalla * 24);
							if (nivel < 48
									&& (db.getInt(nivel + "", 0) == 2
											|| db.getInt(nivel + "", 0) == 1 || nivel == 0)) {
								Intent intent = new Intent(context,
										Arcade.class);
								intent.putExtra("nivel", nivel);
								context.startActivity(intent);
								vibrar.vibrate(800);
								xDedoInicial = 0;
								yDedoInicial = 0;
								cargando = true;
							}
							if (nivel > 47
									&& (db.getInt("contador", 0) > (nivel - 11))) {
								Intent intent = new Intent(context,
										Arcade.class);
								intent.putExtra("nivel", nivel);
								context.startActivity(intent);
								vibrar.vibrate(800);
								xDedoInicial = 0;
								yDedoInicial = 0;
								cargando = true;
							}
						}
					}

					if (event.getX() > 0 && event.getX() < siguiente.getWidth()
							&& event.getY() > 0
							&& event.getY() < siguiente.getHeight()
							&& pantalla != 0) {
						pantalla -= 1;
						vibrar.vibrate(30);
					}

					if (event.getX() > this.getWidth() - siguiente.getWidth()
							&& event.getX() < this.getWidth()
							&& event.getY() > 0
							&& event.getY() < siguiente.getHeight()
							&& pantalla != 2) {
						pantalla += 1;
						vibrar.vibrate(30);
					}

				}
			}
		} else {
			color[0] = (int) (Math.random() * 255);
			color[1] = (int) (Math.random() * 255);
			color[2] = (int) (Math.random() * 255);
		}
		return true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		if (cargado) {
			try {
				if (!cargando) {
					if (pantalla == 0) {
						canvas.drawBitmap(fondo, 0, 0, null);
						canvas.drawBitmap(siguiente, this.getWidth()
								- siguiente.getWidth(), 0, null);
					} else if (pantalla == 1) {
						canvas.drawBitmap(fondo2, 0, 0, null);
						canvas.drawBitmap(siguiente, this.getWidth()
								- siguiente.getWidth(), 0, null);
						canvas.drawBitmap(atras, 0, 0, null);
					} else {
						canvas.drawBitmap(fondo3, 0, 0, null);
						canvas.drawBitmap(atras, 0, 0, null);
					}

					Paint paint = new Paint();
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					paint.setARGB(255, 160, 210, 75);
					paint.setStyle(Paint.Style.STROKE);
					paint.setShadowLayer(1.5f, -1, 1, Color.LTGRAY);
					paint.setTypeface(font);
					paint.setTextSize(textSize * 2);
					canvas.drawText(
							getResources().getString(R.string.selecciona),
							mitadW
									- ((getResources().getString(
											R.string.selecciona).length() - 2)
											* textSize / 2), textSize * 2,
							paint);
					paint.setTextSize(textSize);
					paint.setARGB(255, 160, 210, 75);
					canvas.drawBitmap(estrellaRe, (this.getWidth() / 2)
							- estrellaRe.getWidth(), atras.getHeight(), null);
					canvas.drawText(
							" X " + String.valueOf(db.getInt("contador", 0)),
							(this.getWidth() / 2),
							atras.getHeight() + textSize, paint);
					for (int i = 0; i < 8; ++i) {
						canvas.drawBitmap(dedo, xDedoInicial + dedo.getWidth()
								* i, yDedoInicial, null);

						canvas.drawText(
								(16 + i + (pantalla * 24)) + "",
								((xDedoInicial + dedo.getWidth() * i) + (dedo
										.getWidth() / 2)) - textSize / 2,
								(yDedoInicial + dedo.getHeight() / 2)
										+ textSize / 2, paint);

						if (db.getInt(16 + i + (pantalla * 24) + "", 0) == 2)
							canvas.drawBitmap(estrellaRe,
									xDedoInicial + dedo.getWidth() * i,
									yDedoInicial, null);
						else if (db
								.getInt(((16 + i) + (pantalla * 24)) + "", 0) != 1)
							if (((16 + i) + (pantalla * 24)) != 0)
								if (pantalla == 2) {
									paint.setTextSize(textSize / 2);
									paint.setColor(Color.YELLOW);
									canvas.drawText(" X "
											+ ((16 + i + (pantalla * 24)) - 10)
											+ "",
											xDedoInicial
													+ (dedo.getWidth() * i)
													+ estrellita.getWidth(),
											yDedoInicial + (dedo.getHeight()),
											paint);
									paint.setTextSize(textSize);
									paint.setARGB(255, 160, 210, 75);
									canvas.drawBitmap(
											estrellita,
											xDedoInicial
													+ (dedo.getWidth() * i),
											yDedoInicial
													+ (dedo.getHeight() - estrellita
															.getHeight()), null);
								} else
									canvas.drawBitmap(
											candado,
											xDedoInicial
													+ dedo.getWidth()
													* i
													+ (dedo.getWidth() - candado
															.getWidth()),
											yDedoInicial
													+ (dedo.getHeight() - candado
															.getHeight()), null);
					}
					for (int i = 0; i < 8; ++i) {
						canvas.drawBitmap(dedo, xDedoInicial + dedo.getWidth()
								* i, yDedoInicial - dedo.getHeight(), null);
						canvas.drawText(
								((8 + i) + (pantalla * 24)) + "",
								((xDedoInicial + dedo.getWidth() * i) + (dedo
										.getWidth() / 2)) - textSize / 2,
								((yDedoInicial - dedo.getHeight()) + dedo
										.getHeight() / 2) + textSize / 2, paint);

						if (db.getInt(((8 + i) + (pantalla * 24)) + "", 0) == 2)
							canvas.drawBitmap(estrellaRe,
									xDedoInicial + dedo.getWidth() * i,
									yDedoInicial - dedo.getHeight(), null);
						else if (db.getInt(((8 + i) + (pantalla * 24)) + "", 0) != 1)
							if (((8 + i) + (pantalla * 24)) != 0)
								if (pantalla == 2) {
									paint.setTextSize(textSize / 2);
									paint.setColor(Color.YELLOW);
									canvas.drawText(
											" X "
													+ (((8 + i) + (pantalla * 24)) - 10)
													+ "",
											xDedoInicial
													+ (dedo.getWidth() * i)
													+ estrellita.getWidth(),
											yDedoInicial - dedo.getHeight()
													+ (dedo.getHeight()), paint);
									paint.setTextSize(textSize);
									paint.setARGB(255, 160, 210, 75);
									canvas.drawBitmap(
											estrellita,
											xDedoInicial
													+ (dedo.getWidth() * i),
											yDedoInicial
													- dedo.getHeight()
													+ (dedo.getHeight() - estrellita
															.getHeight()), null);
								} else
									canvas.drawBitmap(
											candado,
											xDedoInicial
													+ dedo.getWidth()
													* i
													+ (dedo.getWidth() - candado
															.getWidth()),
											yDedoInicial
													- dedo.getHeight()
													+ (dedo.getHeight() - candado
															.getHeight()), null);
					}
					for (int i = 0; i < 8; ++i) {
						canvas.drawBitmap(
								dedo,
								xDedoInicial + dedo.getWidth() * i,
								yDedoInicial - dedo.getHeight()
										- dedo.getHeight(), null);
						canvas.drawText(
								(i + (pantalla * 24)) + "",
								((xDedoInicial + dedo.getWidth() * i) + (dedo
										.getWidth() / 2)) - textSize / 2,
								((yDedoInicial - dedo.getHeight() - dedo
										.getHeight()) + dedo.getHeight() / 2)
										+ textSize / 2, paint);

						if (db.getInt((i + (pantalla * 24)) + "", 0) == 2)
							canvas.drawBitmap(
									estrellaRe,
									xDedoInicial + dedo.getWidth() * i,
									yDedoInicial - dedo.getHeight()
											- dedo.getHeight(), null);
						else if (db.getInt((i + (pantalla * 24)) + "", 0) != 1)
							if ((i + (pantalla * 24)) != 0)
								if (pantalla == 2) {
									paint.setTextSize(textSize / 2);
									paint.setColor(Color.YELLOW);
									canvas.drawText(
											" X " + (i + (pantalla * 24) - 10)
													+ "",
											xDedoInicial
													+ (dedo.getWidth() * i)
													+ estrellita.getWidth(),
											yDedoInicial - dedo.getHeight()
													- dedo.getHeight()
													+ (dedo.getHeight()), paint);
									paint.setTextSize(textSize);
									paint.setARGB(255, 160, 210, 75);
									canvas.drawBitmap(
											estrellita,
											xDedoInicial
													+ (dedo.getWidth() * i),
											yDedoInicial
													- dedo.getHeight()
													- dedo.getHeight()
													+ (dedo.getHeight() - estrellita
															.getHeight()), null);
								} else
									canvas.drawBitmap(
											candado,
											xDedoInicial
													+ (dedo.getWidth() * i)
													+ (dedo.getWidth() - candado
															.getWidth()),
											yDedoInicial
													- dedo.getHeight()
													- dedo.getHeight()
													+ (dedo.getHeight() - candado
															.getHeight()), null);
					}
				} else {
					paint = new Paint();
					canvas.drawColor(Color.rgb(color[0], color[1], color[2]));
					paint = new Paint();
					paint.setStyle(Paint.Style.STROKE);
					paint.setStrokeWidth(1);
					paint.setStyle(Paint.Style.FILL_AND_STROKE);
					paint.setARGB(255, color[0], color[1], color[2]);
					paint.setTextSize((float) (Math.random() * 40 + 5));
					paint.setTypeface(Typeface.SERIF);
					canvas.drawBitmap(fondo, 0, 0, null);
					canvas.drawColor(Color.BLACK);

					canvas.drawText(
							getResources().getString(R.string.cargando),
							mitadW
									- (paint.measureText(getResources()
											.getString(R.string.cargando)) / 2),
							mitadH, paint);

				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			terminadoCanvas = true;
		}
	}
}