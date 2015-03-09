package ua.com.sofon.workoutlogger.activities;

import android.support.v7.app.*;
import android.support.v7.widget.Toolbar;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ua.com.sofon.workoutlogger.R;

//TODO LIST:
/*
* 1) Добавить фильтр на список тренировок по дате. По умолчанию показывать тренировки за прошедшый месяц.
* 2) Добавить активацию тренировки. В момент активации должен запускаться таймер отщитывающий длительность трнировки.
* 3) Вывести вес в отделный раздел, в котором можно посмотреть всю историю весов на графике.
*
* */


/**
 * Application main activity.
 * @author Dimowner
 */
public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		Toolbar toolbar = (Toolbar) findViewById(R.id.main_activity_toolbar);
		setSupportActionBar(toolbar);

		Button btnExercisesList = (Button) findViewById(R.id.btn_show_exercises_list);
		btnExercisesList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ExerBase.class);
				startActivity(intent);
			}
		});

		Button btnWorkoutList = (Button) findViewById(R.id.btn_show_workout_list);
		btnWorkoutList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WorkoutBase.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.menu_workout_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//
//		//noinspection SimplifiableIfStatement
//		if (id == R.id.action_settings) {
//			return true;
//		}

		return super.onOptionsItemSelected(item);
	}


	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();
}
