package br.com.uarini.ticket.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import br.com.uarini.ticket.R;
import br.com.uarini.ticket.db.TicketDatabaseHelper;
import br.com.uarini.ticket.db.TicketProvider;
import br.com.uarini.ticket.util.Util;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class CardListFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;

	private OnQueryTextListener listener;

	private SearchView mSearchView;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat(
			"dd/MM/yyy HH:mm");

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.listener = (OnQueryTextListener) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.ListFragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		this.getListView().setEmptyView(view.findViewById(R.id.tv_empty));
		this.setHasOptionsMenu(true);
		this.mAdapter = new SimpleCursorAdapter(this.getActivity(),
				R.layout.item_card, null, new String[] {
						TicketDatabaseHelper.TABLE_CARTAO_COLS[1],
						TicketDatabaseHelper.TABLE_CARTAO_COLS[3],
						TicketDatabaseHelper.TABLE_CARTAO_COLS[2]}, new int[] {
						android.R.id.text1, android.R.id.text2 }, 0);
		this.mAdapter.setViewBinder(new ViewBinder() {

			@Override
			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				if (view.getId() == android.R.id.text2) {
					final Date dt = new Date(cursor.getLong(columnIndex));
					final TextView tv = (TextView) view;
					tv.setText(dateFormat.format(dt));
				} else {
					final TextView tv = (TextView) view;
					tv.setText(Util.formatCardNumber( getResources(), cursor.getInt(2), cursor.getString(columnIndex)));
				}
				return true;
			}
		});
		this.setListAdapter(this.mAdapter);
		this.getLoaderManager().initLoader(0, null, this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_main, menu);
		this.mSearchView = (SearchView) MenuItemCompat.getActionView(menu
				.findItem(R.id.action_search));
		this.mSearchView.setIconifiedByDefault(false);
		this.mSearchView.setOnQueryTextListener((OnQueryTextListener) this
				.getActivity());
		this.mSearchView.setInputType(InputType.TYPE_CLASS_NUMBER);
		this.mSearchView.setQueryHint(getString(R.string.hist_searchview));
		this.mSearchView.clearFocus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.ListFragment#onListItemClick(android.widget.ListView
	 * , android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		final Cursor mCursor = (Cursor) this.mAdapter.getItem(position);
		mCursor.moveToPosition(position);
		this.listener
				.onQueryTextSubmit(mCursor.getString(mCursor
						.getColumnIndexOrThrow(TicketDatabaseHelper.TABLE_CARTAO_COLS[1])));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyOptionsMenu()
	 */
	@Override
	public void onDestroyOptionsMenu() {
		if (this.mSearchView != null) {
			this.mSearchView.clearFocus();
		}
		super.onDestroyOptionsMenu();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int,
	 * android.os.Bundle)
	 */
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		return new CursorLoader(getActivity(),
				TicketProvider.CONTENT_URI_BUILD, null, null, null, TicketDatabaseHelper.TABLE_CARTAO_COLS[3] + " desc");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoadFinished(android
	 * .support.v4.content.Loader, java.lang.Object)
	 */
	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.mAdapter.changeCursor(cursor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.LoaderManager.LoaderCallbacks#onLoaderReset(android
	 * .support.v4.content.Loader)
	 */
	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		this.mAdapter.changeCursor(null);
	}

	public SearchView getSearchView() {
		return this.mSearchView;
	}

}
