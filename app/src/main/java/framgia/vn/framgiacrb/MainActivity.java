package framgia.vn.framgiacrb;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String CURRENT_MENU_ITEM = "currentMenuItem";

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private ActionBarDrawerToggle mDrawerToggle;

    int currentMenuItemId;
    boolean isExpanded = false;
    float currentRotation = 360.0f;

    SubMenu subMenu;
    Toolbar toolbar;

    SimpleDateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", /*Locale.getDefault()*/Locale.ENGLISH);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initUi();
        updateDisplayView(R.id.day);
        currentMenuItemId = R.id.day;
    } // end of method onCreate

    @SuppressWarnings("deprecation")
    private void initUi() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                uncheckAllMenuItems(mNavigationView);
                item.setChecked(true);
                if (currentMenuItemId != item.getItemId()) {
                    updateDisplayView(item.getItemId());
                }
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
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
    } // end of method initUi()

    private void updateDisplayView(int id) {
        Fragment fragment;
        switch (id) {
            case R.id.day:
                break;
            case R.id.few_day:
                break;
            case R.id.month:
                break;
            case R.id.week:
                break;
            default:
        }
        currentMenuItemId = id;
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
}
