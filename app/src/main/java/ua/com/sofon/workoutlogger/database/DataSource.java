package ua.com.sofon.workoutlogger.database;

import java.util.List;
import java.sql.SQLException;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;

/**
 * Base class to communicate with some table T in database.
 * @author Dimowner
 */
public abstract class DataSource<T> {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public DataSource (Context context) {
		dbHelper = new SQLiteHelper(context);
		this.context = context;
	}

	/**
	 * Open connection to SQLite database.
	 */
	public void open() throws SQLException {
		db = dbHelper.getWritableDatabase();
	}

	/**
	 * Close connection to SQLite database.
	 */
	public void close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * Insert new item into database for table T.
	 * @param item Item that will be inserted ind database.
	 */
	public abstract T insertItem(T item);

	/**
	 * Delete item from database for table T.
	 * @param id Item id of element that will be deleted from table T.
	 */
	public abstract void deleteItem(long id);

	/**
	 * Update item in databese for table T.
	 * @param item Item that will be updated.
	 */
	public abstract void updateItem(T item);

	/**
	 * Get all records from database for table T.
	 * @return List that contains all records of table T.
	 */
	public abstract List<T> getAll();

	/**
	 * Get items that match the conditions from table T.
	 * @param where Conditions to select some items.
	 * @return List of some records from table T.
	 */
	public abstract List<T> getItems(String where);

	/**
	 * Get item from table T.
	 * @param id Item id to select.
	 * @return Selected item from table.
	 */
	public abstract T getItem(long id);

	/**
	 * Convert {@link android.database.Cursor Cursor} into item T
	 * @param cursor Cursor item.
	 * @return T item which corresponds some table in database.
	 */
	public abstract T convertCursor(Cursor cursor);

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
				data.append("\nrow[");
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
				data.append("]");
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

	/** Tag for logging mesages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
