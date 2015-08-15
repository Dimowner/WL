package ua.com.sofon.workoutlogger.database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import ua.com.sofon.workoutlogger.parts.BodyWeight;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.parts.PlannedWorkout;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.util.LogUtils;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_PLANNED_WORKOUTS planned_workouts} in database.
 * @author Dimowner
 */
public class PlannedWorkoutsDS extends DataSource<PlannedWorkout>  {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public PlannedWorkoutsDS(Context context) {
		super(context);
	}

	@Override
	public PlannedWorkout insertItem(PlannedWorkout item) {
		ContentValues values = new ContentValues();
		values.put(SQLiteHelper.COLUMN_PLAN_DATE, item.getPlanDate().getTime());
		if (item.getPerformDate() != null) {
			values.put(SQLiteHelper.COLUMN_DESCRIPTION, item.getPerformDate().getTime());
		}
		if (item.getDuration() != PlannedWorkout.DURATION_NOT_SPECIFIED) {
			values.put(SQLiteHelper.COLUMN_PW_DURATION, item.getDuration());
		}
		values.put(SQLiteHelper.COLUMN_PW_STATE, item.getState());
		if (item.getWorkout() != null && item.getWorkout().hasID()) {
			values.put(SQLiteHelper.COLUMN_PW_WORKOUT_ID, item.getWorkout().getId());
		}
		long insertId = db.insert(SQLiteHelper.TABLE_PLANNED_WORKOUTS, null, values);
		LOGD(LOG_TAG, "Insert into " + SQLiteHelper.TABLE_PLANNED_WORKOUTS + " id = " + insertId);
		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_PLANNED_WORKOUTS
						+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + insertId
		);
		cursor.moveToFirst();
		PlannedWorkout pw = convertCursor(cursor);
		cursor.close();
		return pw;
	}

	@Override
	public void deleteItem(long id) {
		LOGD(LOG_TAG, SQLiteHelper.TABLE_PLANNED_WORKOUTS + " deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_PLANNED_WORKOUTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	@Override
	public void updateItem(PlannedWorkout item) {
		if (item.hasID()) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_PLAN_DATE, item.getPlanDate().getTime());
			if (item.getPerformDate() != null) {
				values.put(SQLiteHelper.COLUMN_DESCRIPTION, item.getPerformDate().getTime());
			}
			if (item.getDuration() != PlannedWorkout.DURATION_NOT_SPECIFIED) {
				values.put(SQLiteHelper.COLUMN_PW_DURATION, item.getDuration());
			}
			values.put(SQLiteHelper.COLUMN_PW_STATE, item.getState());
//			if (item.getWorkoutID() != PlannedWorkout.WORKOUT_ID_NOT_SPECIFIED) {
//				values.put(SQLiteHelper.COLUMN_PW_WORKOUT_ID, item.getWorkoutID());
//			}
			String where = SQLiteHelper.COLUMN_ID + " = " + item.getId();
			int n = db.update(SQLiteHelper.TABLE_PLANNED_WORKOUTS, values, where, null);
			LOGD(LOG_TAG, "Updated records count = " + n);
		} else {
			LOGE(LOG_TAG, "PlannedWorkout has no ID");
		}
	}

	@Override
	public List<PlannedWorkout> getAll() {
		List<PlannedWorkout> plannedWorkouts = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_PLANNED_WORKOUTS
				+ " ORDER BY " + SQLiteHelper.COLUMN_PERFORM_DATE + ", "
				+ SQLiteHelper.COLUMN_PLAN_DATE);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PlannedWorkout pw = convertCursor(cursor);
			pw.setWorkout(getWorkoutForID(cursor.getLong(
					cursor.getColumnIndex(SQLiteHelper.COLUMN_PW_WORKOUT_ID))));
			plannedWorkouts.add(pw);
			cursor.moveToNext();
		}
		cursor.close();
		return plannedWorkouts;
	}

	@Override
	public List<PlannedWorkout> getItems(String where) {
		List<PlannedWorkout> plannedWorkouts = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_PLANNED_WORKOUTS
				+ where
				+ " ORDER BY " + SQLiteHelper.COLUMN_PERFORM_DATE + ", "
				+ SQLiteHelper.COLUMN_PLAN_DATE);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			PlannedWorkout pw = convertCursor(cursor);
			pw.setWorkout(getWorkoutForID(cursor.getLong(
					cursor.getColumnIndex(SQLiteHelper.COLUMN_PW_WORKOUT_ID))));
			plannedWorkouts.add(pw);
			cursor.moveToNext();
		}
		cursor.close();
		return plannedWorkouts;
	}

	@Override
	public PlannedWorkout getItem(long id) {
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_PLANNED_WORKOUTS
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + id);
		cursor.moveToFirst();
		return convertCursor(cursor);
	}

	@Override
	public PlannedWorkout convertCursor(Cursor cursor) {
		Date performDate = null;
		if (!cursor.isNull(2)) {
			performDate = new Date(cursor.getLong(2));
		}
		int duration = PlannedWorkout.DURATION_NOT_SPECIFIED;
		if (!cursor.isNull(3)) {
			duration = cursor.getInt(3);
		}
		int state = PlannedWorkout.STATE_DEFAULT;
		if (!cursor.isNull(4)) {
			state = cursor.getInt(4);
		}
//		long workoutID = PlannedWorkout.WORKOUT_ID_NOT_SPECIFIED;
//		if (!cursor.isNull(5)) {
//			workoutID = cursor.getLong(5);
//		} else {
//			LOGE(LOG_TAG, "Failed read field " + SQLiteHelper.COLUMN_PW_WORKOUT_ID + " from cursor");
//		}
		return new PlannedWorkout(
			cursor.getLong(0), new Date(cursor.getLong(1)), performDate, duration, state, null);
	}

	private Workout getWorkoutForID(long id) {
//		WorkoutDataSource dataSource = new WorkoutDataSource(context);
		WorkoutsDS dataSource = new WorkoutsDS(context);
		try {
			dataSource.open();
		} catch (SQLException e) {
			LOGE(LOG_TAG, "", e);
		}
		Workout w = dataSource.getItem(id);
		dataSource.close();
		return w;
	}

	/** Tag for logging mesages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
