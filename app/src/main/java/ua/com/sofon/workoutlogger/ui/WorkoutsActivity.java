package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.R;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Activity shows all workouts.
 * @author Dimowner
 */
public class WorkoutsActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);

		action = getIntent().getAction();
		if (action == null) {
			action = ACTION_VIEW;
		}

		workoutDataSource = new WorkoutDataSource(this);
		try {
			workoutDataSource.open();
		} catch (SQLException e) {
			LOGE(LOG_TAG, "", e);
		}

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
		recyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(mLayoutManager);
//		exeListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

		adapter = new WorkoutsListAdapter(action, workoutDataSource.getAllWorkouts());
		adapter.setOnItemClickListener(new WorkoutsListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (action.equals(ACTION_SELECT)) {
					//TODO: send back selected workout.
				} else {
					Intent intent = new Intent(WorkoutsActivity.this, EditWorkoutActivity.class);
					intent.setAction(EditExerciseActivity.ACTION_VIEW);
					intent.putExtra(EXTRAS_KEY_WORKOUT, adapter.getItem(position));
					startActivityForResult(intent, REQUEST_VIEW_WORKOUT);
				}
			}
		});
		recyclerView.setAdapter(adapter);
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_WORKOUTS;
	}

	@Override
	protected void setupNavDrawer() {
		if (action.equals(ACTION_SELECT)) {
			//If action select don't init navigation drawer and lock it.
			mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
			mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			if (getSupportActionBar() != null) {
				getSupportActionBar().setDisplayHomeAsUpEnabled(true);
			}
		} else {
			super.setupNavDrawer();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_add_item, menu);
		MenuItem addItem = menu.findItem(R.id.action_add);
		if (addItem != null) {
			addItem.setTitle(R.string.menu_item_workout_add);
		}
		MenuItem acceptItem = menu.findItem(R.id.action_accept);
		if (acceptItem != null ) {
			acceptItem.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_add:
				Intent intent = new Intent(WorkoutsActivity.this, EditWorkoutActivity.class);
				intent.setAction(EditWorkoutActivity.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_WORKOUT);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data.hasExtra(EXTRAS_KEY_WORKOUT)) {
				Workout w = (Workout) data.getSerializableExtra(EXTRAS_KEY_WORKOUT);
				switch (requestCode) {
					case REQUEST_ADD_WORKOUT:
						adapter.addItem(
								workoutDataSource.createWorkout(
										w.getName(),
										w.getDate(),
										w.getWeight(),
										w.getDuration(),
										w.getComment(),
										w.getState(),
										w.getExerciseList()
								)
						);
						break;
//					case REQUEST_EDIT_WORKOUT:
//						//Update item in DB and in listAdaptper.);
//						listAdapter.updateEditingItem(w);
//						listAdapter.resetEditItemPos();
//						workoutDataSource.updateWorkout(w);
//						break;
//					case REQUEST_VIEW_WORKOUT:
//						//Probably in Posform VIEW_MODE RESULT_OK only when delete workout requested.
//						if (listAdapter.removeItem(w.getId())) {
//							workoutDataSource.deleteWorkout(w);
//							Toast.makeText(this, "Workout was deleted successfully.",
//									Toast.LENGTH_LONG).show();
//						} else {
//							LOGE(LOG_TAG, "Unsuccessful deletion");
//						}
//						listAdapter.notifyDataSetChanged();
//						break;
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_VIEW = "action_view";
	private final int REQUEST_ADD_WORKOUT = 101;
	private final int REQUEST_VIEW_WORKOUT = 102;
	private final int REQUEST_EDIT_WORKOUT = 103;

//	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";
	public static final String EXTRAS_KEY_WORKOUT = "workout";

	private String action;
	private WorkoutsListAdapter adapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag("WorkoutsActivity");
}
