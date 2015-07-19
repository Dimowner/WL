/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ua.com.sofon.workoutlogger.util;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import android.os.Environment;
import android.util.Log;
import ua.com.sofon.workoutlogger.Config;

public class LogUtils {

	public static String makeLogTag(String str) {
		if (str.length() > MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH) {
			return LOG_PREFIX + str.substring(0, MAX_LOG_TAG_LENGTH - LOG_PREFIX_LENGTH - 1);
		}

		return LOG_PREFIX + str;
	}

	public static void LOGD(final String tag, String message) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (
//				  BuildConfig.DEBUG ||
				Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.DEBUG)) {
			Log.d(tag, message);
			if (Config.IS_LOGS_RECORDING_ENABLED) {
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
			if (Config.IS_LOGS_RECORDING_ENABLED) {
				log2file("D", tag, message);
			}
		}
	}

	public static void LOGV(final String tag, String message) {
		//noinspection PointlessBooleanExpression,ConstantConditions
		if (
//				  BuildConfig.DEBUG &&
				Config.IS_DEBUG_BUILD || Log.isLoggable(tag, Log.VERBOSE)) {
			Log.v(tag, message);
			if (Config.IS_LOGS_RECORDING_ENABLED) {
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
			if (Config.IS_LOGS_RECORDING_ENABLED) {
				log2file("V", tag, message);
			}
		}
	}

	public static void LOGI(final String tag, String message) {
		Log.i(tag, message);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("I", tag, message);
		}
	}

	public static void LOGI(final String tag, String message, Throwable cause) {
		Log.i(tag, message, cause);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("I", tag, message);
		}
	}

	public static void LOGW(final String tag, String message) {
		Log.w(tag, message);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("W", tag, message);
		}
	}

	public static void LOGW(final String tag, String message, Throwable cause) {
		Log.w(tag, message, cause);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("W", tag, message);
		}
	}

	public static void LOGE(final String tag, String message) {
		Log.e(tag, message);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("E", tag, message);
		}
	}

	public static void LOGE(final String tag, String message, Throwable cause) {
		Log.e(tag, message, cause);
		if (Config.IS_LOGS_RECORDING_ENABLED) {
			log2file("E", tag, message);
		}
	}

	/**
	 * Write log into file.
	 * @param logLevel Level of logging message.
	 * @param tag Logging message tag.
	 * @param content Useful information to save in log file.
	 */
	protected static void log2file(String logLevel, String tag, String content) {
		File file = GetFileFromPath();
		PrintWriter out = null;
		if (file != null) {
			try {
				int file_size = Integer.parseInt(String.valueOf(file.length()/1024));
				Log.v("LogUtils", "File size = " + file_size);
				if (file_size > 500) {
					removeStartLines(file, 150);
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
	private static File GetFileFromPath() {
		//Create directory if need.
		File file = new File(
				getAlbumStorageDir(Config.APP_DIRECTORY).getAbsolutePath()
				+ "/" + Config.LOG_FILE_NAME
		);

		//Create file if need.
		if (file.exists()) {
			if (!file.canWrite()) {
				Log.e("LogUtils", "The Log file can not be written.");
			}
		} else {
			try {
				if (file.createNewFile()) {
					Log.i("LogUtils", "The Log file was successfully created! -" + file.getAbsolutePath());
				} else {
					Log.i("LogUtils", "The Log file exist! -" + file.getAbsolutePath());
				}

				if (!file.canWrite()) {
					Log.e("LogUtils", "The Log file can not be written.");
				}
			} catch (IOException e) {
				Log.e("LogUtils", "Failed to create The Log file.", e);
			}
		}
		return file;
	}

	/**
	 * Get or create the directory where located log file.
	 * @param dirName Directory name where stored log file.
	 */
	public static File getAlbumStorageDir(String dirName) {
		File file = new File(Environment.getExternalStorageDirectory(), dirName);
		if (isExternalStorageReadable() && isExternalStorageWritable()
				&& !file.exists() && !file.mkdirs()) {
			Log.e("LogUtils", "Directory not created");
		}
		return file;
	}

	/**
	 * Checks if external storage is available for read and write.
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if external storage is available to at least read.
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			return true;
		}
		return false;
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

	private LogUtils() {
	}

	private static final String LOG_PREFIX = "";
	private static final int LOG_PREFIX_LENGTH = LOG_PREFIX.length();
	private static final int MAX_LOG_TAG_LENGTH = 23;
}
