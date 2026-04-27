import javax.swing.SwingUtilities;
import java.util.*;
import logic.QueryEngine;
import model.Exoplanet;
import GUI.MainWindow;
import data.FilePath;

public class Launcher {

	public static void main(String[] args) {
		
			FilePath data = new FilePath();
			
			List<Exoplanet> planets = FilePath.getPlanets();
			
			
			QueryEngine queryEngine = new QueryEngine(planets);
	
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					MainWindow main = new MainWindow(planets);
					main.show();
				}
			;
		});
	}

}
 

 