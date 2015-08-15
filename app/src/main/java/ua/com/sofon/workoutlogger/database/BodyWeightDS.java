package ua.com.sofon.workoutlogger.database;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import ua.com.sofon.workoutlogger.parts.BodyWeight;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

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
		super(context);
	}

	@Override
	public BodyWeight insertItem(BodyWeight item) {
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
			long insertId = db.insert(SQLiteHelper.TABLE_BODY_WEIGHTS, null, values);
			LOGD(LOG_TAG, "Insert into " + SQLiteHelper.TABLE_BODY_WEIGHTS + " id = " + insertId);
			Cursor cursor = queryLocal(db,
					"SELECT * FROM " + SQLiteHelper.TABLE_BODY_WEIGHTS
							+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + insertId
			);

			cursor.moveToFirst();
			BodyWeight newBW = convertCursor(cursor);
			cursor.close();
			return newBW;
		} else {
			LOGE(LOG_TAG, "Date or Time or Weight not correct");
			return null;
		}
	}

	@Override
	public void deleteItem(long id) {
		LOGD(LOG_TAG, "BodyWeight deleted ID = " + id);
		db.delete(SQLiteHelper.TABLE_BODY_WEIGHTS, SQLiteHelper.COLUMN_ID + " = " + id, null);
	}

	@Override
	public void updateItem(BodyWeight item) {
		if (item.hasID() && item.getDateTime() != null
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
			String where = SQLiteHelper.COLUMN_ID + " = " + item.getId();
			int n = db.update(SQLiteHelper.TABLE_BODY_WEIGHTS, values, where, null);
			LOGV(LOG_TAG, "Updated records count = " + n);
		} else {
			LOGE(LOG_TAG, "ID or Date or Time or Weight not correct");
		}
	}

	public List<BodyWeight> getAll() {
		List<BodyWeight> weights = new ArrayList<>();
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_BODY_WEIGHTS);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			BodyWeight bw = convertCursor(cursor);
			weights.add(bw);
			cursor.moveToNext();
		}
		cursor.close();
		return weights;
	}

	@Override
	public List<BodyWeight> getItems(String where) {
		List<BodyWeight> weights = new ArrayList<>();
		Cursor cursor = queryLocal(db,
				"SELECT * FROM " + SQLiteHelper.TABLE_BODY_WEIGHTS + " WHERE " + where);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			BodyWeight bw = convertCursor(cursor);
			weights.add(bw);
			cursor.moveToNext();
		}
		cursor.close();
		return weights;
	}

	@Override
	public BodyWeight getItem(long id) {
		Cursor cursor = queryLocal(db, "SELECT * FROM " + SQLiteHelper.TABLE_BODY_WEIGHTS
				+ " WHERE " + SQLiteHelper.COLUMN_ID + " = " + id);
		cursor.moveToFirst();
		return convertCursor(cursor);
	}

	@Override
	public BodyWeight convertCursor(Cursor cursor) {
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
		return new BodyWeight(cursor.getLong(0), date, weight, fatIndex, comment);
	}


	/** Tag for logging mesages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
