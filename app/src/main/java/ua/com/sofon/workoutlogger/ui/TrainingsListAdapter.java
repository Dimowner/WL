package ua.com.sofon.workoutlogger.ui;

import java.util.ArrayList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.TrainedWorkout;

/**
 * Data adapter for training list.
 * @author Dimowner
 */
public class TrainingsListAdapter
		extends BaseListAdapter<TrainedWorkout> {

	public TrainingsListAdapter(String action, ArrayList<TrainedWorkout> plannedWorkouts) {
		super(plannedWorkouts);
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
	}

	@Override
	protected View getListItemView(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_planned_workout, parent, false);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		((TextView)holder.mView.findViewById(R.id.planned_workout_list_item_header))
				.setText(data.get(position).getName());
		StringBuilder content = new StringBuilder();
		ArrayList<TrainedExercise> exes = data.get(position).getExerciseList();
		for (int i = 0; i < exes.size(); i++) {
			content.append(i+1).append("). ").append(exes.get(i).getName());
			if (i+1 < exes.size()) {
				content.append("\n");
			}
		}
		((TextView)holder.mView.findViewById(R.id.planned_workout_list_item_content))
				.setText(content.toString());
		((TextView)holder.mView.findViewById(R.id.planned_workout_plan_date))
				.setText(data.get(position).getPlanDateStr());
		((TextView)holder.mView.findViewById(R.id.planned_workout_perform_date))
				.setText(data.get(position).getPerformDateStr());
//		((TextView)holder.mView.findViewById(R.id.planned_workout_duration))
//				.setText(data.get(position).getDuration());
//		((TextView)holder.mView.findViewById(R.id.planned_workout_state))
//				.setText(data.get(position).getState());
	}


	private String action;
}
