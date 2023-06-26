package mk.ukim.finki.NBNP.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Genre;
import mk.ukim.finki.NBNP.repository.GenreRepository;
import mk.ukim.finki.NBNP.service.DataConvertor;
import mk.ukim.finki.NBNP.service.GenreService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_GENRES"))) {
            List<String[]> csvData = reader.readAll();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Genre genre = convertToDataModel(row);
                // Save the data to the database using the repository
                genreRepository.save(genre);
            }
        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Genre convertToDataModel(String[] csvRow) {

        String name = DataConvertor.convert(csvRow);
        Long id = Long.parseLong(csvRow[csvRow.length-1]);
        return new Genre(id, name);
    }

}
