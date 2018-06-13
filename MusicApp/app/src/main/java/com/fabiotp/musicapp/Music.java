package com.fabiotp.musicapp;

/**
 * Created by ftp91 on 09-Mar-18.
 */

public class Music {

  private String musicName;
  private String bandName;
  private int imageResourceId;

  public Music(String musicName, String bandName, int imageResourceId) {
    this.musicName = musicName;
    this.bandName = bandName;
    this.imageResourceId = imageResourceId;
  }

  //Get the name of the music
  public String getMusicName() {
    return musicName;
  }

  //Get the band name
  public String getBandName() {
    return bandName;
  }

  //Get the image resource ID
  public int getImageResourceId() {
    return imageResourceId;
  }


}