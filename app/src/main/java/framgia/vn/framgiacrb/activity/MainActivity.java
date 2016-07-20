package framgia.vn.framgiacrb.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.xdty.preference.colorpicker.ColorPickerDialog;
import org.xdty.preference.colorpicker.ColorPickerSwatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.ListMenuAdapter;
import framgia.vn.framgiacrb.adapter.MonthToolbarPagerAdapter;
import framgia.vn.framgiacrb.fragment.EventFollowWeekFragment;
import framgia.vn.framgiacrb.fragment.EventsFragment;
import framgia.vn.framgiacrb.fragment.MonthFragment;
import framgia.vn.framgiacrb.fragment.item.ItemLeftMenu;
import framgia.vn.framgiacrb.ui.CustomMonthCalendarView;
import framgia.vn.framgiacrb.ui.MonthView;
import framgia.vn.framgiacrb.ui.WrapContentHeightViewPager;
import framgia.vn.framgiacrb.utils.TimeUtils;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_MENU_ITEM = "currentMenuItem";
    public static final String ACTION_BROADCAST = "DAY_CLICKED";
    private static final String HOME = "Home";
    private static final String WEEK = "Week";
    private static final String MONTH = "Month";
    private static final String COLOR = "Color";
    private static final int ANIMATION_DURATION = 300;
    private static final int NUMBER_COLUMN = 5;

    private DrawerLayout mDrawerLayout;
    private ListView mNavigationListView;
    private ArrayList<ItemLeftMenu> mListMenu;
    private ListMenuAdapter mListMenuAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private AppBarLayout mAppBarLayout;
    private CustomMonthCalendarView mCustomMonthCalendarView;
    private TextView mDatePickerTextView;
    private ImageView mArrow;
    private dayClicked mDayClicked;
    private RelativeLayout mDatePickerButton;
    private FrameLayout mFrameLayout;
    private WrapContentHeightViewPager mCalendarViewPager;
    private MonthToolbarPagerAdapter mAdapter;

    int currentMenuItemId;
    boolean isExpanded = false;
    float currentRotation = 360.0f;

    private Toolbar mToolbar;

    private int mSelectedColor;
    private int mPreviousSelected;
    private int mPreviousSelectedMonth;
    private int mPreviousSelectedYear;
    private int mCurrentPosition;
    private int X;
    private int Y;
    private int mCurrentMenuItemPosition;

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
        mCurrentMenuItemPosition = 1;
        mNavigationListView.setItemChecked(mCurrentMenuItemPosition, true);
        updateDisplayView(mCurrentMenuItemPosition);
        TimeUtils.generateRangeDate();
    } // end of method onCreate

    private void initUi() {
        mDatePickerTextView = (TextView) findViewById(R.id.date_picker_text_view);
        mArrow = (ImageView) findViewById(R.id.date_picker_arrow);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                isExpanded = (verticalOffset == 0);
            }
        });
//        mCustomMonthCalendarView = (CustomMonthCalendarView) findViewById(R.id.calendar_view);
//        mCustomMonthCalendarView.setAdapter(getSupportFragmentManager());
        mDatePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
//        mCustomMonthCalendarView = (CustomMonthCalendarView) findViewById(R.id.calendar_view);
//        mCustomMonthCalendarView.setAdapter(getSupportFragmentManager());
        mCalendarViewPager = (WrapContentHeightViewPager) findViewById(R.id.calendar_view_pager);
        mAdapter = new MonthToolbarPagerAdapter(this);
        mCalendarViewPager.setAdapter(mAdapter);
        Calendar calendar = Calendar.getInstance();
        mCalendarViewPager.setCurrentItem((calendar.get(Calendar.YEAR) - MonthToolbarPagerAdapter.MIN_YEAR) * 12 + calendar.get(Calendar.MONTH));
        mPreviousSelected = (calendar.get(Calendar.YEAR) - MonthToolbarPagerAdapter.MIN_YEAR) * 12 + calendar.get(Calendar.MONTH);
        mCurrentPosition = mPreviousSelected;
        mPreviousSelectedMonth = calendar.get(Calendar.MONTH);
        mPreviousSelectedYear = calendar.get(Calendar.YEAR);
        mCalendarViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mPreviousSelected) {
                    MonthView mv = (MonthView) mCalendarViewPager.findViewWithTag("month" + mPreviousSelected);
                    mv.setSelected(false);
                } else if (position == mCurrentPosition){
                    MonthView mv = (MonthView) mCalendarViewPager.findViewWithTag("month"+mCurrentPosition);
                    mv.setSelect(X, Y);
                    mv.setSelected(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mDatePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
        setSubTitle(dateFormat.format(calendar.getTime()));
        mFrameLayout = (FrameLayout) findViewById(R.id.frame);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationListView = (ListView) findViewById(R.id.navigation_view);
        initMenu();
    } // end of method initUi()

    private void initMenu() {
        mListMenu = new ArrayList<>();
        ItemLeftMenu header = new ItemLeftMenu();
        header.setImageResource(R.drawable.profile);
        header.setTitle("Lucky Luke");
        ItemLeftMenu home = new ItemLeftMenu();
        home.setImageResource(R.drawable.ic_home);
        home.setTitle(HOME);
        ItemLeftMenu week = new ItemLeftMenu();
        week.setImageResource(R.drawable.ic_view_week);
        week.setTitle(WEEK);
        ItemLeftMenu month = new ItemLeftMenu();
        month.setImageResource(R.drawable.ic_view_module_black_24dp);
        month.setTitle(MONTH);
        ItemLeftMenu color = new ItemLeftMenu();
        color.setImageResource(R.drawable.ic_color_lens_black_24dp);
        color.setTitle(COLOR);
        mListMenu.add(header);
        mListMenu.add(home);
        mListMenu.add(week);
        mListMenu.add(month);
        mListMenu.add(color);
        mListMenuAdapter = new ListMenuAdapter(this, mListMenu);
        mNavigationListView.setAdapter(mListMenuAdapter);
        //mNavigationListView
    }

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
        mNavigationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateDisplayView(position);
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
        mNavigationListView.setSelection(3);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                // For show Hambuger Menu icon
                mDrawerToggle.syncState();
            }
        });
    }

    private void updateDisplayView(int position) {
        ItemLeftMenu item = mListMenu.get(position);
        Fragment fragment = null;
        switch (item.getTitle()) {
            case HOME:
                fragment = new EventsFragment();
                Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                break;
            case WEEK:
                fragment = new EventFollowWeekFragment();
                Toast.makeText(MainActivity.this, "week", Toast.LENGTH_SHORT).show();
                break;
            case MONTH:
                fragment = new MonthFragment();
                Toast.makeText(MainActivity.this, "month", Toast.LENGTH_SHORT).show();
                break;
            case COLOR:
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
        }
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame, fragment).commit();
        }
        if (position != 0) {
            mDrawerLayout.closeDrawers();
            mCurrentMenuItemPosition = position;
        } else {
            Toast.makeText(MainActivity.this, mCurrentMenuItemPosition+"", Toast.LENGTH_SHORT).show();
            mNavigationListView.setItemChecked(position, false);
            mNavigationListView.setItemChecked(mCurrentMenuItemPosition, true);
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
        outState.putInt(CURRENT_MENU_ITEM, mCurrentMenuItemPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentMenuItemPosition = savedInstanceState.getInt(CURRENT_MENU_ITEM, 1);
        //reCheckMenuItem(mNavigationView);
        updateDisplayView(mCurrentMenuItemPosition);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setSubTitle(String date) {
        if (null != mDatePickerTextView) {
            mDatePickerTextView.setText(date);
        }
    }

    private void setCalendarCheck(int year, int month) {
        if ((year == mPreviousSelectedYear && month != mPreviousSelectedMonth) ||
                (year != mPreviousSelectedYear)) {
            mPreviousSelected = mCurrentPosition;
            mCurrentPosition = (year - MonthToolbarPagerAdapter.MIN_YEAR) * 12 + month;
            mPreviousSelectedYear = year;
            mPreviousSelectedMonth = month;
        }
    }

    private class dayClicked extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_BROADCAST)) {
                setSubTitle(intent.getStringExtra(MonthView.TITLE));
                Calendar calendar = Calendar.getInstance();
                int year = intent.getIntExtra(MonthView.YEAR, calendar.get(Calendar.YEAR));
                int month = intent.getIntExtra(MonthView.MONTH, calendar.get(Calendar.MONTH));
                X = intent.getIntExtra(MonthView.X_AXIS, 0);
                Y = intent.getIntExtra(MonthView.Y_AXIS, 0);
                setCalendarCheck(year, month);
                closeToolbar();
            }
        }
    }
}
