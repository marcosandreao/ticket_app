package br.com.uarini.ticket.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class TicketDatabaseHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "ticket.sqlite";

	private final static int DB_VERSION = 1;

	public static final String TABLE_CARTAO = "cartao";

	public static final String[] TABLE_CARTAO_COLS = { BaseColumns._ID,
			"numero", "tipo", "last_find" };

	private static final String SQL_TABLE_BUILD = String
			.format("create table %s ( %s integer primary key AUTOINCREMENT, %s text, %s integer, %s integer)",
					TicketDatabaseHelper.TABLE_CARTAO,
					TicketDatabaseHelper.TABLE_CARTAO_COLS[0],
					TicketDatabaseHelper.TABLE_CARTAO_COLS[1],
					TicketDatabaseHelper.TABLE_CARTAO_COLS[2],
					TicketDatabaseHelper.TABLE_CARTAO_COLS[3]);

	public TicketDatabaseHelper(Context context) {
		super(context, TicketDatabaseHelper.DB_NAME, null,
				TicketDatabaseHelper.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(TicketDatabaseHelper.SQL_TABLE_BUILD);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	}
}
