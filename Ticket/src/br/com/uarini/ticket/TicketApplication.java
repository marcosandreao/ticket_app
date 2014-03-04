package br.com.uarini.ticket;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class TicketApplication extends Application {
	private static final String CARTAO = "cartao";

	private SharedPreferences sharedPref;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		this.sharedPref = this.getSharedPreferences("ticket",
				Context.MODE_PRIVATE);
	}

	public int getCartao() {
		return this.sharedPref.getInt(CARTAO, 0);
	}

	public void saveCartao(int index) {
		this.sharedPref.edit().putInt(CARTAO, index).commit();
	}
}
