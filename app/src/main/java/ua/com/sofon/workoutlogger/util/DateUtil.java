package ua.com.sofon.workoutlogger.util;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Common methods for work with dates, created on 04.04.2015.
 * @author Dimowner
 */
public class DateUtil {

	/**
	 * Constructor
	 * Is private to forbid creation of an object.
	 */
	private DateUtil() {
	}

	public static DateFormat getActiveDateFormat() {
		return activeDateFormat;
	}

	public static DateFormat getActiveDateTimeFormat() {
		return activeDateTimeFormat;
	}

	/**
	 * Parse time from string.
	 * @param dateStr string with date to parse.
	 * @return Calendar that contains parsed time or throws ParseException if failed to parse.
	 * @throws ParseException
	 */
	public static Calendar parseCalendar(DateFormat format, String dateStr) throws ParseException {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(parseDate(format, dateStr));
		return calendar;
	}

	/**
	 * Parse date from string.
	 * @param dateStr string with date to parse.
	 * @return Parsed date or throws ParseException  if failed to parse.
	 * @throws ParseException
	 */
	public static Date parseDate(DateFormat format, String dateStr) throws ParseException {
		Date date = null;
		if (dateStr != null && !dateStr.isEmpty()) {
			date = format.parse(dateStr);
		}
		return date;
	}

	public static String formatDate(Date date) {
		return activeDateFormat.format(date);
	}

	public static String formatDateTime(Date date) {
		return activeDateTimeFormat.format(date);
	}

//	B – Big-endian (year, month, day), e.g. 1996-04-22
//	L – Little-endian (day, month, year), e.g. 22.04.96 or 22/04/96 or 22 April 1996
//	M – Middle-endian (month, day, year), e.g. 04/22/96 or April 22, 1996

	public static final DateFormat bigEndianDateFormat =
			new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat littleEndianDateFormat =
			new SimpleDateFormat("dd.MM.yyyy");

	public static final DateFormat middleEndianDateFormat =
			new SimpleDateFormat("MM.dd.yyyy");

	public static final DateFormat littleEndianDateTimeFormat =
			new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

	public static final DateFormat littleEndianDateTimeMilsFormat =
			new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");

	/** Time format. */
	public static final DateFormat timeFormat =
			new SimpleDateFormat("HH:mm");

	/** Time with seconds format. */
	public static final DateFormat timeSecFormat =
			new SimpleDateFormat("HH:mm:ss");

//	TODO: Add date format selection into the application settings.
	/** Active date format. */
	private static DateFormat activeDateFormat = littleEndianDateFormat;

	/** Active date and time format. */
	private static DateFormat activeDateTimeFormat = littleEndianDateTimeFormat;

	/** Tag for logging messages. */
	private static final String LOG_TAG = LogUtils.makeLogTag("DateUtil");

	static {
			// отключаем попытки вычисления даты при некорректном вводе
			bigEndianDateFormat.setLenient(false);
			littleEndianDateFormat.setLenient(false);
			middleEndianDateFormat.setLenient(false);
			timeFormat.setLenient(false);
			timeSecFormat.setLenient(false);
	}
}
