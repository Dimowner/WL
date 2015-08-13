package ua.com.sofon.workoutlogger.ui.widget;

import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TimePicker;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.DateUtil;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * View for time input with {@link android.widget.TimePicker TimePicker}.
 * @author Dimowner
 */
public class TimePickerView extends EditText {

	/**
	 * Constructor
	 * @param context Application context.
	 */
	public TimePickerView(Context context) {
		super(context);
		initTimePickerView(context);
	}

	/**
	 * Constructor
	 * @param context Application context.
	 * @param attrs View attributes.
	 */
	public TimePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTimePickerView(context);
	}

	/**
	 * View initialization
	 * @param context Application context.
	 */
	private void initTimePickerView(Context context) {
		setFocusable(false);
		timePicker = new TimePicker(context);
		timePicker.setIs24HourView(true);

		setText(getContext().getString(R.string.time_picker_time) + " "
				+ DateUtil.timeFormat.format(new Date()));

		alertDialog = new AlertDialog.Builder(context)
			.setView(timePicker)
			.setPositiveButton(
					R.string.timepicker_select, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							Calendar calendar = Calendar.getInstance();
							calendar.set(
									calendar.get(Calendar.YEAR),
									calendar.get(Calendar.MONTH),
									calendar.get(Calendar.DAY_OF_MONTH),
									timePicker.getCurrentHour(),
									timePicker.getCurrentMinute()
							);
							setText(getContext().getString(R.string.time_picker_time) +
									" " + DateUtil.timeFormat.format(calendar.getTime()));
						}
					})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.setNeutralButton(R.string.timepicker_clear, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setText(R.string.time_picker_time);
				}
			}).create();
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Calendar calendar = getCalendar();

			timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
			timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
			alertDialog.show();

			if (showNeutralButton) {
				alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
						.setVisibility(View.VISIBLE);
			} else {
				alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
						.setVisibility(View.GONE);
			}
		}
	    return true; 
	}

	public Calendar getCalendar() {
		String str = getText().toString();
		int pos = str.indexOf(":");
		String timeStr = "";
		if (pos != -1) {
			timeStr = str.substring(pos + 1);
		}

		try {
			return DateUtil.parseCalendar(
					DateUtil.timeFormat, timeStr.trim());
		} catch (ParseException e) {
			LOGE(VIEW_LOG_TAG, "", e);
			return null;
		}
	}

	public Date getDate() {
		String str = getText().toString();
		int pos = str.indexOf(":");
		String timeStr = "";
		if (pos != -1) {
			timeStr = str.substring(pos + 1);
		}

		try {
			return DateUtil.parseDate(
					DateUtil.timeFormat, timeStr.trim());
		} catch (ParseException e) {
			LOGE(VIEW_LOG_TAG, "", e);
			return null;
		}
	}

	/**
	 * Set button "Clear" visible.
	 * @param showButton If true the button will be visible either not.
	 */
	public void showNeutralButton(boolean showButton) {
		showNeutralButton = showButton;
	}

	/**
	 * Set the title text for this dialog's window.
	 * @param title The new text to display in the title.
	 */
	public void setTitle(CharSequence title) {
		alertDialog.setTitle(title);
	}

	/**
	 * Set the title using the given resource id.
	 * @param titleId the title's text resource identifier 
	 */
	public void setTitle(int titleId) {
		alertDialog.setTitle(titleId);
	}

	/**
	 * Set the message to display.
	 * @param message Message to display in dialog.
	 */
	public void setMessage(CharSequence message) {
		alertDialog.setMessage(message);
	}

	/**
	 * Set the {@link android.graphics.drawable.Drawable Drawable} to be used in the title.
	 * @param icon Icon to display in dialog's title.
	 */
	public void setIcon(Drawable icon) {
		alertDialog.setIcon(icon);
	}

	/**
	 * Set the resource id of the {@link android.graphics.drawable.Drawable Drawable} 
	 * to be used in the title.
	 * @param resId Resource of icon to display in dialog's title.
	 */
	public void setIcon(int resId) {
		alertDialog.setIcon(resId);
	}

//	/** Pattern for time format that will be used in view */
//	private static final String TIME_PATTERN = "HH:mm";
//
//	/** Time format that will be used in view */
//	private SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());

	/** Dialog window for showing {@link android.widget.TimePicker TimePicker} */
	private AlertDialog alertDialog;

	/** View for picking time. */
	private TimePicker timePicker;

	/** Need or not show button "Clear". */
	private boolean showNeutralButton = true;
}
