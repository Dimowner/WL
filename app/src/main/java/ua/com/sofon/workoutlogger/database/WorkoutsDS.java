package ua.com.sofon.workoutlogger.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.parts.PlannedWorkout;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.util.LogUtils;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_WORKOUTS workouts} in database.
 * @author Dimowner
 */
public class WorkoutsDS extends DataSource<Workout> {

	public WorkoutsDS(Context context) {
		super(context);
	}

	@Override
	public Workout insertItem(Workout item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_W_NAME, item.getName());
		if (!item.getComment().isEmpty()) {
			values.put(SQLiteHelper.COLUMN_W_COMMENT, item.getComment());
		}
		long insertId = db.insert(SQLiteHelper.TABLE_WORKOUTS, null, values);
		LOGD(LOG_TAG, "Insert into " + SQLiteHelper.TABLE_WORKOUTS + " id = " + insertId);

		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + insertId
		);
		cursor.moveToFirst();
		Workout newWorkout = convertCursor(cursor);

		int size = item.getExercisesCount();
		for (int i = 0; i < size; ++i) {
			writePerformedExercise(item.getId(), item.getExerciseList().get(i).getId());
		}

		newWorkout.setExerciseList(readPerformedExercises(item.getId()));
		cursor.close();
		return newWorkout;
	}

	@Override
	public void deleteItem(long id) {
		LOGD(LOG_TAG, SQLiteHelper.TABLE_WORKOUTS + " deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_WORKOUTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	@Override
	public void updateItem(Workout item) {
		if (item.hasID()) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_W_NAME, item.getName());
			if (!item.getComment().isEmpty()) {
				values.put(SQLiteHelper.COLUMN_W_COMMENT, item.getComment());
			}
			String where = SQLiteHelper.COLUMN_ID + " = " + item.getId();
			int n = db.update(SQLiteHelper.TABLE_WORKOUTS, values, where, null);
			LOGV(LOG_TAG, "Updated records count = " + n);
		} else {
			LOGE(LOG_TAG, "Workout has no ID");
		}
	}

	@Override
	public List<Workout> getAll() {
		List<Workout> workouts = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Workout workout = convertCursor(cursor);
			workout.setExerciseList(readPerformedExercises(workout.getId()));
			workouts.add(workout);
			cursor.moveToNext();
		}
		cursor.close();
		return workouts;
	}

	@Override
	public List<Workout> getItems(String where) {
		List<Workout> workouts = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS
				+ " WHERE " + where);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Workout workout = convertCursor(cursor);
			workout.setExerciseList(readPerformedExercises(workout.getId()));
			workouts.add(workout);
			cursor.moveToNext();
		}
		cursor.close();
		return workouts;
	}

	@Override
	public Workout getItem(long id) {
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + id);
		cursor.moveToFirst();
		return convertCursor(cursor);
	}

	@Override
	public Workout convertCursor(Cursor cursor) {
		List<Exercise> exes = readPerformedExercises(cursor.getLong(0));
		return new Workout(
				cursor.getLong(0),
				cursor.getString(1),
				cursor.getString(2),
				exes
		);
	}

	private List<Exercise> readPerformedExercises(long workoutID) {
		Cursor cursor = queryLocal(db,
				"SELECT ex.* FROM " + SQLiteHelper.TABLE_PERFORMED_EXERCISES + " p"
				+ " LEFT JOIN " + SQLiteHelper.TABLE_EXERCISES + " ex"
				+ " ON p." + SQLiteHelper.COLUMN_EXERCISE_ID + " = ex." + SQLiteHelper.COLUMN_ID
				+ " WHERE " + SQLiteHelper.COLUMN_WORKOUT_ID + " = " + workoutID
		);

		List<Exercise> exeList = new ArrayList<>();
		if (cursor.moveToFirst()) {
			do {
				exeList.add(cursorToExercise(cursor));
			} while (cursor.moveToNext());
		}
		LOGV(LOG_TAG, "exeList size = " + exeList.size());
		return  exeList;
	}

	private Exercise cursorToExercise(Cursor cursor) {
		return new Exercise(
				cursor.getLong(0),
				cursor.getString(1),
				cursor.getString(2)
		);
	}

	private void writePerformedExercise(long workoutID, long exerciseID) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_WORKOUT_ID, workoutID);
		values.put(SQLiteHelper.COLUMN_EXERCISE_ID, exerciseID);
		long insertId = db.insert(SQLiteHelper.TABLE_PERFORMED_EXERCISES, null, values);
		LOGV(LOG_TAG, "Insert workout id = " + insertId);
	}


	/** Tag for logging mesages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
