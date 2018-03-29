package com.fabiotp.tourguide;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_main);
    TourGuideAdapter adapter = new TourGuideAdapter(this, getSupportFragmentManager());
    viewPager.setAdapter(adapter);

    /*
      Find the tab layout that shows the tabs
      Connect the tab layout with the view pager.
     */
    TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_main);
    tabLayout.setupWithViewPager(viewPager);
  }
}
