package com.dreamlab.android.vista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.Clasico;
import com.dreamlab.android.controlador.HttpHandler;
import com.dreamlab.android.controlador.MyGameProgress;
import com.dreamlab.android.controlador.Record;
import com.dreamlab.android.controlador.R;
import com.google.example.games.basegameutils.BaseGameActivity;

public class ClasicoView extends View {

	private Bitmap bmpBoton1;
	private Bitmap bmpContenedor;
	private Bitmap botonSinPulsar;
	private Bitmap botonPulsado;

	private int contadorP1Unidad;
	private int contadorP1Decena;
	private int contadorP1Centena;

	private int srcYNumerosP1C;
	private int srcYNumerosP1D;
	private int srcYNumerosP1U;

	private int yBoton;
	private int xBoton;
	private int xContenedor;
	private int yContenedor;
	private int xCentenaP1;
	private int yCentenaP1;
	private int xDecenaP1;
	private int yDecenaP1;
	private int xUnidadP1;
	private int yUnidadP1;
	private int estrellas;

	private int contadorTotal;

	private int tiempoMensaje;

	private boolean mensajePrueba;
	private boolean reproduceUnico2 = true;

	public ClasicoView(Context context) {
		super(context);
		this.requestFocus();
		this.setFocusableInTouchMode(true);

		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				AudioJuego.para_musica();
				boolean retry = true;
				cargado = false;
				mainLoopThread.setRunning(false);
				mainLoopThread.setCargado(false);
				tiempoLoop = false;
				while (retry) {
					try {
						mainLoopThread.join();
							retry = false;
							limpieza();
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

			botonSinPulsar = redimensionarImagen(R.drawable.boton1, redimension);
			botonPulsado = redimensionarImagen(R.drawable.boton2, redimension);
			bmpContenedor = redimensionarImagen(R.drawable.contenedor,
					redimension);
		}

	}

	public void inicializaPosiciones() {
		super.inicializaPosiciones();
		bmpBoton1 = botonSinPulsar;

		xBoton = mitadW - (botonPulsado.getWidth() / 2);
		yBoton = mitadH - (botonPulsado.getHeight() / 2);

		xContenedor = xBoton;
		yContenedor = yBoton + bmpBoton1.getHeight();

		xCentenaP1 = mitadW - (bmpContenedor.getWidth() / 2);
		yCentenaP1 = yContenedor;

		xDecenaP1 = xCentenaP1 + widthNumeros;
		yDecenaP1 = yContenedor;

		xUnidadP1 = xDecenaP1 + widthNumeros;
		yUnidadP1 = yContenedor;

		AudioJuego.reproduce(context, R.raw.fondogame);
		
	}

	public void actualizaContadores() {

		contadorP1Unidad += 1;

		if (contadorP1Unidad == 10) {
			contadorP1Decena += 1;
			contadorP1Unidad = 0;

		}
		if (contadorP1Decena == 10) {
			contadorP1Centena += 1;
			contadorP1Decena = 0;
		}
		contadorTotal = contadorP1Unidad + contadorP1Decena * 10
				+ contadorP1Centena * 100;
		srcYNumerosP1C = contadorP1Centena * widthNumeros;
		srcYNumerosP1D = contadorP1Decena * widthNumeros;
		srcYNumerosP1U = contadorP1Unidad * widthNumeros;

	}

	public void update() {
		if (contadorTotal % 20 == 0 && contadorTotal != 0 && !mensajePrueba) {
			mensajePrueba = true;
			tiempoMensaje = 0;
			reproduceUnico2 = false;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!cargado)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!cargado)
			return true;
		if (ventana) {
			ventana = false;
		}
		synchronized (getHolder()) {
			if (!finTiempo) {
				if (event.getAction() == 0 || event.getAction() == 261) {
					if (event.getX() > xBoton
							&& event.getX() < xBoton + botonPulsado.getWidth()
							&& event.getY() > yBoton
							&& event.getY() < yBoton + botonPulsado.getHeight()) {
						AudioJuego.playTemp(click);
						bmpBoton1 = botonPulsado;
						actualizaContadores();
					}

				}

				if (event.getAction() == 1 || event.getAction() == 262) {

					bmpBoton1 = botonSinPulsar;
				}
			} else {
				if (event.getAction() == 1 && tiempoActualiza > 20)
					if (reintentar(event.getX(), event.getY())) {
						finTiempo = false;
						reproduceUnico = true;
						AudioJuego.playTemp(nivelintro);
						tiempoUnidad = 1;
						tiempoDecena = 1;
						contadorTotal = 0;
						contadorTiempo = 0;
						contadorP1Unidad = -1;
						contadorP1Decena = 0;
						contadorP1Centena = 0;
						estrellas = 0;
						actualizaContadores();
					} else if (boton2(event.getX(), event.getY())) {
						Intent intent = new Intent(context, Record.class);
						intent.putExtra("Area", "CXX");
						context.startActivity(intent);
					}
			}
		}
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			super.draw(canvas);
			if (cargado) {
				if (!finTiempo) {

					if (mensajePrueba == true && tiempoMensaje != 20) {
						++tiempoMensaje;
						canvas.drawBitmap(estrellaRecord, mitadW
								- estrellaRecord.getWidth() / 2,
								(float) (yUnidad + heightNumeros * 1.2), null);
						if (!(tiempoMensaje >= 12)) {
							Rect srcHumo = new Rect(((tiempoMensaje % 6) / 2)
									* widthHumo, (tiempoMensaje / 6)
									* heightHumo, ((tiempoMensaje % 6) / 2)
									* widthHumo + widthHumo,
									((tiempoMensaje / 6) * heightHumo)
											+ heightHumo);
							Rect dstHumo = new Rect(mitadW
									- estrellaRecord.getWidth() / 2,
									(int) (yUnidad + heightNumeros * 1.2),
									mitadW - estrellaRecord.getWidth() / 2
											+ widthHumo,
									(int) (yUnidad + heightNumeros * 1.2)
											+ heightHumo);
							canvas.drawBitmap(humo, srcHumo, dstHumo, null);
						}
						if (!reproduceUnico2) {
							AudioJuego.playTemp(score);
							reproduceUnico2 = true;
						}
					} else if (contadorTotal % 20 != 0 && contadorTotal != 0) {
						mensajePrueba = false;
					}

					if (!ventana) {
						update();
						canvas.drawBitmap(bmpBoton1, xBoton, yBoton, null);
						canvas.drawBitmap(bmpContenedor, xContenedor,
								yContenedor, null);

						Rect srcNumerosP1C = new Rect(srcYNumerosP1C, 0,
								srcYNumerosP1C + widthNumeros, heightNumeros);
						Rect dstNumerosP1C = new Rect(xCentenaP1, yCentenaP1,
								xCentenaP1 + widthNumeros, yCentenaP1
										+ heightNumeros);
						canvas.drawBitmap(numeros, srcNumerosP1C,
								dstNumerosP1C, null);

						Rect srcNumerosP1D = new Rect(srcYNumerosP1D, 0,
								srcYNumerosP1D + widthNumeros, heightNumeros);

						Rect dstNumerosP1D = new Rect(xDecenaP1, yDecenaP1,
								xDecenaP1 + widthNumeros, yDecenaP1
										+ heightNumeros);
						canvas.drawBitmap(numeros, srcNumerosP1D,
								dstNumerosP1D, null);

						Rect srcNumerosP1U = new Rect(srcYNumerosP1U, 0,
								srcYNumerosP1U + widthNumeros, heightNumeros);
						Rect dstNumerosP1U = new Rect(xUnidadP1, yUnidadP1,
								xUnidadP1 + widthNumeros, yUnidadP1
										+ heightNumeros);
						canvas.drawBitmap(numeros, srcNumerosP1U,
								dstNumerosP1U, null);
					}
				} else {
						
						
					++tiempoActualiza;
					canvas.drawBitmap(ganastes, 0, 0, null);
					canvas.drawBitmap(cartel1, xCartel1, yCartel1, null);
					canvas.drawBitmap(cartel2, xCartel2, yCartel2, null);
					if (contadorTotal >= Integer.parseInt(db.getString(
							"clasico", String.valueOf(0)))) {
						if (reproduceUnico) {
							AudioJuego.playTemp(victory);
							editor.putString("clasico",
									String.valueOf(contadorTotal));
						}

						canvas.drawBitmap(estrella, 0, this.getHeight() / 2
								- (estrella.getHeight() / 2), null);
					} else {
						canvas.drawBitmap(estrellaVacia, 0, this.getHeight()
								/ 2 - (estrella.getHeight() / 2), null);
					}
					
					if (contadorTiempo != contadorTotal) {
						this.contadorTiempo++;
						AudioJuego.playTemp(clink);
						if (contadorTiempo % 20 == 0) {
							estrellas++;
							AudioJuego.playTemp(clink);
							tiempoMensaje = 0;
							mensajePrueba = true;
						}

					}
					for (int i = 0; i < 6; i++) {
						canvas.drawBitmap(
								estrellaReVacia,
								((this.getWidth() - (estrellaRecord.getWidth() * 6)) / 2)
										+ (estrellaRecord.getWidth() * i),
								yUnidad, null);
					}
					for (int i = 0; i < estrellas; i++) {
						canvas.drawBitmap(
								estrellaRecord,
								((this.getWidth() - (estrellaRecord.getWidth() * 6)) / 2)
										+ (estrellaRecord.getWidth() * i),
								yUnidad, null);
					}

					if (mensajePrueba) {
						++tiempoMensaje;
						if (!(tiempoMensaje >= 12)) {
							Rect srcHumo = new Rect(((tiempoMensaje % 6) / 2)
									* widthHumo, (tiempoMensaje / 6)
									* heightHumo, ((tiempoMensaje % 6) / 2)
									* widthHumo + widthHumo,
									((tiempoMensaje / 6) * heightHumo)
											+ heightHumo);
							Rect dstHumo = new Rect(
									((this.getWidth() - (estrellaRecord
											.getWidth() * 6)) / 2)
											+ (estrellaRecord.getWidth() * (estrellas - 1)),
									yUnidad,
									((this.getWidth() - (estrellaRecord
											.getWidth() * 6)) / 2)
											+ (estrellaRecord.getWidth() * (estrellas - 1))
											+ widthHumo, yUnidad + heightHumo);
							canvas.drawBitmap(humo, srcHumo, dstHumo, null);
						} else {
							mensajePrueba = false;
						}

					}
					canvas.drawText(String.valueOf(contadorTiempo) + "  "
							+ getResources().getString(R.string.pulsaciones),
							this.getWidth() / 4, this.getHeight() / 2, paint);
					canvas.drawText(
							db.getString("clasico", String.valueOf(0))
									+ "  "
									+ getResources().getString(
											R.string.pulsaciones),
							this.getWidth() / 4,
							this.getHeight()
									- (this.getHeight() / 4 + textSize / 2),
							paint);
					editor.commit();
					if (reproduceUnico) {
						mostrarAds();
							if(contadorTotal > 20)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_not_too_smelly));
							if(contadorTotal > 40)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_must_improve));
							if(contadorTotal > 60)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_not_bad____));
							if(contadorTotal > 80)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_80_aaaaaaawww_yeeeeaaaaah));
							if(contadorTotal > 100)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_100_close_enought_));
							if(contadorTotal == 119)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_jajajaja_119_lol));
							if(contadorTotal > 120)
								MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_120_im_so_op));
								reproduceUnico = false;
					
						
						for (int i = 0; i < 10; i++) {
							try {
								if (contadorTotal > Integer.parseInt(db
										.getString("C" + (i + 1) + "r", "0"))) {
									editor.putString("CT",
											String.valueOf(contadorTotal));
									Intent intent = new Intent(context,
											Record.class);
									intent.putExtra("Area", "C" + i + "r");
									if(db.getInt("conectado", 0) == 1)
									((Clasico) context).getGamesClient().submitScore(getResources().getString(R.string.leaderboard_the_best_score), contadorTotal);
									context.startActivity(intent);
									break;
								}

							} catch (NumberFormatException ex) {
								editor.putString("CT",
										String.valueOf(contadorTotal));
								Intent intent = new Intent(context,
										Record.class);
								intent.putExtra("Area", "C" + i + "r");
								context.startActivity(intent);
								break;
							}
						}

					}
					paint.setTypeface(font);
					paint.setTextSize((float) (textSize * 1.5));
					paint.setARGB(255, 180, 240, 154);
					canvas.drawText(
							getResources().getString(R.string.reintentar),
							(cartel1.getWidth() / 2)
									- (paint.measureText(getResources()
											.getString(R.string.reintentar)) / 2),
							(yCartel1 + (cartel1.getHeight() / 2))
									+ (textSize / 2), paint);
					canvas.drawText(
							getResources().getString(R.string.verRecord),
							(xCartel2 + (cartel2.getWidth() / 2))
									- (paint.measureText(getResources()
											.getString(R.string.verRecord)) / 2),
							(yCartel2 + (cartel2.getHeight() / 2))
									+ (textSize / 2), paint);
					paint.setTextSize(textSize * 2);
					paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
					canvas.drawText(getResources().getString(R.string.tardado),
							(float) (this.getWidth() / 4.5),
							(float) (this.getHeight() / 2.5), paint);
					canvas.drawText(
							getResources().getString(R.string.tiempoRecord),
							(float) (this.getWidth() / 4.5),
							(this.getHeight() - (float) (this.getHeight() / 2.5))
									+ (textSize / 2), paint);

				}

			} else {
				terminadoCanvas = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
