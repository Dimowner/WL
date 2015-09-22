package ua.com.sofon.workoutlogger.ui;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;

/**
 * Data adapter for Trained exercises list.
 * @author Dimowner
 */
public class TrainedExercisesListAdapter extends BaseListAdapter<TrainedExercise> {

	public TrainedExercisesListAdapter(String action, List<TrainedExercise> items) {
		super(items);
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
	}

	@Override
	protected View getListItemView(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_trained_exercise, parent, false);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		super.onBindViewHolder(holder, position);
		((ImageView) holder.mView.findViewById(R.id.tr_exe_item_header_image))
				.setImageResource(R.drawable.ic_android_grey600_24dp);
		((TextView)holder.mView.findViewById(R.id.tr_exe_item_text))
				.setText(data.get(position).getName());
	}

	private String action;
}
