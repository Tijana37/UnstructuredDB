package mk.ukim.finki.NBNP.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import mk.ukim.finki.NBNP.model.Credits;
import mk.ukim.finki.NBNP.model.Movie;
import mk.ukim.finki.NBNP.model.Person;
import mk.ukim.finki.NBNP.repository.CreditsRepository;
import mk.ukim.finki.NBNP.repository.MovieRepository;
import mk.ukim.finki.NBNP.repository.PersonRepository;
import mk.ukim.finki.NBNP.service.CreditsService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Async
public class CreditsServiceImpl implements CreditsService {

    private final CreditsRepository creditsRepository;
    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;

    public CreditsServiceImpl(CreditsRepository creditsRepository, MovieRepository movieRepository, PersonRepository personRepository) {
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.creditsRepository = creditsRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\ALL_CREDITS"))) {
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
        Long movieId = Long.parseLong(csvRow[0]);
        Long personId = Long.parseLong(csvRow[1]);
        Movie movie = new Movie();
        Person person = new Person();
        if (movieRepository.findById(movieId).isPresent()) {
            movie = movieRepository.findById(movieId).get();
        }
        if (personRepository.findById(personId).isPresent()) {
            person = personRepository.findById(personId).get();
        }
        String character_name = "EMPTY";
        if (csvRow.length >= 3) {
            character_name = csvRow[2];
        }

        Credits credits = new Credits();
        credits.setMovie(movie);
        credits.setPerson(person);
        credits.setCharacterName(character_name);
        // Save the data to the database using the repository
        creditsRepository.save(credits);
    }

}

