package mk.ukim.finki.NBNP.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Keyword;
import mk.ukim.finki.NBNP.repository.KeywordRepository;
import mk.ukim.finki.NBNP.service.KeywordsService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class KeywordsServiceImpl implements KeywordsService {

    private final KeywordRepository keywordRepository;

    public KeywordsServiceImpl(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\ALL_KEYWORDS"))) {
            List<String[]> csvData = reader.readAll();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Keyword keyword = convertToDataModel(row);
                // Save the data to the database using the repository
                keywordRepository.save(keyword);
            }
        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Keyword convertToDataModel(String[] csvRow) {

        String name = csvRow[1];
        Long id = Long.parseLong(csvRow[0]);
        return new Keyword(id, name);
    }

}
