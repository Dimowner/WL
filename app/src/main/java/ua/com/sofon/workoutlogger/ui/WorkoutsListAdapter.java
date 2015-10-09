package ua.com.sofon.workoutlogger.ui;

import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.Workout;

/**
 * Data adapter for workouts list.
 * @author Dimowner
 */
public class WorkoutsListAdapter
		extends BaseListAdapter<Workout> {

	public WorkoutsListAdapter(String action, ArrayList<Workout> workouts) {
		super(workouts);
		if (workouts != null) {
			baseData = new ArrayList<>(workouts);
		} else {
			baseData = new ArrayList<>();
		}
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
	}

	@Override
	protected View getListItemView(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_workout, parent, false);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
//		((ImageView) holder.mView.findViewById(R.id.workout_list_item_image))
//				.setImageResource(R.drawable.ic_android_grey600_24dp);
		((TextView)holder.mView.findViewById(R.id.workout_list_item_header))
				.setText(data.get(position).getName());
		StringBuilder content = new StringBuilder();
		ArrayList<TrainedExercise> exes = data.get(position).getExerciseList();
		for (int i = 0; i < exes.size(); i++) {
			content.append(i+1).append("). ").append(exes.get(i).getName());
			if (i+1 < exes.size()) {
				content.append("\n");
			}
		}
		((TextView)holder.mView.findViewById(R.id.workout_list_item_content))
				.setText(content.toString());
	}

	public void applyDataByFilter(String search) {
		data.clear();

		for (int i = 0; i < baseData.size(); i++) {
			if (baseData.get(i).getName().toLowerCase()
					.contains(search.toLowerCase())) {
				data.add(baseData.get(i));
			}
		}
		notifyDataSetChanged();
	}


	protected ArrayList<Workout> baseData;

	private String action;
}
