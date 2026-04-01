package data;
import model.Exoplanet;
import java.util.*;
import java.io.*;


public class FilePath {
	
	public static List<Exoplanet> getPlanets(){

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
    
    return(planets);

 //   MainView view = new MainView(planets);
 //   view.start();
        
    
    }
}

// CALLING IT A NIGHT... SET LAUNCHER AS NEW PRIMARY MAIN??? PRAY THIS WONT FUCK EVERYTHING UP. L O L. 
// THEN ACCESS QUERYENGINE CODE FROM MAIN WINDOW