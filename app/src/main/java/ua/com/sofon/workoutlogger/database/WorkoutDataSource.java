package ua.com.sofon.workoutlogger.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.parts.Workout;

/**
 * Class to communicate with database.
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
	 * Write {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise} to database.
	 * @param name Exercise name.
	 * @param description Exercise description.
	 */
	public Exercise createExercise(String name, String description) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_EXE_NAME, name);
		values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);
		long insertId = db.insert(SQLiteHelper.TABLE_EXERCISES, null, values);
		Log.v(LOG_TAG, "Insert exercise id = " + insertId);
		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_EXERCISES
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + insertId
		);
//		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
//				exercisesAllColumns, SQLiteHelper.COLUMN_ID + " = " + insertId, null,
//				null, null, null);
		cursor.moveToFirst();
		Exercise newExercise = cursorToExercise(cursor);
		cursor.close();
		return newExercise;
	}

	/**
	 * Write {@link ua.com.sofon.workoutlogger.parts.Workout Workout} to database.
	 * @param name Workout name.
	 */
	public Workout createWorkout(String name, Date date, float weight,
					int duration, String comment, int state, List<Exercise> exerciseList) {
//		TODO: ADD Join of exercises
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_WORKOUT_NAME, name);
		values.put(SQLiteHelper.COLUMN_DATE, date.getTime());
		values.put(SQLiteHelper.COLUMN_WEIGHT, weight);
		values.put(SQLiteHelper.COLUMN_DURATION, duration);
		values.put(SQLiteHelper.COLUMN_WORKOUT_STATE, state);
		values.put(SQLiteHelper.COLUMN_WORKOUT_COMMENT, comment);
		long workoutId = db.insert(SQLiteHelper.TABLE_WORKOUTS, null, values);
		Log.v(LOG_TAG, "Insert workout id = " + workoutId);
//		Cursor cursor = db.query(SQLiteHelper.TABLE_WORKOUTS,
//				workoutsAllColumns, SQLiteHelper.COLUMN_ID + " = " + workoutId, null,
//				null, null, null);
		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + workoutId
		);
		cursor.moveToFirst();
		Workout newWorkout = cursorToWorkout(cursor);


		int size = exerciseList.size();
		for (int i = 0; i < size; ++i) {
			writePerformedExercise(workoutId, exerciseList.get(i).getId());
		}

//		cursor = queryLocal(db,
//				"SELECT ex.* FROM " + SQLiteHelper.TABLE_PERFORMED_EXERCISES + " p"
//				+ " LEFT JOIN " + SQLiteHelper.TABLE_EXERCISES + " ex"
//				+ " ON p." + SQLiteHelper.COLUMN_EXERCISE_ID + " = ex." + SQLiteHelper.COLUMN_ID
//				+ " WHERE " + SQLiteHelper.COLUMN_WORKOUT_ID + " = " + workoutId
//		);
//
//		List<Exercise> exeList = new ArrayList<>();
//		if (cursor.moveToFirst()) {
//			do {
//				exeList.add(cursorToExercise(cursor));
//			} while (cursor.moveToNext());
//		}
//		newWorkout.setExerciseList(exeList);
		newWorkout.setExerciseList(readPerformedExercises(workoutId));
		cursor.close();
		return newWorkout;
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
		Log.v(LOG_TAG, "exeList size = " + exeList.size());
		return  exeList;
	}

	private void writePerformedExercise(long workoutID, long exerciseID) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_WORKOUT_ID, workoutID);
		values.put(SQLiteHelper.COLUMN_EXERCISE_ID, exerciseID);
		long insertId = db.insert(SQLiteHelper.TABLE_PERFORMED_EXERCISES, null, values);
		Log.v(LOG_TAG, "Insert workout id = " + insertId);
	}

	/**
	 * Update {@link ua.com.sofon.workoutlogger.parts.Workout Workout} in databese.
	 * @param workout Workout to update.
	 */
	public void updateWorkout(Workout workout) {
		if (workout.hasID()) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_WORKOUT_NAME, workout.getName());
			values.put(SQLiteHelper.COLUMN_DATE, workout.getDate().getTime());
			values.put(SQLiteHelper.COLUMN_WEIGHT, workout.getWeight());
			values.put(SQLiteHelper.COLUMN_DURATION, workout.getDuration());
			values.put(SQLiteHelper.COLUMN_WORKOUT_COMMENT, workout.getComment());
			values.put(SQLiteHelper.COLUMN_WORKOUT_STATE, workout.getState());

			String where = SQLiteHelper.COLUMN_ID + " = " + workout.getId();
			int n = db.update(SQLiteHelper.TABLE_WORKOUTS, values, where, null);
			Log.v(LOG_TAG, "Updated records count = " + n);
		} else {
			Log.e(LOG_TAG, "Workout has no ID");
		}
	}

//	private void updatePerformedExercises(long workoutID, List<Exercise> exeList) {
//
//	}

	/**
	 * Update {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise} in databese.
	 * @param exercise Exercise to update.
	 */
	public void updateExercise(Exercise exercise) {
		if (exercise.hasID()) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_EXE_NAME, exercise.getName());
			values.put(SQLiteHelper.COLUMN_DESCRIPTION, exercise.getDescription());
			String where = SQLiteHelper.COLUMN_ID + " = " + exercise.getId();
			int n = db.update(SQLiteHelper.TABLE_EXERCISES, values, where, null);
			Log.v(LOG_TAG, "Updated records count = " + n);
		} else {
			Log.e(LOG_TAG, "Exercice has no ID");
		}
	}

	/**
	 * Update {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise} in databese.
	 * @param id Exercise ID in database.
	 * @param name Exercise name.
	 * @param description Exercise description.
	 */
	public void updateExercise(long id, String name, String description) {
		if (id >= 0) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_EXE_NAME, name);
			values.put(SQLiteHelper.COLUMN_DESCRIPTION, description);
			String where = SQLiteHelper.COLUMN_ID + " = " + id;
			int n = db.update(SQLiteHelper.TABLE_EXERCISES, values, where, null);
			Log.v(LOG_TAG, "Updated records count = " + n);
		} else {
			Log.e(LOG_TAG, "ID not correct");
		}
	}

	/**
	 * Delete {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise} from database
	 * @param exercise Exercise to delete.
	 */
	public void deleteExersice(Exercise exercise) {
		long id = exercise.getId();
		Log.i(LOG_TAG, "Exercise deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_EXERCISES, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Delete {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise} from database
	 * @param workout Workout to delete.
	 */
	public void deleteWorkout(Workout workout) {
		long id = workout.getId();
		Log.i(LOG_TAG, "workout deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_WORKOUTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	/**
	 * Get all exercises records from database.
	 * @return List of {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise}
	 */
	public List<Exercise> getAllExercises() {
		List<Exercise> exercises = new ArrayList<>();
//		Cursor cursor = db.query(SQLiteHelper.TABLE_EXERCISES,
//				exercisesAllColumns, null, null, null, null, null);
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_EXERCISES);
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
	 * @return List of {@link ua.com.sofon.workoutlogger.parts.Workout Workouts}
	 */
	public List<Workout> getAllWorkouts() {
		List<Workout> workouts = new ArrayList<>();
		//TODO: MAKE QUERY
//		Cursor cursor = db.query(SQLiteHelper.TABLE_WORKOUTS,
//				workoutsAllColumns, null, null, null, null, null, null);
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_WORKOUTS);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Workout workout = cursorToWorkout(cursor);
			workout.setExerciseList(readPerformedExercises(workout.getId()));
			workouts.add(workout);
			cursor.moveToNext();
		}
		// make sure to close the cursor
		cursor.close();
		return workouts;
	}

	/**
	 * Convert {@link android.database.Cursor Cursor} to
	 * {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise}.
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
	 * {@link ua.com.sofon.workoutlogger.parts.Exercise Exercise}.
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

	/**
	 * Query to local SQLite database with write to log query text and query result.
	 * @param db Provides access to database.
	 * @param query Query string.
	 * @return Cursor that contains query result.
	 */
	private Cursor queryLocal(SQLiteDatabase db, String query) {
		Log.d(LOG_TAG, "queryLocal: " + query);
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
		Log.d(LOG_TAG, data.toString());
		return c;
	}

	/** SQLite database manager. */
	private SQLiteHelper dbHelper;

	/** Class provides access to database. */
	private SQLiteDatabase db;

//	/** Array contains all names of columns Exercises table. */
//	private String[] exercisesAllColumns = {
//			SQLiteHelper.COLUMN_ID,
//			SQLiteHelper.COLUMN_EXE_NAME,
//			SQLiteHelper.COLUMN_DESCRIPTION
//		};
//
//	/** Array contains all names of columns Exercises table. */
//	private String[] workoutsAllColumns = {
//			SQLiteHelper.COLUMN_ID,
//			SQLiteHelper.COLUMN_WORKOUT_NAME,
//			SQLiteHelper.COLUMN_DATE,
//			SQLiteHelper.COLUMN_WEIGHT,
//			SQLiteHelper.COLUMN_DURATION,
//			SQLiteHelper.COLUMN_WORKOUT_COMMENT
//		};

	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
