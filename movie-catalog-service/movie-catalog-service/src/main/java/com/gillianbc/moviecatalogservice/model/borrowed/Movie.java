package com.gillianbc.moviecatalogservice.model.borrowed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor  //RestTemplate will use this in conjunction with the setters
public class Movie {
  private String movieId;
  private String name;
  private String desc;

}
