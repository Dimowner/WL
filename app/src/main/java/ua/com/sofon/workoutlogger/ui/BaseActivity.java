package ua.com.sofon.workoutlogger.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import ua.com.sofon.workoutlogger.R;
import ua.com.sofon.workoutlogger.ui.widget.BezelImageView;
import ua.com.sofon.workoutlogger.ui.widget.ScrimInsetsScrollView;

/**
 * Base activity with base functionality and drawer layout.
 * @author Dimowner
 */
public class BaseActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler();
	}

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
	private void setupNavDrawer() {
		// What nav drawer item should be selected?
		int selfItem = getSelfNavDrawerItem();

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (mDrawerLayout == null) {
			return;
		}
		mDrawerLayout.setStatusBarBackgroundColor(
				getResources().getColor(R.color.theme_primary_dark));
		ScrimInsetsScrollView navDrawer = (ScrimInsetsScrollView)
				mDrawerLayout.findViewById(R.id.navdrawer);
		if (selfItem == NAVDRAWER_ITEM_INVALID) {
			// do not show a nav drawer
			if (navDrawer != null) {
				((ViewGroup) navDrawer.getParent()).removeView(navDrawer);
			}
			mDrawerLayout = null;
			return;
		}

		if (navDrawer != null) {
			final View chosenAccountContentView = findViewById(R.id.chosen_account_content_view);
			final View chosenAccountView = findViewById(R.id.chosen_account_view);
			final int navDrawerChosenAccountHeight = getResources().getDimensionPixelSize(
					R.dimen.navdrawer_chosen_account_height);
			navDrawer.setOnInsetsCallback(new ScrimInsetsScrollView.OnInsetsCallback() {
				@Override
				public void onInsetsChanged(Rect insets) {
					ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)
							chosenAccountContentView.getLayoutParams();
					lp.topMargin = insets.top;
					chosenAccountContentView.setLayoutParams(lp);

					ViewGroup.LayoutParams lp2 = chosenAccountView.getLayoutParams();
					lp2.height = navDrawerChosenAccountHeight + insets.top;
					chosenAccountView.setLayoutParams(lp2);
				}
			});

			BezelImageView profileIcon = (BezelImageView) findViewById(R.id.profile_image);
			profileIcon.setVisibility(View.INVISIBLE);
		}

		if (mActionBarToolbar != null) {
			// Static navigation icon.
//			mActionBarToolbar.setNavigationIcon(R.drawable.ic_drawer);
//			mActionBarToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View view) {
//					mDrawerLayout.openDrawer(Gravity.START);
//				}
//			});

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
			mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
		}
		// populate the nav drawer with the correct items
		populateNavDrawer();

		// When the user runs the app for the first time, we want to land them with the
		// navigation drawer open. But just the first time.
//		if (!PrefUtils.isWelcomeDone(this)) {
//			// first run of the app starts with the nav drawer open
//			PrefUtils.markWelcomeDone(this);
//			mDrawerLayout.openDrawer(Gravity.START);
//		}
	}

	protected boolean isNavDrawerOpen() {
		return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(Gravity.START);
	}

	protected void closeNavDrawer() {
		if (mDrawerLayout != null) {
			mDrawerLayout.closeDrawer(Gravity.START);
		}
	}

	/** Populates the navigation drawer with the appropriate items. */
	private void populateNavDrawer() {
		mNavDrawerItems.clear();

		// Explore is always shown
		mNavDrawerItems.add(NAVDRAWER_ITEM_WORKOUTS);
		mNavDrawerItems.add(NAVDRAWER_ITEM_EXERCISES);
		mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR);

		mNavDrawerItems.add(NAVDRAWER_ITEM_WEIGHT);
		mNavDrawerItems.add(NAVDRAWER_ITEM_STATISTICS);
		mNavDrawerItems.add(NAVDRAWER_ITEM_SEPARATOR_SPECIAL);

		mNavDrawerItems.add(NAVDRAWER_ITEM_SETTINGS);
		createNavDrawerItems();
	}

	@Override
	public void onBackPressed() {
		if (isNavDrawerOpen()) {
			closeNavDrawer();
		} else {
			super.onBackPressed();
		}
	}

	private void createNavDrawerItems() {
		mDrawerItemsListContainer = (ViewGroup) findViewById(R.id.navdrawer_items_list);
		if (mDrawerItemsListContainer == null) {
			return;
		}

		mNavDrawerItemViews = new View[mNavDrawerItems.size()];
		mDrawerItemsListContainer.removeAllViews();
		int i = 0;
		for (int itemId : mNavDrawerItems) {
			mNavDrawerItemViews[i] = makeNavDrawerItem(itemId, mDrawerItemsListContainer);
			mDrawerItemsListContainer.addView(mNavDrawerItemViews[i]);
			++i;
		}
	}

	/**
	 * Sets up the given navdrawer item's appearance to the selected state. Note: this could
	 * also be accomplished (perhaps more cleanly) with state-based layouts.
	 */
	private void setSelectedNavDrawerItem(int itemId) {
		if (mNavDrawerItemViews != null) {
			for (int i = 0; i < mNavDrawerItemViews.length; i++) {
				if (i < mNavDrawerItems.size()) {
					int thisItemId = mNavDrawerItems.get(i);
					formatNavDrawerItem(mNavDrawerItemViews[i], thisItemId, itemId == thisItemId);
				}
			}
		}
	}

	private View makeNavDrawerItem(final int itemId, ViewGroup container) {
		boolean selected = getSelfNavDrawerItem() == itemId;
		int layoutToInflate = 0;
		if (itemId == NAVDRAWER_ITEM_SEPARATOR) {
			layoutToInflate = R.layout.navdrawer_separator;
		} else if (itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL) {
			layoutToInflate = R.layout.navdrawer_separator;
		} else {
			layoutToInflate = R.layout.navdrawer_item;
		}
		View view = getLayoutInflater().inflate(layoutToInflate, container, false);

		if (isSeparator(itemId)) {
			// we are done
//			UIUtils.setAccessibilityIgnore(view);
			return view;
		}

		ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		TextView titleView = (TextView) view.findViewById(R.id.title);
		int iconId = itemId >= 0 && itemId < NAVDRAWER_ICON_RES_ID.length ?
				NAVDRAWER_ICON_RES_ID[itemId] : 0;
		int titleId = itemId >= 0 && itemId < NAVDRAWER_TITLE_RES_ID.length ?
				NAVDRAWER_TITLE_RES_ID[itemId] : 0;

		// set icon and text
		iconView.setVisibility(iconId > 0 ? View.VISIBLE : View.GONE);
		if (iconId > 0) {
			iconView.setImageResource(iconId);
		}
		titleView.setText(getString(titleId));

		formatNavDrawerItem(view, itemId, selected);

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onNavDrawerItemClicked(itemId);
			}
		});

		return view;
	}

	private boolean isSeparator(int itemId) {
		return itemId == NAVDRAWER_ITEM_SEPARATOR || itemId == NAVDRAWER_ITEM_SEPARATOR_SPECIAL;
	}

	private boolean isSpecialItem(int itemId) {
		return itemId == NAVDRAWER_ITEM_SETTINGS;
	}

	private void formatNavDrawerItem(View view, int itemId, boolean selected) {
		if (isSeparator(itemId)) {
			// not applicable
			return;
		}

		ImageView iconView = (ImageView) view.findViewById(R.id.icon);
		TextView titleView = (TextView) view.findViewById(R.id.title);

		if (selected) {
			view.setBackgroundResource(R.drawable.selected_navdrawer_item_background);
		}

		// configure its appearance according to whether or not it's selected
		titleView.setTextColor(selected ?
				getResources().getColor(R.color.navdrawer_text_color_selected) :
				getResources().getColor(R.color.navdrawer_text_color));
		iconView.setColorFilter(selected ?
				getResources().getColor(R.color.navdrawer_icon_tint_selected) :
				getResources().getColor(R.color.navdrawer_icon_tint));
	}

	private void onNavDrawerItemClicked(final int itemId) {
		if (itemId == getSelfNavDrawerItem()) {
			mDrawerLayout.closeDrawer(Gravity.START);
			return;
		}

		if (isSpecialItem(itemId)) {
			goToNavDrawerItem(itemId);
		} else {
			// launch the target Activity after a short delay, to allow the close animation to play
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					goToNavDrawerItem(itemId);
				}
			}, NAVDRAWER_LAUNCH_DELAY);

			// change the active item on the list so the user can see the item changed
			setSelectedNavDrawerItem(itemId);
//			// fade out the main content
//			View mainContent = findViewById(R.id.main_content);
//			if (mainContent != null) {
//				mainContent.animate().alpha(0).setDuration(MAIN_CONTENT_FADEOUT_DURATION);
//			}
		}

		mDrawerLayout.closeDrawer(Gravity.START);
	}

	private void goToNavDrawerItem(int item) {
		Intent intent;
		switch (item) {
			case NAVDRAWER_ITEM_WORKOUTS:
				intent = new Intent(this, WorkoutsActivity.class);
				startActivity(intent);
				finish();
				break;
			case NAVDRAWER_ITEM_EXERCISES:
				intent = new Intent(this, ExercisesActivity.class);
				startActivity(intent);
				finish();
				break;
			case NAVDRAWER_ITEM_WEIGHT:
				intent = new Intent(this, WeightActivity.class);
				startActivity(intent);
				finish();
				break;
			case NAVDRAWER_ITEM_STATISTICS:
				intent = new Intent(this, StatisticsActivity.class);
				startActivity(intent);
				finish();
				break;
			case NAVDRAWER_ITEM_SETTINGS:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
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
	protected static final int NAVDRAWER_ITEM_WORKOUTS = 0;
	protected static final int NAVDRAWER_ITEM_EXERCISES = 1;
	protected static final int NAVDRAWER_ITEM_WEIGHT = 2;
	protected static final int NAVDRAWER_ITEM_STATISTICS = 3;
	protected static final int NAVDRAWER_ITEM_SETTINGS = 4;
	protected static final int NAVDRAWER_ITEM_INVALID = -1;
	protected static final int NAVDRAWER_ITEM_SEPARATOR = -2;
	protected static final int NAVDRAWER_ITEM_SEPARATOR_SPECIAL = -3;


	// titles for navdrawer items (indices must correspond to the above)
	private static final int[] NAVDRAWER_TITLE_RES_ID = new int[]{
			R.string.navdrawer_item_workouts,
			R.string.navdrawer_item_exercises,
			R.string.navdrawer_item_weight,
			R.string.navdrawer_item_statistics,
			R.string.navdrawer_item_settings,
			R.string.navdrawer_item_about
	};

	// icons for navdrawer items (indices must correspond to above array)
	private static final int[] NAVDRAWER_ICON_RES_ID = new int[] {
			R.drawable.ic_android_grey600_24dp, // Workouts
			R.drawable.ic_android_grey600_24dp, // Exercises
			R.drawable.ic_android_grey600_24dp, // Weight
			R.drawable.ic_android_grey600_24dp, // Statistics
			R.drawable.ic_android_grey600_24dp, // Settings
			R.drawable.ic_android_grey600_24dp	// About
	};

	// delay to launch nav drawer item, to allow close animation to play
	private static final int NAVDRAWER_LAUNCH_DELAY = 250;

	// fade in and fade out durations for the main content when switching between
	// different Activities of the app through the Nav Drawer
	private static final int MAIN_CONTENT_FADEOUT_DURATION = 150;
	private static final int MAIN_CONTENT_FADEIN_DURATION = 250;

	// list of navdrawer items that were actually added to the navdrawer, in order
	private ArrayList<Integer> mNavDrawerItems = new ArrayList<>();

	// views that correspond to each navdrawer item, null if not yet created
	private View[] mNavDrawerItemViews = null;

	// Primary toolbar and drawer toggle
	private Toolbar mActionBarToolbar;

	// Navigation drawer:
	protected DrawerLayout mDrawerLayout;
	protected ActionBarDrawerToggle mDrawerToggle;

	private ViewGroup mDrawerItemsListContainer;
	private Handler mHandler;
}
