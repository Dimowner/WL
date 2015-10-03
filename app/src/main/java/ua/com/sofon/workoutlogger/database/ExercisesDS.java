package ua.com.sofon.workoutlogger.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_EXERCISES exercises} in database.
 * @author Dimowner
 */
public class ExercisesDS extends DataSource<Exercise> {

	public ExercisesDS(Context context) {
		super(context, SQLiteHelper.TABLE_EXERCISES);
	}

	@Override
	public ContentValues itemToContentValues(Exercise item) {
		if (item.getName() != null) {
			ContentValues values = new ContentValues();
			if (item.getId() != Exercise.NO_ID) {
				values.put(SQLiteHelper.COLUMN_ID, item.getId());
			}
			values.put(SQLiteHelper.COLUMN_EXE_NAME, item.getName());
			values.put(SQLiteHelper.COLUMN_EXE_DESCRIPTION, item.getDescription());
			values.put(SQLiteHelper.COLUMN_EXE_TYPE, item.getType());
			return values;
		} else {
			LOGE(LOG_TAG, "Can't convert Exercise with empty Name!");
			return null;
		}
	}

	@Override
	public Exercise recordToItem(Cursor cursor) {
		return new Exercise(
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_EXE_NAME)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_EXE_DESCRIPTION)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_EXE_TYPE))
		);
	}


	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
