package mk.ukim.finki.wpproject.service.impl;

import mk.ukim.finki.wpproject.model.Movies;
import mk.ukim.finki.wpproject.repository.MovieRepository;
import mk.ukim.finki.wpproject.service.MoviesService;

import java.util.Date;
import java.util.Optional;

public class MoviesServiceImpl implements MoviesService {

    private final MovieRepository movieRepository;

    public MoviesServiceImpl(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Override
    public Optional<Movies> save(String title, String original_title, Float budget, Boolean adult, String imdb_ID,
                                 String overview, Float popularity, String poster_path, Date release_date, Float revenue,
                                 Integer runtime, String status, String tagline, Integer vote_count, Float vote_average) {
        return Optional.empty();
    }
}
