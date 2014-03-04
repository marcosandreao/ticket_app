package br.com.uarini.ticket.acitivity.listener;

public interface IExtratoListener {

	void consultaExtrato( final String cartao, final String token, int limite );
}
