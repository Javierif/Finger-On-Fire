package com.dreamlab.android.vista;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.Record;
import com.dreamlab.android.controlador.R;

public class MultijugadorView extends View {

	private Bitmap bmpBoton1;
	private Bitmap bmpContenedor;
	private Bitmap botonSinPulsar;
	private Bitmap botonPulsado;

	private int contadorP1Unidad;
	private int contadorP1Decena;
	private int contadorP1Centena;
	private int contadorP2Unidad;
	private int contadorP2Decena;
	private int contadorP2Centena;

	private int srcYNumerosP1C;
	private int srcYNumerosP1D;
	private int srcYNumerosP1U;
	private int srcYNumerosP2C;
	private int srcYNumerosP2D;
	private int srcYNumerosP2U;

	private int xBotonP1;
	private int yBotonP1;
	private int xContenedorP1;
	private int yContenedorP1;
	private int xCentenaP1;
	private int yCentenaP1;
	private int xDecenaP1;
	private int yDecenaP1;
	private int xUnidadP1;
	private int yUnidadP1;

	private int xBotonP2;
	private int yBotonP2;
	private int xContenedorP2;
	private int yContenedorP2;
	private int xCentenaP2;
	private int yCentenaP2;
	private int xDecenaP2;
	private int yDecenaP2;
	private int xUnidadP2;
	private int yUnidadP2;

	private int tiempoMensaje;
	private int contador;

	private boolean mensajePrueba;
	private int contadorTotalP1;
	private int contadorTotalP2;
	private int estrellas;
	private Bitmap bmpBoton2;

	public MultijugadorView(Context context) {
		super(context);
		this.requestFocus();
		this.setFocusableInTouchMode(true);
		bmpContenedor = BitmapFactory.decodeResource(getResources(),
				R.drawable.contenedor);

		botonSinPulsar = BitmapFactory.decodeResource(getResources(),
				R.drawable.boton1);
		botonPulsado = BitmapFactory.decodeResource(getResources(),
				R.drawable.boton2);
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
							limpieza();
							mainLoopThread.join();
							retry = false;
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
		bmpBoton2 = botonSinPulsar;

		xBotonP1 = 0;
		yBotonP1 = this.getHeight() / 2 - botonPulsado.getHeight() / 2;

		xBotonP2 = this.getWidth() - bmpBoton1.getWidth();
		yBotonP2 = this.getHeight() / 2 - botonPulsado.getHeight() / 2;

		xContenedorP1 = xBotonP1;
		yContenedorP1 = yBotonP1 + bmpBoton1.getHeight();

		xContenedorP2 = this.getWidth() - bmpContenedor.getWidth();
		yContenedorP2 = yBotonP2 + bmpBoton1.getHeight();

		xCentenaP1 = 0;
		yCentenaP1 = yContenedorP1;

		xDecenaP1 = widthNumeros;
		yDecenaP1 = yContenedorP1;

		xUnidadP1 = widthNumeros + widthNumeros;
		yUnidadP1 = yContenedorP1;

		xCentenaP2 = this.getWidth() - bmpContenedor.getWidth();
		yCentenaP2 = yContenedorP1;

		xDecenaP2 = this.getWidth() - bmpContenedor.getWidth() + widthNumeros;
		yDecenaP2 = yContenedorP1;

		xUnidadP2 = this.getWidth() - bmpContenedor.getWidth() + widthNumeros
				+ widthNumeros;
		yUnidadP2 = yContenedorP1;
		AudioJuego.reproduce(context, R.raw.fondogame);
	}

	public void actualizaContadores(int Player) {
		if (Player == 1) {
			contadorP1Unidad += 1;

			if (contadorP1Unidad == 10) {
				contadorP1Decena += 1;
				contadorP1Unidad = 0;

			}
			if (contadorP1Decena == 10) {
				contadorP1Centena += 1;
				contadorP1Decena = 0;
			}
		} else {
			contadorP2Unidad += 1;

			if (contadorP2Unidad == 10) {

				contadorP2Decena += 1;
				contadorP2Unidad = 0;
			}
			if (contadorP2Decena == 10) {
				contadorP2Centena += 1;
				contadorP2Decena = 0;
			}
		}
		contador = contadorP1Unidad + contadorP1Decena * 10 + contadorP1Centena
				* 100;
		if (Player == 1) {
			srcYNumerosP1C = contadorP1Centena * widthNumeros;
			srcYNumerosP1D = contadorP1Decena * widthNumeros;
			srcYNumerosP1U = contadorP1Unidad * widthNumeros;

		} else {
			srcYNumerosP2C = contadorP2Centena * widthNumeros;
			srcYNumerosP2D = contadorP2Decena * widthNumeros;
			srcYNumerosP2U = contadorP2Unidad * widthNumeros;
		}
		contadorTotalP1 = contadorP1Unidad + contadorP1Decena * 10
				+ contadorP1Centena * 100;
		contadorTotalP2 = contadorP2Unidad + contadorP2Decena * 10
				+ contadorP2Centena * 100;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!cargado)
			return true;
		if (ventana) {
			ventana = false;
			return true;
		}
		if (keyCode == 82 && !finTiempo && event.getRepeatCount() == 0) {
			AudioJuego.playTemp(click);
			actualizaContadores(2);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onTouchEvent(MotionEvent event) {
		if (!cargado)
			return true;
		if (ventana) {
			ventana = false;
			return true;
		}
		if (!finTiempo) {
			if (event.getAction() == 0) {
				synchronized (getHolder()) {
					if (event.getX() > xBotonP1
							&& event.getX() < xBotonP1
									+ botonPulsado.getWidth()
							&& event.getY() > yBotonP1
							&& event.getY() < yBotonP1
									+ botonPulsado.getHeight()) {

						AudioJuego.playTemp(click);
						bmpBoton1 = botonPulsado;
						actualizaContadores(1);
					}

					if (event.getX() > xBotonP2
							&& event.getX() < xBotonP2
									+ botonPulsado.getWidth()
							&& event.getY() > yBotonP2
							&& event.getY() < yBotonP2
									+ botonPulsado.getHeight()) {

						AudioJuego.playTemp(click);
						bmpBoton2 = botonPulsado;
						actualizaContadores(2);
					}
				}
			}
			if (event.getAction() == 261) {
				if (event.getX(1) > xBotonP1
						&& event.getX(1) < xBotonP1 + botonPulsado.getWidth()
						&& event.getY(1) > yBotonP1
						&& event.getY(1) < yBotonP1 + botonPulsado.getHeight()) {

					AudioJuego.playTemp(click);
					bmpBoton1 = botonPulsado;
					actualizaContadores(1);
				}

				if (event.getX(1) > xBotonP2
						&& event.getX(1) < xBotonP2 + botonPulsado.getWidth()
						&& event.getY(1) > yBotonP2
						&& event.getY(1) < yBotonP2 + botonPulsado.getHeight()) {

					AudioJuego.playTemp(click);
					bmpBoton2 = botonPulsado;
					actualizaContadores(2);
				}
			}
		} else {
			if (event.getAction() == 1 && tiempoActualiza > 20)
				if (reintentar(event.getX(), event.getY())) {
					contadorTotalP1 = 0;
					contadorTotalP2 = 0;
					finTiempo = false;
					reproduceUnico = true;
					AudioJuego.playTemp(nivelintro);
					tiempoUnidad = 1;
					tiempoDecena = 1;
					contadorP1Unidad = -1;
					contadorP1Decena = 0;
					contadorP1Centena = 0;
					contadorTiempo = 0;
					contadorP2Unidad = -1;
					contadorP2Decena = 0;
					contadorP2Centena = 0;
					actualizaContadores(1);
					actualizaContadores(2);
				} else if (boton2(event.getX(), event.getY())) {
					Intent intent = new Intent(context, Record.class);
					intent.putExtra("Area", "MXX");
					context.startActivity(intent);
				}
		}
		if (event.getAction() == 1 || event.getAction() == 261) {
			bmpBoton1 = botonSinPulsar;
			bmpBoton2 = botonSinPulsar;
		}
		return true;
	}

	public void update() {
		if (contador % 20 == 0 && contador != 0) {
			mensajePrueba = true;
			tiempoMensaje = 0;
		}
	}

	@Override
	public void onDraw(Canvas canvas) {
		try {
			if (cargado) {
				try {
					update();
					super.draw(canvas);
					if (!finTiempo) {
						if (!ventana) {

							canvas.drawBitmap(bmpBoton1, xBotonP1, yBotonP1,
									null);
							canvas.drawBitmap(bmpContenedor, xContenedorP1,
									yContenedorP1, null);

							Rect srcNumerosP1C = new Rect(srcYNumerosP1C, 0,
									srcYNumerosP1C + widthNumeros,
									heightNumeros);
							Rect dstNumerosP1C = new Rect(xCentenaP1,
									yCentenaP1, xCentenaP1 + widthNumeros,
									yCentenaP1 + heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP1C,
									dstNumerosP1C, null);

							Rect srcNumerosP1D = new Rect(srcYNumerosP1D, 0,
									srcYNumerosP1D + widthNumeros,
									heightNumeros);

							Rect dstNumerosP1D = new Rect(xDecenaP1, yDecenaP1,
									xDecenaP1 + widthNumeros, yDecenaP1
											+ heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP1D,
									dstNumerosP1D, null);

							Rect srcNumerosP1U = new Rect(srcYNumerosP1U, 0,
									srcYNumerosP1U + widthNumeros,
									heightNumeros);
							Rect dstNumerosP1U = new Rect(xUnidadP1, yUnidadP1,
									xUnidadP1 + widthNumeros, yUnidadP1
											+ heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP1U,
									dstNumerosP1U, null);

							canvas.drawBitmap(bmpBoton2, xBotonP2, yBotonP2,
									null);
							canvas.drawBitmap(bmpContenedor, xContenedorP2,
									yContenedorP2, null);

							Rect srcNumerosP2C = new Rect(srcYNumerosP2C, 0,
									srcYNumerosP2C + widthNumeros,
									heightNumeros);
							Rect dstNumerosP2C = new Rect(xCentenaP2,
									yCentenaP2, xCentenaP2 + widthNumeros,
									yCentenaP2 + heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP2C,
									dstNumerosP2C, null);

							Rect srcNumerosP2D = new Rect(srcYNumerosP2D, 0,
									srcYNumerosP2D + widthNumeros,
									heightNumeros);

							Rect dstNumerosP2D = new Rect(xDecenaP2, yDecenaP2,
									xDecenaP2 + widthNumeros, yDecenaP2
											+ heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP2D,
									dstNumerosP2D, null);

							Rect srcNumerosP2U = new Rect(srcYNumerosP2U, 0,
									srcYNumerosP2U + widthNumeros,
									heightNumeros);
							Rect dstNumerosP2U = new Rect(xUnidadP2, yUnidadP2,
									xUnidadP2 + widthNumeros, yUnidadP2
											+ heightNumeros);
							canvas.drawBitmap(numeros, srcNumerosP2U,
									dstNumerosP2U, null);
						} else {
							paint.setTextSize((float) (textSize * 1.5));
							canvas.drawText(
									getResources().getString(R.string.infoM),
									mitadW
											- (paint.measureText(getResources()
													.getString(R.string.infoM)) / 2),
									textSize * 7, paint);
							canvas.drawText(
									getResources().getString(R.string.infoM2),
									mitadW
											- (paint.measureText(getResources()
													.getString(R.string.infoM2)) / 2),
									textSize * 9, paint);
							canvas.drawText(
									getResources().getString(R.string.infoM3),
									mitadW
											- (paint.measureText(getResources()
													.getString(R.string.infoM3)) / 2),
									textSize * 11, paint);
						}
					} else {
						int ganador = 0;
						String vencedor = "";
						++tiempoActualiza;
						canvas.drawBitmap(ganastes, 0, 0, null);

						canvas.drawBitmap(cartel1, xCartel1, yCartel1, null);
						canvas.drawBitmap(cartel2, xCartel2, yCartel2, null);
						
						if (contadorTotalP1 > contadorTotalP2) {
							ganador = contadorTotalP1;
							vencedor = "1";
						} else if (contadorTotalP1 < contadorTotalP2) {
							ganador = contadorTotalP2;
							vencedor = "2";
						} else {
							ganador = contadorTotalP1;
							vencedor = "0";
						}

						if (ganador >= Integer.parseInt(db.getString("multi",
								String.valueOf(0)))) {
							if (reproduceUnico) {
								mostrarAds();
								AudioJuego
										.playTemp(victory);
								editor.putString("multi", ganador + "");
							}

							canvas.drawBitmap(estrella, 0, this.getHeight() / 2
									- (estrella.getHeight() / 2), null);
						} else {
							if (reproduceUnico){
								AudioJuego.playTemp(score);
							canvas.drawBitmap(
									estrellaVacia,
									0,
									this.getHeight() / 2
											- (estrella.getHeight() / 2), null);
							mostrarAds();
							}
						}
						if (contadorTiempo != ganador) {
							this.contadorTiempo++;
							AudioJuego.playTemp(clink);
							if (contadorTiempo % 20 == 0) {
								estrellas++;
								AudioJuego.playTemp(score);
								tiempoMensaje = 0;
								mensajePrueba = true;
							}

						}
						for (int i = 0; i < 6; i++) {
							canvas.drawBitmap(
									estrellaReVacia,
									((this.getWidth() - (estrellaRecord
											.getWidth() * 6)) / 2)
											+ (estrellaRecord.getWidth() * i),
									yUnidad, null);
						}
						for (int i = 0; i < estrellas; i++) {
							canvas.drawBitmap(
									estrellaRecord,
									((this.getWidth() - (estrellaRecord
											.getWidth() * 6)) / 2)
											+ (estrellaRecord.getWidth() * i),
									yUnidad, null);
						}

						if (mensajePrueba) {
							++tiempoMensaje;
							if (!(tiempoMensaje >= 12)) {
								Rect srcHumo = new Rect(
										((tiempoMensaje % 6) / 2) * widthHumo,
										(tiempoMensaje / 6) * heightHumo,
										((tiempoMensaje % 6) / 2) * widthHumo
												+ widthHumo,
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
												+ widthHumo, yUnidad
												+ heightHumo);
								canvas.drawBitmap(humo, srcHumo, dstHumo, null);
							} else {
								mensajePrueba = false;
							}

						}
						canvas.drawText(String.valueOf(contadorTiempo)
								+ "  "
								+ getResources()
										.getString(R.string.pulsaciones),
								this.getWidth() / 4, this.getHeight() / 2,
								paint);
						canvas.drawText(
								db.getString("multi", String.valueOf(0))
										+ "  "
										+ getResources().getString(
												R.string.pulsaciones),
								this.getWidth() / 4,
								this.getHeight()
										- (this.getHeight() / 4 + textSize / 2),
								paint);
						editor.commit();
						if (reproduceUnico) {
							reproduceUnico = false;
							for (int i = 0; i < 10; i++) {
								try {

									if (ganador > Integer
											.parseInt(db.getString("M"
													+ (i + 1) + "r", "0"))) {
										editor.putString("MGanador", vencedor);
										editor.putString("MT", ganador + "");
										Intent intent = new Intent(context,
												Record.class);
										intent.putExtra("Area", "M" + i + "r");
										context.startActivity(intent);

										break;
									}

								} catch (NumberFormatException ex) {
									editor.putString("MGanador", vencedor);
									editor.putString("MT", ganador + "");
									Intent intent = new Intent(context,
											Record.class);
									intent.putExtra("Area", "M" + i + "r");
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
						canvas.drawText(
								getResources().getString(
										R.string.partiasGanadas),
								(float) (this.getWidth() / 4.5),
								(float) (this.getHeight() / 2.5), paint);
						canvas.drawText(
								getResources().getString(R.string.tiempoRecord),
								(float) (this.getWidth() / 4.5),
								(this.getHeight() - (float) (this.getHeight() / 2.5))
										+ (textSize / 2), paint);
						paint.setTextSize((float) (textSize * 2.5));
						paint.setTypeface(Typeface.SERIF);
						canvas.rotate(90, this.getWidth() / 2.0f,
								this.getHeight() / 2.0f);
						canvas.drawText(
								db.getString("MGanador", "").equals("0") ? getResources()
										.getString(R.string.empate)
										: (getResources().getString(
												R.string.Mrecord)
												+ " " + db.getString(
												"MGanador", "")),
								mitadW
										- (paint.measureText(getResources()
												.getString(R.string.verRecord)) / 2),
								0, paint);
					}
				} catch (NullPointerException ex) {

				}
			} else {
				terminadoCanvas = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
