package br.com.uarini.ticket.db;

import java.util.Date;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

public class CartaoBO {

	public void persiste(ContentResolver cResolver, String numeroCartao,
			int tipo) {
		ContentValues values = new ContentValues();
		values.put(TicketDatabaseHelper.TABLE_CARTAO_COLS[1], numeroCartao);
		values.put(TicketDatabaseHelper.TABLE_CARTAO_COLS[2], tipo);
		values.put(TicketDatabaseHelper.TABLE_CARTAO_COLS[3],
				new Date().getTime());

		final String selection = TicketDatabaseHelper.TABLE_CARTAO_COLS[1]
				+ " = ?";
		Cursor cursor = cResolver.query(TicketProvider.CONTENT_URI_BUILD,
				TicketDatabaseHelper.TABLE_CARTAO_COLS, selection,
				new String[] { numeroCartao }, null);
		if (cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
			values.put(BaseColumns._ID, id);
			cResolver.update(ContentUris.withAppendedId(
					TicketProvider.CONTENT_URI_BUILD, id), values, null, null);
			cursor.close();
		} else {
			cursor.close();
			cResolver.insert(TicketProvider.CONTENT_URI_BUILD, values);
		}

	}
	
	public void remove(ContentResolver cResolver, String numeroCartao) {
		final String selection = TicketDatabaseHelper.TABLE_CARTAO_COLS[1]	+ " = ?";
		Cursor cursor = cResolver.query(TicketProvider.CONTENT_URI_BUILD,
				TicketDatabaseHelper.TABLE_CARTAO_COLS, selection,
				new String[] { numeroCartao }, null);
		if (cursor.moveToNext()) {
			long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
			cursor.close();
			cResolver.delete(TicketProvider.CONTENT_URI_BUILD, BaseColumns._ID + " =? ", new String[] { String.valueOf(id) } );
		} 
	}

	public String getUltimoCartao(ContentResolver cResolver) {

		Cursor cursor = cResolver.query(TicketProvider.CONTENT_URI_BUILD,
				TicketDatabaseHelper.TABLE_CARTAO_COLS, null,
				null,
				TicketDatabaseHelper.TABLE_CARTAO_COLS[3] + " desc");
		try {
			if (cursor.moveToNext()) {
				return cursor
						.getString(cursor
								.getColumnIndex(TicketDatabaseHelper.TABLE_CARTAO_COLS[1]));
			}
		} finally {
			cursor.close();
		}

		return "";
	}
	
	public boolean hasCartao(ContentResolver cResolver, final String numCartao) {

		final String selection = TicketDatabaseHelper.TABLE_CARTAO_COLS[1]	+ " = ?";
		Cursor cursor = cResolver.query(TicketProvider.CONTENT_URI_BUILD,
				TicketDatabaseHelper.TABLE_CARTAO_COLS, selection,
				new String[] { numCartao }, null);
		try {
			return cursor != null && cursor.getCount() > 0;
		} finally {
			cursor.close();
		}
	}

}
