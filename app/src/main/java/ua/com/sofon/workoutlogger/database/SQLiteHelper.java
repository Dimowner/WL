package ua.com.sofon.workoutlogger.database;

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
		db.execSQL(DATABASE_CREATE_BODY_WEIGHT_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_TRAINING_TABLE_SCRIPT);
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
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY_WEIGHT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINING);
		onCreate(db);
	}


	private static final String DATABASE_NAME = "workout_logger.db";
	private static final int DATABASE_VERSION = 9;

	//Tables names
	public static final String TABLE_EXERCISES = "exercises";
	public static final String TABLE_WORKOUTS = "workouts";
	public static final String TABLE_PERFORMED_EXERCISES = "performed_exercises";
	public static final String TABLE_SETS = "sets";
	public static final String TABLE_TRAINING = "training";
	public static final String TABLE_BODY_WEIGHT = "body_weight";

	//Common fields
	public static final String COLUMN_ID = "_id";

	//Fields for table
	public static final String COLUMN_TR_DATE = "date";
	public static final String COLUMN_TR_DURATION = "duration";
	public static final String COLUMN_TR_STATE = "state";
	public static final String COLUMN_TR_WORKOUT_ID = "workout_id";

	//Fields for table Exercises
	public static final String COLUMN_EXE_NAME = "exercise_name";
	public static final String COLUMN_DESCRIPTION = "exercise_description";

	//Fields for table Workouts
	public static final String COLUMN_W_NAME = "workout_name";
//	public static final String COLUMN_DATE = "workout_date";
//	public static final String COLUMN_DURATION = "workout_duration";
//	public static final String COLUMN_WEIGHT = "workout_weight";
	public static final String COLUMN_W_COMMENT = "workout_comment";
//	public static final String COLUMN_WORKOUT_STATE = "workout_state";

	//Fields for table Performed Exercises
	public static final String COLUMN_WORKOUT_ID = "workout_id";
	public static final String COLUMN_EXERCISE_ID = "exercise_id";

	//Fields for table Sets
	public static final String COLUMN_PERFORMED_EXE_ID = "performed_exe_id";
	public static final String COLUMN_SET_NUMBER = "set_number";
	public static final String COLUMN_SET_WEIGHT = "set_weight";
	public static final String COLUMN_SET_REPS = "set_reps";

	//Fields for table BodyWeight
	public static final String COLUMN_WEIGHING_DATE_TIME = "weighing_date_time";
	public static final String COLUMN_WEIGHT = "weight";
	public static final String COLUMN_FAT_INDEX = "fat_index";
	public static final String COLUMN_WEIGHT_COMMENT = "comment";

	//Create Exercises table sql statement
	private static final String DATABASE_CREATE_EXERCISES_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_EXERCISES + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_EXE_NAME + " TEXT NOT NULL, "
			+ COLUMN_DESCRIPTION + " TEXT);";

	//Create Workouts table sql statement
	private static final String DATABASE_CREATE_WORKOUTS_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_WORKOUTS + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_W_NAME + " TEXT, "
			+ COLUMN_W_COMMENT + " TEXT);";

	//TODO: Delete on cascade
	//Create Performed exercises table sql statement
	private static final String DATABASE_CREATE_PERFORMED_EXE_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_PERFORMED_EXERCISES + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_WORKOUT_ID + " LONG NOT NULL, "
			+ COLUMN_EXERCISE_ID + " LONG NOT NULL);";

	//TODO: Delete on cascade
	//Create Sets table sql statement
	private static final String DATABASE_CREATE_SETS_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_SETS + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_PERFORMED_EXE_ID + " LONG NOT NULL, "
			+ COLUMN_SET_NUMBER + " INTEGER, "
			+ COLUMN_SET_WEIGHT + " FLOAT, "
			+ COLUMN_SET_REPS + " INTEGER);";

	//Create History table sql statement
	private static final String DATABASE_CREATE_TRAINING_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_TRAINING + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_TR_DATE + " LONG NOT NULL, "
			+ COLUMN_TR_DURATION + " LONG NOT NULL DEFAULT 0, "
			+ COLUMN_TR_STATE + " INTEGER NOT NULL DEFAULT 0, "
			+ COLUMN_TR_WORKOUT_ID + " TEXT NOT NULL);";

	//Create BodyWeight table sql statement
	private static final String DATABASE_CREATE_BODY_WEIGHT_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_BODY_WEIGHT + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_WEIGHING_DATE_TIME + " LONG NOT NULL, "
			+ COLUMN_WEIGHT + " FLOAT NOT NULL, "
			+ COLUMN_FAT_INDEX + " FLOAT, "
			+ COLUMN_WEIGHT_COMMENT + " TEXT);";
}
