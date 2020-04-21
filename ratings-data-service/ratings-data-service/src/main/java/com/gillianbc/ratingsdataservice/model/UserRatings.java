package com.gillianbc.ratingsdataservice.model;

import java.util.List;

public class UserRatings {
  private List<Rating> userRatings;

  public UserRatings() {
  }

  public List<Rating> getUserRatings() {
    return userRatings;
  }

  public void setUserRatings(final List<Rating> userRatings) {
    this.userRatings = userRatings;
  }
}
