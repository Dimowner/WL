package ua.com.sofon.workoutlogger.ui;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.Workout;

/**
 * Data adapter for workouts list.
 * @author Dimowner
 */
public class WorkoutsListAdapter	extends RecyclerView.Adapter<WorkoutsListAdapter.ViewHolder> {

	public WorkoutsListAdapter(String action, List<Workout> workouts) {
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
		if (workouts != null) {
			this.data = workouts;
		} else {
			this.data = new ArrayList<>();
		}
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {

		public ViewHolder(View v) {
			super(v);
			mView = v;
		}

		public View mView;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_workout, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
//		((ImageView) holder.mView.findViewById(R.id.workout_list_item_image))
//				.setImageResource(R.drawable.ic_android_grey600_24dp);
		((TextView)holder.mView.findViewById(R.id.workout_list_item_header))
				.setText(data.get(position).getName());
		StringBuilder content = new StringBuilder();
		List<TrainedExercise> exes = data.get(position).getExerciseList();
		for (int i = 0; i < exes.size(); i++) {
			content.append(i+1).append("). ").append(exes.get(i).getName());
			if (i+1 < exes.size()) {
				content.append("\n");
			}
		}
		((TextView)holder.mView.findViewById(R.id.workout_list_item_content))
				.setText(content.toString());
		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (itemClickListener != null) {
					itemClickListener.onItemClick(v, position);
				}
			}
		});
		holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				return (itemLongClickListener != null)
						&& itemLongClickListener.onItemLongClick(v, position);
			}
		});
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItem(Workout workout) {
		data.add(workout);
		notifyDataSetChanged();
	}

	public void addItems(List<Workout> workouts) {
		data.addAll(workouts);
		notifyDataSetChanged();
	}

	public void addItem(int pos, Workout workout) {
		data.add(pos, workout);
		notifyDataSetChanged();
	}

	public Workout getItem(int position) {
		return data.get(position);
	}

	public boolean removeItem(int position) {
		if (position >=0 && position < data.size()) {
			data.remove(position);
			notifyDataSetChanged();
			return true;
		} else {
			return false;
		}
	}

	public boolean removeItem(long id) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getId() == id) {
				data.remove(i);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}

	public void clear() {
		data.clear();
		notifyDataSetChanged();
	}
	public void setOnItemClickListener(OnItemClickListener listener) {
		itemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener listener) {
		itemLongClickListener = listener;
	}


	private List<Workout> data;
	private String action;

	private OnItemClickListener itemClickListener;
	private OnItemLongClickListener itemLongClickListener;

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickListener {
		boolean onItemLongClick(View view, int position);
	}
}
