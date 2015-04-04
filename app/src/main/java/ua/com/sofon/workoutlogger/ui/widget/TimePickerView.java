package ua.com.sofon.workoutlogger.ui.widget;

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
import java.util.Date;

import ua.com.sofon.workoutlogger.R;

/**
 * Поле для ввода времени, заполняется с помощью {@link android.widget.TimePicker TimePicker}.
 * @author Dimowner
 */
public class TimePickerView extends EditText {

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 */
	public TimePickerView(Context context) {
		super(context);
		initTimePickerView(context);
	}

//    /**
//     * Установить переменные стиля приложения.
//     */
//    protected void setStyleVaribles(Context context) {
//        TypedArray a = context.getTheme().obtainStyledAttributes(pApplication.themeResID,
//                new int[] {R.attr.colorControlNormal});
//        colorControlNormal = a.getColor(0, 0);
//    }

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 * @param attrs Атрибуты поля.
	 */
	public TimePickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTimePickerView(context);
	}

	/**
	 * Инициализация поля.
	 * @param context Контекст приложения.
	 */
	private void initTimePickerView(Context context) {
		setFocusable(false);
		TimePicker timePicker = new TimePicker(context);
		timePicker.setIs24HourView(true);

		alertDialog = new AlertDialog.Builder(context)
			.setView(timePicker)
			.setTitle(R.string.timepicker_set_time)
			.setPositiveButton(
					R.string.timepicker_select, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
//					setValue(DateUtil.getDateTime(DateUtil.getCurDate(),
//							timePicker.getCurrentHour(), timePicker.getCurrentMinute()));
				}
			})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.setNeutralButton(R.string.timepicker_select, new DialogInterface.OnClickListener() {
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
//			Date curDate = new Date();//DateUtil.getCurDateTime();
//
//    		if (getValue() == null) {
//    			timePicker.setCurrentHour(DateUtil.getHour(curDate));
//    			timePicker.setCurrentMinute(DateUtil.getMinute(curDate));
//    		} else {
//    			timePicker.setCurrentHour(DateUtil.getHour(getValue()));
//    			timePicker.setCurrentMinute(DateUtil.getMinute(getValue()));
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

	/** Диалоговое окно в котором отображается {@link android.widget.TimePicker TimePicker} */
	private AlertDialog alertDialog;

//	/** Вюшка для выбора времени. */
//	private TimePicker timePicker;

	/** Признак потребности отображения кнопки "Очистить". */
	private boolean showNeutralButton = true;

//    /** Цвет элемента. */
//    private int colorControlNormal;
}
