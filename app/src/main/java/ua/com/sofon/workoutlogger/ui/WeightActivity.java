package ua.com.sofon.workoutlogger.ui;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.database.BodyWeightDS;
import ua.com.sofon.workoutlogger.parts.BodyWeight;
import ua.com.sofon.workoutlogger.util.LogUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGE;

/**
 * Activity shows weight history.
 * @author Dimowner
 */
public class WeightActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weight);

		dataSource = new BodyWeightDS(this);

		//Init chart.
		chart = (LineChart) findViewById(R.id.chart);
		chart.setDragEnabled(true);
		chart.setScaleEnabled(true);
		chart.setPinchZoom(true);
		chart.setDoubleTapToZoomEnabled(false);
		chart.setHighlightEnabled(false);
		chart.setHighlightPerDragEnabled(false);

		loadWeights();
		updateShowingData();
	}

	@Override
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_WEIGHT;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_weight, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			Fragment prev = fm.findFragmentByTag("dialog_add_weight");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);
			WeightAddDialog dialog = new WeightAddDialog();
			dialog.setOnDialogButtonClickListener(new WeightAddDialog.OnDialogButtonClickListener() {
				@Override
				public void onPositiveButtonClicked() {
					loadWeights();
					updateShowingData();
				}

				@Override
				public void onNegativeButtonClicked() {
				}
			});
			dialog.show(ft, "dialog_add_weight");
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Update graph and weights data stored in activity.
	 */
	public void updateShowingData() {
		if (weights.size() > 0) {
			ArrayList<Entry> valsComp1 = new ArrayList<>();

			for (int i = 0; i < weights.size(); i++) {
				valsComp1.add(new Entry(weights.get(i).getWeight(), i + 1));
			}

			LineDataSet setComp1 = new LineDataSet(valsComp1, "Weight");
			setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);

			ArrayList<LineDataSet> dataSets = new ArrayList<>();
			dataSets.add(setComp1);

			ArrayList<String> xVals = new ArrayList<>();
			for (int i = 0; i < weights.size() + 2; i++) {
				xVals.add(String.valueOf(i));
			}
			LineData data = new LineData(xVals, dataSets);
			chart.setData(data);
			chart.invalidate();
		}
	}

	/**
	 * Load all weights data from database.
	 */
	public void loadWeights() {
		if (dataSource != null) {
			try {
				dataSource.open();
			} catch (SQLException e) {
				LOGE(LOG_TAG, "", e);
			}
			weights = dataSource.getAll();
			dataSource.close();
		} else {
			LOGE(LOG_TAG, "dataSource is null");
		}
	}

	private BodyWeightDS dataSource;
	private LineChart chart;
	private List<BodyWeight> weights;

	/** Tag for logging messages. */
	private final String LOG_TAG = LogUtils.makeLogTag("WeightsActivity");
}
