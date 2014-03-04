package br.com.uarini.ticket.service;

import java.net.URLEncoder;

import android.content.Intent;
import android.util.Log;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class ExtratoService extends TicketService {

	protected static final String URL = "http://www.ticket.com.br/portal-web/consult-card/release/json?txtOperacao=lancamentos&token=%s&card={query}&rows=%d";//&rows=50";
	
	public ExtratoService() {
		super(ExtratoService.class.getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d("TOKEN", intent.getStringExtra(KEY_TOKEN));
		final String endpoint = String.format(URL, URLEncoder.encode(intent.getStringExtra(KEY_TOKEN)), intent.getIntExtra(KEY_NUM_LINHAS, 5));
		intent.putExtra(KEY_ENDPOINT, endpoint);
		super.onHandleIntent(intent);
	}
}
