package mk.ukim.finki.NBNP.service.impl;

import mk.ukim.finki.NBNP.model.Country;
import mk.ukim.finki.NBNP.repository.CountryRepository;
import mk.ukim.finki.NBNP.service.CountryService;
import mk.ukim.finki.NBNP.service.DataConvertor;
import mk.ukim.finki.NBNP.utility.CsvFileReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CountryServiceImpl implements CountryService {

    private final CsvFileReader csvFileReader;
    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.csvFileReader = new CsvFileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_COUNTRIES", ",");
        this.countryRepository = countryRepository;
    }

    public void migrateData() {
        try {
            List<String[]> csvData = csvFileReader.readData();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Country country = convertToDataModel(row);
                // Save the data to the database using the repository
                countryRepository.save(country);
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Country convertToDataModel(String[] csvRow) {

        String isoCode = DataConvertor.convert(csvRow);
        String name = csvRow[csvRow.length-1];
        return new Country(isoCode, name);
    }

}
