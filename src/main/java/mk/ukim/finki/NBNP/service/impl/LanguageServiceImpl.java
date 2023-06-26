package mk.ukim.finki.NBNP.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Language;
import mk.ukim.finki.NBNP.repository.LanguageRepository;
import mk.ukim.finki.NBNP.service.LanguageService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_LANGS"))) {
            List<String[]> csvData = reader.readAll();
            for (int i=1; i<csvData.size(); i++) {
                String[] csvRow = csvData.get(i);
                // Convert the CSV data into your data model objects
                String isoLangCode = csvRow[0];
                String name = "";
                if (csvRow.length > 1){
                    name = csvRow[1];
                }
                Language language = new Language(name, isoLangCode);
                // Save the data to the database using the repository
                languageRepository.save(language);
            }
        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

}
