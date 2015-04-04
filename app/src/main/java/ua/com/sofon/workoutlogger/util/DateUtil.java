package ua.com.sofon.workoutlogger.util;

import java.util.Locale;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

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

//	B – Big-endian (year, month, day), e.g. 1996-04-22
//	L – Little-endian (day, month, year), e.g. 22.04.96 or 22/04/96 or 22 April 1996
//	M – Middle-endian (month, day, year), e.g. 04/22/96 or April 22, 1996

	public static final DateFormat bigEndianDateFormat =
			new SimpleDateFormat("yyyy-MM-dd");

	public static final DateFormat littleEndianDateFormat =
			new SimpleDateFormat("dd.MM.yyyy");

	public static final DateFormat middleEndianDateFormat =
			new SimpleDateFormat("MM.dd.yyyy");

	/** Time format. */
	public static final DateFormat timeFormat =
			new SimpleDateFormat("HH:mm");

	/** Time with seconds format. */
	public static final DateFormat timeSecFormat =
			new SimpleDateFormat("HH:mm:ss");

	static {
			// отключаем попытки вычисления даты при некорректном вводе
			bigEndianDateFormat.setLenient(false);
			littleEndianDateFormat.setLenient(false);
			middleEndianDateFormat.setLenient(false);
			timeFormat.setLenient(false);
			timeSecFormat.setLenient(false);
	}
}
