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
 * Preference field for input numbers with
 * {@link android.widget.NumberPicker NumberPicker}.
 * @author Dimowner
 */
public class NumPickerPreference extends DialogPreference {

	/**
	 * Constructor
	 * @param context Application context.
	 * @param attrs View attributes.
	 */
	public NumPickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		setPositiveButtonText(R.string.btn_select);
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

		updateValues();
		return view;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		// Create instance of custom BaseSavedState
		final SavedState myState = new SavedState(superState);
		// Set the state's value with the class member that holds current setting value
		if (numberPicker != null) {
			myState.value = numberPicker.getValue();
		} else {
			myState.value = selectedNumber;
		}
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// Cast state to custom BaseSavedState and pass to superclass
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());

		// Set this Preference's widget to reflect the restored state
		if (numberPicker != null) {
			numberPicker.setValue(myState.value);
		}
	}

	/**
	 * Get min value from{@link android.widget.NumberPicker NumberPicker}.
	 * @return Min value.
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Get max value from {@link android.widget.NumberPicker NumberPicker}.
	 * @return Max value.
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Get values step from {@link android.widget.NumberPicker NumberPicker}.
	 * @return Value step.
	 */
	public int getStep() {
		return valueStep;
	}

	/**
	 * Set new params for {@link android.widget.NumberPicker NumberPicker}.
	 * @param minValue Min value.
	 * @param maxValue max value.
	 * @param valueStep Value step.
	 */
	public void setNumPickerParams(int minValue, int maxValue, int valueStep) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.valueStep = valueStep;
		updateValues();
	}

	/**
	 * Update parameters in {@link android.widget.NumberPicker NumberPicker}.
	 */
	private void updateValues() {
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

	/** Value by default in {@link android.widget.NumberPicker NumberPicker}. */
	private final int DEFAULT_VALUE = 0;

	/** max value in {@link android.widget.NumberPicker NumberPicker}. */
	private int minValue = 0;

	/** Min value in {@link android.widget.NumberPicker NumberPicker}. */
	private int maxValue = 9;

	/** Value step in {@link android.widget.NumberPicker NumberPicker}. */
	private int valueStep = 1;

	/** View for picking the number. */
	private NumberPicker numberPicker;

	/** Selected number. */
	private int selectedNumber;

	/** Application context. */
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
