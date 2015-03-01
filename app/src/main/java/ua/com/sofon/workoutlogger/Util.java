package ua.com.sofon.workoutlogger;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Common methods, created on 01.03.2015.
 * @author Dimowner
 */
public class Util {

	/**
	 * Constructor
	 * Is private to forbid creation of an object.
	 */
	private Util() {
	}

	/**
	 * Show warning dialog with OK and Cancel buttons.
	 * @param context Application context.
	 * @param mesText Message to show in dialog.
	 * @param positivBtnClickListener Listener for positive button click.
	 * @param negativeBtnClickListener Listener for negative button click.
	 */
	public static void showWarningDialog(
						Context context, String mesText,
						DialogInterface.OnClickListener positivBtnClickListener,
						DialogInterface.OnClickListener negativeBtnClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Warning")
				.setMessage(mesText)
				.setIcon(R.mipmap.ic_alert_grey600_36dp)
				.setCancelable(false)
				.setPositiveButton("OK", positivBtnClickListener)
				.setNegativeButton("Cancel", negativeBtnClickListener);

		AlertDialog alert = builder.create();
		alert.show();
	}
}
