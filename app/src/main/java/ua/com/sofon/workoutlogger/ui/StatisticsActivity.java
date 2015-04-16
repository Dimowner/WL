package ua.com.sofon.workoutlogger.ui;

import android.os.Bundle;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.LogUtils;

/**
 * Activity shows statistics from all workouts.
 * @author Dimowner
 */
public class StatisticsActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_STATISTICS;
	}

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag("StatisticsActivity");
}
