package ua.com.sofon.workoutlogger.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import ua.com.sofon.workoutlogger.R;

/**
 * User interface common methods, created on 01.03.2015.
 * @author Dimowner
 */
public class UIUtil {

	/**
	 * Constructor
	 * Is private to forbid creation of an object.
	 */
	private UIUtil() {
	}

	/**
	 * Show warning dialog with OK and Cancel buttons.
	 * @param context Application context.
	 * @param mesText Message to show in dialog.
	 * @param positiveBtnClickListener Listener for positive button click.
	 * @param negativeBtnClickListener Listener for negative button click.
	 */
	public static void showWarningDialog(
						Context context, String mesText,
						DialogInterface.OnClickListener positiveBtnClickListener,
						DialogInterface.OnClickListener negativeBtnClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_warning)
				.setMessage(mesText)
				.setIcon(R.drawable.ic_alert_grey600_36dp)
				.setCancelable(false)
				.setPositiveButton(R.string.btn_ok, positiveBtnClickListener)
				.setNegativeButton(R.string.btn_cancel, negativeBtnClickListener);

		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show warning dialog with OK and Cancel buttons.
	 * @param context Application context.
	 * @param mesRes Resource id of message to show in dialog.
	 * @param positiveBtnClickListener Listener for positive button click.
	 * @param negativeBtnClickListener Listener for negative button click.
	 */
	public static void showWarningDialog(
			Context context, int mesRes,
			DialogInterface.OnClickListener positiveBtnClickListener,
			DialogInterface.OnClickListener negativeBtnClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.title_warning)
				.setMessage(mesRes)
				.setIcon(R.drawable.ic_alert_grey600_36dp)
				.setCancelable(false)
				.setPositiveButton(R.string.btn_ok, positiveBtnClickListener)
				.setNegativeButton(R.string.btn_cancel, negativeBtnClickListener);

		AlertDialog alert = builder.create();
		alert.show();
	}
}
