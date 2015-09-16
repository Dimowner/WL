package ua.com.sofon.workoutlogger.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;

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
		db.execSQL(DATABASE_CREATE_EXERCISES_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_WORKOUTS_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_TRAINED_WORKOUTS_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_TRAINED_EXERCISES_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_TRAINED_SETS_TABLE_SCRIPT);
		db.execSQL(DATABASE_CREATE_BODY_WEIGHT_TABLE_SCRIPT);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		LOGD(SQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINED_WORKOUTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINED_EXERCISES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINED_SETS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY_WEIGHTS);
		onCreate(db);
	}


	private static final String DATABASE_NAME = "workout_logger.db";
	private static final int DATABASE_VERSION = 13;

	//Tables names
	public static final String TABLE_EXERCISES = "exercises";
	public static final String TABLE_WORKOUTS = "workouts";
	public static final String TABLE_TRAINED_WORKOUTS = "trained_workouts";
	public static final String TABLE_TRAINED_EXERCISES = "trained_exercises";
	public static final String TABLE_TRAINED_SETS = "trained_sets";
	public static final String TABLE_BODY_WEIGHTS = "body_weights";

	//Common fields
	public static final String COLUMN_ID = "_id";

	//Fields for table Exercises
	public static final String COLUMN_EXE_NAME = "name";
	public static final String COLUMN_EXE_DESCRIPTION = "description";
	public static final String COLUMN_EXE_TYPE = "type";

	//Fields for table Workouts
	public static final String COLUMN_W_NAME = "name";
	public static final String COLUMN_W_DESCRIPTION = "description";

	//Fields for Trained Workouts table
	public static final String COLUMN_TW_NAME = "name";
	public static final String COLUMN_TW_DESCRIPTION = "description";
	public static final String COLUMN_TW_PLAN_DATE = "plan_date";
	public static final String COLUMN_TW_DATE = "perform_date";
	public static final String COLUMN_TW_DURATION = "duration";
	public static final String COLUMN_TW_STATE = "state";

	//Fields for table Trained Exercises
	public static final String COLUMN_PARENT_EXE_ID = "parent_exe_id";
	public static final String COLUMN_TE_NAME = "name";
	public static final String COLUMN_TE_DESCRIPTION = "description";
	public static final String COLUMN_TE_TYPE = "type";
	public static final String COLUMN_TE_NUMBER = "number";
	public static final String COLUMN_TW_ID = "trained_workout_id";
	public static final String COLUMN_WORKOUT_ID = "workout_id";

	//Fields for table Trained Sets
	public static final String COLUMN_SET_NUMBER = "number";
	public static final String COLUMN_SET_WEIGHT = "weight";
	public static final String COLUMN_SET_REPS = "reps";
	public static final String COLUMN_SET_STATE = "state";
	public static final String COLUMN_TE_ID = "trained_exe_id";

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
					+ COLUMN_EXE_DESCRIPTION + " TEXT, "
					+ COLUMN_EXE_TYPE + " INTEGER NOT NULL DEFAULT 0);";

	//Create Workouts table sql statement
	private static final String DATABASE_CREATE_WORKOUTS_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_WORKOUTS + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_W_NAME + " TEXT NOT NULL, "
					+ COLUMN_W_DESCRIPTION + " TEXT);";

	//Create Trained Workouts table sql statement
	private static final String DATABASE_CREATE_TRAINED_WORKOUTS_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_TRAINED_WORKOUTS + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_TW_NAME + " TEXT NOT NULL, "
					+ COLUMN_TW_DESCRIPTION + " TEXT, "
					+ COLUMN_TW_PLAN_DATE + " LONG NOT NULL, "
					+ COLUMN_TW_DATE + " LONG, "
					+ COLUMN_TW_DURATION + " LONG NOT NULL DEFAULT 0, "
					+ COLUMN_TW_STATE + " INTEGER NOT NULL DEFAULT 0);";

	//Create Trained Exercises table sql statement
	private static final String DATABASE_CREATE_TRAINED_EXERCISES_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_TRAINED_EXERCISES + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_PARENT_EXE_ID + " INTEGER NOT NULL DEFAULT 0, "
					+ COLUMN_TW_ID + " INTEGER, "
					+ COLUMN_WORKOUT_ID + " INTEGER, "
					+ COLUMN_TE_NAME + " TEXT NOT NULL, "
					+ COLUMN_TE_DESCRIPTION + " TEXT, "
					+ COLUMN_TE_TYPE + " INTEGER NOT NULL DEFAULT 0, "
					+ COLUMN_TE_NUMBER + " INTEGER NOT NULL DEFAULT 0);";

	//Create Trained Sets table sql statement
	private static final String DATABASE_CREATE_TRAINED_SETS_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_TRAINED_SETS + " ("
					+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ COLUMN_TE_ID + " INTEGER NOT NULL, "
					+ COLUMN_SET_NUMBER + " INTEGER NOT NULL, "
					+ COLUMN_SET_WEIGHT + " FLOAT NOT NULL DEFAULT 0, "
					+ COLUMN_SET_STATE + " INTEGER NOT NULL DEFAULT 0, "
					+ COLUMN_SET_REPS + " INTEGER NOT NULL DEFAULT 0);";

	//Create BodyWeight table sql statement
	private static final String DATABASE_CREATE_BODY_WEIGHT_TABLE_SCRIPT =
			"CREATE TABLE " + TABLE_BODY_WEIGHTS + " ("
			+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_WEIGHING_DATE_TIME + " LONG NOT NULL, "
			+ COLUMN_WEIGHT + " FLOAT NOT NULL, "
			+ COLUMN_FAT_INDEX + " FLOAT, "
			+ COLUMN_WEIGHT_COMMENT + " TEXT);";
}
