package ua.com.sofon.workoutlogger.ui;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.parts.Exercise;

/**
 * Data adapter for exercises list.
 * @author Dimowner
 */
public class ExercisesListAdapter
		extends RecyclerView.Adapter<ExercisesListAdapter.ViewHolder> {

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
			mView = v;
		}

		public View mView;
	}

	public ExercisesListAdapter(String action, List<Exercise> exercises) {
		if (action != null && !action.isEmpty()) {
			this.action = action;
		} else {
			this.action = "";
		}
		if (exercises != null) {
			this.data = exercises;
		} else {
			this.data = new ArrayList<>();
		}
		this.checkedItems = new ArrayList<>();
	}

	@Override
	public ExercisesListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// create a new view
		View v = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_exercises, parent, false);
		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
		((ImageView) holder.mView.findViewById(R.id.exe_list_item_image))
				.setImageResource(R.drawable.ic_android_grey600_24dp);
		((TextView)holder.mView.findViewById(R.id.exe_list_item_text))
				.setText(data.get(position).getName());
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
				return  (itemLongClickListener != null)
						&& itemLongClickListener.onItemLongClick(v, position);
			}
		});

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

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItem(Exercise exe) {
		data.add(exe);
		notifyDataSetChanged();
	}

	public void addItems(List<Exercise> exes) {
		data.addAll(exes);
		notifyDataSetChanged();
	}

	public void addItem(int pos, Exercise exe) {
		data.add(pos, exe);
		notifyDataSetChanged();
	}

	public Exercise getItem(int position) {
		return data.get(position);
	}

	public List<Exercise> getAllItems() {
		return new ArrayList<>(data);
	}

	public void removeItem(int position) {
		data.remove(position);
		notifyDataSetChanged();
	}

	public long[] getCheckedItemsIDs() {
		long[] items = new long[checkedItems.size()];
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

	private List<Exercise> data;
	private List<Long> checkedItems;
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