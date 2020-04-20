package com.gillianbc.moviecatalogservice.model;

public class CatalogItem {
  private String name;
  private String desc;
  private int rating;

  public CatalogItem(final String name, final String desc, final int rating) {
    this.name = name;
    this.desc = desc;
    this.rating = rating;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getDesc() {
    return desc;
  }

  public void setDesc(final String desc) {
    this.desc = desc;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(final int rating) {
    this.rating = rating;
  }

  @Override
  public String toString() {
    return "CatalogItem{" +
        "name='" + name + '\'' +
        ", desc='" + desc + '\'' +
        ", rating=" + rating +
        '}';
  }
}
