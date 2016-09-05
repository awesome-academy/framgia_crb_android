package framgia.vn.framgiacrb.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import framgia.vn.framgiacrb.R;
import framgia.vn.framgiacrb.adapter.ListMenuAdapter;
import framgia.vn.framgiacrb.adapter.MonthToolbarPagerAdapter;
import framgia.vn.framgiacrb.data.local.EventRepositoriesLocal;
import framgia.vn.framgiacrb.data.model.Session;
import framgia.vn.framgiacrb.fragment.EventFollowWeekFragment;
import framgia.vn.framgiacrb.fragment.EventsFragment;
import framgia.vn.framgiacrb.fragment.MonthFragment;
import framgia.vn.framgiacrb.fragment.item.ItemLeftMenu;
import framgia.vn.framgiacrb.ui.CustomMonthCalendarView;
import framgia.vn.framgiacrb.ui.MonthView;
import framgia.vn.framgiacrb.ui.WrapContentHeightViewPager;
import framgia.vn.framgiacrb.ui.listener.OnCloseToolbarListener;
import framgia.vn.framgiacrb.utils.DrawableUtil;
import framgia.vn.framgiacrb.utils.NotificationUtil;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    public static final String SHAREPREFF = "framgia.vn.framgiacrb.preference";
    private static final String CURRENT_MENU_ITEM = "currentMenuItem";
    public static final String ACTION_BROADCAST = "DAY_CLICKED";
    public static final String ACTION_TODAY = "GO_TO_TODAY";
    public static final String ACTION_SCROLL_DAY = "framgia.vn.framgiacrb.gotospecifyday";
    public static final String EMAIL_TITLE = "Email";
    public static final String NAME_TITLE = "Title";
    private static final String HOME = "Home";
    private static final String WEEK = "Week";
    private static final String MONTH = "Month";
    private static final String LABEL = "Calendar";
    private static final String LOGOUT = "Logout";
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
    private RealmResults<framgia.vn.framgiacrb.data.model.Calendar> mUserCalendar;
    private SharedPreferences mSharedPreferences;

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
    private int mTotalToolbarRange;
    private Fragment mCurrentFragment;
    private Fragment mEventFollowWeekFragment;
    private FragmentManager mFragmentManager;

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        mUserCalendar = new EventRepositoriesLocal(Realm.getDefaultInstance()).getAllCalendars();
        mSharedPreferences = getSharedPreferences(SHAREPREFF, Context.MODE_PRIVATE);
        mSelectedColor = ContextCompat.getColor(this, R.color.flamingo);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        initUi();
        assignHandler();
        mCurrentMenuItemPosition = 1;
        mNavigationListView.setItemChecked(mCurrentMenuItemPosition, true);
        updateDisplayView(mCurrentMenuItemPosition);
        currentMenuItemId = R.id.home;

    } // end of method onCreate

    private void initUi() {
        mDatePickerTextView = (TextView) findViewById(R.id.date_picker_text_view);
        mArrow = (ImageView) findViewById(R.id.date_picker_arrow);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mTotalToolbarRange = mAppBarLayout.getTotalScrollRange();
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                isExpanded = (verticalOffset == 0);
            }
        });
        mDatePickerButton = (RelativeLayout) findViewById(R.id.date_picker_button);
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
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, position / 12 + MonthToolbarPagerAdapter.MIN_YEAR);
                calendar.set(Calendar.MONTH, position % 12);
                calendar.set(Calendar.DAY_OF_MONTH, 1);
                sendBroadcastGotoToday(MainActivity.dateFormat.format(calendar.getTime()));
                Date date = calendar.getTime();
                setSubTitle(MainActivity.dateFormat.format(date));
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
        header.setTitle(mSharedPreferences.getString(NAME_TITLE, null));
        header.setEmail(mSharedPreferences.getString(EMAIL_TITLE, null));
        ItemLeftMenu home = new ItemLeftMenu();
        home.setImageResource(R.drawable.ic_home);
        home.setTitle(HOME);
        ItemLeftMenu week = new ItemLeftMenu();
        week.setImageResource(R.drawable.view_week);
        week.setTitle(WEEK);
        ItemLeftMenu month = new ItemLeftMenu();
        month.setImageResource(R.drawable.ic_view_month);
        month.setTitle(MONTH);
        ItemLeftMenu label = new ItemLeftMenu();
        label.setTitle(LABEL);
        ItemLeftMenu logout = new ItemLeftMenu();
        logout.setImageResource(R.drawable.ic_logout);
        logout.setTitle(LOGOUT);
        mListMenu.add(header);
        mListMenu.add(home);
        mListMenu.add(week);
        mListMenu.add(month);
        mListMenu.add(label);
        for (int i = 0; i < mUserCalendar.size(); i++) {
            ItemLeftMenu user = new ItemLeftMenu();
            user.setImageResource(R.drawable.ic_calendar_grey600);
            user.setTitle(mUserCalendar.get(i).getName());
            user.setCalendarId(mUserCalendar.get(i).getCalendarId());
            mListMenu.add(user);
        }
        mListMenu.add(logout);
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
//        mNavigationListView.setSelection(3);
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
        switch (item.getTitle()) {
            case HOME:
                mCurrentFragment = new EventsFragment();
                ((EventsFragment) mCurrentFragment).setOnCloseToolbarListener(new OnCloseToolbarListener() {
                    @Override
                    public void onCloseToolbar(boolean isClose) {
                        if (isClose) closeToolbar();
                    }
                });
                break;
            case WEEK:
                mCurrentFragment = new EventFollowWeekFragment();
                break;
            case MONTH:
                mCurrentFragment = new MonthFragment();
                break;
            case LOGOUT:
                NotificationUtil.clearNotification();
                logout();
                new EventRepositoriesLocal(Realm.getDefaultInstance()).clearDatabase(new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, getString(R.string.message_logout), Toast.LENGTH_SHORT).show();
                    }
                });
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                return;
            case LABEL:
                break;
        }
        if (mCurrentFragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.frame, mCurrentFragment).commit();
        }
        if (position != 0 && position < 4) {
            mDrawerLayout.closeDrawers();
            mCurrentMenuItemPosition = position;
        } else if (position == 0 || position == 4){
            Toast.makeText(MainActivity.this, mCurrentMenuItemPosition+"", Toast.LENGTH_SHORT).show();
            mNavigationListView.setItemChecked(position, false);
            mNavigationListView.setItemChecked(mCurrentMenuItemPosition, true);
        } else if (position > 4) {
            mDrawerLayout.closeDrawers();
            Session.sCalendarId = item.getCalendarId();
            SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(Session.CALENDAR_ID, Session.sCalendarId);
            editor.apply();
            Toast.makeText(MainActivity.this, ""+Session.sCalendarId, Toast.LENGTH_SHORT).show();
        }
    }

    private void logout() {
        new EventRepositoriesLocal(Realm.getDefaultInstance()).clearDatabase(new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(MainActivity.this, "Logout Success!", Toast.LENGTH_SHORT).show();
            }
        });
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.SHAREPREFF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void openToolbar() {
        mAppBarLayout.setExpanded(true, true);
        isExpanded = true;
    }

    private void closeToolbar() {
        mAppBarLayout.setExpanded(false, true);
        isExpanded = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.today).setIcon(DrawableUtil.writeOnDrawable(
            getApplicationContext(),
            R.drawable.ic_web_asset_white_24dp,
            DrawableUtil.getTodayDay()
        ));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.today:
                Intent intent = new Intent();
                intent.setAction(ACTION_TODAY);
                Calendar current = Calendar.getInstance();
                intent.putExtra(MonthView.YEAR, current.get(Calendar.YEAR));
                intent.putExtra(MonthView.MONTH, current.get(Calendar.MONTH));
                this.sendBroadcast(intent);
                mCalendarViewPager.setCurrentItem(
                        (current.get(Calendar.YEAR) - MonthToolbarPagerAdapter.MIN_YEAR) * 12
                        + current.get(Calendar.MONTH));
                setSubTitle(dateFormat.format(current.getTime()));
                break;
            case R.id.search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_refresh:
                if (mCurrentFragment instanceof EventsFragment) {
                    ((EventsFragment) mCurrentFragment).refreshData();
                }
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

    public void setSubTitle(String date) {
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
                sendBroadcastGotoToday(intent.getStringExtra(MonthView.TITLE));
                closeToolbar();
            }
        }
    }

    private void sendBroadcastGotoToday(String time) {
        Intent intent = new Intent();
        intent.setAction(MainActivity.ACTION_SCROLL_DAY);
        intent.putExtra(MonthView.TITLE, time);
        MainActivity.this.sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
