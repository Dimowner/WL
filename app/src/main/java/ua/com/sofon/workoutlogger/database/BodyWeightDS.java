package ua.com.sofon.workoutlogger.database;

import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.parts.BodyWeight;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Class to communicate with table
 * {@link ua.com.sofon.workoutlogger.database.SQLiteHelper#TABLE_BODY_WEIGHTS body_weights} in database.
 * @author Dimowner
 */
public class BodyWeightDS extends DataSource<BodyWeight> {

	/**
	 * Constructor.
	 * @param context Application context.
	 */
	public BodyWeightDS(Context context) {
		super(context, SQLiteHelper.TABLE_BODY_WEIGHTS);
	}

	@Override
	public ContentValues itemToContentValues(BodyWeight item) {
		if (item.getDateTime() != null
				&& item.getWeight() != BodyWeight.WEIGHT_NOT_SPECIFIED) {
			ContentValues values = new ContentValues();
			values.put(SQLiteHelper.COLUMN_WEIGHING_DATE_TIME, item.getDateTime().getTime());
			values.put(SQLiteHelper.COLUMN_WEIGHT, item.getWeight());
			if (item.getFatIndex() != BodyWeight.FAT_INDEX_NOT_SPECIFIED) {
				values.put(SQLiteHelper.COLUMN_FAT_INDEX, item.getFatIndex());
			}
			if (!item.getComment().isEmpty()) {
				values.put(SQLiteHelper.COLUMN_WEIGHT_COMMENT, item.getComment());
			}
			return values;
		} else {
			LOGE(LOG_TAG, "Date or Time or Weight not correct");
			return null;
		}
	}

	@Override
	public BodyWeight recordToItem(Cursor cursor) {
		Date date = null;
		if (!cursor.isNull(1)) {
			date = new Date(cursor.getLong(1));
		}
		float weight = BodyWeight.WEIGHT_NOT_SPECIFIED;
		if (!cursor.isNull(2)) {
			weight = cursor.getFloat(2);
		}
		float fatIndex = BodyWeight.FAT_INDEX_NOT_SPECIFIED;
		if (!cursor.isNull(3)) {
			fatIndex = cursor.getFloat(3);
		}
		String comment = "";
		if (!cursor.isNull(4)) {
			comment = cursor.getString(4);
		}
		return new BodyWeight(cursor.getInt(0), date, weight, fatIndex, comment);
	}


	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
