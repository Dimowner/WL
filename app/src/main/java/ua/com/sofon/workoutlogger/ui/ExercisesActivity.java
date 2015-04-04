package ua.com.sofon.workoutlogger.ui;

import java.util.*;
import java.sql.*;
import android.content.*;
import android.support.v7.app.*;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.WorkoutDataSource;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.util.UIUtil;

/**
 * Activity shows all exercises.
 * @author Dimowner
 */
public class ExercisesActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_activity);

		Toolbar toolbar = (Toolbar) findViewById(R.id.list_toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		action = getIntent().getAction();
		if (action == null) {
			action = ACTION_DEFAULT;
		}

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
				Intent intent = new Intent(ExercisesActivity.this, EditExerciseActivity.class);
				intent.setAction(EditExerciseActivity.ACTION_ADD);
				startActivityForResult(intent, REQUEST_ADD_EXERCISE);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data.hasExtra(EXTRAS_KEY_EXERCISE)) {
				Exercise e = (Exercise) data.getSerializableExtra(EXTRAS_KEY_EXERCISE);
				switch (requestCode) {
					case REQUEST_ADD_EXERCISE:
						//Add new item to DB and to listAdaptper.
						listAdapter.addItem(
								workoutDataSource.createExersice(
										e.getName(),
										e.getDescription()
								)
						);
						break;
					case REQUEST_EDIT_EXERCISE:
						//Update item in DB and in listAdaptper.
						listAdapter.updateEditingItem(e);
						listAdapter.resetEditItemPos();
						workoutDataSource.updateExercise(e);
						break;
				}
				listAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		workoutDataSource.close();
	}


	private final int REQUEST_ADD_EXERCISE = 101;
	private final int REQUEST_VIEW_EXERCISE = 102;
	private final int REQUEST_EDIT_EXERCISE = 103;
	public static final String ACTION_SELECT = "action_select";
	public static final String ACTION_DEFAULT = "action_default";
	public static final String EXTRAS_KEY_EXERCISE = "exercise";

	private String action = ACTION_DEFAULT;
	private ListView mListView;
	private ExercisesListAdapter listAdapter;
	private WorkoutDataSource workoutDataSource;

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
		 * Remove Exercise from adapter.
		 * @param exe Exercise.
		 * @return Removing result.
		 */
		public boolean removeItem(Exercise exe) {
			return exerciseList.remove(exe);
		}

		public void resetEditItemPos () {
			editItemPos = NOT_ON_EDITION;
		}

		public void updateEditingItem(Exercise exe) {
			if (editItemPos != NOT_ON_EDITION) {
				exerciseList.remove(editItemPos);
				exerciseList.add(editItemPos, exe);
			}
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
				LayoutInflater inflater = getLayoutInflater();
				view = inflater.inflate(R.layout.list_item, parent, false);
			}

			TextView name = (TextView) view.findViewById(R.id.list_item_title);
			TextView description = (TextView) view.findViewById(R.id.list_item_content);

			Exercise exe = exerciseList.get(position);
			name.setText(exe.getName());
			description.setText(exe.getDescription());

			LinearLayout listItem = (LinearLayout) view.findViewById(R.id.list_item_pnl);
			listItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (action.equals(ACTION_SELECT)) {
						Intent intent = new Intent();
						intent.putExtra(EXTRAS_KEY_EXERCISE, (Exercise) listAdapter.getItem(position));
						setResult(RESULT_OK, intent);
						finish();
					}
				}
			});

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
			MenuItem item = popupMenu.getMenu().findItem(R.id.action_replace);
			item.setVisible(false);

			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					Exercise e = exerciseList.get(position);
					switch (item.getItemId()) {
						case R.id.action_edit:
							Log.v(LOG_TAG, "name = " + e.getName() + " desc = " + e.getDescription() + " id = " + e.getId());
							Intent intent = new Intent(ExercisesActivity.this, EditExerciseActivity.class);
							intent.setAction(EditExerciseActivity.ACTION_EDIT);
							editItemPos = position;
//							intent.putExtra(EXTRAS_KEY_ITEM_POSITION, position);
//							intent.putExtra(EXTRAS_KEY_ID, e.getId());
//							intent.putExtra(EXTRAS_KEY_NAME, e.getName());
//							intent.putExtra(EXTRAS_KEY_DESCRIPTION, e.getDescription());
							intent.putExtra(EXTRAS_KEY_EXERCISE, e);
							startActivityForResult(intent, REQUEST_EDIT_EXERCISE);

							return true;
						case R.id.action_delete:
							UIUtil.showWarningDialog(ExercisesActivity.this, "Delete exercise?",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											workoutDataSource.deleteExersice(exerciseList.get(position));
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


		/** No items on edit. */
		private static final int NOT_ON_EDITION = -1;

		/** Position of editing item. */
		private int editItemPos = NOT_ON_EDITION;

		/** Application context. */
		private Context context;

		/** List contains all exercises. */
		private List<Exercise> exerciseList;
	}
}
