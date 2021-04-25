package com.gillianbc.moviecatalogservice.model.borrowed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
  private String movieId;
  private int rating;

}
