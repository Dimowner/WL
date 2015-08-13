package ua.com.sofon.workoutlogger.database;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_EXERCISES exercises} in database.
 * @author Dimowner
 */
public class ExerciseDS extends DataSource<Exercise> {

	public ExerciseDS(Context context) {
		super(context);
	}

	@Override
	public Exercise insertItem(Exercise item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXE_NAME, item.getName());
		values.put(SQLiteHelper.COLUMN_DESCRIPTION, item.getDescription());
		long insertId = db.insert(SQLiteHelper.TABLE_EXERCISES, null, values);
		LOGD(LOG_TAG, "Insert into " + SQLiteHelper.TABLE_EXERCISES + " id = " + insertId);
		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_EXERCISES
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + insertId
		);

		cursor.moveToFirst();
		Exercise newExercise = convertCursor(cursor);
		cursor.close();
		return newExercise;
	}

	@Override
	public void deleteItem(long id) {
		LOGD(LOG_TAG, "Exercise deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_EXERCISES, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	@Override
	public void updateItem(Exercise item) {
		if (item.hasID()) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_EXE_NAME, item.getName());
			values.put(SQLiteHelper.COLUMN_DESCRIPTION, item.getDescription());
			String where = SQLiteHelper.COLUMN_ID + " = " + item.getId();
			int n = db.update(SQLiteHelper.TABLE_EXERCISES, values, where, null);
			LOGD(LOG_TAG, "Updated records count = " + n);
		} else {
			LOGE(LOG_TAG, "Exercise has no ID");
		}
	}

	@Override
	public List<Exercise> getAll() {
		List<Exercise> exercises = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_EXERCISES);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Exercise exercise = convertCursor(cursor);
			exercises.add(exercise);
			cursor.moveToNext();
		}
		cursor.close();
		return exercises;
	}

	@Override
	public List<Exercise> getItems(String where) {
		List<Exercise> exercises = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM "
				+ SQLiteHelper.TABLE_EXERCISES + " WHERE " + where);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Exercise exercise = convertCursor(cursor);
			exercises.add(exercise);
			cursor.moveToNext();
		}
		cursor.close();
		return exercises;
	}

	@Override
	public Exercise convertCursor(Cursor cursor) {
		return new Exercise(
				cursor.getLong(0),
				cursor.getString(1),
				cursor.getString(2)
		);
	}


	/** Tag for logging mesages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
