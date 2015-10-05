package ua.com.sofon.workoutlogger.ui;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.TrainedExercise;
import ua.com.sofon.workoutlogger.parts.TrainedWorkout;
import ua.com.sofon.workoutlogger.ui.widget.NotScrollableListView;

/**
 * Data adapter for training list.
 * @author Dimowner
 */
public class TrainingsListAdapter
		extends BaseListAdapter<TrainedWorkout> {

	public TrainingsListAdapter(Context context,
				String action, ArrayList<TrainedWorkout> plannedWorkouts) {
		super(plannedWorkouts);
		this.context = context;
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
	}

	@Override
	protected View getListItemView(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext())
						.inflate(R.layout.list_item_training, parent, false);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {

		((TextView)holder.mView.findViewById(R.id.training_list_item_header))
				.setText(data.get(position).getName());
		((TextView)holder.mView.findViewById(R.id.training_list_item_date))
				.setText(data.get(position).getPlanDateStr());
		(holder.mView.findViewById(R.id.training_list_item_start_btn))
				.setVisibility(View.GONE);

		final NotScrollableListView listview = (NotScrollableListView)
				holder.mView.findViewById(R.id.training_list_item_exes);

		ExeAdapter adapter = new ExeAdapter(data.get(position).getExerciseList());
		listview.setAdapter(adapter);
	}


	private Context context;
	private String action;


	private class ExeAdapter extends BaseAdapter {

		public ExeAdapter(ArrayList<TrainedExercise> items) {
			if (items != null && items.size() > 0) {
				data = items;
				isEmpty = false;
			} else {
				data = new ArrayList<>();
				data.add(null);
				isEmpty = true;
			}
		}

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return data.get(position).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = new TextView(context);
			String text;
			if (!isEmpty) {
				text = String.valueOf(position + 1) + "). " + data.get(position).getName();
			} else {
				text = "No exercises";
			}
			tv.setText(text);
			//Convert px to dip.
//			int padd = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
//					context.getResources().getDisplayMetrics());
//			tv.setPadding(0, 0, 0, padd);
//			tv.setCompoundDrawables(context.getResources()
//					.getDrawable(R.drawable.ic_android_grey600_24dp), null, null, null);
//			tv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_android_grey600_24dp, 0, 0, 0);
//			tv.setCompoundDrawablePadding(padd);
			return tv;
		}

		private ArrayList<TrainedExercise> data;
		private boolean isEmpty;
	}
}
