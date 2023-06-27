package mk.ukim.finki.NBNP.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class CsvFileReader {

    private final String filePath;
    private String regex;

    public CsvFileReader(String filePath, String regex) {
        this.filePath = filePath;
        this.regex = regex;
    }

    public List<String[]> readData() throws IOException {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(regex);
                data.add(values);
            }
        }

        return data;
    }
}
