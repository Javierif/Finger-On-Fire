package com.dreamlab.android.controlador;

import java.util.ArrayList;

import com.google.example.games.basegameutils.BaseGameActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class MyGameProgress {

	public static void CompruebaLogros(SharedPreferences db, Editor editor,
			BaseGameActivity context) {
		if (db.getInt("conectado", 0) == 1 && networkAvailable(context))
			for (int i = 0; i < 14; ++i) {
				switch (db.getInt("logros" + i, 0)) {
				case 1:
					context.getGamesClient().unlockAchievement(
							getCodigo(i, context));
					editor.putInt("logros" + i, 2);
					break;
				case 3:
					context.getGamesClient().incrementAchievement(
							getCodigo(i, context), 1);
					editor.putInt("logros" + i, 3);
					break;
				}
			}
	}

	public static void AddLogro(SharedPreferences db, Editor editor,
			BaseGameActivity context, String logro) {
		if (db.getInt("conectado", 0) == 1
				&& db.getInt("logros" + getLogro(logro), 0) != 2 && networkAvailable(context)) {
			context.getGamesClient().unlockAchievement(logro);
			editor.putInt("logros" + getLogro(logro), 2);
		} else if (db.getInt("conectado", 0) == 0)
			editor.putInt("logros" + getLogro(logro), 1);
	}

	public static void AddLogroIncremental(SharedPreferences db, Editor editor,
			BaseGameActivity context, String logro) {
		if (db.getInt("conectado", 0) == 1
				&& db.getInt("logros" + getLogro(logro), 0) != 4 && networkAvailable(context)) {
			context.getGamesClient().unlockAchievement(logro);
			editor.putInt("logros" + getLogro(logro), 4);
		} else if (db.getInt("conectado", 0) == 0)
			editor.putInt("logros" + getLogro(logro), 3);
	}

	private static String getCodigo(int numero, BaseGameActivity context) {
		switch (numero) {
		case 1:
			return context.getResources().getString(
					R.string.achievement_ola_k_ase);
		case 2:
			return context.getResources().getString(
					R.string.achievement_not_too_smelly);
		case 3:
			return context.getResources().getString(
					R.string.achievement_must_improve);
		case 4:
			return context.getResources().getString(
					R.string.achievement_not_bad____);
		case 5:
			return context.getResources().getString(
					R.string.achievement_80_aaaaaaawww_yeeeeaaaaah);
		case 6:
			return context.getResources().getString(
					R.string.achievement_100_close_enought_);
		case 7:
			return context.getResources().getString(
					R.string.achievement_120_im_so_op);
		case 8:
			return context.getResources().getString(
					R.string.achievement_jajajaja_119_lol);
		case 9:
			return context.getResources().getString(
					R.string.achievement_destroyer);
		case 10:
			return context.getResources().getString(
					R.string.achievement_arcade_finished);
		case 11:
			return context.getResources().getString(
					R.string.achievement_vanquished);
		case 12:
			return context.getResources().getString(
					R.string.achievement_kind_of_a_big_deal);
		case 13:
			return context.getResources().getString(
					R.string.achievement_im_very_important);
		case 14:
			return context.getResources().getString(
					R.string.achievement_god_walking_amongst_mere_mortals);

		}
		return "0";
	}

	private static Integer getLogro(String numero) {
		if (numero.equals(R.string.achievement_ola_k_ase))
			return 1;
		if (numero.equals(R.string.achievement_not_too_smelly)) {
			return 2;
		}
		if (numero.equals(R.string.achievement_must_improve)) {
			return 3;
		}
		if (numero.equals(R.string.achievement_not_bad____)) {
			return 4;
		}
		if (numero.equals(R.string.achievement_80_aaaaaaawww_yeeeeaaaaah)) {
			return 5;
		}
		if (numero.equals(R.string.achievement_100_close_enought_)) {
			return 6;
		}
		if (numero.equals(R.string.achievement_120_im_so_op)) {
			return 7;
		}
		if (numero.equals(R.string.achievement_jajajaja_119_lol)) {
			return 8;
		}
		if (numero.equals(R.string.achievement_destroyer)) {
			return 9;
		}
		if (numero.equals(R.string.achievement_arcade_finished)) {
			return 10;
		}
		if (numero.equals(R.string.achievement_vanquished)) {
			return 11;
		}
		if (numero.equals(R.string.achievement_kind_of_a_big_deal)) {
			return 12;
		}
		if (numero.equals(R.string.achievement_im_very_important)) {
			return 13;
		}
		if (numero
				.equals(R.string.achievement_god_walking_amongst_mere_mortals)) {
			return 14;
		}

		return 0;
	}
	
	
	public static boolean networkAvailable(BaseGameActivity context) {
		
		ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectMgr != null) {
			NetworkInfo[] netInfo = connectMgr.getAllNetworkInfo();
			if (netInfo != null) {
				for (NetworkInfo net : netInfo) {
					if (net.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} 
		else {
			
		}
		return false;
	}
}

