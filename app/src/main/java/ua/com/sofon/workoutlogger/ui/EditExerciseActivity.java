package ua.com.sofon.workoutlogger.ui;

import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBarActivity;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.R;

/**
 * Activity for add and edit exercises.
 * @author Dimowner.
 */
public class EditExerciseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_edit);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		exeName = (EditText) findViewById(R.id.txt_exercise_name);
		exeDescription = (EditText) findViewById(R.id.txt_exercise_description);

		if (getIntent().getAction() != null) {
			action = getIntent().getAction();
		}

		Bundle extras = getIntent().getExtras();
		switch (action) {
			case ACTION_VIEW:
				exeName.setEnabled(false);
				exeDescription.setEnabled(false);
			case ACTION_EDIT:
				if (extras.containsKey(ExercisesActivity.EXTRAS_KEY_EXERCISE)) {
					editingExercise = (Exercise) extras.getSerializable(ExercisesActivity.EXTRAS_KEY_EXERCISE);
					exeName.setText(editingExercise.getName());
					exeDescription.setText(editingExercise.getDescription());
				}
				break;
			case ACTION_ADD:
			case ACTION_DEFAULT:
			default:
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_exer_pos, menu);
		if (action.equals(ACTION_VIEW)) {
			menu.findItem(R.id.action_accept).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				if (exeName.getText().length() > 0) {
					Intent intent = new Intent();
					if (action.equals(ACTION_ADD)) {
						Exercise e = new Exercise();
						e.setName(exeName.getText().toString());
						e.setDescription(exeDescription.getText().toString());
						intent.putExtra(ExercisesActivity.EXTRAS_KEY_EXERCISE, e);
					} else if (action.equals(ACTION_EDIT)) {
						editingExercise.setName(exeName.getText().toString());
						editingExercise.setDescription(exeDescription.getText().toString());
						intent.putExtra(ExercisesActivity.EXTRAS_KEY_EXERCISE, editingExercise);
					}
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Can't add exercise with empty name!",
							Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	/** Action type by default. */
	public static final String ACTION_DEFAULT = "action_default";

	/** Action type - View */
	public static final String ACTION_VIEW = "view_exercise";

	/** Action type - Add */
	public static final String ACTION_ADD = "add_exercise";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_exercise";

	/** Action type. */
	private String action = ACTION_DEFAULT;

	/** Exercise to edit. */
	private Exercise editingExercise;

	/** Text field contains exercise name. */
	private EditText exeName;

	/** Text field contains exercise description. */
	private EditText exeDescription;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
