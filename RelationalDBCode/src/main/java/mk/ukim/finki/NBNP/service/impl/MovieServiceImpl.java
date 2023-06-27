package mk.ukim.finki.NBNP.service.impl;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import mk.ukim.finki.NBNP.model.*;
import mk.ukim.finki.NBNP.model.Collection;
import mk.ukim.finki.NBNP.repository.CountryRepository;
import mk.ukim.finki.NBNP.repository.LanguageRepository;
import mk.ukim.finki.NBNP.repository.MovieRepository;
import mk.ukim.finki.NBNP.service.MovieService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final LanguageRepository languageRepository;
    private final CountryRepository countryRepository;

    public MovieServiceImpl(MovieRepository movieRepository, LanguageRepository languageRepository, CountryRepository countryRepository) {
        this.movieRepository = movieRepository;
        this.languageRepository = languageRepository;
        this.countryRepository = countryRepository;
    }


    @Override
    public void migrateData() {

        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\movies_metadata.csv"))) {
            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) { // Start from 1 to skip the header row
                Movie movie = new Movie();

                String[] values = rows.get(i);
                String collectionValue = values[1];
                if (!StringUtils.isEmpty(collectionValue)) {
                    Collection collection = new Collection();
                    JsonObject collectionJson = JsonParser.parseString(collectionValue).getAsJsonObject();
                    collection.setId(collectionJson.get("id").getAsLong());
                    collection.setName(collectionJson.get("name").getAsString());
                    movie.setCollection(collection);
                }

                String genresValue = values[3];
                if (!StringUtils.isEmpty(genresValue)) {
                    List<Genre> genres = new ArrayList<>();
                    // Parse the genres value and create Genre objects accordingly
                    // Example: "[{'id': 16, 'name': 'Animation'}, {'id': 35, 'name': 'Comedy'}, {'id': 10751, 'name': 'Family'}]"
                    JsonArray genresJson = JsonParser.parseString(genresValue).getAsJsonArray();
                    for (JsonElement genreElement : genresJson) {
                        JsonObject genreJson = genreElement.getAsJsonObject();
                        Genre genre = new Genre();
                        genre.setId(genreJson.get("id").getAsLong());
                        genre.setName(genreJson.get("name").getAsString());
                        genres.add(genre);
                    }
                    movie.setGenres(genres);
                }

                String originalLanguageValue = values[7];
                if (!StringUtils.isEmpty(originalLanguageValue)) {
                    Language originalLanguage = languageRepository.findByIso_lang_code(originalLanguageValue);

                    movie.setLanguage(originalLanguage);
                }


                String productionCompaniesValue = values[12];
                if (!StringUtils.isEmpty(productionCompaniesValue)) {
                    List<Company> productionCompanies = new ArrayList<>();
                    // Parse the production_companies value and create Company objects accordingly
                    // Example: "[{'name': 'Pixar Animation Studios', 'id': 3}]"
                    JsonArray productionCompaniesJson = JsonParser.parseString(productionCompaniesValue).getAsJsonArray();
                    for (JsonElement companyElement : productionCompaniesJson) {
                        JsonObject companyJson = companyElement.getAsJsonObject();
                        Company company = new Company();
                        company.setId(companyJson.get("id").getAsLong());
                        company.setName(companyJson.get("name").getAsString());
                        productionCompanies.add(company);
                    }
                    movie.setProductionCompanies(productionCompanies);
                }


                String productionCountriesValue = values[13];
                if (!StringUtils.isEmpty(productionCountriesValue)) {
                    List<Country> productionCountries = new ArrayList<>();
                    // Parse the production_countries value and create Country objects accordingly
                    // Example: "[{'iso_3166_1': 'US', 'name': 'United States of America'}]"
                    JsonArray productionCountriesJson = JsonParser.parseString(productionCountriesValue).getAsJsonArray();
                    for (JsonElement countryElement : productionCountriesJson) {
                        JsonObject countryJson = countryElement.getAsJsonObject();
                        String iso = countryJson.get("iso_3166_1").getAsString();
                        Country country = countryRepository.findByIso_country_code(iso);
                        productionCountries.add(country);
                    }
                    movie.setProductionCountries(productionCountries);
                }


                String spokenLanguagesValue = values[17];
                if (!StringUtils.isEmpty(spokenLanguagesValue)) {
                    List<Language> spokenLanguages = new ArrayList<>();
                    // Parse the spoken_languages value and create Language objects accordingly
                    // Example: "[{'iso_639_1': 'en', 'name': 'English'}]"
                    JsonArray spokenLanguagesJson = JsonParser.parseString(spokenLanguagesValue).getAsJsonArray();
                    for (JsonElement languageElement : spokenLanguagesJson) {
                        JsonObject languageJson = languageElement.getAsJsonObject();
                        String languageIso = languageJson.get("iso_639_1").getAsString();
                        Language language = languageRepository.findByIso_lang_code(languageIso);
                        if (language == null || languageIso.equals("sh") || languageIso.equals("xx")) {
                            // Language not found in the database, create a new Language object
                            continue;
                        }
                        spokenLanguages.add(language);
                    }
                    movie.setSpokenLanguages(spokenLanguages);
                }


                // Create a new YourEntity object and set its attributes based on the CSV values
                movie.setAdult(parseBoolean(values[0]));
                movie.setBudget(floatParse(values[2]));
                movie.setId(Long.parseLong(values[5]));
                movie.setImdb_ID(values[6]);
                movie.setOriginal_title(values[8]);
                movie.setOverview(values[9]);
                movie.setPopularity(floatParse(values[10]));
                movie.setPoster_path(values[11]);
                //1995-10-30


                movie.setRelease_date(dateParse(values[14]));
                movie.setRevenue(floatParse(values[15]));
                movie.setRuntime(floatParse(values[16]));
                movie.setStatus(values[18]);
                movie.setTagline(values[19]);
                movie.setTitle(values[20]);
                movie.setVote_average(floatParse(values[22]));
                movie.setVote_count(integerParse(values[23]));

                //movies.add(movie);
                movieRepository.save(movie);
            }

            //movieRepository.saveAll(movies);
        } catch (IOException e) {
            // Handle IOException
        } catch (CsvException e) {
            // Handle CsvException if there is an issue with the CSV file
        } catch (NumberFormatException e) {
            // Handle NumberFormatException if the CSV values are not in the expected format
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static Float floatParse(String val){
        Float res = null;
        if (!val.isEmpty()){
            res = Float.parseFloat(val);
        }
        return res;
    }

    private static Integer integerParse(String val){
        Integer res = null;
        if (!val.isEmpty()){
            res = Integer.parseInt(val);
        }
        return res;
    }

    private static Boolean parseBoolean(String val){
        Boolean res = null;
        if (!val.isEmpty()){
            res = Boolean.parseBoolean(val);
        }
        return res;
    }

    private static Date dateParse (String val) throws ParseException {
        Date res = null;
        if (!val.isEmpty()) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM", Locale.ENGLISH);
            res = formatter.parse(val);
        }
        return res;
    }

}

