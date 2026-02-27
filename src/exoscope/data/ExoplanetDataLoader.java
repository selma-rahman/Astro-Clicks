package data;

import model.Exoplanet;
import java.io.*;
import java.util.*;

public class ExoplanetDataLoader {

    private String filePath;

    // for constructor
    public ExoplanetDataLoader(String filePath) {
        this.filePath = filePath;
    }

    // this is to load planets from CSV and returns list
    public List<Exoplanet> loadExoplanets() {

        List<Exoplanet> planets = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

        	for (int i = 0; i < 294; i++) {
        		br.readLine();
        	}
        	
            String line;

            // skip comment lines that start with #
            while ((line = br.readLine()) != null && line.startsWith("#")) {
                // skip
            }

            // if no header found
            if (line == null) {
                return planets;
            }

            // header row
            String[] headers = line.split(",");

            // map column name to index
            Map<String, Integer> colIndex = new HashMap<>();
            for (int i = 0; i < headers.length; i++) {
                colIndex.put(headers[i].trim(), i);
            }

            // this is to read data rows
            while ((line = br.readLine()) != null) {

                // split while handling quoted commas
                String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

                String name = fields[1];
                String hostStar = fields[2];
                Double radius = parseDouble(fields[43]);
                if (radius == null || radius == 0.0) continue;
                Double mass = parseDouble(fields[51]);
                if (mass == null || mass == 0.0) continue;
                Double orbitalPeriod = parseDouble(fields[35]);
                if (orbitalPeriod == null || orbitalPeriod == 0.0) continue;
                String discoveryMethod = fields[14];
                Double distance = parseDouble(fields[219]);
                if (distance == null || distance == 0.0) continue;
                Integer year = parseInt(fields[15]);
                if (year == null || year == 0.0) continue;

                Exoplanet planet = new Exoplanet(
                        name,
                        hostStar,
                        radius,
                        mass,
                        orbitalPeriod,
                        discoveryMethod,
                        distance,
                        year
                );

                planets.add(planet);
            }
            

        } catch (IOException e) {
            System.out.println("Error loading CSV: " + e.getMessage());
        }

        return planets;
    }

    // so we can safely get field by column name
    private String getField(String[] fields, Map<String, Integer> map, String key) {
        Integer index = map.get(key);
        if (index == null || index >= fields.length) {
            return null;
        }
        String value = fields[index].trim();
        return value.isEmpty() ? null : value;
    }

    // this wil safely parse double vals
    private Double parseDouble(String value) {
        try {
            if (value == null) return null;
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // this will safely parse intgerss
    private Integer parseInt(String value) {
        try {
            if (value == null) return null;
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}