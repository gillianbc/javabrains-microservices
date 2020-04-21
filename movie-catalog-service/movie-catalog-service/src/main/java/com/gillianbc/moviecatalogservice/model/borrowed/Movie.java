package com.gillianbc.moviecatalogservice.model.borrowed;

public class Movie {
  private String movieId;
  private String name;

  // No args constructor is needed for RestTemplate - it will then yse the setters
  public Movie(){
    super();
  }
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
