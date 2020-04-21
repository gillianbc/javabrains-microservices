package com.gillianbc.moviecatalogservice.model.borrowed;

public class Rating {
  private String movieId;
  private int rating;

  public Rating(final String movieId, final int rating) {
    this.movieId = movieId;
    this.rating = rating;
  }

  public Rating() {
  }

  public String getMovieId() {
    return movieId;
  }

  public void setMovieId(final String movieId) {
    this.movieId = movieId;
  }

  public int getRating() {
    return rating;
  }

  public void setRating(final int rating) {
    this.rating = rating;
  }
}
