package br.com.uarini.ticket.service;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import br.com.uarini.ticket.http.HttpHandler;
import br.com.uarini.ticket.json.ResponseConsulta;

public class TicketService extends IntentService{

	public static final String ACTION_DADOS = "br.com.uarini.ticket.DADOS";
	public static final String KEY_DADOS = "DADOS";
	public static final String KEY_IS_CONSULTA_SALDO = "consuta_saldo";
	
	public static final String KEY_OK = "OK";
	public static final String KEY_TIPO = "tipo";
	public static final String KEY_NUMERO_CARTAO = "numero_cartao";
	public static final String KEY_TOKEN = "token";
	public static final String KEY_ENDPOINT = "endpoint";
	public static final String KEY_NUM_LINHAS = "limite";

	protected static final String URL_GET_COOKIE = "http://www.ticket.com.br/portal/lumis/portal/controller/html/NavigationLogger.jsp";
	
	public TicketService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		final String endpoint = intent.getStringExtra(KEY_ENDPOINT);
		final Intent intentResult = new Intent(ACTION_DADOS);
		intentResult.putExtra(KEY_IS_CONSULTA_SALDO, (this instanceof ConsultaSaldoService));
		intentResult.putExtra(KEY_OK, true);
		try {
			final HttpHeaders requestHeaders = new HttpHeaders();
			requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);
			requestHeaders.add("User-Agent", HttpHandler.USER_AGENT);
			requestHeaders.add("HOST", "www.ticket.com.br");
			requestHeaders.add("X-Requested-With", "XMLHttpRequest");
			requestHeaders.add("Referer", URL_GET_COOKIE);

			if (HttpHandler.getInstance().getCookies() != null) {
				for (final String cookie : HttpHandler.getInstance()
						.getCookies()) {
					requestHeaders.add("Cookie", cookie.split(";", 1)[0]);
				}
			}
			final HttpEntity<Object> requestEntity = new HttpEntity<Object>(requestHeaders);
			final RestTemplate restTemplate = new RestTemplate();

			restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

			final ResponseEntity<ResponseConsulta> responseEntity = restTemplate
					.exchange(endpoint, HttpMethod.GET, requestEntity, ResponseConsulta.class, intent.getStringExtra(KEY_NUMERO_CARTAO));
			final ResponseConsulta body = (ResponseConsulta) responseEntity.getBody();
			intentResult.putExtra(KEY_DADOS, body);
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			intentResult.putExtra(KEY_OK, false);
		} catch (Exception e) {
			e.printStackTrace();
			intentResult.putExtra(KEY_OK, false);
		}
		LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intentResult);
	}

}
