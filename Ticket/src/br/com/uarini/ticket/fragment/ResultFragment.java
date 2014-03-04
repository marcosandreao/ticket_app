package br.com.uarini.ticket.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import br.com.uarini.ticket.R;
import br.com.uarini.ticket.activity.TicketActivity;
import br.com.uarini.ticket.activity.listener.IExtratoListener;
import br.com.uarini.ticket.db.CartaoBO;
import br.com.uarini.ticket.json.AbstractValue;
import br.com.uarini.ticket.json.ResponseConsulta;
import br.com.uarini.ticket.json.Scheduling;
import br.com.uarini.ticket.util.Util;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class ResultFragment extends Fragment {

	private TextView tvData;
	private TextView tvSaldo;
	private ViewGroup llAgendamento;
	private final CartaoBO bo = new CartaoBO();

	private IExtratoListener listener;
	
	private ResponseConsulta response;
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_result, null);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (IExtratoListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_result_fragment, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		final MenuItem item = menu.findItem(R.id.action_persiste);
		if ( this.response == null ) {
			item.setVisible(false);
			return;
		}
		item.setVisible(true);
		if ( this.bo.hasCartao(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber()) ) {
			item.setTitle(R.string.remover_cart_o);
		} else {
			item.setTitle(R.string.salvar_cart_o);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ( item.getItemId() == R.id.action_extrato ){
			if ( this.listener != null ) {
				this.listener.consultaExtrato(this.response.getCard().getBalance().getNumber(), this.response.getToken(), Util.QUANTIDADE_INICIO);
			}
			return true;
		}
		if ( item.getItemId() == R.id.action_persiste ){
			if ( this.bo.hasCartao(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber()) ) {
				this.bo.remove(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber());
			} else {
				int indice = Util.getIndiceCartaoBin( this.response.getCard().getBalance().getBin());
				this.bo.persiste(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber(), indice);
			}
			this.getActivity().supportInvalidateOptionsMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		
		this.response = (ResponseConsulta) getArguments().getSerializable(TicketActivity.KEY_RESULT);
		
		this.tvData = (TextView) view.findViewById(R.id.tv_date);
		this.tvSaldo = (TextView) view.findViewById(R.id.tv_saldo);
		this.llAgendamento = (ViewGroup) view.findViewById(R.id.ll_agendamentos);
		
		final TextView tvNumeroCartao = (TextView) view.findViewById(R.id.tv_number_card);
		
		this.llAgendamento.removeAllViews();
		
		this.tvData.setText(response.getCard().getBalance().getDate());
		this.tvSaldo.setText(response.getCard().getBalance().getValue());
		tvNumeroCartao.setText(Util.formatCardNumber(response.getCard().getBalance().getNumber()));
		
		if (response.getCard().getScheduling() == null	|| response.getCard().getScheduling().length < 1) {
			this.addValue(this.llAgendamento, null);
		} else {
			for (Scheduling scheduling : response.getCard().getScheduling()) {
				this.addValue(this.llAgendamento, scheduling);
			}
		}
		
		this.getActivity().supportInvalidateOptionsMenu();
		
		// atualiza data se existe cartao
		if ( this.bo.hasCartao(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber()) ) {
			int indice = Util.getIndiceCartaoBin( this.response.getCard().getBalance().getBin());
			this.bo.persiste(this.getActivity().getContentResolver(), this.response.getCard().getBalance().getNumber(), indice);
		}
	}

	
	private void addValue(ViewGroup target, AbstractValue release) {
		final View view = LayoutInflater.from(this.getActivity()).inflate(R.layout.item_agendado, null);

		TextView tvDataAgendamento = (TextView) view.findViewById(R.id.tv_agendamento_data);
		TextView tvSaldoAgendamento = (TextView) view.findViewById(R.id.tv_agendamento_saldo);
		TextView tvInfoAgendamento = (TextView) view.findViewById(R.id.tv_agendamento_info);

		if (release == null) {
			tvDataAgendamento.setText("");
			tvSaldoAgendamento.setText(R.string.nenhum_item_encontrado_);
			tvInfoAgendamento.setText("");
		} else {
			tvDataAgendamento.setText(release.getDate());
			tvSaldoAgendamento.setText(release.getValue());
			tvInfoAgendamento.setText(release.getDescription());
		}
		target.addView(view);
	}

}
