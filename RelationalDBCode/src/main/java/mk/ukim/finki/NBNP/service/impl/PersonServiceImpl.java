package mk.ukim.finki.NBNP.service.impl;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;
import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.TimeUnit;
import mk.ukim.finki.NBNP.model.Person;
import mk.ukim.finki.NBNP.repository.PersonRepository;
import mk.ukim.finki.NBNP.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    public void migrateData() {

        try (CSVReader reader = new CSVReader(new FileReader("D:\\fakultet\\8 semestar\\NBNP\\NBNP_project\\data\\ALL_PEOPLE_CLEANED(for ppl)"))) {
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
        String name = values[1];
        Integer gender = Integer.parseInt(values[2]);
        Person person = new Person(personId, name, gender);
        personRepository.save(person);

    }
}

