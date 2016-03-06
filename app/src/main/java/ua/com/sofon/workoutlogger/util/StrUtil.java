package ua.com.sofon.workoutlogger.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Дополнительные методы работы со строками.
 * @author Dimowner
 */
public class StrUtil {

	/**
	 * Конструктор для предотвращения реализации и наследования.
	 */
	private StrUtil() {
	}

	/**
	 * Проверка на пустую строку, строку из пробелов или null
	 * @param str - проверяемая строка
	 * @return true - строка пустая, из пробелов или null/ false - нет
	 */
	public static boolean empty(String str) {
		return (str == null || str.trim().isEmpty());
	}

	/**
	 * Вернуть левую часть строки длиной len с проверкой на длину
	 * @param str исходная строка
	 * @param len возвращаемая длина
	 * @return сгенерированная строка
	 */
	public static String left(String str, int len) {
		return (empty(str) || str.length() < len ? str :
				str.substring(0, len));
	}

	/**
	 * Вернуть правую часть строки длиной len с проверкой на длину
	 * @param str исходная строка
	 * @param len возвращаемая длина
	 * @return сгенерированная строка
	 */
	public static String right(String str, int len) {
		return (empty(str) || str.length() < len ? str :
				str.substring(str.length()-len));
	}

	/**
	 * убрать конечные пробелы из строки
	 * @param str исходная строка
	 * @return Исправленная строка.
	 */
	public static String rtrim(String str) {
		StringBuilder res = new StringBuilder();
		boolean add = false;
		for (int i = str.length()-1; i >= 0; i--) {
			add = (add || str.charAt(i) != ' ');
			if (str.charAt(i) != ' ' || add) {
				res.append(str.charAt(i));
			}
		}
		return res.reverse().toString();
	}

	/**
	 * убрать начальные пробелы из строки
	 * @param str исходная строка
	 * @return Исправленная строка.
	 */
	public static String ltrim(String str) {
		StringBuilder res = new StringBuilder();
		boolean add = false;
		for (int i = 0; i < str.length(); i++) {
			add = (add || str.charAt(i) != ' ');
			if (str.charAt(i) != ' ' || add) {
				res.append(str.charAt(i));
			}
		}
		return res.toString();
	}

	/**
	 * Проверка на равенство двух строк.
	 * @param str1 первая строка
	 * @param str2 вторая строка
	 * @return true если обе строки null или равны между собой
	 */
	public static boolean equals(String str1, String str2) {
		return str1 == null && str2 == null || str1!= null && str1.equals(str2);
	}

	/**
	 * Получтить строку stack trace из исключения.
	 * @param e Исключение.
	 * @return Строка содержащая stack trace.
	 */
	public static String stackTraceToString(Throwable e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}


	/** Тег для логирования информации. */
	private static final String LOG_TAG = LogUtils.makeLogTag("StrUtil");
}
