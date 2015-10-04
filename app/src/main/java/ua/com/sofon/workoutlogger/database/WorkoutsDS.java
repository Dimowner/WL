package ua.com.sofon.workoutlogger.database;

import java.util.ArrayList;
import java.sql.SQLException;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_WORKOUTS workouts} in database.
 * @author Dimowner
 */
public class WorkoutsDS extends DataSource<Workout> {

	public WorkoutsDS(Context context) {
		super(context, SQLiteHelper.TABLE_WORKOUTS);
		exeData = new TrainedExerciseDS(context);
	}

	@Override
	public void open() throws SQLException {
		super.open();
		exeData.open(db);
	}

	@Override
	public Workout insertItem(Workout item) {
		Workout w = super.insertItem(item);
		for (int i = 0; i < item.getExercisesCount(); i++) {
			item.getTrainedExe(i).setWorkoutID(w.getId());
			w.addTrainedExe(exeData.insertItem(item.getTrainedExe(i)));
		}
		return w;
	}

	@Override
	public ContentValues itemToContentValues(Workout item) {
		if (item.getName() != null) {
			ContentValues values = new ContentValues();
			if (item.getId() != Workout.NO_ID) {
				values.put(SQLiteHelper.COLUMN_ID, item.getId());
			}
			values.put(SQLiteHelper.COLUMN_W_NAME, item.getName());
			if (!item.getDescription().isEmpty()) {
				values.put(SQLiteHelper.COLUMN_W_DESCRIPTION, item.getDescription());
			}
			return values;
		} else {
			LOGE(LOG_TAG, "Can't convert Workout with empty Name!");
			return null;
		}
	}

	@Override
	public void deleteItem(int id) {
		super.deleteItem(id);
		int c = db.delete(SQLiteHelper.TABLE_TRAINED_EXERCISES,
				SQLiteHelper.COLUMN_WORKOUT_ID + " = " + id, null);
		LOGD(LOG_TAG, "count deleted TrainedExercises = " + c);
	}

	@Override
	public void updateItem(Workout item) {
		if (item.hasID()) {
			super.updateItem(item);

			ArrayList<TrainedExercise> exes = getTrainedExercisesForWorkout(item.getId());
			for (int i = 0; i < exes.size(); i++) {
				if (item.containsTrainedExe(exes.get(i).getId())) {
					item.removeTrainedExe(exes.get(i).getId());
				} else {
					exeData.deleteItem(exes.get(i).getId());
				}
			}
			for (int i = 0; i < item.getExercisesCount(); i++) {
				exeData.insertItem(item.getTrainedExe(i));
			}
		} else {
			LOGE(LOG_TAG, "Can't update Workout with no ID");
		}
	}

	@Override
	public ArrayList<Workout> getAll() {
		ArrayList<Workout> w = super.getAll();
		for (int i = 0; i < w.size(); i++) {
			loadExercisesForWorkout(w.get(i));
		}
		return w;
	}

	@Override
	public ArrayList<Workout> getItems(String where) {
		ArrayList<Workout> w = super.getItems(where);
		for (int i = 0; i < w.size(); i++) {
			loadExercisesForWorkout(w.get(i));
		}
		return w;
	}

	@Override
	public Workout getItem(int id) {
		return loadExercisesForWorkout(super.getItem(id));
	}

	private Workout loadExercisesForWorkout(Workout w) {
		w.setExerciseList(exeData.getItems(
				SQLiteHelper.COLUMN_WORKOUT_ID + " = " + w.getId())
		);
		return w;
	}

	public ArrayList<TrainedExercise> getTrainedExercisesForWorkout(int workoutID) {
		return exeData.getItems(SQLiteHelper.COLUMN_WORKOUT_ID + " = " + workoutID);
	}

	@Override
	public Workout recordToItem(Cursor cursor) {
		Workout w = new Workout(
				cursor.getInt(0),
				cursor.getString(1),
				cursor.getString(2),
				null
		);
		return loadExercisesForWorkout(w);
	}


	/** Data source for loading
	 * {@link ua.com.sofon.workoutlogger.parts.TrainedExercise TrainedExercise}
	 * from local database. */
	private TrainedExerciseDS exeData;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
