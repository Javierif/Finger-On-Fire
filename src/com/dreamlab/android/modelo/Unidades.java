package com.dreamlab.android.modelo;

import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.vista.ArcadeView;
import com.dreamlab.android.vista.View;
import com.dreamlab.android.controlador.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class Unidades extends Sprite {

	private int maxVida;

	private int columna;
	private int fila;
	private int corteY;

	private boolean girado;

	private Bitmap corazon;

	private int heightCorazon;

	private int widthCorazon;

	private int corteXCorazon;

	private int corteYCorazon;

	private int posicion;

	private int textSize;

	private int corazonX;

	private int corazonY;

	private int limite;

	private Bitmap sandalias;
	private Bitmap sandaliasgiradas;

	private boolean boss = false;

	private Bitmap aura;
	private Bitmap inverso;

	public Unidades(View view, int textSize, Bitmap bmp, Bitmap girado,
			int xSpeed, int ySpeed, int maxVida, Bitmap corazon, int sonido,
			int posicion, int limitePosicion, boolean boss) {
		super(view, bmp, 0, 0, maxVida, sonido);
		this.corazon = corazon;
		this.inverso = girado;
		this.maxVida = maxVida;
		super.xSpeed = xSpeed;
		super.ySpeed = ySpeed;
		this.posicion = posicion;
		this.textSize = textSize;
		this.limite = limitePosicion;
		this.boss = boss;
		super.heightImagen = imagen.getHeight() / 3;
		super.widthImagen = imagen.getWidth() / 3;
		heightCorazon = corazon.getHeight() / 3;
		widthCorazon = corazon.getWidth() / 3;

		super.x = arcadeView.getWidth() / 2 - super.widthImagen / 2;
		super.y = (limitePosicion > 0 && limitePosicion < 3) ? 0 : arcadeView
				.getHeight() - super.heightImagen;

		corazonX = 0;
		corazonY = 0;

		if (xSpeed > 1) {
			this.girado = true;
			columna = 2;
		}
		if (boss)
			aura = BitmapFactory.decodeResource(view.getResources(),
					R.drawable.aura);
		if (boss && maxVida == 58) {
			sandalias = view.redimensionarImagen(R.drawable.muevesandalia,
					true, view.redimension, 3, 1, false);
			sandaliasgiradas = view.redimensionarImagen(
					R.drawable.muevesandalia, true, view.redimension, 3, 1,
					true);
			widthImagen = sandalias.getWidth();
		}

	}

	private void update() {

		if (!muerto) {

			if (!muriendo) {
				if (((y > (arcadeView.getHeight() / 3) * (limite % 3)
						- heightImagen && limite < 3) || (y < (arcadeView
						.getHeight() / 3) * (limite % 3) && limite > 3))
						&& limite > 0) {
					this.ySpeed *= -1;
				}

				if (isChocadoY())
					this.ySpeed *= -1;
				if (isChocadoX() && !boss) {
					this.xSpeed *= -1;
					girarImagen();
				} else if (maxVida == 54
						&& !((ArcadeView) arcadeView).muertosTodos()
						&& isChocadoX() && boss) {
					this.xSpeed *= -1;
					girarImagen();
				} else if (maxVida == 58 && x < -sandalias.getWidth() && xSpeed < 0
						&& boss) {
					this.xSpeed *= -1;
					girarImagen();
				} else if (maxVida == 58 && x > arcadeView.getWidth()
						&& xSpeed > 0 && boss) {
					this.xSpeed *= -1;
					girarImagen();
				}

				if (!boss) {
					y += ySpeed;
					x += xSpeed;
				} else if (maxVida == 54
						&& !((ArcadeView) arcadeView).muertosTodos()) {
					y += ySpeed;
					x += xSpeed;
				} else if (maxVida == 58
						&& !((ArcadeView) arcadeView).muertosTodos()
						&& !(x > arcadeView.getWidth() || x < -sandalias.getWidth())) {
					y += ySpeed;
					x += xSpeed;
				} else if (maxVida == 58
						&& ((ArcadeView) arcadeView).muertosTodos()) {
					y += ySpeed;
					x += xSpeed;
				}

			}

			// esto es un retraso para que no se actualice rapido
			if (fila == 8)
				fila = 0;

			fila += 1;
			corteX = columna * widthImagen;
			corteY = (fila / 3) * heightImagen;

			if (girado) {
				if (columna != 0)
					columna = 2;
				else if (fila == 8)
					muerto = true;
			} else if (columna != 2)
				columna = 0;
			else if (fila == 8)
				muerto = true;
		}

		float porcentajeCalc = maxVida / 8;
		int porcentajeCorazon = (int) (super.vida / porcentajeCalc);
		if (porcentajeCorazon > 8)
			porcentajeCorazon = 8;
		if (porcentajeCorazon >= 6) {
			corteXCorazon = 2 * widthCorazon;
			corteYCorazon = (porcentajeCorazon - 6) * heightCorazon;
		} else if (porcentajeCorazon >= 3) {
			corteXCorazon = 1 * widthCorazon;
			corteYCorazon = (porcentajeCorazon - 3) * heightCorazon;
		} else {
			corteXCorazon = 0;
			corteYCorazon = porcentajeCorazon * heightCorazon;
		}
	}

	public void golpear() {
		if (super.vida == 1 || muriendo) {
			columna = (girado) ? 0 : 2;
			muriendo = true;
		} else {
			AudioJuego.playTemp(sonido);
			columna = 1;
		}
		if (!(super.vida <= 0 && muriendo))
			super.vida -= 1;
	}

	public void reiniciar() {
		if (girado) {
			this.columna = 2;
		} else {
			this.columna = 0;
		}
		super.x = arcadeView.getWidth() / 2 - super.widthImagen / 2;
		super.y = (limite > 0 && limite < 3) ? 0 : arcadeView.getHeight()
				- super.heightImagen;
		vida = maxVida;
		muerto = false;
		muriendo = false;
	}

	public void girarImagen() {
		if (girado) {
			this.girado = false;
			this.columna = 0;
		} else {
			this.girado = true;
			this.columna = 2;
		}

	}

	/**
	 * @return the maxVida
	 */
	public int getMaxVida() {
		return maxVida;
	}

	public boolean isGirado() {
		return girado;
	}

	public boolean isMuriendo() {
		return muriendo;
	}

	public boolean isBoss() {
		return boss;
	}

	public void setBoss(boolean boss) {
		this.boss = boss;
	}

	public void setColumna(int columna) {
		this.columna = columna;
	}

	public void setMuerto(boolean muerto) {
		this.muerto = muerto;
		imagen.recycle();
	}

	public void setMuriendo(boolean muriendo) {
		this.muriendo = muriendo;
	}

	public void onDraw(Canvas canvas) {
		update();

		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setARGB(255, 160, 210, 75);
		paint.setTextSize(textSize);
		paint.setTypeface(Typeface.SERIF);
		if (!(vida == 0 && fila == 8)) {
			if (maxVida == 58 && boss) {
				Rect srcPersonaje = new Rect(0, corteY,sandalias.getWidth(), corteY + heightImagen);
				Rect dstPersonaje;

				dstPersonaje = new Rect(x, y, x + sandalias.getWidth(), y + heightImagen);
				if (columna != 1 || muriendo) {
					if (!girado)
						canvas.drawBitmap(sandalias, srcPersonaje,
								dstPersonaje, null);
					else {
						canvas.drawBitmap(sandaliasgiradas, srcPersonaje,
								dstPersonaje, null);
					}
				} else {
					if (!girado)
						canvas.drawBitmap(imagen, srcPersonaje, dstPersonaje,
								null);
					else {
						canvas.drawBitmap(inverso, srcPersonaje, dstPersonaje,
								null);
					}
				}
			} else {
				Rect srcPersonaje = new Rect(corteX, corteY, corteX
						+ widthImagen, corteY + heightImagen);
				Rect dstPersonaje;

				dstPersonaje = new Rect(x, y, x + widthImagen, y + heightImagen);
				if (!girado)
					canvas.drawBitmap(imagen, srcPersonaje, dstPersonaje, null);
				else {
					canvas.drawBitmap(inverso, srcPersonaje, dstPersonaje, null);
				}
			}
			int fila = posicion / 3;
			int posicionColumna = posicion;
			if (fila > 0)
				posicionColumna = posicion - (fila * 3);

			Rect srcCorazon = new Rect(corteXCorazon, corteYCorazon,
					corteXCorazon + widthCorazon, corteYCorazon + heightCorazon);

			Rect dstCorazon = new Rect(corazonX + widthCorazon
					* posicionColumna, corazonY + heightCorazon * fila,
					corazonX + widthCorazon * posicionColumna + widthCorazon,
					corazonY + heightCorazon * fila + heightCorazon);
			canvas.drawBitmap(corazon, srcCorazon, dstCorazon, null);

			canvas.drawText(String.valueOf(super.vida),
					((widthCorazon * posicionColumna) + widthCorazon / 2)
							- (textSize / 2), heightCorazon * fila
							+ ((heightCorazon / 2) + textSize / 2), paint);
			if (this.arcadeView instanceof ArcadeView)
				// PONER AKI EL AUUURAAAA
				if (!((ArcadeView) arcadeView).muertosTodos() && boss
						&& maxVida == 54) {
					Rect srcAura = new Rect((this.fila % 2)
							* (aura.getWidth() / 2), 0,
							((this.fila % 2) * (aura.getWidth() / 2))
									+ (aura.getWidth() / 2), aura.getHeight());
					Rect dstAura = new Rect(x, y, x + widthImagen, y
							+ heightImagen);
					canvas.drawBitmap(aura, srcAura, dstAura, null);
				}
		}
	}
}