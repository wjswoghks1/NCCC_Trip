package com.exam.administrator.nccc_trip;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    ImageView menu;
    ImageView searcher;
    MenuOnClickListner menuListener;

    ImageView calendar;
    ImageView inventory;
    ImageView tourist;
    ImageView dairy;
    ImageView setting;
    NavigationOnClickListener naviListener;
    DrawerLayout dLayout;

    RegionFragment regionFragment;
    TripFragment tripFragment;
    TasteFragment tasteFragment;
    HotelFragment hotelFragment;
    CourseFragment courseFragment;
    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menu = (ImageView) findViewById(R.id.menu);
        searcher = (ImageView) findViewById(R.id.searcher);
        dLayout = (DrawerLayout) findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this,dLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.setDrawerListener(toggle);
        toggle.syncState();

        menuListener = new MenuOnClickListner() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.menu:
                        dLayout.openDrawer(GravityCompat.START,true);
                        break;
                    case R.id.searcher:
                        Intent i = new Intent(MainActivity.this,SearchActivity.class);
                        startActivity(i);
                        break;
                }
            }
        };
        menu.setOnClickListener(menuListener);
        searcher.setOnClickListener(menuListener);


        // Adding Toolbar to the activity
        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        toolbar.setContentInsetsAbsolute(0,0);
        setSupportActionBar(toolbar);

        calendar = (ImageView)findViewById(R.id.calendar_icon);
        inventory = (ImageView)findViewById(R.id.inventory_icon);
        dairy = (ImageView)findViewById(R.id.dairy_icon);
        tourist = (ImageView)findViewById(R.id.tourist_icon);
        setting = (ImageView)findViewById(R.id.setting_icon);

        naviListener = new NavigationOnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                switch(v.getId()){
                    case R.id.calendar_icon:
                        i = new Intent(MainActivity.this,CalendarActivity.class);
                        startActivity(i);
                        dLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.inventory_icon:
                        i = new Intent(MainActivity.this,InventoryActivity.class);
                        startActivity(i);
                        dLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.dairy_icon:
                        i = new Intent(MainActivity.this,DairyActivity.class);
                        startActivity(i);
                        dLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.tourist_icon:
                        i = new Intent(MainActivity.this,TouristActivity.class);
                        startActivity(i);
                        dLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.setting_icon:
                        i = new Intent(MainActivity.this,SettingActivity.class);
                        startActivity(i);
                        dLayout.closeDrawer(GravityCompat.START);
                        break;
                }
            }
        };
        calendar.setOnClickListener(naviListener);
        inventory.setOnClickListener(naviListener);
        dairy.setOnClickListener(naviListener);
        tourist.setOnClickListener(naviListener);
        setting.setOnClickListener(naviListener);

        regionFragment = new RegionFragment();
        tripFragment = new TripFragment();
        tasteFragment = new TasteFragment();
        hotelFragment = new HotelFragment();
        courseFragment = new CourseFragment();

        // Initializing the TabLayout
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("지역"));
        tabLayout.addTab(tabLayout.newTab().setText("관광"));
        tabLayout.addTab(tabLayout.newTab().setText("맛집"));
        tabLayout.addTab(tabLayout.newTab().setText("숙박"));
        tabLayout.addTab(tabLayout.newTab().setText("코스"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, regionFragment).commit();
        // Set TabSelectedListener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;
                if (position == 0) {
                    selected = regionFragment;
                } else if (position == 1) {
                    selected = tripFragment;
                } else if (position == 2) {
                    selected = tasteFragment;
                } else if (position == 3) {
                    selected = hotelFragment;
                } else if (position == 4) {
                    selected = courseFragment;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }


    @Override
    public void onBackPressed(){
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("정말 종료하시겠습니까?");
        d.setPositiveButton("예", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // process전체 종료
                finish();
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        d.show();
    }
}
