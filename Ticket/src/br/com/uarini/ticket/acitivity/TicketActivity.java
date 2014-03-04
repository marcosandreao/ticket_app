package br.com.uarini.ticket.acitivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import br.com.uarini.ticket.R;
import br.com.uarini.ticket.acitivity.listener.IExtratoListener;
import br.com.uarini.ticket.fragment.CardListFragment;
import br.com.uarini.ticket.fragment.ExtratoFragment;
import br.com.uarini.ticket.fragment.ResultFragment;
import br.com.uarini.ticket.json.ResponseConsulta;
import br.com.uarini.ticket.service.ConsultaSaldoService;
import br.com.uarini.ticket.service.ExtratoService;
import br.com.uarini.ticket.service.TicketService;

/**
 * @author marcos
 * 
 */
public class TicketActivity extends ActionBarActivity implements
		OnQueryTextListener, IExtratoListener {

	public static final String KEY_RESULT = "result";

	private int produtoAtual;

	private DadosConsulta receiver;

	private ProgressDialog pDialog;

	/**
	 * ultimo token consultado.
	 */
	private String token;
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v7.app.ActionBarActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);

		this.setupViewCards();

		this.receiver = new DadosConsulta();
		final IntentFilter filter = new IntentFilter();
		filter.addAction(ConsultaSaldoService.ACTION_DADOS);
		LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver,
				filter);

		this.getSupportActionBar().setHomeButtonEnabled(false);
	}

	private void setupViewCards() {
		Fragment frag = this.getSupportFragmentManager().findFragmentById(
				R.id.fl_container);
		if (frag == null) {
			frag = new CardListFragment();
			final FragmentTransaction fTrans = this.getSupportFragmentManager()
					.beginTransaction();
			fTrans.add(R.id.fl_container, frag);
			fTrans.commit();
		}
	}
	
	private void setupExtratoView(ResponseConsulta response) {
		response.setToken(this.token);

		final Fragment fragCurrnt = this.getSupportFragmentManager().findFragmentById(R.id.fl_container);
		
		if ( fragCurrnt != null && this.isExtratoFragment(fragCurrnt) ) {
			final ExtratoFragment extrantoFrag = (ExtratoFragment) fragCurrnt;
			extrantoFrag.atualizaResultado( response );
		} else {
			final Bundle mBundle = new Bundle();
			mBundle.putSerializable(KEY_RESULT, response);
			Fragment frag = new ExtratoFragment();
			frag.setArguments(mBundle);
			
			final FragmentTransaction fTrans = this.getSupportFragmentManager().beginTransaction();
			fTrans.replace(R.id.fl_container, frag);
			fTrans.addToBackStack(null);
			fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			fTrans.commit();
		}

	}

	private void setupResultView(ResponseConsulta response) {
		
		final Bundle mBundle = new Bundle();
		mBundle.putSerializable(KEY_RESULT, response);
		Fragment frag = new ResultFragment();
		frag.setArguments(mBundle);

		final FragmentTransaction fTrans = this.getSupportFragmentManager()
				.beginTransaction();
		fTrans.replace(R.id.fl_container, frag);
		fTrans.addToBackStack(null);
		fTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		fTrans.commit();
		
		this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (this.isFinishing()) {
			LocalBroadcastManager.getInstance(this)
					.unregisterReceiver(receiver);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if ( item.getItemId() == android.R.id.home ){
			final Fragment frag = this.getSupportFragmentManager().findFragmentById(
					R.id.fl_container);
			if (frag != null && (this.isResultFragment(frag) || this.isExtratoFragment(frag) ) ) {
				this.onBackPressed();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	private boolean isResultFragment(Fragment frag) {
		return frag instanceof ResultFragment;
	}
	
	private boolean isResultFragment() {
		final Fragment frag = this.getSupportFragmentManager().findFragmentById(R.id.fl_container);
		return frag != null && this.isResultFragment( frag );
	}
	
	private boolean isExtratoFragment(Fragment frag) {
		return frag instanceof ExtratoFragment;
	}

	@Override
	public void onBackPressed() {
		if ( this.isResultFragment() ) {
			this.getSupportActionBar().setHomeButtonEnabled(false);
			this.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		}
		super.onBackPressed();
	}

	private void handlerResponse(Intent intent) {
		this.pDialog.dismiss();
		if (intent.getBooleanExtra(ConsultaSaldoService.KEY_OK, false)) {
			final ResponseConsulta response = (ResponseConsulta) intent
					.getSerializableExtra(ConsultaSaldoService.KEY_DADOS);
			if (response.isStatus()) {
				if ( intent.getBooleanExtra(TicketService.KEY_IS_CONSULTA_SALDO, true) ) {
					this.setupResultView(response);
				} else {
					this.setupExtratoView(response);
				}
			} else {
				this.showAlert(response.getMessageError());
			}
		} else {
			this.showAlert(R.string.error_durante_a_consulta);
		}
	}

	private void showAlert(int msg) {
		showAlert(getString(msg));
	}

	private void showAlert(String msg) {
		new AlertDialog.Builder(this).setTitle(R.string.aviso).setMessage(msg)
				.setPositiveButton(android.R.string.ok, null).create().show();
	}

	private final class DadosConsulta extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			handlerResponse(intent);
		}
	}

	@Override
	public boolean onQueryTextChange(String arg0) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// this.esconderResultado();
		this.fecharTeclado();
		this.pDialog = ProgressDialog.show(this,
				getString(R.string.consultando), getString(R.string.aguarde_));
		final Intent intent = new Intent(this, ConsultaSaldoService.class);
		intent.putExtra(ConsultaSaldoService.KEY_NUMERO_CARTAO, query);
		intent.putExtra(ConsultaSaldoService.KEY_TIPO, produtoAtual);
		this.startService(intent);
		return false;
	}

	private void fecharTeclado(View mView) {
		if (mView != null && mView.getWindowToken() != null) {
			InputMethodManager inputManager = (InputMethodManager) this
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(mView.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private void fecharTeclado() {
		Fragment frag = this.getSupportFragmentManager().findFragmentById(
				R.id.fl_container);
		if (frag != null && frag instanceof CardListFragment) {
			final CardListFragment cardListFragment = (CardListFragment) frag;
			final SearchView mSearchView = cardListFragment.getSearchView();
			mSearchView.clearFocus();
			this.fecharTeclado(mSearchView);
		}
	}


	@Override
	public void consultaExtrato(String cartao, String token, int limit) {
		this.token = token;
		this.pDialog = ProgressDialog.show(this, this.getString(R.string.consultando), getString(R.string.aguarde_));
		final Intent intent = new Intent(this, ExtratoService.class);
		intent.putExtra(ExtratoService.KEY_NUMERO_CARTAO, cartao);
		intent.putExtra(ExtratoService.KEY_TOKEN, token);
		intent.putExtra(ExtratoService.KEY_NUM_LINHAS, limit);
		this.startService(intent);
	}

}
