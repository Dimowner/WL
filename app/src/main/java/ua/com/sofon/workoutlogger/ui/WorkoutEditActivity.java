package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.parts.Workout;
import ua.com.sofon.workoutlogger.ui.widget.DividerItemDecoration;
import ua.com.sofon.workoutlogger.util.UIUtil;
import ua.com.sofon.workoutlogger.R;

import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Activity for view, add and edit workouts.
 * @author Dimowner.
 */
public class WorkoutEditActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_workout_edit);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		txtName = (EditText) findViewById(R.id.workout_edit_txt_name);

		fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WorkoutEditActivity.this, ExercisesActivity.class);
				intent.setAction(ExercisesActivity.ACTION_SELECT_MULTI);
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
		});

		workoutDataSource = new WorkoutDataSource(this);
		try {
			workoutDataSource.open();
		} catch (SQLException e) {
			LOGE(LOG_TAG, "", e);
		}

		if (getIntent().getAction() != null) {
			action = getIntent().getAction();
		}

		if (getIntent().hasExtra(WorkoutsActivity.EXTRAS_KEY_ITEM_POSITION)) {
			itemPosition = getIntent().getIntExtra(WorkoutsActivity.EXTRAS_KEY_ITEM_POSITION, -1);
		}

		switch (action) {
			case ACTION_VIEW:
				txtName.setEnabled(false);
				fab.setVisibility(View.GONE);
			case ACTION_EDIT:
				if (getIntent().hasExtra(WorkoutsActivity.EXTRAS_KEY_WORKOUT)) {
					mWorkout = getIntent().getParcelableExtra(WorkoutsActivity.EXTRAS_KEY_WORKOUT);
					txtName.setText(mWorkout.getName());
				}
				break;
			case ACTION_ADD:
			case ACTION_DEFAULT:
			default:
//					TODO: Make normal date for other locations
//					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
//					DateFormat df = DateFormat.getDateInstance();
//					df.setTimeZone(TimeZone.);
//					String curDateStr = sdf.format(new Date());
//					String curDateStr = Workout.dateFormat.format(new Date());
//					txtDate.setText(curDateStr);
//					txtName.setText("Workout");
		}

		if (mWorkout == null) {
			mWorkout = new Workout();
		}

		RecyclerView exeListView = (RecyclerView) findViewById(R.id.recycler_view);
		exeListView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		exeListView.setItemAnimator(new DefaultItemAnimator());
		exeListView.setLayoutManager(mLayoutManager);
		exeListView.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		listAdapter = new ExercisesListAdapter(action, mWorkout.getExerciseList());
		listAdapter.setOnItemClickListener(new ExercisesListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Log.v(LOG_TAG, "onItemClick pos = " + position);
			}
		});
		exeListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_edit, menu);
		MenuItem menuItemAccept = menu.findItem(R.id.action_accept);
		if (action.equals(ACTION_VIEW)) {
			menuItemAccept.setVisible(false);
		}
		if (action.equals(ACTION_ADD)) {
			menu.findItem(R.id.action_edit).setVisible(false);
			menu.findItem(R.id.action_delete).setVisible(false);
		}
		if (action.equals(ACTION_EDIT)) {
			menu.findItem(R.id.action_edit).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Intent intent = new Intent();
		intent.putExtra(WorkoutsActivity.EXTRAS_KEY_ITEM_POSITION, itemPosition);
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				if (txtName.getText().length() > 0) {
					intent.setAction(action);
					intent.putExtra(WorkoutsActivity.EXTRAS_KEY_WORKOUT, updateWorkout(mWorkout));
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Please add workout name!", Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.action_edit:
				action = ACTION_EDIT;
				txtName.setEnabled(true);
				fab.setVisibility(View.VISIBLE);
				invalidateOptionsMenu();
				return true;
			case R.id.action_delete:
				UIUtil.showWarningDialog(WorkoutEditActivity.this, "Delete workout?",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								intent.setAction(ACTION_DELETE);
								intent.putExtra(WorkoutsActivity.EXTRAS_KEY_WORKOUT, mWorkout);
								setResult(RESULT_OK, intent);
								finish();
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						}
				);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Update workout data from layout fields data.
	 * @param w Workout to update with new data.
	 * @return Workout.
	 */
	private Workout updateWorkout(Workout w) {
		w.setName(txtName.getText().toString());
		w.setExerciseList(listAdapter.getAllItems());
		return w;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_ADD:
					if (data.hasExtra(ExercisesActivity.EXTRAS_KEY_EXERCISE)) {
						Parcelable[] ps =  data.getParcelableArrayExtra(ExercisesActivity.EXTRAS_KEY_EXERCISE);
						Exercise[] exes = new Exercise[ps.length];
						System.arraycopy(ps, 0, exes, 0, ps.length);
						for (int i = 0; i < exes.length; i++) {
							listAdapter.addItem(exes[i]);
						}
						listAdapter.notifyDataSetChanged();
					}
					break;
				case REQUEST_CODE_REPLACE:
					break;
			}
		}
	}


	/** Action type by default. */
	public static final String ACTION_DEFAULT = "action_default";

	/** Action type - Add */
	public static final String ACTION_ADD = "add_workout";

	/** Action type - View */
	public static final String ACTION_VIEW = "view_workout";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_workout";

	/** Action type - Delete */
	public static final String ACTION_DELETE = "delete_workout";

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_VIEW = 2;
	private static final int REQUEST_CODE_REPLACE = 3;

	/** Action type. */
	private String action = ACTION_DEFAULT;

	private Workout mWorkout;
	private EditText txtName;
	private FloatingActionButton fab;

	/** Item position in the list */
	private int itemPosition;

	private ExercisesListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();

}
