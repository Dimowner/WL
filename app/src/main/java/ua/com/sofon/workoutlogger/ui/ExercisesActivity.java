package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.ui.widget.DividerItemDecoration;
import ua.com.sofon.workoutlogger.ui.widget.ExercisesListAdapter;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.R;

/**
 * Activity shows all exercises.
 * @author Dimowner
 */
public class ExercisesActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercises_list);

		String action = getIntent().getAction();
		if (action == null) {
			action = ACTION_VIEW;
		}

		workoutDataSource = new WorkoutDataSource(this);
		try {
			workoutDataSource.open();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "", e);
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
				Intent intent = new Intent(ExercisesActivity.this, EditExerciseActivity.class);
				intent.setAction(EditExerciseActivity.ACTION_VIEW);
				intent.putExtra(EXTRAS_KEY_EXERCISE, exeAdapter.getItem(position));
				startActivityForResult(intent, REQUEST_EDIT_EXERCISE);
			}
		});
		exeListView.setAdapter(exeAdapter);

//		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//		fab.attachToRecyclerView(exeListView);
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_EXERCISES;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_add_item, menu);
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
			Log.v(LOG_TAG, "Action = " + data.getAction());
			switch (requestCode) {
				case REQUEST_ADD_EXERCISE:
				case REQUEST_EDIT_EXERCISE:
					exeAdapter.clear();
					exeAdapter.addExercises(workoutDataSource.getAllExercises());
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
	private ExercisesListAdapter exeAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
