package ua.com.sofon.workoutlogger.ui.widget;

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
import java.util.Calendar;
import ua.com.sofon.workoutlogger.R;

/**
 * Поле для выбора даты, заполняется с помощью {@link android.widget.DatePicker DatePicker}.
 * @author Dimowner
 */
public class DatePickerView extends EditText {

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 */
	public DatePickerView(Context context) {
		super(context);
		initDatePickerView(context);
	}

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 * @param attrs Атрибуты поля.
	 */
	public DatePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDatePickerView(context);
	}

	/**
	 * Инициализация поля.
	 * @param context Контекст приложения.
	 */
	private void initDatePickerView(Context context) {
		setFocusable(false);
		datePicker = new DatePicker(context);

		alertDialog = new AlertDialog.Builder(context)
			.setView(datePicker)
			.setTitle(R.string.datepicker_set_date)
			.setPositiveButton(R.string.datepicker_select, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {

				    Calendar calendar = Calendar.getInstance();
				    calendar.set(
				    		datePicker.getYear(),
				    		datePicker.getMonth(),
				    		datePicker.getDayOfMonth()
				    	);
//				    setValue(calendar.getTime());
					setText(calendar.getTime().toString());
				}
			})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.setNeutralButton(R.string.datepicker_clear, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
//					setValue(null);
					setText("");
				}
			})
			.create();
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {

//    		if (!(getValue() == null)) {
//    			Calendar calendar = Calendar.getInstance();
//    			calendar.setTimeInMillis(getValue().getTime());
//
//    			datePicker.updateDate(
//    					calendar.get(Calendar.YEAR),
//    					calendar.get(Calendar.MONTH),
//    					calendar.get(Calendar.DAY_OF_MONTH)
//    				);
//    		}

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

	/**
	 * Сделать видимой или невидимой кнопку "Очистить".
	 * @param showButton Признак потребности отображения кнопки.
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

	/** Диалоговое окно в котором отображается {@link android.widget.DatePicker DatePicker} */
	private AlertDialog alertDialog;

	/** Вюшка для выбора времени. */
	private DatePicker datePicker;

	/** Признак потребности отображения кнопки "Очистить". */
	private boolean showNeutralButton = true;
}
