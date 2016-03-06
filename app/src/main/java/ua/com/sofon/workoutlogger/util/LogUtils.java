package ua.com.sofon.workoutlogger.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import ua.com.sofon.workoutlogger.Config;

/**
 * Общие функции для логирования информации приложения.
 * @author Dimowner
 */
public class LogUtils {

	/**
	 * Конструктор для предотвращения реализации и наследования.
	 */
	private LogUtils() {
	}

	/**
	 * Сформировать тег используемый при логировании информации.
	 * @param str тег для логирования.
	 */
	public static String makeLogTag(String str) {
		if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
			return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
		}

		return LOG_PREFIX + str;
	}

	public static void LOGD(final String tag, String message) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, message);
			if (isLogsRecordingEnabled) {
				log2file("D", tag, message);
			}
		}
	}

	public static void LOGD(final String tag, String message, Throwable cause) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (
//				  BuildConfig.DEBUG ||
				Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, message, cause);
			if (isLogsRecordingEnabled) {
				log2file("D", tag, message + ": " + StrUtil.stackTraceToString(cause));
			}
		}
	}

	public static void LOGV(final String tag, String message) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (
//				  BuildConfig.DEBUG &&
				Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, message);
			if (isLogsRecordingEnabled) {
				log2file("V", tag, message);
			}
		}
	}

	public static void LOGV(final String tag, String message, Throwable cause) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (
//				  BuildConfig.DEBUG &&
				Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, message, cause);
			if (isLogsRecordingEnabled) {
				log2file("V", tag, message + ": " + StrUtil.stackTraceToString(cause));
			}
		}
	}

	public static void LOGI(final String tag, String message) {
		Log.i(tag, message);
		if (isLogsRecordingEnabled) {
			log2file("I", tag, message);
		}
	}

	public static void LOGI(final String tag, String message, Throwable cause) {
		Log.i(tag, message, cause);
		if (isLogsRecordingEnabled) {
			log2file("I", tag, message + ": " + StrUtil.stackTraceToString(cause));
		}
	}

	public static void LOGW(final String tag, String message) {
		Log.w(tag, message);
		if (isLogsRecordingEnabled) {
			log2file("W", tag, message);
		}
	}

	public static void LOGW(final String tag, String message, Throwable cause) {
		Log.w(tag, message, cause);
		if (isLogsRecordingEnabled) {
			log2file("W", tag, message + ": " + StrUtil.stackTraceToString(cause));
		}
	}

	public static void LOGE(final String tag, String message) {
		Log.e(tag, message);
		if (isLogsRecordingEnabled) {
			log2file("E", tag, message);
		}
	}

	public static void LOGE(final String tag, String message, Throwable cause) {
		Log.e(tag, message, cause);
		if (isLogsRecordingEnabled) {
			log2file("E", tag, message + ": " + StrUtil.stackTraceToString(cause));
		}
	}

	/**
	 * Write log into file.
	 * @param logLevel Level of logging message.
	 * @param tag Logging message tag.
	 * @param content Useful information to save in log file.
	 */
	protected static void log2file(String logLevel, String tag, String content) {
		Log.i("LogUtils", "logToFile");
		File file = createLogFileFromPath();
		PrintWriter out = null;
		if (file != null) {
			try {
				int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
//				Log.v("LogUtils", "File size = " + file_size);
				if (file_size > LOG_FILE_SIZE) {
					removeStartLines(file, LOG_FILE_REMOVE_LINES_COUNT);
				}
				out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
				out.println(createLogStr(logLevel, tag, content));
				out.flush();
			} catch (IOException e) {
				Log.e("LogUtils", "", e);
			} finally {
				if (out != null) {
					out.close();
				}
			}
		} else {
			Log.v("LogUtils", "Cant write logs file is null");
		}
	}

	public static void removeStartLines(final File file, final int linesCount) throws IOException{
		Log.v("LogUtils", "removeStartLines file:" + file.toString() + " count = " + linesCount);
		final List<String> lines = new LinkedList<>();
		final Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
		while(reader.hasNextLine())
			lines.add(reader.nextLine());
		reader.close();
		Log.v("LogUtils", "linesSize = " + lines.size());
		if (lines.size() > 0 && linesCount >= 0 && linesCount <= lines.size() - 1) {
			for (int i = 0; i < linesCount && i < lines.size(); i++) {
				lines.remove(i);
			}
			final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
			for (int i = 0; i < lines.size(); i++) {
				writer.write(lines.get(i));
			}
			writer.flush();
			writer.close();
		}
	}

	/**
	 * Get File form the file path.<BR>
	 * if the file does not exist, create it and return it.
	 * @return the file
	 */
	private static File createLogFileFromPath() {
		File dirPath = FileUtil.getStorageDir(FileUtil.getAppDirectoryName());
		//Create directory if need.
		if (dirPath != null) {
			File file = new File(dirPath.getAbsolutePath() + "/" + Config.LOG_FILE_NAME);
			//Create file if need.
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						Log.i("LogUtils", "The Log file was successfully created! -" + file.getAbsolutePath());
					} else {
						Log.i("LogUtils", "The Log file exist! -" + file.getAbsolutePath());
					}
				} catch (IOException e) {
					Log.e("LogUtils", "Failed to create The Log file.", e);
					return null;
				}
			}
			if (!file.canWrite()) {
				Log.e("LogUtils", "The Log file can not be written.");
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Create log string to write in log file.
	 * @param logLevel Level of logging message.
	 * @param tag Logging message tag.
	 * @param content Useful information to save in log file.
	 */
	public static String createLogStr(String logLevel, String tag, String content) {
		return DateUtil.littleEndianDateTimeMilsFormat.format(new Date())
				+ "  " + logLevel
				+ "/" + tag
				+ ":  " + content + "\n";
	}

	/**
	 * Очистить лог-файл.
	 * @return true, если файл успешно очищен, иначе - false.
	 */
	public static boolean clearLogFile() {
		Log.v("LogUtils", "clearLogFile");
		File file = createLogFileFromPath();
		if (file != null) {
			try {
				PrintWriter writer = new PrintWriter(file);
				writer.print("");
				writer.close();
			} catch (FileNotFoundException e) {
				Log.e("LogUtils", "", e);
			}
		}
		return false;
	}

	public static void checkLogsRecordingEnabled(Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		isLogsRecordingEnabled  = prefs.getBoolean("enable_recording_logs", false);
		Log.v("LogUtils", "checkLogsRecordingEnabled  = " + isLogsRecordingEnabled);
	}

	private static final String LOG_PREFIX = "";
	private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
	private static final int MAX_LOG_TAG_LENGTH = 23;

	/** Размер лог-файла (кбайт). */
	private static final int LOG_FILE_SIZE = 1000;

	/** Количество строк, которые будут удалены из начала лог-файла после того,
	 *  как лог-файл станет больше заданого размера. */
	private static final int LOG_FILE_REMOVE_LINES_COUNT = 150;

	/** Триггер, если true, то логи будут сохраняться в файл, а иначе не будут. */
	private static boolean isLogsRecordingEnabled = false;
}
