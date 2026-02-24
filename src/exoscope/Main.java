import model.Exoplanet;
import UI.MainView;
import java.util.*;
import data.ExoplanetDataLoader;
import java.io.*;
import data.API_Consumer;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
    	try {
            // 1) Build the Exoplanet Archive TAP query (all confirmed planets table: ps)
            String base = "https://exoplanetarchive.ipac.caltech.edu/TAP/sync";
            String adql = "select * from ps";   // all rows, all columns
            String format = "json";

            String url = base
                    + "?query=" + URLEncoder.encode(adql, StandardCharsets.UTF_8)
                    + "&format=" + URLEncoder.encode(format, StandardCharsets.UTF_8);
            
         // 2) Get JSON
            API_Consumer consumer = new API_Consumer();
            String jsonResponse = consumer.getAPIData(url);
//            System.out.println(jsonResponse);
    	} catch (Exception e) {
            e.printStackTrace();
        }
    	
//		We are working on API right now
//    	Commenting the line below as it will be necessary later on
    	
//    	String primaryPath = "src/exoscope/data/exoplanets.csv";
//        String fallbackPath = "data/exoplanets.csv";
//
//        String fileToUse = null;
//
//        File primaryFile = new File(primaryPath);
//        File fallbackFile = new File(fallbackPath);
//
//        if (primaryFile.exists()) {
//            fileToUse = primaryPath;
//        } else if (fallbackFile.exists()) {
//            fileToUse = fallbackPath;
//        } else {
//            System.err.println("ERROR: Could not locate exoplanets.csv file.");
//            System.err.println("Checked locations:");
//            System.err.println(" - " + primaryPath);
//            System.err.println(" - " + fallbackPath);
//            System.exit(1);
//        }
//
//        ExoplanetDataLoader loader = new ExoplanetDataLoader(fileToUse);
//        List<Exoplanet> planets = loader.loadExoplanets();

//        MainView view = new MainView(planets);
//        view.start();
    }
}
