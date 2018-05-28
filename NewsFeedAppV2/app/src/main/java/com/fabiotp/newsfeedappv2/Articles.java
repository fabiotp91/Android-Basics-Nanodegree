package com.fabiotp.newsfeedappv2;

public class Articles {

  private String appTitle;
  private String appSection;
  private String appDate;
  private String appUrl;
  private String appAuthor;

  /**
   * Constructs a new {@link Articles} object.
   *
   * @param title is the title of the article.
   * @param author is the author of the article.
   * @param url is the website URL to find more details about the article.
   * @param date is the date the article was published.
   * @param section is the section the article belongs to.
   */
  public Articles(String title, String section, String date, String url, String author) {
    this.appTitle = title;
    this.appSection = section;
    this.appDate = date;
    this.appUrl = url;
    this.appAuthor = author;
  }

  // Return the title of the article
  public String getTitle() {
    return appTitle;
  }

  // Return the section of the article
  public String getSection() {
    return appSection;
  }

  // Return the date of the article
  public String getDate() {
    return appDate;
  }

  // Return the website URL of the article
  public String getUrl() {
    return appUrl;
  }

  // Return the author of the article
  public String getAuthor() {
    return appAuthor;
  }

}
