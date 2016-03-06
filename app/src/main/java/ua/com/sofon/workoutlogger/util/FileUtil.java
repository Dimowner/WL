package ua.com.sofon.workoutlogger.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import ua.com.sofon.workoutlogger.Config;
import ua.com.sofon.workoutlogger.R;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGD;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGI;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

/**
 * Клас для работы с файлами.
 * @author Dimowner
 */
public class FileUtil {

	/**
	 * Создать файл.
	 * Если он не существует, то создать его.
	 * @param path Путь к файлу.
	 * @param fileName Имя файла.
	 */
	public static File createFile(File path, String fileName) {
		if (path != null) {
			LOGV(LOG_TAG, "createFile path = " + path.getAbsolutePath() + " fileName = " + fileName);
			File file = new File(path + "/" + fileName);
			//Create file if need.
			if (!file.exists()) {
				try {
					if (file.createNewFile()) {
						LOGI(LOG_TAG, "The file was successfully created! - " + file.getAbsolutePath());
					} else {
						LOGI(LOG_TAG, "The file exist! - " + file.getAbsolutePath());
					}
				} catch (IOException e) {
					LOGE(LOG_TAG, "Failed to create the file.", e);
					return null;
				}
			}
			if (!file.canWrite()) {
				LOGE(LOG_TAG, "The file can not be written.");
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Записать изображение в файл.
	 * @param file Файл в который будет записано изображение.
	 * @param bitmap Изображение, которое будет записано в файл.
	 * @return Признак успешного выполнения.
	 */
	public static boolean writeImage(File file, Bitmap bitmap) {
		if (!file.canWrite()) {
			LOGE(LOG_TAG, "The file can not be written.");
			return false;
		}
		if (bitmap == null) {
			LOGE(LOG_TAG, "Failed to write! bitmap is null.");
			return false;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)) {
				fos.close();
				return true;
			}
			fos.close();
		} catch (FileNotFoundException e) {
			LOGE(LOG_TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			LOGE(LOG_TAG, "Error accessing file: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Записать данные в файл.
	 * @param file Файл в который будет записано данные.
	 * @param bytes Данные, которое будет записаны в файл.
	 * @return Признак успешного выполнения.
	 */
	public static boolean writeFile(File file, byte[] bytes) {
		if (!file.canWrite()) {
			LOGE(LOG_TAG, "The file can not be written (no rights).");
			return false;
		}
		if (bytes == null) {
			LOGE(LOG_TAG, "Failed to write! Bytes is null.");
			return false;
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bytes);
			fos.close();
		} catch (FileNotFoundException e) {
			LOGE(LOG_TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			LOGE(LOG_TAG, "Error accessing file: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Записать StackTrace исключания в файл, и поместить его в папку ошибок приложения.
	 * @param e Исключительная ситуация, которую нужно записать.
	 * @return File файл содержащий StackTrace исключения.
	 */
	public static File writeExceptionToFile(Exception e) {
		DateFormat dateTimeFormat = new SimpleDateFormat(
				"dd_MM_yyyy_HHmm", Locale.getDefault());

		File exception = FileUtil.createFile(FileUtil.getStorageDir(
				FileUtil.getAppDirectoryName() + "/" + "Errors"),
				"Exception_" + dateTimeFormat.format(new Date()) + ".txt");

		Calendar c = Calendar.getInstance();
		if (exception != null) {
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));

			PrintWriter out = null;
			try {
				out = new PrintWriter(new BufferedWriter(new FileWriter(exception, true)));
				out.println(errors.toString());
				out.flush();

			} catch (IOException e1) {
				LOGE(LOG_TAG, "", e1);
			} finally {
				if (out != null) {
					out.close();
				}
			}
			return exception;
		}
		return null;
	}

	/**
	 * Отправить файл по почте.
	 * @param context Контекст приложения.
	 * @param file Файл, который нужно отправить.
	 */
	public static void sendFileByEmail(Context context, File file) {
		Intent target = new Intent()
				.setAction(Intent.ACTION_SENDTO)
				.setData(Uri.fromParts("mailto", "Dimowner@parus.com.ua", null))
				.putExtra(Intent.EXTRA_SUBJECT, Config.APP_NAME + ", Ошибка, "
						+ DateUtil.formatDateTime(new Date()))
				.putExtra(Intent.EXTRA_TEXT, "");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			target.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
		} else {
			target.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		}
		if (file != null) {
			target.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		}
		try {
			context.startActivity(Intent.createChooser(target, "Send mail..."));
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(context, R.string.log_no_email_clients, Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Получить публичную папку на внешнем хранилище (external storage).
	 * @param dirName Имя папки.
	 */
	public static File getStorageDir(String dirName) {
		if (!StrUtil.empty(dirName)) {
			File file = new File(Environment.getExternalStorageDirectory(), dirName);
			if (isExternalStorageReadable() && isExternalStorageWritable()) {
				if (!file.exists() && !file.mkdirs()) {
					LOGE(LOG_TAG, "Directory " + file.getAbsolutePath() + " was not created");
				}
			}
			return file;
		} else {
			return null;
		}
	}

	/**
	 * Checks if external storage is available for read and write.
	 */
	public static boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/**
	 * Checks if external storage is available to at least read.
	 */
	public static boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return (Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
	}

	/**
	 * Получить адрес папки для файлов приложения.
	 * @return Строка адреса папки.
	 */
	public static String getAppDirectoryName() {
		return Config.APP_DIRECTORY;
	}

	/**
	 * Загрузить {@link Bitmap} из файловой системы.
	 * @param path Путь к файлу.
	 * @return {@link Bitmap} считаный файл.
	 */
	public static Bitmap readBitmap(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		return BitmapFactory.decodeFile(path, options);
	}

	/**
	 * Загрузить файл из файловой системы.
	 * @param file Файл, который нужно загрузить.
	 * @return {@link Bitmap} считаный файл.
	 */
	public static byte[] readFile(File file) {
		if (file.exists()) {
			byte[] data = new byte[(int) file.length()];
			try {
				int r = new FileInputStream(file).read(data);
			} catch (Exception e) {
				LOGE(LOG_TAG, "", e);
			}
			return data;
		}
		return null;
	}

	/**
	 * Загрузить файл из файловой системы.
	 * @param path Путь к файлу, который нужно загрузить.
	 * @return {@link Bitmap} считаный файл.
	 */
	public static byte[] readFile(String path) {
		File file = new File(path);
		if (file.exists()) {
			byte[] data = new byte[(int) file.length()];
			try {
				int r = new FileInputStream(file).read(data);
			} catch (Exception e) {
				LOGE(LOG_TAG, "", e);
			}
			return data;
		}
		return null;
	}

	/**
	 * Проверить что файл с указанным адресом существует.
	 * @param path Адрес файла.
	 */
	public static boolean isExistsFile(String path) {
		return (path != null && new File(path).exists());
	}

	/**
	 * Копировать файл или папку. (Ограничение по размеру до 2GB!)
	 * @param srcDir Папки или файла, который нужно скопировать.
	 * @param dstDir Адрес, куда нужно скопировать исходный файл или папку.
	 */
	public static boolean copyFileOrDirectory(String srcDir, String dstDir) {
		boolean ok = true;
		try {
			File src = new File(srcDir);
			File dst = new File(dstDir, src.getName());

			if (src.isDirectory()) {
				String files[] = src.list();
				int filesLength = files.length;
				for (int i = 0; i < filesLength; i++) {
					String src1 = (new File(src, files[i]).getPath());
					String dst1 = dst.getPath();
					ok &= copyFileOrDirectory(src1, dst1);
				}
			} else {
				ok = copyFile(src, dst);
			}
		} catch (Exception e) {
			LOGE(LOG_TAG, "", e);
			ok = false;
		}
		return ok;
	}

	/**
	 * Копировать файл. (Ограничение по размеру до 2GB!)
	 * @param sourceFile Файл, который нужно скопировать.
	 * @param destFile Адрес, куда нужно скопировать исходный файл.
	 * Имя нового файла должно бить указано!
	 */
	public static boolean copyFile(File sourceFile, File destFile) {
		boolean ok = true;
		if (!destFile.getParentFile().exists()) {
			//Create parent dirs if not exists.
			destFile.getParentFile().mkdirs();
		}
		try {
			if (!destFile.exists()) {
				//Create file if not exists.
				destFile.createNewFile();
			}
			if (destFile.exists()) {
				FileChannel source = null;
				FileChannel destination = null;
				try {
					source = new FileInputStream(sourceFile).getChannel();
					destination = new FileOutputStream(destFile).getChannel();
					destination.transferFrom(source, 0, source.size());
					ok = true;
				} finally {
					if (source != null) {
						source.close();
					}
					if (destination != null) {
						destination.close();
					}
				}
			} else {
				ok = false;
			}
		} catch (IOException e) {
			LOGE(LOG_TAG, "", e);
			ok = false;
		}
		return ok;
	}

	/**
	 * Удалить файл или папку вместе со всем содержимым.
	 * @param file Файл или папка который нужно удалить.
	 */
	public static boolean deleteDirectory(File file) {
		if (deleteRecursivelyDirs(file)) {
			return true;
		}
		LOGE(LOG_TAG, "Failed to delete directory: " + file.getAbsolutePath());
		return false;
	}

	/**
	 * Удалить файл или папку вместе со всем содержимым.
	 * @param file Файл или папка который нужно удалить.
	 */
	private static boolean deleteRecursivelyDirs(File file) {
		boolean ok = true;
		if (file != null ) {
			if (file.exists()) {
				if (file.isDirectory()) {
					String[] children = file.list();
					for (int i = 0; i < children.length; i++) {
						ok &= deleteRecursivelyDirs(new File(file, children[i]));
					}
				}
				if (ok && file.delete()) {
					LOGD(LOG_TAG, "File deleted: " + file.getAbsolutePath());
				}
			}
		}
		return ok;
	}

	/**
	 * Конвертировать изображение из массива байтов в {@link Bitmap}.
	 * @param bytes Массив байтов, который нужно конвертировать.
	 * @return Конвертированое изображение.
	 */
	public static Bitmap bytesToBitmap(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * Конвертировать изображение из {@link Bitmap} в массива байтов.
	 * @param bitmap {@link Bitmap}, который нужно конвертировать.
	 * @return Конвертированое изображение.
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream);
		return stream.toByteArray();
	}

	/**
	 * Получить MD5 ключ для файла.
	 * @param file Файл для которого нужно получить ключ.
	 * @return MD5 ключ.
	 */
	public static String md5(File file) {
		FileInputStream fis   = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			LOGE(LOG_TAG, "", e);
		}
		if (fis != null) {
			try {
				byte[] bytes = new byte[4096];
				int read;
				MessageDigest digest = MessageDigest.getInstance("MD5");
				while ((read = fis.read(bytes)) != -1) {
					digest.update(bytes, 0, read);
				}
				byte[] messageDigest = digest.digest();
				//Convert bytes to String
				StringBuilder sb = new StringBuilder(32);
				for (byte b : messageDigest) {
					sb.append(hexDigits[(b >> 4) & 0x0f]);
					sb.append(hexDigits[b & 0x0f]);
				}
				fis.close();
				return sb.toString();
			} catch (Exception e) {
				LOGE(LOG_TAG, "", e);
			}
			try {
				fis.close();
			} catch (IOException e) {
				LOGE(LOG_TAG, "", e);
			}
		}
		return "";
	}

	/**
	 * Преобразовать масив байтов в строку.
	 * @param bytes Массив байтов, который нужно преобразовать.
	 * @return Результирующая строка.
	 */
	public static String bytesToString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for (byte b : bytes) {
			sb.append(hexDigits[(b >> 4) & 0x0f]);
			sb.append(hexDigits[b & 0x0f]);
		}
		Log.v(LOG_TAG, "bytesToString = " + sb.toString() + " bytes = " + Arrays.toString(bytes));
		return sb.toString();
	}


	private static char[] hexDigits = "0123456789abcdef".toCharArray();

	/** Тег для логирования информации. */
	private static final String LOG_TAG = LogUtils.makeLogTag("FileUtil");
}

