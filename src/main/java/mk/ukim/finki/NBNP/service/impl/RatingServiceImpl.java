package mk.ukim.finki.NBNP.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import mk.ukim.finki.NBNP.model.Movie;
import mk.ukim.finki.NBNP.model.Rating;
import mk.ukim.finki.NBNP.model.Users;
import mk.ukim.finki.NBNP.repository.MovieRepository;
import mk.ukim.finki.NBNP.repository.RatingsRepository;
import mk.ukim.finki.NBNP.repository.UsersRepository;
import mk.ukim.finki.NBNP.service.RatingService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingsRepository ratingsRepository;
    private final MovieRepository movieRepository;
    private final UsersRepository usersRepository;

    public RatingServiceImpl(RatingsRepository ratingsRepository, MovieRepository movieRepository, UsersRepository usersRepository) {
        this.movieRepository = movieRepository;
        this.usersRepository = usersRepository;
        this.ratingsRepository = ratingsRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\ratings_small.csv"))) {
            List<String[]> rows = reader.readAll();

            rows.remove(0);

            processRowsInParallel(rows);

        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    public void processRowsInParallel(List<String[]> rows) {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (String[] row : rows) {
            executor.submit(() -> processRow(row));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Handle the interruption
        }
    }

    public void processRow(String[] csvRow) {
        Long userId = Long.parseLong(csvRow[0]);
        Long movieId = Long.parseLong(csvRow[1]);
        Float rating = Float.parseFloat(csvRow[2]);
        Integer timeStamp = Integer.parseInt(csvRow[3]);

        Movie movie = new Movie();
        Users user = new Users();

        if (movieRepository.findById(movieId).isPresent()) {
            movie = movieRepository.findById(movieId).get();
        }

        if (usersRepository.findById(userId).isPresent()) {
            user = usersRepository.findById(userId).get();
        }

        Rating ratingObj = new Rating(user, movie, rating, timeStamp);

        ratingsRepository.save(ratingObj);
    }

}
