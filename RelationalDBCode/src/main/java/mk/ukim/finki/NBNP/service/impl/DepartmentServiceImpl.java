package mk.ukim.finki.NBNP.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mk.ukim.finki.NBNP.model.Department;
import mk.ukim.finki.NBNP.repository.DepartmentRepository;
import mk.ukim.finki.NBNP.service.DepartmentService;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public void migrateData() {
        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\departments"))) {
            List<String[]> csvData = reader.readAll();
            for (int i=1; i<csvData.size(); i++) {
                String[] row = csvData.get(i);
                // Convert the CSV data into your data model objects
                Department department = convertToDataModel(row);
                // Save the data to the database using the repository
                departmentRepository.save(department);
            }
        } catch (IOException | CsvException e) {
            // Handle any exceptions that occur during CSV file reading
            e.printStackTrace();
        }
    }

    private Department convertToDataModel(String[] csvRow) {
        if (csvRow[0].equals("Acting")){
            csvRow[0] = "Actors";
        }
        String depName = csvRow[0];
        Department department = departmentRepository.findByNameLike(depName);
        if (department == null){
            department = new Department(depName);
        }

        return department;
    }
}
