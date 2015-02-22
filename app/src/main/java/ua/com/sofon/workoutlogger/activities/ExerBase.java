package ua.com.sofon.workoutlogger.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.sql.SQLException;
import java.util.List;
import ua.com.sofon.workoutlogger.Exercise;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.WorkoutDataSource;

/**
 * Activity shows all exercises.
 * @author Dimowner
 */
public class ExerBase extends ActionBarActivity {

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
		listAdapter = new ExercisesListAdapter(this,
				workoutDataSource.getAllExercises());
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
				Intent intent = new Intent(ExerBase.this, ExerPos.class);
				intent.setAction(ExerPos.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_EXERCISE);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ADD_EXERCISE && resultCode == RESULT_OK) {
			//Add new item to DB and to listAdaptper.
			listAdapter.addItem(
					workoutDataSource.createExersice(
							data.getExtras().getString(EXTRAS_KEY_NAME),
							data.getExtras().getString(EXTRAS_KEY_DESCRIPTION)
					)
			);
			listAdapter.notifyDataSetChanged();
		} else if (requestCode == REQUEST_EDIT_EXERCISE && resultCode == RESULT_OK) {

			//Update item in DB and in listAdaptper.
			Bundle extras = data.getExtras();
			String name = extras.getString(EXTRAS_KEY_NAME);
			String description = extras.getString(EXTRAS_KEY_DESCRIPTION);
			Log.v(LOG_TAG, "name = " + name + " desc = " + description + " id = " + extras.getLong(EXTRAS_KEY_ID));
			workoutDataSource.updateExercise(
					extras.getLong(EXTRAS_KEY_ID),
					name,
					description
			);
			Exercise e =  (Exercise) listAdapter.getItem(extras.getInt(EXTRAS_KEY_ITEM_POSITION));
			e.setName(name);
			e.setDescription(description);
			listAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		workoutDataSource.close();
	}


	private final int REQUEST_ADD_EXERCISE = 101;
	private final int REQUEST_EDIT_EXERCISE = 102;
	public static final String EXTRAS_KEY_ID = "exercise_id";
	public static final String EXTRAS_KEY_ITEM_POSITION = "item_position";
	public static final String EXTRAS_KEY_NAME = "exercise_name";
	public static final String EXTRAS_KEY_DESCRIPTION = "exercise_description";
	private ListView mListView;
	private ExercisesListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;

	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();


	/**
	 * List adapter for exercises list.
	 */
	private class ExercisesListAdapter extends BaseAdapter {

		/**
		 * Constructor.
		 * @param contex Application context.
		 * @param exerciseList List contains list of exercises to show.
		 */
		public ExercisesListAdapter(Context contex, List<Exercise> exerciseList) {
			this.context = contex;
			this.exerciseList = exerciseList;
		}

		/**
		 * Add new Exercise to adapter.
		 * @param exe Exercise.
		 */
		public void addItem(Exercise exe) {
			exerciseList.add(exe);
		}

		@Override
		public int getCount() {
			return exerciseList.size();
		}

		@Override
		public Object getItem(int position) {
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
				LayoutInflater inflater = (LayoutInflater)
						context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_item, null);
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

			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					switch (item.getItemId()) {
						case R.id.action_edit:

							Exercise e = exerciseList.get(position);
							Log.v(LOG_TAG, "name = " + e.getName() + " desc = " + e.getDescription() + " id = " + e.getId());
							Intent intent = new Intent(context, ExerPos.class);
							intent.setAction(ExerPos.ACTION_EDIT);
							intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
							intent.putExtra(EXTRAS_KEY_ID, e.getId());
							intent.putExtra(EXTRAS_KEY_NAME, e.getName());
							intent.putExtra(EXTRAS_KEY_DESCRIPTION, e.getDescription());
							startActivityForResult(intent, REQUEST_EDIT_EXERCISE);

							return true;
						case R.id.action_delete:
//							TODO: ADD DIALOG TO ACCEPT DELETION
							workoutDataSource.deleteExersice(exerciseList.get(position));
							exerciseList.remove(position);
							notifyDataSetChanged();
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
