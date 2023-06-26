package mk.ukim.finki.NBNP.service.impl;

import java.util.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import mk.ukim.finki.NBNP.model.Department;
import mk.ukim.finki.NBNP.model.Person;
import mk.ukim.finki.NBNP.model.PersonDepartment;
import mk.ukim.finki.NBNP.repository.DepartmentRepository;
import mk.ukim.finki.NBNP.repository.PersonDepartmentRepository;
import mk.ukim.finki.NBNP.repository.PersonRepository;
import mk.ukim.finki.NBNP.service.PersonDepartmentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;

@Service
public class PersonDepartmentServiceImpl implements PersonDepartmentService {

    private final PersonRepository personRepository;
    private final DepartmentRepository departmentRepository;
    private final PersonDepartmentRepository personDepartmentRepository;

    public PersonDepartmentServiceImpl(PersonRepository personRepository, DepartmentRepository departmentRepository, PersonDepartmentRepository personDepartmentRepository) {
        this.personRepository = personRepository;
        this.departmentRepository = departmentRepository;
        this.personDepartmentRepository = personDepartmentRepository;
    }

    public void migrateData() {

        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\ALL_PEOPLE_CLEANED (for depts)"))) {
            List<String[]> rows = reader.readAll();

            rows.remove(0);

            processRowsInParallel(rows);

            /*
            person_id,person_name,gender,department
            31,Tom Hanks,2,"{'departmentName': 'Acting', 'jobName': 'Actor'}"
             */


        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public void processRowsInParallel(List<String[]> rows) {
        int numThreads = Runtime.getRuntime().availableProcessors(); // Get the number of available processors

        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        for (String[] row : rows) {
            executor.submit(() -> processRow(row));
        }

        executor.shutdown();
        try {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            // Handle the interruption
        }
    }

    public void processRow(String[] values) {
        Long personId = Long.parseLong(values[0]);
        Person person = personRepository.findById(personId).orElse(null);
        String departmentValue = values[3];
        if (!StringUtils.isEmpty(departmentValue)) {
            if (person == null) {
                String name = values[1];
                Integer gender = Integer.parseInt(values[2]);

                person = new Person(personId, name, gender);
            }


            JsonObject departmentJson = JsonParser.parseString(departmentValue).getAsJsonObject();
            String depName = departmentJson.get("departmentName").getAsString();
            String jobName = departmentJson.get("jobName").getAsString();
            if (depName.equals("Acting")) {
                depName = "Actors";
            }
            Department department = departmentRepository.findByNameLike(depName);
            PersonDepartment personDepartment = new PersonDepartment(person, department, jobName);
            personDepartmentRepository.save(personDepartment);

        }
    }

}
