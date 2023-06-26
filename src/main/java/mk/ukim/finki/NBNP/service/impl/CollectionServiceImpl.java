package mk.ukim.finki.NBNP.service.impl;

import java.io.IOException;
import java.util.*;

import mk.ukim.finki.NBNP.model.Collection;
import mk.ukim.finki.NBNP.repository.CollectionRepository;
import mk.ukim.finki.NBNP.service.CollectionService;
import mk.ukim.finki.NBNP.utility.CsvFileReader;
import org.springframework.stereotype.Service;

@Service
public class CollectionServiceImpl implements CollectionService {

    private final CsvFileReader csvFileReader;
    private final CollectionRepository collectionRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository) {
        this.csvFileReader = new CsvFileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_COLLECTIONS", ",");
        this.collectionRepository = collectionRepository;
    }

    public void migrateData() {
        try {
            List<String[]> csvData = csvFileReader.readData();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Collection collection = convertToDataModel(row);
                // Save the data to the database using the repository
                collectionRepository.save(collection);
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Collection convertToDataModel(String[] csvRow) {

        String name = csvRow[1];
        Long id = Long.parseLong(csvRow[0]);
        return new Collection(id, name);
    }
}
