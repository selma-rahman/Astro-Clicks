package model;

public class Exoplanet {
	
	private String name; // using private is standard OOP encapsulation
	private String hostStar;
	private Double radius;
	private Double mass;
	private Double  orbitalPeriod;
	private String discoveryMethod;
	private Double distance;
	private Integer year;
	
	public Exoplanet(String name, String hostStar, Double radius, Double mass, Double orbitalPeriod, String discoveryMethod, Double distance, Integer year) {
		this.name = name;
		this.hostStar = hostStar;
		this.radius = radius;
		this.mass = mass;
		this.orbitalPeriod = orbitalPeriod;
		this.discoveryMethod = discoveryMethod;
		this.distance = distance;
		this.year = year;
	}
	
	public String getName() {
		return name;
	}
	
	public String getHostStar() {
		return hostStar ;
	}
	
	public Double getRadius() {
		return radius ;
	}
	
	public Double getMass() {
		return mass;
	}
	
	public Double getOrbitalPeriod() {
		return orbitalPeriod;
	}
	
	public String getDiscoveryMethod() {
		return discoveryMethod;
	}

	public Double getDistance() {
		return distance ;
	}
	
	public Integer getYear() {
		return year;
	}
	
	@Override
	public String toString() {
		return "Name: " + name +
			   ", Host star: " + hostStar +
		       ", Year: " + year +
		       ", Discovery method: " + discoveryMethod +
		       ", Radius: " + radius +
		       ", Mass: " + mass +
		       ", Orbital period: " + orbitalPeriod;
	}
}

// using wrapper instead of primitive data types because if csv file has missing values (null), calling getRadius() or getYear() will throw a nullpointerexception  eroor