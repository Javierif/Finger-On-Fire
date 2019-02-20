package com.dreamlab.android.modelo;

import com.dreamlab.android.controlador.Arcade;
import com.dreamlab.android.controlador.AudioJuego;
import com.dreamlab.android.controlador.MyGameProgress;
import com.dreamlab.android.vista.ArcadeView;
import com.dreamlab.android.controlador.R;
import com.google.example.games.basegameutils.BaseGameActivity;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class TempSprite extends Sprite {
	private int tipo;
	private int Nx;
	private int retrasillo;
	private boolean muriendo;
	private int tiempoRespawn;

	public TempSprite(ArcadeView gameView, int Tipos) {
		super(gameView, null, 0, 0, 1, 0);
		switch (Tipos) {
		case 49:
			super.imagen = gameView.redimensionarImagen(R.drawable.angeltemp,
					gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.y = (int) Math.round(Math.random()
					* (gameView.getHeight() - imagen.getHeight()));
			super.x = gameView.getWidth() - widthImagen;
			super.xSpeed = (int) -((Math.random() * 10) + 5);
			super.sonido = R.raw.angel;
			break;
		case 50:
			super.imagen = gameView.redimensionarImagen(R.drawable.demontemp,
					gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.vida = 2;
			super.y = (int) Math.round(Math.random()
					* (gameView.getHeight() - imagen.getHeight()));
			super.x = gameView.getWidth() - widthImagen;
			super.xSpeed = (int) -((Math.random() * 15) + 8);
			super.sonido = R.raw.fantasma;
			;
			break;
		case 51:
			super.imagen = gameView.redimensionarImagen(
					R.drawable.congeladotemp, gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.y = (int) Math.round(Math.random()
					* (gameView.getHeight() - imagen.getHeight()));
			super.x = gameView.getWidth() - widthImagen;
			super.xSpeed = (int) -((Math.random() * 10) + 5);
			super.ySpeed = (int) ((Math.random() * 5) + 1);
			super.sonido = R.raw.congelado;
			break;
		case 52:
			super.imagen = gameView.redimensionarImagen(R.drawable.ragetemp,
					gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.y = (int) Math.round(Math.random()
					* (gameView.getHeight() - imagen.getHeight()));
			super.vida = 2;
			super.x = gameView.getWidth() - widthImagen;
			super.xSpeed = (int) -((Math.random() * 14) + 5);
			super.ySpeed = (int) ((Math.random() * 7) + 1);
			sonido = R.raw.rage;
			break;
		case 53:
			super.imagen = gameView.redimensionarImagen(R.drawable.pompatemp,
					gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.y = gameView.getHeight() - imagen.getHeight();
			super.x = (int) Math.round(Math.random()
					* (gameView.getWidth() - widthImagen));
			super.vida = (int) ((Math.random() * 15) + 5);
			super.xSpeed = +5;
			super.ySpeed = (int) -((Math.random() * 14) + 1);
			super.sonido = R.raw.pompa;
			break;
		case 54:
			super.imagen = gameView.redimensionarImagen(
					R.drawable.fantasmatemp, gameView.redimension);
			widthImagen = super.imagen.getWidth() / 4;
			super.y = (int) Math.round(Math.random()
					* (gameView.getHeight() - imagen.getHeight()));
			super.x = (int) Math.round(Math.random()
					* (gameView.getWidth() - widthImagen));
			super.vida = (int) ((Math.random() * 10) + 1);
			super.xSpeed = (int) ((Math.random() * 14) + 5);
			super.ySpeed = (int) ((Math.random() * 10) + 1);
			super.sonido = R.raw.gritofantasma;
			break;
		}

		this.tipo = Tipos;
		this.Nx = super.x;
		heightImagen = imagen.getHeight();
	}

	private void update() {
		if (!muriendo) {
			corteX = (corteX < 1) ? ++corteX : 0;

			if (tipo > 52) {
				if (--super.vida < 1)
					super.muerto = true;

				if (x <= Nx - widthImagen / 2 || x < 0)
					xSpeed *= -1;

				if (x >= Nx + widthImagen / 2)
					xSpeed *= -1;

			} else {
				if (((x < -widthImagen || x > arcadeView.getWidth()
						+ widthImagen) || (isChocadoX()) && vida != 1))
					this.xSpeed *= -1;
				if (vida != 1)
					if (--vida < 1)
						muerto = true;
			}
			corteX = corteX * widthImagen;
			x = xSpeed + x;
			y = ySpeed + y;
		} else {
			corteX = 3 * widthImagen;
			if (++retrasillo == 10)
				muerto = true;
		}

	}

	public int getTipo() {
		return tipo;
	}

	public int getTiempoRespawn() {
		return tiempoRespawn;
	}

	public void setTiempoRespawn(int tiempoRespawn) {
		this.tiempoRespawn = tiempoRespawn;
	}

	public void reiniciar() {
		switch (tipo) {
		case 49:
			super.y = (int) Math.round(Math.random()
					* (arcadeView.getHeight() - imagen.getHeight()));
			super.x = arcadeView.getWidth() - widthImagen;
			super.vida = 1;
			break;
		case 50:
			widthImagen = super.imagen.getWidth() / 4;
			super.vida = 2;
			super.y = (int) Math.round(Math.random()
					* (arcadeView.getHeight() - imagen.getHeight()));
			super.x = arcadeView.getWidth() - widthImagen;
			break;
		case 51:
			super.y = (int) Math.round(Math.random()
					* (arcadeView.getHeight() - imagen.getHeight()));
			super.x = arcadeView.getWidth() - widthImagen;
			super.vida = 1;
			break;
		case 52:
			super.y = (int) Math.round(Math.random()
					* (arcadeView.getHeight() - imagen.getHeight()));
			super.vida = 2;
			super.x = arcadeView.getWidth() - widthImagen;
			break;
		case 53:
			super.y = arcadeView.getHeight() - imagen.getHeight();
			super.x = (int) Math.round(Math.random()
					* (arcadeView.getWidth() - widthImagen));
			super.vida = (int) ((Math.random() * 15) + 5);
			super.ySpeed = (int) -((Math.random() * 14) + 1);
			break;
		case 54:
			super.y = (int) Math.round(Math.random()
					* (arcadeView.getHeight() - imagen.getHeight()));
			super.x = (int) Math.round(Math.random()
					* (arcadeView.getWidth() - widthImagen));
			super.vida = (int) ((Math.random() * 10) + 1);
			super.xSpeed = (int) ((Math.random() * 14) + 5);
			super.ySpeed = (int) ((Math.random() * 10) + 1);
			break;
		}
		muriendo = false;
		muerto = false;
		retrasillo = 0;
	}

	public void golpeado() {
		if (!muriendo) {
			vida = 0;
			switch (tipo) {
			case 49:
				((ArcadeView) arcadeView).gainTime(6);

				MyGameProgress.AddLogroIncremental(arcadeView.getDB(), arcadeView.getEditor(), (BaseGameActivity) arcadeView.getContext(), arcadeView.getContext().getResources().getString(
												R.string.achievement_god_walking_amongst_mere_mortals));
				((ArcadeView) arcadeView).getEditor().commit();
				break;
			case 50:
				((ArcadeView) arcadeView).gainTime(-6);
				break;
			case 51:
				((ArcadeView) arcadeView).efectoVelocidad(true);
				AudioJuego
						.playTemp(sonido);
				break;
			case 52:
				((ArcadeView) arcadeView).efectoVelocidad(false);
				AudioJuego
						.playTemp(sonido);;
				break;
			case 53:
				if (((int) Math.round(Math.random() * 25 + 1)) == 3) {
					((ArcadeView) arcadeView).gainTime(2);
				}
				break;
			case 54:
				if (((int) Math.round(Math.random() * 15 + 1)) == 3) {
					((ArcadeView) arcadeView).gainTime(3);
				}
				break;
			}
			muriendo = true;
			AudioJuego.playTemp(sonido);
		}

	}

	public void onDraw(Canvas canvas) {
		update();
		if (!isBorrar()) {
			Rect srcPersonaje = new Rect(corteX, 0, corteX + super.widthImagen,
					super.imagen.getHeight());
			Rect dstPersonaje = new Rect(x, y, x + super.widthImagen, y
					+ super.imagen.getHeight());
			canvas.drawBitmap(super.imagen, srcPersonaje, dstPersonaje, null);
		}
	}

}