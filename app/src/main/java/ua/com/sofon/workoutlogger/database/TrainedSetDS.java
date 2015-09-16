package ua.com.sofon.workoutlogger.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.parts.TrainedSet;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Created on 11.09.2015.
 * @author Dimowner
 */
public class TrainedSetDS extends DataSource<TrainedSet> {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public TrainedSetDS(Context context) {
		super(context, SQLiteHelper.TABLE_TRAINED_SETS);
	}

	@Override
	public ContentValues itemToContentValues(TrainedSet item) {
		if (item.hasID() && item.getNumber() != 0) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_TE_ID, item.getTrainedExeID());
			values.put(SQLiteHelper.COLUMN_SET_NUMBER, item.getNumber());
			if (item.getWeight() > 0) {
				values.put(SQLiteHelper.COLUMN_SET_WEIGHT, item.getWeight());
			}
			if (item.getReps() > 0) {
				values.put(SQLiteHelper.COLUMN_SET_REPS, item.getReps());
			}
			values.put(SQLiteHelper.COLUMN_SET_STATE, item.getState());
			return values;
		} else {
			LOGE(LOG_TAG, "Unable to convert empty TrainedSet!");
			return null;
		}
	}

	@Override
	public TrainedSet recordToItem(Cursor cursor) {
		return new TrainedSet(
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_ID)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_SET_NUMBER)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_SET_REPS)),
				cursor.getFloat(cursor.getColumnIndex(SQLiteHelper.COLUMN_SET_WEIGHT)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_SET_STATE)),
				cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_TE_ID))
		);
	}


	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
