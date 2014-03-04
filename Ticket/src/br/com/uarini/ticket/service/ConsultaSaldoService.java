package br.com.uarini.ticket.service;

import android.content.Intent;
import br.com.uarini.ticket.http.HttpHandler;
import br.com.uarini.ticket.util.Util;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class ConsultaSaldoService extends TicketService {

	private static final String URL = "http://www.ticket.com.br/portal-web/consult-card/balance/json?chkProduto=%s&card={query}";

	public ConsultaSaldoService() {
		super(ConsultaSaldoService.class.getSimpleName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.IntentService#onHandleIntent(android.content.Intent)
	 */
	@Override
	protected void onHandleIntent(Intent intent) {
		final String endpoint = String.format(URL, Util.getBin(intent.getIntExtra(KEY_TIPO, 0)));
		intent.putExtra(KEY_ENDPOINT, endpoint);
		try {
			HttpHandler.releaseInstance();
			HttpHandler.getInstance().getPageContent(URL_GET_COOKIE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onHandleIntent(intent);
	}
}
