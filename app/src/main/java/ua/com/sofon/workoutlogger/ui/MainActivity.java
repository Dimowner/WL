package ua.com.sofon.workoutlogger.ui;

import android.support.v7.widget.Toolbar;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.ui.widget.DatePickerView;
import ua.com.sofon.workoutlogger.ui.widget.NumPickerView;
import ua.com.sofon.workoutlogger.ui.widget.TimePickerView;

//TODO LIST:
/*
* 1) Добавить фильтр на список тренировок по дате. По умолчанию показывать тренировки за прошедшый месяц.
* 2) Добавить активацию тренировки. В момент активации должен запускаться таймер отщитывающий длительность трнировки.
* 3) Вывести вес в отделный раздел, в котором можно посмотреть всю историю весов на графике.
* 4) Добавить раздел замеры и выводить на графики все замеры.
*
* 1) На главной странице должен бить календарь с тренировками в соответствующих датах.
* 2) Отображение списка тренировок в двух режимах: календарь и список.
* 3) В режиме просмотра тренировки убрать кнопку с тулбара и добавить меню:"Начать тренеровку", "Редактировать", "Удалить"
* 4) Добавить шаблоны тренеровок, аналогично упражнениям.
*
* Workout:
* 5) Добавить в верху кнопку "Начать тренировку" и табло таймера засекающий длительность тренировки.
* 6) Добавить в таблицу Workout StartTime DoneTime.
*
* Stats
* 1) Добавить раздел "Статистика", в котором статистика по разным временным интервалам.
*
* При первом входе в приложение показывать небольшую инструкцию использования приложения.
* */


/**
 * Application main activity.
 * @author Dimowner
 */
public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		Button btnExercisesList = (Button) findViewById(R.id.btn_show_exercises_list);
		btnExercisesList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, ExercisesActivity.class);
				startActivity(intent);
			}
		});

		Button btnWorkoutList = (Button) findViewById(R.id.btn_show_workout_list);
		btnWorkoutList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WorkoutsActivity.class);
				startActivity(intent);
			}
		});

		Button btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
			}
		});

		DatePickerView d = (DatePickerView) findViewById(R.id.datepicker);
		TimePickerView t = (TimePickerView) findViewById(R.id.timepicker);
		NumPickerView n = (NumPickerView) findViewById(R.id.numpicker);

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
