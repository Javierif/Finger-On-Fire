package com.dreamlab.android.vista;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.SoundPool;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.dreamlab.android.controlador.Arcade;
import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.MyGameProgress;
import com.dreamlab.android.modelo.Niveles;
import com.dreamlab.android.modelo.Sprite;
import com.dreamlab.android.modelo.TempSprite;
import com.dreamlab.android.modelo.Unidades;
import com.dreamlab.android.controlador.R;
import com.google.example.games.basegameutils.BaseGameActivity;

public class ArcadeView extends View {

	private Bitmap corazon;

	private ArrayList<Unidades> unidades;
	private ArrayList<TempSprite> temp;
	private Object arrayLock = new Object();

	private String efecto;

	private boolean redimensionado = false;
	private boolean victoria = false;
	// 8315

	private Bitmap efectoCongelado;

	private Bitmap efectoRabia;

	private Bitmap record;
	private int tiempo = 60;

	private final Arcade activity;

	private String puntitos;

	private int tiempoRecord;




	public ArcadeView(Context context, int nivelActual) {
		super(context);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		activity = (Arcade) context;
		this.nivelActual = nivelActual;
		tiempoUnidad = 4;
		tiempoDecena = 3;
		nivelesConf = new ArrayList<Niveles>();
		unidades = new ArrayList<Unidades>();
		temp = new ArrayList<TempSprite>();

		final Thread thread = new Thread(new Runnable() {

			public void run() {
				long ticksPS = 1000 / mainLoopThread.FPS;
				long startTime;
				long sleepTime;
				while (tiempoLoop) {
					startTime = System.currentTimeMillis();
					if (!finTiempo && !ventana && !victoria) {
						update();
					}

					sleepTime = ticksPS
							- (System.currentTimeMillis() - startTime);
					try {
						if (sleepTime > 0)
							Thread.sleep(sleepTime);
						else
							Thread.sleep(10);
					} catch (Exception e) {
					}
				}
			}
		});

		thread.start();

		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				AudioJuego.para_intro();
				boolean retry = true;
				cargado = false;
				tiempoLoop = false;
				while (retry) {
					try {

						retry = false;

						mainLoopThread.setRunning(false);
						mainLoopThread.setCargado(false);
						mainLoopThread.join();
						limpieza();
						limpiaGc();
						activity.finish();

					} catch (InterruptedException e) {
					}
				}
			}

			public void surfaceCreated(SurfaceHolder holder) {
				redimensionarImagenes();
				inicializaPosiciones();
				generarUnidades();
				crearTemps();
				iniciaMotor();
				terminadoCanvas = false;

			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});

	}

	public void limpieza() {
		super.limpieza();
		if (nivelActual > 33) {
			efectoCongelado.recycle();
			efectoCongelado = null;
		}
		if (nivelActual > 43) {
			efectoRabia.recycle();
			efectoRabia = null;
		}
		record.recycle();
		record = null;
		soundTemp.release();
		for (Unidades unidad : unidades)
			unidad.setImagen(null);
		for (TempSprite tempo : temp)
			tempo.setImagen(null);
	}

	// limpiado viernes,18,octubre,2012
	public void generarUnidades() {
		System.gc();
		Runtime.getRuntime().gc();
		narracion();
		victory = AudioJuego.loadTemp(R.raw.victory);
		click = AudioJuego.loadTemp(R.raw.click);
		clink = AudioJuego.loadTemp(R.raw.clink);
		clang = AudioJuego.loadTemp(R.raw.clang);
		looser = AudioJuego.loadTemp(R.raw.looser);
		nivelintro = AudioJuego.loadTemp(R.raw.nivelintro);
		score = AudioJuego.loadTemp(R.raw.score);
		int i = -1;
		boolean repetir = true;
		while (repetir == true) {
			if (i == -1) {
				nivelesConf.add(new Niveles(nivelActual));
				fondo = (redimensionarImagen(nivelesConf.get(0).getFondo(),
						this.getWidth(), this.getHeight()));
			} else {
				nivelesConf.add(new Niveles(nivelActual, i));
			}
			++i;
			System.gc();
			Runtime.getRuntime().gc();
			unidades.add(new Unidades(this, textSize, redimensionarImagen(
					nivelesConf.get(i).getImagen(), true, redimension, 3, 3,
					false), redimensionarImagen(nivelesConf.get(i).getImagen(),
					true, redimension, 3, 3, true), nivelesConf.get(i)
					.getxSpeed(), nivelesConf.get(i).getySpeed(), nivelesConf
					.get(i).getVida(), corazon, AudioJuego.loadTemp(nivelesConf.get(i).getSonido()),
					i, nivelesConf.get(i).getLimitePosicion(), nivelesConf.get(
							i).isBoss()));
			
			repetir = nivelesConf.get(i).getCantidad();
		}
		calcularRecord();
		AudioJuego.reproduceIntro(context, nivelesConf.get(0).getSonidoFondo());
		limpiaGc();
		
		if (nivelesConf.get(0).isBoss()) {
			tiempoUnidad = 1;
			tiempoDecena = 6;
		}

	}

	public void narracion() {
		switch (nivelActual) {
		case 0:
			personaje = redimensionarImagen(R.drawable.narratemp, redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		case 23:
			personaje = redimensionarImagen(R.drawable.charlachunga,
					redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		case 24:
			personaje = redimensionarImagen(R.drawable.narratemp, redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		case 47:
			personaje = redimensionarImagen(R.drawable.charlasandal,
					redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		case 48:
			personaje = redimensionarImagen(R.drawable.charlafinal, redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		case 71:
			personaje = redimensionarImagen(R.drawable.charlafinal, redimension);
			widthPersonaje = personaje.getWidth() / 3;
			break;
		}

	}

	public void redimensionarImagenes() {
		if (isScalable()) {
			if (nivelActual > 33)
				efectoCongelado = redimensionarImagen(R.drawable.congelado,
						this.getWidth(), this.getHeight());
			if (nivelActual > 43)
				efectoRabia = redimensionarImagen(R.drawable.rage,
						this.getWidth(), this.getHeight());
			corazon = redimensionarImagen(R.drawable.corazon, true,
					redimension, 3, 3, false);
			record = redimensionarImagen(R.drawable.record, redimension);
			cajonTexto = redimensionarImagen(R.drawable.cajontexto, redimension);
			redimensionado = true;
		}
	}

	public void gainTime(int tiempo) {
		if (tiempo > 0)
			if (tiempoUnidad < 5)
				tiempoUnidad += tiempo;
			else {
				tiempoUnidad = (tiempoUnidad + 5) - 10;
				tiempoDecena += 1;
			}
		else if (tiempoUnidad > 5)
			tiempoUnidad += tiempo;
		else {
			tiempoUnidad = (10 - (5 - tiempoUnidad));
			tiempoDecena -= 1;
		}
		String tiempoActual = tiempoDecena + "" + tiempoUnidad;
		if (Integer.parseInt(tiempoActual) > 30)
			tiempoUnidad = 0;
	}

	public boolean cambioNivel() {
		if (finTiempo)
			return true;
		if (unidades.size() == 0)
			return true;
		if (unidades.get(0).isBoss() && unidades.get(0).getVida() == 0)
			return true;
		return false;
	}

	private void calcularRecord() {
		tiempoRecord = 0;
		for (Unidades unidad : unidades)
			tiempoRecord += unidad.getMaxVida();
		if (nivelActual > 33)
			tiempoRecord = (tiempoRecord * 15 / 100);
		else if (nivelActual > 23)
			tiempoRecord = (tiempoRecord * 20 / 100);
		else if (nivelActual > 13)
			tiempoRecord = (tiempoRecord * 30 / 100);
		else if (nivelActual < 13)
			tiempoRecord = (tiempoRecord * 50 / 100);
	}

	private void crearTemps() {
		char tipo;
		for (int i = 0; i < 4; i++) {
			tipo = nivelesConf.get(0).getTemTipo().charAt(i);
			if (nivelesConf.get(0).getTemTipo().charAt(6) != '0')
				if (tipo == '6' || tipo == '5') {
					temp.add(new TempSprite(this, tipo));
					temp.add(new TempSprite(this, tipo));
					temp.add(new TempSprite(this, tipo));
					temp.add(new TempSprite(this, tipo));
				} else if (tipo != '0') {
					temp.add(new TempSprite(this, tipo));
					((TempSprite) temp.get(temp.size() - 1))
							.setTiempoRespawn(Integer.parseInt(tiempoDecena
									+ "" + tiempoUnidad));
				}

		}

	}

	private void update() {
		for (TempSprite temporal : temp) {
			if (temporal.getTipo() != 53 && temporal.getTipo() != 52
					&& temporal.isBorrar()) {
				if ((temporal.getTiempoRespawn() - (nivelesConf.get(0)
						.getTemTipo().charAt(7) - 48)) > Integer
						.parseInt(tiempoDecena + "" + tiempoUnidad)) {
					temporal.setTiempoRespawn(Integer.parseInt(tiempoDecena
							+ "" + tiempoUnidad));
					temporal.reiniciar();
				}
			} else if ((temporal.getTipo() == 53 || temporal.getTipo() == 52)
					&& temporal.isBorrar()) {

				temporal.reiniciar();
			}

		}

		if (efecto != null) {
			++tiempoActualiza;
			if (tiempoActualiza == 32) {
				tiempoActualiza = 0;
				if (efecto.equals("speed"))
					efectoVelocidad(true);
				else
					efectoVelocidad(false);
				efecto = null;
			}
		}
	}

	public void efectoVelocidad(boolean positivo) {
		for (Unidades unidad : unidades) {
			if (positivo == true) {
				if (efecto == null)
					efecto = "slow";
				unidad.setxSpeed(unidad.getxSpeed() / 2);
				unidad.setySpeed(unidad.getySpeed() / 2);
			} else {
				if (efecto == null)
					efecto = "speed";
				unidad.setxSpeed(unidad.getxSpeed() * 2);
				unidad.setySpeed(unidad.getySpeed() * 2);
			}

		}

	}

	public void limpia() {
		unidades.clear();
		synchronized (arrayLock) {
			nivelesConf.clear();
			temp.clear();
		}
		tiempoActualiza = 0;
		victoria = false;
		tiempoDecena = 3;
		tiempoUnidad = 4;
		tiempoRecord = 0;
		reproduceUnico = false;
		acabadoTiempo = false;
		tiempo = 60;
		efecto = null;
		contadorTiempo = 0;
		finTiempo = false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!cargado)
			return true;
		return super.onKeyDown(keyCode, event);
	}

	public boolean muertosTodos() {
		int vidaBichos = 0;
		for (int i = 1; i < unidades.size(); i++)
			vidaBichos += unidades.get(i).getVida();
		if (vidaBichos == 0)
			return true;
		return false;
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!cargado)
			return true;
		synchronized (getHolder()) {
			if (cargado && event.getAction() == 0) {
				if (ventana && nivelActual != 0 && nivelActual != 23
						&& nivelActual != 24 && nivelActual != 47
						&& nivelActual != 48 && nivelActual != 71) {
					ventana = false;

				} else if (ventana && tiempoActualiza == -1)
					ventana = false;
				else if (ventana) {
					switch (nivelActual) {
					case 0:
						super.texto = getResources().getString(R.string.intro1);
						editor.putInt("intro", 1);
						break;
					case 23:
						super.texto = getResources().getString(R.string.intro2);
						editor.putInt("intro", 2);
						break;
					case 24:
						super.texto = getResources().getString(R.string.intro3);
						editor.putInt("intro", 3);
						break;
					case 47:
						super.texto = getResources().getString(R.string.intro4);
						editor.putInt("intro", 4);
						break;
					case 48:
						super.texto = getResources().getString(R.string.intro5);
						editor.putInt("intro", 5);
						break;
					case 71:
						super.texto = getResources().getString(R.string.intro6);
						break;
					}
					tiempoActualiza = -1;
					return true;
				}
				if (!victoria && acabadoTiempo) {
					for (TempSprite sprite : temp)
						if (sprite.isGolpeado(event.getX(), event.getY())) {
							sprite.golpeado();
							break;
						}

					for (Unidades unidad : unidades)
						if (unidad.isGolpeado(event.getX(), event.getY())) {
							if (unidad.isBoss()) {
								if (muertosTodos()) {
									unidad.golpear();
									if ((unidad.getVida() % (unidad
											.getMaxVida() / 2)) == 0
											&& unidad.getVida() != 0)
										for (int i = 1; i < unidades.size(); i++) {
											unidades.get(i).reiniciar();
											if (nivelActual == 71)
												for (int e = 0; e < temp.size(); e++) {
													if (temp.get(e).getTipo() == 50) {
														temp.get(e).reiniciar();
														temp.get(e).setX(
																unidad.getX());
														temp.get(e).setY(
																unidad.getY());
														if ((e - 3) <= 0) {
															temp.get(e)
																	.setxSpeed(
																			5);
															temp.get(e)
																	.setySpeed(
																			e
																					* (2 + e));
														} else {
															temp.get(e)
																	.setxSpeed(
																			-5);
															temp.get(e)
																	.setySpeed(
																			-(e * (2 + e)));
														}
													}
												}
										}
								}
							} else {
								unidad.golpear();
							}
							cambioNivel();
							break;
						}

					if (finTiempo && tiempoActualiza > 20) {
						if (event.getX() > (this.getWidth() / 4)
								&& event.getX() < this.getWidth()
										- (this.getWidth() / 4)
								&& event.getY() > this.getHeight()
										- (this.getHeight() / 4)
								&& event.getY() < this.getHeight()) {
							limpia();
							generarUnidades();
						}
					}
				} else {
					if (victoria && tiempoActualiza > 20)
						if (reintentar(event.getX(), event.getY())) {
							limpia();
							generarUnidades();
						} else if (boton2(event.getX(), event.getY())) {
							if (nivelActual < 47) {
								++nivelActual;
								limpia();
								generarUnidades();
							}
							if ((nivelActual > 47 && (db.getInt("contador", 0) > ((nivelActual + 1) - 11)))
									&& nivelActual != 71) {
								++nivelActual;
								limpia();
								generarUnidades();
							} else if ((nivelActual > 47 && (db.getInt(
									"contador", 0) < ((nivelActual + 1) - 11)))) {
								activity.finish();
							}

							if (nivelActual == 71) {
								++nivelActual;
								limpia();
								narracion();
							}
						}
				}
			}
		}
		return true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			if (cargado) {
				if (nivelActual != 72) {
					draw(canvas);
					if (!victoria) {
						if (!ventana) {

							if (Integer.parseInt(tiempoDecena + ""
									+ tiempoUnidad) < 31
									|| nivelesConf.get(0).isBoss()) {
								acabadoTiempo = true;
								CopyOnWriteArrayList<Object> toRemove = new CopyOnWriteArrayList<Object>();
								synchronized (arrayLock) {
									for (TempSprite sprite : temp)
										sprite.onDraw(canvas);
								}
								toRemove.clear();

								for (Unidades unidad : unidades) {
									unidad.onDraw(canvas);
									if (unidad.isBorrar())
										toRemove.add(unidad);
								}
								if (!nivelesConf.get(0).isBoss())
									unidades.removeAll(toRemove);

								if (efecto == "slow") {
									canvas.drawBitmap(efectoCongelado, 0, 0,
											null);
								}

								if (efecto == "speed") {
									canvas.drawBitmap(efectoRabia, 0, 0, null);
								}
								if (cambioNivel()) {
									int maxVida = 0;
									for (int i = 0; i < unidades.size(); ++i)
										maxVida += unidades.get(i).getVida();
									if (maxVida == 0) {
										finTiempo = true;
										victoria = true;
										reproduceUnico = true;
									} else {
										++tiempoActualiza;
										finTiempo = true;
										if (!reproduceUnico) {
											AudioJuego.playTemp(looser);
											reproduceUnico = true;
										}
										paint.setTypeface(font);
										paint.setTextSize((float) (textSize * 1.5));
										paint.setARGB(255, 180, 240, 154);
										canvas.drawBitmap(ganastes, 0, 0, null);
										canvas.drawBitmap(cartel1, mitadW
												- (cartel1.getWidth() / 2),
												yCartel1, null);
										canvas.drawText(
												"NIVEL  " + nivelActual, 0,
												textSize, paint);
										canvas.drawText(
												getResources().getString(
														R.string.reintentar),
												((mitadW - (cartel1.getWidth() / 2)) + (cartel1
														.getWidth() / 2))
														- (paint.measureText(getResources()
																.getString(
																		R.string.reintentar)) / 2),
												(yCartel1 + (cartel1
														.getHeight() / 2))
														+ (textSize / 2), paint);

										canvas.rotate(-45,
												this.getWidth() / 2.0f,
												this.getHeight() / 2.0f);
										paint.setTextSize((float) (textSize * 4));
										canvas.drawText(getResources()
												.getString(R.string.perder),
												this.getWidth() / 5, this
														.getHeight() / 2, paint);
									}

								}
							} else if (!nivelesConf.get(0).isBoss()) {
								paint.setTextSize(textSize * 4);
								paint.setTypeface(Typeface.SERIF);

								if (!((tiempoUnidad - 1) < 1)) {

									if (tiempoUnidad == tiempoUltimaAccion) {

										puntitos = puntitos + ".";
									} else {
										AudioJuego.playTemp(click);
										tiempoUltimaAccion = tiempoUnidad;
										puntitos = "";
										reproduceUnico = true;
									}
									canvas.drawText(
											String.valueOf(tiempoUnidad - 1)
													+ puntitos,
											mitadW - paint.getTextSize() / 2,
											mitadH, paint);
								} else {
									puntitos = "";
									tiempoUltimaAccion = 0;
									if (reproduceUnico) {
										AudioJuego.playTemp(nivelintro);
										reproduceUnico = false;
									}
									canvas.drawText(
											getResources().getString(
													R.string.comenzar),
											mitadW
													- (paint.getTextSize() / 2)
													* (getResources()
															.getString(
																	R.string.comenzar)
															.length() / 2),
											mitadH, paint);

								}
							}
						} else {
							boolean personaje = true;
							if (nivelActual == 0 && db.getInt("intro", 0) == 0) {

								if (tiempoActualiza < getResources().getString(
										R.string.intro1).length() - 1
										&& tiempoActualiza != -1) {

									texto += getResources().getString(
											R.string.intro1).charAt(
											tiempoActualiza);
									++tiempoActualiza;
								} else {
									tiempoActualiza = -1;
								}

								// para que salga el texto poco a poco haz un
								// charat
							} else if (nivelActual == 23
									&& db.getInt("intro", 0) == 1) {
								if (tiempoActualiza < getResources().getString(
										R.string.intro2).length() - 1
										&& tiempoActualiza != -1) {
									texto += getResources().getString(
											R.string.intro2).charAt(
											tiempoActualiza);
									++tiempoActualiza;

								} else {
									tiempoActualiza = -1;
								}

							} else if (nivelActual == 24
									&& db.getInt("intro", -1) == 2) {
								if (tiempoActualiza < getResources().getString(
										R.string.intro3).length() - 1
										&& tiempoActualiza != -1) {
									texto += getResources().getString(
											R.string.intro3).charAt(
											tiempoActualiza);
									++tiempoActualiza;
								} else {
									tiempoActualiza = -1;
								}

							} else if (nivelActual == 47
									&& db.getInt("intro", -1) == 3) {
								if (tiempoActualiza < getResources().getString(
										R.string.intro4).length() - 1
										&& tiempoActualiza != -1) {
									texto += getResources().getString(
											R.string.intro4).charAt(
											tiempoActualiza);
									++tiempoActualiza;
								} else {
									tiempoActualiza = -1;
								}

							} else if (nivelActual == 48
									&& db.getInt("intro", -1) == 4) {
								if (tiempoActualiza < getResources().getString(
										R.string.intro5).length() - 1
										&& tiempoActualiza != -1) {
									texto += getResources().getString(
											R.string.intro5).charAt(
											tiempoActualiza);
									++tiempoActualiza;
								} else {
									tiempoActualiza = -1;
								}

							} else if (nivelActual == 71
									&& db.getInt("intro", -1) == 5) {
								if (tiempoActualiza < getResources().getString(
										R.string.intro6).length() - 1
										&& tiempoActualiza != -1) {

									texto += getResources().getString(
											R.string.intro6).charAt(
											tiempoActualiza);
									++tiempoActualiza;
								} else {
									tiempoActualiza = -1;
								}

							} else {
								personaje = false;
								canvas.drawBitmap(bmpVentanaArcade, mitadW
										- (bmpVentanaArcade.getWidth() / 2), 0,
										null);
							}

							if (personaje) {
								paint.setTextSize(textSize / 1.2f);
								Rect srcP = new Rect(widthPersonaje
										* (columnaSoles / 3), 0, widthPersonaje
										* (columnaSoles / 3) + widthPersonaje,
										this.personaje.getHeight());
								Rect dstP = new Rect(0, this.getHeight()
										- this.personaje.getHeight(),
										widthPersonaje, this.getHeight());
								canvas.drawBitmap(this.personaje, srcP, dstP,
										null);
								canvas.drawBitmap(
										cajonTexto,
										widthPersonaje,
										this.getHeight()
												- cajonTexto.getHeight(), null);
								int y = this.getHeight()
										- cajonTexto.getHeight() + textSize
										+ (textSize / 3);
								for (String line : texto.split("\n")) {
									canvas.drawText(line, widthPersonaje
											+ (cajonTexto.getWidth() / 7), y,
											paint);
									y += textSize * 1.2;
								}

							}
						}

					} else {
						switch (nivelActual) {
						case 0:
							editor.putInt("intro", 1);
							break;
						case 23:
							editor.putInt("intro", 2);
							break;
						case 24:
							editor.putInt("intro", 3);
							break;
						case 47:
							editor.putInt("intro", 4);
							break;
						case 48:
							editor.putInt("intro", 5);
							break;
						case 71:
							break;
						}
						++tiempoActualiza;
						this.efecto = "1";
						canvas.drawBitmap(ganastes, 0, 0, null);
						canvas.drawBitmap(cartel1, xCartel1, yCartel1, null);
						canvas.drawBitmap(cartel2, xCartel2, yCartel2, null);
						String tiempo = tiempoDecena + "" + tiempoUnidad;
						int tardado;
						
						if (!nivelesConf.get(0).isBoss())
							tardado = 30 - Integer.parseInt(tiempo);
						else
							tardado = 60 - Integer.parseInt(tiempo);
						if (tardado <= tiempoRecord
								&& tardado <= db.getInt(
										(String.valueOf(nivelActual) + "R"),
										100)) {
							editor.putInt(String.valueOf(nivelActual) + "R",
									tardado);
							if (reproduceUnico) {
								mostrarAds();
								AudioJuego
										.playTemp(victory);
								this.tiempo = 2021;
								editor.putInt(nivelActual + "", 2);
								editor.putInt("estrellas", db.getInt("estrellas", 0)+1);
								if (db.getInt(nivelActual + "", 0) != 2)
									editor.putInt("contador",
											db.getInt("contador", 0) + 1);

								if (db.getInt((nivelActual + 1) + "", 0) != 1
										&& db.getInt((nivelActual + 1) + "", 0) != 2
										&& nivelActual < 47) {

									editor.putInt((nivelActual + 1) + "", 1);
								}
						
									switch(db.getInt("estrellas", 0)){
									case 23:
										MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_kind_of_a_big_deal));
										break;
									case 47:
										MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_im_very_important));
										break;
									case 71:
										MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_god_walking_amongst_mere_mortals));
										break;
									}

									MyGameProgress.AddLogroIncremental(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_arcade_finished));
									
									if(db.getInt("23", 0) == 2 && db.getInt("47", 0) == 2 && db.getInt("71", 0) ==2)
										MyGameProgress.AddLogro(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_vanquished));
								}
							

						} else {
							if (reproduceUnico) {
								mostrarAds();
								MyGameProgress.AddLogroIncremental(db, editor, (BaseGameActivity) context, getResources().getString(R.string.achievement_arcade_finished));
								AudioJuego.playTemp(score);

								if (db.getInt(
										String.valueOf((nivelActual + 1)), 0) != 1
										&& db.getInt((nivelActual + 1) + "", 0) != 2
										&& nivelActual < 47)
									editor.putInt(
											String.valueOf((nivelActual + 1))
													+ "", 1);

							}
							canvas.drawBitmap(
									estrellaVacia,
									0,
									this.getHeight() / 2
											- (estrella.getHeight() / 2), null);

						}

						reproduceUnico = false;
						if (contadorTiempo != tardado) {
							this.contadorTiempo++;
							AudioJuego.playTemp(clink);
						} else {
							efecto = "0";
						}
						if (efecto.equals("0") && this.tiempo == 2021) {
							this.tiempo = 55;
							AudioJuego.playTemp(clang);
						}
						if (this.tiempo == 55) {
							canvas.drawBitmap(estrella, 0, this.getHeight() / 2
									- (estrella.getHeight() / 2), null);
							canvas.drawBitmap(
									record,
									record.getWidth() + (this.getWidth() / 4),
									(this.getHeight() / 2)
											- (record.getHeight() / 2), null);

						}
						canvas.drawText(String.valueOf(contadorTiempo) + "  "
								+ getResources().getString(R.string.segundos),
								this.getWidth() / 4, this.getHeight() / 2,
								paint);
						if (db.getInt((nivelActual + ""), 0) == 2)
							tiempoRecord = (db.getInt((nivelActual + "R"), 0));
						canvas.drawText(
								String.valueOf(tiempoRecord)
										+ "  "
										+ getResources().getString(
												R.string.segundos),
								this.getWidth() / 4,
								this.getHeight()
										- (this.getHeight() / 4 + textSize / 2),
								paint);
						canvas.drawText(getResources().getString(
								R.string.nivel) + nivelActual, 0, textSize,
								paint);

						editor.commit();

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
								getResources().getString(R.string.siguiente),
								(xCartel2 + (cartel2.getWidth() / 2))
										- (paint.measureText(getResources()
												.getString(R.string.siguiente)) / 2),
								(yCartel2 + (cartel2.getHeight() / 2))
										+ (textSize / 2), paint);
						paint.setTextSize(textSize * 2);
						paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
						canvas.drawText(
								getResources().getString(R.string.tardado),
								(float) (this.getWidth() / 4.5),
								(float) (this.getHeight() / 2.5), paint);
						canvas.drawText(
								getResources().getString(R.string.tiempoRecord),
								(float) (this.getWidth() / 4.5),
								(this.getHeight() - (float) (this.getHeight() / 2.5))
										+ (textSize / 2), paint);
						canvas.drawText(
								getResources().getString(R.string.conseguido),
								(this.getWidth() / 2)
										- (paint.measureText(getResources()
												.getString(R.string.conseguido)) / 2),
								textSize * 2, paint);
					}

				} else {
					if (tiempoActualiza < getResources().getString(
							R.string.intro7).length() - 1
							&& tiempoActualiza != -1) {

						texto += getResources().getString(R.string.intro6)
								.charAt(tiempoActualiza);
						++tiempoActualiza;
					} else {
						tiempoActualiza = -1;
					}

					Rect srcP = new Rect(widthPersonaje * (columnaSoles / 3),
							0, widthPersonaje * (columnaSoles / 3)
									+ widthPersonaje,
							this.personaje.getHeight());
					Rect dstP = new Rect(0, this.getHeight()
							- this.personaje.getHeight(), widthPersonaje,
							this.getHeight());
					canvas.drawBitmap(this.personaje, srcP, dstP, null);
					canvas.drawBitmap(cajonTexto,
							this.getWidth() - cajonTexto.getWidth(),
							this.getHeight() - cajonTexto.getHeight(), null);
					int y = this.getHeight() - cajonTexto.getHeight()
							+ textSize + (textSize / 3);
					for (String line : texto.split("\n")) {
						canvas.drawText(line,
								widthPersonaje + (cajonTexto.getWidth() / 7),
								y, paint);
						y += textSize * 1.2;
					}

				}
			} else {
				terminadoCanvas = true;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
