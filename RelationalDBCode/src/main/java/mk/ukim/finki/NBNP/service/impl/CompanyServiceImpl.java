package mk.ukim.finki.NBNP.service.impl;

import mk.ukim.finki.NBNP.model.Company;
import mk.ukim.finki.NBNP.repository.CompanyRepository;
import mk.ukim.finki.NBNP.service.CompanyService;
import mk.ukim.finki.NBNP.service.DataConvertor;
import mk.ukim.finki.NBNP.utility.CsvFileReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CsvFileReader csvFileReader;
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.csvFileReader = new CsvFileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_COMPANIES", ",");
        this.companyRepository = companyRepository;
    }

    public void migrateData() {
        try {
            List<String[]> csvData = csvFileReader.readData();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Company company = convertToDataModel(row);
                // Save the data to the database using the repository
                companyRepository.save(company);
            }
        } catch (IOException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Company convertToDataModel(String[] csvRow) {

        String name = DataConvertor.convert(csvRow);
        Long id = Long.parseLong(csvRow[csvRow.length-1]);
        return new Company(id, name);
    }

}
