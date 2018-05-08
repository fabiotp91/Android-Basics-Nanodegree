package com.fabiotp.newsfeedapp;

public class Articles {

  private String title;
  private String author;
          String url;
  private String date;
  private String section;

  /**
   * Constructs a new {@link Articles} object.
   *
   * @param title is the title of the article.
   * @param author is the author of the article.
   * @param url is the website URL to find more details about the article.
   * @param date is the date the article was published.
   * @param section is the section the article belongs to.
   */
  public Articles(String title, String author, String url, String date, String section) {
    this.title = title;
    this.author = author;
    this.url = url;
    this.date = date;
    this.section = section;
  }

  // Return the title of the article
  public String getTitle() {
    return title;
  }

  // Set the title of the article
  public void setTitle(String title) {
    this.title = title;
  }

  // Return the author of the article
  public String getAuthor() {
    return author;
  }

  // Set the author of the article
  public void setAuthor(String author) {
    this.author = author;
  }

  // Return the website URL of the article
  public String getUrl() {
    return url;
  }

  // Return the date of the article
  public String getDate() {
    return date;
  }

  // Set the date of the article
  public void setDate(String date) {
    this.date = date;
  }

  // Return the section of the article
  public String getSection() {
    return section;
  }

  // Set the section of the article
  public void setSection(String section) {
    this.section = section;
  }

  @Override
  public String toString() {
    return "News{" +
        "title='" + title + '\'' +
        ", author='" + author + '\'' +
        ", url='" + url + '\'' +
        ", date='" + date + '\'' +
        ", section='" + section + '\'' +
        '}';
  }
}
