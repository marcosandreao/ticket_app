package br.com.uarini.ticket.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

/**
 * @author marcosandreao@gmail.com
 * 
 */
public class TicketProvider extends ContentProvider {

	private static final String AUTHORITY = "br.com.uarini.tickety.TicketProvider";

	private static final int TABLE = 10;

	private static final int TABLE_ID = 20;

	public static final Uri CONTENT_URI_BUILD = Uri
			.parse("content://" + TicketProvider.AUTHORITY + "/"
					+ TicketDatabaseHelper.TABLE_CARTAO);

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		TicketProvider.sURIMatcher.addURI(TicketProvider.AUTHORITY,
				TicketDatabaseHelper.TABLE_CARTAO, TicketProvider.TABLE);
		TicketProvider.sURIMatcher.addURI(TicketProvider.AUTHORITY,
				TicketDatabaseHelper.TABLE_CARTAO + "/#",
				TicketProvider.TABLE_ID);
	}

	private TicketDatabaseHelper helper;

	@Override
	public boolean onCreate() {
		this.helper = new TicketDatabaseHelper(this.getContext());
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = TicketProvider.sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = this.helper.getWritableDatabase();
		String table = null;
		long id = 0;
		switch (uriType) {
		case TABLE:
			table = TicketDatabaseHelper.TABLE_CARTAO;
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		id = sqlDB.insert(table, null, values);
		this.getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(table + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = TicketProvider.sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = this.helper.getWritableDatabase();
		int rowsDeleted = 0;
		String id = null;
		switch (uriType) {
		case TABLE:
			rowsDeleted = sqlDB.delete(TicketDatabaseHelper.TABLE_CARTAO,
					selection, selectionArgs);
			break;
		case TABLE_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(TicketDatabaseHelper.TABLE_CARTAO,
						BaseColumns._ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(TicketDatabaseHelper.TABLE_CARTAO,
						BaseColumns._ID + "=" + id + " and " + selection,
						selectionArgs);
			}
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int uriType = TicketProvider.sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = this.helper.getWritableDatabase();
		int rowsUpdated = 0;
		String id = null;
		switch (uriType) {
		case TABLE:
			rowsUpdated = sqlDB.update(TicketDatabaseHelper.TABLE_CARTAO,
					values, selection, selectionArgs);
			break;
		case TABLE_ID:
			id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TicketDatabaseHelper.TABLE_CARTAO,
						values, BaseColumns._ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(TicketDatabaseHelper.TABLE_CARTAO,
						values, BaseColumns._ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		this.getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		int uriType = TicketProvider.sURIMatcher.match(uri);
		switch (uriType) {
		case TABLE:
			queryBuilder.setTables(TicketDatabaseHelper.TABLE_CARTAO);
			break;
		case TABLE_ID:
			queryBuilder.setTables(TicketDatabaseHelper.TABLE_CARTAO);
			// adding the ID to the original query
			queryBuilder.appendWhere(BaseColumns._ID + "="
					+ uri.getLastPathSegment());
			break;

		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		final SQLiteDatabase db = this.helper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// make sure that potential listeners are getting notified
		cursor.setNotificationUri(this.getContext().getContentResolver(), uri);

		return cursor;
	}

}
