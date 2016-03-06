package ua.com.sofon.workoutlogger.ui;

import java.util.Calendar;
import java.sql.SQLException;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.BodyWeightDS;
import ua.com.sofon.workoutlogger.parts.BodyWeight;
import ua.com.sofon.workoutlogger.ui.widget.DatePickerView;
import ua.com.sofon.workoutlogger.ui.widget.TimePickerView;
import ua.com.sofon.workoutlogger.util.LogUtils;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Dialog shows information about application.
 * @author Dimowner
 */
public class WeightAddDialog extends DialogFragment {

	public WeightAddDialog() {
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
				Context.LAYOUT_INFLATER_SERVICE);

		View body	= layoutInflater.inflate(R.layout.dialog_add_weight, null);
		txtDate		= (DatePickerView) body.findViewById(R.id.add_weight_date);
		txtTime		= (TimePickerView) body.findViewById(R.id.add_weight_time);
		txtWeight	= (EditText) body.findViewById(R.id.add_weight_weight_txt);
		txtFatIndex = (EditText) body.findViewById(R.id.add_weight_fat_index_txt);
		txtComment	= (EditText) body.findViewById(R.id.add_weight_comment_txt);

		return new AlertDialog.Builder(getActivity())
				.setView(body)
				.setPositiveButton(R.string.btn_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								writeWeightIntoDB();
								if (onDialogButtonClickListener != null) {
									onDialogButtonClickListener.onPositiveButtonClicked();
								}
								dialog.dismiss();
							}
						}
				)
				.setNegativeButton(R.string.btn_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								if (onDialogButtonClickListener != null) {
									onDialogButtonClickListener.onNegativeButtonClicked();
								}
								dialog.dismiss();
							}
						}
				)
				.create();
	}

	private void writeWeightIntoDB() {
		Log.v("WeightAddDialog", "writeWeightIntoDB");
		if (txtDate.getCalendar() != null && txtTime.getCalendar() != null
				&& txtWeight.getText().length() > 0) {
			BodyWeightDS bodyWeightDS = new BodyWeightDS(getActivity());
			try {
				bodyWeightDS.open();
			} catch (SQLException e) {
				LOGE(LOG_TAG, "", e);
			}

			BodyWeight bodyWeight = new BodyWeight();

			Calendar date = txtDate.getCalendar();
			Calendar time = txtTime.getCalendar();
			time.set(
					date.get(Calendar.YEAR),
					date.get(Calendar.MONTH),
					date.get(Calendar.DAY_OF_MONTH)
			);
			Log.v(LOG_TAG, "DateTime = " + time.getTime());
			bodyWeight.setDateTime(time.getTime());

			try {
				bodyWeight.setWeight(Float.parseFloat(txtWeight.getText().toString()));
			} catch (NumberFormatException e) {
				LOGE(LOG_TAG, "", e);
			}
			try {
				if (txtFatIndex.getText().length() > 0) {
					bodyWeight.setFatIndex(Float.parseFloat(txtFatIndex.getText().toString()));
				}
			} catch (NumberFormatException e) {
				LOGE(LOG_TAG, "", e);
			}
			if (txtComment.getText().length() > 0) {
				bodyWeight.setComment(txtComment.getText().toString());
			}
			Log.v(LOG_TAG, "bodyWeight = " + bodyWeight.toString());
			bodyWeightDS.insertItem(bodyWeight);
			bodyWeightDS.close();
		} else {
			Log.v(LOG_TAG, "Not filled important fields");
		}
	}

	public void setOnDialogButtonClickListener(OnDialogButtonClickListener onDialogButtonClickListener) {
		this.onDialogButtonClickListener = onDialogButtonClickListener;
	}

	DatePickerView txtDate;
	TimePickerView txtTime;
	EditText txtWeight;
	EditText txtFatIndex;
	EditText txtComment;

	OnDialogButtonClickListener onDialogButtonClickListener;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());


	public interface OnDialogButtonClickListener {
		void onPositiveButtonClicked();
		void onNegativeButtonClicked();
	}
}
