package ua.com.sofon.workoutlogger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
		long insertId = db.insert(SQLiteHelper.TABLE_EXERCISES, null, values);
		Log.v(LOG_TAG, "Insert exercise id = " + insertId);
		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
				exercisesAllColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Exercise newExersice = cursorToExercise(cursor);
		cursor.close();
		return newExersice;
	}

	/**
	 * Write {@link ua.com.sofon.workoutlogger.Workout Workout} to database.
	 * @param name Workout name.
	 */
	public Workout createWorkout(String name, Date date, float weight,
					int duration, String comment, List<Exercise> exerciseList) {
//		TODO: ADD Join of exercises
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_WORKOUT_NAME, name);
		values.put(SQLiteHelper.COLUMN_DATE, date.getTime());
		values.put(SQLiteHelper.COLUMN_WEIGHT, weight);
		values.put(SQLiteHelper.COLUMN_DURATION, duration);
		values.put(SQLiteHelper.COLUMN_WORKOUT_COMMENT, comment);
		long insertId = db.insert(SQLiteHelper.TABLE_WORKOUTS, null, values);
		Log.v(LOG_TAG, "Insert workout id = " + insertId);
		Cursor cursor = db.query(SQLiteHelper.TABLE_WORKOUTS,
				workoutsAllColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
				null, null, null);
		cursor.moveToFirst();
		Workout newWorkout = cursorToWorkout(cursor);
		cursor.close();
		return newWorkout;
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
	 * Delete {@link ua.com.sofon.workoutlogger.Exercise Exercise} from database
	 * @param workout Workout to delete.
	 */
	public void deleteWorkout(Workout workout) {
		long id = workout.getId();
		Log.i(LOG_TAG, "workout deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_WORKOUTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Get all exercises records from database.
	 * @return List of {@link ua.com.sofon.workoutlogger.Exercise Exercise}
	 */
	public List<Exercise> getAllExercises() {
		List<Exercise> exercises = new ArrayList<>();
		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
				exercisesAllColumns, null, null, null, null, null);
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
	 * Get all workout records from database.
	 * @return List of {@link ua.com.sofon.workoutlogger.Workout Workouts}
	 */
	public List<Workout> getAllWorkouts() {
		List<Workout> workouts = new ArrayList<>();
		//TODO: MAKE QUERY
		Cursor cursor = db.query(SQLiteHelper.TABLE_WORKOUTS,
				workoutsAllColumns, null, null, null, null, null, null);
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
		workout.setName(cursor.getString(1));
		workout.setDate(new Date(cursor.getLong(2)));
		workout.setWeight(cursor.getFloat(3));
		workout.setDuration(cursor.getInt(4));
		workout.setComment(cursor.getString(5));
//		TODO: READ EXERCISES
		return workout;
	}

	/** SQLite database manager. */
	private SQLiteHelper dbHelper;

	/** Class provides access to database. */
	private SQLiteDatabase db;

	/** Array contains all names of columns Exercises table. */
	private String[] exercisesAllColumns = {
			SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_EXE_NAME,
			SQLiteHelper.COLUMN_DESCRIPTION
		};

	/** Array contains all names of columns Exercises table. */
	private String[] workoutsAllColumns = {
			SQLiteHelper.COLUMN_ID,
			SQLiteHelper.COLUMN_WORKOUT_NAME,
			SQLiteHelper.COLUMN_DATE,
			SQLiteHelper.COLUMN_WEIGHT,
			SQLiteHelper.COLUMN_DURATION,
			SQLiteHelper.COLUMN_WORKOUT_COMMENT
		};

	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
