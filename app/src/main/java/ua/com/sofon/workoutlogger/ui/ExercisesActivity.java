package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
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

		RecyclerView exeListView = (RecyclerView) findViewById(R.id.my_recycler_view);
		exeListView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		exeListView.setItemAnimator(new DefaultItemAnimator());
		exeListView.setLayoutManager(mLayoutManager);
		exeListView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

		exeAdapter = new ExercisesListAdapter(action, workoutDataSource.getAllExercises());
		exeAdapter.setOnItemClickListener(new ExercisesListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Log.v(LOG_TAG, "onItemClick pos = " + position);
				Intent intent = new Intent(ExercisesActivity.this, EditExerciseActivity.class);
				intent.setAction(EditExerciseActivity.ACTION_VIEW);
				intent.putExtra(EXTRAS_KEY_EXERCISE, exeAdapter.getItem(position));
				startActivityForResult(intent, REQUEST_VIEW_EXERCISE);
			}
		});
		exeListView.setAdapter(exeAdapter);
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_EXERCISES;
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
		getMenuInflater().inflate(R.menu.menu_add_item, menu);
		MenuItem addItem = menu.findItem(R.id.action_add);
		if (addItem != null) {
			addItem.setTitle(R.string.menu_item_exer_add);
		}
		MenuItem acceptItem = menu.findItem(R.id.action_accept);
		if (acceptItem != null ) {
			if (action.equals(ACTION_SELECT)) {
				acceptItem.setVisible(true);
			} else {
				acceptItem.setVisible(false);
			}
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
				Intent intent = new Intent(ExercisesActivity.this, EditExerciseActivity.class);
				intent.setAction(EditExerciseActivity.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_EXERCISE);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_ADD_EXERCISE:
				case REQUEST_EDIT_EXERCISE:
					exeAdapter.clear();
					exeAdapter.addItems(workoutDataSource.getAllExercises());
					break;
				case REQUEST_VIEW_EXERCISE:
					Log.v(LOG_TAG, "View result");
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
	public static final String ACTION_VIEW = "action_view";
	public static final String EXTRAS_KEY_EXERCISE = "exercise";
	private final int REQUEST_ADD_EXERCISE = 101;
	private final int REQUEST_VIEW_EXERCISE = 102;
	private final int REQUEST_EDIT_EXERCISE = 103;
	private String action;
	private ExercisesListAdapter exeAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
