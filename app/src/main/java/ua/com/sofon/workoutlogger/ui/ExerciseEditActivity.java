package ua.com.sofon.workoutlogger.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.ui.widget.MultiSelectTextView;
import ua.com.sofon.workoutlogger.util.UIUtil;

/**
 * Activity for add and edit exercises.
 * @author Dimowner.
 */
public class ExerciseEditActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_edit);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		txtName = (EditText) findViewById(R.id.exercise_name_txt);
		txtName.clearFocus();
		txtDescription = (EditText) findViewById(R.id.exercise_description_txt);
		txtMuscleGroups = (MultiSelectTextView) findViewById(R.id.exercise_muscle_groups);

		//Init muscle groups dialog.
		String[] names = getResources().getStringArray(R.array.exercises_types_array);
		int[] ids = getResources().getIntArray(R.array.exercises_num_types_array);
		txtMuscleGroups.setData(names, ids);
		txtMuscleGroups.setTitle(getString(R.string.title_muscle_groups));
		txtMuscleGroups.showNeutralButton(true);
		txtMuscleGroups.setOnItemsSelectedListener(new MultiSelectTextView.OnItemsSelectedListener() {
			@Override
			public void OnItemsSelected(int[] ids, String[] names) {
				mExercise.setGroups(ids);
			}
		});

		if (getIntent().getAction() != null) {
			action = getIntent().getAction();
		}
		if (getIntent().hasExtra(ExercisesActivity.EXTRAS_KEY_ITEM_POSITION)) {
			itemPosition = getIntent().getIntExtra(ExercisesActivity.EXTRAS_KEY_ITEM_POSITION, -1);
		}

		Bundle extras = getIntent().getExtras();
		switch (action) {
			case ACTION_VIEW:
				txtName.setEnabled(false);
				txtDescription.setEnabled(false);
				txtMuscleGroups.setEnabled(false);
			case ACTION_EDIT:
				if (extras.containsKey(ExercisesActivity.EXTRAS_KEY_EXERCISES)) {
					mExercise = extras.getParcelable(ExercisesActivity.EXTRAS_KEY_EXERCISES);
					if (mExercise != null) {
						txtName.setText(mExercise.getName());
						txtDescription.setText(mExercise.getDescription());
						txtMuscleGroups.setText(mExercise.getMuscleGroupsNames(getApplicationContext()));
					}
				}
				break;
			case ACTION_ADD:
			default:
		}

		if (mExercise == null) {
			mExercise = new Exercise();
		}
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
		intent.putExtra(ExercisesActivity.EXTRAS_KEY_ITEM_POSITION, itemPosition);
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				if (txtName.getText().length() > 0) {
					intent.setAction(action);
					intent.putExtra(ExercisesActivity.EXTRAS_KEY_EXERCISES, updateExercise(mExercise));
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Can't add exercise with empty name!",
							Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.action_edit:
				action = ACTION_EDIT;
				txtName.setEnabled(true);
				txtDescription.setEnabled(true);
				txtMuscleGroups.setEnabled(true);

				invalidateOptionsMenu();
				return true;
			case R.id.action_delete:
				UIUtil.showWarningDialog(this, R.string.exer_delete_this_exe,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								intent.setAction(ACTION_DELETE);
								intent.putExtra(ExercisesActivity.EXTRAS_KEY_EXERCISES, mExercise);
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
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Update exercise data from layout fields data.
	 * @param e Exercise to update with new data.
	 * @return Exercise.
	 */
	private Exercise updateExercise(Exercise e) {
		e.setName(txtName.getText().toString());
		e.setDescription(txtDescription.getText().toString());
		return e;
	}

	/** Action type by default. */
	public static final String ACTION_DEFAULT = "action_default";

	/** Action type - View */
	public static final String ACTION_VIEW = "view_exercise";

	/** Action type - Add */
	public static final String ACTION_ADD = "add_exercise";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_exercise";

	/** Action type - Delete */
	public static final String ACTION_DELETE = "delete_exercise";

	/** Action type. */
	private String action = ACTION_VIEW;

	/** Exercise to edit, delete or add. */
	private Exercise mExercise;

	/** Text field contains exercise name. */
	private EditText txtName;

	/** Text field contains exercise description. */
	private EditText txtDescription;

	/** Text field contains exercise muscle groups. */
	private MultiSelectTextView txtMuscleGroups;

	/** Item position in the list */
	private int itemPosition;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
