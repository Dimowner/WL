package ua.com.sofon.workoutlogger.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import ua.com.sofon.workoutlogger.R;

/**
 * Activity for add and edit exercises.
 * @author Dimowner.
 */
public class ExerPos extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exer_pos);

		Toolbar toolbar = (Toolbar) findViewById(R.id.add_exercise_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		exeName = (EditText) findViewById(R.id.txt_exercise_name);
		exeDescription = (EditText) findViewById(R.id.txt_exercise_description);

		intent = new Intent();
		if (getIntent() != null && getIntent().getAction().equals(ACTION_EDIT)) {
			Bundle extras = getIntent().getExtras();
			if (extras.containsKey(ExerBase.EXTRAS_KEY_ITEM_POSITION)) {
				intent.putExtra(ExerBase.EXTRAS_KEY_ITEM_POSITION,
						extras.getInt(ExerBase.EXTRAS_KEY_ITEM_POSITION));
			}
			if (extras.containsKey(ExerBase.EXTRAS_KEY_ID)) {
				intent.putExtra(ExerBase.EXTRAS_KEY_ID,
						extras.getLong(ExerBase.EXTRAS_KEY_ID));
			}
			if (extras.containsKey(ExerBase.EXTRAS_KEY_NAME)) {
				exeName.setText(extras.getString(ExerBase.EXTRAS_KEY_NAME));
			}
			if (extras.containsKey(ExerBase.EXTRAS_KEY_DESCRIPTION)) {
				exeDescription.setText(extras.getString(ExerBase.EXTRAS_KEY_DESCRIPTION));
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_exer_pos, menu);
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
					intent.putExtra(ExerBase.EXTRAS_KEY_NAME, exeName.getText().toString());
					intent.putExtra(ExerBase.EXTRAS_KEY_DESCRIPTION, exeDescription.getText().toString());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Can't add exercise with empty name!", Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


	/** Action type - Add */
	public static final String ACTION_ADD = "add_exercise";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_exercise";

	/** Intent to return {@link ua.com.sofon.workoutlogger.activities.ExerBase} */
	private Intent intent;

	/** Text field contains exercise name. */
	private EditText exeName;

	/** Text field contains exercise description. */
	private EditText exeDescription;

	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
