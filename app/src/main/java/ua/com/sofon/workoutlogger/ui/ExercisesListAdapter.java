package ua.com.sofon.workoutlogger.ui;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.CompoundButton;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.Exercise;

/**
 * Data adapter for exercises list.
 * @author Dimowner
 */
public class ExercisesListAdapter extends BaseListAdapter<Exercise> {

	public ExercisesListAdapter(String action, List<Exercise> exercises) {
		super(exercises);
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
		this.checkedItems = new ArrayList<>();
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

		if (!action.equals(ExercisesActivity.ACTION_SELECT_MULTI)) {
			holder.mView.findViewById(R.id.exe_list_item_check).setVisibility(View.GONE);
		}

		//Save checked items position into the list
		((CheckBox) holder.mView.findViewById(R.id.exe_list_item_check))
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (isChecked) {
							checkedItems.add(data.get(position).getId());
						}
					}
				});
	}

	public int[] getCheckedItemsIDs() {
		int[] items = new int[checkedItems.size()];
		for (int i = 0; i < checkedItems.size(); i++) {
			items[i] = checkedItems.get(i);
		}
		return items;
	}

	public Exercise[] getCheckedItems() {
		Exercise[] exes = new Exercise[checkedItems.size()];
		for (int i = 0; i < checkedItems.size(); i++) {
			for (int j = 0; j < data.size(); j++) {
				if (checkedItems.get(i) == data.get(j).getId()) {
					exes[i] = data.get(j);
					break;
				}
			}
		}
		return exes;
	}


	private List<Integer> checkedItems;
	private String action;
}
