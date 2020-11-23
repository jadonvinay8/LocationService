package com.capgemini.LocationService.utilities;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CSVConverter {

    public static List<String> csvToCities(InputStream stream) {

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
               CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {

            List<String> cities = new ArrayList<>();
            List<CSVRecord> records = csvParser.getRecords();

            records.forEach(record -> cities.add(record.get("CityName")));

            return cities;

        } catch (IOException e) {
            throw new RuntimeException("Can't parse CSV");
        }
    }

}
