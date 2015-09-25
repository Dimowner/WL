package ua.com.sofon.workoutlogger.database;

import java.util.ArrayList;
import java.sql.SQLException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Base class to communicate with some table T in database.
 * @author Dimowner
 */
public abstract class DataSource<T> {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public DataSource (Context context, String tableName) {
		dbHelper = new SQLiteHelper(context);
		this.context = context;
		this.tableName = tableName;
	}

	/**
	 * Open connection to SQLite database.
	 */
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * Open connection to SQLite database.
	 * @param db Opened connection to database.
	 */
	public void open(SQLiteDatabase db) {
		this.db = db;
	}

	/**
	 * Close connection to SQLite database.
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * Set source table name to will work with.
	 * @param tableName source table name.
	 */
	protected void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Get source table name.
	 * @return Source table name.
	 */
	protected String getTableName() {
		return tableName;
	}

	/**
	 * Insert new item into database for table T.
	 * @param item Item that will be inserted ind database.
	 */
	public T insertItem(T item) {
		ContentValues values = itemToContentValues(item);
		if (values != null) {
			long insertId = db.insert(tableName, null, values);
			LOGD(LOG_TAG, "Insert into " + tableName + " id = " + insertId);
			return getItem(insertId);
		} else {
			LOGE(LOG_TAG, "Unable to write empty item!");
			return null;
		}
	}

	/**
	 * Convert item into {@link android.content.ContentValues ContentValues}
	 * @param item Item to convert
	 * @return Converted item into {@link android.content.ContentValues ContentValues}.
	 */
	public abstract ContentValues itemToContentValues(T item);

	/**
	 * Delete item from database for table T.
	 * @param id Item id of element that will be deleted from table T.
	 */
	public void deleteItem(long id) {
		LOGD(LOG_TAG, tableName + " deleted ID = " + id);
		db.delete(tableName, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Update item in databese for table T.
	 * @param item Item that will be updated.
	 */
	public void updateItem(T item) {
		ContentValues values = itemToContentValues(item);
		if (values != null && values.containsKey(SQLiteHelper.COLUMN_ID)) {
			String where = SQLiteHelper.COLUMN_ID + " = "
					+ values.get(SQLiteHelper.COLUMN_ID);
			int n = db.update(tableName, values, where, null);
			LOGD(LOG_TAG, "Updated records count = " + n);
		} else {
			LOGD(LOG_TAG, "Unable to update empty item!");
		}
	}

	/**
	 * Get all records from database for table T.
	 * @return List that contains all records of table T.
	 */
	public ArrayList<T> getAll() {
		Cursor cursor = queryLocal(db, "SELECT * FROM " + tableName);
		return convertCursor(cursor);
	}

	/**
	 * Get items that match the conditions from table T.
	 * @param where Conditions to select some items.
	 * @return List of some records from table T.
	 */
	public ArrayList<T> getItems(String where) {
		Cursor cursor = queryLocal(db, "SELECT * FROM "
				+ tableName + " WHERE " + where);
		return convertCursor(cursor);
	}

	/**
	 * Get item from table T.
	 * @param id Item id to select.
	 * @return Selected item from table.
	 */
	public T getItem(long id) {
		Cursor cursor = queryLocal(db, "SELECT * FROM " + tableName
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + id);
		return convertCursor(cursor).get(0);
	}

	/**
	 * Convert {@link android.database.Cursor Cursor} into item T
	 * @param cursor Cursor.
	 * @return T item which corresponds some table in database.
	 */
	public ArrayList<T> convertCursor(Cursor cursor) {
		ArrayList<T> items = new ArrayList<>();
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			items.add(recordToItem(cursor));
			cursor.moveToNext();
		}
		cursor.close();
		if (items.size() > 0) {
			return items;
		}
		return new ArrayList<>();
	}

	/**
	 * Convert one record of {@link android.database.Cursor Cursor} into item T
	 * @param cursor Cursor positioned to item need to convert.
	 * @return T item which corresponds some table in database.
	 */
	public abstract T recordToItem(Cursor cursor);

	/**
	 * Query to local SQLite database with write to log query text and query result.
	 * @param db Provides access to database.
	 * @param query Query string.
	 * @return Cursor that contains query result.
	 */
	protected Cursor queryLocal(SQLiteDatabase db, String query) {
		LOGD(LOG_TAG, "queryLocal: " + query);
		Cursor c = db.rawQuery(query, null);
		StringBuilder data = new StringBuilder("Cursor[");
		if (c.moveToFirst()) {
			do {
				int columnCount = c.getColumnCount();
				data.append("row[");
				for (int i = 0; i < columnCount; ++i) {
					data.append(c.getColumnName(i)).append(" = ");

					switch (c.getType(i)) {
						case Cursor.FIELD_TYPE_BLOB:
							data.append("byte array");
							break;
						case Cursor.FIELD_TYPE_FLOAT:
							data.append(c.getFloat(i));
							break;
						case Cursor.FIELD_TYPE_INTEGER:
							data.append(c.getInt(i));
							break;
						case Cursor.FIELD_TYPE_NULL:
							data.append("null");
							break;
						case Cursor.FIELD_TYPE_STRING:
							data.append(c.getString(i));
							break;
					}
					if (i != columnCount - 1) {
						data.append(", ");
					}
				}
				data.append("]\n");
			} while (c.moveToNext());
		}
		data.append("]");
		LOGD(LOG_TAG, data.toString());
		return c;
	}

	/** Application context. */
	protected Context context;

	/** SQLite database manager. */
	protected SQLiteHelper dbHelper;

	/** Class provides access to database. */
	protected SQLiteDatabase db;

	/** Source table name. */
	protected String tableName;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
