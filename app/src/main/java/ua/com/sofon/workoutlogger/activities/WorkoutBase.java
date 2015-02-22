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
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.Workout;
import ua.com.sofon.workoutlogger.WorkoutDataSource;

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
//				intent.setAction(ExerPos.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_WORKOUT);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_ADD_WORKOUT && resultCode == RESULT_OK) {
			//Add new item to DB and to listAdaptper.
//			listAdapter.addItem(
//					workoutDataSource.createExersice(
//							data.getExtras().getString(EXTRAS_KEY_NAME),
//							data.getExtras().getString(EXTRAS_KEY_DESCRIPTION)
//					)
//			);
//			listAdapter.notifyDataSetChanged();
		}
	}

	private final int REQUEST_ADD_WORKOUT = 101;
	private ListView mListView;
	private WorkoutListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;
	/** Tag for logging mesages. */
	private final String LOG_TAG = getClass().getSimpleName();


	/**
	 * List adapter for exercises list.
	 */
	private class WorkoutListAdapter extends BaseAdapter {

		/**
		 * Constructor.
		 * @param contex Application context.
		 * @param workoutList List contains list of exercises to show.
		 */
		public WorkoutListAdapter(Context contex, List<Workout> workoutList) {
			this.context = contex;
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
				LayoutInflater inflater = (LayoutInflater)
						context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.list_item, null);
			}

			TextView name = (TextView) view.findViewById(R.id.list_item_title);
			TextView description = (TextView) view.findViewById(R.id.list_item_content);

			Workout exe = workoutList.get(position);
//			name.setText(exe.getName());
//			description.setText(exe.getDescription());

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
							return true;
						case R.id.action_delete:
//							workoutDataSource.deleteExersice(exerciseList.get(position));
//							exerciseList.remove(position);
//							notifyDataSetChanged();
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
