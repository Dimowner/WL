package ua.com.sofon.workoutlogger.ui;

import java.util.List;
import java.util.ArrayList;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import ua.com.sofon.workoutlogger.parts.BaseParticle;

/**
 * Base data adapter for list.
 * @author Dimowner
 */
public abstract class BaseListAdapter<T extends BaseParticle>
		extends RecyclerView.Adapter<BaseListAdapter.ViewHolder> {

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public ViewHolder(View v) {
			super(v);
			mView = v;
		}

		public View mView;
	}

	public BaseListAdapter(List<T> items) {
		if (items != null) {
			this.data = items;
		} else {
			this.data = new ArrayList<>();
		}
	}

	@Override
	public BaseListAdapter.ViewHolder
					onCreateViewHolder(ViewGroup parent, int viewType) {
		return new ViewHolder(getListItemView(parent));
	}

	/**
	 * Create or inflate View for list item.
	 * @param parent Parent view group.
	 * @return View for list item.
	 */
	protected abstract View getListItemView(ViewGroup parent);

	@Override
	public void onBindViewHolder(ViewHolder holder, final int position) {
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
	}

	@Override
	public int getItemCount() {
		return data.size();
	}

	public void addItem(T item) {
		data.add(item);
		notifyDataSetChanged();
	}

	public void addItems(List<T> items) {
		data.addAll(items);
		notifyDataSetChanged();
	}

	public void addItem(int pos, T item) {
		if (item != null) {
			data.add(pos, item);
			notifyDataSetChanged();
		}
	}

	public T getItem(int position) {
		return data.get(position);
	}

	public List<T> getAllItems() {
		return new ArrayList<>(data);
	}

	public boolean removeItem(int position) {
		if (data.remove(position) != null) {
			notifyDataSetChanged();
			return true;
		}
		return false;
	}

	public boolean removeItem(long id) {
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getId() == id && data.remove(i) != null) {
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

	protected List<T> data;

	protected OnItemClickListener itemClickListener;
	protected OnItemLongClickListener itemLongClickListener;

	public interface OnItemClickListener {
		void onItemClick(View view, int position);
	}

	public interface OnItemLongClickListener {
		boolean onItemLongClick(View view, int position);
	}
}
