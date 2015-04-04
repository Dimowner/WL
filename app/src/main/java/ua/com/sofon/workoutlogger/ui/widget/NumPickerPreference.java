package ua.com.sofon.workoutlogger.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import ua.com.sofon.workoutlogger.R;

/**
 * Поле в настройках для ввода числа, заполняется с помощью
 * {@link android.widget.NumberPicker NumberPicker}.
 * @author Dimowner
 */
public class NumPickerPreference extends DialogPreference {
	
	/**
	 * Конструктор.
	 * @param context Контекст приложения.
	 * @param attrs Атрибуты поля.
	 */
	public NumPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setTitle(R.string.numpicker_set_number);
		setPositiveButtonText(R.string.numpicker_select);
		setNegativeButtonText(R.string.btn_cancel);
	}

	@Override
	protected void onDialogClosed(boolean positiveResult) {
		if (positiveResult) {
			selectedNumber = numberPicker.getValue();
			if (callChangeListener(selectedNumber)) {
				persistInt(selectedNumber);
			}
		}
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			selectedNumber = this.getPersistedInt(DEFAULT_VALUE);
		} else {
			// Set default state from the XML attribute
			selectedNumber = (Integer) defaultValue;
			persistInt(selectedNumber);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return a.getInteger(index,  DEFAULT_VALUE);
	}

	@Override
	protected View onCreateDialogView() {
		LinearLayout view = new LinearLayout(context);
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.setGravity(Gravity.CENTER_HORIZONTAL);
		numberPicker = new NumberPicker(context);

		//Block keyboard for NumberPicker.
//		numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		view.addView(numberPicker);

		setNewValues();
		return view;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		// Create instance of custom BaseSavedState
		final SavedState myState = new SavedState(superState);
		// Set the state's value with the class member that holds current setting value
		if (myState != null) {
			if (numberPicker != null) {
				myState.value = numberPicker.getValue();
			} else {
				myState.value = selectedNumber;
			}
		}
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// Cast state to custom BaseSavedState and pass to superclass
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());

		// Set this Preference's widget to reflect the restored state
		if (myState != null && numberPicker != null) {
			numberPicker.setValue(myState.value);
		}
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
		numberPicker.setValue(selectedNumber);
	}

	/**
	 * Set the title text for this dialog's window.
	 * @param title The new text to display in the title.
	 */
	public void setTitle(CharSequence title) {
		setDialogTitle(title);
	}

	/**
	 * Set the title using the given resource id.
	 * @param titleId the title's text resource identifier 
	 */
	public void setTitle(int titleId) {
		setDialogTitle(titleId);
	}

	/**
	 * Set the message to display.
	 * @param message Message to display in dialog.
	 */
	public void setMessage(CharSequence message) {
		setDialogMessage(message);
	}

	/**
	 * Set the message to display.
	 * @param messageId Message text resource identifier.
	 */
	public void setMessage(int messageId) {
		setDialogMessage(messageId);
	}

	/**
	 * Set the {@link android.graphics.drawable.Drawable Drawable} to be used in the title.
	 * @param icon Icon to display in dialog's title.
	 */
	public void setIcon(Drawable icon) {
		setDialogIcon(icon);
	}

	/**
	 * Set the resource id of the {@link android.graphics.drawable.Drawable Drawable} 
	 * to be used in the title.
	 * @param resId Resource of icon to display in dialog's title.
	 */
	public void setIcon(int resId) {
		setDialogIcon(resId);
	}

	/** Значение по умолчанию. */
	private final int DEFAULT_VALUE = 0;

	/** Минимальное значение в {@link android.widget.NumberPicker NumberPicker}. */
	private int minValue = 0;

	/** Максимальное значение в {@link android.widget.NumberPicker NumberPicker}. */
	private int maxValue = 9;

	/** Шаг значений в {@link android.widget.NumberPicker NumberPicker}. */
	private int valueStep = 1;

	/** Вюшка для выбора числа. */
	private NumberPicker numberPicker;

	/** Значение выбранного элемента. */
	private int selectedNumber;

	/** Контекст приложения. */
	private Context context;


	/**
	 * Saving and restoring the Preference's state.
	 */
	private static class SavedState extends BaseSavedState {
		// Member that holds the setting's value
		// Change this data type to match the type saved by your Preference
		int value;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			// Get the current preference's value
			value = source.readInt();  // Change this to read the appropriate data type
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			// Write the preference's value
			dest.writeInt(value);  // Change this to write the appropriate data type
		}

		// Standard creator object using an instance of this class
		public static final Creator<SavedState> CREATOR =
			new Creator<SavedState>() {

			public SavedState createFromParcel(Parcel in) {
        		return new SavedState(in);
			}

			public SavedState[] newArray(int size) {
				return new SavedState[size];
			}
		};
	}
}
