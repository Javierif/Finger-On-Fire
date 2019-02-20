package com.dreamlab.android.controlador;

import android.content.Context;
import android.media.*;

public class AudioJuego {
	
	private static MediaPlayer introreproductor = null;
	private static MediaPlayer mireproductor = null;
	private static MediaPlayer tempreproductor = null;
	
	private static Context pContext;
	private static SoundPool sndPool;
	private static float rate = 1.0f;
	private static float masterVolume = 1.0f;
	private static float leftVolume = 1.0f;
	private static float rightVolume = 1.0f;
    private static float balance = 0.5f;
	
	
	public static void reproduceIntro(Context context, int resource) {

		para_intro();
			
		introreproductor = MediaPlayer.create(context, resource);
			
		introreproductor.setLooping(true);
			
		introreproductor.start();
	}

	public static void reproduce(Context context, int resource) {

		para_musica();
			
			mireproductor = MediaPlayer.create(context, resource);
			
			mireproductor.setLooping(true);
			
			mireproductor.start();
	}

	public static SoundPool creaSoundTemp(Context context){
		  sndPool = new SoundPool(16, AudioManager.STREAM_MUSIC, 100);
	 	  pContext = context;
	 	  return sndPool;
	}
	
	
	public static int loadTemp(int sound_id){
		return sndPool.load(pContext, sound_id, 1);
	}
	 
	public static void playTemp(int sound_id){
		sndPool.play(sound_id, leftVolume, rightVolume, 1, 0, rate);
	}

	public static void para_musica() {

		if (mireproductor != null && mireproductor.isPlaying()) {
			mireproductor.stop();
			mireproductor.release();
			mireproductor = null;
		}
	}

	public static void para_intro() {

		if (introreproductor != null && introreproductor.isPlaying()) {
			introreproductor.stop();
			introreproductor.release();
			introreproductor = null;
		}
	}
	public static MediaPlayer get_mireproductor() {
		return mireproductor;
	}

	public static MediaPlayer get_tempreproductor() {
		return tempreproductor;
	}
}