package ua.com.sofon.workoutlogger.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import java.util.Calendar;
import java.util.Date;

import ua.com.sofon.workoutlogger.R;

/**
 * Preference field for input time with
 * {@link android.widget.DatePicker DatePicker}.
 * @author Dimowner
 */
public class DatePickerPreference extends DialogPreference {

	/**
	 * Constructor
	 * @param context Application context.
	 * @param attrs View attributes.
	 */
	public DatePickerPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		setTitle(R.string.datepicker_set_date);
	}

	@Override
	protected void showDialog(Bundle state) {
		View contentView = onCreateDialogView();
		AlertDialog alertDialog = new AlertDialog.Builder(context)
				.setView(contentView)
				.setPositiveButton(R.string.datepicker_select, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Calendar calendar = Calendar.getInstance();
						calendar.set(
								datePicker.getYear(),
								datePicker.getMonth(),
								datePicker.getDayOfMonth()
						);
						selectedDate = calendar.getTimeInMillis();
						if (callChangeListener(selectedDate)) {
							persistLong(selectedDate);
						}
					}
				})
				.setNegativeButton(R.string.btn_cancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
					}
				})
				.setNeutralButton(R.string.datepicker_clear, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						Calendar calendar = Calendar.getInstance();
						selectedDate = calendar.getTimeInMillis();
						if (callChangeListener(selectedDate)) {
							persistLong(selectedDate);
						}
					}
				}).create();

		if (state != null) {
			alertDialog.onRestoreInstanceState(state);
		}
		alertDialog.show();

		if (showNeutralButton) {
			alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
					.setVisibility(View.VISIBLE);
		} else {
			alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL)
					.setVisibility(View.GONE);
		}
	}

	@Override
	protected View onCreateDialogView() {
		LinearLayout view = new LinearLayout(context);
		view.setOrientation(LinearLayout.HORIZONTAL);
		view.setGravity(Gravity.CENTER_HORIZONTAL);

		//Create DatePicker.
		datePicker = new DatePicker(context);
		view.addView(datePicker);

		//Set date.
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTimeInMillis(selectedDate);
		datePicker.updateDate(
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH)
			); 
		return view;
	}

	@Override
	protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
		if (restorePersistedValue) {
			// Restore existing state
			selectedDate = this.getPersistedLong(DEFAULT_DATE);
		} else {
			// Set default state from the XML attribute
			selectedDate = (Long) defaultValue;
			persistLong(selectedDate);
		}
	}

	@Override
	protected Object onGetDefaultValue(TypedArray a, int index) {
		return null;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		final Parcelable superState = super.onSaveInstanceState();
		// Create instance of custom BaseSavedState
		final SavedState myState = new SavedState(superState);
		// Set the state's value with the class member that holds current setting value
		if (datePicker != null) {
			 Calendar calendar = Calendar.getInstance();
			 calendar.set(
					datePicker.getYear(),
					datePicker.getMonth(),
					datePicker.getDayOfMonth()
				);
			myState.date = calendar.getTimeInMillis();
		} else {
			myState.date = selectedDate;
		}
		return myState;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		// Cast state to custom BaseSavedState and pass to superclass
		SavedState myState = (SavedState) state;
		super.onRestoreInstanceState(myState.getSuperState());

		// Set this Preference's widget to reflect the restored state
		if (datePicker != null) {
			Calendar calendar = Calendar.getInstance(); 
			calendar.setTimeInMillis(myState.date);
			datePicker.updateDate(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH)
				); 
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


	/** View for picking date. */
	private DatePicker datePicker;

	/** Date by default in milliseconds. */
	private final long DEFAULT_DATE = new Date().getTime();

	/** Selected date in milliseconds. */
	private long selectedDate;

	/** Application context. */
	private Context context;

	/** Need or not show button "Clear". */
	private boolean showNeutralButton = true;


	/**
	 * Saving and restoring the Preference's state.
	 */
	private static class SavedState extends BaseSavedState {
		// Member that holds the setting's value
		// Change this data type to match the type saved by your Preference
		long date;

		public SavedState(Parcelable superState) {
			super(superState);
		}

		public SavedState(Parcel source) {
			super(source);
			// Get the current preference's value
			date = source.readLong();  // Change this to read the appropriate data type
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			super.writeToParcel(dest, flags);
			// Write the preference's value
			dest.writeLong(date);  // Change this to write the appropriate data type
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
