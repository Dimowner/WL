package ua.com.sofon.workoutlogger.activities;

import java.util.*;
import java.sql.*;
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
 * Activity shows all workouts.
 * @author Dimowner
 */
public class WorkoutBase extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);

		Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		workoutDataSource = new WorkoutDataSource(this);
		try {
			workoutDataSource.open();
		} catch (SQLException e) {
			Log.e(LOG_TAG, "", e);
		}

		mListView = (ListView) findViewById(R.id.list);
		listAdapter = new WorkoutListAdapter(this, workoutDataSource.getAllWorkouts());
		mListView.setAdapter(listAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
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
				Intent intent = new Intent(WorkoutBase.this, WorkoutPos.class);
				startActivityForResult(intent, REQUEST_ADD_WORKOUT);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case REQUEST_ADD_WORKOUT:
					Workout w = (Workout)data.getSerializableExtra(EXTRAS_KEY_WORKOUT);
					listAdapter.addItem(
							workoutDataSource.createWorkout(
									w.getName(),
									w.getDate(),
									w.getWeight(),
									w.getDuration(),
									w.getComment(),
									w.getExerciseList()
							)
					);
					listAdapter.notifyDataSetChanged();
					break;
				case REQUEST_EDIT_WORKOUT:
					Toast.makeText(this, "Update list item", Toast.LENGTH_LONG).show();
					break;
			}
		}
	}


	private final int REQUEST_ADD_WORKOUT = 101;
	private final int REQUEST_EDIT_WORKOUT = 102;

	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";
	public static final String EXTRAS_KEY_WORKOUT = "workout";
	private ListView mListView;
	private WorkoutListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging messages. */
	private final String LOG_TAG = getClass().getSimpleName();


	/**
	 * List adapter for exercises list.
	 */
	private class WorkoutListAdapter extends BaseAdapter {

		/**
		 * Constructor.
		 * @param context Application context.
		 * @param workoutList List contains list of exercises to show.
		 */
		public WorkoutListAdapter(Context context, List<Workout> workoutList) {
			this.context = context;
			this.workoutList = workoutList;
		}

		/**
		 * Add new Exercise to adapter.
		 * @param workout Exercise.
		 */
		public void addItem(Workout workout) {
			workoutList.add(workout);
		}

		@Override
		public int getCount() {
			return workoutList.size();
		}

		@Override
		public Object getItem(int position) {
			return workoutList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return workoutList.get(position).getId();
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.list_item, parent, false);
			}

			TextView name = (TextView) view.findViewById(R.id.list_item_title);
			TextView content = (TextView) view.findViewById(R.id.list_item_content);

			Workout workout = workoutList.get(position);
			name.setText(workout.getName());
			content.setText("Date:" + workout.getDateStr() + " weight:" + workout.getWeight());

			ImageButton itemMenu = (ImageButton) view.findViewById(R.id.btn_item_menu);
			itemMenu.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
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
			MenuItem item = popupMenu.getMenu().findItem(R.id.action_replace);
			item.setVisible(false);

			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					Workout w = workoutList.get(position);
					switch (item.getItemId()) {
						case R.id.action_edit:
							Intent intent = new Intent(WorkoutBase.this, WorkoutPos.class);
							intent.setAction(ExerPos.ACTION_EDIT);
							intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
							intent.putExtra(EXTRAS_KEY_WORKOUT, w);
							startActivityForResult(intent, REQUEST_EDIT_WORKOUT);
							return true;
						case R.id.action_delete:
							Util.showWarningDialog(WorkoutBase.this, "Delete workout?",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											workoutDataSource.deleteWorkout(workoutList.get(position));
											workoutList.remove(position);
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
		private List<Workout> workoutList;
	}
}
