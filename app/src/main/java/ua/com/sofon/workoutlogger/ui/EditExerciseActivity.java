package ua.com.sofon.workoutlogger.ui;

import java.sql.SQLException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.R;

/**
 * Activity for add and edit exercises.
 * @author Dimowner.
 */
public class EditExerciseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exercise_edit);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}

		exeName = (EditText) findViewById(R.id.exercise_name_txt);
		exeDescription = (EditText) findViewById(R.id.exercise_description_txt);

		if (getIntent().getAction() != null) {
			action = getIntent().getAction();
		}

		workoutDataSource = new WorkoutDataSource(this);
		try {
			workoutDataSource.open();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "", e);
		}

		Bundle extras = getIntent().getExtras();
		switch (action) {
			case ACTION_VIEW:
				exeName.setEnabled(false);
				exeDescription.setEnabled(false);
			case ACTION_EDIT:
				if (extras.containsKey(ExercisesActivity.EXTRAS_KEY_EXERCISE)) {
					editingExercise = (Exercise) extras.getSerializable(
							ExercisesActivity.EXTRAS_KEY_EXERCISE);
					exeName.setText(editingExercise.getName());
					exeDescription.setText(editingExercise.getDescription());
				}
				break;
			case ACTION_ADD:
			default:
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_exer_edit, menu);
		menuItemAccept = menu.findItem(R.id.action_accept);
		if (action.equals(ACTION_VIEW)) {
			menuItemAccept.setVisible(false);
		}
		if (action.equals(ACTION_ADD)) {
			menu.findItem(R.id.action_exer_edit).setVisible(false);
			menu.findItem(R.id.action_exer_delete).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent();
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				if (exeName.getText().length() > 0) {
					if (action.equals(ACTION_ADD)) {
						Exercise e = new Exercise();
						e.setName(exeName.getText().toString());
						e.setDescription(exeDescription.getText().toString());
						workoutDataSource.createExersice(e.getName(), e.getDescription());
						intent.setAction(ACTION_ADD);
					} else if (action.equals(ACTION_EDIT)) {
						editingExercise.setName(exeName.getText().toString());
						editingExercise.setDescription(exeDescription.getText().toString());
						workoutDataSource.updateExercise(editingExercise);
						intent.setAction(ACTION_EDIT);
					}
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Can't add exercise with empty name!",
							Toast.LENGTH_LONG).show();
				}
				return true;
			case R.id.action_exer_edit:
				action = ACTION_EDIT;
				exeName.setEnabled(true);
				exeDescription.setEnabled(true);
				menuItemAccept.setVisible(true);
				return true;
			case R.id.action_exer_delete:
				workoutDataSource.deleteExersice(editingExercise);
				intent.setAction(ACTION_DELETE);
				setResult(RESULT_OK, intent);
				finish();
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		workoutDataSource.close();
	}


//	/** Action type by default. */
//	public static final String ACTION_DEFAULT = "action_default";

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

	/** Exercise to edit. */
	private Exercise editingExercise;

	/** Text field contains exercise name. */
	private EditText exeName;

	/** Text field contains exercise description. */
	private EditText exeDescription;

	/** Menu item "Accept".*/
	private MenuItem menuItemAccept;

	/** Class provides access to database. */
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
