package ua.com.sofon.workoutlogger.database;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.parts.TrainedWorkout;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_TRAINED_WORKOUTS planned_workouts} in database.
 * @author Dimowner
 */
public class TrainedWorkoutsDS extends DataSource<TrainedWorkout>  {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public TrainedWorkoutsDS(Context context) {
		super(context, SQLiteHelper.TABLE_TRAINED_WORKOUTS);
		exeData = new TrainedExerciseDS(context);
	}

	@Override
	public void open() throws SQLException {
		super.open();
		exeData.open(db);
	}

	@Override
	public TrainedWorkout insertItem(TrainedWorkout item) {
		TrainedWorkout w = super.insertItem(item);
		for (int i = 0; i < item.getExercisesCount(); i++) {
			w.addExercise(exeData.insertItem(item.getExercise(i)));
		}
		return w;
	}

	@Override
	public ContentValues itemToContentValues(TrainedWorkout item) {
		if (item.getName() != null && item.getPlanDate() != null) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_TW_NAME, item.getName());
			if (item.getDescription() != null) {
				values.put(SQLiteHelper.COLUMN_TW_DESCRIPTION, item.getDescription());
			}
			values.put(SQLiteHelper.COLUMN_TW_PLAN_DATE, item.getPlanDate().getTime());
			if (item.getPerformDate() != null) {
				values.put(SQLiteHelper.COLUMN_EXE_DESCRIPTION, item.getPerformDate().getTime());
			}
			if (item.getDuration() != TrainedWorkout.DURATION_NOT_SPECIFIED) {
				values.put(SQLiteHelper.COLUMN_TW_DURATION, item.getDuration());
			}
			values.put(SQLiteHelper.COLUMN_TW_STATE, item.getState());
			return values;
		} else {
			LOGE(LOG_TAG, "Can't convert TrainedWorkout with empty Name or PlanDate!");
			return null;
		}
	}

	@Override
	public void deleteItem(long id) {
		super.deleteItem(id);
		int c = db.delete(SQLiteHelper.TABLE_TRAINED_EXERCISES,
				SQLiteHelper.COLUMN_TW_ID + " = " + id, null);
		LOGD(LOG_TAG, "count deleted TrainedExercises = " + c);
	}

	@Override
	public void updateItem(TrainedWorkout item) {
		if (item.hasID()) {
			super.updateItem(item);
			for (int i = 0; i < item.getExercisesCount(); i++) {
				exeData.updateItem(item.getExercise(i));
			}
		} else {
			LOGE(LOG_TAG, "Can't update Workout with no ID");
		}
	}

	@Override
	public List<TrainedWorkout> getAll() {
		List<TrainedWorkout> w = super.getAll();
		for (int i = 0; i < w.size(); i++) {
			loadExercisesForWorkout(w.get(i));
		}
		return w;
	}

	@Override
	public List<TrainedWorkout> getItems(String where) {
		List<TrainedWorkout> w = super.getItems(where);
		for (int i = 0; i < w.size(); i++) {
			loadExercisesForWorkout(w.get(i));
		}
		return w;
	}

	@Override
	public TrainedWorkout getItem(long id) {
		return loadExercisesForWorkout(super.getItem(id));
	}

	private TrainedWorkout loadExercisesForWorkout(TrainedWorkout w) {
		w.setExerciseList(exeData.getItems(
				SQLiteHelper.COLUMN_TW_ID + " = " + w.getId())
		);
		return w;
	}

	@Override
	public TrainedWorkout recordToItem(Cursor cursor) {
		Date performDate = null;
		if (!cursor.isNull(2)) {
			performDate = new Date(cursor.getLong(2));
		}
		int duration = TrainedWorkout.DURATION_NOT_SPECIFIED;
		if (!cursor.isNull(3)) {
			duration = cursor.getInt(3);
		}
		int state = TrainedWorkout.STATE_DEFAULT;
		if (!cursor.isNull(4)) {
			state = cursor.getInt(4);
		}

		return new TrainedWorkout(
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TW_NAME)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TW_DESCRIPTION)),
				new Date(cursor.getLong(3)),
				performDate,
				duration,
				state,
				null);
	}


	/** Data source for loading
	 * {@link ua.com.sofon.workoutlogger.parts.TrainedExercise TrainedExercise}
	 * from local database. */
	private TrainedExerciseDS exeData;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
