package com.fabiotp.tourguide;

/**
 * Created by ftp91 on 26-Mar-18.
 */

public class Location {

  // Location name
  private String appLocationName;
  // Details text
  private String appDetailsText;
  // Location Photo
  private int appImageResourceId = NO_IMAGE_PROVIDED;
  private static final int NO_IMAGE_PROVIDED = -1;

  public Location(String locationName, String detailsText, int imageResourceId){
    appLocationName = locationName;
    appDetailsText = detailsText;
    appImageResourceId = imageResourceId;
  }

  // Get the Strings and integer for the adapter
  public String getLocationName(){return appLocationName;}
  public String getDetailsText(){return appDetailsText;}
  public int getImageResourceId(){return appImageResourceId;}
  // Returns true or false if location has an image
  public boolean hasImage(){return appImageResourceId != NO_IMAGE_PROVIDED;}

}
