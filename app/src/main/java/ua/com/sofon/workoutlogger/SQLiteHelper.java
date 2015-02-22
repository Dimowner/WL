package ua.com.sofon.workoutlogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite database manager class. Created on 21.02.2015.
 * @author Dimowner
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("SQLiteHelper", "onCreate");
		db.execSQL(DATABASE_CREATE_EXERCISES_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_WORKOUTS_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_PERFORMED_EXE_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_SETS_TABLE_SCRIPT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERFORMED_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
		onCreate(db);
	}


	private static final String DATABASE_NAME = "workout_logger.db";
	private static final int DATABASE_VERSION = 2;

	//Tables names
	public static final String TABLE_EXERCISES = "exercises";
	public static final String TABLE_WORKOUTS = "workouts";
	public static final String TABLE_PERFORMED_EXERCISES = "performed_exercises";
	public static final String TABLE_SETS = "sets";

	//Common fields
	public static final String COLUMN_ID = "_id";

	//Fields for table Exercises
	public static final String COLUMN_EXE_NAME = "exercise_name";
	public static final String COLUMN_DESCRIPTION = "exercise_description";

	//Fields for table Workouts
	public static final String COLUMN_DATE = "workout_date";
	public static final String COLUMN_DURATION = "workout_duration";
	public static final String COLUMN_WEIGHT = "workout_weight";
	public static final String COLUMN_WORKOUT_COMMENT = "workout_comment";

	//Fields for table Performed Exercises
	public static final String COLUMN_WORKOUT_ID = "workout_id";
	public static final String COLUMN_EXERCISE_ID = "exercise_id";

	//Fields for table Sets
	public static final String COLUMN_PERFORMED_EXE_ID = "performed_exe_id";
	public static final String COLUMN_SET_NUMBER = "set_number";
	public static final String COLUMN_SET_WEIGHT = "set_weight";
	public static final String COLUMN_SET_REPS = "set_reps";

	//Create Exercises table sql statement
	private static final String DATABASE_CREATE_EXERCISES_TABLE_SCRIPT =
			"create table " + TABLE_EXERCISES + " ("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_EXE_NAME + " text not null, "
			+ COLUMN_DESCRIPTION + " text);";

	//Create Workouts table sql statement
	private static final String DATABASE_CREATE_WORKOUTS_TABLE_SCRIPT =
			"create table " + TABLE_WORKOUTS + " ("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_DATE + " long not null, "
			+ COLUMN_DURATION + " integer, "
			+ COLUMN_WEIGHT + " integer, "
			+ COLUMN_WORKOUT_COMMENT + " text);";

	//TODO: Delete on cascade
	//Create Performed exercises table sql statement
	private static final String DATABASE_CREATE_PERFORMED_EXE_TABLE_SCRIPT =
			"create table " + TABLE_PERFORMED_EXERCISES + " ("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_WORKOUT_ID + " long not null, "
			+ COLUMN_EXERCISE_ID + " long not null);";

	//TODO: Delete on cascade
	//Create Sets table sql statement
	private static final String DATABASE_CREATE_SETS_TABLE_SCRIPT =
			"create table " + TABLE_SETS + " ("
			+ COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_PERFORMED_EXE_ID + " long not null, "
			+ COLUMN_SET_NUMBER + " integer, "
			+ COLUMN_SET_WEIGHT + " float, "
			+ COLUMN_SET_REPS + " integer);";

}
