package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import ua.com.sofon.workoutlogger.database.WorkoutsDS;
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

		dataSource = new WorkoutsDS(this);
		try {
			dataSource.open();
		} catch (SQLException e) {
			LOGE(LOG_TAG, "", e);
		}

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView.setLayoutManager(mLayoutManager);

		listAdapter = new WorkoutsListAdapter(action, dataSource.getAll());
		listAdapter.setOnItemClickListener(new WorkoutsListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (action.equals(ACTION_SELECT)) {
					//TODO: send back selected workout.
					Log.v(LOG_TAG, "onItemSelected item = " + listAdapter.getItem(position).toString());
					Intent intent = new Intent();
					intent.putExtra(TrainingsActivity.EXTRAS_KEY_WORKOUT, listAdapter.getItem(position));
					setResult(RESULT_OK, intent);
					Log.v(LOG_TAG, "onItemClick action select");
					finish();
				} else {
					Intent intent = new Intent(WorkoutsActivity.this, WorkoutEditActivity.class);
					intent.setAction(WorkoutEditActivity.ACTION_VIEW);
					intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
					intent.putExtra(EXTRAS_KEY_WORKOUT, listAdapter.getItem(position));
					startActivityForResult(intent, REQUEST_VIEW_WORKOUT);
				}
			}
		});
		recyclerView.setAdapter(listAdapter);
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
	protected void onDestroy() {
		super.onDestroy();
		dataSource.close();
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
				Intent intent = new Intent(WorkoutsActivity.this, WorkoutEditActivity.class);
				intent.setAction(WorkoutEditActivity.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_WORKOUT);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data.hasExtra(EXTRAS_KEY_WORKOUT)) {
			Workout w = data.getParcelableExtra(EXTRAS_KEY_WORKOUT);
			switch (requestCode) {
				case REQUEST_ADD_WORKOUT:
					listAdapter.addItem(dataSource.insertItem(w));
					break;
//					case REQUEST_EDIT_WORKOUT:
//						//Update item in DB and in listAdaptper.);
//						listAdapter.updateEditingItem(w);
//						listAdapter.resetEditItemPos();
//						workoutDataSource.updateWorkout(w);
//						break;
				case REQUEST_VIEW_WORKOUT:
					String returnedAction = data.getAction();
					int itemPosition = -1;
					if (data.hasExtra(EXTRAS_KEY_ITEM_POSITION)) {
						itemPosition = data.getIntExtra(EXTRAS_KEY_ITEM_POSITION, -1);
					}
					if (returnedAction != null) {
						if (returnedAction.equals(WorkoutEditActivity.ACTION_EDIT)) {
							dataSource.updateItem(w);
							listAdapter.removeItem(itemPosition);
							listAdapter.addItem(itemPosition, w);

							String text = "Workout " + listAdapter.getItem(itemPosition).getName() + " was updated.";
							Snackbar.make(findViewById(R.id.coordinator_layout),
									text, Snackbar.LENGTH_LONG)
									.setAction("undo", new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//TODO: Undo update workout.
											Log.v(LOG_TAG, "Undo update workout");
										}
									}).show();
						} else if (returnedAction.equals(WorkoutEditActivity.ACTION_DELETE)) {
							if (itemPosition != -1 && listAdapter.removeItem(itemPosition)) {
								dataSource.deleteItem(w.getId());
								Snackbar.make(findViewById(R.id.coordinator_layout),
										"Workout was deleted successfully.", Snackbar.LENGTH_LONG)
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

	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_VIEW = "action_view";
	private final int REQUEST_ADD_WORKOUT = 101;
	private final int REQUEST_VIEW_WORKOUT = 102;
	private final int REQUEST_EDIT_WORKOUT = 103;

	public static final String EXTRAS_KEY_WORKOUT = "workout";
	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";

	private String action;
	private WorkoutsListAdapter listAdapter;
//	private WorkoutDataSource workoutDataSource;
	private WorkoutsDS dataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag("WorkoutsActivity");
}
