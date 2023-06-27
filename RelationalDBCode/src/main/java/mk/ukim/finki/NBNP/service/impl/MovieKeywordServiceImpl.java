package mk.ukim.finki.NBNP.service.impl;

import java.util.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Keyword;
import mk.ukim.finki.NBNP.model.Movie;
import mk.ukim.finki.NBNP.model.MovieKeyword;
import mk.ukim.finki.NBNP.repository.KeywordRepository;
import mk.ukim.finki.NBNP.repository.MovieKeywordRepository;
import mk.ukim.finki.NBNP.repository.MovieRepository;
import mk.ukim.finki.NBNP.service.MovieKeywordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class MovieKeywordServiceImpl implements MovieKeywordService {

    private final MovieKeywordRepository movieKeywordRepository;
    private final MovieRepository movieRepository;
    private final KeywordRepository keywordRepository;

    public MovieKeywordServiceImpl(MovieKeywordRepository movieKeywordRepository, MovieRepository movieRepository, KeywordRepository keywordRepository) {
        this.movieKeywordRepository = movieKeywordRepository;
        this.movieRepository = movieRepository;
        this.keywordRepository = keywordRepository;
    }

    @Override
    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\keywords.csv"))) {
            List<String[]> rows = reader.readAll();


            for (int i = 1; i < rows.size(); i++) { // Start from 1 to skip the header row
                String[] values = rows.get(i);

                Movie movie;
                Long movieId = Long.parseLong(values[0]);

                if (movieRepository.findById(movieId).isPresent()){
                    movie = movieRepository.findById(movieId).get();
                }
                else{
                    continue;
                }

                String keywordsValue = values[1];
                if (!StringUtils.isEmpty(keywordsValue)) {
                    List<Keyword> keywords = new ArrayList<>();
                    // Parse the genres value and create Genre objects accordingly
                    // Example: "[{'id': 16, 'name': 'Animation'}, {'id': 35, 'name': 'Comedy'}, {'id': 10751, 'name': 'Family'}]"
                    JsonArray genresJson = JsonParser.parseString(keywordsValue).getAsJsonArray();
                    for (JsonElement genreElement : genresJson) {
                        JsonObject keywordsJson = genreElement.getAsJsonObject();
                        Long keywordId = keywordsJson.get("id").getAsLong();
                        String name = keywordsJson.get("name").getAsString();
                        Keyword keyword;
                        if (keywordRepository.findById(keywordId).isPresent()){
                            keyword = keywordRepository.findById(keywordId).get();
                        }
                        else {
                            keyword = new Keyword(keywordId, name);
                        }
                        MovieKeyword movieKeyword = new MovieKeyword(movie, keyword);
                        movieKeywordRepository.save(movieKeyword);
                    }
                }
            }

        } catch (CsvException | IOException e) {
            e.printStackTrace();
        }
    }
}