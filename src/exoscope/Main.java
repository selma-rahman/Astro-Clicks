import model.Exoplanet;
import UI.MainView;
import java.util.*;
import data.ExoplanetDataLoader;
import java.io.*;


public class Main {

    public static void main(String[] args) {
    
    	String primaryPath = "src/exoscope/data/exoplanets.csv";
        String fallbackPath = "data/exoplanets.csv";

        String fileToUse = null;

        File primaryFile = new File(primaryPath);
        File fallbackFile = new File(fallbackPath);

        if (primaryFile.exists()) {
            fileToUse = primaryPath;
        } else if (fallbackFile.exists()) {
            fileToUse = fallbackPath;
        } else {
            System.err.println("ERROR: Could not locate exoplanets.csv file.");
            System.err.println("Checked locations:");
            System.err.println(" - " + primaryPath);
            System.err.println(" - " + fallbackPath);
            System.exit(1);
        }

        ExoplanetDataLoader loader = new ExoplanetDataLoader(fileToUse);
        List<Exoplanet> planets = loader.loadExoplanets();

        MainView view = new MainView(planets);
        view.start();
    }
}