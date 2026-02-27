package logic;
import model.Exoplanet;
import java.util.ArrayList;
import java.util.*;

@SuppressWarnings("unused")
public class QueryEngine {
	
	List<Exoplanet> planets;
	private List<Exoplanet> results = new ArrayList<>();
	
	
	// this is a constructor to takes a list of planets
    public QueryEngine(List<Exoplanet> p) {
        // to record a copy so original list does not change
        this.planets = p;
    }
	
	public List<Exoplanet> filterByRadius(double min, double max) {
		List<Exoplanet> results = new ArrayList<>(); // bug fix
		for (Exoplanet planet : planets) {
			if (planet.getRadius() >= min && planet.getRadius() <= max) {
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByMass(double min, double max) {
		List<Exoplanet> results = new ArrayList<>(); // bug fix
		for (Exoplanet planet : planets) {
			if (planet.getMass() >= min && planet.getMass() <= max) {
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByHostStar(String star) {
		List<Exoplanet> results = new ArrayList<>(); //bug fix
		for (Exoplanet planet : planets) {
			if (planet.getHostStar().toLowerCase().contains(star.toLowerCase())){
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByOrbitalPeriod(double min, double max) {
		 List<Exoplanet> results = new ArrayList<>(); // bug fixx
		for (Exoplanet planet: planets) {
			if (planet.getOrbitalPeriod() >= min && planet.getOrbitalPeriod() <= max) {
				results.add(planet);
			}
		}
		return results;
		
	}
	
	public List<Exoplanet> filterByYear(int year) {
		for (Exoplanet planet:planets) {
			if (planet.getYear() == year) {
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByDiscoveryMethod(String dm) {
		List<Exoplanet> results = new ArrayList<>(); //bug fix
		for (Exoplanet planet : planets) {
			if (planet.getDiscoveryMethod().equals(dm)) {
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByDistance(double min, double max) {
		List<Exoplanet> results = new ArrayList<>(); //bug fix
		for (Exoplanet planet: planets) {
			if (planet.getDistance() >= min && planet.getDistance() <= max) {
				results.add(planet);
			}
		}
		return results;
	}
	
	public List<Exoplanet> filterByName(String name) {
		for (Exoplanet planet : planets) {
			if (planet.getName().toLowerCase().contains(name.toLowerCase())) {
				results.add(planet);
			}
		}
		return results;
	}
	
	

}
