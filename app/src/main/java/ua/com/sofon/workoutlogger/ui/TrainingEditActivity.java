package ua.com.sofon.workoutlogger.ui;

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
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.TrainedWorkout;
import ua.com.sofon.workoutlogger.ui.widget.DividerItemDecoration;
import ua.com.sofon.workoutlogger.util.LogUtils;
import ua.com.sofon.workoutlogger.util.UIUtil;

/**
 * Activity for add and edit exercises.
 * @author Dimowner.
 */
public class TrainingEditActivity extends AppCompatActivity {

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
				Intent intent = new Intent(TrainingEditActivity.this, ExercisesActivity.class);
				intent.setAction(ExercisesActivity.ACTION_SELECT_MULTI);
				int[] exeIds = new int[mWorkout.getExercisesCount()];
				for (int i = 0; i < mWorkout.getExercisesCount(); i++) {
					exeIds[i] = mWorkout.getTrainedExe(i).getParentExeID();
				}
				if (exeIds.length > 0) {
					intent.putExtra(ExercisesActivity.EXTRAS_KEY_EXE_IDS, exeIds);
				}
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
		});

		if (getIntent().getAction() != null) {
			action = getIntent().getAction();
			Log.v(LOG_TAG, "action = " + action);
		}

		if (getIntent().hasExtra(WorkoutsActivity.EXTRAS_KEY_ITEM_POSITION)) {
			itemPosition = getIntent().getIntExtra(WorkoutsActivity.EXTRAS_KEY_ITEM_POSITION, -1);
		}

		switch (action) {
			case ACTION_VIEW:
				txtName.setEnabled(false);
				fab.setVisibility(View.GONE);
			case ACTION_EDIT:
				if (getIntent().hasExtra(TrainingsActivity.EXTRAS_KEY_WORKOUT)) {
					mWorkout = getIntent().getParcelableExtra(TrainingsActivity.EXTRAS_KEY_WORKOUT);
					txtName.setText(mWorkout.getName());
				}
				break;
			case ACTION_ADD:
			case ACTION_DEFAULT:
			default:
		}

		if (mWorkout == null) {
			mWorkout = new TrainedWorkout();
		}

		RecyclerView exeListView = (RecyclerView) findViewById(R.id.recycler_view);
		exeListView.setHasFixedSize(true);

		// use a linear layout manager
		RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
		exeListView.setItemAnimator(new DefaultItemAnimator());
		exeListView.setLayoutManager(mLayoutManager);
		exeListView.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		listAdapter = new TrainedExercisesListAdapter(action, mWorkout.getExerciseList());
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
					intent.putExtra(TrainingsActivity.EXTRAS_KEY_WORKOUT, updateWorkout(mWorkout));
					Log.v(LOG_TAG, "accept action = " + action + " workout  = " + mWorkout.toString());
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
				UIUtil.showWarningDialog(TrainingEditActivity.this, "Delete workout?",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Log.v(LOG_TAG, "delete workout w = " + mWorkout.toString());
								intent.setAction(ACTION_DELETE);
								intent.putExtra(TrainingsActivity.EXTRAS_KEY_WORKOUT, mWorkout);
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
	private TrainedWorkout updateWorkout(TrainedWorkout w) {
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
					if (data.hasExtra(ExercisesActivity.EXTRAS_KEY_EXERCISES)) {
						Parcelable[] ps =  data.getParcelableArrayExtra(ExercisesActivity.EXTRAS_KEY_EXERCISES);
						Exercise[] exes = new Exercise[ps.length];
						System.arraycopy(ps, 0, exes, 0, ps.length);

						for (int i = listAdapter.getItemCount() - 1; i >= 0; i--) {
							boolean isDeleted = true;
							for (int j = 0; j < exes.length; j++) {
								if (exes[j] != null
										&& listAdapter.getItem(i).getParentExeID() == exes[j].getId()) {
									exes[j] = null;
									isDeleted = false;
								}
							}
							if (isDeleted) {
								listAdapter.removeItem(i);
							}
						}

						for (int i = 0; i < exes.length; i++) {
							if (exes[i] != null) {
								listAdapter.addItem(new TrainedExercise(exes[i]));
							}
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
	public static final String ACTION_ADD = "add_training";

	/** Action type - View */
	public static final String ACTION_VIEW = "view_training";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_training";

	/** Action type - Delete */
	public static final String ACTION_DELETE = "delete_training";

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_VIEW = 2;
	private static final int REQUEST_CODE_REPLACE = 3;


	/** Action type. */
	private String action = ACTION_DEFAULT;

	private TrainedWorkout mWorkout;
	private EditText txtName;
	private FloatingActionButton fab;

	/** Item position in the list */
	private int itemPosition;

	private TrainedExercisesListAdapter listAdapter;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
