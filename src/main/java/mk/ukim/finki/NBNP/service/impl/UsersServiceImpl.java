package mk.ukim.finki.NBNP.service.impl;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Users;
import mk.ukim.finki.NBNP.repository.UsersRepository;
import mk.ukim.finki.NBNP.service.UsersService;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Service
public class UsersServiceImpl implements UsersService {

    private final UsersRepository usersRepository;

    public UsersServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\FIXED_USERS"))) {
            List<String[]> csvData = reader.readAll();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Users user = convertToDataModel(row);
                // Save the data to the database using the repository
                usersRepository.save(user);
            }
        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Users convertToDataModel(String[] csvRow) {

        String name = csvRow[1];
        Long id = Long.parseLong(csvRow[0]);
        return new Users(id, name);
    }

}
