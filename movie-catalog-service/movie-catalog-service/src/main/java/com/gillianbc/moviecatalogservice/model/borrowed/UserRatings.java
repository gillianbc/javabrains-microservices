package com.gillianbc.moviecatalogservice.model.borrowed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor  //RestTemplate will use this in conjunction with the setters
public class UserRatings {
  private List<Rating> userRatings;
}
