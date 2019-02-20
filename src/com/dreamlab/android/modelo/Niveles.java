package com.dreamlab.android.modelo;

import com.dreamlab.android.controlador.R;

public class Niveles {
	private boolean cantidad = true;
	private int vida;
	private int imagen;
	private int xSpeed = 0;
	private int ySpeed = 0;
	private int fondo;
	private int sonidoFondo;
	private int sonido;
	private String temTipo = "0000000";
	private int limitePosicion = 0;
	private boolean boss = false;

	public Niveles(int nivel) {
		
		if (nivel <= 23) {
			fondo = R.drawable.fondogeneral;
			sonidoFondo = R.raw.fondogame;
		} else if (nivel > 23 && nivel < 48) {
			fondo = R.drawable.fondomar;
			sonidoFondo = R.raw.fondomar;
		} else if (nivel > 47) {
			fondo = R.drawable.fondomiedo;
			sonidoFondo = R.raw.miedo;
		}
		if (nivel < 8)
			cantidad = false;
		switch (nivel) {
		case 0:
			vida = 20;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 1:
			vida = 26;
			xSpeed = 5;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 2:
			vida = 45;
			imagen = R.drawable.pollo;
			sonido = R.raw.pollo;
			xSpeed = 5;
			break;
		case 3:
			vida = 45;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			xSpeed = 12;
			break;
		case 4:
			vida = 40;
			xSpeed = -8;
			ySpeed = 3;
			limitePosicion = 5;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 5:
			vida = 40;
			xSpeed = 14;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 6:
			vida = 50;
			xSpeed = 15;
			imagen = R.drawable.cabra;
			sonido = R.raw.cabra;

			break;
		case 7:
			vida = 55;
			xSpeed = -16;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			break;
		case 8:
			cantidad = true;
			vida = 40;
			xSpeed = -8;
			ySpeed = 3;
			limitePosicion = 5;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 9:
			vida = 30;
			xSpeed = -4;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			break;
		case 10:
			vida = 50;
			xSpeed = -12;
			imagen = R.drawable.pollo;
			sonido = R.raw.pollo;
			break;
		case 11:
			vida = 30;
			xSpeed = 15;
			imagen = R.drawable.gusano2;
			sonido = R.raw.gusano;
			break;
		case 12:
			vida = 65;
			xSpeed = 6;
			imagen = R.drawable.jabali;
			sonido = R.raw.jabali;
			break;
		case 13:
			vida = 40;
			xSpeed = 9;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			break;
		case 14:
			vida = 40;
			xSpeed = 16;
			imagen = R.drawable.cabra;
			sonido = R.raw.cabra;
			break;
		case 15:
			vida = 50;
			limitePosicion = 4;
			ySpeed = 21;
			xSpeed = -6;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 16:
			vida = 20;
			xSpeed = 8;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			break;
		case 17:
			vida = 55;
			ySpeed = -8;
			xSpeed = 31;
			limitePosicion = 2;
			imagen = R.drawable.pajaro;
			sonido = R.raw.pajaro;
			temTipo = "10002197";
			break;
		case 18:
			vida = 70;
			xSpeed = 16;
			ySpeed = -21;
			limitePosicion = 2;
			imagen = R.drawable.pajaro;
			sonido = R.raw.pajaro;
			temTipo = "10005487";
			break;
		case 19:
			vida = 35;
			imagen = R.drawable.gusano4;
			sonido = R.raw.gusano;
			xSpeed = -12;
			temTipo = "10004487";
			break;
		case 20:
			vida = 60;
			xSpeed = 21;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			temTipo = "10006497";
			break;
		case 21:
			vida = 30;
			xSpeed = 5;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			temTipo = "10009977";
			break;
		case 22:
			vida = 30;
			xSpeed = 12;
			imagen = R.drawable.oveja;
			sonido = R.raw.oveja;
			temTipo = "10009977";
			break;
		case 23:
			vida = 54;
			xSpeed = 8;
			imagen = R.drawable.setachungafin;
			sonido = R.raw.punchfinal;
			temTipo = "20001487";
			boss=true;
			break;
		case 24:
			cantidad = false;
			vida = 75;
			ySpeed = 16;
			xSpeed = -4;
			imagen = R.drawable.pez;
			sonido = R.raw.peces;
			temTipo = "50001218";
			break;
		case 25:
			cantidad = false;
			vida = 65;
			ySpeed = -17;
			xSpeed = -9;
			limitePosicion = 1;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			temTipo = "50022157";
			break;
		case 26:
			cantidad = false;
			vida = 80;
			ySpeed = 10;
			xSpeed = -2;
			limitePosicion = 5;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			temTipo = "52001218";
			break;
		case 27:
			cantidad = false;
			vida = 65;
			xSpeed = 18;
			ySpeed = -2;
			limitePosicion = 2;
			imagen = R.drawable.jellyblue;
			sonido = R.raw.medusa;
			temTipo = "52001547";
			break;
		case 28:
			cantidad = false;
			vida = 60;
			xSpeed = 10;
			ySpeed = -20;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			temTipo = "50001999";
			break;
		case 29:
			cantidad = false;
			vida = 65;
			xSpeed = -10;
			ySpeed = -20;
			imagen = R.drawable.calamar;
			sonido = R.raw.peces;
			temTipo = "50002293";
			break;
		case 30:
			cantidad = false;
			vida = 70;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			temTipo = "52001668";
			break;
		case 31:
			cantidad = false;
			vida = 50;
			xSpeed = 8;
			ySpeed = 7;
			limitePosicion = 2;
			imagen = R.drawable.jellyorange;
			sonido = R.raw.medusa;
			temTipo = "52001237";
			break;
		case 32:
			vida = 40;
			xSpeed = 8;
			ySpeed = 4;
			limitePosicion = 4;
			imagen = R.drawable.rana1;
			sonido = R.raw.rana;
			temTipo = "52001239";
			break;
		case 33:
			vida = 45;
			xSpeed = 3;
			imagen = R.drawable.tortuga;
			sonido = R.raw.defecto;
			temTipo = "52001218";
			break;
		case 34:
			vida = 60;
			xSpeed = 8;
			ySpeed = 24;
			imagen = R.drawable.jellyblue;
			sonido = R.raw.medusa;
			temTipo = "50003234";
			break;
		case 35:
			vida = 40;
			xSpeed = 8;
			ySpeed = 10;
			limitePosicion = 1;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			temTipo = "50003238";
			break;
		case 36:
			vida = 70;
			xSpeed = 8;
			ySpeed = -4;
			imagen = R.drawable.tortuga;
			sonido = R.raw.defecto;
			temTipo = "52003236";
			break;
		case 37:
			vida = 30;
			xSpeed = 0;
			ySpeed = 20;
			imagen = R.drawable.calamar2;
			sonido = R.raw.peces;
			temTipo = "53001239";
			break;
		case 38:
			vida = 60;
			xSpeed = 2;
			ySpeed = 0;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			temTipo = "53102237";
			break;
		case 39:
			vida = 40;
			xSpeed = -10;
			ySpeed = 8;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			temTipo = "52103218";
			break;
		case 40:
			vida = 30;
			xSpeed = 7;
			ySpeed = 8;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			temTipo = "53102217";
			break;
		case 41:
			cantidad = true;
			vida = 40;
			xSpeed = 10;
			ySpeed = 2;
			imagen = R.drawable.caracol;
			sonido = R.raw.peces;
			temTipo = "50001219";
			break;
		case 42:
			cantidad = true;
			vida = 70;
			xSpeed = 10;
			ySpeed = 4;
			imagen = R.drawable.tortuga;
			sonido = R.raw.peces;
			temTipo = "50002214";
			break;
		case 43:
			cantidad = true;
			vida = 20;
			xSpeed = 14;
			ySpeed = 0;
			imagen = R.drawable.caracol3;
			sonido = R.raw.peces;
			temTipo = "50004215";
			break;
		case 44:
			cantidad = true;
			vida = 30;
			xSpeed = 14;
			ySpeed = 0;
			imagen = R.drawable.tortuga;
			sonido = R.raw.defecto;
			temTipo = "50004213";
			break;
		case 45:
			vida = 20;
			xSpeed = 35;
			ySpeed = -2;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			temTipo = "53004215";
			break;
		case 46:
			vida = 40;
			xSpeed = 8;
			ySpeed = 3;
			imagen = R.drawable.rana1;
			sonido = R.raw.rana;
			temTipo = "52134215";
			break;
		case 47:
			vida = 58;
			xSpeed = -7;
			ySpeed = 3;
			imagen = R.drawable.sandaliadolida;
			sonido = R.raw.sandal;
			temTipo = "50004218";
			boss=true;
			break;
		case 48:
			cantidad = false;
			vida = 70;
			xSpeed = -8;
			ySpeed = -16;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			temTipo = "64000458";
			break;
		case 49:
			cantidad = true;
			vida = 60;
			xSpeed = 8;
			ySpeed = 12;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			temTipo = "64000566";
			break;
		case 50:
			cantidad = true;
			vida = 40;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.cerullo;
			sonido = R.raw.gusano;
			temTipo = "64300556";
			break;
		case 51:
			cantidad = true;
			vida = 45;
			xSpeed = -18;
			ySpeed = 0;
			imagen = R.drawable.boom;
			sonido = R.raw.defecto;
			temTipo = "64200677";
			break;
		case 52:
			cantidad = true;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo2;
			sonido = R.raw.gusano;
			temTipo = "64100347";
			break;
		case 53:
			cantidad = true;
			vida = 45;
			xSpeed = 6;
			ySpeed = 9;
			imagen = R.drawable.demonio;
			sonido = R.raw.defecto;
			temTipo = "64120547";
			break;
		case 54:
			cantidad = true;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo;
			sonido = R.raw.gusano;
			temTipo = "64123397";
			break;
		case 55:
			cantidad = true;
			vida = 30;
			xSpeed = -12;
			ySpeed = 10;
			imagen = R.drawable.demonio5;
			sonido = R.raw.pajaro;
			temTipo = "64123657";
			break;
		case 56:
			cantidad = true;
			vida = 30;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.raton1;
			sonido = R.raw.gritito;
			temTipo = "64003496";
			break;
		case 57:
			cantidad = true;
			vida = 50;
			xSpeed = -18;
			ySpeed = 12;
			limitePosicion = 2;
			imagen = R.drawable.mrcielago;
			sonido = R.raw.murcielago;
			temTipo = "64123556";
			break;
		case 58:
			cantidad = true;
			vida = 40;
			xSpeed = -18;
			ySpeed = 10;
			limitePosicion = 4;
			imagen = R.drawable.boom;
			sonido = R.raw.defecto;
			temTipo = "64123756";
			break;
		case 59:
			cantidad = true;
			vida = 25;
			xSpeed = 18;
			ySpeed = 8;
			imagen = R.drawable.demonio3;
			sonido = R.raw.fantasma;
			temTipo = "64123456";
			break;
		case 60:
			cantidad = true;
			vida = 45;
			xSpeed = 11;
			ySpeed = -10;
			imagen = R.drawable.fantasmabueno;
			sonido = R.raw.fantasma;
			temTipo = "64123756";
			break;
		case 61:
			cantidad = true;
			vida = 45;
			xSpeed = 4;
			ySpeed = 10;
			imagen = R.drawable.mrcielago2;
			sonido = R.raw.murcielago;
			temTipo = "64123856";
			break;
		case 62:
			cantidad = true;
			vida = 45;
			xSpeed = 14;
			ySpeed = 10;
			limitePosicion = 2;
			imagen = R.drawable.demonio;
			sonido = R.raw.fantasma;
			temTipo = "64123836";
			break;
		case 63:
			cantidad = true;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo5;
			sonido = R.raw.gusano;
			temTipo = "64123976";
			break;
		case 64:
			cantidad = true;
			vida = 45;
			xSpeed = -18;
			ySpeed = 0;
			imagen = R.drawable.bloom4;
			sonido = R.raw.medusa;
			temTipo = "64123546";
			break;
		case 65:
			cantidad = true;
			vida = 20;
			xSpeed = -5;
			ySpeed = 0;
			imagen = R.drawable.cerullo;
			sonido = R.raw.gusano;
			temTipo = "64123646";
			break;
		case 66:
			cantidad = true;
			vida = 55;
			xSpeed = -18;
			ySpeed = 10;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			temTipo = "64123756";
			break;
		case 67:
			cantidad = true;
			vida = 55;
			xSpeed = 8;
			ySpeed = 10;
			limitePosicion = 2;
			imagen = R.drawable.demonio3;
			sonido = R.raw.pajaro;
			temTipo = "64123846";
			break;
		case 68:
			cantidad = true;
			vida = 30;
			xSpeed = -12;
			ySpeed = 4;
			limitePosicion = 4;
			imagen = R.drawable.boom;
			sonido = R.raw.gritito;
			temTipo = "64123646";
			break;
		case 69:
			cantidad = true;
			vida = 15;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.bloom3;
			sonido = R.raw.medusa;
			temTipo = "64123736";
			break;
		case 70:
			cantidad = true;
			vida = 40;
			imagen = R.drawable.pajaro;
			sonido = R.raw.pajaro;
			ySpeed = 3;
			xSpeed = 10;
			temTipo = "64123576";
			break;
		case 71:
			vida = 54;
			xSpeed = 16;
			ySpeed = 0;
			imagen = R.drawable.tabris;
			sonido = R.raw.punchfinal;
			temTipo = "42222835";
			boss=true;
			break;

		}
	}

	public int getVida() {
		return vida;
	}

	public void setVida(int vida) {
		this.vida = vida;
	}

	public int getImagen() {
		return imagen;
	}

	public void setImagen(int imagen) {
		this.imagen = imagen;
	}

	/**
	 * @return the temTipo
	 */
	public String getTemTipo() {
		return temTipo;
	}

	/**
	 * @return the xSpeed
	 */
	public int getxSpeed() {
		return xSpeed;
	}

	/**
	 * @param xSpeed
	 *            the xSpeed to set
	 */
	public void setxSpeed(int xSpeed) {
		this.xSpeed = xSpeed;
	}

	/**
	 * @return the ySpeed
	 */
	public int getySpeed() {
		return ySpeed;
	}

	/**
	 * @param ySpeed
	 *            the ySpeed to set
	 */
	public void setySpeed(int ySpeed) {
		this.ySpeed = ySpeed;
	}

	/**
	 * @return the cantidad
	 */
	public boolean getCantidad() {
		return cantidad;
	}

	public int getFondo() {
		return fondo;
	}

	public int getLimitePosicion() {
		return limitePosicion;
	}

	/**
	 * @return the sonidoFondo
	 */
	public int getSonidoFondo() {
		return sonidoFondo;
	}

	/**
	 * @return the sonido
	 */
	public int getSonido() {
		return sonido;
	}

	public boolean isBoss() {
		return boss;
	}

	public void setBoss(boolean boss) {
		this.boss = boss;
	}

	public Niveles(int nivel, int repeticion) {
		int Nivel = nivel + (repeticion * 1000);
		if (nivel < 16)
			cantidad = false;
		switch (Nivel) {
		case 8:
			vida = 26;
			xSpeed = 5;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 9:
			vida = 45;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			xSpeed = 9;
			break;
		case 10:
			vida = 50;
			xSpeed = 12;
			imagen = R.drawable.oveja;
			sonido = R.raw.oveja;
			break;
		case 11:
			vida = 65;
			xSpeed = -15;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 12:
			vida = 15;
			xSpeed = 9;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 13:
			vida = 40;
			xSpeed = 20;
			imagen = R.drawable.jabali;
			sonido = R.raw.jabali;
			break;
		case 14:
			vida = 60;
			xSpeed = 5;
			imagen = R.drawable.oveja;
			sonido = R.raw.oveja;
			break;
		case 15:
			vida = 55;
			ySpeed = 20;
			xSpeed = 3;
			limitePosicion =5;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 16:
			vida = 25;
			xSpeed = -7;
			imagen = R.drawable.gusano2;
			sonido = R.raw.gusano;
			break;
		case 1016:
			cantidad = false;
			vida = 20;
			xSpeed = 2;
			imagen = R.drawable.gusano3;
			sonido = R.raw.gusano;
			break;
		case 17:
			vida = 40;
			xSpeed = 20;
			imagen = R.drawable.jabali;
			sonido = R.raw.jabali;
			break;
		case 1017:
			cantidad = false;
			vida = 15;
			xSpeed = 9;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 18:
			vida = 20;
			xSpeed = -16;
			ySpeed = 7;
			limitePosicion = 2;
			imagen = R.drawable.pajaro2;
			sonido = R.raw.pajaro;
			break;
		case 1018:
			cantidad = false;
			vida = 20;
			xSpeed = 16;
			ySpeed = 7;
			limitePosicion = 2;
			imagen = R.drawable.pajaro3;
			sonido = R.raw.pajaro;
			break;
		case 19:
			vida = 50;
			xSpeed = 12;
			imagen = R.drawable.oveja;
			sonido = R.raw.oveja;
			break;
		case 1019:
			cantidad = false;
			vida = 20;
			xSpeed = -5;
			imagen = R.drawable.oveja;
			sonido = R.raw.oveja;
			break;
		case 20:
			vida = 60;
			xSpeed = 3;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			break;
		case 1020:
			cantidad = false;
			vida = 60;
			xSpeed = -3;
			limitePosicion = 2;
			imagen = R.drawable.pajaro4;
			sonido = R.raw.pajaro;
			break;
		case 21:
			vida = 30;
			xSpeed = -5;
			imagen = R.drawable.gusano4;
			sonido = R.raw.gusano;
			break;

		case 1021:
			cantidad = false;
			vida = 30;
			xSpeed = -7;
			imagen = R.drawable.gusano5;
			sonido = R.raw.gusano;
			break;
		case 22:
			vida = 30;
			xSpeed = -7;
			imagen = R.drawable.pollo;
			sonido = R.raw.pollo;
			break;
		case 1022:
			cantidad = false;
			vida = 55;
			xSpeed = 20;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		case 23:
			vida = 20;
			xSpeed = 30;
			ySpeed = 12;
			imagen = R.drawable.pajaro3;
			sonido = R.raw.pajaro;
			break;
		case 1023:
			cantidad = false;
			vida = 20;
			xSpeed = 20;
			ySpeed = 12;
			imagen = R.drawable.pajaro3;
			sonido = R.raw.pajaro;
			break;
		case 32:
			cantidad = true;
			vida = 15;
			xSpeed = -18;
			ySpeed = 0;
			imagen = R.drawable.rana2;
			sonido = R.raw.rana;
			break;
		case 1032:
			cantidad = false;
			vida = 15;
			xSpeed = 38;
			ySpeed = 7;
			limitePosicion = 2;
			imagen = R.drawable.rana3;
			sonido = R.raw.rana;
			break;
		case 33:
			vida = 25;
			xSpeed = 8;
			ySpeed = -10;
			imagen = R.drawable.pez;
			sonido = R.raw.peces;
			break;
		case 1033:
			cantidad = false;
			vida = 25;
			xSpeed = 5;
			ySpeed = -10;
			imagen = R.drawable.gusano5;
			sonido = R.raw.gusano;
			break;
		case 34:
			cantidad = true;
			vida = 25;
			xSpeed = 14;
			ySpeed = -12;
			imagen = R.drawable.jellyorange;
			sonido = R.raw.medusa;
			break;
		case 1034:
			cantidad = false;
			vida = 25;
			xSpeed = 24;
			ySpeed = -16;
			imagen = R.drawable.jellyblue;
			sonido = R.raw.medusa;
			break;
		case 35:
			cantidad = true;
			vida = 40;
			xSpeed = -8;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			break;
		case 1035:
			cantidad = false;
			vida = 25;
			xSpeed = 4;
			ySpeed = 0;
			imagen = R.drawable.rana2;
			sonido = R.raw.rana;
			break;
		case 36:
			cantidad = true;
			vida = 25;
			xSpeed = 5;
			imagen = R.drawable.caracol;
			sonido = R.raw.peces;
			break;
		case 1036:
			cantidad = false;
			vida = 30;
			xSpeed = 2;
			ySpeed = -17;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 37:
			cantidad = true;
			vida = 30;
			xSpeed = 0;
			ySpeed = 8;
			imagen = R.drawable.jellypurple;
			sonido = R.raw.medusa;
			break;
		case 1037:
			cantidad = false;
			vida = 40;
			xSpeed = 10;
			ySpeed = 8;
			imagen = R.drawable.tortuga;
			sonido = R.raw.peces;
			break;
		case 38:
			cantidad = true;
			vida = 20;
			xSpeed = 10;
			ySpeed = 0;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			break;
		case 1038:
			cantidad = false;
			vida = 10;
			xSpeed = 14;
			ySpeed = 0;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			break;
		case 39:
			vida = 15;
			xSpeed = 10;
			ySpeed = 8;
			limitePosicion = 1;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 1039:
			cantidad = false;
			vida = 5;
			xSpeed = 2;
			ySpeed = -4;
			limitePosicion =2;
			imagen = R.drawable.jellyorange;
			sonido = R.raw.medusa;
			break;
		case 40:
			cantidad = true;
			vida = 30;
			xSpeed = 12;
			ySpeed = -3;
			imagen = R.drawable.gusano2;
			sonido = R.raw.gusano;
			break;
		case 1040:
			cantidad = true;
			vida = 30;
			xSpeed = -5;
			ySpeed = -13;
			imagen = R.drawable.gusano3;
			sonido = R.raw.gusano;
			break;
		case 2040:
			cantidad = true;
			vida = 20;
			xSpeed = 9;
			ySpeed = 8;
			imagen = R.drawable.gusano4;
			sonido = R.raw.gusano;
			break;
		case 3040:
			cantidad = true;
			vida = 20;
			xSpeed = -18;
			ySpeed = 8;
			imagen = R.drawable.gusano5;
			sonido = R.raw.gusano;
			break;
		case 4040:
			cantidad = false;
			vida = 30;
			xSpeed = 14;
			ySpeed = -2;
			imagen = R.drawable.gusano;
			sonido = R.raw.gusano;
			break;
		case 41:
			cantidad = true;
			vida = 20;
			xSpeed = 8;
			ySpeed = 3;
			imagen = R.drawable.caracol;
			sonido = R.raw.peces;
			break;
		case 1041:
			cantidad = true;
			vida = 10;
			xSpeed = 9;
			ySpeed = 8;
			limitePosicion = 1;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 2041:
			cantidad = true;
			vida = 10;
			xSpeed = 20;
			ySpeed = 6;
			limitePosicion = 4;
			imagen = R.drawable.caracol3;
			sonido = R.raw.peces;
			break;
		case 3041:
			cantidad = false;
			vida = 10;
			xSpeed = 4;
			ySpeed = 20;
			imagen = R.drawable.pez;
			sonido = R.raw.peces;
			break;
		case 42:
			cantidad = true;
			vida = 15;
			xSpeed = 4;
			ySpeed = 0;
			imagen = R.drawable.rana1;
			sonido = R.raw.rana;
			break;
		case 1042:
			cantidad = false;
			vida = 15;
			xSpeed = 4;
			ySpeed = 7;
			imagen = R.drawable.rana2;
			sonido = R.raw.rana;
			break;
		case 2042:
			cantidad = false;
			vida = 15;
			xSpeed = 4;
			ySpeed = 7;
			limitePosicion = 2;
			imagen = R.drawable.pez3;
			sonido = R.raw.rana;
			break;
		case 43:
			cantidad = true;
			vida = 32;
			xSpeed = 14;
			ySpeed = 8;
			imagen = R.drawable.caracol2;
			sonido = R.raw.defecto;
			break;
		case 1043:
			cantidad = false;
			vida = 35;
			xSpeed = 24;
			ySpeed = 2;
			imagen = R.drawable.caracol;
			sonido = R.raw.defecto;
			break;
		case 44:
			cantidad = true;
			vida = 10;
			xSpeed = 9;
			ySpeed = 8;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 1044:
			cantidad = true;
			vida = 10;
			xSpeed = 20;
			ySpeed = 6;
			imagen = R.drawable.pez3;
			sonido = R.raw.peces;
			break;
		case 2044:
			cantidad = false;
			vida = 10;
			xSpeed = 10;
			ySpeed = 2;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 45:
			cantidad = true;
			vida = 35;
			xSpeed = 15;
			ySpeed = -12;
			imagen = R.drawable.calamar;
			sonido = R.raw.peces;
			break;
		case 1045:
			cantidad = false;
			vida = 10;
			xSpeed = 14;
			ySpeed = 0;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			break;
		case 46:
			cantidad = true;
			vida = 40;
			xSpeed = -12;
			ySpeed = -2;
			imagen = R.drawable.rana2;
			sonido = R.raw.rana;
			break;
		case 1046:
			cantidad = true;
			vida = 30;
			xSpeed = 8;
			ySpeed = 9;
			imagen = R.drawable.rana3;
			sonido = R.raw.rana;
			break;
		case 2046:
			cantidad = false;
			vida = 25;
			xSpeed = -8;
			ySpeed = -1;
			imagen = R.drawable.rana1;
			sonido = R.raw.rana;
			break;
		case 47:
			cantidad = true;
			vida = 30;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.pato;
			sonido = R.raw.pato;
			break;
		case 1047:
			cantidad = false;
			vida = 5;
			xSpeed = 2;
			ySpeed = 4;
			imagen = R.drawable.pez2;
			sonido = R.raw.peces;
			break;
		case 49:
			cantidad = false;
			vida = 20;
			xSpeed = 18;
			ySpeed = 12;
			imagen = R.drawable.fantasmabueno;
			sonido = R.raw.fantasma2;
			break;
		case 50:
			cantidad = true;
			vida = 50;
			xSpeed = 8;
			ySpeed = -20;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 1050:
			cantidad = false;
			vida = 20;
			xSpeed = 3;
			ySpeed = 0;
			imagen = R.drawable.cerullo;
			sonido = R.raw.defecto;
			break;
		case 51:
			cantidad = true;
			vida = 40;
			xSpeed = -8;
			ySpeed = 0;
			imagen = R.drawable.bloom;
			sonido = R.raw.medusa;
			break;
		case 1051:
			cantidad = true;
			vida = 20;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.bloom2;
			sonido = R.raw.medusa;
			break;
		case 2051:
			cantidad = false;
			vida = 40;
			xSpeed = 12;
			imagen = R.drawable.bloom3;
			sonido = R.raw.medusa;
			break;
		case 52:
			cantidad = true;
			vida = 35;
			xSpeed = 3;
			ySpeed = 0;
			imagen = R.drawable.cerullo3;
			sonido = R.raw.gusano;
			break;
		case 1052:
			cantidad = false;
			vida = 50;
			xSpeed = 8;
			ySpeed = -20;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 53:
			cantidad = false;
			vida = 35;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo5;
			sonido = R.raw.defecto;
			break;
		case 54:
			cantidad = true;
			vida = 20;
			xSpeed = 5;
			ySpeed = 0;
			imagen = R.drawable.cerullo2;
			sonido = R.raw.gritito;
			break;
		case 1054:
			cantidad = true;
			vida = 25;
			xSpeed = 2;
			ySpeed = 0;
			imagen = R.drawable.cerullo3;
			sonido = R.raw.gusano;
			break;
		case 2054:
			cantidad = true;
			vida = 10;
			xSpeed = -6;
			ySpeed = 0;
			imagen = R.drawable.cerullo4;
			sonido = R.raw.gritito;
			break;
		case 3054:
			cantidad = true;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo5;
			sonido = R.raw.gusano;
			break;
		case 4054:
			cantidad = false;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo6;
			sonido = R.raw.gritito;
			break;
		case 5054:
			cantidad = false;
			vida = 20;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.cerullo7;
			sonido = R.raw.gusano;
			break;
		case 55:
			cantidad = true;
			vida = 30;
			xSpeed = -2;
			ySpeed = 5;
			imagen = R.drawable.demonio2;
			sonido = R.raw.fantasma;
			break;
		case 1055:
			cantidad = false;
			vida = 30;
			xSpeed = 18;
			ySpeed = 10;
			imagen = R.drawable.demonio3;
			sonido = R.raw.fantasma;
			break;
		case 56:
			cantidad = true;
			vida = 2;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.raton1;
			sonido = R.raw.gritito;
			break;
		case 1056:
			cantidad = true;
			vida = 6;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.raton1;
			sonido = R.raw.gritito;
			break;
		case 2056:
			cantidad = true;
			vida = 10;
			xSpeed = -19;
			ySpeed = 0;
			imagen = R.drawable.raton2;
			sonido = R.raw.gritito;
			break;
		case 3056:
			cantidad = false;
			vida = 7;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.raton2;
			sonido = R.raw.gritito;
			break;
		case 57:
			cantidad = true;
			vida = 20;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.raton2;
			sonido = R.raw.gritito;
			break;
		case 1057:
			cantidad = false;
			vida = 30;
			xSpeed = -12;
			ySpeed = 10;
			imagen = R.drawable.bloom2;
			sonido = R.raw.medusa;
			break;
		case 58:
			cantidad = true;
			vida = 40;
			xSpeed = 18;
			ySpeed = 10;
			imagen = R.drawable.bloom4;
			sonido = R.raw.medusa;
			break;
		case 1058:
			cantidad = true;
			vida = 20;
			xSpeed = -20;
			ySpeed = 0;
			imagen = R.drawable.raton1;
			sonido = R.raw.gritito;
			break;
		case 2058:
			cantidad = false;
			vida = 15;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.bloom2;
			sonido = R.raw.medusa;
			break;
		case 59:
			cantidad = true;
			vida = 30;
			xSpeed = -12;
			ySpeed = 10;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 1059:
			cantidad = false;
			vida = 30;
			xSpeed = 7;
			ySpeed = 0;
			imagen = R.drawable.demonio4;
			sonido = R.raw.fantasma;
			break;
		case 60:
			cantidad = true;
			vida = 30;
			xSpeed = 7;
			ySpeed = -12;
			imagen = R.drawable.fantasmabueno;
			sonido = R.raw.gritito;
			break;
		case 1060:
			cantidad = false;
			vida = 40;
			xSpeed = -12;
			ySpeed = 10;
			imagen = R.drawable.fantasmabueno;
			sonido = R.raw.fantasma;
			break;
		case 61:
			cantidad = false;
			vida = 65;
			xSpeed = -11;
			ySpeed = 10;
			imagen = R.drawable.mrcielago3;
			sonido = R.raw.medusa;
			break;
		case 62:
			cantidad = true;
			vida = 15;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.raton2;
			sonido = R.raw.gritito;
			break;
		case 1062:
			cantidad = false;
			vida = 30;
			xSpeed = -12;
			ySpeed = -10;
			limitePosicion = 4;
			imagen = R.drawable.boom;
			sonido = R.raw.defecto;
			break;
		case 63:
			cantidad = true;
			vida = 20;
			xSpeed = 12;
			ySpeed = 0;
			imagen = R.drawable.cerullo3;
			sonido = R.raw.gusano;
			break;
		case 1063:
			cantidad = false;
			vida = 40;
			xSpeed = -4;
			ySpeed = -1;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 64:
			cantidad = true;
			vida = 25;
			xSpeed = 18;
			ySpeed = 8;
			imagen = R.drawable.mrcielago2;
			sonido = R.raw.murcielago;
			break;
		case 1064:
			cantidad = true;
			vida = 25;
			xSpeed = -18;
			ySpeed = -8;
			imagen = R.drawable.mrcielago3;
			sonido = R.raw.murcielago;
			break;
		case 2064:
			cantidad = false;
			vida = 25;
			xSpeed = -7;
			ySpeed = 8;
			imagen = R.drawable.mrcielago;
			sonido = R.raw.murcielago;
			break;
		case 65:
			cantidad = true;
			vida = 20;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.cerullo;
			sonido = R.raw.gusano;
			break;
		case 1065:
			cantidad = false;
			vida = 25;
			xSpeed = -18;
			ySpeed = 22;
			imagen = R.drawable.mrcielago;
			sonido = R.raw.murcielago;
			break;
		case 66:
			cantidad = true;
			vida = 40;
			xSpeed = -8;
			ySpeed = 8;
			imagen = R.drawable.fantasmabueno;
			sonido = R.raw.fantasma;
			break;
		case 1066:
			cantidad = false;
			vida = 30;
			xSpeed = -12;
			ySpeed = -10;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 67:
			cantidad = true;
			vida = 55;
			xSpeed = 8;
			ySpeed = -10;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 1067:
			cantidad = false;
			vida = 35;
			xSpeed = 13;
			ySpeed = 10;
			imagen = R.drawable.fantasmamalo;
			sonido = R.raw.fantasma;
			break;
		case 68:
			cantidad = true;
			vida = 30;
			xSpeed = 7;
			ySpeed = 0;
			imagen = R.drawable.raton2;
			sonido = R.raw.gritito;
			break;
		case 1068:
			cantidad = true;
			vida = 30;
			xSpeed = -2;
			ySpeed = 0;
			imagen = R.drawable.raton1;
			sonido = R.raw.gritito;
			break;
		case 2068:
			cantidad = false;
			vida = 20;
			xSpeed = 8;
			ySpeed = 10;
			imagen = R.drawable.mrcielago2;
			sonido = R.raw.murcielago;
			break;
		case 69:
			cantidad = true;
			vida = 15;
			xSpeed = -12;
			ySpeed = 0;
			imagen = R.drawable.bloom4;
			sonido = R.raw.medusa;
		case 1069:
			cantidad = true;
			vida = 30;
			xSpeed = -2;
			ySpeed = 0;
			limitePosicion = 2;
			imagen = R.drawable.demonio4;
			sonido = R.raw.fantasma;
			break;
		case 2069:
			cantidad = true;
			vida = 20;
			xSpeed = 8;
			ySpeed = 0;
			imagen = R.drawable.cerullo7;
			sonido = R.raw.gritito;
			break;
		case 3069:
			cantidad = false;
			vida = 30;
			xSpeed = -12;
			ySpeed = -10;
			imagen = R.drawable.cerullo6;
			sonido = R.raw.gusano;
			break;
		case 70:
			cantidad = true;
			vida = 25;
			xSpeed = 8;
			ySpeed = -10;
			imagen = R.drawable.pez;
			sonido = R.raw.peces;
			break;
		case 1070:
			cantidad = false;
			vida = 35;
			imagen = R.drawable.pollo;
			sonido = R.raw.pollo;
			ySpeed = -8;
			break;
		case 71:
			cantidad = true;
			vida = 50;
			imagen = R.drawable.cerdo;
			sonido = R.raw.cerdo;
			ySpeed = 15;
			break;
		case 1071:
			cantidad = true;
			vida = 25;
			xSpeed = -7;
			ySpeed = 8;
			imagen = R.drawable.mrcielago;
			sonido = R.raw.murcielago;
			break;
		case 2071:
			cantidad = true;
			vida = 20;
			xSpeed = 0;
			ySpeed = 0;
			imagen = R.drawable.huevo;
			sonido = R.raw.defecto;
			break;
		case 3071:
			cantidad = false;
			vida = 50;
			xSpeed = 15;
			ySpeed = 1;
			limitePosicion = 4;
			imagen = R.drawable.ardilla;
			sonido = R.raw.gritito;
			break;
		}
	}

}
