package com.dreamlab.android.vista;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.modelo.Niveles;
import com.dreamlab.android.modelo.Unidades;
import com.dreamlab.android.controlador.R;

public class KidsView extends View {

	private Bitmap corazon;

	private ArrayList<Unidades> unidades;
	private ArrayList<Niveles> nivelesConf;
	private Object arrayLock = new Object();

	private boolean redimensionado = false;
	private boolean victoria = false;

	private Bitmap record;

	private Bitmap derrota;
	private String puntitos;

	public KidsView(Context context) {
		super(context);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		ventana = false;
		tiempoUnidad = 4;
		tiempoDecena = 6;
		nivelesConf = new ArrayList<Niveles>();
		unidades = new ArrayList<Unidades>();

		getHolder().addCallback(new SurfaceHolder.Callback() {

			public void surfaceDestroyed(SurfaceHolder holder) {
				AudioJuego.para_intro();
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
				generarUnidades();
				iniciaMotor();
				terminadoCanvas = false;
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}
		});

	}

	public void inicializaPosiciones() {
		super.inicializaPosiciones();
		xCartel1 = mitadW - (cartel1.getWidth() / 2);
		yCartel1 = this.getHeight() - cartel1.getHeight();
	}

	// limpiado viernes,18,octubre,2012
	public void generarUnidades() {
		System.gc();
		Runtime.getRuntime().gc();
		nivelActual = (int) (Math.random() * db.getInt("kidv", 0));
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
			if (redimensionado) {
				unidades.add(new Unidades(this, textSize, BitmapFactory
						.decodeResource(getResources(), nivelesConf.get(i)
								.getImagen()), redimensionarImagen(nivelesConf
						.get(i).getImagen(), true, redimension, 3, 3, true),
						nivelesConf.get(i).getxSpeed() % 4, nivelesConf.get(i)
								.getySpeed() % 4, 10, corazon, AudioJuego.loadTemp(nivelesConf.get(
								i).getSonido()), i, nivelesConf.get(i)
								.getLimitePosicion(), false));
			} else {
				unidades.add(new Unidades(this, textSize, BitmapFactory
						.decodeResource(getResources(), nivelesConf.get(i)
								.getImagen()), redimensionarImagen(nivelesConf
						.get(i).getImagen(), true, redimension, 3, 3, true),
						nivelesConf.get(i).getxSpeed(), nivelesConf.get(i)
								.getySpeed(), nivelesConf.get(i).getVida(),
						corazon,AudioJuego.loadTemp( nivelesConf.get(i).getSonido()), i, nivelesConf
								.get(i).getLimitePosicion(), false));
			}

			repetir = nivelesConf.get(i).getCantidad();
		}
		AudioJuego.reproduceIntro(context, nivelesConf.get(0).getSonidoFondo());
		limpiaGc();
	}

	public void redimensionarImagenes() {
		if (isScalable()) {
			corazon = redimensionarImagen(R.drawable.corazon, true,
					redimension, 3, 3, false);
			record = redimensionarImagen(R.drawable.record, redimension);

			redimensionado = true;
		}
	}

	public boolean cambioNivel() {
		if (finTiempo)
			return true;
		if (unidades.size() == 0)
			return true;
		return false;
	}

	public void limpia() {
		unidades.clear();
		synchronized (arrayLock) {
			nivelesConf.clear();
		}
		tiempoActualiza = 0;
		victoria = false;
		tiempoDecena = 6;
		tiempoUnidad = 4;
		reproduceUnico = false;
		acabadoTiempo = false;
		contadorTiempo = 0;
		finTiempo = false;
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
		synchronized (getHolder()) {
			if (cargado
					&& (event.getAction() == 0 || event.getAction() == 261 || event
							.getAction() == 571)) {

				if (!victoria && acabadoTiempo) {
					for (Unidades unidad : unidades)
						if (unidad.isGolpeado(event.getX(), event.getY())
								) {
							unidad.golpear();
							cambioNivel();
							break;
						}
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
			}

		}
		return true;

	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			if (cargado) {
				draw(canvas);
				if (!victoria) {

					if (Integer.parseInt(tiempoDecena + "" + tiempoUnidad) < 61) {
						acabadoTiempo = true;
						CopyOnWriteArrayList<Object> toRemove = new CopyOnWriteArrayList<Object>();
						for (Unidades unidad : unidades) {
							unidad.onDraw(canvas);
							if (unidad.isBorrar())
								toRemove.add(unidad);
						}
						unidades.removeAll(toRemove);

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
								canvas.drawBitmap(derrota, 0, 0, null);
								canvas.drawText(getResources().getString(
										R.string.nivel) + nivelActual, 0,
										textSize, paint);
							}

						}
					} else {
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
							canvas.drawText(String.valueOf(tiempoUnidad - 1)
									+ puntitos, mitadW - paint.getTextSize()
									/ 2, mitadH, paint);
						} else {
							puntitos = "";
							tiempoUltimaAccion = 0;
							if (reproduceUnico) {
								AudioJuego.playTemp(nivelintro);
								reproduceUnico = false;
							}
							canvas.drawText(
									getResources().getString(R.string.comenzar),
									mitadW
											- (paint.getTextSize() / 2)
											* (getResources().getString(
													R.string.comenzar).length() / 2),
									mitadH, paint);

						}
					}

				} else {
					++tiempoActualiza;
					canvas.drawBitmap(ganastes, 0, 0, null);
					canvas.drawBitmap(cartel1, xCartel1, yCartel1, null);
					if (reproduceUnico) {
						if (db.getInt("kid", 0) <= 71)
							editor.putInt("kid", db.getInt("kid", 0) + 1);
						editor.putInt("kidv", db.getInt("kidv", 0) + 1);
						reproduceUnico = false;
						puntitos = "unico";
					}

					if (contadorTiempo != db.getInt("kidv", 0)) {
						this.contadorTiempo++;
						System.out.println(contadorTiempo + "YYYY   "
								+ db.getInt("kidv", 0));
						AudioJuego.playTemp(clink);
					} else {
						if (puntitos.equals("unico")) {
							AudioJuego.playTemp(clang);
							AudioJuego.playTemp(victory);
							puntitos = "";
						}

						canvas.drawBitmap(estrella, 0, this.getHeight() / 2
								- (estrella.getHeight() / 2), null);
						canvas.drawBitmap(record,
								record.getWidth() + (this.getWidth() / 4),
								(this.getHeight() / 2)
										- (record.getHeight() / 2), null);

					}

					canvas.drawText(contadorTiempo + "  "
							+ getResources().getString(R.string.partidas),
							this.getWidth() / 4, this.getHeight() / 2
									+ textSize * 2, paint);

					editor.commit();

					paint.setTypeface(font);
					paint.setTextSize((float) (textSize * 1.5));
					paint.setARGB(255, 180, 240, 154);
					canvas.drawText(
							getResources().getString(R.string.siguiente),
							xCartel1
									+ (cartel1.getWidth() / 2)
									- (paint.measureText(getResources()
											.getString(R.string.reintentar)) / 2),
							(yCartel1 + (cartel1.getHeight() / 2))
									+ (textSize / 2), paint);
					paint.setTextSize(textSize * 2);
					paint.setShadowLayer(5.0f, 10.0f, 10.0f, Color.BLACK);
					canvas.drawText(getResources().getString(R.string.tardado),
							(float) (this.getWidth() / 4.5),
							this.getHeight() / 2, paint);
					paint.setTextSize((float) (textSize * 3));
					canvas.drawText(
							getResources().getString(R.string.conseguido),
							(this.getWidth() / 2)
									- (paint.measureText(getResources()
											.getString(R.string.conseguido)) / 2),
							textSize * 3, paint);
				}
			} else {
				terminadoCanvas = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
