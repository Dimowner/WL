package ua.com.sofon.workoutlogger.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Created on 11.09.2015.
 * @author Dimowner
 */
public class TrainedExerciseDS extends DataSource<TrainedExercise> {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public TrainedExerciseDS(Context context) {
		super(context, SQLiteHelper.TABLE_TRAINED_EXERCISES);
	}

	@Override
	public ContentValues itemToContentValues(TrainedExercise item) {
		if (item.getName() != null && item.getWorkoutID() > 0) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_PARENT_EXE_ID, item.getParentExeID());
			values.put(SQLiteHelper.COLUMN_TE_NAME, item.getName());
			if (item.getDescription() != null && !item.getDescription().isEmpty()) {
				values.put(SQLiteHelper.COLUMN_TE_DESCRIPTION, item.getDescription());
			}
			values.put(SQLiteHelper.COLUMN_TE_TYPE, item.getType());
			values.put(SQLiteHelper.COLUMN_TE_NUMBER, item.getNumber());
			values.put(SQLiteHelper.COLUMN_WORKOUT_ID, item.getWorkoutID());
			values.put(SQLiteHelper.COLUMN_TW_ID, item.getTrainedWorkoutID());
			return values;
		} else {
			LOGE(LOG_TAG, "Can't convert TrainedExercise with empty Name or Workout ID!");
			return null;
		}
	}

	@Override
	public TrainedExercise recordToItem(Cursor cursor) {
		return new TrainedExercise(
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TE_NAME)),
				cursor.getString(cursor.getColumnIndex(SQLiteHelper.COLUMN_TE_DESCRIPTION)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_TE_TYPE)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_TE_NUMBER)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_TW_ID)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_WORKOUT_ID)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_PARENT_EXE_ID))
		);
	}


	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
