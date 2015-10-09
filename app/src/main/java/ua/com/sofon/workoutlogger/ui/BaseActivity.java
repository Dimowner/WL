package ua.com.sofon.workoutlogger.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import ua.com.sofon.workoutlogger.R;

/**
 * Base activity with base functionality and drawer layout.
 * @author Dimowner
 */
public class BaseActivity extends AppCompatActivity {

	//TODO:  Add search by exercises in toolbar.
	//TODO:  Add into settings licence item.
	//TODO:  Add sort to exercises and workouts.
	//TODO:  Add section "Training Programs".
	//TODO:  Add to weight activity ability switch to view type List.
	//TODO:  Add export/import weight DB to/from Libra app.
	//TODO:  Use unit test in this project.
	//TODO:  Add feature on long click show menu with item delete item from trained exes.
	//TODO:  Replace all parts constructors by builders.

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		setupNavDrawer();
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		getActionBarToolbar();
	}

	/**
	 * Returns the navigation drawer item that corresponds to this Activity. Subclasses
	 * of BaseActivity override this to indicate what nav drawer item corresponds to them
	 * Return NAVDRAWER_ITEM_INVALID to mean that this Activity should not have a Nav Drawer.
	 */
	protected int getSelfNavDrawerItem() {
		return NAVDRAWER_ITEM_INVALID;
	}

	/**
	 * Sets up the navigation drawer as appropriate. Note that the nav drawer will be
	 * different depending on whether the attendee indicated that they are attending the
	 * event on-site vs. attending remotely.
	 */
	protected void setupNavDrawer() {
		// What nav drawer item should be selected?
		int selfItem = getSelfNavDrawerItem();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (mDrawerLayout == null) {
			return;
		}
		navigationView = (NavigationView) findViewById(R.id.nav_view);
		if (navigationView != null) {
			navigationView.setNavigationItemSelectedListener(
					new NavigationView.OnNavigationItemSelectedListener() {
						@Override
						public boolean onNavigationItemSelected(MenuItem menuItem) {
							menuItem.setChecked(true);
							mDrawerLayout.closeDrawers();
							if (getSelfNavDrawerItem() != menuItem.getItemId()) {
								goToNavDrawerItem(menuItem.getItemId());
							}
							return true;
						}
					});
			if (selfItem > NAVDRAWER_ITEM_INVALID) {
				navigationView.getMenu().findItem(selfItem).setChecked(true);
			}
		}

		if (mActionBarToolbar != null) {
			// Static navigation icon.
			mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
			mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					mDrawerLayout.openDrawer(Gravity.LEFT);
				}
			});

			// Dynamic navigation icon
			// ActionBarDrawerToggle ties together the the proper interactions
			// between the sliding drawer and the action bar app icon
			mDrawerToggle = new ActionBarDrawerToggle(
					this,                  /* host Activity */
					mDrawerLayout,         /* DrawerLayout object */
					mActionBarToolbar,
					R.string.drawer_open,  /* "open drawer" description for accessibility */
					R.string.drawer_closed  /* "close drawer" description for accessibility */
			) {
				public void onDrawerClosed(View view) {
//					invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				}

				public void onDrawerOpened(View drawerView) {
//					invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
				}
			};

			// Sync the toggle state after onRestoreInstanceState has occurred.
			mDrawerToggle.syncState();
			mDrawerLayout.setDrawerListener(mDrawerToggle);
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.LEFT);
		}
	}

	protected boolean isNavDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.LEFT);
	}

	protected void closeNavDrawer() {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(Gravity.LEFT);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (navigationView != null && getSelfNavDrawerItem() > NAVDRAWER_ITEM_INVALID) {
			navigationView.getMenu().findItem(getSelfNavDrawerItem()).setChecked(true);
		}
	}

	@Override
	public void onBackPressed() {
		if (isNavDrawerOpen()) {
			closeNavDrawer();
		} else {
			super.onBackPressed();
		}
	}

	private void goToNavDrawerItem(int itemID) {

		switch (itemID) {
			case NAVDRAWER_ITEM_TRAINING:
				startActivity(new Intent(this, TrainingsActivity.class));
				finish();
				break;
			case NAVDRAWER_ITEM_WORKOUTS:
				startActivity(new Intent(this, WorkoutsActivity.class));
				finish();
				break;
			case NAVDRAWER_ITEM_EXERCISES:
				startActivity(new Intent(this, ExercisesActivity.class));
				finish();
				break;
			case NAVDRAWER_ITEM_WEIGHT:
				startActivity(new Intent(this, WeightActivity.class));
				finish();
				break;
			case NAVDRAWER_ITEM_STATISTICS:
				startActivity(new Intent(this, StatisticsActivity.class));
				finish();
				break;
			case NAVDRAWER_ITEM_SETTINGS:
				startActivity(new Intent(this, SettingsActivity.class));
				break;
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/**
	 * Get toolbar and init if need.
	 */
	protected Toolbar getActionBarToolbar() {
		if (mActionBarToolbar == null) {
			mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
			if (mActionBarToolbar != null) {
				setSupportActionBar(mActionBarToolbar);
			}
		}
		return mActionBarToolbar;
	}


	// symbols for navdrawer items (indices must correspond to array below). This is
	// not a list of items that are necessarily *present* in the Nav Drawer; rather,
	// it's a list of all possible items.
	protected static final int NAVDRAWER_ITEM_TRAINING		= R.id.nav_training;
	protected static final int NAVDRAWER_ITEM_WORKOUTS		= R.id.nav_workouts;
	protected static final int NAVDRAWER_ITEM_EXERCISES	= R.id.nav_exercises;
	protected static final int NAVDRAWER_ITEM_WEIGHT		= 0;//R.id.nav_weight;
	protected static final int NAVDRAWER_ITEM_STATISTICS	= 1;//R.id.nav_statistics;
	protected static final int NAVDRAWER_ITEM_SETTINGS		= R.id.nav_settings;
	protected static final int NAVDRAWER_ITEM_INVALID		= -1;

	// Primary toolbar and drawer toggle
	private Toolbar mActionBarToolbar;

	// Navigation drawer:
	protected DrawerLayout mDrawerLayout;
	protected NavigationView navigationView;
	protected ActionBarDrawerToggle mDrawerToggle;
}
