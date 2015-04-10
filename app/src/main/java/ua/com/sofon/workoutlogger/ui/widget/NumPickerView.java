package ua.com.sofon.workoutlogger.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.NumberPicker;
import ua.com.sofon.workoutlogger.R;

/**
 * View for number input with {@link android.widget.NumberPicker NumberPicker}.
 * @author Dimowner
 */
public class NumPickerView extends EditText {

	/**
	 * Constructor
	 * @param context Application context.
	 */
	public NumPickerView(Context context) {
		super(context);
		initNumPickerView(context);
	}

	/**
	 * Constructor
	 * @param context Application context.
	 * @param attrs View attributes.
	 */
	public NumPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initNumPickerView(context);
	}

	/**
	 * View initialization
	 * @param context Application context.
	 */
	private void initNumPickerView(Context context) {
		setFocusable(false);
		numberPicker = new NumberPicker(context);
		updateValues();

		alertDialog = new AlertDialog.Builder(context)
			.setView(numberPicker)
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
					setText("");
				}
			})
			.create();
	}

	@Override
	public boolean onTouchEvent(@NonNull MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
    		if (getText() == null || getText().length() == 0) {
    			numberPicker.setValue(minValue);
    		}
			alertDialog.show();
		}
	    return true;
	}

	/**
	 * Get min value from {@link android.widget.NumberPicker NumberPicker}.
	 * @return Min value.
	 */
	public int getMinValue() {
		return minValue;
	}

	/**
	 * Get max value from {@link android.widget.NumberPicker NumberPicker}.
	 * @return Max value
	 */
	public int getMaxValue() {
		return maxValue;
	}

	/**
	 * Get value step from {@link android.widget.NumberPicker NumberPicker}.
	 * @return Value step.
	 */
	public int getStep() {
		return valueStep;
	}

	/**
	 * Set params for {@link android.widget.NumberPicker NumberPicker}.
	 * @param minValue Min value.
	 * @param maxValue Max value.
	 * @param valueStep value step.
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

	/** Min value in {@link android.widget.NumberPicker NumberPicker}. */
	private int minValue = 0;

	/** Max value in {@link android.widget.NumberPicker NumberPicker}. */
	private int maxValue = 9;

	/** Value step in {@link android.widget.NumberPicker NumberPicker}. */
	private int valueStep = 1;

	/** Dialog window for showing {@link android.widget.NumberPicker NumberPicker} */
	private AlertDialog alertDialog;

	/** View for picking number. */
	private NumberPicker numberPicker;
}
