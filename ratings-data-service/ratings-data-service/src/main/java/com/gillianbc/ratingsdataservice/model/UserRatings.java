package com.gillianbc.ratingsdataservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRatings {
  private List<Rating> userRatings;

  public List<Rating> getUserRatings() {
    return userRatings;
  }

}
