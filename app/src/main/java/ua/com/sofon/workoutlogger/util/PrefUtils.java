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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Utilities and constants related to app preferences.
 * @author Dimowner
 */
public class PrefUtils {

	/**
	 * Boolean preference that when checked, indicates that the user would like to see times
	 * in their local timezone throughout the app.
	 */
	public static final String PREF_LOCAL_TIMES = "pref_local_times";

	/**
	 * Boolean indicating whether we should attempt to sign in on startup (default true).
	 */
	public static final String PREF_USER_REFUSED_SIGN_IN = "pref_user_refused_sign_in";

	/**
	 * Boolean indicating whether the debug build warning was already shown.
	 */
	public static final String PREF_DEBUG_BUILD_WARNING_SHOWN = "pref_debug_build_warning_shown";

	/**
	 * Boolean indicating whether we performed the (one-time) welcome flow.
	 */
	public static final String PREF_WELCOME_DONE = "pref_welcome_done";


	public static boolean isUsingLocalTime(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(PREF_LOCAL_TIMES, false);
	}

	public static void setUsingLocalTime(final Context context, final boolean usingLocalTime) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putBoolean(PREF_LOCAL_TIMES, usingLocalTime).apply();
	}

	public static void markUserRefusedSignIn(final Context context) {
		markUserRefusedSignIn(context, true);
	}

	public static void markUserRefusedSignIn(final Context context, final boolean refused) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putBoolean(PREF_USER_REFUSED_SIGN_IN, refused).apply();
	}

	public static boolean hasUserRefusedSignIn(final Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(PREF_USER_REFUSED_SIGN_IN, false);
	}

	public static boolean wasDebugWarningShown(final Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(PREF_DEBUG_BUILD_WARNING_SHOWN, false);
	}

	public static void markDebugWarningShown(final Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putBoolean(PREF_DEBUG_BUILD_WARNING_SHOWN, true).apply();
	}

	public static boolean isWelcomeDone(final Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getBoolean(PREF_WELCOME_DONE, false);
	}

	public static void markWelcomeDone(final Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.edit().putBoolean(PREF_WELCOME_DONE, true).apply();
	}

	public static void registerOnSharedPreferenceChangeListener(final Context context,
										SharedPreferences.OnSharedPreferenceChangeListener listener) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.registerOnSharedPreferenceChangeListener(listener);
	}

	public static void unregisterOnSharedPreferenceChangeListener(final Context context,
										SharedPreferences.OnSharedPreferenceChangeListener listener) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		sp.unregisterOnSharedPreferenceChangeListener(listener);
	}
}
