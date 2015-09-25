package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.TrainedWorkoutsDS;
import ua.com.sofon.workoutlogger.parts.TrainedWorkout;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.util.LogUtils;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Activity shows list of planned and finished workouts.
 * @author Dimowner
 */
public class TrainingsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.WorkoutTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_training);

		action = getIntent().getAction();
		if (action == null) {
			action = ACTION_VIEW;
		}

		dataSource = new TrainedWorkoutsDS(this);
		try {
			dataSource.open();
		} catch (SQLException e) {
			LOGE(LOG_TAG, "", e);
		}

		//Init RecyclerView
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.training_recycler_view);
		recyclerView.setHasFixedSize(true);
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(mLayoutManager);

		loadPlannedWrokouts();
		updateShowingData();

		listAdapter = new TrainingsListAdapter(action, plannedWorkouts);
//		listAdapter.setOnItemClickListener(new TrainingsListAdapter.OnItemClickListener() {
//			@Override
//			public void onItemClick(View view, int position) {
//				if (action.equals(ACTION_SELECT)) {
//					//TODO: send back selected workout.
//					Log.v(LOG_TAG, "onItemClick action select");
//				} else {
//					Intent intent = new Intent(WorkoutsActivity.this, WorkoutEditActivity.class);
//					intent.setAction(WorkoutEditActivity.ACTION_VIEW);
//					intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
//					intent.putExtra(EXTRAS_KEY_WORKOUT, listAdapter.getItem(position));
//					startActivityForResult(intent, REQUEST_VIEW_WORKOUT);
//				}
//			}
//		});
		recyclerView.setAdapter(listAdapter);

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TrainingsActivity.this, WorkoutsActivity.class);
				intent.setAction(WorkoutsActivity.ACTION_SELECT);
				startActivityForResult(intent, REQUEST_SELECT_WORKOUT);
			}
		});
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_TRAINING;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		dataSource.close();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data.hasExtra(EXTRAS_KEY_WORKOUT)) {
			Workout w = data.getParcelableExtra(EXTRAS_KEY_WORKOUT);
			switch (requestCode) {
				case REQUEST_SELECT_WORKOUT:
					listAdapter.addItem(
							dataSource.insertItem(
									new TrainedWorkout(
										TrainedWorkout.NO_ID,
										w.getName(),
										w.getDescription(),
										new Date(),
										null,
										TrainedWorkout.DURATION_NOT_SPECIFIED,
										TrainedWorkout.STATE_DEFAULT,
										w.getExerciseList()
									)
							)
					);
					break;
			}
		}
	}

	/**
	 * Load all planned workouts data from database.
	 */
	public void loadPlannedWrokouts() {
		if (dataSource != null) {
//			try {
//				dataSource.open();
//			} catch (SQLException e) {
//				LOGE(LOG_TAG, "", e);
//			}
			plannedWorkouts = dataSource.getAll();
//			dataSource.close();
		} else {
			LOGE(LOG_TAG, "dataSource is null");
		}
	}

	/**
	 * Update graph and weights data stored in activity.
	 */
	public void updateShowingData() {
		if (plannedWorkouts != null && plannedWorkouts.size() > 0) {
			//TODO: DO SOMETHING
		}
	}


	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_VIEW = "action_view";

	private final int REQUEST_SELECT_WORKOUT = 201;

	public static final String EXTRAS_KEY_WORKOUT = "workout";
	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";

	private String action;
	private TrainingsListAdapter listAdapter;
	private TrainedWorkoutsDS dataSource;
	private ArrayList<TrainedWorkout> plannedWorkouts;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
