package ua.com.sofon.workoutlogger.ui;

import android.support.v7.widget.RecyclerView;
import android.test.ActivityInstrumentationTestCase2;

import ua.com.sofon.workoutlogger.R;

/**
 * Unit test for testing {@link ExercisesActivity}
 * @author Dimowner
 */
public class ExercisesActivityTest extends ActivityInstrumentationTestCase2<ExercisesActivity> {

	ExercisesActivity activity;

	public ExercisesActivityTest(Class<ExercisesActivity> activityClass) {
		super(activityClass);
	}

	@org.junit.Before
	public void setUp() throws Exception {
		super.setUp();
		activity = getActivity();

	}

	@org.junit.After
	public void tearDown() throws Exception {

	}

	public void testRecyclerVieNotNull() {
		RecyclerView exeListView = (RecyclerView) activity.findViewById(R.id.recycler_view);
		assertNotNull(exeListView);
	}

}