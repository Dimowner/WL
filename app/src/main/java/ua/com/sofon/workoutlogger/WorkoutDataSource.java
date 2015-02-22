package ua.com.sofon.workoutlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 21.02.2015.
 * @author Dimowner
 */
public class WorkoutDataSource {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public WorkoutDataSource (Context context) {
		dbHelper = new SQLiteHelper(context);
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
	 * Write {@link ua.com.sofon.workoutlogger.Exercise Exercise} to database.
	 * @param name Exercisce name.
	 * @param description Exercise description.
	 */
	public Exercise createExersice(String name, String description) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXE_NAME, name);
		values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);
		long insertId = db.insert(SQLiteHelper.TABLE_EXERCISES, null,
				values);
		Log.v(LOG_TAG, "Insert exercise id = " + insertId);
		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
				ExercisesAllColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Exercise newExersice = cursorToExercise(cursor);
		cursor.close();
		return newExersice;
	}

	/**
	 * Update {@link ua.com.sofon.workoutlogger.Exercise Exercise} in databese.
	 * @param exercise Exercise to update.
	 */
	public void updateExercise(Exercise exercise) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXE_NAME, exercise.getName());
		values.put(SQLiteHelper.COLUMN_DESCRIPTION, exercise.getDescription());
		String where = SQLiteHelper.COLUMN_ID + " = " + exercise.getId();
		int n = db.update(SQLiteHelper.TABLE_EXERCISES, values, where, null);
		Log.v(LOG_TAG, "Updated records count = " + n);
	}

	/**
	 * Update {@link ua.com.sofon.workoutlogger.Exercise Exercise} in databese.
	 * @param id Exercise ID in database.
	 * @param name Exercise name.
	 * @param description Exercise description.
	 */
	public void updateExercise(long id, String name, String description) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXE_NAME, name);
		values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);
		String where = SQLiteHelper.COLUMN_ID + " = " + id;
		int n = db.update(SQLiteHelper.TABLE_EXERCISES, values, where, null);
		Log.v(LOG_TAG, "Updated records count = " + n);
	}

	/**
	 * Delete {@link ua.com.sofon.workoutlogger.Exercise Exercise} from database
	 * @param exercise Exercise to delete.
	 */
	public void deleteExersice(Exercise exercise) {
		long id = exercise.getId();
		Log.i(LOG_TAG, "Exercise deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_EXERCISES, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Get all exercises records from database.
	 * @return List of {@link ua.com.sofon.workoutlogger.Exercise Exercise}
	 */
	public List<Exercise> getAllExercises() {
		List<Exercise> exercises = new ArrayList<>();
		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
				ExercisesAllColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Exercise exercise = cursorToExercise(cursor);
			exercises.add(exercise);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return exercises;
	}

	/**
	 * Get all exercises records from database.
	 * @return List of {@link ua.com.sofon.workoutlogger.Exercise Exercise}
	 */
	public List<Workout> getAllWorkouts() {
		List<Workout> workouts = new ArrayList<>();
		//TODO: MAKE QUERY
		Cursor cursor = db.query(SQLiteHelper.TABLE_WORKOUTS,
				null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Workout workout = cursorToWorkout(cursor);
			workouts.add(workout);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return workouts;
	}

	/**
	 * Convert {@link android.database.Cursor Cursor} to
	 * {@link ua.com.sofon.workoutlogger.Exercise Exercise}.
	 * @param cursor Cursor item.
	 * @return Exercise item.
	 */
	private Exercise cursorToExercise(Cursor cursor) {
		Exercise exercise = new Exercise();
		exercise.setId(cursor.getLong(0));
		exercise.setName(cursor.getString(1));
		exercise.setDescription(cursor.getString(2));
		return exercise;
	}

	/**
	 * Convert {@link android.database.Cursor Cursor} to
	 * {@link ua.com.sofon.workoutlogger.Exercise Exercise}.
	 * @param cursor Cursor item.
	 * @return Exercise item.
	 */
	private Workout cursorToWorkout(Cursor cursor) {
		Workout workout = new Workout();
		workout.setId(cursor.getLong(0));
//		workout.setName(cursor.getString(1));
//		workout.setDescription(cursor.getString(2));
		return workout;
	}

	/** SQLite database manager. */
	private SQLiteHelper dbHelper;

	/** Class provides access to database. */
	private SQLiteDatabase db;

	/** Array contains all names of columns Exercises table. */
	private String[] ExercisesAllColumns = {
			SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_EXE_NAME,
			SQLiteHelper.COLUMN_DESCRIPTION
		};

	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
