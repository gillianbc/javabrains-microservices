package com.gillianbc.moviecatalogservice.resource;

import com.gillianbc.moviecatalogservice.model.Catalog;
import com.gillianbc.moviecatalogservice.model.CatalogItem;
import com.gillianbc.moviecatalogservice.model.borrowed.Movie;
import com.gillianbc.moviecatalogservice.model.borrowed.Rating;
import com.gillianbc.moviecatalogservice.model.borrowed.UserRatings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
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
  public Catalog getCatalog(@PathVariable("userId") String userId){
    System.out.println("CATALOG: GET CATALOG");
    UserRatings ratings = restTemplate.getForObject("http://localhost:8083/ratings/users/" + userId, UserRatings.class);

    final List<CatalogItem> catalogItemList = ratings.getUserRatings().stream().map(rating -> {
      Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
      return new CatalogItem(movie.getName(), "desc", rating.getRating());
    }).collect(Collectors.toList());

    Catalog catalog = new Catalog();
    catalog.setCatalogItemList(catalogItemList);
    catalog.setUserId(userId);
    return catalog;
  }
}
