package br.com.uarini.ticket.activity.listener;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public interface IExtratoListener {

	void consultaExtrato(final String cartao, final String token, int limite);
}
