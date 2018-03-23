package com.example.android.miwok;

/**
 * Created by ftp91 on 04-Mar-18.
 */

public class Word {

  /**
   * Default translation for the word
   */
  private String mDefaultTranslation;

  /**
   * Miwok translation for the word
   */
  private String mMiwokTranslation;

  private int mImageResourceId = NO_IMAGE_PROVIDED;

  private static final int NO_IMAGE_PROVIDED = -1;

  private int mAudioResource;

  public Word(String defaultTranslation, String miwokTranslation, int audioResourceId) {
    mDefaultTranslation = defaultTranslation;
    mMiwokTranslation = miwokTranslation;
    mAudioResource = audioResourceId;
  }

  public Word(String defaultTranslation, String miwokTranslation, int imageResourceId,
      int audioResourceId) {
    mDefaultTranslation = defaultTranslation;
    mMiwokTranslation = miwokTranslation;
    mImageResourceId = imageResourceId;
    mAudioResource = audioResourceId;
  }

  /**
   * Get the default translation of the word.
   */
  public String getDefaultTranslation() {
    return mDefaultTranslation;
  }

  /**
   * Get the Miwok translation of the word.
   */
  public String getMiwokTranslation() {
    return mMiwokTranslation;
  }

  public int getImageResourceId() {
    return mImageResourceId;
  }

  //Returns true or false if word has an image
  public boolean hasImage() {
    return mImageResourceId != NO_IMAGE_PROVIDED;
  }

  public int getAudioResourceId() {
    return mAudioResource;
  }

}
