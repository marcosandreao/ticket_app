package br.com.uarini.ticket.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import br.com.uarini.ticket.R;
import br.com.uarini.ticket.acitivity.TicketActivity;
import br.com.uarini.ticket.acitivity.listener.IExtratoListener;
import br.com.uarini.ticket.adapter.ExtratoAdapter;
import br.com.uarini.ticket.json.Release;
import br.com.uarini.ticket.json.ResponseConsulta;
import br.com.uarini.ticket.util.Util;

public class ExtratoFragment extends Fragment {

	private ExtratoAdapter mAdapter;
	
	private IExtratoListener listener;
	
	private ResponseConsulta response;
	
	private TextView tvTitulo;
	
	private int numItem = 100;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (IExtratoListener) activity;
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_extrato, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		this.tvTitulo = (TextView) view.findViewById(R.id.tv_title);
		final ListView lv = (ListView) view.findViewById(android.R.id.list);
		
		lv.setEmptyView(view.findViewById(R.id.tv_empty));
		
		this.response = (ResponseConsulta) this.getArguments().getSerializable(TicketActivity.KEY_RESULT);

		this.mAdapter = new ExtratoAdapter(this.getActivity(), new ArrayList<Release>());
		lv.setAdapter(this.mAdapter);
		this.exibiExtrato();
	}

	private void exibiExtrato() {
		this.mAdapter.clear();
		if ( response.getCard().getRelease() != null && this.response.getCard().getRelease().length > 0) {
			for ( Release release : this.response.getCard().getRelease() ) {
				this.mAdapter.add(release);
			}
		} 
		this.exibiNumeroItens(this.mAdapter.getCount());
		this.mAdapter.notifyDataSetChanged();
		this.numItem = this.mAdapter.getCount();
		this.getActivity().supportInvalidateOptionsMenu();
	}

	private void exibiNumeroItens( final int total ) {
		if ( total == 0 ) {
			this.tvTitulo.setText(R.string.transa_es);
		} else {
			final String titulo = this.getString(R.string.ultimas) + " (" + total + ") " + this.getString(R.string.transa_es);
			this.tvTitulo.setText(titulo);
		}
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		final MenuItem itemIncio = menu.findItem(R.id.action_ultimos_cinco);
		final MenuItem itemMeio = menu.findItem(R.id.action_ultimos_quinze);
		final MenuItem itemFim = menu.findItem(R.id.action_ultimos_trinta);
		
		itemIncio.setVisible(true);
		itemMeio.setVisible(true);
		itemFim.setVisible(true);
		
		if ( this.numItem < Util.QUANTIDADE_INICIO ) {
			itemIncio.setVisible(false);
			itemMeio.setVisible(false);
			itemFim.setVisible(false);
		} else if ( numItem > Util.QUANTIDADE_INICIO && numItem < Util.QUANTIDADE_MEDIA ) {
			itemIncio.setVisible(true);
			itemMeio.setVisible(true);
			itemFim.setVisible(false);
		} 
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_extrato_fragment, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_ultimos_cinco:
			this.listener.consultaExtrato(this.response.getCard().getBalance().getNumber(), this.response.getToken(), Util.QUANTIDADE_INICIO);
			break;
		case R.id.action_ultimos_quinze:
			this.listener.consultaExtrato(this.response.getCard().getBalance().getNumber(), this.response.getToken(), Util.QUANTIDADE_MEDIA);
			break;
		case R.id.action_ultimos_trinta:
			this.listener.consultaExtrato(this.response.getCard().getBalance().getNumber(), this.response.getToken(), Util.QUANTIDADE_GRANDE);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void atualizaResultado(ResponseConsulta response) {
		this.response = response;
		this.exibiExtrato();		
	}

}
