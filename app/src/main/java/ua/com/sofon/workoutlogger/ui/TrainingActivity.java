package ua.com.sofon.workoutlogger.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.LogUtils;

/**
 * Activity shows list of planned and finished workouts.
 * @author Dimowner
 */
public class TrainingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TrainingActivity.this, WorkoutsActivity.class);
				intent.setAction(WorkoutsActivity.ACTION_SELECT);
				startActivityForResult(intent, REQUEST_SELECT_WORKOUT);
			}
		});
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_TRAINING;
	}


	private final int REQUEST_SELECT_WORKOUT = 201;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
