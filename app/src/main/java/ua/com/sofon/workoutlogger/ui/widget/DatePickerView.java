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
import android.widget.DatePicker;
import android.widget.EditText;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.DateUtil;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * View for date input with {@link android.widget.DatePicker DatePicker}.
 * @author Dimowner
 */
public class DatePickerView extends EditText {

	/**
	 * Constructor
	 * @param context Application context.
	 */
	public DatePickerView(Context context) {
		super(context);
		initDatePickerView(context);
	}

	/**
	 * Constructor
	 * @param context Application context.
	 * @param attrs View attributes.
	 */
	public DatePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDatePickerView(context);
	}

	/**
	 * View initialization
	 * @param context Application context.
	 */
	private void initDatePickerView(Context context) {
		setFocusable(false);
		datePicker = new DatePicker(context);

		setText(getContext().getString(R.string.date_picker_date) + " "
				+ DateUtil.getActiveDateFormat().format(new Date()));

		alertDialog = new AlertDialog.Builder(context)
			.setView(datePicker)
			.setPositiveButton(R.string.datepicker_select, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					Calendar calendar = Calendar.getInstance();
					calendar.set(
							datePicker.getYear(),
							datePicker.getMonth(),
							datePicker.getDayOfMonth()
					);
					setText(getContext().getString(R.string.date_picker_date) +
							" " + DateUtil.getActiveDateFormat().format(calendar.getTime()));
				}
			})
				.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.setNeutralButton(R.string.datepicker_clear, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setText(R.string.date_picker_date);
				}
			})
			.create();
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Calendar calendar = getCalendar();

			datePicker.updateDate(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)
			);
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
		String dateStr = null;
		if (pos != -1) {
			dateStr = str.substring(pos + 1);
		}

		try {
			return DateUtil.parseCalendar(
					DateUtil.getActiveDateFormat(), dateStr);
		} catch (ParseException e) {
			LOGE(VIEW_LOG_TAG, "", e);
			return null;
		}
	}

	public Date getDate() {
		String str = getText().toString();
		int pos = str.indexOf(":");
		String dateStr = null;
		if (pos != -1) {
			dateStr = str.substring(pos + 1);
		}

		try {
			return DateUtil.parseDate(
					DateUtil.getActiveDateFormat(), dateStr);
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
	 * Sets whether the {@link android.widget.CalendarView CalendarView} is shown.
	 * @param shown True if the calendar view is to be shown.
	 */
	public void setCalendarViewShown(boolean shown) {
		datePicker.setCalendarViewShown(shown);
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

	/** Dialog window for showing{@link android.widget.DatePicker DatePicker} */
	private AlertDialog alertDialog;

	/** View for picking date. */
	private DatePicker datePicker;

	/** Need or not show button "Clear". */
	private boolean showNeutralButton = true;
}
