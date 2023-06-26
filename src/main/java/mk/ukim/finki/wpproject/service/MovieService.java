package mk.ukim.finki.wpproject.service;

import java.util.*;

import mk.ukim.finki.wpproject.model.Movies;

public interface MoviesService {

    Optional<Movies> save (String title, String original_title, Float budget,
                           Boolean adult, String imdb_ID, String overview, Float popularity,
                           String poster_path, Date release_date, Float revenue, Integer runtime,
                           String status, String tagline, Integer vote_count, Float vote_average);

}
