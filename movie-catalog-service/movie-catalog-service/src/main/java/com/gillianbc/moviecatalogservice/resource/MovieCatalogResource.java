package com.gillianbc.moviecatalogservice.resource;

import com.gillianbc.moviecatalogservice.model.CatalogItem;
import com.gillianbc.moviecatalogservice.model.borrowed.Movie;
import com.gillianbc.moviecatalogservice.model.borrowed.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
  @Autowired
  RestTemplate restTemplate;

  @RequestMapping("/{userId}")
  public List<CatalogItem> getCatalog(String userId){



    List<Rating> ratings = Arrays.asList(
        new Rating("1",4),
        new Rating("2",3)
    );

    System.out.println("GET CATALOG");
    return ratings.stream().map(rating -> {
      Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
      return new CatalogItem(movie.getName(),"desc", rating.getRating());
    }).collect(Collectors.toList());
  }
}
