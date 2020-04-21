package com.gillianbc.moviecatalogservice.model;

import java.util.List;

public class Catalog {
  private String userId;
  List<CatalogItem> catalogItemList;

  public String getUserId() {
    return userId;
  }

  public void setUserId(final String userId) {
    this.userId = userId;
  }

  public Catalog() {
  }

  public List<CatalogItem> getCatalogItemList() {
    return catalogItemList;
  }

  public void setCatalogItemList(final List<CatalogItem> catalogItemList) {
    this.catalogItemList = catalogItemList;
  }
}
