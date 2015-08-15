package ua.com.sofon.workoutlogger;

/**
 * Configuration constants of project.
 */
public class Config {
    // General configuration
    public static final boolean IS_DEBUG_BUILD = true;
    public static final boolean IS_LOGS_RECORDING_ENABLED = false;

    // shorthand for some units of time
    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS = 24 * HOUR_MILLIS;

    public static final String APP_NAME = "Workout Logger";
    public static final String API_KEY = "";

    // Play store URL prefix
    public static final String PLAY_STORE_URL_PREFIX
            = "https://play.google.com/store/apps/details?id=";

    public static final String APP_DIRECTORY = "WorkoutLogger";
    public static final String LOG_FILE_NAME = "Log.txt";
}
