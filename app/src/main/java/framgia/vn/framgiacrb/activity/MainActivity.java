package framgia.vn.framgiacrb.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.fragment.CalendarFragment;
import framgia.vn.framgiacrb.fragment.EventFollowWeekFragment;
import framgia.vn.framgiacrb.fragment.EventsFragment;
import framgia.vn.framgiacrb.fragment.MonthFragment;
import framgia.vn.framgiacrb.ui.CustomMonthCalendarView;


public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_MENU_ITEM = "currentMenuItem";
    public static final String ACTION_BROADCAST = "DAY_CLICKED";
    private static final int ANIMATION_DURATION = 100;
    private static final int NUMBER_COLUMN = 5;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;
    private AppBarLayout mAppBarLayout;
    private CustomMonthCalendarView mCustomMonthCalendarView;
    private TextView mDatePickerTextView;
    private ImageView mArrow;
    private dayClicked mDayClicked;
    private RelativeLayout mDatePickerButton;
    private FrameLayout mFrameLayout;

    int currentMenuItemId;
    boolean isExpanded = false;
    float currentRotation = 360.0f;

    private Toolbar mToolbar;

    private int mSelectedColor;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initUi();
        assignHandler();
        updateDisplayView(R.id.home);
        currentMenuItemId = R.id.home;
    } // end of method onCreate

    private void initUi() {
        mDatePickerTextView = (TextView) findViewById(R.id.date_picker_text_view);
        mArrow = (ImageView) findViewById(R.id.date_picker_arrow);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mCustomMonthCalendarView = (CustomMonthCalendarView) findViewById(R.id.calendar_view);
        mCustomMonthCalendarView.setAdapter(getSupportFragmentManager());
        mDatePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
        Calendar calendar = Calendar.getInstance();
        setSubTitle(dateFormat.format(calendar.getTime()));
        mFrameLayout = (FrameLayout) findViewById(R.id.frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
    } // end of method initUi()

    @SuppressWarnings("deprecation")
    private void assignHandler() {
        mDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    closeToolbar();
                } else {
                    openToolbar();
                }
            }
        });
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                uncheckAllMenuItems(mNavigationView);
                if (item.getItemId() != R.id.color && item.getItemId() != R.id.setting) {
                    if (item.getItemId() != currentMenuItemId || item.getItemId() == R.id.color) {
                        updateDisplayView(item.getItemId());
                    }
                    currentMenuItemId = item.getItemId();
                } else {
                    item.setChecked(false);
                }
                reCheckMenuItem(mNavigationView);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                // For show Hambuger Menu icon
                mDrawerToggle.syncState();
            }
        });
    }

    private void updateDisplayView(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.home:
                fragment = new EventsFragment();
                Toast.makeText(this, "home", Toast.LENGTH_SHORT).show();
                break;
            case R.id.week:
                fragment = new EventFollowWeekFragment();
                Toast.makeText(this, "week", Toast.LENGTH_SHORT).show();
                break;
            case R.id.month:
                fragment = new MonthFragment();
                Toast.makeText(this, "month", Toast.LENGTH_SHORT).show();
                break;
            case R.id.color:
                int[] mColors = getResources().getIntArray(R.array.default_rainbow);
                final ColorPickerDialog dialog = ColorPickerDialog.newInstance(R.string.color_picker_default_title,
                        mColors,
                        mSelectedColor,
                        NUMBER_COLUMN,
                        ColorPickerDialog.SIZE_SMALL);
                dialog.setOnColorSelectedListener(new ColorPickerSwatch.OnColorSelectedListener() {

                    @Override
                    public void onColorSelected(int color) {
                        mSelectedColor = color;
                    }
                });
                dialog.show(getFragmentManager(), "color_dialog_test");
                break;
            default:
                fragment = new EventsFragment();
        }
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }
    }

    private void uncheckAllMenuItems(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    subMenuItem.setChecked(false);
                }
            } else {
                item.setChecked(false);
            }
        }
    }

    private void reCheckMenuItem(NavigationView navigationView) {
        Menu menu = navigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                SubMenu subMenu = item.getSubMenu();
                for (int j = 0; j < subMenu.size(); j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    if (subMenuItem.getItemId() == currentMenuItemId) subMenuItem.setChecked(true);
                    else subMenuItem.setChecked(false);
                }
            } else {
                if (item.getItemId() == currentMenuItemId) item.setChecked(true);
                else item.setChecked(false);
            }
        }
    }

    private void openToolbar() {
        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation - 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentRotation = (currentRotation - 180.0f) % 360.0f;
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setDuration(ANIMATION_DURATION);
        mArrow.startAnimation(anim);
        mAppBarLayout.setExpanded(true, true);
        isExpanded = true;
    }

    private void closeToolbar() {
        RotateAnimation anim = new RotateAnimation(currentRotation, currentRotation + 180.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        currentRotation = (currentRotation + 180.0f) % 360.0f;
        anim.setInterpolator(new LinearInterpolator());
        anim.setFillAfter(true);
        anim.setFillEnabled(true);
        anim.setDuration(ANIMATION_DURATION);
        mArrow.startAnimation(anim);
        mAppBarLayout.setExpanded(false, true);
        isExpanded = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.today:
                // TODO: 08/07/2016
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_settings:
                // TODO: 08/07/2016
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDayClicked = new dayClicked();
        IntentFilter intentFilter = new IntentFilter(ACTION_BROADCAST);
        registerReceiver(mDayClicked, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mDayClicked);
        mDayClicked = null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CURRENT_MENU_ITEM, currentMenuItemId);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        currentMenuItemId = savedInstanceState.getInt(CURRENT_MENU_ITEM, R.id.day);
        reCheckMenuItem(mNavigationView);
        updateDisplayView(currentMenuItemId);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setSubTitle(String date) {
        if (null != mDatePickerTextView) {
            mDatePickerTextView.setText(date);
        }
    }

    private class dayClicked extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_BROADCAST)) {
                setSubTitle(intent.getStringExtra(CalendarFragment.TITLE));
                closeToolbar();
            }
        }
    }
}