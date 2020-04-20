package com.gillianbc.movieinfoservice.model;

public class Movie {
  private String movieId;
  private String name;

  public Movie(final String movieId, final String name) {
    this.movieId = movieId;
    this.name = name;
  }

  public String getMovieId() {
    return movieId;
  }

  public void setMovieId(final String movieId) {
    this.movieId = movieId;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Movie{" +
        "movieId='" + movieId + '\'' +
        ", name='" + name + '\'' +
        '}';
  }
}
