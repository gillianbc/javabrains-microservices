package com.gillianbc.ratingsdataservice.resource;

import com.gillianbc.ratingsdataservice.model.Rating;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/ratings")
public class RatingService {

  @RequestMapping("/{movieId}")
  public Rating getMovieRating(@PathVariable("movieId") String movieId){
    return new Rating(movieId,4);
  }
}
