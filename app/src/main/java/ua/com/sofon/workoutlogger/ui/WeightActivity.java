package ua.com.sofon.workoutlogger.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.EditText;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.LogUtils;

/**
 * Activity shows weight history.
 * @author Dimowner
 */
public class WeightActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		etWeight = (EditText)findViewById(R.id.weight);
		int weight = prefs.getInt("weight", 0);
		etWeight.setText(String.valueOf(weight));
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_WEIGHT;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		int weight = 0;
		try {
			if (etWeight.getText().length() > 0) {
				weight = Integer.parseInt(etWeight.getText().toString());
			}
		} catch (NumberFormatException e) {
			Log.e(LOG_TAG, "", e);
		}

		SharedPreferences.Editor edit = prefs.edit();
		edit.putInt("weight", weight);
		edit.apply();
	}


	private SharedPreferences prefs;
	private EditText etWeight;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag("WeightsActivity");
}
