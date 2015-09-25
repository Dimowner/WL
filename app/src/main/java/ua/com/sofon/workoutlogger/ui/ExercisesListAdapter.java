package ua.com.sofon.workoutlogger.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CompoundButton;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.Exercise;
import ua.com.sofon.workoutlogger.util.LogUtils;

/**
 * Data adapter for exercises list.
 * @author Dimowner
 */
public class ExercisesListAdapter extends BaseListAdapter<Exercise> {

	public ExercisesListAdapter(String action, ArrayList<Exercise> exercises) {
		super(exercises);
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
		this.checkedItems = new HashSet<>();
	}

	@Override
	protected View getListItemView(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_exercise, parent, false);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		((ImageView) holder.mView.findViewById(R.id.exe_list_item_image))
				.setImageResource(R.drawable.ic_android_grey600_24dp);
		((TextView)holder.mView.findViewById(R.id.exe_list_item_text))
				.setText(data.get(position).getName());

		CheckBox checkBox = (CheckBox) holder.mView.findViewById(R.id.exe_list_item_check);
		if (!action.equals(ExercisesActivity.ACTION_SELECT_MULTI)) {
			checkBox.setVisibility(View.GONE);
		}

		if (checkedItems.contains(data.get(position).getId())) {
			checkBox.setChecked(true);
		}
		//Save checked items position into the list
		checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if (!checkedItems.contains(data.get(position).getId())) {
							checkedItems.add(data.get(position).getId());
						}
					} else {
						checkedItems.remove(Integer.valueOf(data.get(position).getId()));
					}
				}
			});
	}

	public int[] getCheckedItemsIDs() {
		int[] items = new int[checkedItems.size()];
		int i = 0;
		for (int item : checkedItems) {
			items[i] = item;
			i++;
		}
		return items;
	}

	public Exercise[] getCheckedItems() {
		Exercise[] exes = new Exercise[checkedItems.size()];
		int i = 0;
		for (int item : checkedItems) {
			for (int j = 0; j < data.size(); j++) {
				if (item == data.get(j).getId()) {
					exes[i] = data.get(j);
					break;
				}
			}
			i++;
		}
		Log.v(LOG_TAG, "exes = " + exes.length);
		return exes;
	}

	public void setCheckedItemIDs(int[] items) {
		Log.v(LOG_TAG, "setCheckedItemIDs = " + checkedItems.size());
		for (int i = 0; i < items.length; i++) {
			checkedItems.add(items[i]);
		}
		notifyDataSetChanged();
	}

	/** List contains ids of checked items */
	private HashSet<Integer> checkedItems;

	private String action;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag(getClass().getSimpleName());
}
