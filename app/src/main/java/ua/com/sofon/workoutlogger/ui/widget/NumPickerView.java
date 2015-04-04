package ua.com.sofon.workoutlogger.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.NumberPicker;

import ua.com.sofon.workoutlogger.R;

/**
 * Поле для ввода числа, заполняется с помощью {@link android.widget.NumberPicker NumberPicker}.
 * @author Dimowner
 */
public class NumPickerView extends EditText {

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 */
	public NumPickerView(Context context) {
		super(context);
		initNumPickerView(context);
	}

	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 * @param attrs Атрибуты поля.
	 */
	public NumPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initNumPickerView(context);
	}

	/**
	 * Инициализация поля.
	 * @param context Контекст приложения.
	 */
	private void initNumPickerView(Context context) {
		setFocusable(false);
		numberPicker = new NumberPicker(context);
		setNewValues();

		alertDialog = new AlertDialog.Builder(context)
			.setView(numberPicker)
			.setTitle(R.string.numpicker_set_number)
			.setPositiveButton(R.string.numpicker_select, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					setText(Integer.toString((numberPicker.getValue() * valueStep)));
				}
			})
			.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
				}
			})
			.setNeutralButton(R.string.numpicker_clear, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
//					setValue(null);
					setText("");
				}
			})
			.create();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
    		if (getText() == null || getText().length() == 0) {
    			numberPicker.setValue(minValue);
    		}
			alertDialog.show();
		}
	    return true; 
	}

	/**
	 * Получить минимальное значение в {@link android.widget.NumberPicker NumberPicker}.
	 * @return Минимальное значение
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Получить максимальное значение в {@link android.widget.NumberPicker NumberPicker}.
	 * @return Максимальное значение
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Получить шаг между значениями в {@link android.widget.NumberPicker NumberPicker}.
	 * @return Шаг между значениями.
	 */
	public int getStep() {
		return valueStep;
	}

	/**
	 * Задать новые значения в {@link android.widget.NumberPicker NumberPicker}.
	 * @param minValue Минимальное значение.
	 * @param maxValue Максимальное значение.
	 * @param valueStep Шаг значений.
	 */
	public void setNumPickerParams(int minValue, int maxValue, int valueStep) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.valueStep = valueStep;
		setNewValues();
	}

	/**
	 * Применить новые значения в {@link android.widget.NumberPicker NumberPicker}.
	 */
	private void setNewValues() {
		if (valueStep <= 0) {
			valueStep = 1;
		}
		final String[] valueSet = new String[maxValue/valueStep + 1];

		for (int i = minValue; i <= maxValue; i += valueStep) {
			valueSet[(i/valueStep)] = String.valueOf(i);
		}
		numberPicker.setDisplayedValues(valueSet);
		numberPicker.setMaxValue(maxValue/valueStep);
		numberPicker.setMinValue(minValue);
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

	/** Минимальное значение в {@link android.widget.NumberPicker NumberPicker}. */
	private int minValue = 0;

	/** Максимальное значение в {@link android.widget.NumberPicker NumberPicker}. */
	private int maxValue = 9;

	/** Шаг значений в {@link android.widget.NumberPicker NumberPicker}. */
	private int valueStep = 1;

	/** Диалоговое окно в котором отображается {@link android.widget.NumberPicker NumberPicker} */
	private AlertDialog alertDialog;

	/** Вюшка для выбора числа. */
	private NumberPicker numberPicker;
}
