package com.gillianbc.movieinfoservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class Movie {
  private String movieId;
  private String name;
  private String desc;

}
