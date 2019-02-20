package com.dreamlab.android.modelo;

import com.dreamlab.android.vista.View;

import android.graphics.Bitmap;

public abstract class Sprite {

	protected int y;
	protected int x;
	protected int corteX;
	protected int vida;
	protected int sonido;
	protected boolean muerto = false;
	protected boolean muriendo = false;
	protected Bitmap imagen;
	protected int widthImagen;
	protected int heightImagen;
	protected View arcadeView;
	protected int xSpeed;
	protected int ySpeed;

	public Sprite(View view, Bitmap imagen, int x, int y, int vida, int sonido) {
		this.x = x;
		this.y = y;
		this.vida = vida;
		this.imagen = imagen;
		this.arcadeView = view;
		this.sonido = sonido;
	}

	public boolean isGolpeado(float X, float Y) {
		return X > this.x && X < this.x + widthImagen && Y > this.y
				&& Y < this.y + heightImagen;
	}

	public boolean isChocadoX() {
		if (x > arcadeView.getWidth() - widthImagen - xSpeed || x + xSpeed < 0)
			return true;
		return false;
	}

	public boolean isChocadoY() {
		if (y > arcadeView.getHeight() - heightImagen - ySpeed
				|| y + ySpeed < 0)
			return true;
		return false;
	}

	public int getSonido() {
		return sonido;
	}

	public boolean isBorrar() {
		return muerto;
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getY() {
		return y;
	}

	public int getX() {
		return x;
	}

	public int getxSpeed() {
		return xSpeed;
	}

	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	public int getySpeed() {
		return ySpeed;
	}

	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}

	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setX(int x) {
		this.x = x;
	}

}
