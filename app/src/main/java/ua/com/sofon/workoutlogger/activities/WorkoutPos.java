package ua.com.sofon.workoutlogger.activities;

import java.text.*;
import java.util.*;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import ua.com.sofon.workoutlogger.*;
import ua.com.sofon.workoutlogger.R;

/**
 * Activity for add and edit workouts.
 * @author Dimowner.
 */
public class WorkoutPos extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.workout_pos);

		Toolbar toolbar = (Toolbar) findViewById(R.id.workout_pos_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		txtExeName = (EditText) findViewById(R.id.workout_pos_txt_name);
		txtDate = (EditText) findViewById(R.id.workout_pos_txt_date);
		txtWeight = (EditText) findViewById(R.id.workout_pos_txt_weight);
		txtComment = (EditText) findViewById(R.id.workout_pos_txt_comment);
		exeList = (ListView) findViewById(R.id.workout_pos_exe_list);

		List<Exercise> exercises = new ArrayList<>();
		adapter = new ExercisesListAdapter(this, exercises);
		exeList.setAdapter(adapter);
		TextView listHeader = new TextView(this);
		listHeader.setText("Exercises:");
		listHeader.setGravity(Gravity.CENTER);
		listHeader.setPadding(16,16,16,16);
		exeList.addHeaderView(listHeader);

		Button btnAddExe = (Button) findViewById(R.id.workout_pos_btn_add_exe);
		btnAddExe.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WorkoutPos.this, ExerBase.class);
				intent.setAction(ExerBase.ACTION_SELECT);
				startActivityForResult(intent, REQUEST_CODE_ADD);
			}
		});

		intent = new Intent();
		if (getIntent().getAction() != null
				&& getIntent().getAction().equals(ACTION_EDIT)) {
			Bundle extras = getIntent().getExtras();
			if (extras.containsKey(WorkoutBase.EXTRAS_KEY_ITEM_POSITION)) {
				intent.putExtra(WorkoutBase.EXTRAS_KEY_ITEM_POSITION,
						extras.getInt(WorkoutBase.EXTRAS_KEY_ITEM_POSITION));
			}
			if (extras.containsKey(WorkoutBase.EXTRAS_KEY_WORKOUT)) {
				Workout workout = (Workout)extras.getSerializable(WorkoutBase.EXTRAS_KEY_WORKOUT);
				txtExeName.setText(workout.getName());
				txtDate.setText(workout.getDateStr());
				txtWeight.setText(String.valueOf(workout.getWeight()));
				txtComment.setText(workout.getComment());
			}
		} else {
//			TODO: Make normal date for other locations
//			SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
//			DateFormat df = DateFormat.getDateInstance();
//			df.setTimeZone(TimeZone.);
//			String curDateStr = sdf.format(new Date());
			String curDateStr = Workout.dateFormat.format(new Date());
			txtDate.setText(curDateStr);
			txtExeName.setText("Workout");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_workout_pos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
			case R.id.action_accept:
				if (txtDate.getText().length() > 0) {
					Intent intent = new Intent();
					intent.putExtra(WorkoutBase.EXTRAS_KEY_WORKOUT, makeWorkout());
					setResult(RESULT_OK, intent);
					finish();
				} else {
					Toast.makeText(this, "Field date must be filled!", Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Extract layout fields data and instantiate
	 * {@link ua.com.sofon.workoutlogger.Workout Workout} with this data.
	 * @return Workout.
	 */
	private Workout makeWorkout() {
		Workout workout = new Workout();
		workout.setName(txtExeName.getText().toString());
		try {
			String dateStr = txtDate.getText().toString();
			if (!dateStr.isEmpty()) {
				Date date = Workout.dateFormat.parse(dateStr);
				workout.setDate(date);
			}
		} catch (ParseException e) {
			Log.e(LOG_TAG, "", e);
		}
		try {
			String weightStr = txtWeight.getText().toString();
			if (!weightStr.isEmpty()) {
				float weight = Float.parseFloat(weightStr);
				workout.setWeight(weight);
			}
		} catch (NumberFormatException e) {
			Log.e(LOG_TAG, "", e);
		}
		workout.setComment(txtComment.getText().toString());
		workout.setExerciseList(adapter.getExercises());
		return workout;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_CODE_ADD:
					if (data.hasExtra(ExerBase.EXTRAS_KEY_EXERCISE)) {
						Log.v(LOG_TAG, "Add item to exeList");
						adapter.addItem((Exercise)data.getSerializableExtra(ExerBase.EXTRAS_KEY_EXERCISE));
						adapter.notifyDataSetChanged();
					}
					break;
				case REQUEST_CODE_REPLACE:
					if (data.hasExtra(ExerBase.EXTRAS_KEY_EXERCISE)) {
						if (selectedItemPos != -1) {
							adapter.removeItem(adapter.getItem(selectedItemPos));
							adapter.insertItem(
									(Exercise) data.getSerializableExtra(ExerBase.EXTRAS_KEY_EXERCISE),
									selectedItemPos
								);
							adapter.notifyDataSetChanged();
							selectedItemPos = -1;
						}
					}
					break;
			}
		}
	}


	/** Action type - Add */
	public static final String ACTION_ADD = "add_exercise";

	/** Action type - Edit */
	public static final String ACTION_EDIT = "edit_exercise";

	private static final int REQUEST_CODE_ADD = 1;
	private static final int REQUEST_CODE_REPLACE = 2;

	private EditText txtExeName;
	private EditText txtDate;
	private EditText txtWeight;
	private EditText txtComment;
	private ListView exeList;
	private int selectedItemPos = -1;

	/** Intent to return {@link ua.com.sofon.workoutlogger.activities.WorkoutBase} */
	private Intent intent;

	private ExercisesListAdapter adapter;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();


	/**
	 * List adapter for exercises list.
	 */
	private class ExercisesListAdapter extends BaseAdapter {

		/**
		 * Constructor.
		 * @param context Application context.
		 * @param exerciseList List contains list of exercises to show.
		 */
		public ExercisesListAdapter(Context context, List<Exercise> exerciseList) {
			this.context = context;
			this.exerciseList = exerciseList;
		}

		/**
		 * Add new Exercise to adapter.
		 * @param exe Exercise.
		 */
		public void addItem(Exercise exe) {
			exerciseList.add(exe);
		}

		/**
		 * Add new Exercise to adapter.
		 * @param exe Exercise.
		 */
		public void insertItem(Exercise exe, int position) {
			Log.v(LOG_TAG, "Insert item pos = " + position);
			exerciseList.add(position, exe);
		}

		/**
		 * Remove Exercise from adapter.
		 * @param exe Exercise.
		 */
		public void removeItem(Exercise exe) {
			exerciseList.remove(exe);
		}

		private List<Exercise> getExercises() {
			return new ArrayList<>(exerciseList);
		}

		@Override
		public int getCount() {
			return exerciseList.size();
		}

		@Override
		public Exercise getItem(int position) {
			return exerciseList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return exerciseList.get(position).getId();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.list_item, parent, false);
			}

			TextView name = (TextView) view.findViewById(R.id.list_item_title);
			TextView description = (TextView) view.findViewById(R.id.list_item_content);

			Exercise exe = exerciseList.get(position);
			name.setText(exe.getName());
			description.setText(exe.getDescription());

			ImageButton itemMenu = (ImageButton) view.findViewById(R.id.btn_item_menu);
			itemMenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Log.v(LOG_TAG, "Show item menu pos = " + position);
					showPopupMenu(v, position);
				}
			});

			return view;
		}

		/**
		 * Show menu for list item to manage it.
		 * @param view View anchor.
		 * @param position Item position in list.
		 */
		private void showPopupMenu(View view, final int position) {
			final PopupMenu popupMenu = new PopupMenu(context, view);
			popupMenu.inflate(R.menu.list_item_popupmenu);
			MenuItem item = popupMenu.getMenu().findItem(R.id.action_edit);
			item.setVisible(false);

			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {

					switch (item.getItemId()) {
						case R.id.action_replace:
							Intent intent = new Intent(context, ExerBase.class);
							intent.setAction(ExerBase.ACTION_SELECT);
							selectedItemPos = position;
							startActivityForResult(intent, REQUEST_CODE_REPLACE);
							return true;
						case R.id.action_delete:
							Util.showWarningDialog(WorkoutPos.this, "Delete exercise?",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											exerciseList.remove(position);
											notifyDataSetChanged();
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
							return false;
					}
				}
			});
			popupMenu.show();
		}

		/** Application context. */
		private Context context;

		/** List contains all exercises. */
		private List<Exercise> exerciseList;
	}
}
