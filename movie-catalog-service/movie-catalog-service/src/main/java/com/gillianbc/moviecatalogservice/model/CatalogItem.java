package com.gillianbc.moviecatalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor  // No args constructor is needed for RestTemplate - it will then yse the setters
public class CatalogItem {
  private String name;
  private String desc;
  private int rating;

}
