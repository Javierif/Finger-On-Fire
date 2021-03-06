package com.dreamlab.android.controlador;

import com.dreamlab.android.vista.View;

import android.graphics.Canvas;

public class MainLoopThread extends Thread {

	public static final long FPS = 10;
	private View view;
	private boolean running = false;
	private boolean cargado = false;

	public MainLoopThread(View view) {
		this.view = view;
	}

	public void setRunning(boolean run) {
		running = run;
	}

	public boolean isCargado() {
		return cargado;
	}

	public void setCargado(boolean cargado) {
		this.cargado = cargado;
	}

	@Override
	public void run() {
		long ticksPS = 1000 / FPS;
		long startTime;
		long sleepTime;

		while (running) {
			if (cargado) {
				Canvas c = null;
				startTime = System.currentTimeMillis();
				try {
					c = view.getHolder().lockCanvas();
					synchronized (view.getHolder()) {
						view.onDraw(c);
					}
				} finally {
					if (c != null) {
						view.getHolder().unlockCanvasAndPost(c);
					}
				}
				sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
				try {
					if (sleepTime > 0)
						sleep(sleepTime);
					else
						sleep(10);
				} catch (Exception e) {
				}
			}
		}
	}
}