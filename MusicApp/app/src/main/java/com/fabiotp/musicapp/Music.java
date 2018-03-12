package com.fabiotp.musicapp;

/**
 * Created by ftp91 on 09-Mar-18.
 */

public class Music {

  private String mMusicName;
  private String mBandName;
  private Integer mImageResourceId;

  public Music(String musicName, String bandName, int imageResourceId) {
    this.mMusicName = musicName;
    this.mBandName = bandName;
    this.mImageResourceId = imageResourceId;
  }

  //Get the name of the music
  public String getMusicName() {
    return mMusicName;
  }

  //Get the band name
  public String getBandName() {
    return mBandName;
  }

  //Get the image resource ID
  public int getImageResourceId() {
    return mImageResourceId;
  }


}