package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.SQLiteHelper;
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

		loadTrainings();
		updateShowingData();

		listAdapter = new TrainingsListAdapter(this, action, trainedWorkouts);
		listAdapter.setOnItemClickListener(new TrainingsListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(TrainingsActivity.this, TrainingEditActivity.class);
				intent.setAction(TrainingEditActivity.ACTION_VIEW);
				intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
				intent.putExtra(EXTRAS_KEY_WORKOUT, listAdapter.getItem(position));
				startActivityForResult(intent, REQUEST_VIEW_WORKOUT);
			}
		});
		listAdapter.setOnItemLongClickListener(new BaseListAdapter.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(View view, int position) {
				Log.v(LOG_TAG, "onItemLongClick");
				return false;
			}
		});
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
			switch (requestCode) {
				case REQUEST_SELECT_WORKOUT:
					Workout w = data.getParcelableExtra(EXTRAS_KEY_WORKOUT);
					listAdapter.addItem(0, dataSource.insertItem(new TrainedWorkout(w)));
					break;
				case REQUEST_VIEW_WORKOUT:
					TrainedWorkout tw = data.getParcelableExtra(EXTRAS_KEY_WORKOUT);
					String returnedAction = data.getAction();
					int itemPosition = -1;
					if (data.hasExtra(EXTRAS_KEY_ITEM_POSITION)) {
						itemPosition = data.getIntExtra(EXTRAS_KEY_ITEM_POSITION, -1);
					}
					if (returnedAction != null) {
						if (returnedAction.equals(TrainingEditActivity.ACTION_EDIT)) {
							dataSource.updateItem(tw);
							listAdapter.removeItem(itemPosition);
							listAdapter.addItem(itemPosition, tw);

							String text = "TrainedWorkout " + listAdapter.getItem(itemPosition).getName() + " was updated.";
							Snackbar.make(findViewById(R.id.coordinator_layout),
									text, Snackbar.LENGTH_LONG)
									.setAction("undo", new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//TODO: Undo update workout.
											Log.v(LOG_TAG, "Undo update workout");
										}
									}).show();
						} else if (returnedAction.equals(TrainingEditActivity.ACTION_DELETE)) {
							if (itemPosition != -1 && listAdapter.removeItem(itemPosition)) {
								dataSource.deleteItem(tw.getId());
								Snackbar.make(findViewById(R.id.coordinator_layout),
										"TrainedWorkout was deleted successfully.", Snackbar.LENGTH_LONG)
										.setAction("undo", new View.OnClickListener() {
											@Override
											public void onClick(View v) {
												//TODO: UNDO WORKOUT.
												Log.v(LOG_TAG, "Undo workout");
											}
										}).show();
							} else {
								final Snackbar snackBar = Snackbar.make(findViewById(R.id.coordinator_layout),
										"Unsuccessful deletion", Snackbar.LENGTH_LONG);
								snackBar.setAction("dismiss", new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										snackBar.dismiss();
									}
								}).show();
							}
							listAdapter.notifyDataSetChanged();
						}
					}
					break;
			}
			listAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Load all trainings data from database.
	 */
	public void loadTrainings() {
		if (dataSource != null) {
			trainedWorkouts = dataSource.getItems(
					" 1=1 ORDER BY " + SQLiteHelper.COLUMN_TW_PLAN_DATE + " DESC");
		} else {
			LOGE(LOG_TAG, "dataSource is null");
		}
	}

	/**
	 * Update graph and weights data stored in activity.
	 */
	public void updateShowingData() {
		if (trainedWorkouts != null && trainedWorkouts.size() > 0) {
			//TODO: DO SOMETHING
		}
	}


	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_VIEW = "action_view";

	private final int REQUEST_SELECT_WORKOUT = 201;
	private final int REQUEST_VIEW_WORKOUT = 202;

	public static final String EXTRAS_KEY_WORKOUT = "workout";
	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";

	private String action;
	private TrainingsListAdapter listAdapter;
	private TrainedWorkoutsDS dataSource;
	private ArrayList<TrainedWorkout> trainedWorkouts;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
