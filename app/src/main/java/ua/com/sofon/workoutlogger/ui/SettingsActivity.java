package ua.com.sofon.workoutlogger.ui;

import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.util.PrefUtils;
import static ua.com.sofon.workoutlogger.util.LogUtils.LOGV;

/**
 * Activity for customizing app settings.
 */
public class SettingsActivity extends ActionBarActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		toolbar.setTitle(R.string.activity_settings);
		toolbar.setNavigationIcon(R.drawable.ic_arrow_left_grey600_24dp);
		toolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				navigateUpToFromChild(SettingsActivity.this,
						IntentCompat.makeMainActivity(new ComponentName(SettingsActivity.this,
								MainActivity.class)));
			}
		});

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new SettingsFragment())
					.commit();
		}
	}

	public static class SettingsFragment extends PreferenceFragment
			implements SharedPreferences.OnSharedPreferenceChangeListener {
		public SettingsFragment() {
		}

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setupSimplePreferencesScreen();
            PrefUtils.registerOnSharedPreferenceChangeListener(getActivity(), this);
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
            PrefUtils.unregisterOnSharedPreferenceChangeListener(getActivity(), this);
		}

		private void setupSimplePreferencesScreen() {
			// Add 'general' preferences.
			addPreferencesFromResource(R.xml.preferences);
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			LOGV("SettingsActivity", "onPreferenceChanged");
		}
	}
}
