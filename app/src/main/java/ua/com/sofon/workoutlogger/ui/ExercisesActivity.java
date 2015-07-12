package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.ui.widget.DividerItemDecoration;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.R;
import  static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Activity shows all exercises.
 * @author Dimowner
 */
public class ExercisesActivity extends BaseActivity {

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

		RecyclerView exeListView = (RecyclerView) findViewById(R.id.recycler_view);
		exeListView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		exeListView.setItemAnimator(new DefaultItemAnimator());
		exeListView.setLayoutManager(mLayoutManager);
		exeListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

		listAdapter = new ExercisesListAdapter(action, workoutDataSource.getAllExercises());
		listAdapter.setOnItemClickListener(new ExercisesListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (action.equals(ACTION_SELECT)) {
					Intent intent = new Intent();
					intent.putExtra(EXTRAS_KEY_EXERCISE, listAdapter.getItem(position));
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Intent intent = new Intent(ExercisesActivity.this, ExerciseEditActivity.class);
					intent.setAction(ExerciseEditActivity.ACTION_VIEW);
					intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
					intent.putExtra(EXTRAS_KEY_EXERCISE, listAdapter.getItem(position));
					startActivityForResult(intent, REQUEST_VIEW_EXERCISE);
				}
			}
		});
		exeListView.setAdapter(listAdapter);
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_EXERCISES;
	}

	@Override
	protected void setupNavDrawer() {
		if (action.equals(ACTION_SELECT_MULTI)) {
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
		getMenuInflater().inflate(R.menu.menu_add_item, menu);
		MenuItem addItem = menu.findItem(R.id.action_add);
		if (addItem != null) {
			addItem.setTitle(R.string.menu_item_exer_add);
		}
		MenuItem acceptItem = menu.findItem(R.id.action_accept);
		if (acceptItem != null ) {
			if (action.equals(ACTION_SELECT_MULTI)) {
				acceptItem.setVisible(true);
			} else {
				acceptItem.setVisible(false);
			}
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_add:
				intent = new Intent(ExercisesActivity.this, ExerciseEditActivity.class);
				intent.setAction(ExerciseEditActivity.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_EXERCISE);
				return true;
			case R.id.action_accept:
				intent = new Intent();
				intent.putExtra(EXTRAS_KEY_EXERCISE, listAdapter.getCheckedItems());
				setResult(RESULT_OK, intent);
				finish();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data.hasExtra(EXTRAS_KEY_EXERCISE)) {
			Exercise exercise = data.getParcelableExtra(EXTRAS_KEY_EXERCISE);
			switch (requestCode) {
				case REQUEST_ADD_EXERCISE:
					workoutDataSource.createExercise(exercise.getName(), exercise.getDescription());
					listAdapter.addItem(exercise);
					//					}
					break;
				case REQUEST_VIEW_EXERCISE:
					//If result returned then its probably exercise was edited or deleted.
					String returnedAction = data.getAction();
					//get back selected item position.
					int itemPosition = -1;
					if (data.hasExtra(EXTRAS_KEY_ITEM_POSITION)) {
						itemPosition = data.getIntExtra(EXTRAS_KEY_ITEM_POSITION, -1);
					}
					if (returnedAction != null && itemPosition != -1) {
						if (returnedAction.equals(ExerciseEditActivity.ACTION_EDIT)) {
							workoutDataSource.updateExercise(exercise);
							listAdapter.removeItem(itemPosition);
							listAdapter.addItem(itemPosition, exercise);
//									(Exercise) data.getParcelableExtra(EXTRAS_KEY_EXERCISE));
							String text = "Exercise " + listAdapter.getItem(itemPosition).getName() + " was updated.";
							Snackbar.make(findViewById(R.id.coordinator_layout),
									text, Snackbar.LENGTH_LONG)
									.setAction("undo", new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//TODO: Undo update exercise.
											Log.v(LOG_TAG, "Undo update exercise");
										}
									}).show();
						} else if (returnedAction.equals(ExerciseEditActivity.ACTION_DELETE)) {
							workoutDataSource.deleteExersice(exercise);
							String text = "Exercise " + listAdapter.getItem(itemPosition).getName() + " was deleted.";
							Snackbar.make(findViewById(R.id.coordinator_layout),
									text, Snackbar.LENGTH_LONG)
									.setAction("undo", new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											//TODO: Undo delete exercise.
											Log.v(LOG_TAG, "Undo delete exercise");
										}
									}).show();
							listAdapter.removeItem(itemPosition);
						}
					}
					break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		workoutDataSource.close();
	}


	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_SELECT_MULTI = "action_select_multi";
	public static final String ACTION_VIEW = "action_view";
	public static final String EXTRAS_KEY_EXERCISE = "exercise";
	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";
	private final int REQUEST_ADD_EXERCISE = 101;
	private final int REQUEST_VIEW_EXERCISE = 102;
	private final int REQUEST_EDIT_EXERCISE = 103;
	private String action;
	private ExercisesListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
