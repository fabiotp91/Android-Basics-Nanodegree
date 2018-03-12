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

  public Word(String defaultTranslation, String miwokTranslation) {
    mDefaultTranslation = defaultTranslation;
    mMiwokTranslation = miwokTranslation;
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

}
