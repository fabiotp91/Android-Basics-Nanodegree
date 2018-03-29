package com.fabiotp.tourguide;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by ftp91 on 26-Mar-18.
 */

public class TourGuideAdapter extends FragmentPagerAdapter {

  //Context of the app
  private Context appContext;

  public TourGuideAdapter(Context context, FragmentManager fm) {
    super(fm);
    appContext = context;
  }

  @Override
  public android.support.v4.app.Fragment getItem(int position) {
    if (position == 0) {
      return new SintraFragment();
    } else if (position == 1) {
      return new SightsFragment();
    } else if (position == 2) {
      return new FoodFragment();
    } else {
      return new HotelsFragment();
    }
  }

  @Override
  public int getCount(){return 4;}



  @Override
  public CharSequence getPageTitle(int position){
    if (position ==0){
      return appContext.getString(R.string.tab1_title);
    }else if(position==1){
      return appContext.getString(R.string.tab2_title);
    }else if(position ==2){
      return appContext.getString(R.string.tab3_title);
    }else {
      return appContext.getString(R.string.tab4_title);
    }
  }

}
