package com.gillianbc.movieinfoservice.resource;

import com.gillianbc.movieinfoservice.model.Movie;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movies")
public class MovieService {

    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){
        if ("1".equals(movieId))
            return new Movie(movieId, "Fried Green Tomatoes at the Whistlestop Cafe");
        if ("2".equals(movieId))
            return new Movie(movieId, "Shawshank Redemption");

        return new Movie(movieId, "Star Wars");
    }
}